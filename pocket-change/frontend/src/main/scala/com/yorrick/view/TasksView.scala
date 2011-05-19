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
      <h2>Liste des taches à partir de {taskImportance.is}</h2>
      <ul >
        <lift:Tasks.list>
          <li>
            <h3><tasks:label/></h3>
            <p><tasks:description/></p>
          </li>
        </lift:Tasks.list>
      </ul>
    </lift:surround>

}
