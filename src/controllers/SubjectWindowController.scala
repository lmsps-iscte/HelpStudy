package controllers


import classes.Notebook.Note
import classes.RemindersManager.Reminder
import classes.Subject
import classes.Subject.Evaluation
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._

import java.lang.Double.parseDouble
import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

class SubjectWindowController extends Initializable {

  @FXML private var remindersListView: ListView[Reminder] = _
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

    val rems_list = RemindersController.getReminders.getByCUnit(subj_aux.name)
    var list_rems_obs = FXCollections.observableArrayList[Reminder]()
    rems_list.forall(list_rems_obs.add(_))
    remindersListView.setItems(list_rems_obs)

    val notes_list = NotesController.getNotes.getNotesbyCUnit(subj_aux.name.trim)
    //NotesController.getNotes
    var list_notes_obs = FXCollections.observableArrayList[String]()
    notes_list.forall(note => list_notes_obs.add(note._1))
    notesListView.setItems(list_notes_obs)

    val evals_list = subj_aux.evals
    var list_evals_obs = FXCollections.observableArrayList[Evaluation]()
    evals_list.forall(list_evals_obs.add(_))
    evalsListView.setItems(list_evals_obs)

    date_picker.setValue(LocalDate.now())
  }

  def delEvaluation_Clicked(): Unit = {
    val item = evalsListView.getSelectionModel.getSelectedItem
    evalsListView.getItems.remove(item)
    subj = subj.delEvaluation(item._3)
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
    System.out.println(subj.calculate_FinalGrade())
  }

  def time_spent_buttonClicked(): Unit = {
  }

}
