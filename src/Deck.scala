import java.time.LocalDate

import Deck.Card

import scala.annotation.tailrec
import scala.io.StdIn.readLine

//Flashcard deck with a random number generator
case class Deck(cards: List[Card], ro: RandomWithState) {

  def addCard(card: Card): Deck = Deck.addCard(this, card)

  def removeCard(card: Card): Deck = Deck.removeCard(card, this)

  //Ask the user a question from a random card at a valid time, allow for response and update the deck
  def ask(course: String): (Card, Deck) = Deck.ask(this, course)

  //Print deck cards, opt is an optional String that works as a filter is not blank
  def printCards(cards: List[Card])(opt: String): Unit = Deck.printCards(cards)(opt)

  def getCard(question: String) : Option[Card] = Deck.getCard(question, this)

  override def toString: String = Deck.toString(cards)
}

object Deck {

  type Card = (String, String, Int, String, LocalDate)

  val boundary = "////0xFFFF////EOF"

  def addCard(deck: Deck, card: Card): Deck = Deck(card :: deck.cards, deck.ro)

  def removeCard(card: Card, deck: Deck): Deck = {
    Deck(deck.cards.filterNot(c => c == card), deck.ro)
  }

  def getCard(question: String, deck: Deck): Option[Card] = {
    deck.cards.find(card => card._1 == question)
  }

  private def available_cards(course_cards: List[(String, String, Int, String, LocalDate)]) = {
    course_cards filter (card => card._5.isBefore(LocalDate.now()))
  }

  def ask(deck: Deck, course: String): (Card, Deck) = {
    val course_cards = deck.cards.filter(_._4 == course)
    val cards = available_cards(course_cards)
    if (cards.isEmpty) {
      return (("Deck is Empty", "", 0, "", LocalDate.now()),deck)
    }
    val num_ro = deck.ro.nextIntRange(deck.cards.size)
    val card = cards(num_ro._1)
    (card, Deck(deck.cards,num_ro._2))
  }

  def answer(deck: Deck, card: Card, answer: String): (Boolean, Deck) = {
    val index = deck.cards.indexOf(card)
    if (answer.compareToIgnoreCase(card._2) == 0) {
      val newDate = LocalDate.now().plusDays(deck.cards(index)._3 * 2)
      if (deck.cards(index)._3 >= 5)
        (true, Deck(deck.cards.updated(index, (card._1, card._2, card._3 + 1, card._4, LocalDate.MAX)), deck.ro))
      else
        (true, Deck(deck.cards.updated(index, (card._1, card._2, card._3 + 1, card._4, newDate)), deck.ro))
    }
    else {
      (false, Deck(deck.cards.updated(index, (card._1, card._2, 1, card._4, LocalDate.now())), deck.ro))
    }
  }

  def printCards(cards: List[Card])(opt: String = ""): Unit = {
    @tailrec
    def aux(card_list: List[Card], acc: String): Unit = cards match {
      case card :: Nil => s"Q: ${card._1} A: ${card._2} Level: ${card._3} Next Due Date: ${card._5}"
      case card :: tail => aux(tail, s"Q: ${card._1} A: ${card._2} Level: ${card._3} Next Due Date: ${card._5}\n")
    }

    if (opt == "")
      aux(cards, "")
    else aux(cards.filter(card => card._4 == opt), "")
  }

  def toString(cards: List[Card]): String = cards match {
    case head :: Nil => s"${head._3},${head._5},${head._4} $boundary ${head._1} $boundary ${head._2}}"
    case head :: tail => s"${head._3},${head._5},${head._4} $boundary ${head._1} $boundary ${head._2}\n${toString(tail)}"
  }

  def parseItem(item: String): (String, String, Int, String, LocalDate) = {
    val num = item.split(",")(0).toInt
    val time = LocalDate.parse(item.split(",")(1))
    val subj = item.split(",")(2).split(boundary)(0).trim
    val q = item.split(boundary)(1).trim
    val a = item.split(boundary)(2).trim
    (q,a,num,subj,time)
  }

  def fromString(toParse: String): Deck = {
    @tailrec
    def aux(deck: Deck, items: List[String]): Deck = items match {
      case item :: Nil => deck.addCard(parseItem(item))
      case head :: tail => aux(deck.addCard(parseItem(head)), tail)
    }
    aux(Deck(List(), RandomWithState(0)), toParse.split("\n").toList)
  }

}
