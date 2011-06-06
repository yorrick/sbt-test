

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
    case "list" => listOfSnippets(taskImportance.is) _
    case "listEditorFriendly" => listEditorFriendly _
    case "viewTask" => viewTask _
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
    val result = Task.getTasks.flatMap(task => {
        val redirectPath = "/tasks/edition/"
        // callback du lien
        def linkCallback = {
          currentTask(Full(task))
          //S.redirectTo(redirectPath)
        }

        (
          "#label *" #> task.label &
          ".description *+" #> task.detail &
          "#editLink"  #> SHtml.link(redirectPath, linkCallback _, content \\ "a")
        ).apply(content)
      }
    )

    result
  }

}

}
}
