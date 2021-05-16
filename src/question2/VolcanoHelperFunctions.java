package question2;

import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * Volcano Helper Functions - Functions that can be used across multiple
 * controllers
 *
 * @author Joshua Coss
 */
public class VolcanoHelperFunctions {

    static DecimalFormat averageFormat = new DecimalFormat("#.00");
    static VolcanoListSingleton volData = VolcanoListSingleton.getInstance();
    static ObservableList<Volcano> data;
    static HashSet<String> names;
    static ObservableList<String> volcanoNames;

    /**
     * getData - populates VolcanoListSingleton with data from both volcanoes.csv and
 eruptions.csv
     *
     * @return VolcanoListSingleton
     * @throws FileNotFoundException
     */
    public static VolcanoListSingleton getData() throws FileNotFoundException {
        VolcanoListSingleton volData = VolcanoListSingleton.getInstance();
        Scanner volFile = new Scanner(new File("volcanoes.csv"));
        Scanner erupFile = new Scanner(new File("eruptions.csv"));

        while (volFile.hasNextLine()) {
            String volcano = volFile.nextLine();
            Volcano addition = new Volcano(volcano);
            volData.setVolcano(addition);
        }
        while (erupFile.hasNextLine()) {
            String eruption = erupFile.nextLine();
            Eruption addition = new Eruption(eruption);
            for (Volcano entry : volData.getVolcanoList()) {
                if (entry.getName().equals(addition.getName())) {
                    entry.addEruption(addition);
                }
            }
            volData.setErupNum(addition.getErupNum());
            volData.erupNumPlusOne();
        }
        volFile.close();
        erupFile.close();
        return volData;
    }

    /**
     * adds a volcano to the volcanoes.csv file
     *
     * @param nameField - Textfield for name
     * @param descriptionField - Textfield for description
     * @param latitude - String for latitude
     * @param longitude - String for longitude
     * @param typeBox - Combobox for type
     * @param classificationBox - Combobox for classification
     * @throws IOException
     */
    public static void addVolcano(TextField nameField, TextArea descriptionField,
            String latitude, String longitude, ComboBox<String> typeBox,
            ComboBox<String> classificationBox) throws IOException {

        String name = nameField.getText();
        String description = descriptionField.getText().replace("\n", "~").replace(",", "`");
        String type = typeBox.getValue();
        String classification = classificationBox.getValue();

        String csv = "volcanoes.csv";
        try ( CSVWriter writer = new CSVWriter(new FileWriter(csv, true))) {
            String data = name + "," + description + "," + latitude + ","
                    + longitude + "," + type + "," + classification;

            String[] record = data.split(",");
            writer.writeNext(record);
        }
    }

    /**
     * updateCSV - updates the csv file based on the name given to it
     *
     * @param name
     * @param filename
     * @throws IOException
     * @throws CsvException
     */
    public static void updateCSV(String name, String filename) throws IOException, CsvException {

        Scanner inFile = new Scanner(new File(filename));
        CSVWriter writer = new CSVWriter(new FileWriter("buffer.csv"));

        while ((inFile.hasNextLine())) {
            String line = inFile.nextLine();
            line = line.replace("\n", "~").replace(",", "`");
            String[] parts = line.replace("\"", "").split(",");
            if (!parts[0].equals(name) && !parts[1].equals(name)) {
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
     * toDMS - converts a coordinate in decimal degrees format into degrees,
     * minutes, seconds format
     *
     * @param coordinate - double coordinate in decimal degrees format
     * @param latOrLong - either latitude or longitude
     * @return - string of coordinate in DMS format
     */
    public static String toDMS(double coordinate, String latOrLong) {
        String direction;
        double c = coordinate;
        double absolute = Math.abs(c);
        int degrees = (int) Math.floor(absolute);
        double minutesNotTruncated = (absolute - degrees) * 60;
        int minutes = (int) Math.floor(minutesNotTruncated);
        int seconds = (int) Math.round((minutesNotTruncated - minutes) * 60);

        if (latOrLong.equals("lat")) {
            direction = c >= 0 ? "N" : "S";
        } else {
            direction = c >= 0 ? "E" : "W";
        }

        return (Integer.toString(degrees) + "° " + Integer.toString(minutes)
                + "' " + Integer.toString(seconds) + "\" " + direction);
    }

    /**
     * showAverage - returns the string of average time between eruptions, if
     * the number of eruptions is greater than 1
     *
     * @param v - Volcano object
     * @return - string
     */
    public static String showAverage(Volcano v) {
        if (v.numberOfEruptions() > 1) {
            return "Average time between eruptions: "
                    + averageFormat.format(v.averageTime())
                    + " years";
        } else {
            return "";
        }
    }

    /**
     * showDescription - gets the description of a volcano and sets it to the
     * description area. also updates the averagearea with the average time
     * between eruptions of the volcano
     *
     * @param v - Volcano object
     * @param descriptionArea - TextArea to display the description
     * @param averageArea - TextArea to display the average time between
     * eruptions
     */
    public static void showDescription(Volcano v, TextArea descriptionArea,
            TextArea averageArea) {
        String desc = v.getDescription().replace("`", ",").replace("~", "\n");
        descriptionArea.setText(desc);
        String average = showAverage(v);
        averageArea.setText(average);
    }

    /**
     * showEruptions - takes a volcano and and TableView and populates the table
     * with the eruptions for volcano
     *
     * @param v - Volcano
     * @param eruptionView - TableView
     */
    @SuppressWarnings("unchecked")
    public static void showEruptions(Volcano v, TableView eruptionView) {
        if (!v.getAllEruptions().isEmpty()) {
            ObservableList<Eruption> eruptions
                    = FXCollections.observableArrayList(v.getAllEruptions());
            eruptionView.setItems(eruptions);
        } else {
            eruptionView.setItems(null);
        }
    }

    /**
     * updateView - updates a tableview
     *
     * @param volcanoView - TableView
     */
    @SuppressWarnings("unchecked")
    public static void updateView(TableView volcanoView) {
        data = FXCollections.observableArrayList(volData.getVolcanoList());
        volcanoView.setItems(null);
        volcanoView.setItems(data);
    }

    /**
     * setNameDropdown - sets a given dropdown with names from list of all
     * volcano names in the volcanolist
     *
     * @param dropdown - ComboBox
     */
    public static void setNameDropdown(ComboBox<String> dropdown) {
        if (names != null) {
            names.clear();
        }
        names = volData.getAllNames();
        volcanoNames = FXCollections.observableArrayList(names);
        dropdown.setItems(volcanoNames);
    }

    /**
     * populateTextArea - gets a volcano with the name chosen by the dropdown,
     * and populates a given TextArea with data from the Volcano
     *
     * @param dropdown - ComboBox
     * @param textArea - TextArea
     */
    public static void populateTextArea(ComboBox<String> dropdown, TextArea textArea) {
        String name = dropdown.getValue();
        if (name != null) {
            for (Volcano entry : volData.getVolcanoList()) {
                if (name.equals(entry.getName())) {

                    textArea.setText("Volcano Name: " + entry.getName() + "\n"
                            + "Description: " + entry.getDescription().replace("`", ",").replace("~", "\n") + "\n"
                            + "Location: " + toDMS(entry.getLatitude(), "lat")
                            + ", " + toDMS(entry.getLongitude(), "long") + "\n"
                            + "Volcano Type: " + entry.getType() + "\n"
                            + "Classification: " + entry.getClassification());
                }
            }
        }
    }
}
