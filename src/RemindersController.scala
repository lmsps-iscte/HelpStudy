import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

import RemindersManager.Reminder
import javafx.collections.{FXCollections, ObservableList}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.ListView

import scala.collection.mutable.ListBuffer


class RemindersController extends Initializable {

  val list: List[String] = List("ze", "joao", "bff")
  //val test : Seq[String] = list

 //

  @FXML
  private var remindersListView: ListView[Reminder] = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    val rems: RemindersManager = RemindersManager(List(("Titulo1", "Body1", 3, LocalDate.now(), 0.0),
      ("Titulo2", "Body2", 1,LocalDate.parse("2020-11-20") , 0.0), ("Titulo3", "Body3", 1, LocalDate.parse("2020-11-23"), 0.0),
      ("Titulo4", "Body4", 4, LocalDate.parse("2020-11-30"), 0.0), ("Titulo5", "Body5", 4, LocalDate.parse("2020-11-24"), 0.0)))
    val rems_list = rems.lst_rem
    val list_obs = FXCollections.observableList[Reminder](rems_list)
    remindersListView.setItems()

  }
}
