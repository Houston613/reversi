
package view;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.Objects;

public class Main extends Application{
    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("MainFxml.fxml")));
        primaryStage.setScene(new Scene(root,1024,1024));
        primaryStage.setTitle("Hello JavaFX");
        primaryStage.show();
    }
}