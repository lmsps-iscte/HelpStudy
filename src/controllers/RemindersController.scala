package controllers

import classes.RemindersManager.{Reminder, getReminder}
import classes.{RemindersManager, Util}
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control._
import java.io.FileNotFoundException
import java.net.URL
import java.nio.file.Paths
import java.time.LocalDate
import java.util.ResourceBundle



class RemindersController extends Initializable {

  @FXML private var remindersListView: ListView[String] = _
  @FXML private var title_box: TextField = _
  @FXML private var priority_box: ChoiceBox[Int] = _
  @FXML private var text_area: TextArea = _
  @FXML private var date_box: DatePicker = _
  @FXML private var subject_picker: ChoiceBox[String] = _
  private var rem_man = RemindersController.getReminders
  private var list_subjs = SubjectsManagerController.getSubjectsManager.subjs

  def initialize(location: URL, resources: ResourceBundle): Unit = {
    rem_man = RemindersManager.sort_smart(rem_man, "SIGMOID") //Sort de acordo com a funcao desenvolvida

    val rems_list = rem_man.lst_rem
    val list_obs = FXCollections.observableArrayList[String]()
    rems_list.foreach(r => list_obs.add(r._1 + " - " + r._6))
    remindersListView.setItems(list_obs)

    val list = FXCollections.observableArrayList[Int]()
    List(1, 2, 3, 4).foreach(num => list.add(num))
    priority_box.setItems(list)

    priority_box.setValue(1)
    date_box.setValue(LocalDate.now())

    val list_subjs_obs = FXCollections.observableArrayList[String]()
    list_subjs.foreach(s => list_subjs_obs.add(s.name))
    subject_picker.setItems(list_subjs_obs)
  }

  def add_func(): Unit = {
    val title = title_box.getText.trim
    val priority = priority_box.getSelectionModel.getSelectedItem
    val body_aux = text_area.getText
    val body = body_aux.trim
    val cunit = subject_picker.getValue.trim
    val rem: Reminder = (title, body, priority, date_box.getValue, 0.0, cunit)
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
  }

  //ESTA A APAGAR O REMINDER na lista
  def elementClicked(): Unit = {
    val item = remindersListView.getSelectionModel.getSelectedItem
    val title = item.split("-")(0).trim
    val cunit = item.split("-")(1).trim
    System.out.println(cunit)
    val rem = rem_man.getReminder(title, cunit)
    title_box.setText(rem._1)
    text_area.setText(rem._2)
    subject_picker.setValue(rem._6)
    date_box.setValue(rem._4)
    priority_box.setValue(rem._3)
    deleteOps(rem, item)
    //val title = rem.split("-")(0).trim
    System.out.println(rem)
    //LANCAR A POP-UP AQUI
  }


  def deleteOps(rem: Reminder, item: String): Unit = {
    val list = remindersListView.getItems.remove(item)
    rem_man = rem_man.delReminder(rem._1)
    Util.saveToFile(rem_man.toString, "notes_paths.obj")
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