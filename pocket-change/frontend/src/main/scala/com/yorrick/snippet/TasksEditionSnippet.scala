

package com.yorrick {
package snippet {

import net.liftweb.util.BindHelpers._
import requestvars.currentTask
import net.liftweb.http._
import model.{Image, Task, TaskImportance}
import xml.NodeSeq
import net.liftweb.common.{Empty, Full, Box}

class TasksEditionSnippet extends StatefulSnippet {

  var dispatch : DispatchIt = {
    case "editTask" => firstStage _
  }

  case class TaskHolder (
    var id : Option[Int] = None,
    var label : String   = "",
    var desc : String    = "",
    var importance : TaskImportance.Value = TaskImportance.Normal,
    var image : Option[(String, Array[Byte])] = Empty
  )

  var taskToSave = new TaskHolder

  /**
   * Implémente les controles
   */
  def controlTask(task : TaskHolder) : Boolean = {
    val minLabelLenght = 5

    // controles
    if (task.label.size < minLabelLenght) {
      S.error("label must be at least " + minLabelLenght + " chars long")
      false
    } else {
      task.image match {
        case None => true
        // An empty upload gets reported with a null mime type,
        // so we need to handle this special case
        case Some((null, _)) =>
          S.error("Emty upload")
          false
        case Some((mime, data)) if (!(mime.startsWith("image/"))) =>
          S.error("Please upload an image")
          false
        case Some((_, _)) => true
      }
    }
  }

  /**
   * Sauvegarde des données
   */
  def saveTask() = {
    println("trying to save task " + taskToSave)

    val taskId = taskToSave.id match {
      case None => -1
      case Some(id) => id
    }

    val image = taskToSave.image match {
      case Some((mime, data)) => Some(new Image(data, mime))
      case None => None
    }

    Task.saveTask(new Task(taskId, taskToSave.label, taskToSave.desc, taskToSave.importance, image))

    // on supprime le snippet de la session, et redirection
    unregisterThisSnippet()
    S.redirectTo("/tasks/")
  }

  private def firstStage(c : NodeSeq) : NodeSeq = {
    println("firstStage")

     val content = TemplateFinder.findAnyTemplate("tasks" :: "stage1" :: Nil) match {
      case Full(content) => content
      case _ => <span>Could not load template</span>
    }

    // lorsque'on provient de la page de liste, la tache à sauvegarder est la tache courante (requestParam)
    currentTask.get match {
      case Full(taskFromList) =>
        // premier affichage du first stage, on va chercher les données de la current task
        taskToSave.id         = Full(taskFromList.id)
        taskToSave.label      = taskFromList.label
        taskToSave.desc       = taskFromList.detail
        taskToSave.importance = taskFromList.importance

        taskFromList.image match {
          case Some(Image(data, mime)) => taskToSave.image = Full(mime, data)
          case _ => // nothing to do
        }

      case _ =>
        // rien à faire
    }

    def saveFirstStageData = {
      if (controlTask(taskToSave)){
        saveTask()
      }
    }

    def goToSecondStage = {
      if (controlTask(taskToSave)){
        dispatch = {
          case "editTask" => secondStage _
        }
      }

    }

    val options = List(
      (TaskImportance.Important, "Important"),
      (TaskImportance.Normal,    "Normal"),
      (TaskImportance.Low,       "Faible"))


    val result = (
      "#label *+"       #> SHtml.text(taskToSave.label, taskToSave.label = _, "maxlength" -> "20", "cols" -> "20") &
      "#description *+" #> SHtml.textarea(taskToSave.desc, taskToSave.desc = _, "cols" -> "30", "rows" -> "8") &
      "#importance"     #> SHtml.selectObj(options, Full(taskToSave.importance), {imp : TaskImportance.Value => taskToSave.importance = imp}) &
//      "#image"          #> SHtml.fileUpload({holder : FileParamHolder => fileHolder = Full(holder)}) &
      "#saveButton"   #> SHtml.submit("Sauvegarder", saveFirstStageData _) &
      "#nextButton"   #> SHtml.submit("Ajouter une image", goToSecondStage _)
    ).apply(content)


    result
  }

  private def secondStage(content : NodeSeq) : NodeSeq = {
    def handleFileUpload : FileParamHolder => Any = {holder : FileParamHolder =>
      holder match {
        case FileParamHolder(name, mime, fileName, data) =>
          taskToSave.image = Some((mime, data))
        case _ => // nothing to do
      }
    }

    def saveFirstAndSecondStageData = {
      if (controlTask(taskToSave)){
        saveTask()
      }
    }

    def previous = {
      dispatch = {
        case "editTask" => firstStage _
      }
    }

    val result = (
      "#image"        #> SHtml.fileUpload(handleFileUpload) &
      "#previousButton"#> SHtml.submit("Précédent", previous _) &
      "#saveButton"   #> SHtml.submit("Sauvegarder", saveFirstAndSecondStageData _)
    ).apply(content)

    result
  }

}

}
}
