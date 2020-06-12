package sample;

import javafx.beans.property.SimpleStringProperty;

public class MedicationSimple {
    private SimpleStringProperty status;
    private SimpleStringProperty date;

    public MedicationSimple(String status, String date) {
        this.status = new SimpleStringProperty(status);
        this.date = new SimpleStringProperty(date);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }
}
