// GF --- Smart Robot Car ---
// Playlist: https://www.youtube.com/playlist?list=PLRFnGJH1nJiJxoO0woBW6vl_8URTQPhfL
// Smart Robot Car: Part 2 - Arduino Sensor Shield Test with Sonar and Servo Motor 
// Wiring & Video Demo: https://www.youtube.com/watch?v=4I-Hj8sffGM

#define TRIG_PIN A0
#define ECHO_PIN A1
#include <Servo.h> 
//#include <NewPing.h>

#define MAX_DISTANCE 300
//NewPing sonar(TRIG_PIN, ECHO_PIN, MAX_DISTANCE); 
Servo myServo;  // create servo object to control a servo 
char dist[3];
char rot[3];
int rotation = 0;
String output = "";

void setup() {
  pinMode (TRIG_PIN, OUTPUT);
  pinMode (ECHO_PIN, INPUT);
  myServo.attach(4);  // attaches the servo on pin 9 to the servo object 
  Serial.begin(115200);
  Serial.println("===== Ultrasonic rotating sonar =====");
}

void loop() { 
  // scan right to left
  for (int deg = 10; deg < 170; deg+=5) {
    myServo.write(deg);
    delay(300);
    displaySonar(deg);
  }

  // scan left to right
  for (int deg = 170; deg > 10; deg-=5) {
    myServo.write(deg);
    delay(300);
    displaySonar(deg);
  }
}

void displaySonar(int degrees) {
//  int distance = sonar.ping_cm(); 
  delay(30);
  if (distance < 0) distance = 0; 
  
  sprintf(dist,"%3d",distance);
  Serial.print("Range:");
  Serial.print(dist);
  Serial.print("cms/");
  sprintf(rot,"%3d",degrees);
  Serial.print(rot);
  Serial.print("deg:");

  for (int dloop = 0; dloop < distance/4; dloop++) {
    Serial.print("-");
  }
  Serial.println("=");
}
