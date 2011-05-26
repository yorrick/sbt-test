package com.yorrick.snippet

import net.liftweb.http.StatefulSnippet
import xml.{Text, NodeSeq}
import net.liftweb.http.DispatchSnippet
import net.liftweb.util.BindHelpers._
import com.yorrick.model
import model.TaskImportance

class BridgeKeeper extends StatefulSnippet {

  var (count, stringValue) = (0, "rien")

  def dispatch : DispatchIt = {
    case "challenge" => firstPage _
  }

  def firstPage(content : NodeSeq) : NodeSeq = {

    println(this)

    count += 1
    stringValue = "quelque chose " + count

    (
      "#count *+"       #> count &
      "#stringValue *+" #> stringValue
    ).apply(content)
  }

}