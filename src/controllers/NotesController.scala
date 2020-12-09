package controllers

import java.io.FileNotFoundException

import classes.{Notebook, Util}
import classes.Notebook.Note
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.{Button, ListView, TextArea, TextField}
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.net.URL
import java.util.ResourceBundle

class NotesController extends Initializable {

  @FXML private var notesListView: ListView[String] = _
  @FXML private var titleTextBox: TextField = _
  @FXML private var cUnitTextBox: TextField = _
  @FXML private var addButton: Button = _
  @FXML private var editButton: Button = _
  @FXML private var deleteButton: Button = _
  @FXML private var openButton: Button = _
  @FXML private var textArea: TextArea = _
  private var notebook : Notebook = Notebook(List())

  def initialize(location: URL, resources: ResourceBundle): Unit = {
    try {
      val masterFileContent = Util.readFromFile("notes_paths.obj")
      notebook = Notebook.fromString(masterFileContent)
    } catch {
      case e: FileNotFoundException =>
        /*val note1: Note = ("Nota1","Corpo1","MC")
        val note2: Note = ("Nota2","Corpo2","TS")
        val note3: Note = ("Nota3","Corpo3","PPM")
        notebook = notebook.addNote(note1)
        notebook = notebook.addNote(note2)
        notebook = notebook.addNote(note3)*/
    }

    var list_obs = FXCollections.observableArrayList[String]()
    notebook.notes.foreach(note => list_obs.add(note._1+" - "+note._3))
    notesListView.setItems(list_obs)
  }

  def openFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    val title = item.split(" ")(0).trim
    val cunit = item.split(" ")(2).trim
    val secondStage: Stage = new Stage()
    val textArea2 = new TextArea(notebook.getNote(title, cunit)._2)
    textArea2.setEditable(false)
    secondStage.setScene(new Scene(new BorderPane(textArea2)))
    secondStage.setTitle(item)
    secondStage.show()

  }

  def addFunc(): Unit = {
    val title = titleTextBox.getText.trim
    val body = textArea.getText.trim
    val cunit = cUnitTextBox.getText.trim
    val note: Note = (title, body, cunit)
    notebook = notebook.addNote(note)
    notesListView.getItems.add(title+" - "+cunit)
    notebook.exportToFile(note)
    Util.saveToFile(notebook.toString, "notes_paths.obj")
    titleTextBox.clear()
    textArea.clear()
    cUnitTextBox.clear()
  }

  def deleteFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    var list = notesListView.getItems.remove(item)
  }

  def editFunc(): Unit = {
    /*val item = notesListView.getSelectionModel.getSelectedItem
    val note = notebook.getNoteB(item)
    titleTextBox.setText(note._1)
    cUnitTextBox.setText(note._3)
    textArea.setText(note._2)
    var list = notesListView.getItems.remove(item)*/
  }

}