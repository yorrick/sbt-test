package com.yorrick.model

import sun.security.krb5.internal.crypto.Nonce
import scala.None
import java.lang.{IllegalArgumentException, IllegalStateException}

case object TaskImportance extends Enumeration {
  val Low, Normal, Important = Value
}


class Task(val id : Int, val label : String, val detail : String, val importance : TaskImportance.Value) {
  override def toString = "Task " + id + ", label : " + label
}

object Task {

  def apply(id : Int) = new Task(id, "", "", TaskImportance.Normal)

  private var tasks : List[Task] = List(
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
   * Simule la persistence d'une tache
   */
  def saveTask(newTask : Task) : Task = {
    if (newTask.id >= 0) {
      tasks.find(_.id == newTask.id) match {
        // cas de la modification
        case Some(taskToBeModified) =>
          tasks = newTask :: tasks.filterNot {taskToBeModified => taskToBeModified.id == newTask.id}
       // erreur : on renseigne un id qui n'existe pas
        case None =>
          throw new IllegalArgumentException("Not task with id " + newTask.id)
      }
      newTask
    }
    else {
      val nextId = (tasks max Ordering.by {t : Task => t.id}).id
      new Task(nextId, newTask.label, newTask.detail, newTask.importance)
    }
  }

  /** Fonction qui determine l'ordre dans lequel les taches sont renvoyees */
  val taskSorter = (task : Task) => (task.importance, task.id)

  /**
   * Simule un chargement de données
   */
  def getTasks(importance: TaskImportance.Value) : List[Task] =
    tasks filter (_.importance >= importance) sortBy taskSorter reverse

  /**
   * Retourne toutes les taches
   */
  def getTasks : List[Task] = getTasks(TaskImportance.Low)

}
