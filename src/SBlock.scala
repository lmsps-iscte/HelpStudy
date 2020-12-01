import java.time.temporal.ChronoUnit
import java.time.{LocalDate, LocalTime}

case class SBlock(date: LocalDate, start_time: LocalTime, end_time: LocalTime, title: String, cunit: String) {

  //GIVES THE DURATION OF A BLOCK OF TIME

  def duration(): Long = SBlock.duration(this)

  //CHECKS IF A BLOCKS OVERLAYS ANOTHER

  def isOverlay(sblock: SBlock): Boolean = SBlock.isOverlay(this, sblock)

  //CHECKS IF BLOCK OF TIME IS TOO LONG (OVER 90 MINUTES)

  def isTooLong(): Boolean = SBlock.isTooLong(this)

}

object SBlock {

  //GIVES THE DURATION OF A BLOCK OF TIME

  def duration(sblock: SBlock): Long = sblock.start_time.until(sblock.end_time, ChronoUnit.MINUTES)

  //CHECKS IF A BLOCKS OVERLAYS ANOTHER

  def isOverlay(block: SBlock, sblock: SBlock): Boolean = if(block.date == sblock.date
    && ((sblock.start_time.isAfter(block.start_time) && sblock.start_time.isBefore(block.end_time)) ||
    sblock.start_time == block.start_time)) true else false

  //CHECKS IF BLOCK OF TIME IS TOO LONG (OVER 90 MINUTES)

  def isTooLong(sblock: SBlock): Boolean = if(sblock.duration() > 90.longValue()) true else false

}
