import java.net.URL
import java.util.ResourceBundle

import Notebook.Note
import RemindersManager.Reminder
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, ListView}

class SubjectWindowController extends Initializable {

  @FXML private var remindersListView: ListView[Reminder] = _
  @FXML private var notesListView: ListView[Note] = _
  @FXML private var subject_name_label: Label = _
  @FXML private var schedule_evaluation_button: Button = _
  @FXML private var calculate_final_grade_button: Button = _
  @FXML private var time_spent_button: Button = _


  var subj: Subject = _

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
  }

  def schedule_evaluation_buttonClicked(): Unit = {

  }

  def calculate_final_grade_buttonClicked(): Unit = {
    System.out.println(subj.calculate_FinalGrade())
  }

  def time_spent_buttonClicked(): Unit = {
  }

}
