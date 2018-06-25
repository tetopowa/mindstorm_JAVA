package mindstorm;

import lejos.nxt.LCD;

/**
 * Classe permettant de gerer la lampe de bureau
 *
 */
public class Lampe implements Lumiere {
	
	private boolean etat;
	
	public Lampe(boolean etat) {
		this.etat = etat;
	}

	public boolean isEtat() {
		return etat;
	}

	public void setEtat(boolean etat) {
		this.etat = etat;
	}

	/**
	 * Apres l'allumage de la lampe fictive: 
	 * Affiche a l'ecran du NXT l'indicateur si la lampe est allumee ou non 
	 * Utilisation de lejos.nxt.lcd pour afficher un texte a l'ecran
	 */
	public void allumer() {
		this.setEtat(true);
		LCD.clear(0);
		LCD.drawString("Light ON",0,0);
	}

	/**
	 * Apres l'extinction de la lampe fictive: 
	 * Affiche à l'écran du NXT l'indicateur si la lampe est allumée ou non 
	 * Utilisation de lejos.nxt.lcd pour afficher un texte à l'écran
	 */
	public void eteindre() {
		this.setEtat(false);
		LCD.clear(0);
		LCD.drawString("Light OFF",0,0);
	}
}
