package view;

import model.ComunicacaoSerial;



public class Principal2 {

	
	public static void main(String[] args) {
		 ComunicacaoSerial serial = new ComunicacaoSerial();
		serial.initialize(); //abre a porta serial serial
		serial.enviaDados('b');
		serial.close();//fecha e destrava a porta serial
		

	}

}
