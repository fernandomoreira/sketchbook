


int buzzer = 50;
int led = 52;

void setup() {

  Serial.begin(9600);
  pinMode (buzzer, OUTPUT);
  pinMode (led, OUTPUT);


}

void loop() {

  char c = Serial.read();

  if (c == 'a') {
    digitalWrite(buzzer, 1);
    Serial.println("Buzzer Ligado");
  }
  if (c == 'A') {
    digitalWrite(buzzer, 0);
    Serial.println("Buzzer desligado");
  }

    if (c == 'C') {
    digitalWrite(led, 1);
    Serial.println("led Ligado");
  }
  if (c == 'c') {
    digitalWrite(led, 0);
    Serial.println("lod desligado");
  }

}
