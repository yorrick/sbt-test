

package com.yorrick {
package snippet {

import requestvars.taskImportance
import xml.{Text, NodeSeq}
import model.{Task, TaskImportance}
import net.liftweb.util.BindHelpers._
import xml.NodeSeq._
import net.liftweb.http.{RequestVar, SHtml, S, DispatchSnippet}
import net.liftweb.common.{Empty, Full, Box}

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

        (
          "#label *"        #> (task.label + "(" + task.id + ", " + task.importance.toString + ")") &
          ".description *+" #> task.detail &
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

    val importanceMap : Map[String, TaskImportance.Value] = Map(
      "Important" -> TaskImportance.Important,
      "Normal"    -> TaskImportance.Normal,
      "Faible"    -> TaskImportance.Low
    )

    def findLabelForValue(imp : TaskImportance.Value) : String = importanceMap find {entry => entry._2 == importance} match {
      case Some((label, importance)) => label
      case None => ""
    }

    def modifierTask = {
      Task.saveTask(new Task(task.id, label, description, importance))
      S.redirectTo("/tasks/")
    }

    val result = (
      "#label *+"       #> SHtml.text(label, label = _, "maxlength" -> "20", "cols" -> "20") &
      "#description *+" #> SHtml.textarea(description, description = _, "cols" -> "30", "rows" -> "8") &
      "#importance"  #> SHtml.radio(importanceMap.keys.toList, Full(findLabelForValue(importance)), {str : String => importance = importanceMap(str)}).toForm &
      "#submitButton"   #> SHtml.submit("Modifier", modifierTask _)
    ).apply(content)


    result
  }
}

}
}
