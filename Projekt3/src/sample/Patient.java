package sample;

import com.sun.jdi.DoubleValue;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class Patient{
    private String id;
    private String name;
    private String phoneNumber;
    private String gender;
    private String birthDate;
    private String address;
    private String martialStatus;
    private ArrayList<Observation> observations;
    private ArrayList<MedicationRequest> medicationRequests;

    public Patient(String id, String name, String phoneNumber, String gender, String birthDate, String address, String martialStatus) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.birthDate = birthDate;
        this.address = address;
        this.martialStatus = martialStatus;
        this.observations = new ArrayList<Observation>();
        this.medicationRequests = new ArrayList<MedicationRequest>();
    }

    public Patient(String id, String name, String phoneNumber, String gender, String address, String martialStatus) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.address = address;
        this.martialStatus = martialStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;
    }

    public ArrayList<Observation> getObservations() {
        return observations;
    }

    public void setObservations(ArrayList<Observation> observations) {
        this.observations = observations;
    }

    public ArrayList<MedicationRequest> getMedicationRequests() {
        return medicationRequests;
    }

    public void setMedicationRequests(ArrayList<MedicationRequest> medicationRequests) {
        this.medicationRequests = medicationRequests;
    }

    public void refreshData() throws IOException {
        observations = new ArrayList<Observation>();
        medicationRequests = new ArrayList<MedicationRequest>();

        String urlString = "http://localhost:8080/baseDstu3/Patient/" + this.id  + "/$everything?_count=100&_format=json";
        //System.out.println(urlString);
        downloadPatientInformation(urlString);


        String jsonString = getJsonString();
        parseData(jsonString);

        JSONObject jsonObject = new JSONObject(jsonString);

        urlString = ifNextLinkExists(jsonObject.getJSONArray("link"));
        while(!urlString.equals("")){
            //System.out.println(urlString);
            downloadPatientInformation(urlString);
            jsonString = getJsonString();
            parseData(jsonString);
            jsonObject = new JSONObject(jsonString);
            urlString = ifNextLinkExists(jsonObject.getJSONArray("link"));
        }


    }

    private String ifNextLinkExists(JSONArray links){
        String link = "";
        for (int i=0; i<links.length(); i++){
            if(links.getJSONObject(i).getString("relation").equals("next")) {
                link = links.getJSONObject(i).getString("url");
                break;
            }
        }
        return link;
    }

    public String getJsonString() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("patientInform.json"));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        String jsonString = stringBuilder.toString();

        return jsonString;
    }

    public void downloadPatientInformation(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("patientInform.json")));
        bufferedWriter.write(content.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    public void parseData(String jsonString) throws IOException {


        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray entries = jsonObject.getJSONArray("entry");

        JSONArray resources = new JSONArray();
        for(int i =0; i<entries.length(); i++){
            resources.put(entries.getJSONObject(i).getJSONObject("resource"));
        }

        for(int i=0; i<resources.length(); i++){
           // System.out.println(i);
            if(resources.getJSONObject(i).get("resourceType").equals("Observation")){
                observations.add(this.parseObservation(resources.getJSONObject(i)));
            }
            if(resources.getJSONObject(i).get("resourceType").equals("MedicationRequest")){
                medicationRequests.add(this.parseMedRequest(resources.getJSONObject(i)));
            }
            //System.out.println(resources.getJSONObject(i).get("resourceType"));
        }

        Collections.sort(observations);
        Collections.sort(medicationRequests);
    }

    public Observation parseObservation(JSONObject observResource){
        Observation tmpObservation;
        //id
        String id = observResource.getString("id");
        //status
        String status = observResource.getString("status");
        //category
        String category = "";
        JSONArray categories = observResource.getJSONArray("category");
        category += categories.getJSONObject(0).getJSONArray("coding").getJSONObject(0).getString("code");
        for(int i=1; i<categories.length(); i++){
            category = category + ", " + categories.getJSONObject(i).getJSONArray("coding").getJSONObject(0).getString("code");
        }
        //code
        String code = observResource.getJSONObject("code").getString("text");
        //date
        String dateString = observResource.getString("issued");
        LocalDate date = LocalDate.parse(dateString.substring(0,10));

        Double value = null;
        String unit = "";
       // System.out.println(category);
        if (!category.equals("survey") && !category.equals("exam")) {
            if (observResource.has("component")) {
                value = observResource.getJSONArray("component").getJSONObject(0).getJSONObject("valueQuantity").getDouble("value");
                unit = observResource.getJSONArray("component").getJSONObject(0).getJSONObject("valueQuantity").getString("unit");
            } else {
                //value
                value = observResource.getJSONObject("valueQuantity").getDouble("value");
                //unit
                unit = observResource.getJSONObject("valueQuantity").getString("unit");
            }
        }
        else{
            unit = observResource.getJSONObject("valueCodeableConcept").getString("text");
        }
        tmpObservation = new Observation(id, status, category, code, date, value, unit);

        //System.out.println(tmpObservation.toString());
        return tmpObservation;
    }

    public MedicationRequest parseMedRequest(JSONObject medReq){
        MedicationRequest tmpMedRequest;
        //id
        String id = medReq.getString("id");
        //status
        String status = medReq.getString("status");
        //intent
        String intent = medReq.getString("intent");
        //concept
        String concept = medReq.getJSONObject("medicationCodeableConcept").getString("text");
        //date
        String dateString = medReq.getString("authoredOn");
        LocalDate date = LocalDate.parse(dateString.substring(0,10));

        if(medReq.has("dosageInstruction")){
            //asNeedBoolean
            Boolean asNeed = medReq.getJSONArray("dosageInstruction").getJSONObject(0).getBoolean("asNeededBoolean");

            if(asNeed){
                tmpMedRequest = new MedicationRequest(id, status, intent, concept, date, asNeed);
            }
            else{
                //frequency
                Integer freq = medReq.getJSONArray("dosageInstruction").getJSONObject(0).getJSONObject("timing").getJSONObject("repeat").getInt("frequency");
                //period
                Integer period = medReq.getJSONArray("dosageInstruction").getJSONObject(0).getJSONObject("timing").getJSONObject("repeat").getInt("period");
                //period unit
                String unit = medReq.getJSONArray("dosageInstruction").getJSONObject(0).getJSONObject("timing").getJSONObject("repeat").getString("periodUnit");
                //dose quantity
                Integer dQuantity = medReq.getJSONArray("dosageInstruction").getJSONObject(0).getJSONObject("doseQuantity").getInt("value");

                tmpMedRequest = new MedicationRequest(id, status, intent, concept, date, asNeed, freq, period, unit, dQuantity);
            }
        }
        else
            tmpMedRequest = new MedicationRequest(id, status, intent, concept, date, null);

        return  tmpMedRequest;
    }


}
