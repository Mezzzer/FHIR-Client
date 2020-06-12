package sample;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class ObservationController {
    @FXML
    Text statusText;
    @FXML
    Text categoryText;
    @FXML
    Text codeText;
    @FXML
    Text dateText;
    @FXML
    Text valueText;
    @FXML
    Text unitText;

    Observation observation;


    public void initializeData(Observation observation){
        this.observation = observation;

        statusText.setText("Status: " + this.observation.status);
        categoryText.setText("Category: " + this.observation.category);
        codeText.setText("Code: " + this.observation.code);
        dateText.setText("Date: " + this.observation.date.toString());
        valueText.setText("Value: "+ this.observation.value);
        if(this.observation.category.equalsIgnoreCase("survey")){
            unitText.setText("Context: " + this.observation.unit);
        }
        else
            unitText.setText("Unit: " + this.observation.unit);
    }
}
