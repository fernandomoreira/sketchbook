/*
  Arrays

 Demonstrates the use of  an array to hold pin numbers
 in order to iterate over the pins in a sequence.
 Lights multiple LEDs in sequence, then in reverse.

 Unlike the For Loop tutorial, where the pins have to be
 contiguous, here the pins can be in any random order.

 The circuit:
 * LEDs from pins 2 through 7 to ground

 created 2006
 by David A. Mellis
 modified 30 Aug 2011
 by Tom Igoe

This example code is in the public domain.

 http://www.arduino.cc/en/Tutorial/Array
 */


int button = 2;
int buzzer = 13;
int buttonState = 0;        
int timer = 150;
int atraso;
int ledPins[] = {
  8, 9, 10, 11, 12
};       // an array of pin numbers to which LEDs are attached
int pinCount = 5;           // the number of pins (i.e. the length of the array)

int potpin = 0;  // analog pin used to connect the potentiometer
int val;    // variable to read the value from the analog pin


void setup() {
  Serial.begin(9600);
  // the array elements are numbered from 0 to (pinCount - 1).
  // use a for loop to initialize each pin as an output:
  for (int thisPin = 0; thisPin < pinCount; thisPin++) {
    pinMode (buzzer, OUTPUT);
    pinMode(ledPins[thisPin], OUTPUT);
    pinMode(button, INPUT);
  }
}

void loop() {

  
    val = analogRead(potpin); 
    atraso = map(val, 0, 1023, 5000, 20000);
    Serial.println (atraso);
    buttonState = digitalRead(button);
    
    if (buttonState ==HIGH){
      fazBinario();      
     }else{
      animacaoInicial();  
     
     }
}

void fazBinario(){
    digitalWrite(buzzer, 1);
    delay (100);
    digitalWrite(buzzer, 0);
    delay(100);
    digitalWrite(buzzer, 1);
    delay (100);
    digitalWrite(buzzer, 0);
    delay (100);
    digitalWrite(buzzer, 0);
    delay(200);
    digitalWrite(buzzer, 1);
    delay (200);
    digitalWrite(buzzer, 0);
    delay(200);
    digitalWrite(buzzer, 1);
    delay (200);
    digitalWrite(buzzer, 0);
    delay(200);
   
  
   for (int i = 0; i < pinCount; i++) {
      int randomico = random(0, 2);
      Serial.println (randomico);
      digitalWrite(ledPins[i], randomico);
      delay(200) ;
      
  }
  
delay (atraso);
  
  }

void animacaoInicial(){
 
  // loop from the lowest pin to the highest:
  for (int thisPin = 0; thisPin < pinCount; thisPin++) {
    // turn the pin on:
    digitalWrite(ledPins[thisPin], HIGH);
    delay(timer);
    // turn the pin off:
    digitalWrite(ledPins[thisPin], LOW);

  }

  // loop from the highest pin to the lowest:
  for (int thisPin = pinCount - 1; thisPin >= 0; thisPin--) {
    // turn the pin on:
    digitalWrite(ledPins[thisPin], HIGH);
    delay(timer);
    // turn the pin off:
    digitalWrite(ledPins[thisPin], LOW);
  }
  
 
  }
