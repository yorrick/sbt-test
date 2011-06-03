

package com.yorrick {
package snippet {

import requestvars.taskImportance
import xml.{Text, NodeSeq}
import net.liftweb.http.DispatchSnippet
import model.{Task, TaskImportance}
import net.liftweb.util.BindHelpers._

object TasksSnippet extends DispatchSnippet {

  def dispatch : DispatchIt = {
    case "list" => listOfSnippets(taskImportance.is) _
    case "listEditorFriendly" => listEditorFriendly _
  }

  private def listOfSnippets(importance: TaskImportance.Value) (content : NodeSeq) : NodeSeq = {
    //<h2>test</h2>
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

//    println(result)
    result
  }

}

}
}
