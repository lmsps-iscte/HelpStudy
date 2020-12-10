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

/*    val subj: Subject = Subject("PPM")
    val subj2 = subj.associate_reminder(("Titulo1", "Body1", 3, LocalDate.now(), 0.0))
    val subj3 = subj2.add_evaluation((LocalDate.parse("2020-11-20"), (100.0, 17.0), "TRABALHO"))
    val subj1 = subj3.associate_note(("Nota 1","Corpo 1","PPM"))
    val subs_list = List(subj1)*/
    //val subs_list: List[Subject] = List()
    subj_man = SubjectsManagerController.getSubjectsManager
//    SubjectsManagerController.setSubjectsManager(subj_man)
    //System.out.println("Cheguei")
    //val subs_list: List[Subject] = subj_man.subjs
    //System.out.println(subj_man.subjs)
    var list_obs = FXCollections.observableArrayList[String]()
    subj_man.subjs.forall(subj => list_obs.add(subj.name))
    subjectsListView.setItems(list_obs)
    var list = FXCollections.observableArrayList[Int]()

  }

  def add_func(): Unit = {
    val title = title_box.getText.trim
    val subj: Subject = Subject(title)
    subj_man = subj_man.addSubject(subj)
    SubjectsManagerController.setSubjectsManager(subj_man)
    subjectsListView.getItems.add(subjectsListView.getItems.size, subj.name)
//    Util.saveToFile(subj_man.toString, "subjects_paths.obj")

  }

  def delete_func(): Unit = {
    val item = subjectsListView.getSelectionModel.getSelectedItem
    var list = subjectsListView.getItems.remove(item)
    SubjectsManagerController.setSubjectsManager(subj_man)
  }

  def elementClicked(): Unit = {
    val subj_name = subjectsListView.getSelectionModel.getSelectedItem
    val subj = subj_man.searchSubject(subj_name).get
    /*val fxmlLoader = new FXMLLoader(getClass.getResource("controllers.SubjectWindowController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()

    val subjectWindowController: controllers.SubjectWindowController = fxmlLoader.getController
    subjectWindowController.setController(subj)
    open_button.getScene.setRoot(mainViewRoot)*/
    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/SubjectWindowController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    mainController.getMainPane.setCenter(mainViewRoot)
    val subjectWindowController: SubjectWindowController = fxmlLoader.getController
    subjectWindowController.setController(subj)
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
      //Importar as notas e os reminders e depois dar filter pela subj?
      val subj_notes = List()
      val subj_rems = List()

      val masterFileContent = Util.readFromFile("subjects_paths.obj")
      System.out.println(masterFileContent)
      SubjectsManager.fromString(masterFileContent/*, subj_notes, subj_rems*/)
      //System.out.println(subj_man)
    } catch {
      case e: FileNotFoundException =>
        SubjectsManager(List())
    }
  }

  def setSubjectsManager(newSubjectsManager: SubjectsManager): Unit = {
    if (SubjectsManager == null)
      sub_man = firstSubjectsManager
    sub_man = newSubjectsManager
  }

  def getSubjectsManager: SubjectsManager = {
    if (sub_man == null)
      sub_man = firstSubjectsManager
    sub_man
  }

}