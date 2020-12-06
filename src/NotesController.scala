import java.net.URL
import java.util.ResourceBundle

import Notebook.Note
import javafx.collections.FXCollections
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{Button, Label, ListView, TextArea, TextField}
import javafx.scene.layout.{AnchorPane, BorderPane, FlowPane, HBox}
import javafx.stage.Stage

class NotesController extends Initializable {

  @FXML private var notesListView: ListView[Note] = _
  @FXML private var titleTextBox: TextField = _
  @FXML private var cUnitTextBox: TextField = _
  @FXML private var addButton: Button = _
  @FXML private var editButton: Button = _
  @FXML private var deleteButton: Button = _
  @FXML private var openButton: Button = _
  @FXML private var textArea: TextArea = _

  def initialize(location: URL, resources: ResourceBundle): Unit = {
    val note1: Note = ("Nota1","Corpo1","MC")
    val note2: Note = ("Nota2","Corpo2","TS")
    val note3: Note = ("Nota3","Corpo3","PPM")
    val notebook = Notebook(List())
    val notebook1 = notebook.addNote(note1)
    val notebook2 = notebook1.addNote(note2)
    val notebook3 = notebook2.addNote(note3)

    var list_obs = FXCollections.observableArrayList[Note]()
    notebook3.notes.forall(list_obs.add(_))
    notesListView.setItems(list_obs)
  }

  def openFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    val secondStage: Stage = new Stage()
    val textArea2 = new TextArea(item._2)
    textArea2.setEditable(false)
    secondStage.setScene(new Scene(new BorderPane(textArea2)))
    secondStage.setTitle(item._1)
    secondStage.show()

  }

  def addFunc(): Unit = {
    val title = titleTextBox.getText.trim
    val body = textArea.getText.trim
    val cunit = cUnitTextBox.getText.trim
    val note: Note = (title, body, cunit)
    notesListView.getItems.add(note)
    titleTextBox.clear()
    textArea.clear()
    cUnitTextBox.clear()
  }

  def deleteFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    var list = notesListView.getItems.remove(item)
  }

  def editFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    titleTextBox.setText(item._1)
    cUnitTextBox.setText(item._3)
    textArea.setText(item._2)
    var list = notesListView.getItems.remove(item)
  }

}