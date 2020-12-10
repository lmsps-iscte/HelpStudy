package controllers

import classes.Deck.Card
import classes.{Deck, RandomWithState, Subject, SubjectsManager}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Alert.AlertType
import javafx.scene.control._
import javafx.scene.layout.GridPane

import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

class DeckController extends Initializable{

  @FXML private var cardList: ListView[String] = _
  @FXML private var subj_box: ChoiceBox[String] = _
  private var deck: Deck = DeckController.getDeck
  private var subjs: SubjectsManager = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {
//    subjs = classes.SubjectsManager.fromString("", List(), List())
    subjs = SubjectsManager(List(Subject("PPM")))
    subj_box.getItems.clear()
    subjs.subjs.foreach(sub => subj_box.getItems.add(sub.name))
  }

  def addCard(): Unit = {
    var alert: Alert = new Alert(AlertType.CONFIRMATION)
    alert.setTitle("Add a Card")
    alert.setHeaderText("Choose a Question and Answer")
    var grid = new GridPane
    val quest_text = new TextField()
    val ans_text = new TextField()
    val ok_button = new Button("OK")
    val cancel_button = new Button("Cancel")
    subjs.subjs.foreach(sub => subj_box.getItems.add(sub.name))
    grid.add(new Label("Question: "), 0, 0)
    grid.add(quest_text, 1, 0)
    grid.add(new Label("Answer: "), 0, 1)
    grid.add(ans_text, 1, 1)
    alert.getDialogPane.setContent(grid)
    val result = alert.showAndWait()

    result.get match {
      case ButtonType.OK => val card = (quest_text.getText.trim, ans_text.getText.trim, 0, subj_box.getSelectionModel.getSelectedItem, LocalDate.now)
        DeckController setDeck deck.addCard(card)
        deck = DeckController.getDeck
        cardList.getItems.add(card._1)
      case ButtonType.CANCEL =>
    }
  }

  def deleteCard(): Unit = {
    val question = cardList.getSelectionModel.getSelectedItem
    val card = deck.getCard(question)
    DeckController setDeck deck.removeCard(card)
    deck = DeckController.getDeck
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
    DeckController.setDeck(deck)
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
    DeckController.setDeck(deck)
    if (correct._1)
      launchCorrect(card_deck._1)
    else
      launchWrong(card_deck._1)
  }

}

object DeckController {

  var deck: Deck = _

  lazy val firstDeck: Deck = loadDeck

  private def loadDeck: Deck = {
    Deck(List(), RandomWithState(0))
  }

  private def setDeck(newDeck: Deck): Unit = {
    if (deck == null)
      deck = firstDeck
    deck = newDeck
  }

  def getDeck: Deck = {
    if (deck == null)
      deck = firstDeck
    deck
  }

}