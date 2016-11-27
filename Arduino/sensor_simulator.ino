#include <Time.h>
#include <TimeLib.h>
//#include <RTClib.h>
#include <aJSON.h>
//#include <Wire.h>


//RTC_DS1307 rtc;
int counter;
String value;

void setup() {
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
  //rtc.begin();
  //if (rtc.isrunning()){
    //Serial.println("Clock is running");
  //}
    
  setTime(14,1,1, 26, 11, 2016);
  counter = 0;
  value = "0";
}

void loop() {
  String timestamp;
  
  String Year = (String) year();
  String Month = (String) month();
  String Day = (String) day();
  String Hour = (String) hour();
  String Minute = (String) minute();
  String Second= (String) second();
  
  timestamp += Year;
  timestamp += "-";
  timestamp += Month;
  timestamp += "-";
  timestamp += Day;
  timestamp += "T";
  timestamp += Hour;
  timestamp += ":";
  timestamp += Minute;
  

  if( Second.toInt()%59 == 0){
    Serial.print("Sec:");
    Serial.println(Second);
    if(Minute.toInt()%2 == 0){
      Serial.print("Min:");
      Serial.println(Minute);
      value = "1";
    } else{
      Serial.print("Min:");
      Serial.println(Minute);
      value = "0";
    }

//  Serial.println("  ");
//  Serial.println((String)timestamp);
  
  int id = counter++;
  String g_self = "/measurements/measurements/";
  g_self += (String)id;
  char timestamp_m[19];
  timestamp.toCharArray(timestamp_m, 19);
  char value_m[2];
  value.toCharArray(value_m, 2);
  Serial.println(value);
  char g_self_m[35];
  g_self.toCharArray(g_self_m, 35);
  aJsonObject *root,*src;
    root = aJson.createObject();  
    aJson.addItemToObject(root, "id", aJson.createItem(id));
    aJson.addStringToObject(root,"time",     timestamp_m);
    aJson.addStringToObject(root,"type",     "energy");
    aJson.addNumberToObject(root,"x",     33);
    aJson.addNumberToObject(root,"z",     44);
    aJson.addNumberToObject(root,"y",     19);
    aJson.addNumberToObject(root,"floor",     7);
    aJson.addStringToObject(root,"value",     value_m);
    aJson.addStringToObject(root,"self",     g_self_m);
    aJson.addItemToObject(root, "source", src = aJson.createObject());
    aJson.addStringToObject(src,"self",        "/inventory/managedObjects/1080");
    aJson.addNumberToObject(src,"id",       1080);
    //aJson.addFalseToObject (fmt,"interlace");
   
    char *json_String=aJson.print(root);
    Serial.println(" ");
    Serial.println(json_String);
    aJson.deleteItem(root);
    free(json_String);
    delay(1000);
    }
}
