package controllers


import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane

class MainController {

  @FXML private var scheduleButton: Button = _
  @FXML private var subjectsButton: Button = _
  @FXML private var deckButton: Button = _
  @FXML private var remindersButton: Button = _
  @FXML private var notesButton: Button = _
  @FXML private var homeButton: Button = _
  @FXML private var big_pane: BorderPane = _

  def homeButtonClicked(): Unit = {

  }

  def scheduleButtonClicked(): Unit = {
    /*val fxmlLoader = new FXMLLoader(getClass.getResource(("controllers.ScheduleController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()
    scheduleButton.getScene().setRoot(mainViewRoot)*/

    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/ScheduleController.fxml"))
    big_pane.setCenter(fxmlLoader.load)
//    main_pane.getChildren.clear()
//    main_pane.getChildren.add(fxmlLoader.load())
  }

  def subjectsButtonClicked(): Unit = {
    //var controller: controllers.SubjectsManagerController
    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/SubjectsManagerController.fxml"))
    big_pane.setCenter(fxmlLoader.load)
//    main_pane.getChildren.clear()
//    main_pane.getChildren.add(fxmlLoader.load())
    val controller_child: SubjectsManagerController = fxmlLoader.getController
    controller_child.setParent(this)
    //val mainViewRoot: Parent = fxmlLoader.load()
    //fxmlLoader.setRoot(main_pane)
    //fxmlLoader.setController(controller)

    //subjectsButton.getScene().setRoot(mainViewRoot)
  }

  def deckButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/DeckController.fxml"))
    big_pane.setCenter(fxmlLoader.load)
    //main_pane.getChildren.clear()
   // main_pane.getChildren.add(fxmlLoader.load())
  }

  def remindersButtonClicked(): Unit = {
    /*val fxmlLoader = new FXMLLoader(getClass.getResource(("controllers.RemindersController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()
    //val scene = new Scene(mainViewRoot)
    remindersButton.getScene().setRoot(mainViewRoot)
    //val aboutStage: Stage = new Stage()
    //aboutStage.setScene(scene)
    //aboutStage.show()*/
    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/RemindersController.fxml"))
    big_pane.setCenter(fxmlLoader.load)
//    main_pane.getChildren.clear()
//    main_pane.getChildren.add(fxmlLoader.load())
  }

  def notesButtonClicked(): Unit = {
    /*val fxmlLoader = new FXMLLoader(getClass.getResource(("NotesManagerController.fxml")))
    val mainViewRoot: Parent = fxmlLoader.load()
    notesButton.getScene().setRoot(mainViewRoot)*/
    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/NotesManagerController.fxml"))
    big_pane.setCenter(fxmlLoader.load)
//    main_pane.getChildren.clear()
//    main_pane.getChildren.add(fxmlLoader.load())
  }

  /*@FXML
  def handleMenuButton(event: ActionEvent): Unit = {
    if(event.getSource() == aboutButton)
      aboutPane.toFront()
    else if(event.getSource() == notesButton)
      notesPane.toFront()

  }*/

  def getMainPane: BorderPane = {
    big_pane
  }


}