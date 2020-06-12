package sample;

import java.time.LocalDate;
import java.util.Date;

public class Observation implements Comparable<Observation> {
    String id;
    String status;
    String category;
    String code;
    LocalDate date;
    Double value;
    String unit; //if category == survey then unit = context

    public Observation(String id, String status, String category, String code, LocalDate date, Double value, String unit) {
        this.id = id;
        this.status = status;
        this.category = category;
        this.code = code;
        this.date = date;
        this.value = value;
        this.unit = unit;
    }

    @Override
    public int compareTo(Observation o) {
        return this.date.compareTo(o.date);
    }

    @Override
    public String toString(){
        return this.date.toString();
    }
}
