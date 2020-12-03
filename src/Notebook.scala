import java.io._
import java.nio.file.Paths

import Notebook.Note

import scala.annotation.tailrec
import scala.io.Source

case class Notebook(notes: List[Note]) {
  def addNote(note: Note): Notebook = Notebook.addNote(this, note)

  //Get a filtered list of notes of a certain Course
  def getNotesbyCUnit(cunit: String): List[Note] = Notebook.getNotesbyCUnit(this, cunit)

  //Import a note from a file, the name of the file is the title
  def importFromFile(file: String, cunit: String): Notebook = Notebook.importFromFile(this, file, cunit)

  //Export a note to a file, the name of the file is the title
  def exportToFile(index: Int): Unit = Notebook.exportToFile(this, index)

  //Prints all notes, filtered by Course if provided
  def printNotes(opt: String = ""): Unit = Notebook.printNotes(this)(opt)

  //Sorts notes by Course by default or TITLE if provided "TITLE"
  def sortNoteBy(opt: String): Notebook = Notebook.sortNotesBy(this, opt)

  override def toString(): String = Notebook.toString(notes)
}

object Notebook {

  type Note = (String, String, String)

  val boundary = "////0xFFFF////EOF"

  def addNote(nbook: Notebook, note: Note): Notebook = Notebook(note :: nbook.notes)

  def getNotesbyCUnit(nbook: Notebook, cunit: String): List[Note] = nbook.notes.filter(_._3 == cunit)

  def importFromFile(nb: Notebook, file: String, cunit: String): Notebook = {
    val body = Source.fromFile(Paths.get(file).toAbsolutePath.toString).getLines.mkString
    val newNote = (Paths.get(file).getFileName.toString.stripSuffix(".TXT"), body, cunit)
    Notebook.addNote(nb, newNote)
  }

  def exportToFile(nb: Notebook, index: Int): Unit = {
    val note = nb.notes(index)
    val file = Paths.get(System.getProperty("user.dir"), note._3, note._1 + ".txt")
    if (!file.getParent.toFile.exists()) file.getParent.toFile.mkdirs()
    val pw = new PrintWriter(file.toFile)
    pw.write(note._2)
    pw.close()
  }

  def sortNotesBy(nb: Notebook, opt: String): Notebook = {
    if (opt == "TITLE") Notebook(nb.notes.sortBy(note => note._1))
    else Notebook(nb.notes.sortBy(note => note._3))
  }

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

  def toString(notes: List[Note]): String = notes match {
    case head :: Nil => s"${head._1} $boundary ${head._2} $boundary ${head._3}"
    case head :: tail => s"${head._1} $boundary ${head._2} $boundary ${head._3} \n ${toString(tail)}"
  }

  def parseItem(item: String): (String, String, String) = {
    val title = item.split(boundary)(0).trim
    val body = item.split(boundary)(1).trim
    val cunit = item.split(boundary)(2).trim
    (title, body, cunit)
  }

  def fromString(toParse: String): Notebook = {
    @tailrec
    def aux(nb: Notebook, items: List[String]): Notebook = items match {
      case head :: Nil => nb.addNote(parseItem(head))
      case head :: tail => aux(nb.addNote(parseItem(head)),tail)
    }
    aux(Notebook(List()),toParse.split("\n").toList)
  }

  def main(args: Array[String]): Unit = {
    val notebook = Notebook(List())
    val n1 = notebook.addNote("Nota 1","Corpo 1","MC")
    val n2 = n1.addNote("Nota 2", "Corpo 2", "TS")
    val n3 = n2.addNote("Nota 3", "Corpo 3", "TS")
    print("ESTA É A NOTA 3 ANTES... "+n3)
    print("ESTE É O TOSTRING... "+n3.toString())
    val aux = n3.toString()
    val aux1 = fromString(aux)
    print("ESTE É O FROM STRING... "+aux1)
  }
}
