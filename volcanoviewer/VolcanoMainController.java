package volcanoviewer;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.Initializable;
import static volcanoviewer.VolcanoHelperFunctions.*;

/**
 * Main Controller Class - initializes and loads the data from CSV files into
 data objects (Volcano and Eruption) into the VolcanoListSingleton singleton object
 *
 * @author Joshua Coss
 */
public class VolcanoMainController implements Initializable {

    VolcanoListSingleton volData = VolcanoListSingleton.getInstance();

    /**
     * initialize the application, load the data into the VolcanoListSingleton
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (volData != null) {
                volData.clear();
            }
            volData = getData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VolcanoMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
