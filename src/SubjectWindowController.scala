
import java.lang.Double.parseDouble
import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import Notebook.Note
import RemindersManager.Reminder
import Subject.{Evaluation, add_evaluation, calculate_FinalGrade, del_evaluation}
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, DatePicker, Label, ListView, TextField}

class SubjectWindowController extends Initializable {

  @FXML private var remindersListView: ListView[Reminder] = _
  @FXML private var notesListView: ListView[Note] = _
  @FXML private var evalsListView: ListView[Evaluation] = _
  @FXML private var subject_name_label: Label = _
  @FXML private var schedule_evaluation_button: Button = _
  @FXML private var calculate_final_grade_button: Button = _
  @FXML private var time_spent_button: Button = _
  @FXML private var percentage_field: TextField = _
  @FXML private var date_picker: DatePicker = _
  @FXML private var delEvaluation_button: Button = _

  private var subj: Subject = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {}

  def setController(subj1: Subject): Unit = {
    subj = subj1
    System.out.println(subj)
    val subj_aux = subj

    subject_name_label.setText(subj_aux.name)

    val rems_list = subj_aux.rems
    var list_rems_obs = FXCollections.observableArrayList[Reminder]()
    rems_list.forall(list_rems_obs.add(_))
    remindersListView.setItems(list_rems_obs)

    val notes_list = subj_aux.notes
    var list_notes_obs = FXCollections.observableArrayList[Note]()
    notes_list.forall(list_notes_obs.add(_))
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
    subj = del_evaluation(subj, item._3)
  }

  def schedule_evaluation_buttonClicked(): Unit = {
    /*val eval: Evaluation = (LocalDate.parse("2020-11-20"), (100.0, 17))*/
    val percentage: Double = parseDouble(percentage_field.getText)
    val date: LocalDate = date_picker.getValue
    val eval: Evaluation = (date, (percentage, 0.0), "TESTE")
    subj = add_evaluation(subj, eval)
    evalsListView.getItems.add(evalsListView.getItems.size, eval)
    percentage_field.clear()
  }

  def calculate_final_grade_buttonClicked(): Unit = {
    System.out.println(calculate_FinalGrade(subj))
  }

  def time_spent_buttonClicked(): Unit = {
  }

}
