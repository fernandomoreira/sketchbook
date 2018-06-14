/*
  Blink
  Turns on an LED on for one second, then off for one second, repeatedly.
 
  This example code is in the public domain.
 */
 
// Pin 13 has an LED connected on most Arduino boards.
// give it a name:
int porta3 = 3;
int porta2 = 2;

// the setup routine runs once when you press reset:
void setup() {                
  // initialize the digital pin as an output.
  pinMode(porta3, OUTPUT);     
  pinMode(porta2, OUTPUT);     
}

// the loop routine runs over and over again forever:
void loop() {
  //trava o motor
   delay(1000);   
  digitalWrite(porta3, 1); 
  digitalWrite(porta2, 1);  
  delay(1000);      ]
  //desliga o motor
  digitalWrite(porta3, 0); 
  digitalWrite(porta2, 0);   
  delay(1000);  
  //gira no sentido horario
  digitalWrite(porta3, 1); 
  digitalWrite(porta2, 0);    
  delay(1000);     
  //gira no sentido anti-horario
  digitalWrite(porta3, 0); 
  digitalWrite(porta2, 1);    
  delay(1000);                   
}
