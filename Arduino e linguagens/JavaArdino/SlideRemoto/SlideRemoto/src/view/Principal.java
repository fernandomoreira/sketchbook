package view;

import model.ComunicacaoSerial;

public class Principal {



public static void main(String[] args) throws Exception {
	ComunicacaoSerial main = new ComunicacaoSerial();
	main.initialize();
	Thread t = new Thread() {
		public void run() {
			
			try {
				Thread.sleep(1000000);
			} catch (InterruptedException ie) {
			}
		}
	};
	t.start();
	System.out.println("- Iniciando, aguarde...-");
	System.out.println("");
	}

}
