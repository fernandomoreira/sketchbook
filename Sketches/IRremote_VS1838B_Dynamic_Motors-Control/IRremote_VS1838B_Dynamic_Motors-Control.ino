// GF --- Smart Robot Car ---
// Playlist: https://www.youtube.com/playlist?list=PLRFnGJH1nJiJxoO0woBW6vl_8URTQPhfL
// Smart Robot Car: Part 5 - Wiring and Program Sketch with IR Control 
// Wiring & Video Demo: https://www.youtube.com/watch?v=BqYcmBRYGWQ

#include "IRremote.h"

int receiver = 11; // Signal Pin of IR receiver to Arduino Digital Pin 11

// connect motor controller pins to Arduino digital pins
// motor A [right side]
int enA = 10;
int in1 = 9;
int in2 = 8;
// motor B [left side]
int enB = 5;
int in3 = 7;
int in4 = 6;

/*-----( Declare objects )-----*/
IRrecv irrecv(receiver);           // create instance of 'irrecv'
decode_results results;            // create instance of 'decode_results'

void setup()   /*----( SETUP: RUNS ONCE )----*/
{
  Serial.begin(9600);
  Serial.println("IR Receiver Button Decode"); 
  irrecv.enableIRIn(); // Start the receiver

  // set all the motor control pins to outputs
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
}/*--(end setup )---*/

void loop()   /*----( LOOP: RUNS CONSTANTLY )----*/
{
  if (irrecv.decode(&results)) // have we received an IR signal?
  {
    translateIR(); 
    irrecv.resume(); // receive the next value
  }  
}/* --(end main loop )-- */

void translateIR() // takes action based on IR code received
{
  switch(results.value)
  {
  // describing Remote IR codes 
  case 0xFF629D: Serial.println(" CH"); break;
  case 0xFF22DD: 
                Serial.println(" PREV");
                moveBackwards(200);
                break;
  case 0xFF02FD: 
                Serial.println(" NEXT");
                moveForward(200);
                break;
  case 0xFFC23D: 
                Serial.println(" PLAY/PAUSE");
                moveStop();
                break;
  case 0xFFA857: Serial.println(" +"); break;
  case 0xFF6897: Serial.println(" 0");    break;
  case 0xFF9867: Serial.println(" 100+");    break;
  case 0xFFB04F: Serial.println(" 200+");    break;
  case 0xFF30CF: Serial.println(" 1");    break;
  case 0xFF18E7: Serial.println(" 2");    break;
  case 0xFF7A85: Serial.println(" 3");    break;
  case 0xFF10EF: Serial.println(" 4");    break;
  case 0xFF38C7: Serial.println(" 5");    break;
  case 0xFF5AA5: Serial.println(" 6");    break;
  case 0xFF42BD: Serial.println(" 7");    break;
  case 0xFF4AB5: Serial.println(" 8");    break;
  case 0xFF52AD: Serial.println(" 9");    break;
  case 0xFFE01F: Serial.println(" -");    break;
  case 0xFF906F: Serial.println(" EQ");    break;
  case 0xFFA25D:
                Serial.println(" CH-");
                moveLeft(150);
                break;
  case 0xFFE21D:
                Serial.println(" CH+");
                moveRight(150);
                break;
  
  case 0xFFFFFFFF: Serial.println(" REPEAT");break;  

  default: 
    Serial.println(" other button   ");
    Serial.println(results.value, HEX);

  }// End Case

  delay(500); // Do not get immediate repeat

} //END translateIR

void moveForward(int speed)
{
  moveStop();
  
  // turn on motor A
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  // turn on motor B
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);

  // set speed within a possible range 0~255
  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveBackwards(int speed)
{
  moveStop();
  
  // turn on motor A
  digitalWrite(in1, LOW);
  digitalWrite(in2, HIGH);

  // turn on motor B
  digitalWrite(in3, LOW);
  digitalWrite(in4, HIGH);

  // set speed within a possible range 0~255
  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveLeft(int speed)
{
  moveStop();
  
  // turn on motor A
  digitalWrite(in1, HIGH);
  digitalWrite(in2, LOW);

  // turn off motor B
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  // set speed within a possible range 0~255
  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveRight(int speed)
{
  moveStop();
  
  // turn on motor A
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);

  // turn off motor B
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);

  // set speed within a possible range 0~255
  analogWrite(enA, speed);
  analogWrite(enB, speed);
}

void moveStop()
{
  // now turn off motors
  digitalWrite(in1, LOW);
  digitalWrite(in2, LOW);  
  digitalWrite(in3, LOW);
  digitalWrite(in4, LOW);

  delay(200);
}
/* ( THE END ) */


