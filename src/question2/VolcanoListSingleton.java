package question2;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * VolcanoListSingleton - a singleton class to update and maintain data across
 * controllers
 *
 * @author Joshua Coss
 */
public final class VolcanoListSingleton {

    private ArrayList<Volcano> volcanoes = new ArrayList<>();
    private final static VolcanoListSingleton INSTANCE = new VolcanoListSingleton();

    private Volcano currentVolcano;
    private int erupNum = 0;

    private VolcanoListSingleton() {
    }

    /**
     * getInstance - returns the instance of this singleton object
     *
     * @return
     */
    public static VolcanoListSingleton getInstance() {
        return INSTANCE;
    }

    /**
     * removeVolcano - remove volcano from singleton list object
     *
     * @param v Volcano
     */
    public void removeVolcano(Volcano v) {
        volcanoes.remove(v);
    }

    /**
     * setVolcano - adds a volcano object to the list
     *
     * @param v Volcano
     */
    public void setVolcano(Volcano v) {
        volcanoes.add(v);
    }

    /**
     * getVolcanoList - returns the list of volcanoes in the singleton object
     *
     * @return - ArrayList of volcanoes
     */
    public ArrayList<Volcano> getVolcanoList() {
        return this.volcanoes;
    }

    /**
     * getAllNames - returns hash set of all volcano names in the singleton
     * object
     *
     * @return
     */
    public HashSet<String> getAllNames() {
        HashSet<String> allNames = new HashSet<>();
        for (Volcano entry : this.volcanoes) {
            allNames.add(entry.getName());
        }
        return allNames;
    }

    /**
     * setCurrentVolcano - sets the current volcano with the name string
     *
     * @param name -string
     */
    public void setCurrentVolcano(String name) {
        for (Volcano entry : this.volcanoes) {
            if (entry.getName().equals(name)) {
                this.currentVolcano = entry;
            }
        }
    }

    /**
     * getCurrentVolcano - returns the currentVolcano of the list
     *
     * @return
     */
    public Volcano getCurrentVolcano() {
        return currentVolcano;
    }

    /**
     * setEruption - adds an eruption to the currently selected volcano
     *
     * @param e - Eruption
     */
    public void setEruption(Eruption e) {
        currentVolcano.addEruption(e);
    }

    /**
     * getEruptions - returns a list of all eruptions in the currentVolcano
     *
     * @return - arraylist of eruptions
     */
    public ArrayList<Eruption> getEruptions() {
        ArrayList<Eruption> allEruptions = new ArrayList<>();
        for (Eruption entry : this.currentVolcano.getAllEruptions()) {
            allEruptions.add(entry);
        }
        return allEruptions;
    }

    /**
     * clear - removes all volcanoes from singleton
     */
    public void clear() {
        this.volcanoes.clear();
    }

    /**
     * erupNumPlusOne - increases unique id number by 1
     */
    public void erupNumPlusOne() {
        this.erupNum++;
    }

    /**
     * getErupNum - return the current unique id number
     *
     * @return - erupNum int
     */
    public int getErupNum() {
        return this.erupNum;
    }

    /**
     * setErupNum - sets the erupNum to the singleton
     *
     * @param erupNum - int
     */
    public void setErupNum(int erupNum) {
        this.erupNum = erupNum;
    }

}
