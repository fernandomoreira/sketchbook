//---------------------------------------------------------------------------------------------------

  #include <IRremote.h>
  #define FNV_PRIME_32 16777619
  #define FNV_BASIS_32 2166136261
  
  int ledPin1 = 2;   //Onde será ligado o led 1
  int ledPin2 = 3;   //Onde será ligado o led 2
  int ledPin3 = 4;   //Onde será ligado o led 3
  int ledPin4 = 5;    //Onde será ligado o led 4
  int RECV_PIN = 11;    //Onde será ligado o Receptor IR
  IRrecv irrecv(RECV_PIN);
  decode_results results;
  
  void setup()
  {
   
    irrecv.enableIRIn();   //Iniciando a recepção do sinal 
    Serial.begin(9600);
  }
  int compare(unsigned int oldval, unsigned int newval) {  
    if (newval < oldval * .8) {
      return 0;
    } 
    else if (oldval < newval * .8) {
      return 2;
    } 
    else {
      return 1;
    }
  }
  unsigned long decodeHash(decode_results *results) {
    unsigned long hash = FNV_BASIS_32;
    for (int i = 1; i+2 < results->rawlen; i++) {
      int value =  compare(results->rawbuf[i], results->rawbuf[i+2]);
      hash = (hash * FNV_PRIME_32) ^ value;
    }
    return hash;
  }  
  void loop() {
    if (irrecv.decode(&results)) {
      /*
      Serial.print("'real' decode: ");     //Mostra o valor recebido
      Serial.print(results.value, HEX);    //!!!!!!!PARA IDENTIFICAR AS TEClAS
      Serial.print(", hash decode: ");      
      */
      
      unsigned long hash = decodeHash(&results); 
     //Serial.println (hash);                 //Mostra o valor já decodificado!
      irrecv.resume(); // Obrigatório a reinicialização da recepção!


  if (hash == 1386468383){   
       Serial.println ("back");
              
     }


  if (hash == 3622325019){   
       Serial.println ("next");
              
     }
     
       if (hash == 553536955){   
       Serial.println ("play");
              
     }

    }
  }
//---------------------------------------------------------------------------------------------------
/* CÓDIGO PARA VER O FUNCIONAMENTO USANDO LEDS, LEMBRANDO QUE OS VALORES SÃO FICTICIOS
  if (hash == 3321326852){   
       Serial.println (" Pra Cima!");
        digitalWrite(ledPin1, HIGH);  
        delay(100);  
        digitalWrite(ledPin1, LOW);         
     }
     if (hash == 4172527103){
       Serial.println (" Pra Direita!");
        digitalWrite(ledPin2, HIGH);  
        delay(100);  
        digitalWrite(ledPin2, LOW);
     }       
     if (hash == 3207847040){
     Serial.println (" Pra Baixo!");
        digitalWrite(ledPin3, HIGH);  
        delay(100);  
        digitalWrite(ledPin3, LOW);        
     }
     if (hash == 130972539){
     Serial.println (" Pra Esquerda!");     
        digitalWrite(ledPin4, HIGH);  
        delay(100);  
        digitalWrite(ledPin4, LOW);      
   }





*/
