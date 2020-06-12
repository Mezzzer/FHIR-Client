package sample;

import com.google.errorprone.annotations.FormatMethod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    Button showButton;
    @FXML
    Button refreshButton;
    @FXML
    TextField findField;
    @FXML
    ListView patientsListView;
    @FXML
    Button findButton;

    private ObservableList<String> patientsNamesList = FXCollections.observableArrayList();
    private ArrayList<Patient> currentPatients;

    public void onActionFind(){
        currentPatients = new ArrayList<>(Main.patients);
        String findString = this.findField.getText();

        patientsNamesList.clear();
        ArrayList<Patient> tmpPatients = new ArrayList<>();
        for(int i=0; i<currentPatients.size(); i++){
            if(currentPatients.get(i).getName().toLowerCase().contains(findString.toLowerCase())){
                tmpPatients.add(currentPatients.get(i));
                patientsNamesList.add(currentPatients.get(i).getName());
            }
        }

        currentPatients = tmpPatients;
    }

    public void onActionRefresh() throws IOException {
        this.refreshList();
    }

    public void onActionDetails() throws IOException {
        if(currentPatients.size() != 0) {
            Stage stage = (Stage) showButton.getScene().getWindow();


            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(this.getClass().getResource("patientDetails.fxml"));
            Parent root = (Parent) loader2.load();
            PatientDetailsController patientDetailsController = loader2.getController();
            Stage stage2 = new Stage();
            stage2.setTitle("Patient details");
            stage2.setScene(new Scene(root));


            patientDetailsController.initializeData(currentPatients.get(patientsListView.getSelectionModel().getSelectedIndex()));
            stage2.show();

            stage.close();
        }
    }

    public void refreshList() throws IOException {
        Main.makePatientsList();
        currentPatients = new ArrayList<>(Main.patients);

        patientsNamesList.clear();

        for(int i=0; i<Main.patients.size(); i++) {
            patientsNamesList.add(Main.patients.get(i).getName());
            //System.out.println(Main.patients.get(i).getName());
        }
        patientsListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        patientsListView.setItems(patientsNamesList);
        patientsListView.refresh();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            this.refreshList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
