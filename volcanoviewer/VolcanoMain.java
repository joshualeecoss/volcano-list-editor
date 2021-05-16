package volcanoviewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This program is designed to allow a user to add, edit, and delete volcanoes 
 * from a VolcanoList, as well as add, edit, and delete eruptions from each
 * volcano in VolcanoList.
 * 
 * @author Joshua Coss
 */
public class VolcanoMain extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("VolcanoMain.fxml"));
        stage.setTitle("Volcano List Editor");
        Scene scene = new Scene(root);
        stage.setResizable(false);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * main - launches the application
     * 
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }

}
