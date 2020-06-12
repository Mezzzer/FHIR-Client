package sample;

import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;

public class ChartController {

    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    ComboBox<String> startBox;
    @FXML
    ComboBox<String> endBox;
    @FXML
    Button periodButton;

    ArrayList<LocalDate> dates;
    ArrayList<Double> values;

    public void initializeData(String title, String yTitle, ArrayList<LocalDate> dates, ArrayList<Double> values){

        this.dates = dates;
        this.values = values;

        ArrayList<String> stringDates = new ArrayList<>();

        XYChart.Series<String, Number> seriesValues = new XYChart.Series<String, Number>();
        for(int i=0; i<dates.size(); i++){
            stringDates.add(dates.get(i).toString());
            seriesValues.getData().addAll(new XYChart.Data<String, Number>(dates.get(i).toString(), values.get(i)));
        }
        XYChart.Series<String, Number> series = seriesValues;

        lineChart.setTitle(title);

        lineChart.getData().add(series);

        startBox.getItems().addAll(stringDates);
        endBox.getItems().addAll(stringDates);

        startBox.getSelectionModel().select(0);
        endBox.getSelectionModel().select(0);
    }

    public void onActionStart(){
        for(int i=0; i<this.dates.size(); i++){
            LocalDate tmpDate = LocalDate.parse(startBox.getItems().get(i));

            if(this.dates.get(i).compareTo(tmpDate) < 0){
                endBox.getItems().add(tmpDate.toString());
            }
        }
    }


    public void refreshChart(){
        lineChart.getData().clear();

        LocalDate tmpStart = LocalDate.parse(startBox.getSelectionModel().getSelectedItem());
        LocalDate tmpEnd = LocalDate.parse(endBox.getSelectionModel().getSelectedItem());

        XYChart.Series<String, Number> seriesValues = new XYChart.Series<String, Number>();
        for(int i=0; i<dates.size(); i++){

            if(this.dates.get(i).compareTo(tmpStart) >= 0 && this.dates.get(i).compareTo(tmpEnd) <= 0)
                seriesValues.getData().addAll(new XYChart.Data<String, Number>(dates.get(i).toString(), values.get(i)));
        }
        XYChart.Series<String, Number> series = seriesValues;

        lineChart.getData().add(series);
    }

    public Integer dateToNumber(LocalDate date){
        Integer number;
        number = date.getYear() * 10000 + date.getMonthValue() * 100 + date.getDayOfMonth();
        return number;
    }
}
