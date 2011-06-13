

package bootstrap.liftweb

import net.liftweb._
import common.{Empty, Failure, Full}
import http._
import sitemap.{SiteMap, Menu, Loc}
import util.{ NamedPF }
import _root_.net.liftweb.sitemap.Loc._
import net.liftweb._
import mapper.{Schemifier, DB, StandardDBVendor, DefaultConnectionIdentifier}
import util.{Props}
import com.yorrick.model._
import com.yorrick.view.{FormsTestView, TasksView, RssView}
import com.yorrick.snippet._
import requestvars.currentTask

class Boot {
  def boot {
  
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = 
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
        			               Props.get("db.url") openOr 
        			               "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
        			               Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User, Account)
  
    // where to search snippet
    LiftRules.addToPackages("com.yorrick")

    // url rewriting
    LiftRules.statelessRewrite.append({
      case RewriteRequest(ParsePath(List("account", accountName), _, _, _), _, _) =>
         RewriteResponse("viewAcct" :: Nil, Map("accountName" -> accountName))


      case RewriteRequest(ParsePath(list @ List("tasks", "edition", _*), _, _, _), _, _) =>
        //println("request rewriting" + list)
        RewriteResponse("tasks-management" :: "edit" :: Nil)

//      case RewriteRequest(ParsePath(List("tasks", "edition", taskId), _, _, _), _, _) =>
//        try {
//          val id = taskId.toInt
//          currentTask(Full(Task.getTask(id)))
//        } catch {
//          case e : NumberFormatException => currentTask(Failure("Task id must be a number"))
//          case e => currentTask(Failure("Error : " + e.getMessage))
//        }
//        RewriteResponse("tasks-management" :: "edit" :: Nil)

      case RewriteRequest(ParsePath(List("tasks", taskImportance), _, _, _), _, _) =>
         RewriteResponse("tasks-management" :: "list" :: Nil, Map("taskImportance" -> taskImportance))
    })

    // Custom dispatch for image generation
    LiftRules.dispatch.append {
      case Req(List("tasks", "image", taskId), _, _) =>
        () => Image.viewImage(taskId)
    }

    // build sitemap
    val entries = List(Menu("Home") / "index") :::
                  List(Menu(Loc("Static", Link(List("static"), true, "/static/index"), "Static Content"))) :::
                  List(Menu(Loc("Test",   Link(List("test-templates"), true, "/test-templates/test"), "Test template"))) :::
                  // vues statiques
                  List(Menu(Loc("RssView", ("site" :: Nil) -> true,                "RssViewMenuLabel", Hidden )) ) :::
                  List(Menu(Loc("Expense", ("expense" :: "recent" :: Nil) -> true, "RssViewMenuLabel", Hidden )) ) :::
                  List(Menu(Loc("TestForms", ("forms" :: Nil) -> true, "Test des formulaires"))) :::
                  // vues dynamiques
                  List(Menu(Loc("ExpenseDynamicView", ("ExpenseView" :: Nil) -> true, "ExpenseViewMenuLabel", Hidden )) ) :::
                  List(Menu(Loc("ExpenseDynamicInsecureView", ("ExpenseInsecureView" :: Nil) -> true, "ExpenseInsecureViewMenuLabel", Hidden )) ) :::
                  // taches
                  //List(Menu(Loc("TaskView", ("viewTasks" :: Nil) -> true, "List of tasks"))) :::
                  List(Menu(Loc("Tasks", Link(List("tasks-management"), true, "/tasks/"), "List of tasks"))) :::
                  //List(Menu(Loc("TaskEdition", Link(List("tasks-management", "edit"), true, "/tasks/edition"), "Edit a task", Hidden))) :::
                  // the User management menu items
                  User.sitemap :::
                  Nil

    LiftRules.uriNotFound.prepend(NamedPF("404handler"){
      case (req,failure) => NotFoundAsTemplate(
        ParsePath(List("exceptions","404"),"html",false,false))
    })
    
    LiftRules.setSiteMap(SiteMap(entries:_*))
    
    // set character encoding
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))
    
    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)
    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)


    // view dispatching
    LiftRules.viewDispatch.append {
      // This is an explicit dispatch to a particular method based on the path
      case List("expense", "recent", acctId, authToken) =>
        Left(() => Full(RssView.recent(acctId, authToken)))

      // This is a dispatch via the same LiftView object. The path
      // "/Site/news" will match this dispatch because of our dispatch
      // method defined in RSSView. The path "/Site/stuff/news" will not
      // match because the dispatch will be attempted on List("Site","stuff")
      case List("site") => Right(RssView)

      // list of tasks
      //case "tasks-management" :: "list" :: Nil => {println("vue liste des taches appelée"); Left(() => Full(TasksView.list))}
      case "tasks-management" :: Nil => {println("vue des taches appelée"); Right(TasksView)}

      // test des formulaires
      case "forms" :: Nil => Right(FormsTestView)
    }

    // snippet dispatching
    LiftRules.snippetDispatch.append {
      case "StaticDispatchSnippet" => StaticDispatchSnippet
      case "Ledger"                => LedgerSnippet
      //case "Tasks"                 => TasksSnippet

      case "Tasks"                 => S.snippetForClass("TasksSnippet") openOr {
        println("creating new snippet for tasks")
        val instance = new TasksSnippet
        instance.addName("TasksSnippet")
        S.overrideSnippetForClass("TasksSnippet", instance)
        instance
      }

      // S.snippetForClass checks to see if an instance has already
      // registered. This is the case after form submission or when
      // we use the StatefulSnippet.link or .redirectTo methods
      case "BridgeKeeper" => S.snippetForClass("BridgeKeeperSnippet") openOr {
        // If we haven’t already registered an instance, create one
        println("creating new snippet for bridge keeper")
        val inst = new BridgeKeeper
        // The name is what Lift uses to locate an instance (S.snippetForClass)
        // We need to add it so that the Stateful callback functions can
        // self-register
        inst.addName("BridgeKeeperSnippet")
        // Register this instance for the duration of the request
        S.overrideSnippetForClass("BridgeKeeperSnippet", inst)
        inst
      }
    }

    LiftRules.snippets.append {
      // other method by default
      case "StaticDispatchSnippet" :: Nil => StaticDispatchSnippet.render("no name") _
      case "StaticDispatchSnippet" :: name :: Nil => StaticDispatchSnippet.render(name) _
    }
  }

}
