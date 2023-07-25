package it.uniroma1.textadv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Classe che identifica una stanza, l'elemento base del mondo, ossia un'area del gioco.
 * 
 * @author Valerio Mesiti
 */
public class Stanza {

	private String name;
	private String description;
	private List<Oggetto> items = new ArrayList<>();
	private List<Character> characters = new ArrayList<>();
	private Map<Direzione, Link> links = new HashMap<>();

	/*
	 * Costruttore con visibilita di package per il builder pattern
	 * 
	 * @param	name		il nome della stanza
	 * @param	description	la descrizione della stanza
	 * @param	items		gli oggetti che contiene la stanza
	 * @param	characters	i personaggi che contiene la stanza
	 * @param	links		le uscite che contiene la stanza	
	 */
	Stanza(String name, String description, List<Oggetto> items, List<Character> characters, Map<Direzione, Link> links) 
	{
		this.name = name; 
		this.description = description;
		this.items = items;
		this.characters = characters;
		this.links = links;
	}
	
	/*
	 * Setter per aggiungere oggetti nella stanza.
	 * 
	 * @param item l'oggetto da aggiungere.
	 */
	public void addItem(Oggetto o) { items.add(o); }
	
	/*
	 * Getter per i personaggi presenti nella stanza.
	 */
	public List<Character> getCharacters() { return characters; }
	
	/*
	 * Getter di un personaggio dal nome.
	 * 
	 * @param	nome	nome del personaggio cercato
	 * @return			il personaggio cercato se è nella stanza se non è presente null.
	 */
	public Character getCharacter(String nome) { for(Character p : getCharacters()) if(p.getNome().toLowerCase().equals(nome)) return p; return null; }
	
	/*
	 * Getter per la descrizione della stanza.
	 */
	public String getDescription() { return description; }
	
	/*
	 * Getter di un oggetto dal nome.
	 * 
	 * @param	nome	nome del oggetto cercato
	 * @return			l'oggetto cercato se è nella stanza se non è presente null.
	 */
	public Oggetto getItem(String nome) { for(Oggetto o : getItems()) if(o.getNome().contains(nome)) return o; return null; }
	
	/*
	 * Getter per gli oggetti della stanza.
	 */
	public List<Oggetto> getItems() { return items; }
	
	/*
	 * Getter per il nome della stanza.
	 */
	public String getName() { return name; }
	
	/*
	 * Getter per le uscite della stanza.
	 */
	public Map<Direzione, Link> getLinks() { return links; }
	
	/*
	 * Getter di un uscita dal nome.
	 * 
	 * @param	nome	nome dell'uscita cercata
	 * @return			l'uscita cercata se è nella stanza se non è presente null.
	 */
	public Link getLink(String nome) { for(Link l : getLinks().values()) if(l.getNome().equals(nome)) return l; return null; }
	
	/*
	 * Getter di un uscita dalla direzione.
	 * 
	 * @param	d	direzione dell'uscita cercata
	 * @return		l'uscita cercata se è nella stanza se non è presente null.
	 */
	public Link getLink(Direzione d) { for(Direzione l : getLinks().keySet()) if(l.equals(d)) return getLinks().get(l); return null; }
	
	/*
	 * Override del metodo toString.
	 */
	@Override
	public String toString() { return name.toString(); }
	
	/*
	 * Metodo che verifica se è presente un personaggio chiave nella storia del gioco dentro la stanza in cui ci troviamo.
	 * 
	 * @return	Se è presente ritorna un venditore o un guardiano se non è presente null.
	 */
	public Character getPersonaggioChiave()
	{
		for(Character c : getCharacters()) 
		{
			var string = c.getClass().toString();
			if(string.equals("class it.uniroma1.textadv.Character$Venditore") || string.equals("class it.uniroma1.textadv.Character$Guardiano")) return c; 
		}
		return null;
	}
}
