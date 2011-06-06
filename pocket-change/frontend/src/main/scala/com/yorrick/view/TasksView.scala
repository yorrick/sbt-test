package com.yorrick.view

import net.liftweb.http.LiftView
import xml.NodeSeq
import com.yorrick.snippet.currentTask
import com.yorrick.snippet.requestvars.{taskImportance}
import com.yorrick.model.Task
import net.liftweb.common.{Failure, Empty, Full}

object TasksView extends LiftView {

  def dispatch = {
    case "edit" => edit _
    case "list" => list _
  }

  /**
   * Liste des taches, avec liens pour edition
   */
  def list : NodeSeq =
    <lift:surround with="default" at="content">
      <h2>Liste des taches à partir de {taskImportance.is}</h2>
      <ul >
        <div class="lift:Tasks.viewTask">
          <li>
            <h3 id="label">Label de la tâche</h3>
            <p class="description">Description : </p>
            <p>
              <a id="editLink">Editer la tâche</a>
            </p>
          </li>
        </div>
      </ul>
     </lift:surround>

  def edit : NodeSeq = {
    <lift:surround with="default" at="content">
      {
        currentTask.is match {
          case Full(task : Task) => <h2>Edition de la tache {task}</h2>
          case Failure(message, _, _) => <h2>Impossible d'editer la tâche ({message})</h2>
          case _ => <h2>Veuillez spécifier une tache svp</h2>
        }
      }

    </lift:surround>
  }


}
