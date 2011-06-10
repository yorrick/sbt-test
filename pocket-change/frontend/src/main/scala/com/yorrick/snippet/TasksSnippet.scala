

package com.yorrick {
package snippet {

import requestvars.taskImportance
import net.liftweb.util.BindHelpers._
import xml.NodeSeq._
import net.liftweb.common.{Empty, Full, Box}
import java.awt.Color
import net.liftweb.http._
import model.{Image, Task, TaskImportance}
import xml.{Null, Attribute, Text, NodeSeq}

object currentTask extends RequestVar[Box[Task]](Empty)

object TasksSnippet extends DispatchSnippet {

  def dispatch : DispatchIt = {
//    case "list" => listOfSnippets(taskImportance.is) _
    case "listEditorFriendly" => listEditorFriendly _
    case "viewTask" => viewTask _
    case "editTask" => editTask _
  }

  private def listOfSnippets(importance: TaskImportance.Value) (content : NodeSeq) : NodeSeq = {
    Task.getTasks(importance).flatMap(task =>
      bind("tasks", content,
           "label" -> Text(task.label),
           "description" -> Text(task.detail)
      )
    )
  }

  private def listEditorFriendly(content : NodeSeq) : NodeSeq = {
    val result = Task.getTasks.flatMap(task =>
      (
        "#label *" #> task.label &
        ".description *+" #> task.detail
      ).apply(content)
    )

    result
  }

  private def viewTask(content : NodeSeq) : NodeSeq = {
    val result = Task.getTasks(taskImportance.is).flatMap(task => {
        val redirectPath = "/tasks/edition/"

        // callback du lien
        def linkCallback = {
          currentTask(Full(task))
        }

        val imageTag = task.image match {
          case Some(image) =>
            val imageSource = "/tasks/image/" + task.id
            <img id="image" alt="Image de test"/> % Attribute(None, "src", Text(imageSource), Null)
          case None =>
            <span>Pas d'image pour cette tâche</span>
        }

        (
          "#label *"        #> (task.label + "(" + task.id + ", " + task.importance.toString + ")") &
          ".description *+" #> task.detail &
          "#image"          #> imageTag &
          "#editLink"       #> SHtml.link(redirectPath, linkCallback _, content \\ "a")
        ).apply(content)
      }
    )

    result
  }


  private def editTask(content : NodeSeq) : NodeSeq = {
    val task = currentTask.get.get
    var label = task.label
    var description = task.detail
    var importance : TaskImportance.Value = task.importance
    // Add a variable to hold the FileParamHolder on submission
    var fileHolder : Box[FileParamHolder] = Empty

//    val importanceMap : Map[String, TaskImportance.Value] = Map(
//      "Important" -> TaskImportance.Important,
//      "Normal"    -> TaskImportance.Normal,
//      "Faible"    -> TaskImportance.Low
//    )
//
//    def findLabelForValue(imp : TaskImportance.Value) : String = importanceMap find {entry => entry._2 == importance} match {
//      case Some((label, importance)) => label
//      case None => ""
//    }

    def modifierTask = {
      println("modifierTask")

      val receiptOk = fileHolder match {
        // An empty upload gets reported with a null mime type,
        // so we need to handle this special case
        case Full(FileParamHolder(_, null, _, _)) => true
        case Full(FileParamHolder(_, mime, _, data))
          if mime.startsWith("image/") => {
            true
          }
        case Full(_) => {
          S.error("Invalid receipt attachment")
          false
        }
        case _ => true
      }

      println("receiptOk=" + receiptOk)

      receiptOk match {
        case true =>
          val taskToSave = fileHolder match {
            case Full(FileParamHolder(_, mime, _, data)) =>
              println("Saving task with image!!!!!!!!!!!!!!!!!!!!!!!!")
              new Task(task.id, label, description, importance, Some(new Image(data, mime)))
            case _ =>
              new Task(task.id, label, description, importance)
          }

          Task.saveTask(taskToSave)
          S.redirectTo("/tasks/")
        case false => // do nothing
      }


    }

    val options = List(
      (TaskImportance.Important, "Important"),
      (TaskImportance.Normal,    "Normal"),
      (TaskImportance.Low,       "Faible"))


    val result = (
      "#label *+"       #> SHtml.text(label, label = _, "maxlength" -> "20", "cols" -> "20") &
      "#description *+" #> SHtml.textarea(description, description = _, "cols" -> "30", "rows" -> "8") &
//      "#importance"     #> SHtml.radio(importanceMap.keys.toList, Full(findLabelForValue(importance)), {str : String => importance = importanceMap(str)}).toForm &
      "#importance"     #> SHtml.selectObj(options, Full(importance), {imp : TaskImportance.Value => importance = imp}) &
      "#image"          #> SHtml.fileUpload({holder : FileParamHolder => fileHolder = Full(holder)}) &
      "#submitButton"   #> SHtml.submit("Modifier", modifierTask _)
    ).apply(content)


    result
  }

//    private def editTask(content : NodeSeq) : NodeSeq = {
//    val task = currentTask.get.get
//    var label = task.label
//    var description = task.detail
//    var importance : TaskImportance.Value = task.importance
//
//    val importanceMap : Map[String, TaskImportance.Value] = Map(
//      "Important" -> TaskImportance.Important,
//      "Normal"    -> TaskImportance.Normal,
//      "Faible"    -> TaskImportance.Low
//    )
//
//    def findLabelForValue(imp : TaskImportance.Value) : String = importanceMap find {entry => entry._2 == importance} match {
//      case Some((label, importance)) => label
//      case None => ""
//    }
//
//    def modifierTask = {
//      Task.saveTask(new Task(task.id, label, description, importance))
//      S.redirectTo("/tasks/")
//    }
//
//    val result = (
//      "#label *+"       #> SHtml.text(label, label = _, "maxlength" -> "20", "cols" -> "20") &
//      "#description *+" #> SHtml.textarea(description, description = _, "cols" -> "30", "rows" -> "8") &
//      "#importance"  #> SHtml.radio(importanceMap.keys.toList, Full(findLabelForValue(importance)), {str : String => importance = importanceMap(str)}).toForm &
//      "#submitButton"   #> SHtml.submit("Modifier", modifierTask _)
//    ).apply(content)
//
//
//    result
//  }
}

}
}
