package com.yorrick.snippet.requestvars

import net.liftweb.http.{S, RequestVar}
import net.liftweb.common.Box._
import net.liftweb.common.{Empty, Full, Box}
import com.yorrick.model.{Task, TaskImportance}

object accountNumber extends RequestVar[Int](S.param("accountNumber").map(_.toInt) openOr 20)

object taskImportance extends RequestVar[TaskImportance.Value](S.param("taskImportance").map(_ match {
  case "important" => TaskImportance.Important
  case "normal" => TaskImportance.Normal
  case "low" => TaskImportance.Low
  case _ => TaskImportance.Low
}) openOr TaskImportance.Low)

object currentTask extends RequestVar[Box[Task]](Empty)


