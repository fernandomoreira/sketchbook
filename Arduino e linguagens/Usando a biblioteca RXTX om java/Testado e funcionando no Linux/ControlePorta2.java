package model;


import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;



public class ControlePorta2 {

	private OutputStream serialOut;
	public void initSerial() {

		try {
			CommPortIdentifier portId = null;
			try {
				portId = CommPortIdentifier.getPortIdentifier("/dev/ttyUSB1");
			} catch (NoSuchPortException npe) {
				System.out.println("Porta não encontrada!");
				
			}
			SerialPort port = (SerialPort)portId.open("Título comunicação serial", 9600);
			serialOut = port.getOutputStream();
			port.setSerialPortParams(9600,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close(){
		try {
			serialOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void enviaDados(char opcao){
		try {
			serialOut.write(opcao);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}