package zhen;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for Zhen using FXML.
 */
public class Main extends Application {

    private Zhen zhen = new Zhen("database.ser");

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            assert fxmlLoader != null : "Can't find the FXML file.";
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            stage.setTitle("Zhen");
            fxmlLoader.<MainWindow>getController().setZhen(zhen);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
