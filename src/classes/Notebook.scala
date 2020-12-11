package classes

import classes.Notebook.Note
import java.io._
import java.nio.file.Paths
import scala.annotation.tailrec
import scala.io.Source

case class Notebook(notes: List[Note]) {

  //Adds a new note

  def addNote(note: Note): Notebook = Notebook.addNote(this, note)

  //Removes a given note

  def removeNote(note: Note): Notebook = Notebook.removeNote(this, note)

  //Get a filtered list of notes of a certain Course

  def getNotesbyCUnit(cunit: String): List[Note] = Notebook.getNotesbyCUnit(this, cunit)

  //Gets a note with a given title and curricular unit

  def getNote(title: String, cunit: String): Note = Notebook.getNote(this, title, cunit)

  //Import a note from a file, the name of the file is the title

  def importFromFile(file: String, cunit: String): Notebook = Notebook.importFromFile(this, file, cunit)

  //Export a note to a file, the name of the file is the title

  def exportToFile(note: Note, opt: String): Unit = Notebook.exportToFile(this, note, opt)

  //Prints all notes, filtered by Course if provided

  def printNotes(opt: String = ""): Unit = Notebook.printNotes(this)(opt)

  //Sorts notes by Course by default or TITLE if provided "TITLE"

  def sortNotesBy(opt: String): Notebook = Notebook.sortNotesBy(this, opt)

  //Override of the oiginal toString method

  override def toString: String = Notebook.toString(notes)
}

object Notebook {

  type Note = (String, String, String)

  val boundary = "////0xFFFF////EOF"

  //Adds a new note

  def addNote(nbook: Notebook, note: Note): Notebook = Notebook(note :: nbook.notes)

  //Removes a given note

  def removeNote(notebook: Notebook, note: Note): Notebook = Notebook(notebook.notes.filter(n => !n.equals(note)))

  //Gets a note with a given title and curricular unit

  def getNote(notebook: Notebook, title: String, cunit: String): Note = {
    notebook.notes.filter(note => note._1.equals(title) && note._3.equals(cunit)).head
  }

  //Get a filtered list of notes of a certain Course

  def getNotesbyCUnit(nbook: Notebook, cunit: String): List[Note] = nbook.notes.filter(_._3 equalsIgnoreCase cunit)

  //Import a note from a file, the name of the file is the title

  def importFromFile(nb: Notebook, file: String, cunit: String): Notebook = {
    val handle = Source.fromFile(Paths.get(file).toAbsolutePath.toString)
    val body = handle.getLines.mkString
    handle.close()
    val newNote = (Paths.get(file).getFileName.toString.stripSuffix(".txt"), body, cunit)
    Notebook.addNote(nb, newNote)
  }

  //Export a note to a file, the name of the file is the title

  def exportToFile(nb: Notebook, note: Note, opt: String): Unit = {
    if (opt.equals("normal")) {
      val file = Paths.get(System.getProperty("user.dir"), note._3, note._1 + ".txt")
      if (!file.getParent.toFile.exists()) file.getParent.toFile.mkdirs()
      val pw = new PrintWriter(file.toFile)
      pw.write(note._2)
      pw.close()
    }
    else {
      val file = Paths.get(System.getProperty("user.home") + "/Desktop", note._1 + ".txt")
      if (!file.getParent.toFile.exists()) file.getParent.toFile.mkdirs()
      val pw = new PrintWriter(file.toFile)
      pw.write(note._2)
      pw.close()
    }
  }

  //Sorts notes by Course by default or TITLE if provided "TITLE"

  def sortNotesBy(nb: Notebook, opt: String): Notebook = {
    if (opt == "TITLE") Notebook(nb.notes.sortBy(note => note._1))
    else Notebook(nb.notes.sortBy(note => note._3))
  }

  //Prints all notes, filtered by Course if provided

  def printNotes(nb: Notebook)(opt: String): Unit = {
    @tailrec
    def aux(notes: List[Note], index: Int): Unit = {
      notes match {
        case Nil =>
        case note :: tail => println(s"$index:- Title: ${note._1}\nBody: ${note._2}\nCunit: ${note._3}")
          aux(tail, index + 1)
      }
    }

    if (opt == "") {
      aux(nb.notes, 0)
    } else {
      aux(nb.notes.filter(note => note._3 == opt), 0)
    }
  }

  //Override of the oiginal toString method

  def toString(notes: List[Note]): String = notes match {
    case Nil => ""
    case head :: Nil => s"${Paths.get(System.getProperty("user.dir"), head._3, head._1 + ".txt")} $boundary ${head._3} $boundary"
    case head :: tail => s"${Paths.get(System.getProperty("user.dir"), head._3, head._1 + ".txt")} $boundary ${head._3} $boundary" +
      s"\\n ${toString(tail)}"
  }

  //Creates a Note from a string

  def parseItem(item: String): (String, String) = {
    val path = item.split(boundary)(0).trim
    val cunit = item.split(boundary)(1).trim
    (path, cunit)
  }

  //Creates a Notebook from a string

  def fromString(toParse: String): Notebook = {
    @tailrec
    def aux(nb: Notebook, items: List[String]): Notebook = items match {
      case Nil => nb
      case "" :: tail => nb
      case head :: Nil => importFromFile(nb, parseItem(head)._1, parseItem(head)._2)
      case head :: tail => aux(importFromFile(nb, parseItem(head)._1, parseItem(head)._2), tail)
    }

    aux(Notebook(List()), toParse.split("\\\\n").toList)
  }

}