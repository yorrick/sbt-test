

package com.yorrick {
package snippet {

import net.liftweb.util.BindHelpers._
import requestvars.{currentTask, taskImportance}
import xml.NodeSeq._
import net.liftweb.common.{Empty, Full, Box}
import net.liftweb.http._
import model.{Image, Task, TaskImportance}
import xml.{Null, Attribute, Text, NodeSeq}

object  TasksListSnippet extends DispatchSnippet {

  def dispatch : DispatchIt = {
    case "viewTask" => viewTask _
  }

  private def viewTask(content : NodeSeq) : NodeSeq = {
    val result = Task.getTasks(taskImportance.is).flatMap(task => {
        val redirectPath = "/tasks/edition/"

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
          "#editLink"       #> SHtml.link(redirectPath, () => currentTask(Full(task)), content \\ "a")
        ).apply(content)
      }
    )

    result
  }

}

}
}
