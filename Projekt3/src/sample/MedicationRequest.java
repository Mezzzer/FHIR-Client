package sample;

import java.time.LocalDate;

public class MedicationRequest implements Comparable<MedicationRequest>{
    String id;
    String status;
    String intent;
    String concept;
    LocalDate date;
    Boolean asNeedBoolean; //if true rest of the parameters are null
    Integer frequency;
    Integer period;
    String periodUnit;
    Integer doseQuantity;

    public MedicationRequest(String id, String status, String intent, String concept, LocalDate date, Boolean asNeedBoolean) {
        this.id = id;
        this.status = status;
        this.intent = intent;
        this.concept = concept;
        this.date = date;
        this.asNeedBoolean = asNeedBoolean;
        this.period = null;
        this.periodUnit = null;
        this.doseQuantity = null;
    }

    public MedicationRequest(String id, String status, String intent, String concept, LocalDate date, Boolean asNeedBoolean, Integer frequency, Integer period, String periodUnit, Integer doseQuantity) {
        this.id = id;
        this.status = status;
        this.intent = intent;
        this.concept = concept;
        this.date = date;
        this.asNeedBoolean = asNeedBoolean;
        this.frequency = frequency;
        this.period = period;
        this.periodUnit = periodUnit;
        this.doseQuantity = doseQuantity;
    }

    @Override
    public int compareTo(MedicationRequest o) {
        return this.date.compareTo(o.date);
    }
}
