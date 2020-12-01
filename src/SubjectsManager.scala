import scala.annotation.tailrec

case class SubjectsManager(val subjs: List[Subject]) {

  def addSubject(subj: Subject): SubjectsManager = SubjectsManager.addSubject(this, subj)

  def sort_Subjects(): SubjectsManager = SubjectsManager.sort_Subjects(this)

}

object SubjectsManager {

  def addSubject(subj_man: SubjectsManager, subj: Subject): SubjectsManager = {
    sort_Subjects(SubjectsManager(subj :: subj_man.subjs))
  }

  def sort_Subjects(subj_man: SubjectsManager): SubjectsManager = {
    SubjectsManager(subj_man.subjs.sortBy(_.name))
  }

  def searchSubject(subj_man: SubjectsManager, title: String): Option[Subject] = {
    Option((subj_man.subjs filter ( s => s.name.equals(title))) (0))
  }

  @tailrec
  def printReminders(lst: List[Subject]) : Unit =
    lst match {
      case Nil => Nil
      case head :: tail => println(s"Name: ${head.name}"); printReminders(tail)
    }

}
