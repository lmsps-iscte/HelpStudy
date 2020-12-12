package controllers


import java.io.FileNotFoundException

import classes.Notebook.Note
import classes.RemindersManager.Reminder
import classes.{Schedule, Subject, SubjectsManager, Util}
import classes.Subject.Evaluation
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._
import java.lang.Double.parseDouble
import java.net.URL
import java.nio.file.Paths
import java.time.LocalDate
import java.util.ResourceBundle

import javafx.scene.Scene
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

import scala.io.Source

class SubjectWindowController extends Initializable {

  @FXML private var remindersListView: ListView[String] = _
  @FXML private var notesListView: ListView[String] = _
  @FXML private var evalsListView: ListView[String] = _
  @FXML private var subject_name_label: Label = _
  @FXML private var schedule_evaluation_button: Button = _
  @FXML private var calculate_final_grade_button: Button = _
  @FXML private var time_spent_button: Button = _
  @FXML private var percentage_field: TextField = _
  @FXML private var type_eval_field: TextField = _
  @FXML private var date_picker: DatePicker = _
  @FXML private var delEvaluation_button: Button = _
  @FXML private var type_grade: TextField = _

  private var subj: Subject = _
  private var subj_man: SubjectsManager = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}

  def setController(subj1: Subject, subj_man1: SubjectsManager): Unit = {
    subj_man = subj_man1
    subj = subj1
    SubjectWindowController.setSubject(subj)
    System.out.println(subj)
    val subj_aux = subj

    subject_name_label.setText(subj_aux.name)

    val rems_list = RemindersController.getReminders.getRemindersbyCUnit(subj_aux.name.trim)
    System.out.println(rems_list)
    var list_rems_obs = FXCollections.observableArrayList[String]()
    rems_list.forall( rem => list_rems_obs.add(rem._1 + " - " + rem._4))
    remindersListView.setItems(list_rems_obs)

    val notes_list = NotesController.getNotes.getNotesbyCUnit(subj_aux.name.trim)
    var list_notes_obs = FXCollections.observableArrayList[String]()
    notes_list.forall(note => list_notes_obs.add(note._1))
    notesListView.setItems(list_notes_obs)

    val evals_list = subj_aux.evals
    var list_evals_obs = FXCollections.observableArrayList[String]()
    evals_list.forall(eval => list_evals_obs.add(eval._3.trim + " - " + eval._1 + " - " + eval._2._1 + "% Grade: " + eval._2._2))
    evalsListView.setItems(list_evals_obs)

    date_picker.setValue(LocalDate.now())

    type_grade.setText("0")
  }

  def delEvaluation_Clicked(): Unit = {
    val item = evalsListView.getSelectionModel.getSelectedItem
    evalsListView.getItems.remove(item)
    System.out.println("Antes"+ subj.evals)
    subj = subj.del_evaluation(item.split("-")(0).trim)
    System.out.println("Depois"+subj.evals)
    SubjectWindowController.setSubject(subj)
    subj_man = subj_man.replaceSubject(subj)
    Util.saveToFile(subj_man.toString, "subjects_paths.obj")
  }

  def schedule_evaluation_buttonClicked(): Unit = {
    val percentage: Double = parseDouble(percentage_field.getText)
    val date: LocalDate = date_picker.getValue
    val title: String = type_eval_field.getText
    val grade: Double = type_grade.getText.toDouble
    val eval: Evaluation = (date, (percentage, grade), title)
    subj = subj.add_evaluation(eval)
    evalsListView.getItems.add(evalsListView.getItems.size, eval._3 + " - " + eval._1 + " - " + eval._2._1 + "% Grade: " + eval._2._2)
    percentage_field.clear()
    type_eval_field.clear()

    //System.out.println(subj_man)
    subj_man = subj_man.replaceSubject(subj)
    //System.out.println(subj_man)
    SubjectWindowController.setSubject(subj) //NAO APAGAR
    //SubjectsManagerController.setSubjectsManager(subj_man)
    Util.saveToFile(subj_man.toString, "subjects_paths.obj")

  }

  def evalClicked(): Unit = {
    val item: String = evalsListView.getSelectionModel.getSelectedItem
    System.out.println(item)
    val title = item.split("-")(0).trim
    //val date = LocalDate.parse(item.split("-")(1).trim)
    //date_picker.setValue(date)
    type_eval_field.setText(title)

  }

  def editEvaluation(): Unit = {
    val item = evalsListView.getSelectionModel.getSelectedItem
    deleteOps(item)
    subj = subj.add_evaluation(fieldsToEvaluation())
    SubjectWindowController.setSubject(subj)
    loadInfo()
    clearFields()
  }

  def calculate_final_grade_buttonClicked(): Unit = {
      try {
        showFinalGradeAlert()
      } catch {
        case e: IllegalStateException => badEvalListAlert()
      }
  }

  def time_spent_buttonClicked(): Unit = {
    System.out.println(ScheduleController.getSchedule.timebyCUnitL7Days(subj.name.trim))
    timeSpentAlert()
  }

  @throws(classOf[IllegalStateException])
  def showFinalGradeAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("Final Grade - " + subj.name.trim)
    alert.setHeaderText("Your final grade on " + subj.name.trim + " is " +  subj.calculate_FinalGrade())
    alert.showAndWait()
  }

  def timeSpentAlert(): Unit = {
    val alert =  new Alert(AlertType.WARNING)
    alert.setTitle("Time Spent - " + subj.name.trim)
    alert.setHeaderText("Your total time spent on " + subj.name.trim + " is " + ScheduleController.getSchedule.timebyCUnitL7Days(subj.name.trim) + " mins")
    alert.showAndWait()
  }

  def badEvalListAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("THE SUM OF PERCENTAGES OF YOUR EVALUATIONS IS DIFFERENT FROM 100%! CHECK YOUR LIST!")
    alert.showAndWait()
  }

  def deleteOps(item: String): Unit = {
    evalsListView.getItems.remove(item)
    subj = subj.del_evaluation(item.split("-")(0).trim)
    SubjectWindowController.setSubject(subj)
  }

  def fieldsToEvaluation(): Evaluation = {
    val title = type_eval_field.getText.trim
    val percent = percentage_field.getText.toDouble
    val date = date_picker.getValue
    val grade = type_grade.getText.toDouble
    (date, (percent, grade), title)
  }

  def loadInfo(): Unit = {
    evalsListView.getItems.clear()
    subj.evals.foreach(e => evalsListView.getItems.add(evalsListView.getItems.size, e._3 + " - " + e._1 + " - " + e._2._1 + "% Grade: " + e._2._2))
  }

  def clearFields(): Unit = {
    type_grade.clear()
    percentage_field.clear()
    type_eval_field.clear()
  }

}

object SubjectWindowController {

  var sub: Subject = _

  def setSubject(newSubject: Subject): Unit = {
    sub = newSubject
  }

  def getSubject: Subject = {
    sub
  }

}