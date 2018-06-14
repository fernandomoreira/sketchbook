//Programa: Conectando Sensor Ultrassonico HC-SR04 ao Arduino
//Autor: FILIPEFLOP


/*
No programa todo o cálculo é feito por meio da biblioteca Ultrasonic. 
O sensor é inicializado nos pinos trigger e echo, e depois efetuamos 
a leitura dos dados do sensor, atribuindo às variáveis cmMsec e inMsec
os valores das distâncias em centímetros e polegadas, respectivamente. 
Os dados são mostrados no serial monitor:
*/
 
//Carrega a biblioteca do sensor ultrassonico
#include <Ultrasonic.h>
 
//Define os pinos para o trigger e echo
#define pino_trigger A0
#define pino_echo A1
 
//Inicializa o sensor nos pinos definidos acima
Ultrasonic ultrasonic(pino_trigger, pino_echo);
 
void setup()
{
  Serial.begin(9600);
  Serial.println("Lendo dados do sensor...");
}
 
void loop()
{
  //Le as informacoes do sensor, em cm e pol
  float cmMsec, inMsec;
  long microsec = ultrasonic.timing();
  cmMsec = ultrasonic.convert(microsec, Ultrasonic::CM);
  inMsec = ultrasonic.convert(microsec, Ultrasonic::IN);
  //Exibe informacoes no serial monitor
  Serial.print("Distancia em cm: ");
  Serial.println(cmMsec);
  //Serial.print(" - Distancia em polegadas: ");
  //Serial.println(inMsec);
  delay(1000);
}
