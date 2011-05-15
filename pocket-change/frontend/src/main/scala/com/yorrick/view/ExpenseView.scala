package com.yorrick.view

import net.liftweb.http.LiftView
import xml.NodeSeq


class ExpenseView extends LiftView {

  override def dispatch = {
    case "enumerate" => doEnumerate _
  }

  def doEnumerate () : NodeSeq = {
    <lift:surround with="default" at="content">
     { "test de la vue dynamique" }
    </lift:surround>
  }

  def privateMethod() : NodeSeq = {
    <p>call on privateMethod</p>
  }

}
