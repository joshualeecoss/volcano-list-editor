package question2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import static question2.VolcanoHelperFunctions.*;

/**
 * Graph View Controller - initializes a linechart object
 *
 * @author Joshua Coss
 */
public class GraphViewController implements Initializable {

    VolcanoListSingleton volData = VolcanoListSingleton.getInstance();

    @FXML
    LineChart<Number, Number> graph;
    @FXML
    ComboBox<String> volcanoDropdown;

    @FXML
    private NumberAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //sets the axis labels and yaxis upper and lower bounds
        xAxis.setLabel("Year");
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(10);
        yAxis.setTickUnit(1);
        yAxis.setLabel("VEI");
        setNameDropdown(volcanoDropdown);

    }

    /**
     * populateGraph - when a name is chosen from the dropdown list, the
     * linechart is populated with the years across the x axis and the VEI
     * across the y axis. if the number of eruptions in the volcano is less than
     * two, does not populate the chart.
     *
     * @param event
     */
    public void populateGraph(ActionEvent event) {
        if (graph != null) {
            graph.getData().clear();
        }

        String volcanoName = volcanoDropdown.getValue();
        volData.setCurrentVolcano(volcanoName);

        Volcano v = volData.getCurrentVolcano();
        if (v.numberOfEruptions() > 1) {
            v.setMinMaxYear();
            xAxis.setAutoRanging(false);
            xAxis.setLowerBound(v.getFurthestYear());
            xAxis.setUpperBound(v.getClosestYear());

            //formats the x-axis with BC or BCE years
            xAxis.setTickLabelFormatter(new StringConverter<Number>() {
                @Override
                public String toString(Number object) {
                    if (object.intValue() < 0) {
                        return (Math.abs(object.intValue())) + "BC";
                    } else {
                        return object.intValue() + "BCE";
                    }
                }

                @Override
                public Number fromString(String string) {
                    return 0;
                }
            });

            yAxis.setAutoRanging(false);

            XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
            for (Eruption entry : v.getAllEruptions()) {
                series1.getData().add(new XYChart.Data<>(entry.getUnformattedYear(), entry.getVEI()));
            }
            graph.getData().add(series1);
        }
    }

}
