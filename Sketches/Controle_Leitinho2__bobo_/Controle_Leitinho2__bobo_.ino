#include <Servo.h>

//motor1
//int speed1 = 3;
int motor1A = 2;
int motor1B = 4;
// motor 2
//int speed2 = 6;
int motor2A = 5;
int motor2B = 7;

//servosmotores
Servo myservo; 

int pos = 0;   

void setup(){

//  pinMode(speed1, OUTPUT);
 // pinMode(speed2, OUTPUT);
  pinMode(motor1A, OUTPUT);
  pinMode(motor1B, OUTPUT);
  pinMode(motor2A, OUTPUT);
  pinMode(motor2B, OUTPUT);
  Serial.begin(9600);

myservo.attach(9);  
     
}


void loop(){
//1200 =360
//750 = 360

delay(500);
int randomic = random(11);

Serial.println(randomic);



if (randomic == 0){
  scannerNeck();
  goFront(1200);
  neutral(2000);
  runLeft(360);
}


 if (randomic == 1){
  scannerNeck();
  goBack(300);
  runRight(1200);
  neutral(2000);
}

 if (randomic == 2){
  goFront(1000);
  delay(200);
  runLeft(300);
  delay(200);
  goFront(400);
  neutral(2000);
}


 if (randomic == 3){
  runLeft(1700);
  neutral(3000);
  goFront(1000);
}

if (randomic == 4){
  runRight(200);
  delay(200);
  goFront(300);
  delay(200);
  runLeft(1300);
  
}


if (randomic == 5){
park(1000);
}


if (randomic == 6){
park(1000);
}
if (randomic == 7){
park(1000);
}

if (randomic == 8){
park(1000);
}

if (randomic == 9){
park(1000);
}

if (randomic == 10){
 park(1000);
  
}


if (randomic == 11){
park(1000);
}


}



void scannerNeck(){for (pos = 0; pos <= 180; pos += 1) { 
   
    myservo.write(pos);              
    delay(15);                       
   
  }
  delay(200);
  for (pos = 180; pos >= 0; pos -= 1) { 
    myservo.write(pos);              
    delay(15);                       

  }
  delay(200);
}
 
void runLeft(int tempo){
  // antihorario
digitalWrite(motor1A, 0);
digitalWrite (motor1B, 1);
digitalWrite(motor2A, 0);
digitalWrite (motor2B, 1);
delay(tempo);


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

  
 void runRight(int tempo){
  
digitalWrite(motor1A, 1);
digitalWrite (motor1B, 0);
digitalWrite(motor2A, 1);
digitalWrite (motor2B, 0);
delay(tempo);

 
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

