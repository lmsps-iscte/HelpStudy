import scala.io.StdIn.readLine

object SubjectMenu {

  def showPrompt(): Unit = {
    print("\nESCOLHA UMA DAS SEGUINTES OPÇÕES:")
    print("\nASSOCIATE REMINDER, ASSOCIATE NOTE, ADD EVALUATION, CALCULATE FINAL GRADE OU BACK")
    print("\nINPUT: ")
  }

  def getUserInput: String = {
    readLine.trim.toUpperCase
  }

  def mainLoop_Subject_Open(subj: Subject): Unit = {
    showPrompt()
    val input = getUserInput
    input match {
      case "ASSOCIATE REMINDER" => associate_Reminder_menu(subj)
      case "ASSOCIATE NOTE" => associate_Note_menu(subj)
      case "ADD EVALUATION" => addEvaluation_menu(subj)
      case "CALCULATE FINAL GRADE" => calcFinalGrade_menu(subj)
      case "BACK" => subj
      case _ =>
    }
    mainLoop_Subject_Open(subj)
  }

  def associate_Reminder_menu(subject: Subject): Unit = {

  }

  def associate_Note_menu(subject: Subject): Unit = {

  }

  def addEvaluation_menu(subject: Subject): Unit = {

  }

  def calcFinalGrade_menu(subject: Subject): Unit = {

  }







}
