import java.time.LocalDate
import Notebook.Note
import RemindersManager.Reminder
import Subject.Evaluation

import scala.annotation.tailrec


//val rems_default = List()

case class Subject(name: String, rems: List[Reminder] = List(), notes: List[Note] = List(), evals: List[Evaluation] = List()) {

  /*def this(m: Map[String, String]) =
    this(m.getOrElse("rems", rems_default))*/


  def associate_reminder(rem: Reminder): Subject = Subject.associate_reminder(this, rem)

  def associate_note(note: Note): Subject = Subject.associate_note(this, note)

  def add_evaluation(eval: Evaluation): Subject = Subject.add_evaluation(this, eval)

  def calculate_FinalGrade(): Double = Subject.calculate_FinalGrade(this)

  override def toString: String = Subject.toString(this)

}

object Subject {

  type Grade = Int
  type Percent_Grade = Double
  type Date = LocalDate
  type Evaluation = (Date, (Percent_Grade, Grade))
  val boundary = "////0xFFFF////EOF"

  def associate_reminder(subj: Subject, rem: Reminder): Subject = {
    Subject(subj.name, rem :: subj.rems, subj.notes, subj.evals)
  }

  
  def associate_note(subj: Subject, note: Note): Subject = {
    Subject(subj.name, subj.rems, note :: subj.notes, subj.evals)
  }

  def add_evaluation(subj: Subject, eval: Evaluation): Subject = {
    val rem = ("Avaliação: " + subj.name, "", 4, eval._1, 0.0)
    associate_reminder(subj, rem)
    Subject(subj.name, subj.rems, subj.notes, eval :: subj.evals)
  }

  def calculate_FinalGrade(subj: Subject): Double = {
    val x = subj.evals.map( a => a._2)
    val result = x.map{ case(a,b) => a*b/100}
    result.foldRight(0.0)(_+_)
  }


  def product(xs: List[Int]): Int = (xs foldLeft 1) (_*_)
  def sum(xs: List[Int]): Int = (xs foldLeft 1) (_+_)

  def toString(subj: Subject) : String = {
    def aux(evals: List[Evaluation]): String = evals match {
      case Nil => s""
      case head :: tail => s"${head._1},${head._2._1},${head._2._2} $boundary ${aux(tail)}"
    }
    s"${subj.name} $boundary ${aux(subj.evals)}"
  }

  def parseEval(head: String): (Date, (Percent_Grade, Grade)) = {
    val fields = head.split(",")
    val date = LocalDate.parse(fields.head)
    val percent_Grade = fields(1).toDouble
    val grade = fields.last.toInt
    (date, (percent_Grade, grade))
  }

  def fromString(toParse: String, rems: List[Reminder], notes: List[Note]) : Subject = {
    @tailrec
    def aux(lst: List[String], evals: List[Evaluation]): List[Evaluation] = lst match {
      case item :: Nil => parseEval(item) :: evals
      case head :: tail => aux(tail, parseEval(head) :: evals)
    }
    val fields = toParse.split(s"$boundary").toList
    val name = fields.head
    Subject(name, rems, notes, aux(fields.tail, List()))
  }

  def main(args: Array[String]): Unit = {
    val eval1 = (LocalDate.parse("2020-11-19"), (30.0, 20))
    val eval2 = (LocalDate.parse("2020-12-18"), (70.0, 15))
    val subj = Subject("PPM")
    println(subj.associate_reminder(("Titulo1", "Body1", 3, LocalDate.now(), 0.0)))
    val subj1 = add_evaluation(subj, eval1)
    val subj2 = add_evaluation(subj1, eval2)
    println(subj2.evals)
    println(calculate_FinalGrade(subj2))
  }


}
