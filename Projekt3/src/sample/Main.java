package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;


public class Main extends Application {

    public static ArrayList<Patient> patients;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("mainMenu.fxml"));
        //StackPane stackPane = loader.load();
        Pane borderPane = loader.load();
        Scene scene = new Scene(borderPane);

        MainController controller = loader.getController();
        primaryStage.setScene(scene);
        primaryStage.setTitle("FHIR Client");
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        launch(args);
    }

    public static void makePatientsList() throws IOException {
        getPatiensFromServer();

        BufferedReader reader = new BufferedReader(new FileReader("patients.json"));
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


        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray entries = jsonObject.getJSONArray("entry");


        patients = parsePatients(entries);
    }


    public static void getPatiensFromServer() throws IOException {
        URL url = new URL("http://localhost:8080/baseDstu3/Patient?_pretty=true&_format=json");
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

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("patients.json")));
        bufferedWriter.write(content.toString());
        bufferedWriter.flush();
        bufferedWriter.close();
    }



    public static ArrayList<Patient> parsePatients(JSONArray entries){

        ArrayList<Patient> patients = new ArrayList<Patient>();
        patients.clear();
        for(int i=0; i<entries.length(); i++){
            //identifier
            String id = entries.getJSONObject(i).getJSONObject("resource").getString("id");
            //name
            String name = "";
            JSONArray given  = entries.getJSONObject(i).getJSONObject("resource").getJSONArray("name").getJSONObject(0).getJSONArray("given");
            for(int j=0; j<given.length(); j++)
                name = name + given.get(j) + " ";
            name = name + entries.getJSONObject(i).getJSONObject("resource").getJSONArray("name").getJSONObject(0).getString("family");
            name = name.replaceAll("[0-9]","");
            //phone
            String phone = entries.getJSONObject(i).getJSONObject("resource").getJSONArray("telecom").getJSONObject(0).getString("value");
            //gender
            String gender = entries.getJSONObject(0).getJSONObject("resource").getString("gender");
            //birth
            String birth = entries.getJSONObject(0).getJSONObject("resource").getString("birthDate");
            //address
            String address = "";
            JSONArray lines = entries.getJSONObject(i).getJSONObject("resource").getJSONArray("address").getJSONObject(0).getJSONArray("line");
            for(int j=0; j<lines.length(); j++)
                address = address + lines.get(j) + "\n";
            address = address + entries.getJSONObject(i).getJSONObject("resource").getJSONArray("address").getJSONObject(0).get("city") + ", " +
                    entries.getJSONObject(i).getJSONObject("resource").getJSONArray("address").getJSONObject(0).get("state") + ", " +
                            entries.getJSONObject(i).getJSONObject("resource").getJSONArray("address").getJSONObject(0).get("country");
            //martial status
            String martial = entries.getJSONObject(i).getJSONObject("resource").getJSONObject("maritalStatus").getString("text");

            Patient tmpPatient = new Patient(id, name, phone, gender, birth, address, martial);
            patients.add(tmpPatient);
        }
        return patients;
    }



}
