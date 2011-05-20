package com.yorrick.model


case object TaskImportance extends Enumeration /*with Ordered[TaskImportance]*/ {
  val Important, Normal, Low = Value

  //def compare(that: TaskImportance) : Int = 0
}


class Task(val id : Int, val label : String, val detail : String, val importance : TaskImportance.Value)

object Task {

  private val tasks : List[Task] = List(
    new Task(1, "Dishes", "Boring, but I have to do it", TaskImportance.Low),
    new Task(1, "Tondeuse", "Passer la tondeuse", TaskImportance.Important),
    new Task(1, "Musique", "Enregistrer musique", TaskImportance.Important),
    new Task(1, "Banque", "Prendre RDV", TaskImportance.Normal),
    new Task(1, "DRH", "Demander les papiers", TaskImportance.Low)
  )

  /**
   * Simule un chargement de données
   */
  def getTasks(importance: TaskImportance.Value) : List[Task] = tasks filter (_.importance == importance)

  /**
   * Retounre toutes les taches
   */
  def getTasks = tasks

}
