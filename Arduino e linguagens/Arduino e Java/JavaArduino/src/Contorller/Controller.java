package Contorller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import View.FrameTela;

public class Controller implements ActionListener {
	private FrameTela frame;
	private ControlePorta arduino;
	
	public Controller(FrameTela frame){
		this.frame = frame;
		arduino = new ControlePorta();
		arduino.initSerial();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		Component comp = (Component) arg0.getSource();
		if(comp.getName() == "btnRight"){
			arduino.enviaDados('a');
		}else if(comp.getName() == "btnLeft"){
			arduino.enviaDados('b');
		}else if(comp.getName() == "btnFront"){
			arduino.enviaDados('3');
		}else if(comp.getName() == "btnBack"){
			arduino.enviaDados('4');
		}else{
			arduino.close();
		}
		
	}

}
