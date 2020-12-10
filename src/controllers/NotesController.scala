package controllers

import java.io.{File, FileNotFoundException}

import classes.{Notebook, Util}
import classes.Notebook.{Note, getNote}
import controllers.NotesController.setNotes
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.{Alert, Button, ChoiceBox, Label, ListView, TextArea, TextField}
import javafx.scene.layout.BorderPane
import javafx.stage.{FileChooser, Stage}
import java.net.URL
import java.nio.file.Paths
import java.util.ResourceBundle

import javafx.scene.control.Alert.AlertType

class NotesController extends Initializable {

  @FXML private var notesListView: ListView[String] = _
  @FXML private var titleTextBox: TextField = _
  @FXML private var cUnitTextBox: TextField = _
  @FXML private var importTextField: TextField = _
  @FXML private var infoLabel: Label = _
  @FXML private var infoLabel2: Label = _
  @FXML private var addButton: Button = _
  @FXML private var editButton: Button = _
  @FXML private var deleteButton: Button = _
  @FXML private var openButton: Button = _
  @FXML private var textArea: TextArea = _
  @FXML private var sortPicker: ChoiceBox[String] = _
  private var notebook: Notebook = NotesController.getNotes

  def initialize(location: URL, resources: ResourceBundle): Unit = {


    var list_obs = FXCollections.observableArrayList[String]()
    notebook.notes.foreach(note => list_obs.add(note._1 + " - " + note._3))
    notesListView.setItems(list_obs)

    var choices = FXCollections.observableArrayList[String]()
    choices.add("Title")
    choices.add("Subject")
    sortPicker.setItems(choices)
  }

  def openFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    val title = item.split("-")(0).trim
    val cunit = item.split("-")(1).trim
    val secondStage: Stage = new Stage()
    val textArea2 = new TextArea(notebook.getNote(title, cunit)._2)
    textArea2.setEditable(false)
    secondStage.setScene(new Scene(new BorderPane(textArea2)))
    secondStage.setTitle(title)
    secondStage.show()

  }

  def addFunc(): Unit = {
    val title = titleTextBox.getText.trim
    val body = textArea.getText.trim
    val cunit = cUnitTextBox.getText.trim
    val note: Note = (title, body, cunit)
    if(title.isEmpty || cunit.isEmpty)
      launchAlert()
    else {
      notebook = notebook.addNote(note)
      NotesController.setNotes(notebook)
      notesListView.getItems.add(title + " - " + cunit)
      notebook.exportToFile(note, "normal")
      //    Util.saveToFile(notebook.toString, "notes_paths.obj")
      titleTextBox.clear()
      textArea.clear()
      cUnitTextBox.clear()
    }
  }

  def deleteFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    deleteOps(item)
  }

  def editFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    val title = item.split("-")(0).trim
    val cunit = item.split("-")(1).trim
    val note = notebook.getNote(title, cunit)
    titleTextBox.setText(note._1)
    textArea.setText(note._2)
    cUnitTextBox.setText(note._3)
    deleteOps(item)

  }

  def importFunc(): Unit = {
    if (cUnitTextBox.getText.isEmpty)
      cUnitTextBox.setText("Unknown")
    val path = importTextField.getText()
    val title = Paths.get(path).getFileName.toString.stripSuffix(".txt")
    val cunit = cUnitTextBox.getText()
    notebook = notebook.importFromFile(path, cunit)
    setNotes(notebook)
    notesListView.getItems.add(title + " - " + cunit)
    notebook.exportToFile(getNote(notebook, title, cunit), "normal")
    //    Util.saveToFile(notebook.toString, "notes_paths.obj")
    importTextField.clear()
    cUnitTextBox.clear()
  }

  def exportFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    notebook.exportToFile(getNote(notebook, item.split("-")(0).trim,
      item.split("-")(1).trim), "desktop")
  }

  def hoverFuncEnter(): Unit = {
    infoLabel.setVisible(true)
  }

  def hoverFuncExit(): Unit = {
    infoLabel.setVisible(false)
  }

  def hoverFuncEnter2(): Unit = {
    infoLabel2.setVisible(true)
  }

  def hoverFuncExit2(): Unit = {
    infoLabel2.setVisible(false)
  }

  def applyFunc(): Unit = {
    val choice = sortPicker.getValue

    if (choice.equals("Title"))
      notebook = notebook.sortNotesBy("TITLE")
    else
      notebook = notebook.sortNotesBy("Subject")

    var list_obs = FXCollections.observableArrayList[String]()
    notebook.notes.foreach(note => list_obs.add(note._1 + " - " + note._3))
    notesListView.setItems(list_obs)

  }

  def deleteOps(item: String): Unit = {
    var list = notesListView.getItems.remove(item)
    val file = Paths.get(System.getProperty("user.dir"), item.split("-")(1).trim, item.split("-")(0).trim + ".txt")
    notebook = notebook.removeNote(getNote(notebook, item.split("-")(0).trim, item.split("-")(1).trim))
    NotesController.setNotes(notebook)
    Util.saveToFile(notebook.toString, "notes_paths.obj")
    Util.deleteFile(file.toString)
  }

  def launchAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("You mus fill the fields title and subject to create a note!")
    alert.show()
  }

}

object NotesController {

  var notes: Notebook = _

  lazy val firstNotes: Notebook = loadNotes

  private def loadNotes: Notebook = {
    try {
      val masterFileContent = Util.readFromFile("notes_paths.obj")
      Notebook.fromString(masterFileContent)
    } catch {
      case e: FileNotFoundException =>
        /*val note1: Note = ("Nota1","Corpo1","MC")
        val note2: Note = ("Nota2","Corpo2","TS")
        val note3: Note = ("Nota3","Corpo3","PPM")
        notebook = notebook.addNote(note1)
        notebook = notebook.addNote(note2)
        notebook = notebook.addNote(note3)*/

        Notebook(List())
    }
  }

  private def setNotes(newNotes: Notebook): Unit = {
    if (notes == null)
      notes = firstNotes
    notes = newNotes
  }

  def getNotes: Notebook = {
    if (notes == null)
      notes = firstNotes
    notes
  }

}