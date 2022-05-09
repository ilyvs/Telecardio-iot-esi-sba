# Telecardio-Embed-Lab

## 1- MySignals Librairies
- MySignals
- Wire
- SPI


## 2- MQTT Protocol
- Publish-and-Subscribe protocol, meaning that instead of communicating with a server, client devices and applications publish and subscribe to topics handled by a broker


## 3- Database 
- InfluxDB : Open-source time series database, used for Internet of Things sensor data storage


## 4- Backend Microservice 
- Spring Boot Framework : Open source Java-based framework used to create a microservices & web application
- Maven Dependencies : (Jackson is a java based library to serialize or map java objects to JSON and vice versa)
```
        <dependency>
            <groupId>org.eclipse.paho</groupId>
            <artifactId>org.eclipse.paho.client.mqttv3</artifactId>
            <version>1.2.0</version>
        </dependency>

```
```
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.12.5</version>
        </dependency>

```
```   
        <dependency>
            <groupId>com.influxdb</groupId>
            <artifactId>influxdb-client-java</artifactId>
            <version>3.2.0</version>
        </dependency>
```

## 5- How it's work 
1. Upload MySignals-program.ino into MySignals cart (televersing operation)

2. Connect Raspberry Pi3 To Pc (network connection)

3. Run Mosquitto Mqtt Broker in Pc (localhost:1883)

4. Run cardio-data-service, the service will Subscribe to 3 topics (ECG + Blood pressure  + Temperature)

5. Run pi_client.py (python script) in Raspberry Pi3 carte, in terminal: 
```
   python3 pi_client.py --broker Pc-IVP4-Addrs)
```
6. The Sensors send date to MySignals cart. (Data : ECG + Blood pressure  + Temperature)

7. MySignals cart send data to Raspberry Pi3 (cable connection)

8. Raspberry Pi3 will Publish the 3 topics to Mosquitto Broker (the broker adresse will be my pc ipv4 adresse)

9. When cardio-data-service get's the data, he will deserialize the json object (using Jackson) then save the date in influxdb 

## Notes :  
- ECG Data : The sonsor send 500 value every second, so to save the data correctly, every 2 millisecond we will save one value.
- Data presenting :  we can use "Grafana" that automatically connect to influxb, or building Api that retrieve from influxdb and send it to fronend client
- Check : Subscriber.java, WebApiController.java classes and Pictures Folder



