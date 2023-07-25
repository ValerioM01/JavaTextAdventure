package it.uniroma1.textadv;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/*
 * Classe che identifica il mondo di gioco: 
 * Un insieme di stanze dotato di un nome, una descrizione testuale, un pointer per ricordare in che punto siamo della mappa
 * e degli elenchi che memorizzano gli oggetti, i link e i personaggi presenti nel gioco.
 */
public class Mondo {
	
	private Mondo() {}

	private Giocatore player;
	private String name;
	private String description;
	private Stanza posizione;
	private List<Stanza> map = new ArrayList<>();
	private List<Oggetto> items = new ArrayList<>();
	private List<Link> links = new ArrayList<>();
	private List<Character> characters = new ArrayList<>();
	
	/*
	 * Metodo che carica e istanzia il mondo di gioco dal file specificato in input.
	 * 
	 * @param	fileName	il path del file contenente il mondo di gioco.
	 * @return				un mondo di gioco completo
	 */
	private static Mondo fromFile(Path fileName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException 
	{
		Mondo m = new Mondo();
		String titolo = "", nome = "";
		String[] array = Files.readString(fileName).replaceAll("\\]", "").substring(1).split("\\[");
		Arrays.sort(array, Comparator.naturalOrder());
		String start = m.creazioneMondo(array[array.length-1]);
		array = Arrays.copyOfRange(array, 0, array.length-1);
		
		for(String s : array)
		{
			String[] blocco = s.split("\r\n");
			if(blocco[0].contains(":")) { String[] etichetta = blocco[0].split(":"); titolo = etichetta[0]; nome = etichetta[1]; }
			else titolo = blocco[0];
			String[] settings = Arrays.copyOfRange(blocco, 1, blocco.length);
			
			switch(titolo) 
			{
				case "characters": m.creazionePersonaggi(settings); break;
				case "links": m.creazioneLinks(settings); break;
				case "objects": m.creazioneOggetti(settings); break;
				case "player": m.setPlayer(Giocatore.getInstance(settings[0].split("\t")[0])); break;
				case "room": m.addRoom(m.creazioneStanza(StanzaBuilder.newBuilder(nome), settings)); break;
			}
		}
		m.setPosizione(m.getStanza(start));
		return m;
	}
	
	/*
	 * Metodo che prendendo la stringa del path contenente il mondo chiama il metodo che lo crea.
	 * 
	 * @param	fileName	la stringa del path del file contenente il mondo di gioco.
	 */
	public static Mondo fromFile(String fileName) throws IOException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException { return fromFile(Path.of(fileName)); }
	
	/*
	 * Metodo che gestisce la prima parte di file, quella dell'etichetta [world]
	 * aggiunge al mondo che stiamo creando il nome e la descrizione e la stanza di partenza.
	 * 
	 * @param	string	il blocco [world]
	 * @return			la stanza di partenza
	 */
	private String creazioneMondo(String string) 
	{
		String start = "";
		String[] blocco = string.split("\r\n");
		String[] settings = Arrays.copyOfRange(blocco, 1, blocco.length);
		setName(blocco[0].split(":")[1]);
		
		for (String world : settings)
		{
			if(world.startsWith("description")) setDescription(world.replace("description\t", ""));
			else start = world.replace("start\t", "");
		}
		return start;
	}
	
	/*
	 * Metodo che gestisce la parte di file dell'etichetta [room]
	 * aggiunge al mondo che stiamo creando tutte le stanze che contiene la mappa di gioco,
	 * utilizzando il builder delle stanze riesce cosi ad aggiungere man mano che scorre il blocco,
	 * nome, descrizione, oggetti, link e personaggi presenti in quella stanza.
	 * 
	 * @param	stanza	un builder della stanza
	 * @param	setting	il blocco [room]
	 * @return			la stanza costruita
	 */
	private Stanza creazioneStanza(StanzaBuilder stanza, String[] settings)
	{
		String tag = "";
		String[] oggetti;

		for (String room : settings)
		{	
			if(!room.contains("\t")) continue;
			else
			{
				tag = room.split("\t")[0];
				oggetti = room.split("\t")[1].split(",");
			}
			
			switch(tag)
			{
				case "description": 
					stanza.descrizione(oggetti[0]); 
					break;
					
				case "objects": 
					for (String oggetto : oggetti) stanza.addItem(getItem(oggetto.strip()));
					break;
					
				case "links":
					for (String link : oggetti) 
					{
						String[] g = link.split(":");
						Link tp = getLink(g[1]);
						Direzione direzione = Direzione.valueOf(g[0].toLowerCase());
						
						if(tp != null) stanza.addLink(direzione, tp);
						else stanza.addLink(direzione, new Link(g[1], g[1], false));
					}
					break;
					
				case "characters": 
					for (String personaggio : oggetti) stanza.addCharacter(getCharacter(personaggio.strip()));
					break;
			}
		}
		return stanza.build();
	}
	
	/*
	 * Metodo che gestisce la parte di file dell'etichetta [characters]
	 * aggiunge al mondo che stiamo creando tutti i personaggi che contiene la mappa di gioco,
	 * usando la reflection e chiamando il costruttore segnalato nel blocco in input riesce cosi
	 * a dare: nome e inventario a tutti i personaggi da costruire.
	 * 
	 * @param	setting	il blocco [characters]
	 */
	private void creazionePersonaggi(String[] settings) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		for (String personaggio : settings)
		{
			String[] arraycharacters = formatta(personaggio);
			Class<?> c = Class.forName("it.uniroma1.textadv.Character$"+arraycharacters[1]);
			
			if(arraycharacters.length<3) characters.add((Character) c.getConstructor(String.class).newInstance(arraycharacters[0]));
			else characters.add((Character) c.getConstructor(String.class, String[].class).newInstance(arraycharacters[0], Arrays.copyOfRange(arraycharacters, 2, arraycharacters.length)));
		}
	}
	
	/*
	 * Metodo che gestisce la parte di file dell'etichetta [objects]
	 * aggiunge al mondo che stiamo creando tutti gli oggetti che contiene la mappa di gioco,
	 * usando la reflection e chiamando il costruttore segnalato nel blocco in input riesce cosi
	 * a dare: nome e, se è un utensile, oggetto su cui va usato a tutti gli oggetti da costruire.
	 * 
	 * @param	setting	il blocco [objects]
	 */
	private void creazioneOggetti(String[] settings) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		for (String oggetto : settings)
		{
			if(oggetto.contains("\t// ")) oggetto = oggetto.split("\\t// ")[0];
			String[] arrayitems = formatta(oggetto);
			Class<?> c = Class.forName("it.uniroma1.textadv.Oggetto$"+arrayitems[1]);
			
			if(arrayitems.length == 2) items.add((Oggetto) c.getConstructor(String.class).newInstance(arrayitems[0]));
			else 
			{
				items.add((Oggetto) c.getConstructor(String.class, String.class).newInstance(arrayitems[0], arrayitems[2]));
				try { getLink(arrayitems[2]).setUtil(arrayitems[0]); }
				catch(NullPointerException e) { }
			}
		}
	}
	
	/*
	 * Metodo che gestisce la parte di file dell'etichetta [links]
	 * aggiunge al mondo che stiamo creando tutti i link che contiene la mappa di gioco,
	 * usando la reflection e chiamando il costruttore segnalato nel blocco in input riesce cosi
	 * a dare: nome e dove conduce a tutti i link da costruire.
	 * 
	 * @param	setting	il blocco [links]
	 */
	private void creazioneLinks(String[] settings) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		for (String link : settings)
		{
			String[] arraylinks = formatta(link);
			Class<?> c = Class.forName("it.uniroma1.textadv.Link$"+arraylinks[1]);
			links.add((Link) c.getConstructor(String.class, String.class).newInstance(arraylinks[0], arraylinks[3]));
		}
	}
	
	/*
	 * Metodo che consente di rimuovere commenti dal file in input (es secchio	Secchio	//	inizialmente vuoto)
	 */
	private String[] formatta(String s) { if(s.contains(" // ")) s = s.split(" // ")[0]; return s.split("\t"); }
	
	//Getter e setter
	public List<Character> getCharacters() { return characters; }
	
	public List<Oggetto> getItems() { return items; }

	public List<Link> getLinks() { return links; }
	
	public String getDescription() { return description; }
	
	public List<Stanza> getMap() { return map; }
	
	public String getName() { return name; }
	
	public Giocatore getPlayer() { return player; }
	
	public Stanza getPosizione() { return posizione; }
	
	public Oggetto getItem(String nome) { for(Oggetto o : getItems()) if(o.getNome().equals(nome)) return o; return null; }
	
	public Character getCharacter(String nome) { for(Character c : getCharacters()) if(c.getNome().equals(nome)) return c; return null; }
	
	public Link getLink(String nome) { for(Link l : getLinks()) if(l.getNome().equals(nome)) return l; return null; }
	
	public Stanza getStanza(String nome) { for(Stanza s : getMap()) if(s.getName().equals(nome)) return s; return null; }
	
	public Stanza getObjLocation(String obj) { for(Stanza s : getMap()) if(s.getItem(obj) != null) return s; return null; }
	
	public Stanza getCharacterLocation(String character) { for(Stanza s : getMap()) if(s.getCharacter(character) != null) return s; return null; }
	
	public void addRoom(Stanza s) { map.add(s); }
	
	public void setPosizione(Stanza posizione) { this.posizione = posizione; }
	
	public void setDescription(String description) { this.description = description; }
	
	public void setMap(ArrayList<Stanza> map) { this.map = map; }
	
	public void setName(String name) { this.name = name; }
	
	public void setPlayer(Giocatore player) { this.player = player; }
}
