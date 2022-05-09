package dz.esi.cardiodata.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

import dz.esi.cardiodata.entity.*;

import org.eclipse.paho.client.mqttv3.*;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.Instant;
import java.util.*;



/** ======================================================
 * TODO : Subscriber Class
 * ===================================================== **/

@Component
public class Subscriber {

    private MqttClient client;
    private String brokerUrl;

    final static String TOPIC_ECG = "ecg";
    final static String TOPIC_TEMP = "temp";
    final static String TOPIC_BP = "bp";
    private List<String> topicList;

    InfluxDBService influxDBService = new InfluxDBService();


    public Subscriber(){
        System.out.println("== START SUBSCRIBER ==");

        this.brokerUrl = "tcp://localhost:1883";

        topicList = new ArrayList<>();
        topicList.add(TOPIC_ECG);
        topicList.add(TOPIC_TEMP);
        topicList.add(TOPIC_BP);

        runSubscriber();
    }

    public void runSubscriber(){
        try {
            client = new MqttClient(this.brokerUrl, MqttClient.generateClientId());
            client.setCallback(new MqttCallBack(influxDBService));
            client.connect();
            SubscribeClientToTopic();

        } catch (MqttException ex){
            System.out.println("Error on the subscription method");
        }
    }

    public void SubscribeClientToTopic() throws MqttException {
        for (String topic : topicList){
            client.subscribe(topic);
        }
    }

}

/** ======================================================
 * TODO : MQTT Class
 * ===================================================== **/

class MqttCallBack implements MqttCallback {

    InfluxDBService influxDBService;

    public MqttCallBack(InfluxDBService influxDBService){
        this.influxDBService = influxDBService;
    }

    public void connectionLost(Throwable throwable) {
        System.out.println("Error reading data" );
        throwable.printStackTrace();
    }

    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);

        if (topic.equals(Subscriber.TOPIC_ECG)){
            ECG esg = objectMapper.readValue(mqttMessage.getPayload(), ECG.class);
            influxDBService.writeNewDataOfECG(esg);
            System.out.println(esg.toString());
        }
        else if (topic.equals(Subscriber.TOPIC_TEMP)){
            Temp temp = objectMapper.readValue(mqttMessage.getPayload(), Temp.class);
            influxDBService.writeNewDataOfTemp(temp);
            System.out.println(temp.toString());
        }
        else if (topic.equals(Subscriber.TOPIC_BP)){
            Bp bp = objectMapper.readValue(mqttMessage.getPayload(), Bp.class);
            influxDBService.writeNewDataOfBp(bp);
            System.out.println(bp.toString());
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

}

/** ======================================================
 * TODO : Influxdb connection Class
 * ===================================================== **/

@Controller
class InfluxDBService{
    private String serverURL = "http://127.0.0.1:8086";
    private String databaseName = "test_db";
    private String username = "admin";
    private String password = "";

    /** Make connection to influxdb */
    InfluxDB influxDB = InfluxDBFactory.connect(serverURL, username, password);

    /** Create influxdb-java-client */
    InfluxDBClient influxDBClient;

    public InfluxDBService(){

        /** Set settings for influxdb-java-client */
        influxDB.createDatabase(databaseName);
        influxDB.createRetentionPolicy("defaultPolicy", "test_db", "30d", 1, true);
        this.influxDBClient = InfluxDBClientFactory.createV1(serverURL,
                username, password.toCharArray(), databaseName, "defaultPolicy");
    }

    /** Get influxdb-java-client for future use */
    public InfluxDBClient getInfluxDBClient(){
        return this.influxDBClient;
    }

    /** ======= Save out topic ECG-Temp-Bp in influxdb */
    public void writeNewDataOfTemp(Temp data){

        TempMappedDB tempMappedDB = new TempMappedDB();
        tempMappedDB.setValue(Double.valueOf(data.getValue()));
        tempMappedDB.setTime(Instant.now());

        WriteApiBlocking writeApi = getInfluxDBClient().getWriteApiBlocking();
        writeApi.writeMeasurement( WritePrecision.NS, tempMappedDB);

    }

    public void writeNewDataOfBp(Bp data){

        BpMappedDB bpMappedDB = new BpMappedDB();
        bpMappedDB.setPulse(data.getValue().getPulse());
        bpMappedDB.setDiastolic(data.getValue().getDiastolic());
        bpMappedDB.setSystolic(data.getValue().getSystolic());
        bpMappedDB.setTime(Instant.now());

        WriteApiBlocking writeApi = getInfluxDBClient().getWriteApiBlocking();
        writeApi.writeMeasurement( WritePrecision.NS, bpMappedDB);
    }

    public void writeNewDataOfECG(ECG data)  {
        Calendar calendar = Calendar.getInstance();
        Long timeMilli = calendar.getTimeInMillis();
        for (int i=0; i<data.getValue().toArray().length; i++ ) {
            ECGMappedDB ecgMappedDB = new ECGMappedDB();
            ecgMappedDB.setValue(data.getValue().get(i));
            ecgMappedDB.setValueSaveTime(timeMilli);

            timeMilli = timeMilli + 2;

            WriteApiBlocking writeApi = getInfluxDBClient().getWriteApiBlocking();
            writeApi.writeMeasurement( WritePrecision.NS, ecgMappedDB);
        }


    }






}
