import java.net.URL
import java.time.{LocalDate, LocalTime}
import java.time.format.DateTimeFormatter
import java.util.ResourceBundle

import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, ListView, TextField}

class ScheduleController extends Initializable {

    @FXML private var listView1: ListView[SBlock] = _
    @FXML private var listView2: ListView[SBlock] = _
    @FXML private var listView3: ListView[SBlock] = _
    @FXML private var listView4: ListView[SBlock] = _
    @FXML private var listView5: ListView[SBlock] = _
    @FXML private var listView6: ListView[SBlock] = _
    @FXML private var listView7: ListView[SBlock] = _
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
      val schedule = Schedule(List(),50)
      val schedule1 = schedule.addSBlock(bloco1)
      val schedule2 = schedule1.addSBlock(bloco2)
      val schedule3 = schedule2.addSBlock(bloco3)
      ratioTextBox.setText(schedule3.school_percent.toString)
      var list_obs = FXCollections.observableArrayList[SBlock]()
      schedule3.sblocks.forall(list_obs.add(_))
      listView1.setItems(list_obs)
    }

    def addFunc(): Unit = {
      val date = LocalDate.parse(dateTextField.getText().trim)
      val stime = LocalTime.parse(sTimeTextField.getText().trim)
      val etime = LocalTime.parse(eTimeTextField.getText().trim)
      val title = titleTextField.getText.trim
      val cunit = cUnitTextField.getText().trim
      val sblock = SBlock(date, stime, etime, title, cunit)
      listView1.getItems.add(sblock)
      dateTextField.clear()
      sTimeTextField.clear()
      eTimeTextField.clear()
      titleTextField.clear()
      cUnitTextField.clear()
    }

    def editFunc(): Unit = {
      val item = listView1.getSelectionModel.getSelectedItem
      dateTextField.setText(item.date.toString)
      sTimeTextField.setText(item.start_time.toString)
      eTimeTextField.setText(item.end_time.toString)
      titleTextField.setText(item.title)
      cUnitTextField.setText(item.cunit)
      var list = listView1.getItems.remove(item)
   }

    def deleteFunc(): Unit = {
      val item = listView1.getSelectionModel.getSelectedItem
      var list = listView1.getItems.remove(item)
    }

    def updateFunc(): Unit = {

    }

}
