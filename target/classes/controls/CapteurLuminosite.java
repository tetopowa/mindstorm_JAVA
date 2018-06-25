package controls;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;

/**
 * Classe permettant d'acceder au capteur de luminosite
 *
 */
public class CapteurLuminosite {
	
	private LightSensor light;
	
	public CapteurLuminosite(SensorPort port) {
		this.light = new LightSensor(port);
	}


	/**
	 * @return the luminosite
	 */
	public int getLuminosite() {
		return light.getNormalizedLightValue();
	}	
}
