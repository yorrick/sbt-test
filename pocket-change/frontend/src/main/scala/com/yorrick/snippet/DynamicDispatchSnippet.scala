

package com.yorrick {
package snippet {

import net.liftweb.http.DispatchSnippet
import xml.{Text, NodeSeq}

class DynamicDispatchSnippet extends DispatchSnippet {

  def dispatch : DispatchIt = {
    case "toto" => totoMethod _
    case methodName => catchAllMethod(methodName, _)
  }


  def totoMethod(xhtml : NodeSeq) : NodeSeq = xhtml

  def catchAllMethod(methodName: String, xhtml : NodeSeq) : NodeSeq = Text("No method to handle snippet " + methodName)

}

}
}
