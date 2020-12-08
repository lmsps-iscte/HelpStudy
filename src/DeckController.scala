import Deck.Card
import javafx.collections.FXCollections
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.{Alert, Button, ComboBox, Label, ListView, TextField}
import javafx.scene.layout.GridPane

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

class DeckController extends Initializable{

  @FXML private var cardList: ListView[String] = _
  @FXML private var subj_box: ComboBox[String] = _
  private var deck: Deck = Deck(List(), RandomWithState(0))
  private var subjs: SubjectsManager = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
    subjs = SubjectsManager.fromString("", List(), List())
    subjs.subjs.foreach(sub => subj_box.getItems.add(sub.name))
  }

  def addCard(): Unit = {
    var alert: Alert = new Alert(AlertType.NONE)
    alert.setTitle("Add a Card")
    alert.setHeaderText("Choose a Question and Answer")
    var grid = new GridPane
    val quest_text = new TextField()
    val ans_text = new TextField()
    val ok_button = new Button()
    val cancel_button = new Button()
    subjs.subjs.foreach(sub => subj_box.getItems.add(sub.name))
    grid.add(new Label("Question: "), 0, 0)
    grid.add(quest_text, 1, 0)
    grid.add(new Label("Answer: "), 0, 1)
    grid.add(ans_text, 1, 1)
    grid.add(ok_button, 0, 2)
    grid.add(cancel_button, 1, 2)
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

  def launchCorrect(card: Card): Unit = {
    var alert = new Alert(AlertType.NONE)
    alert.setTitle("Your answer is...")
    alert.setHeaderText("CORRECT!")
  }

  def launchWrong(card: Card): Unit = {
    var alert = new Alert(AlertType.WARNING)
    alert.setTitle("Your answer is...")
    alert.setHeaderText(s"Incorrect! The correct answer is:\n${card._2}")
  }

  def doQuiz(): Unit ={
    val card_deck = deck.ask(subj_box.getSelectionModel.getSelectedItem)
    deck = card_deck._2
    var alert: Alert = new Alert(AlertType.CONFIRMATION)
    alert.setTitle("Quiz - Give Us An Answer")
    alert.setHeaderText(card_deck._1._1)
    val gridPane = new GridPane
    gridPane.add(new Label("Answer: "), 0, 0)
    val answerField = new TextField()
    gridPane.add(answerField, 0, 1)
    alert.getDialogPane.getChildren.add(gridPane)
    alert.showAndWait()
    val correct = deck.answer(card_deck._1,answerField.getText)
    deck = correct._2
    if (correct._1)
      launchCorrect(card_deck._1)
    else
      launchWrong(card_deck._1)
  }

}