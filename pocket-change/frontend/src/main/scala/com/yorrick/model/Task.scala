package com.yorrick.model


object TaskImportance extends Enumeration {
  val Important, Normal, Low = Value
}

class Task(val id : Int, val label : String, val detail : String, val importance : TaskImportance.Value) {

}

