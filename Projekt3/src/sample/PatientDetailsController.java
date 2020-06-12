package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class PatientDetailsController {
    @FXML
    Text nameText;
    @FXML
    Text phoneText;
    @FXML
    Text genderText;
    @FXML
    Text birthText;
    @FXML
    Text addressText;
    @FXML
    Text martialText;
    @FXML
    Button returnButton;
    @FXML
    TableView observationsTableView;
    @FXML
    TableView medicationTableView;
    @FXML
    Button bmiButton;
    @FXML
    Button bloodPressureButton;
    @FXML
    Button weightButton;
    @FXML
    Button heightButton;
    @FXML
    Button periodButton;
    @FXML
    ComboBox<Integer> startComboBox;
    @FXML
    Button clearButton;

    ArrayList<Integer> observationIndexes;
    ArrayList<Integer> medicationIndexes;

    TableColumn<String, ObservationSimple> observationColumn;
    TableColumn<String, ObservationSimple> obsertationDateColumn;
    TableColumn<String, MedicationSimple> medicationSimpleTableColumn;
    TableColumn<String, MedicationSimple> medicationDateTableColumn;


    Patient patient;


    public void onActionReturn() throws IOException {
        Stage stage = (Stage) returnButton.getScene().getWindow();

        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(this.getClass().getResource("mainMenu.fxml"));
        Parent root = (Parent) loader2.load();
        MainController mainController = loader2.getController();
        Stage stage2 = new Stage();
        stage2.setTitle("Patient details");
        stage2.setScene(new Scene(root));
        stage2.show();

        stage.close();
    }

    public void onActionPeriod(){
        Integer year = startComboBox.getSelectionModel().getSelectedItem();
        ArrayList<Integer> tmpObservationIndexes = new ArrayList<>();
        ArrayList<Integer> tmpMedicationIndexes = new ArrayList<>();

        /*for(int i = 0; i<this.patient.getObservations().size(); i++){
            if(this.patient.getObservations().get(i).date.getYear() == year){
                tmpObservationIndexes.add(i);
            }
        }

        for(int i = 0; i<this.patient.getMedicationRequests().size(); i++){
            if(this.patient.getMedicationRequests().get(i).date.getYear() == year){
                tmpMedicationIndexes.add(i);
            }
        }

        observationIndexes = tmpObservationIndexes;
        medicationIndexes = tmpMedicationIndexes;*/

        this.refreshTables(year);
    }

    public void onActionClear(){
        this.refreshTables(0);
    }


    public void refreshTables(int year){

        observationsTableView.getColumns().clear();
        medicationTableView.getColumns().clear();

        observationColumn = new TableColumn<>("Category");
        obsertationDateColumn = new TableColumn<>("Date");
        medicationSimpleTableColumn = new TableColumn<>("Status");
        medicationDateTableColumn = new TableColumn<>("Date");

        observationIndexes = new ArrayList<>();
        medicationIndexes = new ArrayList<>();
        ArrayList<ObservationSimple> observationSimples = new ArrayList<>();
        ArrayList<MedicationSimple> medicationSimples = new ArrayList<>();

        if(year == 0) {
            for (int i = 0; i < this.patient.getObservations().size(); i++) {
                observationSimples.add(new ObservationSimple(this.patient.getObservations().get(i).category, this.patient.getObservations().get(i).date.toString()));
                observationIndexes.add(i);
            }
            for (int i = 0; i < this.patient.getMedicationRequests().size(); i++) {
                medicationSimples.add(new MedicationSimple(this.patient.getMedicationRequests().get(i).status, this.patient.getMedicationRequests().get(i).date.toString()));
                medicationIndexes.add(i);
            }
        }
        else{
            for (int i = 0; i < this.patient.getObservations().size(); i++) {
                if(this.patient.getObservations().get(i).date.getYear() == year) {
                    observationSimples.add(new ObservationSimple(this.patient.getObservations().get(i).category, this.patient.getObservations().get(i).date.toString()));
                    observationIndexes.add(i);
                }
            }
            for (int i = 0; i < this.patient.getMedicationRequests().size(); i++) {
                if(this.patient.getMedicationRequests().get(i).date.getYear() == year) {
                    medicationSimples.add(new MedicationSimple(this.patient.getMedicationRequests().get(i).status, this.patient.getMedicationRequests().get(i).date.toString()));
                    medicationIndexes.add(i);
                }
            }
        }


        observationColumn.setCellValueFactory(new PropertyValueFactory<String, ObservationSimple>("category"));
        obsertationDateColumn.setCellValueFactory(new PropertyValueFactory<String, ObservationSimple>("date"));
        observationColumn.setSortable(false);
        obsertationDateColumn.setSortable(false);

        ObservableList<ObservationSimple> observationObservableList =  FXCollections.observableArrayList(observationSimples);
        observationsTableView.setItems(observationObservableList);
        observationsTableView.getColumns().addAll(observationColumn, obsertationDateColumn);
        observationsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        medicationSimpleTableColumn.setCellValueFactory(new PropertyValueFactory<String, MedicationSimple>("status"));
        medicationDateTableColumn.setCellValueFactory(new PropertyValueFactory<String, MedicationSimple>("date"));
        medicationSimpleTableColumn.setSortable(false);
        medicationDateTableColumn.setSortable(false);

        ObservableList<MedicationSimple> medicationSimpleObservableList = FXCollections.observableArrayList(medicationSimples);
        medicationTableView.setItems(medicationSimpleObservableList);
        medicationTableView.getColumns().addAll(medicationSimpleTableColumn, medicationDateTableColumn);
        medicationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }


    public void initializeData(Patient patient) throws IOException {
        this.patient = patient;
        this.patient.refreshData();

        this.nameText.setText(this.patient.getName());
        this.phoneText.setText("Phone: " + this.patient.getPhoneNumber());
        this.genderText.setText("Gender: " + this.patient.getGender());
        this.birthText.setText("Birth date: " + this.patient.getBirthDate());
        this.addressText.setText(this.patient.getAddress());
        this.martialText.setText("Martial status: " + this.patient.getMartialStatus());


        this.refreshTables(0);


        for(Integer i=this.patient.getObservations().get(0).date.getYear(); i<=LocalDate.now().getYear(); i++){
            startComboBox.getItems().add(i);
        }

        startComboBox.getSelectionModel().select(0);
    }

    public void onActionObservation() throws IOException {
        int id = observationsTableView.getSelectionModel().getSelectedIndex();
        if(id != -1) {
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(this.getClass().getResource("observationDetails.fxml"));
            Parent root = (Parent) loader2.load();
            ObservationController observationController = loader2.getController();
            Stage stage2 = new Stage();
            stage2.setTitle("Observation details");
            stage2.setScene(new Scene(root));


            observationController.initializeData(this.patient.getObservations().get(observationIndexes.get(id)));
            stage2.show();
        }
    }

    public void onActionMedication() throws IOException {
        int id = medicationTableView.getSelectionModel().getSelectedIndex();
        if(id != -1) {
            FXMLLoader loader2 = new FXMLLoader();
            loader2.setLocation(this.getClass().getResource("medicationDetails.fxml"));
            Parent root = (Parent) loader2.load();
            MedicationController medicationController = loader2.getController();
            Stage stage2 = new Stage();
            stage2.setTitle("Observation details");
            stage2.setScene(new Scene(root));


            medicationController.initializeData(this.patient.getMedicationRequests().get(medicationIndexes.get(id)));
            stage2.show();
        }
    }

    public void onActionBodyWeight() throws IOException {
        ArrayList<LocalDate> dates = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();


        for (Observation o : patient.getObservations())
        {
            if(o.code.equalsIgnoreCase("Body Weight")){
                dates.add(o.date);
                values.add(o.value);
            }
        }


        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(this.getClass().getResource("chartLayout.fxml"));
        Parent root = (Parent) loader2.load();
        ChartController chartController = loader2.getController();
        Stage stage2 = new Stage();
        stage2.setTitle("Observation details");
        stage2.setScene(new Scene(root));

        chartController.initializeData("Body Weight","weight[kg]", dates, values);

        stage2.show();
    }

    public void onActionBodyHeight() throws IOException {
        ArrayList<LocalDate> dates = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();


        for (Observation o : patient.getObservations())
        {
            if(o.code.equalsIgnoreCase("Body Height")){
                dates.add(o.date);
                values.add(o.value);
            }
        }


        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(this.getClass().getResource("chartLayout.fxml"));
        Parent root = (Parent) loader2.load();
        ChartController chartController = loader2.getController();
        Stage stage2 = new Stage();
        stage2.setTitle("Observation details");
        stage2.setScene(new Scene(root));

        chartController.initializeData("Body Height","height[cm]", dates, values);

        stage2.show();
    }

    public void onActionBloodPressure() throws IOException {
        ArrayList<LocalDate> dates = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();


        for (Observation o : patient.getObservations())
        {
            if(o.code.equalsIgnoreCase("Blood Pressure")){
                dates.add(o.date);
                values.add(o.value);
            }
        }


        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(this.getClass().getResource("chartLayout.fxml"));
        Parent root = (Parent) loader2.load();
        ChartController chartController = loader2.getController();
        Stage stage2 = new Stage();
        stage2.setTitle("Observation details");
        stage2.setScene(new Scene(root));

        chartController.initializeData("Blood Pressure","pressure", dates, values);

        stage2.show();
    }


    public void onActionBodyMassIndex() throws IOException {
        ArrayList<LocalDate> dates = new ArrayList<>();
        ArrayList<Double> values = new ArrayList<>();


        for (Observation o : patient.getObservations())
        {
            if(o.code.equalsIgnoreCase("Body Mass Index")){
                dates.add(o.date);
                values.add(o.value);
            }
        }


        FXMLLoader loader2 = new FXMLLoader();
        loader2.setLocation(this.getClass().getResource("chartLayout.fxml"));
        Parent root = (Parent) loader2.load();
        ChartController chartController = loader2.getController();
        Stage stage2 = new Stage();
        stage2.setTitle("Observation details");
        stage2.setScene(new Scene(root));

        chartController.initializeData("Body Mass Index","BMI", dates, values);

        stage2.show();
    }
}
