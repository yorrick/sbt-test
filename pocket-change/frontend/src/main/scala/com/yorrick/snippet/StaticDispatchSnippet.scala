

package com.yorrick {
package snippet {

import xml.{Text, NodeSeq}
import net.liftweb.http.{S, DispatchSnippet}
import net.liftweb.common.{Full, Empty}

object StaticDispatchSnippet extends DispatchSnippet {

  def dispatch : DispatchIt = {
    case methodName => render(methodName) _
  }

  def render(methodName: String) (xhtml : NodeSeq) : NodeSeq = {
//    val s = "simple " + (S.param("simple") match {
//      case Empty => "does not exist"
//      case Full(value) => "exists : " + value
//    })
    Text("Explicit snippet mapping, invocation of method " + methodName)
  }

}

}
}
