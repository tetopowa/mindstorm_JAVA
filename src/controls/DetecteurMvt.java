package controls;

import lejos.nxt.SensorPort;
import lejos.nxt.SoundSensor;

/**
 * Classe permettant de gerer le detecteur de mouvement
 *
 */
public class DetecteurMvt {
	
	private SoundSensor sound;
	
	public DetecteurMvt(SensorPort port) {
		this.sound = new SoundSensor(port);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean detecte() {
		if(sound.readValue() > 20){
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public int getSound() {
		return sound.readValue();
	}
}
