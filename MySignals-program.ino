
#include <MySignals.h>
#include "Wire.h"
#include "SPI.h" 

int deviceId = 1;
String token = "token";

void setup() 
{ 
  Serial.begin(19200);
  
  MySignals.begin();

  MySignals.initSensorUART();
  MySignals.enableSensorUART(BLOODPRESSURE);

}


void loop() 
{
  getTemp();
  getECG();
  getBP();
}


void getTemp(){
  float temperature = MySignals.getTemperature();
  Serial.println("\n{ \"type\": \"temp\", \"value\": " + String(temperature) + ", \"deviceId\": " + String(deviceId) + "}");
  delay(1000);
}

void getECG(){
  Serial.print("\n{ \"type\": \"ecg\", \"value\": [ ");
  
  float ECG;
  ECG = MySignals.getECG(VOLTAGE);
  Serial.print(String(ECG));
  delay(20);
  for(int i = 0; i < 100; i = i + 2){
    Serial.print(",");
    ECG = MySignals.getECG(VOLTAGE);
    Serial.print(String(ECG));
    delay(100);
  }  
   
  Serial.println("]}");
  delay(1000);
}


void getBP(){
  if (MySignals.getStatusBP())
  {

    if (MySignals.getBloodPressure() == 1)
    {
      MySignals.disableMuxUART();
      
      Serial.println("\n {\"type\": \"bp\", \"value\": {  \"diastolic\": " + String(MySignals.bloodPressureData.diastolic) + ", \"systolic\": "+ String(MySignals.bloodPressureData.systolic) + + ", \"pulse\": "+ String(MySignals.bloodPressureData.pulse) +  "}}"); 
      MySignals.enableMuxUART();

    }
  }
  delay(1000);
}
