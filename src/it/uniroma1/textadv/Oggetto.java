package it.uniroma1.textadv;

/*
 * Classe astratta che identifica un oggetto, dotato di un nome, e di una serie di metodi 
 * che permettono al giocatore di interagire con esso.
 * 
 * @author Valerio Mesiti
 */
public abstract class Oggetto {

	private String nome;
	private boolean visibile = true;
	
	/*
	 * Costruttore
	 * 
	 * @param	nome	nome dell'oggetto
	 */
	public Oggetto(String nome) { this.nome = nome; }
	
	/*
	 * Getter del nome
	 */
	public String getNome() { return nome; }
	
	/*
	 * metodo che permette di avere una descrizione di un oggetto guardandolo
	 */
	public abstract String guarda();
	
	/*
	 * Getter dello stato dell'oggetto
	 */
	public boolean isVisibile() { return visibile; }
	
	/*
	 * Setter dello stato dell'oggetto
	 */
	public void setVisibile(boolean visibile) { this.visibile = visibile; }
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Martello".
	 * Oggetto che può rompere un salvadanaio.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Martello extends Oggetto implements Inventario, Utensile
	{
		/*
		 * Guarda {@link Oggetto(String)}
		 */
		public Martello(String nome) { super(nome); }
	
		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "Un martello"; }

		/*
		 * Guarda {@link #it.uniroma1.textadv.Utensile}
		 */
		@Override
		public String usa(Oggetto oggetto, Mondo m) { return ((Salvadanaio) oggetto).apri(m); }
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Cacciavite".
	 * Oggetto che può aprire una vite.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Cacciavite extends Oggetto implements Inventario, Utensile
	{ 
		/*
		 * Guarda {@link Oggetto(String)}
		 */
		public Cacciavite(String nome) { super(nome); }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "Un cacciavite"; }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Utensile}
		 */
		@Override
		public String usa(Oggetto oggetto, Mondo m) { return ((Vite) oggetto).apri(m, getNome()); }
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Tronchesi".
	 * Oggetto che può aprire un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Tronchesi extends Oggetto implements Inventario, Utensile
	{
		private String apre;
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	apre	nome dell'oggetto che apre
		 */
		public Tronchesi(String nome, String apre) { super(nome); this.apre = apre; }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "Delle tronchesi"; }

		/*
		 * Guarda {@link #it.uniroma1.textadv.Utensile}
		 */
		@Override
		public String usa(Oggetto oggetto, Mondo m) 
		{
			if(oggetto.getNome().equals(apre)) return ((Armadio) oggetto).apri(m);
			else return "Non apre questo oggetto!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Secchio".
	 * Oggetto che può essere riempito in un pozzo d'acqua e puo essere usato per spegnere un fuoco.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Secchio extends Oggetto implements Inventario, Utensile
	{
		private boolean pieno = false;
		
		/*
		 * Guarda {@link Oggetto(String)}
		 */
		public Secchio(String nome) { super(nome); }
		
		/*
		 * Metodo che riempie il secchio
		 */
		public String riempi() { pieno = true; return "riempito"; }
		
		/*
		 * Metodo che svuota il secchio
		 */
		public void svuota() { pieno = false; }

		/*
		 * Getter dello stato del secchio
		 */
		public boolean isPieno() { return pieno; }
		
		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return pieno ? "Un secchio pieno d'acqua" : "Un secchio vuoto"; }

		/*
		 * Guarda {@link #it.uniroma1.textadv.Utensile}
		 */
		@Override
		public String usa(Oggetto oggetto, Mondo m) 
		{
			if(oggetto.getNome().equals("camino") && isPieno())
			{
				svuota();
				return ((Camino) oggetto).apri(m);
			}
			else return riempi();
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Chiave".
	 * Oggetto che può essere usato per aprire un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Chiave extends Oggetto implements Inventario, Opener
	{
		private String apre;
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	apre	nome dell'oggetto che apre
		 */
		public Chiave(String nome, String apre) { super(nome); this.apre = apre; setVisibile(true); }
		
		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return getNome()+" apre "+apre; }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Opener}
		 */
		@Override
		public String apri(Link l) 
		{
			if(l.getNome().equals(apre)) return l.apri();
			else return "Non è l'oggetto giusto per aprire la porta!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Scrivania".
	 * Oggetto che contiene un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Scrivania extends Oggetto implements Container
	{
		private String contiene;
		private boolean chiusa = true;
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	contiene	nome dell'oggetto che contiene
		 */
		public Scrivania(String nome, String contiene) { super(nome); this.contiene = contiene; }
		
		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return chiusa ? "è una scrivania chiusa" : "nel cassetto c'è un oggetto "+contiene; }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Container}
		 */
		@Override
		public String apri(Mondo m) 
		{ 
			chiusa = false;
			m.getPosizione().addItem(m.getItem(contiene)); 
			return "Aperta!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Scrivania".
	 * Oggetto che contiene un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Armadio extends Oggetto implements Container
	{
		private String contiene;
		private boolean chiuso = true;
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	contiene	nome dell'oggetto che contiene
		 */
		public Armadio(String nome, String contiene) { super(nome); this.contiene = contiene; }
		
		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return chiuso ? "è un armadio chiuso con un lucchetto!" : "Nell'armadio c'è un oggetto "+contiene; }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Container}
		 */
		@Override
		public String apri(Mondo m) 
		{
			chiuso = false; 
			m.getPosizione().addItem(m.getItem(contiene));
			return "Aperto!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Cassetto".
	 * Oggetto che contiene un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Cassetto extends Oggetto implements Container
	{
		private String contiene;
		private boolean chiuso = true;
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	contiene	nome dell'oggetto che contiene
		 */
		public Cassetto(String nome, String contiene) { super(nome); this.contiene = contiene; }
		
		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return chiuso ? "Un cassetto chiuso" : "Nel cassetto c'è un oggetto: "+contiene; }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Container}
		 */
		@Override
		public String apri(Mondo m) 
		{ 
			chiuso = false; 
			m.getPosizione().addItem(m.getItem(contiene)); 
			return "Aperto!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Salvadanaio".
	 * Oggetto che contiene un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Salvadanaio extends Oggetto implements Container
	{
		private String contiene;
		private boolean rotto = false;
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	contiene	nome dell'oggetto che contiene
		 */
		public Salvadanaio(String nome, String contiene) { super(nome); this.contiene = contiene; }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return rotto ? "Un salvadanaio rotto con "+contiene+" dentro" : "Un salvadanaio"; }

		/*
		 * Guarda {@link #it.uniroma1.textadv.Container}
		 */
		@Override
		public String apri(Mondo m) 
		{
			rotto = true; 
			m.getPosizione().addItem(m.getItem(contiene));
			return "Rotto!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Camino".
	 * Oggetto che contiene un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Camino extends Oggetto implements Container
	{
		private String contiene;
		private boolean acceso = true;
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	contiene	nome dell'oggetto che contiene
		 */
		public Camino(String nome, String contiene) { super(nome); this.contiene = contiene; }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return acceso ? "è un camino acceso" : "Dentro il camino spento c'è un oggetto: "+contiene; }

		/*
		 * Guarda {@link #it.uniroma1.textadv.Container}
		 */
		@Override
		public String apri(Mondo m) 
		{
			acceso = false;
			m.getPosizione().addItem(m.getItem(contiene));
			return "Spento!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Vite".
	 * Oggetto che chiude un oggetto dato nel costruttore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Vite extends Oggetto implements Container
	{
		private String chiude;
		private String item = "cacciavite";
		
		/*
		 * Costruttore
		 * 
		 * @param	nome	nome dell'oggetto
		 * @param	chiude	nome dell'oggetto che chiude
		 */
		public Vite(String nome, String chiude) { super(nome); this.chiude = chiude; }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "Questa vite blocca la botola!"; }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Container}
		 */
		@Override
		public String apri(Mondo m) { return apri(m, ""); }
		
		/*
		 * Guarda {@link #it.uniroma1.textadv.Container}
		 */
		public String apri(Mondo m, String nome) 
		{
			if (nome.equals(item))
			{
				setVisibile(false);
				m.getPosizione().getLink(chiude).setUtil("");
				return "Svitata!";
			}
			return "comando non valido!";
		}
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Soldi".
	 * Oggetto che può essere tenuto nell'inventario e usato per comprare oggetti dal venditore.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Soldi extends Oggetto implements Inventario
	{
		/*
		 * Guarda {@link Oggetto(String)}
		 */
		public Soldi(String nome) { super(nome); }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "Soldi con cui comprare al negozietto"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Spada".
	 * Oggetto che può essere tenuto nell'inventario e usato per combattere un nemico.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Spada extends Oggetto implements Inventario
	{
		/*
		 * Guarda {@link Oggetto(String)}
		 */
		public Spada(String nome) { super(nome); }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "Spada con inciso un nome... Sora"; }
	}

	/*
	 * Classe annidata che identifica il tipo di oggetto "Spada".
	 * Oggetto che permette di vincere il gioco.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Tesoro extends Oggetto implements Inventario
	{ 
		/*
		 * Guarda {@link Oggetto(String)}
		 */
		public Tesoro(String nome) { super(nome); }
	
		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "aaaaaaaa coppa daaa maggica"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di oggetto "Pozzo".
	 * Oggetto che permette di riempire il secchio.
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Pozzo extends Oggetto 
	{ 
		/*
		 * Guarda {@link Oggetto(String)}
		 */
		public Pozzo(String nome) { super(nome); }

		/*
		 * Guarda {@link #Oggetto.guarda()}
		 */
		@Override
		public String guarda() { return "Un pozzo pieno d'acqua;"; }
	}
}
