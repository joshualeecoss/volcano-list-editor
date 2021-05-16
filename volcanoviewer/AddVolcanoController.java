package volcanoviewer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.text.Text;
import javafx.util.Duration;
import static volcanoviewer.VolcanoHelperFunctions.*;

/**
 * Add Volcano Controller - allows user to fill fields in order to add a new
 volcano to the VolcanoListSingleton and csv file
 *
 * @author Joshua Coss
 */
public class AddVolcanoController implements Initializable {

    private static final String REGEX_DEGREE_INTEGER = "([0-9]|[1-8][0-9]|[9][0])";
    private static final String REGEX_MINUTE_SECOND_INTEGER = "([0-9]|[1-5][0-9]|[6][0])";

    ObservableList<String> typeList = FXCollections.observableArrayList("Cinder Cone", "Composite", "Shield", "Lava Dome");
    ObservableList<String> classificationList = FXCollections.observableArrayList("Volcanic Field", "Cone Volcano", "Caldera Volcano");
    ObservableList<String> latitudeDirectionOptions = FXCollections.observableArrayList("N", "S");
    ObservableList<String> longitudeDirectionOptions = FXCollections.observableArrayList("W", "E");
    VolcanoListSingleton volData = VolcanoListSingleton.getInstance();

    @FXML
    private Button addVolcanoButton;
    @FXML
    private TextField nameField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField latitudeDegreeField;
    @FXML
    private TextField latitudeMinuteField;
    @FXML
    private TextField latitudeSecondField;
    @FXML
    private ComboBox<String> latitudeDirectionBox;
    @FXML
    private TextField longitudeDegreeField;
    @FXML
    private TextField longitudeMinuteField;
    @FXML
    private TextField longitudeSecondField;
    @FXML
    private ComboBox<String> longitudeDirectionBox;
    @FXML
    private ComboBox<String> typeBox;
    @FXML
    private ComboBox<String> classificationBox;

    @FXML
    private Text confirmationMessage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        latitudeDirectionBox.setItems(latitudeDirectionOptions);
        longitudeDirectionBox.setItems(longitudeDirectionOptions);

        typeBox.setItems(typeList);
        classificationBox.setItems(classificationList);

        // force the field to be numeric only within a certain range
        latitudeDegreeField.setTextFormatter(new TextFormatter<>(this::degreeFilter));
        latitudeMinuteField.setTextFormatter(new TextFormatter<>(this::minuteSecondFilter));
        latitudeSecondField.setTextFormatter(new TextFormatter<>(this::minuteSecondFilter));
        longitudeDegreeField.setTextFormatter(new TextFormatter<>(this::degreeFilter));
        longitudeMinuteField.setTextFormatter(new TextFormatter<>(this::minuteSecondFilter));
        longitudeSecondField.setTextFormatter(new TextFormatter<>(this::minuteSecondFilter));

    }

    /**
     * toDecimalDegrees - converts DMS location format into Decimal Degrees
     *
     * @param degree - int
     * @param minute - int
     * @param second - int
     * @param direction - String
     * @return
     */
    public String toDecimalDegrees(int degree, int minute, int second, String direction) {
        double convertedMinute = (double) minute / 60;
        double convertedSecond = (double) second / 3600;
        double finalConverted = degree + convertedMinute + convertedSecond;
        if (direction.equals("S") || direction.equals("W")) {
            finalConverted *= -1;
        }
        return Double.toString(finalConverted);
    }

    /**
     * addVolcanoButtonPressed - gets values from textfields and dropdowns and
     * uses values to add a new volcano object
     *
     * @param event
     * @throws Exception
     */
    public void addVolcanoButtonPressed(ActionEvent event) throws Exception {
        String latitude = toDecimalDegrees(Integer.parseInt(latitudeDegreeField.getText()),
                Integer.parseInt(latitudeMinuteField.getText()),
                Integer.parseInt(latitudeSecondField.getText()),
                latitudeDirectionBox.getValue());
        String longitude = toDecimalDegrees(Integer.parseInt(longitudeDegreeField.getText()),
                Integer.parseInt(longitudeMinuteField.getText()),
                Integer.parseInt(longitudeSecondField.getText()),
                longitudeDirectionBox.getValue());
        addVolcano(nameField, descriptionField, latitude, longitude,
                typeBox, classificationBox);
        confirmationMessageUpdated();
        if (volData != null) {
            volData.clear();
        }
        volData = getData();
    }

    /**
     * fadeConfirmationMessage - fades the confirmation message after 2 seconds
     */
    public void fadeConfirmationMessage() {
        FadeTransition ft = new FadeTransition(Duration.millis(2000), confirmationMessage);
        ft.setDelay(Duration.seconds(2));
        ft.setFromValue(1.0);
        ft.setToValue(0);
        ft.play();
    }

    /**
     * confirmationMessageUpdate -
     */
    public void confirmationMessageUpdated() {
        confirmationMessage.setText("Volcano: " + nameField.getText()
                + "\nSuccessfully added");
        fadeConfirmationMessage();
    }

    /**
     * degreeFilter - filters the input to be between 0 and 90
     *
     * @param change
     * @return
     */
    private TextFormatter.Change degreeFilter(TextFormatter.Change change) {
        if (!change.getControlNewText().matches(REGEX_DEGREE_INTEGER)) {
            change.setText("");
        }
        return change;
    }

    /**
     * minuteSecondFilter - filters the input to be between 0 and 60
     *
     * @param change
     * @return
     */
    private TextFormatter.Change minuteSecondFilter(TextFormatter.Change change) {
        if (!change.getControlNewText().matches(REGEX_MINUTE_SECOND_INTEGER)) {
            change.setText("");
        }
        return change;
    }

}
