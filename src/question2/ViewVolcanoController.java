package question2;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;
import static question2.VolcanoHelperFunctions.*;

/**
 * View Volcano Controller - initializes and populates volcano table, and
 * populates eruption table when a volcano is chosen. Allows all cells to be
 * edited when double click. Shows given volcano description and average time
 * between eruptions.
 *
 * Current bugs - for some reason, the first edit made in the volcano table is
 * not reflected in the tableview. I don't know why it's doing this, but it is
 * updating the csv file properly.
 *
 * @author Joshua Coss
 */
public class ViewVolcanoController implements Initializable {

    VolcanoListSingleton volData = VolcanoListSingleton.getInstance();
    HashSet<String> names;
    ObservableList<String> volcanoNames;
    String columnName;
    ObservableList<Volcano> data;

    ObservableList<String> typeOptions = FXCollections.observableArrayList(
            "Cinder Cone", "Composite", "Shield", "Lava Dome");
    ObservableList<String> volClassOptions = FXCollections.observableArrayList(
            "Volcanic Field", "Cone Volcano", "Caldera Volcano");
    ObservableList<String> erupClassOptions = FXCollections.observableArrayList(
            "Hydrothermal", "Phreatic", "Phreatomagmatic", "Lava", "Strombolian",
            "Hawaian", "Vulcanian", "Subplinian", "Plinian");
    ObservableList<String> eraOptions = FXCollections.observableArrayList(
            "BC", "BCE");

    HashMap<String, Integer> volColumnMap = new HashMap<>();
    HashMap<String, Integer> erupColumnMap = new HashMap<>();

    @FXML
    private Button refreshVolcanoTableButton;
    @FXML
    private Button updateDescriptionButton;

    @FXML
    private TableView<Eruption> eruptionView;
    @FXML
    private TableColumn<Eruption, Integer> yearColumn;
    @FXML
    private TableColumn<Eruption, String> eraColumn;
    @FXML
    private TableColumn<Eruption, Float> VEIColumn;
    @FXML
    private TableColumn<Eruption, String> eruptionClassificationColumn;

    @FXML
    private TableView<Volcano> volcanoView;
    @FXML
    private TableColumn<Volcano, String> nameColumn;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextArea averageArea;
    @FXML
    private TableColumn<Volcano, Double> latitudeColumn;
    @FXML
    private TableColumn<Volcano, Double> longitudeColumn;
    @FXML
    private TableColumn<Volcano, String> typeColumn;
    @FXML
    private TableColumn<Volcano, String> volcanoClassificationColumn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        volColumnMap.put("name", 0);
        volColumnMap.put("latitude", 2);
        volColumnMap.put("longitude", 3);
        volColumnMap.put("type", 4);
        volColumnMap.put("classification", 5);

        erupColumnMap.put("year", 2);
        erupColumnMap.put("era", 3);
        erupColumnMap.put("vei", 4);
        erupColumnMap.put("classification", 5);

        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        eraColumn.setCellValueFactory(new PropertyValueFactory<>("era"));
        VEIColumn.setCellValueFactory(new PropertyValueFactory<>("VEI"));
        eruptionClassificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        latitudeColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
        longitudeColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        volcanoClassificationColumn.setCellValueFactory(new PropertyValueFactory<>("classification"));

        eruptionView.setEditable(true);
        eruptionView.getSelectionModel().setCellSelectionEnabled(true);
        yearColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        eraColumn.setCellFactory(ComboBoxTableCell.forTableColumn(eraOptions));
        VEIColumn.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        eruptionClassificationColumn.setCellFactory(ComboBoxTableCell.forTableColumn(erupClassOptions));

        volcanoView.setEditable(true);
        volcanoView.getSelectionModel().setCellSelectionEnabled(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        latitudeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        latitudeColumn.setCellFactory(col -> new TableCell<Volcano, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {

                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(toDMS(item, "lat"));
                }
            }

        });
        longitudeColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        longitudeColumn.setCellFactory(col -> new TableCell<Volcano, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {

                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(toDMS(item, "long"));
                }
            }
        });

        typeColumn.setCellFactory(ComboBoxTableCell.forTableColumn(typeOptions));
        volcanoClassificationColumn.setCellFactory(ComboBoxTableCell.forTableColumn(typeOptions));

        try {
            if (volData != null) {
                volData.clear();
            }
            volData = getData();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VolcanoMainController.class.getName()).log(Level.SEVERE, null, ex);
        }

        data = FXCollections.observableArrayList(volData.getVolcanoList());
        volcanoView.setItems(data);

        final ObservableList<TablePosition> volcanoCells
                = volcanoView.getSelectionModel().getSelectedCells();
        volcanoCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change change) {
                for (TablePosition pos : volcanoCells) {
                    if (pos.getTableColumn().getText().toLowerCase() != null) {
                        columnName = pos.getTableColumn().getText().toLowerCase();
                    }
                }
                Volcano v = volcanoView.getSelectionModel().getSelectedItem();
                if (v != null) {
                    volData.setCurrentVolcano(v.getName());
                    showDescription(v, descriptionArea, averageArea);
                    showEruptions(v, eruptionView);
                }
            }
        ;
        });
        
        final ObservableList<TablePosition> eruptionCells
                = eruptionView.getSelectionModel().getSelectedCells();
        eruptionCells.addListener(new ListChangeListener<TablePosition>() {
            @Override
            public void onChanged(Change change) {
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
     * changeVolcanoSelectedEvent - saves changes into the volcanoes.csv file when
     * a cell is edited in the volcanoview table
     * @param editedCell
     * @throws IOException
     * @throws CsvException 
     */
    public void changeVolcanoSelectedEvent(CellEditEvent editedCell) throws IOException, CsvException {
        int index = volColumnMap.get(columnName);
        Volcano volcanoSelected = volcanoView.getSelectionModel().getSelectedItem();
        String name = volcanoSelected.getName();
        String newValue = editedCell.getNewValue().toString();
        switch (index) {
            case 0:
                String oldName = volcanoSelected.getName();
                volcanoSelected.setName(newValue);
                updateRow("volcanoes.csv", oldName, newValue, index, name);
                updateRow("eruptions.csv", oldName, newValue, 1, name);
                break;
            case 2:
                double oldLatitude = volcanoSelected.getLatitude();
                volcanoSelected.setLatitude(Double.parseDouble(newValue));
                updateRow("volcanoes.csv", String.valueOf(oldLatitude),
                        newValue, index, name);
                break;
            case 3:
                double oldLongitude = volcanoSelected.getLatitude();
                volcanoSelected.setLongitude(Double.parseDouble(newValue));
                updateRow("volcanoes.csv", String.valueOf(oldLongitude),
                        newValue, index, name);
                break;
            case 4:
                String oldType = volcanoSelected.getType();
                volcanoSelected.setType(newValue);
                updateRow("volcanoes.csv", oldType, newValue, index, name);
                break;
            case 5:
                String oldClassification = volcanoSelected.getClassification();
                volcanoSelected.setClassification(editedCell.getNewValue().toString());
                updateRow("volcanoes.csv", oldClassification, newValue, index, name);
                break;
        }
        updateView(volcanoView);
    }

    /**
     * changeEruptionSelectedEvent - saves changes into the eruptions.csv file
     * when a cell is edited in the eruptionview table
     *
     * @param editedCell
     * @throws IOException
     * @throws CsvException
     */
    public void changeEruptionSelectedEvent(CellEditEvent editedCell)
            throws IOException, CsvException {

        int index = erupColumnMap.get(columnName);
        Eruption eruptionSelected = eruptionView.getSelectionModel().getSelectedItem();
        String name = String.valueOf(eruptionSelected.getErupNum());
        String newValue = editedCell.getNewValue().toString();
        switch (index) {
            case 2:
                int oldYear = eruptionSelected.getYear();
                eruptionSelected.setYear(Integer.parseInt(editedCell.getNewValue().toString()));
                updateRow("eruptions.csv", String.valueOf(oldYear), newValue, index, name);
                break;
            case 3:
                String oldEra = eruptionSelected.getEra();
                eruptionSelected.setEra(editedCell.getNewValue().toString());
                updateRow("eruptions.csv", oldEra, newValue, index, name);
                break;
            case 4:
                float oldVEI = eruptionSelected.getVEI();
                eruptionSelected.setVEI((float) editedCell.getNewValue());
                updateRow("eruptions.csv", String.valueOf(oldVEI), newValue, index, name);
                break;
            case 5:
                String oldClass = eruptionSelected.getClassification();
                eruptionSelected.setClassification(editedCell.getNewValue().toString());
                updateRow("eruptions.csv", oldClass, newValue, index, name);
                break;
        }
    }

    /**
     * updateRow - updates the row in a given csv file
     *
     * @param filename
     * @param oldValue
     * @param newValue
     * @param index
     * @param name
     * @throws IOException
     * @throws CsvException
     */
    public void updateRow(String filename, String oldValue, String newValue,
            int index, String name) throws IOException, CsvException {

        Scanner inFile = new Scanner(new File(filename));
        CSVWriter writer = new CSVWriter(new FileWriter("buffer.csv"));

        while ((inFile.hasNextLine())) {
            String line = inFile.nextLine();
            String[] parts = line.replace("\"", "").split(",");
            if (parts[index].equals(oldValue) && ((parts[1].equals(name)
                    || parts[0].equals(name)))) {
                parts[index] = newValue;
            }
            writer.writeNext(parts);
        }
        writer.close();
        inFile.close();

        Path sourceDirectory = Paths.get("buffer.csv");
        Path targetDirectory = Paths.get(filename);

        //copy source to target using Files Class
        Files.copy(sourceDirectory, targetDirectory,
                StandardCopyOption.REPLACE_EXISTING);

        //delete buffer file
        File buffer = new File("buffer.csv");
        buffer.delete();

    }

    /**
     * updateDescription - when the description field is edited, updates the csv
     * file with the new description
     *
     * @throws IOException
     * @throws CsvException
     */
    public void updateDescription() throws IOException, CsvException {
        Volcano v = volData.getCurrentVolcano();
        String oldText = v.getDescription();
        String newText = descriptionArea.getText().replace("\n", "~").replace(",", "`");
        if (v != null) {
            v.setDescription(newText);
        }
        updateRow("volcanoes.csv", oldText, newText, 1, v.getName());
        updateView(volcanoView);
    }

    /**
     * updateButtonPressed - updates the volcanoview when the update button is
     * pressed
     */
    public void updateButtonPressed() {
        updateView(volcanoView);
    }

}
