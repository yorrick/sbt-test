package com.yorrick.snippet.requestvars

import net.liftweb.http.{S, RequestVar}
import net.liftweb.common.Box._

object accountNumber extends RequestVar[Int](S.param("accountNumber").map(_.toInt) openOr 20)