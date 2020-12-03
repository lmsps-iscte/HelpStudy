import java.time.LocalDate

import Deck.Card

import scala.annotation.tailrec
import scala.io.StdIn.readLine

//Flashcard deck with a random number generator
case class Deck(cards: List[Card], ro: RandomWithState) {

  def addCard(card: Card): Deck = Deck.addCard(this, card)

  //Ask the user a question from a random card at a valid time, allow for response and update the deck
  def ask(course: String): Deck = Deck.ask(this, course)

  //Print deck cards, opt is an optional String that works as a filter is not blank
  def printCards(cards: List[Card])(opt: String): Unit = Deck.printCards(cards)(opt)
}

object Deck {

  type Card = (String, String, Int, String, LocalDate)

  val boundary = "////0xFFFF////EOF"

  def addCard(deck: Deck, card: Card): Deck = Deck(card :: deck.cards, deck.ro)

  private def available_cards(course_cards: List[(String, String, Int, String, LocalDate)]) = {
    course_cards filter (card => card._5.isBefore(LocalDate.now()))
  }

  def ask(deck: Deck, course: String): Deck = {
    val course_cards = deck.cards.filter(_._4 == course)
    val cards = available_cards(course_cards)
    if (cards.isEmpty) {
      System.err.println("No available cards have been found")
      return deck
    }
    val num_ro = deck.ro.nextIntRange(deck.cards.size)
    val card = cards(num_ro._1)
    print(card._1)
    val answer = readLine.trim
    val index = deck.cards.indexOf(card)
    if (answer.compareToIgnoreCase(card._2) == 0) {
      println("Correct")
      val newDate = LocalDate.now().plusDays(deck.cards(index)._3 * 2)
      if (deck.cards(index)._3 >= 5)
        Deck(deck.cards.updated(index, (card._1, card._2, card._3 + 1, card._4, LocalDate.MAX)), num_ro._2)
      else
        Deck(deck.cards.updated(index, (card._1, card._2, card._3 + 1, card._4, newDate)), num_ro._2)
    }
    else {
      println("Wrong")
      Deck(deck.cards.updated(index, (card._1, card._2, 1, card._4, LocalDate.now())), num_ro._2)
    }
  }

  def printCards(cards: List[Card])(opt: String = ""): Unit = {
    @tailrec
    def aux(card_list: List[Card]): Unit = cards match {
      case Nil => Nil
      case card :: tail => println(s"Q: ${card._1} A: ${card._2} Level: ${card._3} Next Due Date: ${card._5}")
        aux(tail)
    }

    if (opt == "")
      aux(cards)
    else aux(cards.filter(card => card._4 == opt))
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
