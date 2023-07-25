package it.uniroma1.textadv;

/*
 * Classe che identifica un link (passaggio), l'elemento usato per spostarsi da una stanza ad un altra.
 * Dotato di un nome, una destinazione, un campo per determinare se chiuso o no e un campo per determinare l'oggetto utile ad aprirlo.
 * 
 * @author Valerio Mesiti
 */
public class Link {

	private String nome;
	private String destinazione;
	protected boolean chiusa;
	private String util = "";
	
	/*
	 * Costruttore.
	 * 
	 * @param	nome			nome del link
	 * @param	destinazione	dove porta il link
	 * @param	chiusa			stato del link (aperto/chiuso)
	 */
	public Link(String nome, String destinazione, boolean chiusa) 
	{
		this.nome = nome;
		this.destinazione = destinazione;
		this.chiusa = chiusa;
	}
	
	/*
	 * Getter della destinazione
	 */
	public String getDestinazione() { return destinazione; }
	
	/*
	 * Setter della destinazione
	 */
	public void setDestinazione(String destinazione) { this.destinazione = destinazione; }
	
	/*
	 * Getter del nome
	 */
	public String getNome() { return nome; }
	
	/*
	 * Metodo che permette l'apertura di un link
	 */
	protected String apri() { chiusa = false; return "Aperta!"; }
	
	/*
	 * Getter dello stato del link
	 */
	public boolean getChiusa() { return chiusa; }
	
	/*
	 * Setter dello stato del link
	 */
	public void setChiusa(boolean chiusa) { this.chiusa = chiusa; }
	
	/*
	 * metodo che permette di avere una descrizione del link guardandolo
	 */
	public String guarda() { return "Da questa parte "+destinazione+" !"; }
	
	/*
	 * Override del metodo toString
	 */
	@Override
	public String toString() { return getNome(); }
	
	/*
	 * Getter dell'oggetto utile ad aprire il link
	 */
	public String getUtil() { return util; }
	
	/*
	 * Setter dell'oggetto utile ad aprire il link
	 */
	public void setUtil(String util) { this.util = util; }
	
	/*
	 * Classe annidata che identifica il tipo di link "Botola".
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Botola extends Link {
		
		public Botola(String name, String d) { super(name, d, true); }
		
		@Override
		public String guarda() { return (chiusa) ? "Una botola chiusa" : "Una botola aperta"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di link "Porta".
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Porta extends Link {
		
		public Porta(String name, String d) { super(name, d, true); }
		
		@Override
		public String guarda() { return (chiusa) ? "Una porta chiusa" : "Una porta aperta"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di link "Bus".
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Bus extends Link {

		public Bus(String name, String d) { super(name, d, false); }
		
		@Override
		public String guarda() { return "una navetta!"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di link "Treno".
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Treno extends Link {

		public Treno(String name, String d) { super(name, d, true); }
		
		@Override
		public String guarda() { return (chiusa) ? "Non puoi partire senza biglietto!" : "un treno!"; }
	}
	
	/*
	 * Classe annidata che identifica il tipo di link "Teletrasporto".
	 * 
	 * @author Valerio Mesiti
	 */
	public static class Teletrasporto extends Link {

		public Teletrasporto(String name, String d) { super(name, d, false); }
		
		@Override
		public String guarda() { return "un teletrasporto, chissa dove porta?"; }
	}
}
