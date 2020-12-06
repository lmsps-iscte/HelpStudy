import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.ResourceBundle

import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Label, ListView}

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


      /*val note1: Note = ("Nota1","Corpo1","MC")
      val note2: Note = ("Nota2","Corpo2","TS")
      val note3: Note = ("Nota3","Corpo3","PPM")
      val notebook = Notebook(List())
      val notebook1 = notebook.addNote(note1)
      val notebook2 = notebook1.addNote(note2)
      val notebook3 = notebook2.addNote(note3)

      var list_obs = FXCollections.observableArrayList[Note]()
      notebook3.notes.forall(list_obs.add(_))
      notesListView.setItems(list_obs)*/
    }

}
