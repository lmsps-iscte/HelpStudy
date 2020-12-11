package classes

import java.time.{LocalDate, LocalTime}
import scala.annotation.tailrec

case class Schedule(sblocks: List[SBlock], school_percent: Int) {

  //ADDS ONE BLOCK OF TIME TO THE SCHEDULE

  def addSBlock(sblock: SBlock): Schedule = Schedule.addSBlock(this, sblock)

  //GIVES ONE SBLOCK OBJECT WITH A GIVEN TITLE

  def getSBlockByName(title: String): SBlock = Schedule.getSBlockByName(this, title)

  //REMOVES A GIVEN SBLOCK

  def removeSBlock(sblock: SBlock): Schedule = Schedule.removeSBlock(this, sblock)

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

  def fatigueAlert(): Boolean = Schedule.fatigueAlert(this)

  //UPDATES RATIO

  def updateRatio(value: Int): Schedule = Schedule.updateRatio(this, value)

  //OVERRIDES THE ORIGINAL TOSTRING METHOD

  override def toString: String = Schedule.toString(sblocks, school_percent)

}

object Schedule {

  val boundary = "////0xFFFF////EOF"

  //ADDS ONE BLOCK OF TIME TO THE SCHEDULE

  def addSBlock(schedule: Schedule, sblock: SBlock): Schedule = Schedule(sblock :: schedule.sblocks, schedule.school_percent)

  //GIVES ONE SBLOCK OBJECT WITH A GIVEN TITLE

  def getSBlockByName(schedule: Schedule, title: String): SBlock =
    schedule.sblocks.filter(block => block.title == title).head

  //REMOVES A GIVEN SBLOCK

  def removeSBlock(schedule: Schedule, sblock: SBlock): Schedule =
    Schedule(schedule.sblocks.filter(s => !s.equals(sblock)), schedule.school_percent)

  //GIVES THE LIST OF BLOCKS OF ONE SPECIFIC DAY

  def blocksByDay(schedule: Schedule, date: LocalDate): List[SBlock] =
    schedule.sblocks.filter(x => x.date == date).sortBy(_.start_time)

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
      case _ => (f(schedule, cunit).map(x => x.duration()) foldRight 0.longValue()) (_ + _)
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
      case head :: Nil => if (head.isOverlay(sblock)) true else false
      case head :: tail => if (head.isOverlay(sblock)) true else aux(tail, sb)
    }

    aux(schedule.sblocks, sblock)
  }

  //ALERTS USER IF IT IS NOT FOLLOWING THE FUN - STUDY RATIO

  def fatigueAlert(schedule: Schedule): Boolean = {
    if (timeSpentBySchoolToday(schedule) > 960.longValue() * (schedule.school_percent / 100.floatValue()))
      true
    else false
  }

  //UPDATES RATIO

  def updateRatio(schedule: Schedule, value: Int): Schedule = Schedule(schedule.sblocks, value)

  //OVERRIDES THE ORIGINAL TOSTRING METHOD

  def toString(sblocks: List[SBlock], school_percent: Int): String = sblocks match {
    case Nil => s""
    case head :: Nil => s"$school_percent,${head.date},${head.start_time},${head.end_time} $boundary ${head.title} " +
      s"$boundary ${head.cunit}"
    case head :: tail => s"$school_percent,${head.date},${head.start_time},${head.end_time}  $boundary ${head.title} " +
      s"$boundary ${head.cunit} \\n ${toString(tail, school_percent)}"
  }

  //GETS AN SBLOCK FROM A STRING

  def parseItem(item: String): SBlock = {
    val date = LocalDate.parse(item.split(",")(1))
    val start_time = LocalTime.parse(item.split(",")(2))
    val end_time = LocalTime.parse(item.split(",")(3).split(boundary)(0).trim)
    val title = item.split(boundary)(1).trim
    val cunit = item.split(boundary)(2).trim
    SBlock(date, start_time, end_time, title, cunit)
  }

  //GETS A SCHEDULE FROM A STRING

  def fromString(toParse: String): Schedule = {
    @tailrec
    def aux(schedule: Schedule, items: List[String]): Schedule = items match {
      case Nil => schedule
      case "" :: tail => schedule
      case head :: Nil => schedule.addSBlock(parseItem(head))
      case head :: tail => aux(schedule.addSBlock(parseItem(head)), tail)
    }

    if (toParse.isEmpty)
      aux(Schedule(List(), 50), toParse.split("\\\\n").toList)
    else
      aux(Schedule(List(), toParse.split(",")(0).trim.toInt), toParse.split("\\\\n").toList)
  }

}