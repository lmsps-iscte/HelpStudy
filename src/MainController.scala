
import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.Button
import javafx.scene.layout.Pane
import javafx.stage.Stage

class MainController {

  @FXML
  private var scheduleButton: Button = _

  @FXML
  private var subjectsButton: Button = _

  @FXML
  private var deckButton: Button = _

  @FXML
  private var remindersButton: Button = _

  @FXML
  private var notesButton: Button = _

  @FXML
  private var homeButton: Button = _


  def remindersButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource(("RemindersController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()
    //val scene = new Scene(mainViewRoot)
    remindersButton.getScene().setRoot(mainViewRoot)
    //val aboutStage: Stage = new Stage()
    //aboutStage.setScene(scene)
    //aboutStage.show()
  }

  def scheduleButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource(("ScheduleController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()
    scheduleButton.getScene().setRoot(mainViewRoot)
  }

  def subjectsButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource(("SubjectsManagerController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()
    subjectsButton.getScene().setRoot(mainViewRoot)
  }

  def notesButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource(("NotesManagerController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()
    notesButton.getScene().setRoot(mainViewRoot)
  }

  /*@FXML
  def handleMenuButton(event: ActionEvent): Unit = {
    if(event.getSource() == aboutButton)
      aboutPane.toFront()
    else if(event.getSource() == notesButton)
      notesPane.toFront()

  }*/


}
