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
}

object Notebook {

  type Note = (String, String, String)

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
}
