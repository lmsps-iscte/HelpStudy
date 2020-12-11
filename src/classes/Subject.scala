package classes

import classes.Notebook.Note
import classes.RemindersManager.Reminder
import classes.Subject.Evaluation
import java.time.LocalDate

import scala.annotation.tailrec
import scala.util.control.Breaks.break



//val rems_default = List()

case class Subject(name: String, rems: List[Reminder] = List(), notes: List[Note] = List(), evals: List[Evaluation] = List()) {

  /*def this(m: Map[String, String]) =
    this(m.getOrElse("rems", rems_default))*/


  def associate_reminder(rem: Reminder): Subject = Subject.associate_reminder(this, rem)

  def associate_note(note: Note): Subject = Subject.associate_note(this, note)

  def add_evaluation(eval: Evaluation): Subject = Subject.add_evaluation(this, eval)

  def del_evaluation(eval: String): Subject = Subject.del_evaluation(this, eval)

  def getEvaluation(title: String, date: LocalDate): Evaluation = Subject.getEvaluation(this, title, date)

  def calculate_FinalGrade(): Double = Subject.calculate_FinalGrade(this)

  override def toString: String = Subject.toString(this)

}

object Subject {

  type Grade = Double
  type Percent_Grade = Double
  type Date = LocalDate
  type Title = String
  type Evaluation = (Date, (Percent_Grade, Grade), Title)
  val boundary = "////0xFFFF////EOF"

  def associate_reminder(subj: Subject, rem: Reminder): Subject = {
    Subject(subj.name, rem :: subj.rems, subj.notes, subj.evals)
  }

  def associate_note(subj: Subject, note: Note): Subject = {
    Subject(subj.name, subj.rems, note :: subj.notes, subj.evals)
  }

  def add_evaluation(subj: Subject, eval: Evaluation): Subject = {
    val rem = ("Avaliação: " + eval._3, "", 4, eval._1, 0.0, subj.name)
    associate_reminder(subj, rem)
    Subject(subj.name, subj.rems, subj.notes, eval :: subj.evals)
  }

  def del_evaluation(subj: Subject, title: Title): Subject = {
    searchEvaluation(subj, title) match {
      case Some(b) => Subject(subj.name, subj.rems, subj.notes, subj.evals.filter(e => !e._3.equalsIgnoreCase(title)))
      case None => throw new IllegalArgumentException("Erro: Essa avaliação não existe")
    }
  }

  def update_root_manager_subject(subj_man: SubjectsManager, subj: Subject): SubjectsManager = {
    subj_man.replaceSubject(subj)
  }

  def getEvaluation(subj: Subject, title: String, date: LocalDate): Evaluation = {
    subj.evals.filter(eval => eval._3.equals(title) && eval._1.equals(date)).head
  }

  def searchEvaluation(subj: Subject, title: String): Option[Evaluation] = {
    Option((subj.evals filter (e => e._3.equalsIgnoreCase(title))).head)
  }

  def calculate_FinalGrade(subj: Subject): Double = {
    val p = subj.evals.map(e => e._2._1).foldRight(0.0)(_+_)
    if(p < 100 || p > 100) throw new IllegalStateException("ERRO: A soma das percentagens dos teus elementos de avaliação é diferente 100%")
    val x = subj.evals.map( a => a._2)
    val result = x.map{ case(a,b) => a*b/100}
    result.foldRight(0.0)(_+_)
  }

  def calculate_FinalGrade2(percent_test: Double, test: List[Double])(percent_project: Double, project: List[Double])(percent_labs: Double, labs: List[Double]): Double = {
    percent_test*test.sum/test.length + percent_project*project.sum/project.length + percent_labs*labs.sum/labs.length
  }


  def product(xs: List[Int]): Int = (xs foldLeft 1) (_*_)
  def sum(xs: List[Int]): Int = (xs foldLeft 1) (_+_)

  def toString(subj: Subject) : String = {
    def aux(evals: List[Evaluation]): String = evals match {
      case Nil => s""
      case head :: tail => s"${head._1},${head._2._1},${head._2._2},${head._3} $boundary ${aux(tail)}"
    }
    println(subj.evals)
    s"${subj.name} $boundary ${aux(subj.evals)}"
  }

  def parseEval(head: String): (Date, (Percent_Grade, Grade), Title) = {
    val fields = head.split(",")
    val date = LocalDate.parse(fields.head.trim)
    val percent_Grade = fields(1).toDouble
    val grade = fields(2).toDouble //ATENCAO---------!!!!!!!!!
    val title = fields.last
    (date, (percent_Grade, grade), title)
  }

  def fromString(toParse: String, rems: List[Reminder], notes: List[Note]) : Subject = {

    @tailrec
    def aux(lst: List[String], evals: List[Evaluation]): List[Evaluation] = lst match {
      case Nil => evals
      case "" :: tail => evals
      case head :: Nil => parseEval(head) :: evals
      case head :: tail => aux(tail, parseEval(head) :: evals)
    }
    val fields = toParse.split(s"$boundary").toList
    val name = fields.head
    Subject(name, rems, notes, aux(fields.tail, List()))

  }

  def main(args: Array[String]): Unit = {
    val eval1 = (LocalDate.parse("2020-11-19"), (30.0, 20.0), "Teste")
    val eval2 = (LocalDate.parse("2020-12-18"), (70.0, 15.0), "Teste")
    val subj = Subject("PPM")
    val subj1 = add_evaluation(subj, eval1)
    val subj2 = add_evaluation(subj1, eval2)
    //println(calculate_FinalGrade(subj2))
     val i = calculate_FinalGrade2(0.5 , List(20)) (0.25, List(15))_
  }


}
