package controls;

import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

/**
 * Classe permettant de gerer le capteur de contact, ON/OFF
 *
 */
public class CapteurContact {
	
	
	private TouchSensor touch;
	
	
	public CapteurContact(SensorPort port) {
		this.touch = new TouchSensor(port);
	}

	/**
	 * 
	 * @return
	 */
	public boolean contact() {
		if (!touch.isPressed())
			return false;
		else
			return true;		
	}
}
