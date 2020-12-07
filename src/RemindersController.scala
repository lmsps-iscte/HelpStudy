import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import RemindersManager.Reminder
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, ChoiceBox, DatePicker, ListView, TextArea, TextField}

import scala.collection.mutable.ListBuffer


class RemindersController extends Initializable {

  @FXML private var remindersListView: ListView[String] = _
  @FXML private var title_box: TextField = _
  @FXML private var priority_box: ChoiceBox[Int] = _
  @FXML private var text_area: TextArea = _
  @FXML private var date_box: DatePicker = _
  var rem_man = RemindersManager(List())

  def initialize(location: URL, resources: ResourceBundle): Unit = {
    rem_man = RemindersManager(List(("Titulo1", "Body1", 3, LocalDate.now(), 0.0),
      ("Titulo2", "Body2", 1,LocalDate.parse("2020-11-20") , 0.0), ("Titulo3", "Body3", 1, LocalDate.parse("2020-11-23"), 0.0),
      ("Titulo4", "Body4", 4, LocalDate.parse("2020-11-30"), 0.0), ("Titulo5", "Body5", 4, LocalDate.parse("2020-11-24"), 0.0)))
    val rems_list = rem_man.lst_rem
    var list_obs = FXCollections.observableArrayList[String]()
    rems_list.forall(r => list_obs.add(r._1))
    remindersListView.setItems(list_obs)
    var list = FXCollections.observableArrayList[Int]()
    List(1, 2, 3, 4).foreach(num => list.add(num))
    priority_box.setItems(list)
    priority_box.setValue(1)
    date_box.setValue(LocalDate.now())
  }

  def add_func(): Unit = {
    val title = title_box.getText.trim
    val priority = priority_box.getSelectionModel.getSelectedItem
    val body_aux = text_area.getText
    val body = body_aux.trim
    val rem: Reminder = (title, body, priority, date_box.getValue, 0.0)
    rem_man = rem_man.addReminder(rem)
    remindersListView.getItems.add(remindersListView.getItems.size(), rem._1)
  }

  def delete_func(): Unit = {
    val item = remindersListView.getSelectionModel.getSelectedItem
    var list = remindersListView.getItems.remove(item)
  }

  def elementClicked(): Unit = {
    val rem = remindersListView.getSelectionModel.getSelectedItem
    System.out.println(rem)
    //LANCAR A POP-UP AQUI
  }

}
