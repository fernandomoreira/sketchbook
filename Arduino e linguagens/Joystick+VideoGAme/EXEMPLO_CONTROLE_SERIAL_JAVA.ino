/*********************************************/
// this constant won't change:
const int  buttonPin = 5; // the pin that the pushbutton is attached to
int pino13 = 13;
// Variables will change:
int buttonState = 0;         // current state of the button
int lastButtonState = 0;     // previous state of the button

void setup() {
  // Initialize the button pin as an input:
  pinMode(pino13, OUTPUT);
  pinMode(buttonPin, INPUT);
  // Initialize serial communication:
  Serial.begin(9600);
}

void loop() {
  // Read the pushbutton input pin:
  buttonState = digitalRead(buttonPin);
 
    if (buttonState == HIGH) {
      // If the current state is HIGH then the button
      // Send to serial that the engine has started:   
      Serial.println("Fernando");
      digitalWrite(pino13, HIGH);
      delay (100);
  }
  else{
   digitalWrite(pino13, LOW);
  }
  // Save the current state as the last state,
  // for next time through the loop
  lastButtonState = buttonState;
}
