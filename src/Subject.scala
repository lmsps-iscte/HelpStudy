import java.time.LocalDate

import Notebook.Note
import RemindersManager.Reminder
import Subject.Evaluation


//val rems_default = List()

case class Subject(name: String, rems: List[Reminder] = List(), notes: List[Note] = List(), evals: List[Evaluation] = List()) {

  /*def this(m: Map[String, String]) =
    this(m.getOrElse("rems", rems_default))*/


  def associate_reminder(rem: Reminder): Subject = Subject.associate_reminder(this, rem)

  def associate_note(note: Note): Subject = Subject.associate_note(this, note)

  def add_evaluation(eval: Evaluation): Subject = Subject.add_evaluation(this, eval)

  def calculate_FinalGrade(): Double = Subject.calculate_FinalGrade(this)


}

object Subject {

  type Grade = Int
  type Percent_Grade = Double
  type Date = LocalDate
  type Evaluation = (Date, (Percent_Grade, Grade))

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


  def product(xs: List[Int]) = (xs foldLeft 1) (_*_)
  def sum(xs: List[Int]) = (xs foldLeft 1) (_+_)

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
