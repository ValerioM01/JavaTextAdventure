package it.uniroma1.textadv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/*
 * Classe astratta che identifica un personaggio, dotato di un nome, un inventario di oggetti e di una serie di metodi 
 * che gli permettono di interagire con la stanza in cui si trova e con il giocatore.
 * 
 * @author Valerio Mesiti
 */
public abstract class Character {

	private String nome;
	private List<String> util = new ArrayList<String>();
	private boolean tranquillo = true;
	
	/*
	 * Costruttore 1.
	 * 
	 * @param	nome	nome del personaggio
	 * @param	util	inventario del personaggio
	 */
	public Character(String nome, String... util) { this(nome, true, util); }
	
	/*
	 * Costruttore 2.
	 * 
	 * @param	nome		nome del personaggio
	 * @param	tranquillo	stato del personaggio
	 * @param	util		inventario del personaggio
	 */
	public Character(String nome, boolean tranquillo, String... util) { this.nome = nome; this.tranquillo = tranquillo; this.util = Arrays.asList(util); }
	
	/*
	 * Metodo che permette ai personaggi di comunicare con il giocatore.
	 * 
	 * @param	m	Mondo di gioco
	 */
	public abstract String parla(Mondo m);
	
	/*
	 * Getter dell'inventario di un personaggio.
	 */
	public List<String> getInventory() { return util; }
	
	/*
	 * Getter del nome di un personaggio
	 */
	public String getNome() { return nome; }
	
	/*
	 * Getter dello stato di un personaggio
	 */
	public boolean isTranquillo() { return tranquillo; }
	
	/*
	 * Setter dello stato di un personaggio
	 * 
	 * @param	tranquillo	parametro booleano che rappresenta lo stato del personaggio.
	 */
	public void setTranquillo(boolean tranquillo) { this.tranquillo = tranquillo; }
	
	/*
	 * Metodo che permette al personaggio di dare gli oggetti contenuti nell'suo inventario al giocatore.
	 * 
	 * @param	m	Mondo di gioco
	 */
	public void prendi(Mondo m)
	{
		for(String o : getInventory())
		{
			Inventario item = (Inventario) m.getPosizione().getItem(o);
			m.getPlayer().addInventory(item);
			item.setVisibile(false);
			System.out.println("oggetto "+item.getNome()+" ottenuto!");
		}
	}
	
	/*
	 * Override del metodo toString.
	 */
	@Override
	public String toString() { return getNome(); }
	
	/*
	 * Classe annidata che identifica il tipo di personaggio "Amico".
	 * Personaggio che regala gli oggetti presenti nel suo inventario al giocatore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Amico extends Character
	{
		/*
		 * Guarda {@link #Character(String, String...)}
		 */
		public Amico(String nome, String... prodotti) { super(nome, true, prodotti); }

		/*
		 * Guarda {@link #parla(Mondo)}
		 */
		@Override
		public String parla(Mondo m) 
		{
			System.out.println("Ciao amico voglio farti un regalo, guarda che roba!");
			prendi(m);
			return "Alla prossima fratello";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di personaggio "Nemico".
	 * Personaggio che va combattuto e nel caso vengano esaudite delle richieste droppa l'inventario al giocatore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Nemico extends Character
	{
		/*
		 * Guarda {@link #Character(String, String...)}
		 */
		public Nemico(String nome, String... prodotti) { super(nome, true, prodotti); }

		/*
		 * Guarda {@link #parla(Mondo)}
		 */
		@Override
		public String parla(Mondo m) {
			try
			{
				if(!m.getPlayer().getItem("spada").equals(null));
				{
					System.out.println("Allora combattiamo...");
					System.out.println("...");
					System.out.println("...");
					System.out.println("...");
					prendi(m);
					return "Sei forte ragazzo";
				}
			}
			catch(NullPointerException e) { return "Cerchi guai?!?! Vattene, non hai neanche una spada per combattere!"; }
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di personaggio generico.
	 * Personaggio puo solo parlare.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Personaggio extends Character
	{
		/*
		 * Guarda {@link #Character(String, String...)}
		 */
		public Personaggio(String nome) { super(nome); }

		/*
		 * Guarda {@link #parla(Mondo)}
		 */
		@Override
		public String parla(Mondo m) {
			Random rand = new Random();
			switch(rand.nextInt(4))
			{
				case 0 -> { return "Buona fortuna"; }
				case 1 -> { return "Buona giornata, spero ti stia divertendo"; }
				case 2 -> { return "Esplora ogni angolo della citta!"; }
				default -> { return "ciao"; }
			}
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di personaggio "Venditore".
	 * Personaggio che nel caso venga pagato con dei soldi droppa l'inventario al giocatore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Venditore extends Character implements Persona
	{
		/*
		 * Guarda {@link #Character(String, String...)}
		 */
		public Venditore(String nome, String... prodotti) { super(nome, false, prodotti); }

		/*
		 * Override del metodo dell'interfaccia funzionale persona.
		 * {@link it.uniroma1.textadv.Persona}
		 * Se vengono dati dei soldi al venditore si viene serviti con gli oggetti presenti nel suo inventario
		 * 
		 * @param	s	Oggetto dato al venditore
		 * @param	m	Mondo di gioco
		 */
		@Override
		public void dai(Inventario s, Mondo m) 
		{
			if(s.getNome().equals("soldi")) 
			{
				prendi(m);
				setTranquillo(true); 
			}
		}

		/*
		 * Guarda {@link #parla(Mondo)}
		 */
		@Override
		public String parla(Mondo m) { return (isTranquillo()) ? "grazie per esserti servito!" : "dammi del denaro e in cambio potrai servirti nel mio negozietto!"; }
		
		/*
		 * Override del metodo toString.
		 */
		@Override
		public String toString() { return getNome()+" il venditore"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di personaggio "Guardiano".
	 * Personaggio che nel caso venga soddisfatto con una richiesta sblocca il tesoro al giocatore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Guardiano extends Character implements Persona
	{	
		/*
		 * Guarda {@link #Character(String, String...)}
		 */
		public Guardiano(String nome, String... util) { super(nome, false, util); }

		/*
		 * Override del metodo dell'interfaccia funzionale persona.
		 * {@link it.uniroma1.textadv.Persona}
		 * Se viene dato l'oggetto richiesto al guardiano si sblocca il tesoro che protegge.
		 * 
		 * @param	chiave	Oggetto dato al guardiano
		 * @param	m		Mondo di gioco
		 */
		public void dai(Inventario chiave, Mondo m)
		{ 
			if(getInventory().contains(chiave.getNome())) 
			{
				setTranquillo(true);
				System.out.println("Grazie!"); 
			}
		}
		
		/*
		 * Guarda {@link #parla(Mondo)}
		 */
		@Override
		public String parla(Mondo m) { return (isTranquillo()) ? "mmm..." : "non ti permettere di prendere il tesoro!"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di personaggio "Cane".
	 * Personaggio che può accompagnare il giocatore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Cane extends Character implements Inventario
	{
		boolean visibile = true;
		/*
		 * Guarda {@link #Character(String, String...)}
		 */
		public Cane(String nome) { super(nome); }
		
		/*
		 * Override del metodo toString.
		 */
		@Override
		public String toString() { return getNome()+" il cane"; }

		/*
		 * Guarda {@link #parla(Mondo)}
		 */
		@Override
		public String parla(Mondo m) { return "woof woof"; }

		/*
		 * Guarda {@link #it.uniroma1.textadv.Inventario.setVisibile(boolean)}
		 */
		@Override
		public void setVisibile(boolean b) { visibile = b; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di personaggio "Gatto".
	 * Personaggio che può accompagnare il giocatore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Gatto extends Character implements Inventario
	{
		boolean visibile = true;
		
		/*
		 * Guarda {@link #Character(String, String...)}
		 */
		public Gatto(String nome) { super(nome); }

		/*
		 * Override del metodo toString.
		 */
		@Override
		public String toString() { return getNome()+" il gatto"; }
		
		/*
		 * Guarda {@link #parla(Mondo)}
		 */
		@Override
		public String parla(Mondo m) { return "frrrr"; }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Inventario.setVisibile(boolean)}
		 */
		@Override
		public void setVisibile(boolean b) { visibile = b; }
	}
}
