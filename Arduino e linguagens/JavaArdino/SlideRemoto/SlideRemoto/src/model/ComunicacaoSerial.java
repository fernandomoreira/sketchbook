package model;



import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

public class ComunicacaoSerial implements SerialPortEventListener {
	
	SerialPort serialPort;
	/** The port we're normally going to use. */
	private static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1", // Mac
																				// OS
																				// X
			"/dev/ttyACM0", // Linux
			"COM1", // Windows
	};
	
	
	/**
	 * A BufferedReader which will be fed by a InputStreamReader converting the
	 * bytes into characters making the displayed results codepage independent
	 */
	private BufferedReader input;
	/** The output stream to the port */
	private OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 1000;
	/** Default bits per second for COM port. */
	private static final int DATA_RATE = 9600;

	public void initialize() {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		System.out.println("Port ID: ");
		System.out.println(portId);
		System.out.println("");
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			// add event listeners
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}

	/**
	 * This should be called when you stop using the port. This will prevent
	 * port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Handle an event on the serial port. Read the data and print it.
	 */
	
	//cria as variaveis com coordenadas inisiais referentes ao centro do desktop
	int coordenadaX = 512;
	int coordenadaY = 380;
	public synchronized void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				
								
				String inputLine = input.readLine();
				
								
				
				//PROCEDIMENTOS PARA CAPTURAR BOTOES PELA SERIAL ATRAVES DO ARDUINO E SIMULAR CLIQUE DO MOUSE
				if (inputLine.equals("direito")) {
					
					try {
						Robot robot = new Robot();
						 // Clicando com o botao direito                     
				            robot.mousePress(InputEvent.BUTTON3_MASK);
				            // Soltando o botão direito
				            robot.mouseRelease(InputEvent.BUTTON3_MASK);
				            
						} catch (AWTException e) {
							e.printStackTrace();
						}
					}
				
				if (inputLine.equals("esquerdo")) {
					
					try {
						Robot robot = new Robot();
						 // Clicando com o botao direito do mouse                    
				            robot.mousePress(InputEvent.BUTTON1_MASK);
				            // Soltando o botão direito
				            robot.mouseRelease(InputEvent.BUTTON1_MASK);
				            
					} catch (AWTException e) {
						e.printStackTrace();
					}
				}	
				
					
				
				
				if (inputLine.equals("teclado")) {
					System.out.println("Pressiona e solta a tecla S");
					try {
						Robot robot = new Robot();
						robot.keyPress(KeyEvent.VK_F5);
						robot.delay(500);
						robot.keyRelease(KeyEvent.VK_F5);
					} catch (AWTException e) {
						e.printStackTrace();
					}
				}
			
				
			} catch (Exception e) {
				System.err.println(e.toString());
			}
		}
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

		}
		// Ignore all the other eventTypes, but you should consider the other
		// ones.
	}


}