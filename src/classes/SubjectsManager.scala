package classes

import classes.Notebook.Note
import classes.RemindersManager.Reminder

import scala.annotation.tailrec

case class SubjectsManager(subjs: List[Subject]) {

  def addSubject(subj: Subject): SubjectsManager = SubjectsManager.addSubject(this, subj)

  def sort_Subjects(): SubjectsManager = SubjectsManager.sort_Subjects(this)

  def searchSubject(name: String): Option[Subject] = SubjectsManager.searchSubject(this, name)

  override def toString: String = SubjectsManager.toString(subjs)

}

object SubjectsManager {

  def addSubject(subj_man: SubjectsManager, subj: Subject): SubjectsManager = {
    sort_Subjects(SubjectsManager(subj :: subj_man.subjs))
  }

  def delSubject(subj_man: SubjectsManager, subj: Subject): SubjectsManager = {
    searchSubject(subj_man, subj.name) match {
      case Some(b) => SubjectsManager(subj_man.subjs.filter(r => !r.name.equals(subj.name)))
      case None => /*System.err.println("Erro: Esse lembrete não existe")*/
        throw new IllegalArgumentException("Erro: Esse lembrete não existe")
    }
  }

  def sort_Subjects(subj_man: SubjectsManager): SubjectsManager = {
    SubjectsManager(subj_man.subjs.sortBy(_.name))
  }

  def searchSubject(subj_man: SubjectsManager, title: String): Option[Subject] = {
    Option((subj_man.subjs filter (s => s.name.equals(title))).head)
  }

  def toString(subjs: List[Subject]): String = {
    def aux(subjL: List[Subject]): String = subjL match {
      case Nil => ""
      case head :: tail => s"${head.toString}\\n${aux(tail)}"
    }
    aux(subjs)
  }

  def fromString(str: String, reminders: List[Reminder], notes: List[Note]): SubjectsManager = {
    @tailrec
    def aux(subj_man: SubjectsManager, lst: List[String]): SubjectsManager = lst match {
      case Nil => subj_man
      case head :: tail => aux(subj_man.addSubject(Subject.fromString(head, reminders, notes)), tail)
    }
    aux(SubjectsManager(List()) , str.split("\\\\n").toList.map(string => string.trim))
  }

  @tailrec
  def printReminders(lst: List[Subject]) : Unit =
    lst match {
      case Nil => Nil
      case head :: tail => println(s"Name: ${head.name}"); printReminders(tail)
    }

}
