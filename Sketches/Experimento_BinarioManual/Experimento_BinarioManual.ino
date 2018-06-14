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
int timer = 150;           // The higher the number, the slower the timing.
int ledPins[] = {8, 9, 10, 11, 12
};       // an array of pin numbers to which LEDs are attached
int pinCount = 5;           // the number of pins (i.e. the length of the array)

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
   
      int atraso = 7000;
      int randomico = random(1, 31);
      Serial.println (randomico);

      if (randomico = 32){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("31");
      }
       if (randomico = 0){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("0");
       }

       if (randomico = 16){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("16");
       }
if (randomico = 24){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("24");
       }
 if (randomico = 28){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("28");
       }
      
 if (randomico = 30){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("30");
       }
  
if (randomico = 8){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("8");
       }
  if (randomico = 4){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("4");
       }
if (randomico = 2){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("2");
       }
if (randomico = 1){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("1");
       }

       if (randomico = 12){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("12");
       }
if (randomico = 6){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("6");
       }
if (randomico = 3){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("3");
       }

if (randomico = 14){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("14");
       }
       if (randomico = 7){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("7");
       }

       if (randomico = 15){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("15");
       }
       if (randomico = 17){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("17");
       }

       if (randomico = 10){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("10");
       }

       if (randomico = 21){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("21");
       }

       if (randomico = 5){
         digitalWrite(ledPins[8], 0);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 1);
         digitalWrite(ledPins[11], 0);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("5");
       }

       if (randomico = 26){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 1);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 0);
         delay (atraso);
         Serial.print("26");
       }

       if (randomico = 19){
         digitalWrite(ledPins[8], 1);
         digitalWrite(ledPins[9], 0);
         digitalWrite(ledPins[10], 0);
         digitalWrite(ledPins[11], 1);
         digitalWrite(ledPins[12], 1);
         delay (atraso);
         Serial.print("19");
       }
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
