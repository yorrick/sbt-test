package com.yorrick.snippet

import net.liftweb.http.StatefulSnippet
import xml.{Text, NodeSeq}
import net.liftweb.http.DispatchSnippet
import net.liftweb.util.BindHelpers._
import com.yorrick.model
import model.TaskImportance

class BridgeKeeper extends StatefulSnippet {

  var (count, stringValue) = (0, "rien")

  var dispatch : DispatchIt = {
    case "challenge" => firstPage _
  }

  def firstPage(content : NodeSeq) : NodeSeq = {
    dispatch = {case "challenge" => secondPage _}

    etape("Initiale", content)
  }

  def secondPage(content: NodeSeq) : NodeSeq = {
    dispatch = {case "challenge" => firstPage _}

    etape("Deuxième", content)
  }

  def etape(nomEtape : String, content : NodeSeq) = {
    // permet de voir quelle instance de snippet est utilisee
    println(this)

    count += 1
    stringValue = "quelque chose " + count

    (
      "#etape *+"       #> nomEtape &
      "#count *+"       #> count &
      "#stringValue *+" #> stringValue &
      //"#link"           #> link("test", () => redirectTo("test"), <span>Passer à l'étape suivante</span>)
      ".link"           #> link("test", () => redirectTo("test"), chooseTemplate("link", "link", content))
    ).apply(content)
  }

}