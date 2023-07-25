package it.uniroma1.textadv;

/*
 * Interfaccia che identifica oggetti trasportabili nell'inventario in game.
 * 
 * @author Valerio Mesiti
 */
public interface Inventario 
{
	/*
	 * Getter del nome dell'oggetto
	 */
	String getNome();
	
	/*
	 * Setter dello stato dell'oggetto (visibile o no nella stanza)
	 */
	void setVisibile(boolean b); 
}
