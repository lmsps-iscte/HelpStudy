package controllers

import classes.{SBlock, Schedule}
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, ListView, TextField}

import java.net.URL
import java.time.format.DateTimeFormatter
import java.time.{LocalDate, LocalTime}
import java.util.ResourceBundle

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
    @FXML private var ratioTextBox: TextField = _
    @FXML private var dateTextField: TextField = _
    @FXML private var sTimeTextField: TextField = _
    @FXML private var eTimeTextField: TextField = _
    @FXML private var titleTextField: TextField = _
    @FXML private var cUnitTextField: TextField = _
    @FXML private var addButton: Button = _
    @FXML private var editButton: Button = _
    @FXML private var deleteButton: Button = _
    @FXML private var updateButton: Button = _
    private var schedule: Schedule = Schedule(List(), 50)

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


      val bloco1 = SBlock(LocalDate.parse("06-12-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        LocalTime.of(9,30,0), LocalTime.of(10,30,0), "Aula TP de MC", "MC")
      val bloco2 = SBlock(LocalDate.parse("07-12-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        LocalTime.of(10,30,0), LocalTime.of(11,30,0), "Aula TP de MC", "CDSI")
      val bloco3 = SBlock(LocalDate.parse("08-12-2020", DateTimeFormatter.ofPattern("dd-MM-yyyy")),
        LocalTime.of(11,30,0), LocalTime.of(13,0,0), "Aula TP de MC", "MC")
      schedule = schedule.addSBlock(bloco1)
      schedule = schedule.addSBlock(bloco2)
      schedule = schedule.addSBlock(bloco3)
      ratioTextBox.setText(schedule.school_percent.toString)
      var list_obs = FXCollections.observableArrayList[String]()
      schedule.sblocks.foreach(block => list_obs.add(block.title))
      listView1.setItems(list_obs)
    }

    def addFunc(): Unit = {
      val date = LocalDate.parse(dateTextField.getText().trim)
      val stime = LocalTime.parse(sTimeTextField.getText().trim)
      val etime = LocalTime.parse(eTimeTextField.getText().trim)
      val title = titleTextField.getText.trim
      val cunit = cUnitTextField.getText().trim
      val sblock = SBlock(date, stime, etime, title, cunit)
      schedule.addSBlock(sblock)
      listView1.getItems.add(title)
      dateTextField.clear()
      sTimeTextField.clear()
      eTimeTextField.clear()
      titleTextField.clear()
      cUnitTextField.clear()
    }

    def editFunc(): Unit = {
      val item = listView1.getSelectionModel.getSelectedItem
      val block = schedule.getSBlockByName(item)
      dateTextField.setText(block.date.toString)
      sTimeTextField.setText(block.start_time.toString)
      eTimeTextField.setText(block.end_time.toString)
      titleTextField.setText(block.title)
      cUnitTextField.setText(block.cunit)
      var list = listView1.getItems.remove(item)
   }

    def deleteFunc(): Unit = {
      val item = listView1.getSelectionModel.getSelectedItem
      var list = listView1.getItems.remove(item)
    }

    def updateFunc(): Unit = {

    }

}
