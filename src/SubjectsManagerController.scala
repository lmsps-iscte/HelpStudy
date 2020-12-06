import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.control.{Button, ChoiceBox, DatePicker, ListView, TextArea, TextField}
import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.stage.Stage

class SubjectsManagerController extends Initializable {

  @FXML private var subjectsListView: ListView[Subject] = _
  @FXML private var title_box: TextField = _
  @FXML private var open_button: Button = _

  def initialize(location: URL, resources: ResourceBundle): Unit = {
    val subj: Subject = Subject("PPM")
    val subj2 = subj.associate_reminder(("Titulo1", "Body1", 3, LocalDate.now(), 0.0))
    val subj1 = subj2.associate_note(("Nota 1","Corpo 1","MC"))
    val subs_list = List(subj1)
    var list_obs = FXCollections.observableArrayList[Subject]()
    subs_list.forall(list_obs.add(_))
    subjectsListView.setItems(list_obs)
    var list = FXCollections.observableArrayList[Int]()

  }

  def add_func(): Unit = {
    val title = title_box.getText.trim
    val subj: Subject = Subject(title)
    subjectsListView.getItems.add(1, subj)

  }

  def delete_func(): Unit = {
    val item = subjectsListView.getSelectionModel.getSelectedItem
    var list = subjectsListView.getItems.remove(item)
  }

  def elementClicked(): Unit = {
    val subj = subjectsListView.getSelectionModel.getSelectedItem
    val fxmlLoader = new FXMLLoader(getClass.getResource(("SubjectWindowController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()

    val subjectWindowController: SubjectWindowController = fxmlLoader.getController
    subjectWindowController.setController(subj)
    open_button.getScene().setRoot(mainViewRoot)
  }

}
