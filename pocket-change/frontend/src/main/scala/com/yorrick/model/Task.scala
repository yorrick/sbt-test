package com.yorrick.model

import java.lang.{IllegalStateException, IllegalArgumentException}

case object TaskImportance extends Enumeration /*with Ordered[TaskImportance]*/ {
  val Important, Normal, Low = Value

  //def compare(that: TaskImportance) : Int = 0
}


class Task(val id : Int, val label : String, val detail : String, val importance : TaskImportance.Value) {
  override def toString = "Task " + id + ", label : " + label
}

object Task {

  def apply(id : Int) = new Task(id, "", "", TaskImportance.Normal)

  private val tasks : List[Task] = List(
    new Task(1, "Dishes", "Boring, but I have to do it", TaskImportance.Low),
    new Task(2, "Tondeuse", "Passer la tondeuse", TaskImportance.Important),
    new Task(3, "Musique", "Enregistrer musique", TaskImportance.Important),
    new Task(4, "Banque", "Prendre RDV", TaskImportance.Normal),
    new Task(5, "DRH", "Demander les papiers", TaskImportance.Low)
  )

  def getTask(id : Int) : Task = tasks.filter(_.id == id) match {
    case task :: Nil => task
    case Nil => throw new IllegalArgumentException("No task with id " + id)
    case List(task1, task2, _*) => throw new IllegalStateException("More than one task with id " + id)
  }

  /**
   * Simule un chargement de données
   */
  def getTasks(importance: TaskImportance.Value) : List[Task] = tasks filter (_.importance == importance)

  /**
   * Retounre toutes les taches
   */
  def getTasks = tasks

}
