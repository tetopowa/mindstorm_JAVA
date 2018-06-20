package mindstorm;

import lejos.nxt.Motor;

/**
 * Classe permettant de gerer le plafonnier
 *
 */
public class Plafonnier implements Lumiere {
	
	private int intensite;
	
	/**
	 * Constructeur, prend en parametre l'intensite initiale du plafonnier
	 * @param intensite Intensite de demarrage
	 * @param ctrl Controleur lumiere
	 */
	public Plafonnier(int intensite) {
		this.intensite = intensite;
	}

	/**
	 * 
	 * @return Integer: L'intensite
	 */
	public int getIntensite() {
		return Motor.A.getSpeed();
	}

	/**
	 * Utilise la l'objet Motor de la bibliotheque NXT pour faire tourner le moteur
	 * Le moteur va se touner vers la droite pour signifier une augmentation d'intensite, et inversement pour la diminution
	 * @param intensite Intensite
	 */
	public void setIntensite(int intensite) {
		Motor.A.setSpeed(intensite);
	}

	public boolean isEtat() {
		return Motor.A.isMoving();
	}

	/**
	 * Utilise l'objet Motor de la bibliotheque NXT pour faire tourner le moteur
	 */
	public void allumer() {
		Motor.A.forward();
	}

	/**
	 * Utilise l'objet Motor de la bibliotheque NXT pour faire tourner le moteur
	 */
	public void eteindre() {
		Motor.A.stop();
	}
}
