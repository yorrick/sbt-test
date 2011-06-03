package com.yorrick.view

import net.liftweb.http.LiftView
import xml.NodeSeq


object FormsTestView extends LiftView {

  def dispatch = {
    case "test1" => test1 _
    case "test2" => test2 _
    case "test3" => test3 _
    // redirection sur le premier test par defaut
    case _ => test1 _
  }

  def test1 : NodeSeq =
    <lift:surround with="default" at="content">
      <lift:Ledger.add form="POST">
        <entry:description />
        <entry:amount /><br />
        <entry:submit />
      </lift:Ledger.add>
    </lift:surround>

  def test2 : NodeSeq =
    <lift:surround with="default" at="content">
      <lift:Ledger.listFormElements form="POST">
        <entry:checkbox/>
        <entry:submit />
      </lift:Ledger.listFormElements>
    </lift:surround>

  def test3 : NodeSeq =
    <lift:surround with="default" at="content">
      <h2>Liste des taches</h2>
      <ul >
        <div class="lift:Tasks.listEditorFriendly">
          <li>
            <h3 id="label">Label de la tâche</h3>
            <p class="description">Description : </p>
            <p>
              <lift:Ledger.editLink>
                <a id="editLink">Editer la tâche</a>
              </lift:Ledger.editLink>
            </p>
          </li>
        </div>
      </ul>
     </lift:surround>

}
