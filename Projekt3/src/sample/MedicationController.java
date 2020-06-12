package sample;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class MedicationController {
    @FXML
    Text statusText;
    @FXML
    Text intentText;
    @FXML
    Text conceptText;
    @FXML
    Text dateText;
    @FXML
    Text asNeedText;
    @FXML
    Text frequencyText;
    @FXML
    Text periodText;
    @FXML
    Text periodUnitText;
    @FXML
    Text doseText;

    MedicationRequest medicationRequest;

    public void initializeData(MedicationRequest medicationRequest){
        this.medicationRequest = medicationRequest;

        statusText.setText("Status: " + this.medicationRequest.status);
        intentText.setText("Intent: " + this.medicationRequest.intent);
        conceptText.setText("Concept: " + this.medicationRequest.concept);
        dateText.setText("Date: " + this.medicationRequest.date.toString());

        if(this.medicationRequest.asNeedBoolean != null) {
            asNeedText.setText("As need: " + this.medicationRequest.asNeedBoolean.toString());

            if (this.medicationRequest.asNeedBoolean) {
                frequencyText.setText("");
                periodText.setText("");
                periodUnitText.setText("");
                doseText.setText("");
            } else {
                frequencyText.setText("Frequency: " + this.medicationRequest.frequency.toString());
                periodText.setText("Period: " + this.medicationRequest.period.toString());
                periodUnitText.setText("Period unit: " + this.medicationRequest.periodUnit);
                doseText.setText("Dose: " + this.medicationRequest.doseQuantity.toString());
            }
        }
        else {
            frequencyText.setText("");
            periodText.setText("");
            periodUnitText.setText("");
            doseText.setText("");
            asNeedText.setText("");
        }
    }
}
