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

  def delEvaluation(eval: String): Subject = Subject.del_evaluation(this, eval)

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
    val rem = ("Avaliação: " + eval._3, "", 4, eval._1, 0.0)
    associate_reminder(subj, rem)
    Subject(subj.name, subj.rems, subj.notes, eval :: subj.evals)
  }

  def del_evaluation(subj: Subject, title: Title): Subject = {
    searchEvaluation(subj, title) match {
      case Some(b) => Subject(subj.name, subj.rems, subj.notes, subj.evals.filter(e => !e._3.equals(title)))
      case None => /*System.err.println("Erro: Esse lembrete não existe")*/
        throw new IllegalArgumentException("Erro: Esse lembrete não existe")
    }
  }

  def searchEvaluation(subj: Subject, title: String): Option[Evaluation] = {
    Option((subj.evals filter (e => e._3.equals(title))).head)
  }

  def calculate_FinalGrade(subj: Subject): Double = {
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
    s"${subj.name} $boundary ${aux(subj.evals)}"
  }

  def parseEval(head: String): (Date, (Percent_Grade, Grade), Title) = {
    System.out.println("Cheguei2")
    System.out.println(head)
    val fields = head.split(",")
    val date = LocalDate.parse(fields.head.trim)
    val percent_Grade = fields(1).toDouble
    System.out.println(percent_Grade)
    val grade = fields(2).toDouble //ATENCAO---------!!!!!!!!!
    System.out.println(grade)
    val title = fields.last
    System.out.println(title)
    (date, (percent_Grade, grade), title)
  }

  def fromString(toParse: String/*, rems: List[Reminder], notes: List[Note]*/) : Subject = {
    System.out.println(toParse)

    @tailrec
    def aux(lst: List[String], evals: List[Evaluation]): List[Evaluation] = lst match {
      case Nil => evals
      case "" :: tail => evals
      case head :: Nil => parseEval(head) :: evals
      case head :: tail => aux(tail, parseEval(head) :: evals)
    }
    System.out.println("Olá")
    val fields = toParse.split(s"$boundary").toList
    System.out.println("Olá")
    System.out.println(fields)
    val name = fields.head
    System.out.println(name)
    Subject(name/*, rems, notes,*/, List(), List(), aux(fields.tail, List()))

  }

  def main(args: Array[String]): Unit = {
    val eval1 = (LocalDate.parse("2020-11-19"), (30.0, 20.0), "Teste")
    val eval2 = (LocalDate.parse("2020-12-18"), (70.0, 15.0), "Teste")
    val subj = Subject("PPM")
    println(subj.associate_reminder(("Titulo1", "Body1", 3, LocalDate.now(), 0.0)))
    val subj1 = add_evaluation(subj, eval1)
    val subj2 = add_evaluation(subj1, eval2)
    println(subj2.evals)
    //println(calculate_FinalGrade(subj2))
    println(calculate_FinalGrade2(0.5 , List(20, 15)) (0.25, List(15))(0.25, List(15)))
    val i = calculate_FinalGrade2(0.5 , List(20)) (0.25, List(15))_
    println(i(0.25, List(15)))
  }


}
