package controllers

import classes.RemindersManager.Reminder
import classes.{RemindersManager, Util}
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._

import java.io.FileNotFoundException
import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle


class RemindersController extends Initializable {

  @FXML private var remindersListView: ListView[String] = _
  @FXML private var title_box: TextField = _
  @FXML private var priority_box: ChoiceBox[Int] = _
  @FXML private var text_area: TextArea = _
  @FXML private var date_box: DatePicker = _
  private var rem_man = RemindersController.getReminders

  def initialize(location: URL, resources: ResourceBundle): Unit = {
    rem_man = RemindersManager.sort_smart(rem_man, "SIGMOID") //Sort de acordo com a funcao desenvolvida
    val rems_list = rem_man.lst_rem
    val list_obs = FXCollections.observableArrayList[String]()
    rems_list.forall(r => list_obs.add(r._1))
    remindersListView.setItems(list_obs)
    val list = FXCollections.observableArrayList[Int]()
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
    RemindersController.setReminders(rem_man)
    //rem_man = sort_smart(rem_man, "SIGMOID") NAO ATUALIZA A LISTA
    remindersListView.getItems.add(remindersListView.getItems.size(), rem._1)
//    Util.saveToFile(rem_man.toString(), "reminders.obj")
  }

  def delete_func(): Unit = {
    val item: String = remindersListView.getSelectionModel.getSelectedItem
    remindersListView.getItems.remove(item)
    rem_man = rem_man.delReminder(item)
    RemindersController.setReminders(rem_man)
//    Util.saveToFile(rem_man.toString(), "reminders.obj")
  }

  def elementClicked(): Unit = {
    val rem = remindersListView.getSelectionModel.getSelectedItem
    System.out.println(rem)
    //LANCAR A POP-UP AQUI
  }

}

object RemindersController {

  var reminders: RemindersManager = _

  lazy val firstReminders: RemindersManager = loadReminders

  private def loadReminders: RemindersManager = {
    try {
      val body = Util.readFromFile("reminders.obj")
      RemindersManager.fromString(body)
    } catch {
      case e: FileNotFoundException =>
        RemindersManager(List())
    }
  }

  private def setReminders(newReminders: RemindersManager): Unit = {
    reminders = newReminders
  }

  def getReminders: RemindersManager = {
    if (reminders == null)
      reminders = firstReminders
    reminders
  }

}