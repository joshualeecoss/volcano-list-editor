package question2;

import com.opencsv.exceptions.CsvException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import static question2.VolcanoHelperFunctions.*;

/**
 * Delete Volcano Controller - allows the user to delete a volcano from the
 * system
 *
 * @author Joshua Coss
 */
public class DeleteVolcanoController implements Initializable {

    VolcanoListSingleton volData = VolcanoListSingleton.getInstance();

    @FXML
    private Button deleteButton;

    @FXML
    private ComboBox<String> volcanoDropdown;
    @FXML
    private TextArea statsArea;
    @FXML
    private TableView<Eruption> erupTable;
    @FXML
    private TableColumn<Eruption, Integer> yearColumn;
    @FXML
    private TableColumn<Eruption, String> eraColumn;
    @FXML
    private TableColumn<Eruption, Float> VEIColumn;
    @FXML
    private TableColumn<Eruption, String> eruptionClassificationColumn;

    HashSet<String> names;
    ObservableList<String> volcanoNames;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        eraColumn.setCellValueFactory(new PropertyValueFactory<>("era"));
        VEIColumn.setCellValueFactory(new PropertyValueFactory<>("VEI"));
        eruptionClassificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));

        setNameDropdown(volcanoDropdown);

    }

    /**
     * deleteButtonPressed - removes the selected volcano from both the
     * volcanolist and csv file, and removes all eruptions associated with the
     * volcano from the csv file
     *
     * @param event
     * @throws IOException
     * @throws CsvException
     */
    public void deleteButtonPressed(ActionEvent event) throws IOException, CsvException {
        String name = volcanoDropdown.getValue();
        deleteVolcano(name);
        names = volData.getAllNames();
        updateCSV(name, "volcanoes.csv");
        updateCSV(name, "eruptions.csv");
        statsArea.setText(name + " deleted.");
        setNameDropdown(volcanoDropdown);
        erupTable.setItems(null);
    }

    /**
     * deleteVolcano - takes a string name and deletes the volcano with that
 name from the VolcanoListSingleton
     *
     * @param name
     */
    public void deleteVolcano(String name) {
        for (Volcano entry : volData.getVolcanoList()) {
            if (entry.getName().equals(name)) {
                volData.removeVolcano(entry);
                break;
            }
        }
    }

    /**
     * clickDropdown - sets the current volcano and shows the stats and
     * eruptions for the chosen volcano when selected
     */
    public void clickDropdown() {
        volData.setCurrentVolcano(volcanoDropdown.getValue());
        populateTextArea(volcanoDropdown, statsArea);
        showEruptions(volData.getCurrentVolcano(), erupTable);
    }

    /**
     * onDropdownShown - sets names in the dropdown list when the dropdown is
     * clicked
     */
    public void onDropdownShown() {
        setNameDropdown(volcanoDropdown);
    }

}
