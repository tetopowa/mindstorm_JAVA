package mindstorm;

/**
 * Interface Lumière permmettant d'instancier les deux types de lumieres possiblent dans le projet
 * La lampe de bureau et le plafonnier
 * Chaque classe a deux methodes , allumer et eteindre
 * 
 *
 */
public interface Lumiere {

	/**
	 * Permet d'allumer la lumiere, elle ne prend pas de parametre et s'applique directement sur l'objet appele
	 */
	public void allumer();
	
	
	/**
	 * Permet d'eteindre la lumiere, elle ne prend pas de parametre et s'applique directement sur l'objet appele
	 */
	public void eteindre();

}
