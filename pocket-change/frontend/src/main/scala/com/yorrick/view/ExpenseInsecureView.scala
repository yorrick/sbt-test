package com.yorrick.view

import xml.NodeSeq
import net.liftweb.http.{InsecureLiftView, LiftView}


class ExpenseInsecureView extends InsecureLiftView {

  def privateMethod() : NodeSeq = {
    <lift:surround with="default" at="content">
     { "insecure lift view, call on method privateMethod" }
    </lift:surround>
  }

}