package question2;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import static question2.VolcanoHelperFunctions.*;

/**
 * Add Eruption Controller - allows a user to input data to create a new
 * eruption object on a specific volcano
 *
 * @author Joshua Coss
 */
public class AddEruptionController implements Initializable {

    VolcanoListSingleton volData = VolcanoListSingleton.getInstance();

    HashSet<String> names;
    ObservableList<String> volcanoNames;
    String columnName;

    int erupNum = volData.getErupNum();
    //dropdown lists
    ObservableList<String> eraOptions = FXCollections.observableArrayList("BC", "BCE");
    ObservableList<String> classificationOptions = FXCollections.observableArrayList(
            "Hydrothermal", "Phreatic", "Phreatomagmatic", "Lava", "Strombolian",
            "Hawaian", "Vulcanian", "Subplinian", "Plinian");

    @FXML
    private TextField yearField;
    @FXML
    private ComboBox<String> eraBox;
    @FXML
    private Slider VEISlider;
    @FXML
    private TextField VEIField;
    @FXML
    private ComboBox<String> classificationBox;
    @FXML
    private ComboBox<String> volcanoDropdown;
    @FXML
    private Button addEruptionButton;
    @FXML
    private Button deleteEruptionButton;
    @FXML
    private Text nameText;
    @FXML
    private TableView<Eruption> eruptionTable;

    @FXML
    private TableColumn<Eruption, Integer> yearColumn;
    @FXML
    private TableColumn<Eruption, String> eraColumn;
    @FXML
    private TableColumn<Eruption, Float> VEIColumn;
    @FXML
    private TableColumn<Eruption, String> eruptionClassificationColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //nameText.setText("Eruptions for " + volData.getCurrentVolcano().getName());
        eraBox.setItems(eraOptions);
        classificationBox.setItems(classificationOptions);

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        eraColumn.setCellValueFactory(new PropertyValueFactory<>("era"));
        VEIColumn.setCellValueFactory(new PropertyValueFactory<>("VEI"));
        eruptionClassificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));

        eruptionTable.getSelectionModel().getSelectedItem();
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        eraColumn.setCellFactory(ComboBoxTableCell.forTableColumn(eraOptions));
        VEIColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        eruptionClassificationColumn.setCellFactory(ComboBoxTableCell.forTableColumn(classificationOptions));

        VEIField.textProperty().bind(VEISlider.valueProperty().asString("%.1f"));

        setNameDropdown(volcanoDropdown);

        //listener to let the controller know if an eruption is selected in the eruption table view
        final ObservableList<TablePosition> eruptionCells = eruptionTable.getSelectionModel().getSelectedCells();
        eruptionCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(ListChangeListener.Change change) {
                for (TablePosition pos : eruptionCells) {
                    if (pos.getTableColumn().getText().toLowerCase() != null) {
                        columnName = pos.getTableColumn().getText().toLowerCase();
                    }
                }
            }
        ;
    }

    );   
    }    
    
    /**
     * addEruptionButtonPressed - sets the currently selected volcano, takes the
     * values from the input fields, creates a new eruption object, and adds
     * the eruption to the currently selected volcano
     * 
     * @param event
     * @throws Exception 
     */
    public void addEruptionButtonPressed(ActionEvent event) throws Exception {

        volData.setCurrentVolcano(volcanoDropdown.getValue());
        Volcano v = volData.getCurrentVolcano();

        int year = Integer.parseInt(yearField.getText());
        String era = eraBox.getValue();
        String VEI = VEIField.getText();
        String classification = classificationBox.getValue();

        try ( CSVWriter writer = new CSVWriter(new FileWriter("eruptions.csv", true))) {
            String data = volData.getErupNum() + "," + v.getName()
                    + "," + year + "," + era + "," + VEI + "," + classification;
            String[] record = data.split(",");
            writer.writeNext(record);
        }
        Eruption e = new Eruption(volData.getErupNum(), v.getName(), year, era,
                Float.parseFloat(VEI), classification);

        //add eruption to volcano
        v.addEruption(e);
        //increase erupNum id number
        volData.erupNumPlusOne();
        //update eruption list
        updateEruptions(v);

        //reset all fields
        eraBox.setValue(null);
        yearField.clear();
        VEISlider.setValue(0);
        classificationBox.setValue(null);

    }

    /**
     * populateEruptionList - populates the list of eruptions with the eruptions
     * in the currently selected volcano when dropdown item is selected
     */
    public void populateEruptionList() {
        String name = volcanoDropdown.getValue();
        if (name != null) {
            for (Volcano entry : volData.getVolcanoList()) {
                if (name.equals(entry.getName())) {
                    ObservableList<Eruption> eruptions = FXCollections.observableArrayList(entry.getAllEruptions());
                    eruptionTable.setItems(eruptions);
                }
            }
        }
    }

    /**
     * updateEruptions - updates eruption list based on currently selected
     * volcano
     *
     * @param v - Volcano object
     */
    public void updateEruptions(Volcano v) {
        if (!v.getAllEruptions().isEmpty()) {
            ObservableList<Eruption> eruptions = FXCollections.observableArrayList(v.getAllEruptions());
            eruptionTable.setItems(eruptions);
        }
    }

    /**
     * deleteEruption - takes the selected eruption, and removes it from both
     * the volcano object and the eruptions.csv
     *
     * @throws IOException
     * @throws CsvException
     */
    public void deleteEruption() throws IOException, CsvException {

        volData.setCurrentVolcano(volcanoDropdown.getValue());
        Volcano v = volData.getCurrentVolcano();
        Eruption e = eruptionTable.getSelectionModel().getSelectedItem();
        for (Eruption entry : v.getAllEruptions()) {
            if (e.getName().equals(entry.getName()) && e.getErupNum() == entry.getErupNum()) {
                v.removeEruption(e);
            }
        }

        updateCSV("eruptions.csv", e.getErupNum());

        updateEruptions(v);
    }

    /**
     * updateCSV - updates the csv file
     *
     * @param filename
     * @param erupNum
     * @throws IOException
     * @throws CsvException
     */
    public void updateCSV(String filename, int erupNum) throws IOException, CsvException {

        Scanner inFile = new Scanner(new File(filename));
        CSVWriter writer = new CSVWriter(new FileWriter("buffer.csv"));

        while ((inFile.hasNextLine())) {
            String line = inFile.nextLine();
            String[] parts = line.replace("\"", "").split(",");
            if (Integer.parseInt(parts[0]) != erupNum) {
                writer.writeNext(parts);
            }
        }
        writer.close();
        inFile.close();

        Path sourceDirectory = Paths.get("buffer.csv");
        Path targetDirectory = Paths.get(filename);

        //copy source to target using Files Class
        Files.copy(sourceDirectory, targetDirectory, StandardCopyOption.REPLACE_EXISTING);

        //delete buffer file
        File buffer = new File("buffer.csv");
        buffer.delete();

    }

    /**
     * onDropdownShown - updates the dropdown list when the volcanoDropdown is
     * clicked
     */
    public void onDropdownShown() {
        setNameDropdown(volcanoDropdown);
    }
}
