/*
  AnalogReadSerial
 Reads an analog input on pin 0, prints the result to the serial monitor 
 
 This example code is in the public domain.

EXEMPLO DE CONTROLE DO MOUSE COM POTENCIOMETRO
 */

void setup() {
  Serial.begin(9600);
}

void loop() {
  int potenciometro1 = analogRead(A0);
  Serial.print("x");
  Serial.println(potenciometro1);
  
  int potenciometro2 = analogRead(A1);
  Serial.print("y");
  Serial.println(potenciometro2);
  delay(500);
}
