//Motores
int motor1A = 2;
int motor1B = 3;
int motor2A = 4;
int motor2B = 5;

//Acessorios
int led = 12;
int buzzer = 13;


void setup() {

  pinMode (motor1A, OUTPUT);
  pinMode (motor1B, OUTPUT);
  pinMode (motor2A, OUTPUT);
  pinMode( motor2B, OUTPUT);
  pinMode (led, OUTPUT);
  pinMode (buzzer, OUTPUT);  
  Serial.begin(9600);
}

void loop() {
// horario
digitalWrite(motor1A, 1);
digitalWrite (motor1B, 0);
Serial.println("horario");
delay(5000);

// freia o motor
digitalWrite(motor1A, 1);
digitalWrite (motor1B, 1);
Serial.println("freio");
delay(5000);



// antihorario
digitalWrite(motor1A, 0);
digitalWrite (motor1B, 1);
Serial.println("antihorario");
delay(5000);

  // freia o motor
digitalWrite(motor1A, 0);
digitalWrite (motor1B, 0);
Serial.println("neutro");
delay(5000);
  
}
