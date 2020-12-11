package controllers


import java.io.FileNotFoundException

import classes.Notebook.Note
import classes.RemindersManager.Reminder
import classes.{Schedule, Subject}
import classes.Subject.Evaluation
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._
import java.lang.Double.parseDouble
import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import javafx.scene.Scene
import javafx.scene.control.Alert.AlertType
import javafx.scene.layout.BorderPane
import javafx.stage.Stage

class SubjectWindowController extends Initializable {

  @FXML private var remindersListView: ListView[String] = _
  @FXML private var notesListView: ListView[String] = _
  @FXML private var evalsListView: ListView[Evaluation] = _
  @FXML private var subject_name_label: Label = _
  @FXML private var schedule_evaluation_button: Button = _
  @FXML private var calculate_final_grade_button: Button = _
  @FXML private var time_spent_button: Button = _
  @FXML private var percentage_field: TextField = _
  @FXML private var type_eval_field: TextField = _
  @FXML private var date_picker: DatePicker = _
  @FXML private var delEvaluation_button: Button = _

  private var subj: Subject = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}

  def setController(subj1: Subject): Unit = {
    subj = subj1
    System.out.println(subj)
    val subj_aux = subj

    subject_name_label.setText(subj_aux.name)

    val rems_list = RemindersController.getReminders.getRemindersbyCUnit(subj_aux.name.trim)
    System.out.println(rems_list)
    var list_rems_obs = FXCollections.observableArrayList[String]()
    //rems_list.forall(list_rems_obs.add(_))
    rems_list.forall( rem => list_rems_obs.add(rem._1 + " - " + rem._4))
    remindersListView.setItems(list_rems_obs)

    val notes_list = NotesController.getNotes.getNotesbyCUnit(subj_aux.name.trim)
    //NotesController.getNotes
    var list_notes_obs = FXCollections.observableArrayList[String]()
    notes_list.forall(note => list_notes_obs.add(note._1))
    notesListView.setItems(list_notes_obs)

    val evals_list = subj_aux.evals
    //var list_evals_obs = FXCollections.observableArrayList[String]()
    var list_evals_obs = FXCollections.observableArrayList[Evaluation]()
    evals_list.forall(list_evals_obs.add(_))
    //evals_list.forall(eval => list_evals_obs.add(eval._3 + " - " + eval._1 + " - " + "%: " + eval._2._1 + " Grade: " + eval._2._2))
    evalsListView.setItems(list_evals_obs)

    date_picker.setValue(LocalDate.now())
  }

  def delEvaluation_Clicked(): Unit = {
    val item = evalsListView.getSelectionModel.getSelectedItem
    evalsListView.getItems.remove(item)
    subj = subj.delEvaluation(item._3)
    //eliminar no subj_man???
  }

  def schedule_evaluation_buttonClicked(): Unit = {
    /*val eval: Evaluation = (LocalDate.parse("2020-11-20"), (100.0, 17))*/
    val percentage: Double = parseDouble(percentage_field.getText)
    val date: LocalDate = date_picker.getValue
    val title: String = type_eval_field.getText
    val eval: Evaluation = (date, (percentage, 0.0), title)
    subj = subj.add_evaluation(eval)
    evalsListView.getItems.add(evalsListView.getItems.size, eval)
    percentage_field.clear()
    type_eval_field.clear()
  }

  def calculate_final_grade_buttonClicked(): Unit = {
    val grade =
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
    alert.setHeaderText("Your total time spent on " + subj.name.trim + " is " + ScheduleController.getSchedule.timebyCUnitL7Days(subj.name.trim) + " hours")
    alert.showAndWait()
  }

  def badEvalListAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("THE SUM OF PERCENTAGES OF YOUR EVALUATIONS IS DIFFERENT FROM 100%! CHECK YOUR LIST!")
    alert.showAndWait()
  }

}
