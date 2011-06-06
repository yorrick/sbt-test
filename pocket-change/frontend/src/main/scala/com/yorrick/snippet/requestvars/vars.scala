package com.yorrick.snippet.requestvars

import net.liftweb.http.{S, RequestVar}
import net.liftweb.common.Box._
import com.yorrick.model.TaskImportance
import net.liftweb.common.{Empty, Full, Box}

object accountNumber extends RequestVar[Int](S.param("accountNumber").map(_.toInt) openOr 20)

object taskImportance extends RequestVar[TaskImportance.Value](S.param("taskImportance").map(_ match {
  case "important" => TaskImportance.Important
  case "normal" => TaskImportance.Normal
  case "low" => TaskImportance.Low
  case _ => TaskImportance.Low
}) openOr TaskImportance.Low)

//object taskId extends RequestVar[Box[Int]](S.param("taskId").map(_ match {
//  case taskId : String => println("taskId value : " + taskId); Full(taskId.toInt)
//}) openOr Empty)

