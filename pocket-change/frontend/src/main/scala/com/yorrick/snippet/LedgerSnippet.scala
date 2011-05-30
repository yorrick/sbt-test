

package com.yorrick {
package snippet {

import requestvars.taskImportance
import xml.{Text, NodeSeq}
import model.{Task, TaskImportance}
import net.liftweb.util.BindHelpers._
import net.liftweb.http.{S, RequestVar, SHtml, DispatchSnippet}

object amount extends RequestVar("0")
object description extends RequestVar("")

object LedgerSnippet extends DispatchSnippet {

  def dispatch : DispatchIt = {
    case "add" => addLedger _
  }

  private def addLedger(content : NodeSeq) : NodeSeq = {
    println("addLedger rendering " + amount + ", " + description)

    def processEntryAdd = {
      try {
        if (amount.is.toDouble < 0) {
          S.error("Invalid amount")
        } else{
          // redirect
          println("processing entry add with value " + amount + ", " + description)
        }
      } catch {
        case e : NumberFormatException => S.error("Wrong value")
      }
    }

    bind("entry", content,
      "description" -> SHtml.text(description, {newDesc => println("setting desc to new value " + newDesc); description(newDesc)}),
      "amount"      -> SHtml.text(amount, {newAmount => println("setting amount to new value " + newAmount); amount(newAmount)}),
      "submit"      -> SHtml.submit("Add ledger", processEntryAdd _)
    )
  }


//  private def addLedger(content : NodeSeq) : NodeSeq = {
//    println("addLedger rendering " + Thread.currentThread.getName)
//
//    var desc = ""
//    var amount = "0"
//
//    def processEntryAdd = {
//      println("processing entry add with value " + amount + ", " + desc + "  " + Thread.currentThread.getName)
//    }
//
//    bind("entry", content,
//      "description" -> SHtml.text(desc, {newDesc => println("setting desc to new value " + newDesc); desc = newDesc}),
//      "amount"      -> SHtml.text(amount, {newAmount => println("setting amount to new value " + newAmount); amount = newAmount}),
//      "submit"      -> SHtml.submit("Add ledger", processEntryAdd _)
//    )
//  }


}

}
}
