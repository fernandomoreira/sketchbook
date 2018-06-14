#include <Servo.h>

//motor1
int speed1 = 3;
int motor1A = 2;
int motor1B = 4;
// motor 2
int speed2 = 6;
int motor2A = 5;
int motor2B = 7;

//servosmotores
Servo myservo; 

int pos = 0;   

void setup(){

  pinMode(speed1, OUTPUT);
  pinMode(speed2, OUTPUT);
  pinMode(motor1A, OUTPUT);
  pinMode(motor1B, OUTPUT);
  pinMode(motor2A, OUTPUT);
  pinMode(motor2B, OUTPUT);
  Serial.begin(9600);

myservo.attach(9);  
     
}


void loop(){


//definindo velocidade
analogWrite(speed1, 80);
analogWrite(speed2, 80);
scannerNeck();
goFront(2000);
neutral(1000);



}

  void posicionaCabeca(){
    
    
    
    }

void scannerNeck(){for (pos = 0; pos <= 180; pos += 1) { 
   
    myservo.write(pos);              
    delay(15);                       
    Serial.println(pos);
  }
  delay(200);
  for (pos = 180; pos >= 0; pos -= 1) { 
    myservo.write(pos);              
    delay(15);                       
    Serial.println(pos);
  }
  delay(200);
}
 
void runLeft(){
  // antihorario
digitalWrite(motor1A, 0);
digitalWrite (motor1B, 1);
digitalWrite(motor2A, 0);
digitalWrite (motor2B, 1);
Serial.println("antihorario");

  }

void goFront(int tempo){
  digitalWrite(motor1A, 0);
digitalWrite (motor1B, 1);
digitalWrite(motor2A, 1);
digitalWrite (motor2B, 0);
delay(tempo);
  
  
  }

void goBack(int tempo){
  digitalWrite(motor1A, 1);
digitalWrite (motor1B, 0);
digitalWrite(motor2A, 0);
digitalWrite (motor2B, 1);
 delay(tempo);



  }

  
 void runRight(){
    
    // horario
digitalWrite(motor1A, 1);
digitalWrite (motor1B, 0);
digitalWrite(motor2A, 1);
digitalWrite (motor2B, 0);
Serial.println("horario");

 
    }

void park(int tempo){
  // freia o motor
digitalWrite(motor1A, 1);
digitalWrite (motor1B, 1);
digitalWrite(motor2A, 1);
digitalWrite (motor2B, 1);
delay(tempo);
 
  }

void neutral(int tempo){
  
  digitalWrite(motor1A, 0);
digitalWrite (motor1B, 0);
digitalWrite(motor2A, 0);
digitalWrite (motor2B, 0);
delay(tempo);

  
  }

void desacelerar(){
   // decelerate from maximum speed to zero
  for (int i = 255; i >= 0; --i)  {
    analogWrite(speed1, i);
    analogWrite(speed2, i);
    delay(20);
  }

  }
void acelerar(){
  
   // accelerate from zero to maximum speed
  for (int i = 0; i < 256; i++)
  {
    analogWrite(speed1, i);
    analogWrite(speed2, i);
    delay(20);
  } 
  
  }


  /*
 void downHead(){
  
  for (posServoHead = 5; posServoHead <= 45; posServoHead += 1) { 
      servoHead.write(posServoHead);            
    delay(30);                      
  }
 }

 
void upHead(){
  for (posServoHead = 45; posServoHead >= 5; posServoHead -= 1) { 
    servoHead.write(posServoHead);              
    delay(30);                       
  }

  */
 
