package com.yorrick.snippet

import net.liftweb.util.BindHelpers._
import com.yorrick.model
import model.TaskImportance
import xml.{Elem, Text, NodeSeq}
import net.liftweb.http.{S, SHtml, StatefulSnippet, DispatchSnippet}

class BridgeKeeper extends StatefulSnippet {

  var (count, stringValue) = (0, "rien")

  var dispatch : DispatchIt = {
    case "challenge" => firstPage _
  }

  def firstPage(content : NodeSeq) : NodeSeq = {
    def processFirstCall() = {
      dispatch = {case "challenge" => secondPage _}
    }

    etape("Initiale", link("test", processFirstCall, content \\ "a"), content)
  }

  def secondPage(content: NodeSeq) : NodeSeq = {
    def processSecondCall = {
      unregisterThisSnippet()
      S.redirectTo("test")
    }

    etape("Deuxième", SHtml.link("test", processSecondCall _, content \\ "a"), content)
  }

  def etape(nomEtape : String, link : Elem, content : NodeSeq) = {
    count += 1
    stringValue = "quelque chose " + count

    (
      "#etape *+"       #> nomEtape &
      "#count *+"       #> count &
      "#stringValue *+" #> stringValue &
      ".link"           #> link
    ).apply(content)
  }

}
