import java.time.LocalDate

import Main.mainLoop

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object RemindersMenu {

  def showPrompt(): Unit = {
    print("\nESCOLHA UMA DAS SEGUINTES OPÇÕES:")
    print("\nADD, DELETE, SEARCH, SORT BY PRIORITY, SORT BY DATE OU BACK")
    print("\nINPUT: ")
  }

  def getUserInput: String = {
    readLine.trim.toUpperCase
  }

  def getUserInput_Original: String = {
    readLine.trim
  }

  @tailrec
  def mainLoop_Reminders(rem_man: RemindersManager): Unit = {
    showPrompt()
    val input = getUserInput
    input match {
      case "ADD" => addReminder_menu(rem_man)
      case "DELETE" => delReminder_menu(rem_man)
      case "SHOW" => showReminders_menu(rem_man)
      case "SEARCH" => searchReminder_menu(rem_man)
      case "SORT" => sortReminders_menu(rem_man)
      case "BACK" => mainLoop()
      case _ =>
    }
    mainLoop_Reminders(rem_man)
  }

  def addReminder_menu(rem_m: RemindersManager): Unit = {
    print("\nINSERT TITLE:")
    val title = getUserInput
    print("\nINSERT BODY:")
    val body = getUserInput_Original
    print("\nINSERT PRIORITY:")
    val prior = getUserInput_Original.toInt
    print("\nINSERT DATE(yyyy-mm-dd):")
    val date = LocalDate.parse(getUserInput_Original)
    val points = 0.0
    val rem = (title, body, prior, date, points)
    val rem_m1 = RemindersManager.addReminder(rem_m, rem)
    mainLoop_Reminders(rem_m1)
  }


  def delReminder_menu(rem_m: RemindersManager): Unit = {
    print("\nREMINDERS:")
    RemindersManager.printReminders(rem_m.lst_rem)
    print("\nINSERT THE TITLE OF THE REMINDER YOU WANT TO DELETE:")
    val title = getUserInput
    val rem_m1 = RemindersManager.delReminder(rem_m, title)
    mainLoop_Reminders(rem_m1)
  }


  def showReminders_menu(rem_m: RemindersManager): Unit = {
    print("\nREMINDERS:")
    RemindersManager.printReminders(rem_m.lst_rem)
    mainLoop_Reminders(rem_m)
  }

  def searchReminder_menu(rem_m: RemindersManager): Unit = {
    print("\nINSERT TITLE:")
    val title = getUserInput
    print("\n" + RemindersManager.searchReminder(rem_m, title))
    mainLoop_Reminders(rem_m)
  }

  def sortReminders_menu(rem_m: RemindersManager): Unit = {
    print("\nESCOLHA UMA DAS SEGUINTES OPÇÕES:")
    print("\nSMART SORT, SORT BY DATE, SORT BY PRIORITY OU BACK")
    print("\nINPUT: ")
    val input = getUserInput
    input match {
      case "SMART SORT" => smartSort_menu(rem_m)
      case "SMART" => smartSort_menu(rem_m)
      case "SORT BY DATE" => dateSort_menu(rem_m)
      case "DATE" => dateSort_menu(rem_m)
      case "SORT BY PRIORITY" => prioritySort_menu(rem_m)
      case "PRIORITY" => prioritySort_menu(rem_m)
      case "BACK" => mainLoop_Reminders(rem_m)
      case _ =>
    }
    mainLoop_Reminders(rem_m)
  }

  def smartSort_menu(rem_m: RemindersManager): Unit = {
    val rem_m1 = RemindersManager.sort_smart(rem_m)
    RemindersManager.printReminders(rem_m1.lst_rem)
    mainLoop_Reminders(rem_m1)
  }

  def dateSort_menu(rem_m: RemindersManager): Unit = {
    val rem_m1 = RemindersManager.sort_by_date(rem_m)
    RemindersManager.printReminders(rem_m1.lst_rem)
    mainLoop_Reminders(rem_m1)
  }

  def prioritySort_menu(rem_m: RemindersManager): Unit = {
    val rem_m1 = RemindersManager.sort_by_priority(rem_m)
    RemindersManager.printReminders(rem_m1.lst_rem)
    mainLoop_Reminders(rem_m1)
  }

}
