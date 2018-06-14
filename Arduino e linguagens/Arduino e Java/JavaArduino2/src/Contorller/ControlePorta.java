package Contorller;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import java.io.IOException;
import java.io.OutputStream;



public class ControlePorta {

	private OutputStream serialOut;
	public void initSerial() {

		try {
			CommPortIdentifier portId = null;
			try {
				portId = CommPortIdentifier.getPortIdentifier("COM5");
			} catch (NoSuchPortException npe) {
				
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

	public void enviaDados(int opcao){
		try {
			serialOut.write(opcao);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}