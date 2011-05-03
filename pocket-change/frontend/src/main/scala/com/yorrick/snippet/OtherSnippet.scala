

package com.yorrick {
package snippet {

import requestvars.accountNumber
import scala.xml.{NodeSeq, Text}
import net.liftweb._
import http.{SHtml, S, RequestVar}
import util._
import common._
import Helpers._
import model.User

class OtherSnippet {

  def summary(xhtml : NodeSeq) : NodeSeq = {
    val value = "TEST"
    bind("other", xhtml, "value" -> accountNumber.is)
  }

}

}
}
