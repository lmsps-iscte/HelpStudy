package classes

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class App extends Application {

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Help Study")
    val fxmlLoader = new FXMLLoader(getClass.getResource("../resources/MainController.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}


object HelpStudyApp {

  //carregar o ficheiro hs

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[App], args: _*)
  }
}
