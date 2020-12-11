package controllers

import java.io.FileNotFoundException

import classes.{Notebook, SubjectsManager, Util}
import classes.Notebook.{Note, getNote}
import controllers.NotesController.setNotes
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.Scene
import javafx.scene.control.{Alert, Button, ChoiceBox, Label, ListView, TextArea, TextField}
import javafx.scene.layout.BorderPane
import javafx.stage.Stage
import java.net.URL
import java.nio.file.Paths
import java.util.ResourceBundle

import javafx.scene.control.Alert.AlertType

class NotesController extends Initializable {

  @FXML private var notesListView: ListView[String] = _
  @FXML private var titleTextBox: TextField = _
  @FXML private var importTextField: TextField = _
  @FXML private var infoLabel: Label = _
  @FXML private var infoLabel2: Label = _
  @FXML private var infoLabel3: Label = _
  @FXML private var addButton: Button = _
  @FXML private var editButton: Button = _
  @FXML private var deleteButton: Button = _
  @FXML private var openButton: Button = _
  @FXML private var textArea: TextArea = _
  @FXML private var sortPicker: ChoiceBox[String] = _
  @FXML private var subjectChoiceBox: ChoiceBox[String] = _
  private var notebook: Notebook = NotesController.getNotes
  private var note: Note = _
  private var subjs: SubjectsManager = _

  //LOADS INFO TO BE PRESENTED ON THE WINDOW

  def initialize(location: URL, resources: ResourceBundle): Unit = {

    subjs = SubjectsManagerController.getSubjectsManager
    subjectChoiceBox.getItems.clear()
    subjs.subjs.foreach(sub => subjectChoiceBox.getItems.add(sub.name.trim))

    var list_obs = FXCollections.observableArrayList[String]()
    notebook.notes.foreach(note => list_obs.add(note._1 + " - " + note._3))
    notesListView.setItems(list_obs)

    var choices = FXCollections.observableArrayList[String]()
    choices.add("Title")
    choices.add("Subject")
    sortPicker.setItems(choices)
  }

  //OPEN BUTTON FUNC

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

  //ADD BUTTON FUNC

  def addFunc(): Unit = {
    if (titleTextBox.getText().isEmpty || subjectChoiceBox.getSelectionModel.getSelectedItem == null)
      launchAlert()
    else {
      val title = titleTextBox.getText.trim
      val body = textArea.getText.trim
      val cunit = subjectChoiceBox.getSelectionModel.getSelectedItem.trim
      val note: Note = (title, body, cunit)

      notebook = notebook.addNote(note)
      NotesController.setNotes(notebook)
      notesListView.getItems.add(title + " - " + cunit)
      notebook.exportToFile(note, "normal")
      titleTextBox.clear()
      textArea.clear()
      subjectChoiceBox.getSelectionModel.clearSelection()
    }
  }

  //DELETE BUTTON FUNC

  def deleteFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    deleteOps(item)
  }

  //EDIT BUTTON FUNC

  def editFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    deleteOps(item)
    notebook = notebook.addNote(fieldsToNote())
    NotesController.setNotes(notebook)
    loadInfo()
    notebook.exportToFile(fieldsToNote(), "normal")
    clearFields()

  }

  //IMPORT BUTTON FUNC

  def importFunc(): Unit = {
    if (subjectChoiceBox.getSelectionModel.getSelectedItem == null)
      launchAlert1()
    else {
      val path = importTextField.getText()
      val title = Paths.get(path).getFileName.toString.stripSuffix(".txt")
      val cunit = subjectChoiceBox.getSelectionModel.getSelectedItem.trim
      notebook = notebook.importFromFile(path, cunit)
      setNotes(notebook)
      notesListView.getItems.add(title + " - " + cunit)
      notebook.exportToFile(getNote(notebook, title, cunit), "normal")
      importTextField.clear()
      subjectChoiceBox.getSelectionModel.clearSelection()
    }
  }

  //EXPORT BUTTON FUNC

  def exportFunc(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    notebook.exportToFile(getNote(notebook, item.split("-")(0).trim,
      item.split("-")(1).trim), "desktop")
  }

  //EXPORT BUTTON MOUSE ENTER FUNC

  def hoverFuncEnter(): Unit = {
    infoLabel.setVisible(true)
  }

  //EXPORT BUTTON MOUSE EXIT FUNC

  def hoverFuncExit(): Unit = {
    infoLabel.setVisible(false)
  }

  //IMPORT BUTTON MOUSE ENTER FUNC

  def hoverFuncEnter2(): Unit = {
    infoLabel2.setVisible(true)
  }

  //IMPORT BUTTON MOUSE EXIT FUNC

  def hoverFuncExit2(): Unit = {
    infoLabel2.setVisible(false)
  }

  //EDIT BUTTON MOUSE ENTER FUNC

  def hoverFuncEnter3(): Unit = {
    infoLabel3.setVisible(true)
  }

  //EDIT BUTTON MOUSE EXIT FUNC

  def hoverFuncExit3(): Unit = {
    infoLabel3.setVisible(false)
  }

  //APPLY BUTTON FUNC

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

  //GETS AND SETS FIELDS OF SELECTED ITEM

  def elementClicked(): Unit = {
    val item = notesListView.getSelectionModel.getSelectedItem
    setFields(item)
    note = fieldsToNote()
  }

  //SETS ALL FIELDS

  def setFields(item: String): Unit = {
    val title = item.split("-")(0).trim
    val cunit = item.split("-")(1).trim
    val note = notebook.getNote(title, cunit)
    titleTextBox.setText(note._1)
    textArea.setText(note._2)
    subjectChoiceBox.setValue(note._3.trim)
  }

  //CLEARS ALL FIELDS

  def clearFields(): Unit = {
    titleTextBox.clear()
    textArea.clear()
    subjectChoiceBox.getSelectionModel.clearSelection()
  }

  //CREATES NOTE BASED ON FIELDS CONTENT

  def fieldsToNote(): Note = {
    val title = titleTextBox.getText().trim
    val body = textArea.getText().trim
    val cunit = subjectChoiceBox.getSelectionModel.getSelectedItem.trim
    (title, body, cunit)
  }

  //DELETE OPERATIONS

  def deleteOps(item: String): Unit = {
    notesListView.getItems.remove(item)
    val file = Paths.get(System.getProperty("user.dir"), item.split("-")(1).trim, item.split("-")(0).trim + ".txt")
    notebook = notebook.removeNote(getNote(notebook, item.split("-")(0).trim, item.split("-")(1).trim))
    NotesController.setNotes(notebook)
    Util.deleteFile(file.toString)
  }

  //LOADS INFO TO LISTVIEW

  def loadInfo(): Unit = {
    notesListView.getItems.clear()
    notebook.notes.foreach(note => notesListView.getItems.add(note._1 + " - " + note._3))
  }

  //ALERT OF ALL FIELDS MUST BE FILLED

  def launchAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("You must fill the fields title and subject to create a note!")
    alert.showAndWait()
  }

  //ALERT OF SUBJECT NOT SPECIFIED

  def launchAlert1(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("You must specify the subject!")
    alert.showAndWait()
  }

}

object NotesController {

  var notes: Notebook = _

  lazy val firstNotes: Notebook = loadNotes

  //LOADS NOTEBOOK FROM FILE

  private def loadNotes: Notebook = {
    try {
      val masterFileContent = Util.readFromFile("notes_paths.obj")
      Notebook.fromString(masterFileContent)
    } catch {
      case e: FileNotFoundException => Notebook(List())
    }
  }

  //SETS NEW NOTEBOOK

  private def setNotes(newNotes: Notebook): Unit = {
    notes = newNotes
  }

  //GETS EXISTING NOTEBOOK

  def getNotes: Notebook = {
    if (notes == null)
      notes = firstNotes
    notes
  }

}