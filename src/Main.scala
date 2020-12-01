import java.nio.file.Paths
import java.time.{LocalDate, LocalTime}

import About.about

import scala.annotation.tailrec
import scala.io.StdIn.readLine
import scala.sys.exit

object Main {

  def showPrompt(): Unit = {
    print("\nWELCOME TO THE APP HELP STUDY!")
    print("\nCHOOSE ONE OF THE FOLLOWING OPTIONS:")
    print("\nABOUT, DECK, NOTEBOOK, REMINDERS, SCHEDULE, SUBJECTS OR QUIT")
  }

  def getUserInput: String = {
    readLine.trim.toUpperCase
  }

  @tailrec
  def mainLoop(): Unit = {
    print("\nINPUT: ")
    val input = getUserInput
    input match {
      case "ABOUT" => print(about())
      case "DECK" => //deckLoop()
      case "NOTEBOOK" => notebookLoop(Notebook(Nil))
      case "REMINDERS" => remindersLoop()
      case "SCHEDULE" => scheduleLoop(Schedule(Nil, 50))
      case "SUBJECTS" => //subjectsLoop()
      case "QUIT" => exit(0)
      case _ =>
    }
    mainLoop()
  }

  def notebookLoop(notebook: Notebook): Unit = {
    print("\nCHOOSE ONE OF THE FOLLOWING OPTIONS:")
    print("\nADD_NOTE, NOTES_BY_CUNIT, IMPORT_FROM_FILE, EXPORT_TO_FILE, PRINT_NOTES, " +
      "SORT_NOTES OR BACK")
    print("\nINPUT: ")
    val input = getUserInput
    input match {
      case "ADD_NOTE" => print("\nTITLE: ")
        val title = getUserInput
        print("\nBODY: ")
        val body = getUserInput
        print("\nCURRICULAR UNIT: ")
        val cunit = getUserInput
        notebookLoop(notebook.addNote(title,body,cunit))

      case "NOTES_BY_CUNIT" => print("\nCURRICULAR UNIT: ")
        val cunit = getUserInput
        print("\n"+notebook.getNotesbyCUnit(cunit))

      case "IMPORT_FROM_FILE" => print("\nFILE PATH: ")
        val fpath = getUserInput
        print("\nCURRICULAR UNIT: ")
        val cunit = getUserInput
        notebookLoop(notebook.importFromFile(fpath,cunit))

      case "EXPORT_TO_FILE" => print("\nNOTE INDEX (EX:. 0:) ")
        val note_index = getUserInput.toInt
        notebook.exportToFile(note_index)

      case "PRINT_NOTES" => print("\nPRESS ENTER FOR ALL OR SPECIFY CURRICULAR UNIT: ")
        val choice = getUserInput
        print("\n"+notebook.printNotes(choice))

      case "SORT_NOTES" => print("\nWRITE TITLE FOR SORT BY TITLE OR PRESS ENTER FOR SORT BY CUNIT: ")
        val choice = getUserInput
        notebookLoop(notebook.sortNoteBy(choice))

      case "BACK" => mainLoop()
      case "QUIT" => exit(0)
      case _ =>
    }
    notebookLoop(notebook)
  }

  def scheduleLoop(schedule: Schedule): Unit = {
    print("\nCHOOSE ONE OF THE FOLLOWING OPTIONS:")
    print("\nADD_BLOCK, BLOCKS_BY_DAY, TIME_CUNIT_L7DAYS, TIME_CUNIT_L30DAYS, " +
      "TIME_SPENT_TODAY, FATIGUE_ALERT OR BACK")
    print("\nINPUT: ")
    val input = getUserInput
    input match {
      case "ADD_BLOCK" => print("\nDATE (EX:. 2020-11-20): ")
        val date = LocalDate.parse(getUserInput)
        print("\nSTART TIME (EX:. 10:30): ")
        val start_time = LocalTime.parse(getUserInput)
        print("\nEND TIME (EX:. 11:30): ")
        val end_time = LocalTime.parse(getUserInput)
        print("\nTITLE: ")
        val title = getUserInput
        print("\nCURRICULAR UNIT: ")
        val cunit = getUserInput
        scheduleLoop(schedule.addSBlock(SBlock(date, start_time, end_time, title, cunit)))

      case "BLOCKS_BY_DAY" => print("\nDATE (EX:. 2020-11-20): ")
        val date = LocalDate.parse(getUserInput)
        schedule.blocksByDay(date)

      case "TIME_CUNIT_L7DAYS" => print("\nCURRICULAR UNIT: ")
        val cunit = getUserInput
        print("\nON THE LAST 7 DAYS YOU HAVE INVESTED "+schedule.timebyCUnitL7Days(cunit)+" MINUTES")

      case "TIME_CUNIT_L30DAYS" => print("\nCURRICULAR UNIT: ")
        val cunit = getUserInput
        print("\nON THE LAST 30 DAYS YOU HAVE INVESTED "+schedule.timebyCUnitL30Days(cunit)+" MINUTES")

      case "TIME_SPENT_TODAY" => print("\nTODAY YOU WILL INVEST "+schedule.timeSpentBySchoolToday()+" MINUTES OF YOUR DAY")
      case "FATIGUE_ALERT" => print("\n"+schedule.fatigueAlert())
      case "BACK" => mainLoop()
      case "QUIT" => exit(0)
      case _ =>
    }
    scheduleLoop(schedule)
  }

  def remindersLoop(): Unit = {
    /*print("\nESCOLHA UMA DAS SEGUINTES OPÇÕES:")
    print("\nADD, DELETE, SEARCH, SORT BY PRIORITY, SORT BY DATE OU BACK")
    print("\nINPUT: ")
    val input = getUserInput
    input match {



      case "BACK" => mainLoop()
      case _ =>
    }*/
    val rems: RemindersManager = RemindersManager(List(("Titulo1", "Body1", 3, LocalDate.now(), 0.0),
      ("Titulo2", "Body2", 1,LocalDate.parse("2020-11-20") , 0.0), ("Titulo3", "Body3", 1, LocalDate.parse("2020-11-23"), 0.0),
      ("Titulo4", "Body4", 4, LocalDate.parse("2020-11-30"), 0.0), ("Titulo5", "Body5", 4, LocalDate.parse("2020-11-24"), 0.0)))
    RemindersMenu.mainLoop_Reminders(rems)
  }

  //Load hs.state file
  def loadState(): Unit = {

  }

  //Initialize all the needed variables for the program and generate the hs.state file
  def initHS(): Unit = {

  }

  def main(args: Array[String]): Unit = {
    showPrompt()
    mainLoop()
    if (Paths.get(System.getProperty("user.dir", "hs.state")).toFile.exists())
      loadState()
    else
      initHS()
  }
}
