

package com.yorrick {
package snippet {

import requestvars.taskImportance
import xml.{Text, NodeSeq}
import net.liftweb.http.DispatchSnippet
import model.{Task, TaskImportance}

object TasksSnippet extends DispatchSnippet {

  def dispatch : DispatchIt = {
    case "list" => listOfSnippets(taskImportance.is) _
  }

  private def listOfSnippets(importance: TaskImportance.Value) (xhtml : NodeSeq) : NodeSeq = {
    xhtml
  }

}

}
}
