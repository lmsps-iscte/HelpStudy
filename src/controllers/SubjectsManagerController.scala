package controllers

import java.io.FileNotFoundException

import classes.{Subject, SubjectsManager, Util}
import javafx.collections.FXCollections
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.Parent
import javafx.scene.control.{Button, ListView, TextField}
import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

class SubjectsManagerController extends Initializable {

  @FXML private var subjectsListView: ListView[String] = _
  @FXML private var title_box: TextField = _
  @FXML private var open_button: Button = _
  private var subj_man : SubjectsManager = SubjectsManagerController.getSubjectsManager
  private var mainController : MainController = _

  def initialize(location: URL, resources: ResourceBundle): Unit = {

    subj_man = SubjectsManagerController.getSubjectsManager
    var list_obs = FXCollections.observableArrayList[String]()
    subj_man.subjs.forall(subj => list_obs.add(subj.name))
    subjectsListView.setItems(list_obs)
    var list = FXCollections.observableArrayList[Int]()

  }

  //ADD SUBJECT
  def add_func(): Unit = {
    val title = title_box.getText.trim
    val subj: Subject = Subject(title)
    subj_man = subj_man.addSubject(subj)
    SubjectsManagerController.setSubjectsManager(subj_man)
    subjectsListView.getItems.add(subjectsListView.getItems.size, subj.name)

  }

  //DELETE SUBJECT
  def delete_func(): Unit = {
    val item = subjectsListView.getSelectionModel.getSelectedItem
    var list = subjectsListView.getItems.remove(item)
    SubjectsManagerController.setSubjectsManager(subj_man)
  }

  def elementClicked(): Unit = {
    val subj_name = subjectsListView.getSelectionModel.getSelectedItem
    val subj = subj_man.searchSubject(subj_name).get

    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/SubjectWindowController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    mainController.getMainPane.setCenter(mainViewRoot)
    val subjectWindowController: SubjectWindowController = fxmlLoader.getController
    subjectWindowController.setController(subj, subj_man)
  }

  def setParent(mainController1: MainController): Unit = {
      mainController = mainController1
  }

}
object SubjectsManagerController {

  var sub_man: SubjectsManager = _

  lazy val firstSubjectsManager: SubjectsManager = loadSubjectsManager

  private def loadSubjectsManager: SubjectsManager = {
    try {
      val subj_notes = List()
      val subj_rems = List()

      val masterFileContent = Util.readFromFile("subjects_paths.obj")
      System.out.println(masterFileContent)
      SubjectsManager.fromString(masterFileContent, subj_notes, subj_rems)
    } catch {
      case e: FileNotFoundException =>
        SubjectsManager(List())
    }
  }

  def setSubjectsManager(newSubjectsManager: SubjectsManager): Unit = {
    sub_man = newSubjectsManager
  }

  def getSubjectsManager: SubjectsManager = {
    if (sub_man == null)
      sub_man = firstSubjectsManager
    sub_man
  }

}