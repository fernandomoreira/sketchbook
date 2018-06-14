// EnviarCaractereSerial.ino

int cont = 0;


void setup() {
	Serial.begin(9600);
	pinMode(13, OUTPUT);


}

void loop() {
	char opcao= Serial.read();
	if (opcao == 'a'){
		digitalWrite(13, HIGH);


	}

	if (opcao == 'b'){
		digitalWrite(13, LOW);

}

}