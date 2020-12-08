import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalTime}

import scala.annotation.tailrec

case class Schedule(sblocks: List[SBlock], school_percent: Int) {

  //ADDS ONE BLOCK OF TIME TO THE SCHEDULE
  def addSBlock(sblock: SBlock): Schedule = Schedule.addSBlock(this, sblock)

  //GIVES ONE SBLOCK OBJECT WITH A GIVEN TITLE
  def getSBlockByName(title: String): SBlock = Schedule.getSBlockByName(this, title)

  //GIVES THE LIST OF BLOCKS OF ONE SPECIFIC DAY

  def blocksByDay(date: LocalDate): List[SBlock] = Schedule.blocksByDay(this, date)

  //GIVES THE TIME SPENT ON A SPECIFIC CUNIT ON THE LAST 7 DAYS

  def timebyCUnitL7Days(cunit: String): Long = Schedule.timebyCUnitL7Days(this, cunit)

  //GIVES THE TIME SPENT ON A SPECIFIC CUNIT ON THE LAST 30 DAYS

  def timebyCUnitL30Days(cunit: String): Long = Schedule.timebyCUnitL7Days(this, cunit)

  //GIVES THE TIME SPENT ON SCHOOL STUFF TODAY

  def timeSpentBySchoolToday(): Long = Schedule.timeSpentBySchoolToday(this)

  //GIVES THE SUM OF TIME SPENT ON A SPECIFIC CUNIT

  def sumTime(cunit: String, f: (Schedule, String) => List[SBlock]): Long =
    Schedule.sumTime(this, cunit, f)

  //GIVES THE SUM OF TIME OF ALL CUNITS

  def sumTimeV1(f: Schedule => List[SBlock]): Long = Schedule.sumTimeV1(this, f)

  //GIVES THE LIST OF BLOCKS OF TIME OF A SPECIFIC CUNIT ON THE LAST 7 DAYS

  def last7Days(cunit: String): List[SBlock] = Schedule.last7Days(this, cunit)

  //GIVES THE LIST OF BLOCKS OF TIME OF A SPECIFIC CUNIT ON THE LAST 30 DAYS

  def last30Days(cunit: String): List[SBlock] = Schedule.last30Days(this, cunit)

  //GIVES THE LIST OF BLOCKS OF TIME OF TODAY

  def today(): List[SBlock] = Schedule.today(this)

  //CHECKS IF BLOCK OF TIME WILL OVERLAY ONE THAT ALREADY EXISTS

  def willOverlay(sblock: SBlock): Boolean = Schedule.willOverlay(this, sblock)

  //ALERTS USER IF IT IS NOT FOLLOWING THE FUN - STUDY RATIO
  def fatigueAlert(): String = Schedule.fatigueAlert(this)

  //PRINTS SCHEDULE TO A FILE

  def printToFile(): Unit = Schedule.printToFile(this)

  override def toString: String = Schedule.toString(sblocks, school_percent)


  //TALVEZ JÁ NÃO SEJA PRECISO!!! CONFIRMAR!!!

  //def timebyCUnit(cunit: String): Long = Schedule.timebyCUnit(this, cunit)

  //def isProblematic(sblock: SBlock): Boolean = Schedule.isProblematic(this, sblock)

}

object Schedule {

  val boundary = "////0xFFFF////EOF"

  //ADDS ONE BLOCK OF TIME TO THE SCHEDULE

  def addSBlock(schedule: Schedule, sblock: SBlock): Schedule = if(sblock.isTooLong()) {
    println("YOU SHOULD NOT INSERT THIS BLOCK ON THE SCHEDULE BECAUSE IT IS TOO LONG (MORE THAN 90 MINUTES)!")
    schedule
  } else if(schedule.willOverlay(sblock)) {
    println("YOU CANNOT INSERT THIS BLOCK ON THE SCHEDULE BECAUSE IT WILL OVERLAY ANOTHER!")
    schedule
  } else Schedule(sblock :: schedule.sblocks, schedule.school_percent)

  //GIVES ONE SBLOCK OBJECT WITH A GIVEN TITLE
  def getSBlockByName(schedule: Schedule, title: String): SBlock =
    schedule.sblocks.filter(block => block.title == title).head

  //GIVES THE LIST OF BLOCKS OF ONE SPECIFIC DAY

  def blocksByDay(schedule: Schedule, date: LocalDate): List[SBlock] =
    schedule.sblocks.filter(x => x.date == date)

  //GIVES THE TIME SPENT ON A SPECIFIC CUNIT ON THE LAST 7 DAYS

  def timebyCUnitL7Days(schedule: Schedule, cunit: String): Long = sumTime(schedule, cunit, last7Days)

  //GIVES THE TIME SPENT ON A SPECIFIC CUNIT ON THE LAST 30 DAYS

  def timebyCUnitL30Days(schedule: Schedule, cunit: String): Long = sumTime(schedule, cunit, last30Days)

  //GIVES THE TIME SPENT ON SCHOOL STUFF TODAY

  def timeSpentBySchoolToday(schedule: Schedule): Long = sumTimeV1(schedule, today)

  //GIVES THE SUM OF TIME SPENT ON A SPECIFIC CUNIT

  def sumTime(schedule: Schedule, cunit: String, f: (Schedule, String) => List[SBlock]): Long =
    schedule.sblocks match {
      case Nil => 0
      case _ => (f(schedule,cunit).map(x => x.duration()) foldRight 0.longValue()) (_ + _)
    }

  //GIVES THE SUM OF TIME

  def sumTimeV1(schedule: Schedule, f: Schedule => List[SBlock]): Long =
    schedule.sblocks match {
      case Nil => 0
      case _ => (f(schedule).map(x => x.duration()) foldRight 0.longValue()) (_ + _)
    }

  //GIVES THE LIST OF BLOCKS OF TIME OF A SPECIFIC CUNIT ON THE LAST 7 DAYS

  def last7Days(schedule: Schedule, cunit: String): List[SBlock] =
    schedule.sblocks.filter(x => (x.cunit == cunit) &&
      (x.date.isEqual(LocalDate.now().minusDays(7)) ||
        (x.date.isAfter(LocalDate.now().minusDays(7)) &&
          x.date.isBefore(LocalDate.now()))))

  //GIVES THE LIST OF BLOCKS OF TIME OF A SPECIFIC CUNIT ON THE LAST 30 DAYS

  def last30Days(schedule: Schedule, cunit: String): List[SBlock] =
    schedule.sblocks.filter(x => x.cunit == cunit &&
      (x.date.isEqual(LocalDate.now().minusDays(30)) ||
        (x.date.isAfter(LocalDate.now().minusDays(30)) &&
          x.date.isBefore(LocalDate.now()))))

  //GIVES THE LIST OF BLOCKS OF TIME OF TODAY

  def today(schedule: Schedule): List[SBlock] =
    schedule.sblocks.filter(x => x.date.isEqual(LocalDate.now()))

  //CHECKS IF BLOCK OF TIME WILL OVERLAY ONE THAT ALREADY EXISTS

  def willOverlay(schedule: Schedule, sblock: SBlock): Boolean = {
    @tailrec
    def aux(sbl: List[SBlock], sb: SBlock): Boolean = sbl match {
      case Nil => false
      case head :: Nil => if(head.isOverlay(sblock)) true else false
      case head :: tail =>if(head.isOverlay(sblock)) true else aux(tail, sb)
    }
    aux(schedule.sblocks, sblock)
  }

  //ALERTS USER IF IT IS NOT FOLLOWING THE FUN - STUDY RATIO
  def fatigueAlert(schedule: Schedule): String = {
    val bad = "TODAY YOU ARE SPENDING TOO MUCH TIME WITH SCHOOL! IF POSSIBLE PLAN YOUR DAY DIFFERENTLY TO BE MORE PRODUCTIVE! :)"
    val good = "YOUR DAY HAS A GOOD BALANCE BETWEEN SCHOOL AND FUN TIME! HAVE A NICE DAY! :)"
    if(timeSpentBySchoolToday(schedule) > 960.longValue()*(schedule.school_percent/100.floatValue()))
      bad
    else good
  }

  //PRINTS SCHEDULE TO A FILE

  def printToFile(schedule: Schedule): Unit = {
    val file = "MySchedule.txt"
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))
    for (x <- schedule.sblocks) {
      writer.write(x + "\n")
    }
    writer.close()
  }

  def toString(sblocks: List[SBlock], school_percent: Int): String = sblocks match {
    case Nil => s"$school_percent"
    case head :: Nil => s"$school_percent,${head.date},${head.start_time},${head.end_time} $boundary ${head.title} " +
      s"$boundary ${head.cunit}"
    case head :: tail => s"$school_percent,${head.date},${head.start_time},${head.end_time}  $boundary ${head.title} " +
      s"$boundary ${head.cunit} \n ${toString(tail, school_percent)}"
  }

  def parseItem(item: String): SBlock = {
    val date = LocalDate.parse(item.split(",")(1))
    val start_time = LocalTime.parse(item.split(",")(2))
    val end_time = LocalTime.parse(item.split(",")(3).split(boundary)(0).trim)
    val title = item.split(boundary)(1).trim
    val cunit = item.split(boundary)(2).trim
    SBlock(date, start_time, end_time, title, cunit)
  }

  def fromString(toParse: String): Schedule = {
    @tailrec
    def aux(schedule: Schedule, items: List[String]): Schedule = items match {
      case head :: Nil => schedule.addSBlock(parseItem(head))
      case head :: tail => aux(schedule.addSBlock(parseItem(head)),tail)
    }
    aux(Schedule(List(), toParse.split(",")(0).trim.toInt), toParse.split("\n").toList)
  }

  def main(args: Array[String]): Unit = {


        val bloco1 = SBlock(LocalDate.parse("19-11-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
          LocalTime.of(9,30,0), LocalTime.of(10,30,0), "Aula TP de MC", "MC")
        val bloco2 = SBlock(LocalDate.parse("19-11-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        LocalTime.of(10,30,0), LocalTime.of(11,30,0), "Aula TP de MC", "CDSI")
        val bloco3 = SBlock(LocalDate.parse("19-11-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        LocalTime.of(11,30,0), LocalTime.of(13,0,0), "Aula TP de MC", "MC")

        val horario = Schedule(Nil,30)

        val horario1 = horario.addSBlock(bloco1)
        val horario2 = horario1.addSBlock(bloco2)
        val horario3 = horario2.addSBlock(bloco3)

//        println(horario)
//        println(horario1)
//        println(horario2)
//        println(horario3)
//        println(horario3.fatigueAlert())
//        horario2.printToFile()

    print("ESTE É O HORÁRIO 3 "+horario3+"\n")
    val horario4 = horario3.toString
    print("ESTA É O TOSTRING DO HORÁRIO 3 "+horario4+"\n")
    val horario5 = fromString(horario4)
    print("ESTE É O FROM STRING DO HORÁRIO 3 "+horario5+"\n")


  }



  //TALVEZ JÁ NÃO SEJA PRECISO!!! CONFIRMAR!!!

  /*def timebyCUnit(schedule: Schedule, cunit: String): Long = {
    @tailrec
    def aux (l: List[SBlock], c: String, acc: Long): Long = l match {
      case Nil => 0
      case head :: Nil => if(head.cunit == c) head.duration() else 0
      case head :: tail => if(head.cunit == c) aux(tail, c, acc + head.duration()) else aux(tail, c, acc)
    }
    aux(schedule.sblocks, cunit, 0)
  }*/

  //TALVEZ JÁ NÃO SEJA PRECISO!!! CONFIRMAR!!!

  /*  def isProblematic(schedule: Schedule, sblock: SBlock): Boolean = {
      @tailrec
      def aux (l: List[SBlock], s: SBlock): Boolean = l match {
        case Nil => false
        case head :: Nil => if(head.isOverlay(s)) true else false
        case head :: tail => if(head.isOverlay(s)) true else aux(tail, s)
      }
      aux(schedule.sblocks, sblock)
    }*/

  //TALVEZ JÁ NÃO SEJA PRECISO!!! CONFIRMAR!!!

  /*def addSBlock(schedule: Schedule, sblock: SBlock): Schedule = {
    @tailrec
    def aux(l: List[SBlock], s: SBlock, stock: List[SBlock]): Schedule = l match {
      case Nil => Schedule(sblock :: stock)
      case head :: Nil => if(head.isOverlay(s)) {
        println("Não pode inserir este bloco no horário porque irá existir uma sobreposição!")
        Schedule(l)
      } else if(head.isTooLong()) {
        println("Não deve inserir este bloco no horário porque é muito longo!")
        Schedule(l)
      } else Schedule(s::stock)
      case head :: tail => if(head.isOverlay(s)) {
        println("Não pode inserir este bloco no horário porque irá existir uma sobreposição!")
        Schedule(l)
      } else aux(tail, s, stock)
    }
    aux(schedule.sblocks, sblock, schedule.sblocks)
  }*/
}