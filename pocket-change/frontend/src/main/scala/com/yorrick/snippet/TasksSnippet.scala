

package com.yorrick {
package snippet {

import net.liftweb.util.BindHelpers._
import requestvars.currentTask
import net.liftweb.common.{Empty, Full, Box}
import net.liftweb.http._
import model.{Image, Task, TaskImportance}
import xml.NodeSeq

class TasksEditionSnippet extends StatefulSnippet {

  def dispatch : DispatchIt = {
    case "editTask" => editTask _
  }

  var alreadyModifyied = false
  var (label, description, importance) = ("", "", TaskImportance.Normal)


  private def editTask(content : NodeSeq) : NodeSeq = {
    val task = currentTask.get.get

    if (!alreadyModifyied) {
      label = task.label
      description = task.detail
      importance = task.importance
      //(label, description, importance) = (task.label, task.detail, task.importance)
    }

    // Add a variable to hold the FileParamHolder on submission
    var fileHolder : Box[FileParamHolder] = Empty

    def modifierTask = {
      alreadyModifyied = true

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

      receiptOk match {
        case true =>
          val taskToSave = fileHolder match {
            case Full(FileParamHolder(_, mime, _, data)) =>
              new Task(task.id, label, description, importance, Some(new Image(data, mime)))
            case _ =>
              new Task(task.id, label, description, importance)
          }

          Task.saveTask(taskToSave)
          unregisterThisSnippet()
          S.redirectTo("/tasks/")
        case false =>
          // erreur, dans ce cas on re sette la current task, pour que la vue n'affiche pas d'erreur
          currentTask(Full(task))
      }


    }

    val options = List(
      (TaskImportance.Important, "Important"),
      (TaskImportance.Normal,    "Normal"),
      (TaskImportance.Low,       "Faible"))


    val result = (
      "#label *+"       #> SHtml.text(label, label = _, "maxlength" -> "20", "cols" -> "20") &
      "#description *+" #> SHtml.textarea(description, description = _, "cols" -> "30", "rows" -> "8") &
      "#importance"     #> SHtml.selectObj(options, Full(importance), {imp : TaskImportance.Value => importance = imp}) &
      "#image"          #> SHtml.fileUpload({holder : FileParamHolder => fileHolder = Full(holder)}) &
      "#submitButton"   #> SHtml.submit("Modifier", modifierTask _)
    ).apply(content)


    result
  }

}

}
}
