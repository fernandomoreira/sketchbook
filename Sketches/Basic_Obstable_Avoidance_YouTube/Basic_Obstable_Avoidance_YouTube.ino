// Armuino (GF) --- Smart Robot Car ---
// Playlist: https://www.youtube.com/playlist?list=PLRFnGJH1nJiJxoO0woBW6vl_8URTQPhfL
// Smart Robot Car: Part 6 - Obstacle Avoidance Prototype Program Test 
// Video Demo: https://www.youtube.com/watch?v=_XRHTeurU34

#include <Servo.h> 
#include "NewPing.h"

// Pins on the Arduino Sensor Shield v5.0
#define SONAR_SERVO_PIN 4
#define TRIGGER_PIN     A0
#define ECHO_PIN        A1

#define MAX_DISTANCE    200

NewPing sonar(TRIGGER_PIN, ECHO_PIN, MAX_DISTANCE);
Servo myServo;

// Motor A [Right Side]
const int enA = 10;
const int in1 = 9;
const int in2 = 8;
// Motor B [Left Side]
const int enB = 5;
const int in3 = 7;
const int in4 = 6;

const int triggerDistance = 20;

// Variables
unsigned int time;            // to store how long it takes for the ultrasonic wave to come back
int distance;                 // to store the distance calculated from the sensor
int fDistance;                // to store the distance in front of the robot
int lDistance;                // to store the distance on the left side of the robot
int rDistance;                // to store the distance on the right side of the robot


char dist[3];
char rot[3];
int rotation = 0;
String output = "";

void setup() 
{ 
  pinMode (TRIGGER_PIN, OUTPUT);
  pinMode (ECHO_PIN, INPUT);
  myServo.attach(SONAR_SERVO_PIN);  // Attaches the Servo to the Servo Object 

  // set all the motor control pins to outputs
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
} 

void loop()
{
  scan();                                //Get the distance retrieved
  fDistance = distance;
  if(fDistance < triggerDistance){
    moveBackwards(255);
    delay(1000); 
    moveRight(255);
    delay(300);
    moveStop();
    scan();
    rDistance = distance;
    moveLeft(255);
    delay(500);
    moveStop();
    scan();
    lDistance = distance;
    if(lDistance < rDistance){
      moveRight(255);
      delay(1000);
      moveForward(255);
    }
    else{
      moveForward(255);
    }
  }
  else{
    moveForward(255);
  }
}

void scan()
{
  int deg = 90;
  myServo.write(deg);
  delay(10);

  time = sonar.ping();
  distance = time / US_ROUNDTRIP_CM;
  delay(10);
  if(distance <= 0){
    distance = triggerDistance;
  }

  delay(30);
}

void moveForward(int speed)
{
  // turn on motor A
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  // turn on motor B
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);

  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveBackwards(int speed)
{
  // turn on motor A
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);

  // turn on motor B
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);

  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveRight(int speed)
{
  // turn on motor A
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);

  // turn off motor B
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);

  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveLeft(int speed)
{
  // turn on motor A
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  // turn off motor B
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveStop()
{
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);  
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  delay(200);
}


