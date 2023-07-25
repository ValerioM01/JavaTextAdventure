package it.uniroma1.textadv;

import java.util.ArrayList;
import java.util.List;

/*
 * Classe che identifica il giocatore, dotato di un nome, un inventario di oggetti (inizialmente vuoto) e di una serie di metodi 
 * che gli permettono di interagire con la stanza in cui si trova e con il giocatore.
 * E’ previsto un solo giocatore nel gioco, quindi è sempre possibile ottenere un riferimento al giocatore grazie al singleton pattern.
 * 
 * @author Valerio Mesiti
 */
public class Giocatore{
	
	private static Giocatore instance = null;
	private String nome;
	private List<Inventario> inventory = new ArrayList<Inventario>();
	
	/*
	 * Costruttore privato
	 * 
	 * @param	nome	nome del giocatore
	 */
	private Giocatore(String nome) { this.nome = nome; }
	
	/*
	 * Metodo che istanzia e restituisce l'unico oggetto che può essere creato.
	 * 
	 * @param	nome	nome del giocatore
	 */
	public static Giocatore getInstance(String nome) 
	{
		if (instance == null) { instance = new Giocatore(nome); }
		return instance;
	}
	
	/*
	 * Metodo che aggiunge un oggetto in input all'inventario
	 * controlla anche se viene aggiunto il tesoro cosi da dare vittoria al giocatore.
	 * 
	 * @param	o	oggetto da aggiungere all'inventario
	 */
	public String addInventory(Inventario o) 
	{ 
		if(o.getClass().getName().equals("it.uniroma1.textadv.Oggetto$Tesoro")) { System.out.println("HAI COMPLETATO IL GIOCO, GRAZIE PER AVER GIOCATO!"); System.exit(1); return ""; }
		else { inventory.add(o); return "Preso!"; }
	}

	/*
	 * Getter di un oggetto dal nome.
	 * 
	 * @param	nome	nome del oggetto cercato
	 * @return			l'oggetto cercato se è nell'inventario se non è presente null.
	 */
	public Inventario getItem(String nome) { for(Inventario o : getInventory()) if(o.getNome().equals(nome)) return o; return null; }
	
	/*
	 * Getter del nome del giocatore
	 */
	public String getNome() { return nome; }
	
	/*
	 * Getter dell'inventario
	 */
	public List<Inventario> getInventory() { return inventory; }
}
