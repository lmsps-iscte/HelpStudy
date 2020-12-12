package controllers


import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane

class MainController {

  @FXML private var scheduleButton: Button = _
  @FXML private var subjectsButton: Button = _
  @FXML private var deckButton: Button = _
  @FXML private var remindersButton: Button = _
  @FXML private var notesButton: Button = _
  @FXML private var homeButton: Button = _
  @FXML private var big_pane: BorderPane = _

  def homeButtonClicked(): Unit = {

    val fmxlLoader = new FXMLLoader(getClass.getResource("../resources/HomeView.fxml"))
    big_pane.setCenter(fmxlLoader.load)

  }

  def scheduleButtonClicked(): Unit = {

    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/ScheduleController.fxml"))
    big_pane.setCenter(fxmlLoader.load)

  }

  def subjectsButtonClicked(): Unit = {

    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/SubjectsManagerController.fxml"))
    big_pane.setCenter(fxmlLoader.load)

    val controller_child: SubjectsManagerController = fxmlLoader.getController
    controller_child.setParent(this)

  }

  def deckButtonClicked(): Unit = {

    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/DeckController.fxml"))
    big_pane.setCenter(fxmlLoader.load)

  }

  def remindersButtonClicked(): Unit = {

    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/RemindersController.fxml"))
    big_pane.setCenter(fxmlLoader.load)

  }

  def notesButtonClicked(): Unit = {

    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/NotesManagerController.fxml"))
    big_pane.setCenter(fxmlLoader.load)

  }



  def getMainPane: BorderPane = {
    big_pane
  }


}
