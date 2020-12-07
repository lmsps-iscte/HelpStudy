import javafx.fxml.FXML
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, Button, ComboBox, Label, ListView, TextField}
import javafx.scene.layout.GridPane

import java.time.LocalDate

class DeckController {

  @FXML private var cardList: ListView[String] = _
  private var deck: Deck = Deck(List(), RandomWithState(0))
  private var subjs: SubjectsManager = _

  def addCard(): Unit = {
    var alert: Alert = new Alert(AlertType.NONE)
    alert.setTitle("Add a Card")
    alert.setHeaderText("Choose a Question and Answer")
    var grid = new GridPane
    val quest_text = new TextField()
    val ans_text = new TextField()
    val ok_button = new Button()
    val cancel_button = new Button()
    val subj_box: ComboBox[String] = new ComboBox[String]()
    subjs.subjs.foreach(sub => subj_box.getItems.add(sub.name))
    grid.add(new Label("Question: "), 0, 0)
    grid.add(quest_text, 1, 0)
    grid.add(new Label("Answer: "), 0, 1)
    grid.add(ans_text, 1, 1)
    grid.add(new Label("Subject: "), 0, 2)
    grid.add(subj_box, 1, 2)
    grid.add(ok_button, 0, 3)
    grid.add(cancel_button, 0, 3)
    alert.getDialogPane.setContent(grid)
    alert.showAndWait()
    val card = (quest_text.getText, ans_text.getText, 0, subj_box.getSelectionModel.getSelectedItem, LocalDate.now)
    deck.addCard(card)
    cardList.getItems.add(card._1)
  }

  def deleteCard(): Unit = {
    val question = cardList.getSelectionModel.getSelectedItem
    val card = deck.getCard(question).get
    deck = deck.removeCard(card)
    cardList.getItems.remove(question)
  }

  def doQuiz(): Unit ={

  }

}