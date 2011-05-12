package com.yorrick.view

import net.liftweb.http.LiftView
import xml.NodeSeq


object RssView extends LiftView {

  def dispatch = {
    case "news" => siteNews _
  }

  // specific rss feed for the user
  def recent(acctId : String, authToken: String) : NodeSeq =
    <lift:surround with="rss" at="content">
      <ul>
        <li>News 1 pour le user {acctId}</li>
        <li>News autre pour le user {acctId}</li>
        <li>Test : {test(5, 7)}</li>
      </ul>
    </lift:surround>


  // general rss feed for the site
  def siteNews : NodeSeq = <ul><li>News 1</li><li>News autre</li></ul>


  def test : (Int, Int) => String =
    (a : Int, b : Int) => "(" + (a + b).toString + ")"
}
