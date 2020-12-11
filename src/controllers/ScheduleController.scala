package controllers

import java.io.FileNotFoundException

import classes.{SBlock, Schedule, SubjectsManager, Util}
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Alert, Button, ChoiceBox, DatePicker, Label, ListView, TextField}
import java.net.URL
import java.time.{LocalDate, LocalTime}
import java.util.ResourceBundle

import javafx.scene.control.Alert.AlertType

class ScheduleController extends Initializable {

  @FXML private var listView1: ListView[String] = _
  @FXML private var listView2: ListView[String] = _
  @FXML private var listView3: ListView[String] = _
  @FXML private var listView4: ListView[String] = _
  @FXML private var listView5: ListView[String] = _
  @FXML private var listView6: ListView[String] = _
  @FXML private var listView7: ListView[String] = _
  @FXML private var dWeek1: Label = _
  @FXML private var dWeek2: Label = _
  @FXML private var dWeek3: Label = _
  @FXML private var dWeek4: Label = _
  @FXML private var dWeek5: Label = _
  @FXML private var dWeek6: Label = _
  @FXML private var dWeek7: Label = _
  @FXML private var date1: Label = _
  @FXML private var date2: Label = _
  @FXML private var date3: Label = _
  @FXML private var date4: Label = _
  @FXML private var date5: Label = _
  @FXML private var date6: Label = _
  @FXML private var date7: Label = _
  @FXML private var infoLabel: Label = _
  @FXML private var infoLabel1: Label = _
  @FXML private var badAlert: Label = _
  @FXML private var goodAlert: Label = _
  @FXML private var timeInfoLabel: Label = _
  @FXML private var ratioTextBox: TextField = _
  @FXML private var sTimeTextField: TextField = _
  @FXML private var eTimeTextField: TextField = _
  @FXML private var titleTextField: TextField = _
  @FXML private var subjectChoiceBox: ChoiceBox[String] = _
  @FXML private var datePicker: DatePicker = _
  @FXML private var addButton: Button = _
  @FXML private var editButton: Button = _
  @FXML private var deleteButton: Button = _
  @FXML private var updateButton: Button = _
  private var schedule: Schedule = ScheduleController.getSchedule
  private var sblock: SBlock = _
  private var list_obs1 = FXCollections.observableArrayList[String]()
  private var list_obs2 = FXCollections.observableArrayList[String]()
  private var list_obs3 = FXCollections.observableArrayList[String]()
  private var list_obs4 = FXCollections.observableArrayList[String]()
  private var list_obs5 = FXCollections.observableArrayList[String]()
  private var list_obs6 = FXCollections.observableArrayList[String]()
  private var list_obs7 = FXCollections.observableArrayList[String]()
  private var subjs: SubjectsManager = _

  //LOADS INFO TO BE PRESENTED ON THE WINDOW

  def initialize(location: URL, resources: ResourceBundle): Unit = {

    date1.setText(LocalDate.now().toString)
    date2.setText(LocalDate.now().plusDays(1).toString)
    date3.setText(LocalDate.now().plusDays(2).toString)
    date4.setText(LocalDate.now().plusDays(3).toString)
    date5.setText(LocalDate.now().plusDays(4).toString)
    date6.setText(LocalDate.now().plusDays(5).toString)
    date7.setText(LocalDate.now().plusDays(6).toString)

    dWeek1.setText(LocalDate.now().getDayOfWeek.toString)
    dWeek2.setText(LocalDate.now().plusDays(1).getDayOfWeek.toString)
    dWeek3.setText(LocalDate.now().plusDays(2).getDayOfWeek.toString)
    dWeek4.setText(LocalDate.now().plusDays(3).getDayOfWeek.toString)
    dWeek5.setText(LocalDate.now().plusDays(4).getDayOfWeek.toString)
    dWeek6.setText(LocalDate.now().plusDays(5).getDayOfWeek.toString)
    dWeek7.setText(LocalDate.now().plusDays(6).getDayOfWeek.toString)

    ratioTextBox.setText(schedule.school_percent.toString)

    subjs = SubjectsManagerController.getSubjectsManager
    subjectChoiceBox.getItems.clear()
    subjs.subjs.foreach(sub => subjectChoiceBox.getItems.add(sub.name.trim))

    loadInfo()

  }

  //ADD BUTTON FUNC

  def addFunc(): Unit = {
    if (datePicker.getValue == null || sTimeTextField.getText().isEmpty || eTimeTextField.getText().isEmpty
      || titleTextField.getText().isEmpty || subjectChoiceBox.getSelectionModel.getSelectedItem == null)
      launchAlert()
    else {
      val date = datePicker.getValue
      val stime = LocalTime.parse(sTimeTextField.getText().trim)
      val etime = LocalTime.parse(eTimeTextField.getText().trim)
      val title = titleTextField.getText.trim
      val cunit = subjectChoiceBox.getSelectionModel.getSelectedItem.trim
      val sblock = SBlock(date, stime, etime, title, cunit)

      if (sblock.isTooLong)
        launchTooLongAlert()
      else {
        if (schedule.willOverlay(sblock))
          launchOverlayAlert()
        else {
          schedule = schedule.addSBlock(sblock)
          ScheduleController.setSchedule(schedule)
          loadInfo()
          clearFields()
        }
      }
    }
  }

  //EDIT BUTTON FUNC

  def editFunc(): Unit = {
    schedule = schedule.removeSBlock(sblock)
    schedule = schedule.addSBlock(fieldsToSBlock())
    ScheduleController.setSchedule(schedule)
    loadInfo()
    clearFields()
  }

  //DELETE BUTTON FUNC

  def deleteFunc(): Unit = {
    schedule = schedule.removeSBlock(fieldsToSBlock())
    ScheduleController.setSchedule(schedule)
    loadInfo()
    clearFields()
  }

  //UPDATE BUTTON FUNC

  def updateFunc(): Unit = {
    schedule = schedule.updateRatio(ratioTextBox.getText().toInt)
    ScheduleController.setSchedule(schedule)
    loadInfo()
  }

  //GETS AND SETS FIELDS OF SELECTED ITEM FROM LISTVIEW 1

  def elementClicked1(): Unit = {
    val item = listView1.getSelectionModel.getSelectedItem
    setFields(item, date1.getText)
    sblock = fieldsToSBlock()
  }

  //GETS AND SETS FIELDS OF SELECTED ITEM FROM LISTVIEW 2

  def elementClicked2(): Unit = {
    val item = listView2.getSelectionModel.getSelectedItem
    setFields(item, date2.getText)
    sblock = fieldsToSBlock()
  }

  //GETS AND SETS FIELDS OF SELECTED ITEM FROM LISTVIEW 3

  def elementClicked3(): Unit = {
    val item = listView3.getSelectionModel.getSelectedItem
    setFields(item, date3.getText)
    sblock = fieldsToSBlock()
  }

  //GETS AND SETS FIELDS OF SELECTED ITEM FROM LISTVIEW 4

  def elementClicked4(): Unit = {
    val item = listView4.getSelectionModel.getSelectedItem
    setFields(item, date4.getText)
    sblock = fieldsToSBlock()
  }

  //GETS AND SETS FIELDS OF SELECTED ITEM FROM LISTVIEW 5

  def elementClicked5(): Unit = {
    val item = listView5.getSelectionModel.getSelectedItem
    setFields(item, date5.getText)
    sblock = fieldsToSBlock()
  }

  //GETS AND SETS FIELDS OF SELECTED ITEM FROM LISTVIEW 6

  def elementClicked6(): Unit = {
    val item = listView6.getSelectionModel.getSelectedItem
    setFields(item, date6.getText)
    sblock = fieldsToSBlock()
  }

  //GETS AND SETS FIELDS OF SELECTED ITEM FROM LISTVIEW 7

  def elementClicked7(): Unit = {
    val item = listView7.getSelectionModel.getSelectedItem
    setFields(item, date7.getText)
    sblock = fieldsToSBlock()
  }

  //RATIO LABEL MOUSE ENTER FUNC

  def hoverFuncEnter(): Unit = {
    infoLabel.setVisible(true)
  }

  //RATIO LABEL MOUSE EXIT FUNC

  def hoverFuncExit(): Unit = {
    infoLabel.setVisible(false)
  }

  //EDIT BUTTON MOUSE ENTER FUNC

  def hoverFuncEnter1(): Unit = {
    infoLabel1.setVisible(true)
  }

  //EDIT BUTTON MOUSE EXIT FUNC

  def hoverFuncExit1(): Unit = {
    infoLabel1.setVisible(false)
  }

  //LOADS INFO TO LISTVIEW

  def loadInfo(): Unit = {
    list_obs1.clear()
    list_obs2.clear()
    list_obs3.clear()
    list_obs4.clear()
    list_obs5.clear()
    list_obs6.clear()
    list_obs7.clear()
    ratioTextBox.clear()

    schedule.blocksByDay(LocalDate.parse(date1.getText)).foreach(sblock => list_obs1.add(sblock.start_time + " - " +
      sblock.end_time + "\n" + sblock.title + "\n" + sblock.cunit))
    schedule.blocksByDay(LocalDate.parse(date2.getText)).foreach(sblock => list_obs2.add(sblock.start_time + " - " +
      sblock.end_time + "\n" + sblock.title + "\n" + sblock.cunit))
    schedule.blocksByDay(LocalDate.parse(date3.getText)).foreach(sblock => list_obs3.add(sblock.start_time + " - " +
      sblock.end_time + "\n" + sblock.title + "\n" + sblock.cunit))
    schedule.blocksByDay(LocalDate.parse(date4.getText)).foreach(sblock => list_obs4.add(sblock.start_time + " - " +
      sblock.end_time + "\n" + sblock.title + "\n" + sblock.cunit))
    schedule.blocksByDay(LocalDate.parse(date5.getText)).foreach(sblock => list_obs5.add(sblock.start_time + " - " +
      sblock.end_time + "\n" + sblock.title + "\n" + sblock.cunit))
    schedule.blocksByDay(LocalDate.parse(date6.getText)).foreach(sblock => list_obs6.add(sblock.start_time + " - " +
      sblock.end_time + "\n" + sblock.title + "\n" + sblock.cunit))
    schedule.blocksByDay(LocalDate.parse(date7.getText)).foreach(sblock => list_obs7.add(sblock.start_time + " - " +
      sblock.end_time + "\n" + sblock.title + "\n" + sblock.cunit))

    listView1.setItems(list_obs1)
    listView2.setItems(list_obs2)
    listView3.setItems(list_obs3)
    listView4.setItems(list_obs4)
    listView5.setItems(list_obs5)
    listView6.setItems(list_obs6)
    listView7.setItems(list_obs7)

    ratioTextBox.setText(schedule.school_percent.toString)

    if (schedule.fatigueAlert()) {
      badAlert.setVisible(true)
      goodAlert.setVisible(false)
    }
    else {
      badAlert.setVisible(false)
      goodAlert.setVisible(true)
    }

    timeInfoLabel.setText(schedule.timeSpentBySchoolToday().toString)
  }

  //SETS ALL FIELDS

  def setFields(item: String, date: String): Unit = {
    datePicker.setValue(LocalDate.parse(date))
    sTimeTextField.setText(item.split(" ")(0).trim)
    eTimeTextField.setText(item.split(" ")(2).split("\n")(0).trim)
    titleTextField.setText(item.split("\n")(1).trim)
    subjectChoiceBox.setValue(item.split("\n")(2).trim)
  }

  //CLEARS ALL FIELDS

  def clearFields(): Unit = {
    sTimeTextField.clear()
    eTimeTextField.clear()
    titleTextField.clear()
    subjectChoiceBox.getSelectionModel.clearSelection()
  }

  //CREATES SBLOCK BASED ON FIELDS CONTENT

  def fieldsToSBlock(): SBlock = {
    val date = datePicker.getValue
    val stime = LocalTime.parse(sTimeTextField.getText().trim)
    val etime = LocalTime.parse(eTimeTextField.getText().trim)
    val title = titleTextField.getText.trim
    val cunit = subjectChoiceBox.getSelectionModel.getSelectedItem
    SBlock(date, stime, etime, title, cunit)
  }

  //ALERT OF FIELDS NOT FILLED

  def launchAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("You must fill all the fields!")
    alert.showAndWait()
  }

  //ALERT OF OVERLAY

  def launchOverlayAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("You cannot insert this block on the schedule because it will overlay another!")
    alert.showAndWait()
  }

  //ALERT OF SBLOCK TOO LONG

  def launchTooLongAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("You should not insert this block on the schedule because it is too long (more than 90 minutes)!")
    alert.showAndWait()
  }
}

object ScheduleController {

  var schedule: Schedule = _

  lazy val firstSchedule: Schedule = loadSchedule

  //LOADS SCHEDULE FROM FILE

  private def loadSchedule: Schedule = {
    try {
      val fileContent = Util.readFromFile("schedule.obj")
      Schedule.fromString(fileContent)
    } catch {
      case e: FileNotFoundException => Schedule(List(), 50)
    }
  }

  //SETS NEW SCHEDULE

  private def setSchedule(newSchedule: Schedule): Unit = {
    schedule = newSchedule
  }

  //GETS EXISTING SCHEDULE

  def getSchedule: Schedule = {
    if (schedule == null)
      schedule = firstSchedule
    schedule
  }

}