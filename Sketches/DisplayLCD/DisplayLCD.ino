/*
  Projeto Arduino com LCD.
  Por Jota
  ----------------------------------------
  --=<| www.ComoFazerAsCoisas.com.br |>=--
  ----------------------------------------
*/

//Define a utilização da biblioteca para controle de telas LDCs.
#include "LiquidCrystal.h"

//Criando um objeto da classe LiquidCrystal e
//inicializando com os pinos da interface.
LiquidCrystal lcd(9, 8, 5, 4, 3, 2);

void setup() {
  //Inicializando o LCD e informando o tamanho de 16 colunas e 2 linhas
  //que é o tamanho do LCD JHD 162A usado neste projeto.
  lcd.begin(16, 2);
}

void loop() {


frase1();
delay (1000);
frase4();
delay(1000);
rolarDisplay();
delay(600);



  delay(1000);
}

void rolarDisplay() {
  //Rolando o display para a esquerda 12 vezes
  for (int i = 0; i < 12; i++) {
    lcd.scrollDisplayLeft();
    delay(1000);
  }
}
  void frase1() {

    lcd.clear();          //limpa o display do LCD.
    lcd.print("Oi!!! ");  //imprime a string no display do LCD.
    delay(2000);

  }


  void frase2(){

    lcd.clear();
    lcd.setCursor(5, 0);  //posiciona cursor na coluna 0 linha 1 do LCD
    lcd.print("Tudo Bem???"); //imprime a string no display do LCD.
    delay(2000);

  }

  void frase3() {

    lcd.clear();
    lcd.print("Bem vindo!");
    lcd.setCursor(0, 1);
    lcd.print("este projeto?");
    delay(4000);


  }

  void frase4() {
    lcd.clear();
    lcd.print("www.fernandomoreira.info");
    delay(1000);


  }


