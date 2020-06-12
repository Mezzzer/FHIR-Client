package sample;

import javafx.beans.property.SimpleStringProperty;

public class ObservationSimple {

    private SimpleStringProperty category;
    private SimpleStringProperty date;

    public ObservationSimple(String category, String date) {
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(date);
    }

    public String getCategory() {
        return category.get();
    }

    public SimpleStringProperty categoryProperty() {
        return category;
    }

    public void setCategory(String category) {
        this.category.set(category);
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
