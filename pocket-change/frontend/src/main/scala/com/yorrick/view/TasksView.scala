package com.yorrick.view

import net.liftweb.http.LiftView
import xml.NodeSeq
import com.yorrick.snippet.requestvars.taskImportance


object TasksView extends LiftView {

  def dispatch = {
    case _ : String => list _
  }

  // specific rss feed for the user
  def list : NodeSeq =
    <lift:surround with="default" at="content">
      <p>Liste des taches à partir de {taskImportance.is}</p>
      <ul >
        <li>
          <div class="lift:Tasks.list">
            <span>test</span>
          </div>
        </li>
      </ul>
    </lift:surround>

}
