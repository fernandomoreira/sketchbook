/*
  AnalogReadSerial
 Reads an analog input on pin 0, prints the result to the serial monitor 
 
 This example code is in the public domain.

CONTROLE DE MOUSE E TECLADO ATRAVÉS DE BOTÕES (NÃO USA POTENCIOMTRO)
 */



const int  setaCima = 2;
const int  setaBaixo = 3;
const int  setaEsquerda = 5;
const int  setaDireita = 4;
const int  botao4 = 7; 
const int  botao3 = 6; 
const int  pinoTeste = 13;


// Variables will change:
int estadosetaCima = 0;         // current state of the button
int estadosetaBaixo = 0;
int estadosetaDireita = 0;
int estadosetaEsquerda = 0;
int estadosetaBotao3 = 0;
int estadosetaBotao4 = 0;
int lastButtonState = 0;     // previous state of the button


//obtendo eixos

int eixoX = 512;
int eixoY = 380;


void setup() {
  Serial.begin(9600);

   // Initialize the button pin as an input:
  pinMode(pinoTeste, OUTPUT);
  pinMode(setaCima,INPUT);
  pinMode(setaBaixo, INPUT);
  pinMode(setaEsquerda, INPUT);
  pinMode(setaDireita, INPUT);
  pinMode(botao3, INPUT);
  pinMode(botao4, INPUT);


 
}

void loop() {


  // Read the pushbutton input pin:
  estadosetaCima = digitalRead(setaCima);
  estadosetaBaixo = digitalRead(setaBaixo);
  estadosetaEsquerda = digitalRead(setaEsquerda);
  estadosetaDireita = digitalRead(setaDireita);
  estadosetaBotao3 = digitalRead (botao3);
  estadosetaBotao4=  digitalRead (botao4);

   if (estadosetaBotao4 == HIGH) {
      Serial.println("botao4");
      delay (100);
  }

 if (estadosetaBotao3 == HIGH) {
      Serial.println("botao3");
      delay (100);
  }

//PASSADO COMANDOS PARA O MOUSE COORDENADA -Y

  if (estadosetaCima == HIGH) {
      if (eixoY>=4){
       eixoY-=4;
      }
   Serial.print("y");
   Serial.println(eixoY);
   delay (50); 

  }


//PASSADO COMANDOS PARA O MOUSE COORDENADA +Y

if (estadosetaBaixo == HIGH) {
   if (eixoY<760){
       eixoY+=4;
       }
    Serial.print("y");
    Serial.println(eixoY);
    delay (50);



  }

//PASSADO COMANDOS PARA O MOUSE COORDENADA -X
if (estadosetaEsquerda  == HIGH) {
      if (eixoX>4){
       eixoX-=4;
      }
   Serial.print("x");
   Serial.println(eixoX);
   delay (50); 
  }

//PASSADO COMANDOS PARA O MOUSE COORDENADA +Y
  if ( estadosetaDireita == HIGH) {
      if (eixoX<=1023){
       eixoX+=4;
       }
    Serial.print("x");
    Serial.println(eixoX);
    delay (50);
  }




/*

  lastButtonState = estadosetaCima;

 int potenciometro1 = analogRead(A0);
  Serial.print("x");
  Serial.println(potenciometro1);
  
  int potenciometro2 = analogRead(A1);
  Serial.print("y");
  Serial.println(potenciometro2);
  delay(50);


*/
}
