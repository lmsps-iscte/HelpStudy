package classes

import classes.Deck.Card

import java.time.LocalDate
import scala.annotation.tailrec

//Flashcard deck with a random number generator
case class Deck(cards: List[Card], ro: RandomWithState) {

  def addCard(card: Card): Deck = Deck.addCard(this, card)

  def removeCard(card: Card): Deck = Deck.removeCard(card, this)

  //Ask the user a question from a random card at a valid time, allow for response and update the deck
  def ask(course: String): (Card, Deck) = Deck.ask(this, course)

  def answer(card: Card, answer: String): (Boolean, Deck) = Deck.answer(this, card, answer)

  //Print deck cards, opt is an optional String that works as a filter is not blank
  def printCards(cards: List[Card])(opt: String): Unit = Deck.printCards(cards)(opt)

  def getCard(question: String) : Card = Deck.getCard(question, this)

  override def toString: String = Deck.toString(cards)
}

object Deck {

  type Card = (String, String, Int, String, LocalDate)

  val boundary = "////0xFFFF////EOF"

  def addCard(deck: Deck, card: Card): Deck = Deck(card :: deck.cards, deck.ro)

  def removeCard(card: Card, deck: Deck): Deck = {
    Deck(deck.cards.filterNot(c => c == card), deck.ro)
  }

  def getCard(question: String, deck: Deck): Card = {
    deck.cards.filter(card => card._1 == question).head
  }

  private def available_cards(course_cards: List[Card]): List[Card] = {
    course_cards filter (card => card._5.isBefore(LocalDate.now()) || card._5.isEqual(LocalDate.now()))
  }

  def ask(deck: Deck, course: String): (Card, Deck) = {
    val course_cards = deck.cards.filter(card => card._4.equalsIgnoreCase(course))
    println(deck.cards)
    val cards = available_cards(course_cards)
    if (cards.isEmpty) {
      return (("classes.Deck is Empty", "", 0, "", LocalDate.now()),deck)
    }
    val num_ro = deck.ro.nextIntRange(deck.cards.size)
    val card = cards(num_ro._1)
    (card, Deck(deck.cards,num_ro._2))
  }

  def answer(deck: Deck, card: Card, answer: String): (Boolean, Deck) = {
    val index = deck.cards.indexOf(card)
    if (answer.compareToIgnoreCase(card._2) == 0) {
      val newDate = LocalDate.now().plusDays(deck.cards(index)._3 /* * 2*/)
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
    case Nil => ""
    case head :: Nil => s"${head._3},${head._5},${head._4} $boundary ${head._1} $boundary ${head._2}"
    case head :: tail => s"${head._3},${head._5},${head._4} $boundary ${head._1} $boundary ${head._2}\\n${toString(tail)}"
  }

  def parseItem(item: String): (String, String, Int, String, LocalDate) = {
    val list = item.split(",")
    val num = list.head.toInt
    val time = LocalDate.parse(list(1))
    val subj = list(2).split(boundary)(0).trim
    val q = item.split(boundary)(1).trim
    val a = item.split(boundary)(2).trim
    (q,a,num,subj,time)
  }

  def fromString(toParse: String): Deck = {
    @tailrec
    def aux(deck: Deck, items: List[String]): Deck = items match {
      case "" :: tail => deck
      case item :: Nil => deck.addCard(parseItem(item))
      case head :: tail => aux(deck.addCard(parseItem(head)), tail)
    }
    println(toParse.split("\\\\n").toList)
    aux(Deck(List(), RandomWithState(0)), toParse.split("\\\\n").toList)
  }

}
