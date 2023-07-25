package it.uniroma1.textadv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Classe builder per le stanze in game.
 * 
 * @author Valerio Mesiti
 */
public class StanzaBuilder {

	private String nome;
	private String descrizione;
	private List<Oggetto> items = new ArrayList<>();
	private List<Character> characters = new ArrayList<>();
	private Map<Direzione, Link> links = new HashMap<>();
	
	/*
	 * Costruttore privato.
	 * 
	 * @param	nome	il nome della stanza
	 */
	private StanzaBuilder(String nome) { this.nome = nome; }
	
	/*
	 * Punto di accesso al costruttore.
	 * 
	 * @param	nome	il nome della stanza
	 */
	public static StanzaBuilder newBuilder(String nome) { return new StanzaBuilder(nome); }
	
	/*
	 * Setter per il nome.
	 * 
	 * @param	nome	il nome della stanza
	 */
	public StanzaBuilder nome(String nome) { this.nome = nome; return this; }
	
	/*
	 * Setter per la descrizione.
	 * 
	 * @param	descrizione	la descrizione della stanza.
	 */
	public StanzaBuilder descrizione(String descrizione) { this.descrizione = descrizione; return this; }
	
	/*
	 * Setter per aggiungere oggetti nella stanza.
	 * 
	 * @param item l'oggetto da aggiungere.
	 */
	public StanzaBuilder addItem(Oggetto item) { this.items.add(item); return this; }
	
	/*
	 * Setter per aggiungere personaggi nella stanza.
	 * 
	 * @param	character	il personaggio da aggiungere.
	 */
	public StanzaBuilder addCharacter(Character character) { this.characters.add(character); return this; }

	/*
	 * Setter per aggiungere uscite dalla stanza.
	 * 
	 * @param	d		la direzione in cui si trova l'uscita.
	 * @param	link	l'uscita da aggiungere.
	 */
	public StanzaBuilder addLink(Direzione d, Link link) { this.links.put(d, link); return this; }
	
	/*
	 * Metodo build che costruisce la stanza.
	 * 
	 * @return la stanza con i parametri inseriti.
	 */
	public Stanza build() { return new Stanza(nome, descrizione, items, characters, links); }
}
