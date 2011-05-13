

package bootstrap.liftweb

import net.liftweb._
import http._
import sitemap.{SiteMap, Menu, Loc}
import util.{ NamedPF }
import _root_.net.liftweb.sitemap.Loc._
import net.liftweb._
import mapper.{Schemifier, DB, StandardDBVendor, DefaultConnectionIdentifier}
import util.{Props}
import common.{Full}
import com.yorrick.model._
import com.yorrick.view.RssView


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

    // build sitemap
    val entries = List(Menu("Home") / "index") :::
                  List(Menu(Loc("Static", Link(List("static"), true, "/static/index"), "Static Content"))) :::
                  List(Menu(Loc("Test",   Link(List("test-templates"), true, "/test-templates/test"), "Test template"))) :::
                  List(Menu(Loc("RssView", ("site" :: Nil) -> true,                "RssView", Hidden )) ) :::
                  List(Menu(Loc("Expense", ("expense" :: "recent" :: Nil) -> true, "RssView", Hidden )) ) :::
                  List(Menu(Loc("ExpenseDynamicView", ("ExpenseView" :: Nil) -> true, "ExpenseView", Hidden )) ) :::
                  List(Menu(Loc("ExpenseDynamicInsecureView", ("ExpenseInsecureView" :: Nil) -> true, "ExpenseInsecureView", Hidden )) ) :::
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

    LiftRules.statelessRewrite.append({
      case RewriteRequest(ParsePath(List("account", accountName), _, _, _), _, _) =>
         RewriteResponse("viewAcct" :: Nil, Map("accountName" -> accountName))
    })

    // view dispatching
    LiftRules.viewDispatch.append {
      // This is an explicit dispatch to a particular method based on the path
      case List("expenses", "recent", acctId, authToken) =>
        Left(() => Full(RssView.recent(acctId, authToken)))

      // This is a dispatch via the same LiftView object. The path
      // "/Site/news" will match this dispatch because of our dispatch
      // method defined in RSSView. The path "/Site/stuff/news" will not
      // match because the dispatch will be attempted on List("Site","stuff")
      case List("site") => Right(RssView)
    }
  }

}
