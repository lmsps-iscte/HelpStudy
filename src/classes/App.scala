package classes

import controllers._
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.{Stage, WindowEvent}

class App extends Application {

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Help Study")
    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/MainController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()

    primaryStage.addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, (_: WindowEvent) => HelpStudyApp.saveState())

  }

}


object HelpStudyApp {

  //carregar o ficheiro hs

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[App], args: _*)
  }

  def saveState(): Unit = {
    val deck = DeckController.getDeck
    Util.saveToFile(deck.toString, "deck.obj")
    val notebook = NotesController.getNotes
    Util.saveToFile(notebook.toString, "notes_paths.obj")
    val rem_man = RemindersController.getReminders
    Util.saveToFile(rem_man.toString, "reminders.obj")
    val schedule = ScheduleController.getSchedule
    Util.saveToFile(schedule.toString, "schedule.obj")
    val last_subj = SubjectWindowController.getSubject
    if (last_subj != null){
      val subjectsManager = SubjectsManagerController.getSubjectsManager
      val oneDown = subjectsManager.delSubject(subjectsManager.searchSubject(last_subj.name).get)
      Util.saveToFile(oneDown.addSubject(last_subj).toString, "subjects_paths.obj")
    } else {
      val subjectsManager = SubjectsManagerController.getSubjectsManager
      Util.saveToFile(subjectsManager.toString, "subjects_paths.obj")
    }
  }
}
