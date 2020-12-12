package controllers

import classes.Deck.Card
import classes.{Deck, RandomWithState, SubjectsManager, Util}
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.Alert.AlertType
import javafx.scene.control._
import javafx.scene.layout.GridPane

import java.io.FileNotFoundException
import java.net.URL
import java.time.LocalDate
import java.util.ResourceBundle

class DeckController extends Initializable{

  @FXML private var cardList: ListView[String] = _
  @FXML private var subj_box: ChoiceBox[String] = _
  private var deck: Deck = DeckController.getDeck
  private var subjs: SubjectsManager = _

  override def initialize(location: URL, resources: ResourceBundle): Unit = {

    subjs = SubjectsManagerController.getSubjectsManager
    subj_box.getItems.clear()
    subjs.subjs.foreach(sub => subj_box.getItems.add(sub.name))
    deck.cards.foreach(card => cardList.getItems.add(s"${card._1} - ${card._4}"))
  }

  def addCard(): Unit = {
    if(subj_box.getSelectionModel.getSelectedItem == null) {
      launchAlert()
    }
    else {
      val alert: Alert = new Alert(AlertType.CONFIRMATION)
      alert.setTitle("Add a Card")
      alert.setHeaderText("Choose a Question and Answer")
      val grid = new GridPane
      val quest_text = new TextField()
      val ans_text = new TextField()
      //subjs.subjs.foreach(sub => subj_box.getItems.add(sub.name))
      grid.add(new Label("Question: "), 0, 0)
      grid.add(quest_text, 1, 0)
      grid.add(new Label("Answer: "), 0, 1)
      grid.add(ans_text, 1, 1)
      alert.getDialogPane.setContent(grid)
      val result = alert.showAndWait()

      result.get match {
        case ButtonType.OK => val card = (quest_text.getText.trim, ans_text.getText.trim, 0, subj_box.getSelectionModel.getSelectedItem.trim, LocalDate.now)
          if (!(quest_text.getText.isEmpty || ans_text.getText.isEmpty)) {
            DeckController setDeck deck.addCard(card)
            deck = DeckController.getDeck
            cardList.getItems.add(s"${card._1} - ${card._4}")
          }
        case ButtonType.CANCEL =>
      }
    }
  }

  def deleteCard(): Unit = {
    val question = cardList.getSelectionModel.getSelectedItem.split("-").toList.head.trim
    val card = deck.getCard(question)
    DeckController setDeck deck.removeCard(card)
    deck = DeckController.getDeck
    cardList.getItems.remove(cardList.getSelectionModel.getSelectedItem)
  }

  def launchCorrect(): Unit = {
    val alert = new Alert(AlertType.INFORMATION)
    alert.setTitle("Your answer is...")
    alert.setHeaderText("CORRECT!")
    alert.showAndWait()
  }

  def launchWrong(card: Card): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("Your answer is...")
    alert.setHeaderText(s"Incorrect! The correct answer is:\n${card._2}")
    alert.showAndWait()
  }

  def doQuiz(): Unit ={
    if(subj_box.getSelectionModel.getSelectedItem == null) {
      launchAlert()
    }
    else {
      val card_deck = deck.ask(subj_box.getSelectionModel.getSelectedItem.trim)
      deck = card_deck._2
      DeckController.setDeck(deck)
      val alert: Alert = new Alert(AlertType.CONFIRMATION)
      alert.setTitle("Quiz - Give Us An Answer")
      alert.setHeaderText(card_deck._1._1)
      val gridPane = new GridPane
      gridPane.add(new Label("Answer: "), 0, 0)
      val answerField = new TextField()
      gridPane.add(answerField, 1, 0)
      alert.getDialogPane.setContent(gridPane)
      val result = alert.showAndWait()
      result.get() match {
        case ButtonType.OK =>
          val correct = deck.answer(card_deck._1, answerField.getText)
          deck = correct._2
          DeckController.setDeck(deck)
          if (correct._1)
            launchCorrect()
          else
            launchWrong(card_deck._1)
        case ButtonType.CANCEL =>
      }
    }

  }

  def launchAlert(): Unit = {
    val alert = new Alert(AlertType.WARNING)
    alert.setTitle("WARNING")
    alert.setHeaderText("You must select a subject!")
    alert.showAndWait()
  }

}

object DeckController {

  var deck: Deck = _

  lazy val firstDeck: Deck = loadDeck

  private def loadDeck: Deck = {
    try {
      val masterFileContent = Util.readFromFile("deck.obj")
      Deck.fromString(masterFileContent)
    } catch {
      case _: FileNotFoundException =>
    Deck(List(), RandomWithState(0))
    }
  }

  private def setDeck(newDeck: Deck): Unit = {
    deck = newDeck
  }

  def getDeck: Deck = {
    if (deck == null) {
      deck = firstDeck
    }
    deck
  }

}