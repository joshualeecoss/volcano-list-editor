package volcanoviewer;

/**
 * This class is meant to take a line from a csv file and store the data in it's
 * variables
 *
 * @author Joshua Coss
 */
public class Eruption {

    public int year;
    public String era;
    public float VEI;
    public String classification;
    public String name;
    public int erupNum; //id number
    public String[] token;

    /**
     * Creates an Eruption object based on a string being handed to it
     *
     * @param csv line from csv file
     */
    public Eruption(String csv) {
        token = csv.replace("\"", "").split(",", -1);

        erupNum = Integer.parseInt(token[0]);
        name = token[1];
        year = Integer.parseInt(token[2]);
        era = token[3];
        VEI = Float.parseFloat(token[4]);
        classification = token[5];
    }

    /**
     * Creates an Eruption object when handed parameters
     *
     * @param erupNum - unique id number
     * @param name - name of volcano the Eruption is attached to
     * @param year - year of the eruption
     * @param era - era of the eruption (BC or BCE)
     * @param VEI - VEI of the eruption
     * @param classification - classification of eruption
     */
    public Eruption(int erupNum, String name, int year, String era, float VEI,
            String classification) {
        this.erupNum = erupNum;
        this.name = name;
        this.year = year;
        this.era = era;
        this.VEI = VEI;
        this.classification = classification;
    }

    /**
     * setYear - sets the year of the eruption
     *
     * @param year int
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * setVEI - sets the VEI of the eruption
     *
     * @param VEI float
     */
    public void setVEI(float VEI) {
        this.VEI = VEI;
    }

    /**
     * setClassification - sets the classification of the eruption
     *
     * @param classification string
     */
    public void setClassification(String classification) {
        this.classification = classification;
    }

    /**
     * getYear - returns the year of the eruption object
     *
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * getVEI - returns the VEI number of the eruption
     *
     * @return VEI
     */
    public float getVEI() {
        return VEI;
    }

    /**
     * getClassification - returns the classification string of the eruption
     *
     * @return classification
     */
    public String getClassification() {
        return classification;
    }

    /**
     * getName - returns the name of the volcano that the eruption is assigned
     * to
     *
     * @return name string
     */
    public String getName() {
        return name;
    }

    /**
     * setName - sets a string to the name of the eruption
     *
     * @param name string
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * setEra - sets a string to the era of the eruption
     *
     * @param era string
     */
    public void setEra(String era) {
        this.era = era;
    }

    /**
     * getEra - returns the era of the eruptions
     *
     * @return era string
     */
    public String getEra() {
        return era;
    }

    /**
     * getErupNum - returns the id number of the eruption
     *
     * @return int erupNum
     */
    public int getErupNum() {
        return erupNum;
    }

    /**
     * getUnformattedYear - returns a positive or negative year depending on the
     * era of the eruption
     *
     * @return
     */
    public int getUnformattedYear() {
        if (this.era.equals("BC")) {
            return this.year * -1;
        } else {
            return this.year;
        }
    }
}
