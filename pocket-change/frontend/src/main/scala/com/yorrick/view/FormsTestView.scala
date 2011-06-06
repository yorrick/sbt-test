package com.yorrick.view

import net.liftweb.http.LiftView
import xml.NodeSeq


object FormsTestView extends LiftView {

  def dispatch = {
    case "test1" => test1 _
    case "test2" => test2 _
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

}
