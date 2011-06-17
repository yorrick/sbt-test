package com.yorrick.view

import xml.NodeSeq
import com.yorrick.model.Task
import net.liftweb.common.{Failure, Empty, Full}
import com.yorrick.snippet.requestvars.{currentTask, taskImportance}
import net.liftweb.http.{TemplateFinder, LiftView}

object TasksView extends LiftView {

  def dispatch = {
    case "list" => list _
  }

  /**
   * Liste des taches, avec liens pour edition
   */
  private def list : NodeSeq =
    <lift:surround with="default" at="content">
      <h2>Liste des taches à partir de {taskImportance.is}</h2>
      <ul >
        <div class="lift:TasksList.viewTask">
          <li>
            <h3 id="label">Label de la tâche</h3> <br/>
            <p class="description">Description : </p> <br/>
            <img id="image"/> <br/>
            <p>
              <a id="editLink">Editer la tâche</a>
            </p> <br/>
          </li>
        </div>
      </ul>
     </lift:surround>

}
