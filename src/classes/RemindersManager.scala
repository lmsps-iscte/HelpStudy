package classes

import classes.RemindersManager.{Reminder, Reminder_List}

import java.time.{LocalDate, Period}
import scala.annotation.tailrec
import scala.math._

case class RemindersManager(lst_rem: Reminder_List) {

  def addReminder(new_rem: Reminder): RemindersManager = RemindersManager.addReminder(this, new_rem)

  def delReminder(title: String): RemindersManager = RemindersManager.delReminder(this, title)

  def searchReminder(title: String): Option[Reminder] = RemindersManager.searchReminder(this, title)

  def sort_by_priority(): RemindersManager = RemindersManager.sort_by_priority(this)

  def sort_by_date(): RemindersManager = RemindersManager.sort_by_date(this)

  def getRemindersbyCUnit(cunit: String): List[Reminder] = RemindersManager.getRemindersbyCUnit(this, cunit)

  def getReminder(title: String, cunit: String): Reminder =  RemindersManager.getReminder(this, title, cunit)

  override def toString: String = RemindersManager.toString(lst_rem)
}

object RemindersManager {
  type Title = String
  type Body = String
  type Priority = Int
  type Date = LocalDate
  type Points = Double
  type CUnit = String
  type Reminder = (Title, Body, Priority, Date, Points, CUnit)
  type Reminder_List = List[Reminder]

  val boundary = "////0xFFFF////EOF"

  //PRINT DOS LEMBRETES
  @tailrec
  def printReminders(lst: List[Reminder]) : Unit =
    lst match {
    case Nil => Nil
    case head :: tail => println(s"Title: ${head._1} Body: ${head._2} Priority: ${head._3} Date: ${head._4} CUNIT: ${head._6}"); printReminders(tail)
  }

  //-------------UPDATE------------------------
  def addReminder(rem_man: RemindersManager, new_rem: Reminder): RemindersManager = {
    if(new_rem._3 < 1 || new_rem._3 > 4) {
      throw new IllegalArgumentException("Erro: A priordidade deve estar entre 1 e 4!")
    }
    else {
      RemindersManager(new_rem :: rem_man.lst_rem)
    }
  }

  def delReminder(rem_man: RemindersManager, title: String): RemindersManager = {
    searchReminder(rem_man, title) match {
      case Some(b) => RemindersManager(rem_man.lst_rem.filter(r => !r._1.equals(title)))
      case None => throw new IllegalArgumentException("Erro: Esse lembrete não existe")
    }
  }

  def getRemindersbyCUnit(rem_man: RemindersManager, cunit: String): List[Reminder] = {
    rem_man.lst_rem.filter(_._6 equalsIgnoreCase cunit)
  }

  def getReminder(rem_man: RemindersManager, title: String, cunit: String): Reminder = {
    rem_man.lst_rem.filter(_._1 equalsIgnoreCase title).filter(_._6 equalsIgnoreCase cunit).head
  }

  //------------AUXILIAR------------------------------
  def searchReminder(rem_man: RemindersManager, title: String): Option[Reminder] = {
    Option((rem_man.lst_rem filter (r => r._1.equals(title))).head)
  }

  //-------------SORTING------------------------------
  def sort_by_priority(rem_man: RemindersManager): RemindersManager = {
    RemindersManager(rem_man.lst_rem.sortBy(_._3).reverse)
  }

  def sort_by_date(rem_man: RemindersManager): RemindersManager = {
    RemindersManager(rem_man.lst_rem.sortBy(_._4))
  }

  //ALGORITMO MATEMÁTICO QUE COMBINA A PRIORIDADE COM A DATA DO REMINDER
  def sort_smart(rem_man: RemindersManager, f_name: String): RemindersManager = {
    val f1_name = f_name.toUpperCase
    f1_name match {
      case "SIGMOID" => RemindersManager(smart_list(rem_man.lst_rem)(points_sigmoid).sortBy(_._5).reverse)
      case "GAUSSIAN" => RemindersManager(smart_list(rem_man.lst_rem)(points_gaussian).sortBy(_._5).reverse)
      case _ => throw new IllegalArgumentException("ERROR: FUNCTION DOES NOT EXIST")
    }
  }

  def smart_list(rems: List[Reminder])(dist_func: (Reminder, Int) => Double): List[Reminder] = rems match {
      case Nil => Nil
      case head :: tail => if (head._4.isBefore(LocalDate.now))
        (head._1, head._2, head._3, head._4, 0.0, head._6) :: smart_list(tail)(dist_func)
      else
        (head._1, head._2, head._3, head._4, points(head)(dist_func), head._6) :: smart_list(tail)(dist_func)
    }

  def points(rem: Reminder)(dist_func: (Reminder, Int) => Double): Double = {
    val today = LocalDate.now()
    val days = Period.between(today, rem._4).getDays
    dist_func(rem, days)
  }

  def points_gaussian(rem: Reminder, days: Int): Double = {
    val variancia = 5
    (1/sqrt(2*Math.PI*variancia)*Math.exp(-pow(days,2)/(2*variancia)) + 1) * rem._3

  }

  def points_sigmoid(rem: Reminder, days: Int): Double = {
    rem._3 * (2 * 1/(1 + Math.exp(days)) + 1)
  }

  def toString(lst_rem: List[Reminder]): String = lst_rem match {
    case Nil => s""
    case head :: Nil => s"${head._1} $boundary ${head._2} $boundary ${head._3},${head._4},${head._5},${head._6}"
    case head :: tail => s"${head._1} $boundary ${head._2} $boundary ${head._3},${head._4},${head._5},${head._6}$boundary" +
      s"\\n${toString(tail)}"
  }

  def parseItem(item: String): (String, String, Int, LocalDate, Double, String) = {
    val title = item.split(boundary)(0).trim
    val body = item.split(boundary)(1).trim
    val priority = item.split(boundary)(2).split(",")(0).trim.toInt
    val date = LocalDate.parse(item.split(boundary)(2).split(",")(1))
    val points = item.split(boundary)(2).split(",")(2).toDouble
    val cunit = item.split(boundary)(2).split(",")(3)
    (title, body,priority,date,points, cunit)
  }

  def fromString(toParse: String): RemindersManager = {
    @tailrec
    def aux(rems_man: RemindersManager, items: List[String]): RemindersManager = items match {
      case Nil => rems_man
      case "" :: tail => rems_man
      case head :: Nil => rems_man.addReminder(parseItem(head))
      case head :: tail => aux(rems_man.addReminder(parseItem(head)),tail)
    }
    aux(RemindersManager(List()),toParse.split("\\\\n").toList)
  }

  def main(args: Array[String]): Unit = {
    val rems: RemindersManager = RemindersManager(List(("Titulo1", "Body1", 3, LocalDate.now(), 0.0, "Pessoal"),
      ("Titulo2", "Body2", 1,LocalDate.parse("2020-11-20") , 0.0, "PPM"), ("Titulo3", "Body3", 1, LocalDate.parse("2020-11-23"), 0.0, "ES"),
      ("Titulo4", "Body4", 4, LocalDate.parse("2020-11-30"), 0.0, "PPM"), ("Titulo5", "Body5", 4, LocalDate.parse("2020-11-24"), 0.0, "PPM")))
    //println(rems.sort_by_priority())
    //println(rems.sort_by_date())
    //println(rems.addReminder(("Titulo4", "Body4", 4, LocalDate.now())))
    //println(rems.delReminder("Titulo20"))
    //println(rems.searchReminder("Titulo7"))
    //printReminders(rems.lst_rem)
   // println(points_gaussion(rems.lst_rem.head))
    //println(sort_smart(rems))
    print("ESTE É REMS ORIGINAL "+rems)
    val rems1 = rems.toString()
    print("ESTE É O TOSTRING "+rems1)
    val rems2 = fromString(rems1)
    print("ESTE É O FROM STRING "+rems2)
  }


}

