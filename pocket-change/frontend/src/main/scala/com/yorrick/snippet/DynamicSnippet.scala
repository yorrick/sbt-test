

package com.yorrick {
package snippet {

import requestvars.accountNumber
import scala.xml.NodeSeq
import net.liftweb._
import util._
import Helpers._


class DynamicSnippet {

  def dynamicMethod(xhtml : NodeSeq) : NodeSeq = {
    // simply return the template's content
    xhtml
  }

}

}
}
