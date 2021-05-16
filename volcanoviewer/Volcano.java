package volcanoviewer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Volcano takes data from volcanoes.csv and populates data in variables
 *
 * @author Joshua Coss
 */
public class Volcano implements Serializable {

    private String name;
    private String description;
    private double latitude, longitude;
    private String type;
    private String classification;
    private ArrayList<Eruption> eruptions;
    private int furthestYear;
    private int closestYear;
    private double average;
    private String[] token;

    /**
     * Creates a Volcano object based on a string being handed to it
     *
     * @param csv line from csv file
     */
    public Volcano(String csv) {
        token = csv.replace("\"", "").split(",", -1);

        name = token[0];
        description = token[1];
        latitude = Double.parseDouble(token[2]);
        longitude = Double.parseDouble(token[3]);
        type = token[4];
        classification = token[5];
        eruptions = new ArrayList<>();
    }

    /**
     * Creates a deep copy of the Volcano object
     *
     * @param clone
     */
    public Volcano(Volcano clone) {
        name = clone.name;
        description = clone.description;
        latitude = clone.latitude;
        longitude = clone.longitude;
        type = clone.type;
        classification = clone.classification;
        eruptions = clone.eruptions;
    }

    /**
     * setName - sets a string to the name of the Volcano
     *
     * @param name - string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setDescription - sets a string to the description of the Volcano
     *
     * @param description - string
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * setLatitude - sets a double to the latitude of the Volcano
     *
     * @param latitude - double
     */
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    /**
     * setLongitude - sets a double to the longitude of the Volcano
     *
     * @param longitude - double
     */
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    /**
     * setType - sets a string to the type of the Volcano
     *
     * @param type - String
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * setClassification - sets a string to the classification of the Volcano
     *
     * @param classification - string
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * addEruption - adds an eruption to the list of eruptions in the Volcano
     *
     * @param eruption - Eruption object
     */
    public void addEruption(Eruption eruption) {
        this.eruptions.add(eruption);
    }

    /**
     * getName - returns the name of the Volcano
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * getDescription - returns the description of the Volcano
     *
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * getLatitude - returns the latitude of the Volcano
     *
     * @return latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * getLongitude - returns the longitude of the Volcano
     *
     * @return longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * getType - returns the type of the volcano
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * getClassification - returns the classification of the volcano
     *
     * @return classification
     */
    public String getClassification() {
        return classification;
    }

    /**
     * getAllEruptions - returns the eruption list as an observable array list
     *
     * @return observable array list of eruptions
     */
    public ObservableList<Eruption> getAllEruptions() {
        return FXCollections.observableArrayList(eruptions);
    }

    /**
     * removeEruption - removes an eruption e from the volcano's list of
     * eruptions
     *
     * @param e - eruption object to be removed
     */
    public void removeEruption(Eruption e) {
        this.eruptions.remove(e);
    }

    /**
     * numberOfEruptions - returns the size of the eruption list
     *
     * @return int number of eruptions
     */
    public int numberOfEruptions() {
        return this.eruptions.size();
    }

    /**
     * averageTime - calculates and sets, and returns average time between
     * eruptions
     *
     * @return average time between eruptions
     */
    public double averageTime() {
        this.setMinMaxYear();
        this.average = (this.closestYear - this.furthestYear) / (eruptions.size() - 1);
        return this.average;
    }

    /**
     * setMinMaxYear - calculates and sets the furthestYear and closestYear of
     * the Volcano object
     */
    public void setMinMaxYear() {
        ArrayList<Integer> years = new ArrayList<>();
        for (Eruption entry : eruptions) {
            int value = entry.getYear();
            if (entry.getEra().equals("BC")) {
                value *= -1;
            }
            years.add(value);
        }
        int min = Collections.min(years);
        int max = Collections.max(years);

        this.furthestYear = min;
        this.closestYear = max;
    }

    /**
     * getFurthestYear - returns the furthest back year in the Eruption list
     *
     * @return furthestYear int
     */
    public int getFurthestYear() {
        return this.furthestYear;
    }

    /**
     * getClosestYear - returns the most recent year in the Eruption list
     *
     * @return closestYear int
     */
    public int getClosestYear() {
        return this.closestYear;
    }

}
