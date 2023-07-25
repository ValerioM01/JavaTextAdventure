package it.uniroma1.textadv;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import static java.util.stream.Collectors.*;

/*
 * Classe che rappresenta il motore del gioco, contiene tutto ciò che è utile per farlo funzionare.
 * Troviamo i metodi per giocare sia alla modalita di base che al FF (fast forward),
 * il motore testuale, ossia i vari metodi per interpretare ciò che inserisce il giocatore e un piccolo ester egg.
 */
public class Gioco {
	
	private final static List<String> COMANDI = new ArrayList<>(Arrays.asList(/*"tp", "oggetto",*/ "comandi", "entra", "guarda", "prendi", "vai", "apri", "accarezza", "rompi", "usa", "inventario", "dai", "parla"));
	private final static List<String> DIREZIONI = Arrays.asList("n","s","e","o");
	
	private final static Direzione[] KONAMI_CODE = { Direzione.n, Direzione.n, Direzione.s, Direzione.s, Direzione.o, Direzione.e, Direzione.o, Direzione.e };
	private static int currentKonami;
	
	/*
	 * Metodo che fa partire il gioco in modalità normale, dopo aver letto il mondo dà un breve benvenuto
	 * e fa iniziare a giocare leggendo gli input e dando un output a schermo.
	 * Il metodo continuerà finche non si vince o si esce premendo il tasto q.
	 * 
	 * @param	w	mondo dove si vuole giocare.
	 */
	public void play(Mondo w) throws IOException 
	{
        System.out.println(w.getName());
        System.out.println(w.getDescription());
        System.out.println("Il tuo viaggio iniziera tra poco, ti trovi nella zona: "+w.getPosizione());
		System.out.println("Cosa vuoi fare adesso?");
		System.out.println("Per uscire dal gioco, premi Q");
		System.out.println("Per la lista dei comandi, scrivi comandi");
		
		String input;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        do {
            System.out.print("Inserisci un comando > ");
            input = in.readLine().trim().toLowerCase();
            String output = leggiComando(input, w);
            System.out.println(output);
        } while (!"q".equals(input));
	}
	
	/*
	 * Metodo che fa partire il gioco in modalità ff (fast forward), dopo aver letto il mondo dà un breve benvenuto
	 * e fa iniziare a giocare leggendo gli input dati da un file e dando un output a schermo.
	 * Il metodo continuerà finche non si vince.
	 * 
	 * @param	w	mondo dove si vuole giocare.
	 * @param	p	il file dove sono presenti i comandi per vincere.
	 */
	public void play(Mondo w, Path p) throws FileNotFoundException, IOException 
	{
		System.out.println(w.getName());
        System.out.println(w.getDescription());
        System.out.println("Il tuo viaggio iniziera tra poco, ti trovi nella zona: "+w.getPosizione());
		System.out.println("Cosa vuoi fare adesso?");
		System.out.println("Per uscire dal gioco, premi Q");
		System.out.println("Per la lista dei comandi, scrivi comandi");
		
		try(BufferedReader br  = Files.newBufferedReader(p))
		{
			while(br.ready())
			{
				String riga = br.readLine();
				if(riga.contains(" // ")) riga = riga.split(" // ")[0];
				System.out.println(riga);
				leggiComando(riga.toLowerCase(), w);
			}
		}
	}
	
	/*
	 * Metodo che permette la lettura di un comando:
	 * se viene inserita la "q" il metodo non reagisce cosi da permettere l'uscita dal gioco,
	 * se non viene inserito nulla viene ritornato un messaggio a schermo invece,
	 * se viene inserito un comando considerato valido da questo metodo lo si manda
	 * al parseCommand che gestisce i funzionamenti dei comandi.
	 * 
	 * @param	input	il comando inserito
	 * @param	w		il mondo di gioco
	 * @return			una stringa vuota per un comando valido, un errore se l'input è vuoto
	 */
	private static String leggiComando(String input, Mondo w) 
	{
        String s = "";
        
        if (!input.equals("q")) 
        {
            if (input.equals("")) s = "Devi inserire un comando";
            else 
            {
            	List<String> wl = tokenizzatore(input);
                //wl.forEach((token) -> System.out.println(token));
                parseCommand(wl, w);
            }
        }
        return s;
    }
	
	/*
	 * Metodo che permette la tokenizzazione in parole chiave di un comando:
	 * la frase inserita in input viene spogliata di tutti gli articoli e le preposizioni
	 * per poi suddividerla in token e restituire un array di parole chiave che utilizzerà
	 * il parseCommand.
	 * 
	 * @param	input	il comando inserito
	 * @return			un array di stringhe chiave
	 */
	private static List<String> tokenizzatore(String input) 
	{
        String separatori = " \t,.:;?!\"'";
        List<String> articoli = Arrays.asList("il","lo","la","i","gli","le");
        List<String> preposizioni = Arrays.asList("a","da","in","con","per","tra","fra",
        										  "dello","della","dei","degli","delle",
        										  "al","allo","alla","ai","agli","alle",
        										  "dal","dallo","dalla","dagli","dalle",
        										  "nel","nello","nella","nei","negli","nelle",
        										  "col","coi","sul","sullo","sulla","sui","sugli","sulle");
        List<String> strlist = new ArrayList<>();
        StringTokenizer tokenizer = new StringTokenizer(input, separatori);
        
        while (tokenizer.hasMoreTokens()) 
        	{
        		String token = tokenizer.nextToken();
        		if(!articoli.contains(token) && !preposizioni.contains(token)) strlist.add(token);
        	}
        return strlist;
    }
	
	/*
	 * Metodo che rappresenta il motore testuale
	 * Prende in input la lista di parole chiave e le gestisce cosi da fornirci un output a schermo,
	 * la prima parola della lista è il comando e viene confrontato con la lista dei comandi funzionanti
	 * cosi da indirizzarlo sullo switch giusto, da li ogni caso ha il suo mini-funzionamento gestito da
	 * metodi seguenti. 
	 * Restituisce a schermo "Comando non valido" se: 
	 * non trova il verbo giusto,
	 * si inserisce una direzione non consentita,
	 * si interagisce con qualcosa che non è nella stanza dove siamo
	 * ecc.
	 * 
	 * @param	wordList	la lista delle parole chiave date dal tokenizzatore
	 * @param	m			il mondo di gioco
	 */
	private static void parseCommand(List<String> wordList, Mondo m) 
	{
		Giocatore player = m.getPlayer();
		Stanza posizione = m.getPosizione();
        String verb = wordList.get(0);
        List<String> oggetti = posizione.getItems().stream().filter(Oggetto :: isVisibile).map(Oggetto :: getNome).collect(toList());
        List<String> personaggi = posizione.getCharacters().stream().map(Character :: getNome).map(String :: toLowerCase).collect(toList());
        List<String> links = posizione.getLinks().values().stream().map(Link :: getNome).map(String :: toLowerCase).collect(toList());
        List<String> inventario = player.getInventory().stream().map(Inventario :: getNome).collect(toList());
        String oggetto;
        
        if (!COMANDI.contains(verb)) System.out.println(verb + " non è un comando valido!");
        else if(wordList.size() == 1)
        {
        	switch(verb)
			{
				case "guarda": 
					System.out.println("Sei nella zona: "+posizione.getDescription());
					if(personaggi.size() != 0) System.out.println("Con te in questa zona ci sono: "+ posizione.getCharacters());
					if(oggetti.size() != 0) System.out.println("Ci sono i seguenti oggetti sparsi nella zona: "+ oggetti);
					System.out.println("Ci sono inoltre delle uscite: "+ posizione.getLinks());
					break;
				case "inventario": System.out.println("INVENTARIO: "+inventario); break;
				case "comandi": System.out.println("COMANDI: "+COMANDI); break;
				default: System.out.println("Comando incompleto!");
			}
        }
        else
        {
        	oggetto = wordList.get(1);
        	if(wordList.size() > 2 && (oggetto.equals("porta") || oggetto.equals("armadio") || oggetto.equals("grande"))) oggetto+=" "+wordList.get(2);
        	switch(verb)
			{
				case "guarda": 
					if (oggetti.contains(oggetto)) { System.out.println(posizione.getItem(oggetto).guarda()); }
					else if (links.contains(oggetto)) { System.out.println(posizione.getLink(oggetto).guarda()); }
					else if (personaggi.contains(oggetto)) { System.out.println(posizione.getCharacter(oggetto).getNome()); }
					else System.out.println(oggetto + " non è presente in questa stanza!");					
					break;
					
				case "vai":
					if(DIREZIONI.contains(oggetto)) { System.out.println(caseVai(Direzione.valueOf(oggetto), m)); checkKonami(Direzione.valueOf(oggetto)); }
					else System.out.println(oggetto+" non è una direzione consentita");
					break;
				
				case "entra":
					oggetto = String.join(" ", wordList.subList(1, wordList.size()));
					if (links.contains(oggetto)) System.out.println(caseVai(posizione.getLink(oggetto), m));
					else System.out.println("Comando non valido!");
					break;
					
				case "apri":
					if (links.contains(oggetto)) 
					{
						Link l = posizione.getLink(oggetto);
						if(wordList.size() > 3) 
						{
							var utensile = player.getItem(String.join(" ", wordList.subList(3, wordList.size())));
							if (utensile != null && l.getUtil().equals(utensile.getNome())) System.out.println(((Opener) utensile).apri(l)); 
							else System.out.println("non hai "+wordList.get(3)+" nell'inventario!");
						}
						else if(l.getUtil().equals("")) System.out.println(l.apri());
						else System.out.println(oggetto + " è chiusa!");
						break;
					}
					else System.out.println(caseApri(oggetto, m));
					break;
					
				case "prendi": 
					var personaggioChiave = posizione.getPersonaggioChiave();
					if(personaggioChiave != null && personaggioChiave.getInventory().contains(oggetto))
						if(personaggioChiave.getInventory().contains(oggetto) && !personaggioChiave.isTranquillo()) System.out.println(personaggioChiave.parla(m));
						else casePrendi(oggetto, posizione, player);
					
					else if (links.contains(oggetto)) System.out.println(caseVai(posizione.getLink(oggetto), m));
					else if (personaggi.contains(oggetto)) System.out.println(player.addInventory((Inventario) posizione.getCharacter(oggetto)));
					else System.out.println(casePrendi(oggetto, posizione, player));
					break;
					
				case "accarezza":
				case "parla":
					if(personaggi.contains(oggetto)) System.out.println(posizione.getCharacter(oggetto).parla(m));
					else System.out.println("Comando non valido!");
					break;
					
				case "rompi":
					if (oggetti.contains(oggetto) && wordList.size() > 2) System.out.println(((Utensile) player.getItem(wordList.get(2))).usa(posizione.getItem(oggetto), m));
					else System.out.println("Comando non valido!");
					break;
					
				case "usa":
					if(wordList.size() == 2 && links.contains(oggetto)) System.out.println(caseVai(posizione.getLink(oggetto), m));
					else if (wordList.size() > 2) 
					{
						String linkName = wordList.get(wordList.size()-1);
						if (links.contains(linkName))
						{	
							if (oggetto.equals("chiave")) oggetto += " "+wordList.get(2);
							Link l = posizione.getLink(linkName);
							System.out.println(((Opener) player.getItem(oggetto)).apri(l));
							if(l.getNome().equals("teletrasporto")) System.out.println(caseVai(l, m));
						}
						else System.out.println(((Utensile) player.getItem(oggetto)).usa(posizione.getItem(linkName), m));
					}
					else System.out.println("Comando non valido!");
					break;
					
				case "dai":
					if (inventario.contains(oggetto) && wordList.size() > 2) ((Persona) posizione.getCharacter(String.join(" ", wordList.subList(2, wordList.size())))).dai((Inventario) player.getItem(oggetto), m);
					else System.out.println("Comando non valido!");
					break;
					
				/*
				 * Cheat per la programmazione
				case "oggetto":
					oggetto = String.join(" ", wordlist.subList(1, wordlist.size()));
					player.addInventory(m.getItem(oggetto));
					break;
					
				case "tp":
					oggetto = String.join(" ", wordlist.subList(1, wordlist.size()));
					m.setPosizione(m.getStanza(oggetto));
					System.out.println("la nuova posizone è "+posizione);*/
					
				default: System.out.println("Comando non valido!");
			}
        }
        System.out.println();
    }
	
	/*
	 * Metodo che rappresenta il caso "Vai *direzione*"
	 * controlla che ci sia un link in quella direzione
	 * e se presente chiama il secondo metodo che ti ci fa spostare.
	 * 
	 * @param	direzione	la direzione in cui si vuole andare
	 * @param	m			il mondo di gioco
	 * @return				se non c'è un link nella direzione inserita una frase di errore, sennò il secondo metodo.
	 */
	private static String caseVai(Direzione direzione, Mondo m)
	{
		Link link = m.getPosizione().getLink(direzione);
		if(link == null) return "non c'è niente a "+direzione;
		return caseVai(link, m);
	}
	
	/*
	 * Metodo che rappresenta il caso "Vai *link*"
	 * controlla che il link dato sia aperto, se si ti ci fa spostare
	 * cambiando la posizione del giocatore e ribaltando i setting del link
	 * cosi da trovarlo spechhiato nella stanza dove stiamo andando.
	 * 
	 * @param	link	il link in cui si vuole andare
	 * @param	m		il mondo di gioco
	 * @return			guarda() se chiuso cosi da restituire un messaggio, la nuova posizione nel caso di spostamento.
	 */
	private static String caseVai(Link link, Mondo m)
	{
		if(link.getChiusa()) return link.guarda();
		else
		{
			Stanza destinazione = m.getStanza(link.getDestinazione());
			if(!link.getNome().equals(link.getDestinazione())) link.setDestinazione(m.getPosizione().getName());
			if (destinazione == null) m.setPosizione(m.getStanza(link.getNome()));
			else m.setPosizione(destinazione);
			return "la nuova posizone è "+m.getPosizione();
		}
	}
	
	/*
	 * Metodo che rappresenta il caso "Prendi *oggetto*"
	 * prova a prendere l'oggetto facendo il cast ad Inventario, aggiungendolo e rimuovendolo dalla stanza
	 * cosi nel caso in cui non si possa fare il cast vuol dire che l'oggetto non si può raccogliere
	 * o nel secondo caso di errore non sarà valido il comando
	 * 
	 * @param	oggetto			l'oggetto che si vuole raccogliere
	 * @param	posizione		la stanza dove ci troviamo
	 * @param	player			il giocatore
	 * @return					nel caso vada bene addInventory(Inventario) cosi da restituire un messaggio di successo, in altri casi un messaggio d'errore
	 */
	private static String casePrendi(String oggetto, Stanza posizione, Giocatore player)
	{
		try
		{
			Oggetto o = posizione.getItem(oggetto);
			posizione.getItems().remove(o);
			return player.addInventory((Inventario) o);
		}
		catch (ClassCastException e) { return "Non puoi raccogliere quest'oggetto!"; }
		catch (NullPointerException e) { return "Comando non valido!"; }
	}
	
	/*
	 * Metodo che rappresenta il caso "Apri *oggetto*"
	 * prova ad aprire l'oggetto facendo il cast ad Container
	 * cosi nel caso in cui non si possa fare il cast vuol dire che l'oggetto non si può aprire
	 * o nel secondo caso di errore non sarà valido il comando
	 * 
	 * @param	oggetto			l'oggetto che si vuole aprire
	 * @param	m				il mondo di gioco
	 * @return					nel caso vada bene apri(Mondo) cosi da restituire un messaggio di successo, in altri casi un messaggio d'errore
	 */
	private static String caseApri(String oggetto, Mondo m)
	{
		try { return ((Container) m.getPosizione().getItem(oggetto)).apri(m); }
		catch (ClassCastException e) { return "Non puoi aprire quest'oggetto!"; }
		catch (NullPointerException e) { return "Comando non valido!"; }
	}
	
	/*
	 * Metodo che gestisce l'ester egg.
	 * Ogni volta viene inserito un comando direzionale si attiva questo metodo
	 * e se la sequenza di tasti premuta è uguale a quella salvata tra i campi
	 * restituisce l'ester egg attivo
	 * 
	 * @param	code	il comando direzionale inserito
	 */
	private static void checkKonami(Direzione code) {
        if (code == KONAMI_CODE[currentKonami]) currentKonami++;
        else currentKonami = 0;
        if (currentKonami == KONAMI_CODE.length) 
        {
        	System.out.println();
        	System.out.println();
        	System.out.println("Konami_Code Attivato!!!!\n");
            System.out.println("____0000000000______0000000000_____\r\n"
            				 + "__000________000__000________000___\r\n"
            				 + "_000___________0000___________000__\r\n"
            				 + "000_____________00_____________000_\r\n"
            				 + "000____________________________000_\r\n"
            				 + "000___________GRAZIE____________000_\r\n"
            				 + "_000____________PER____________000_\r\n"
            				 + "__000_________GIOCARE_________000___\r\n"
            				 + "___000______________________000____\r\n"
            				 + "_____000__________________000_____\r\n"
            				 + "_______000______________000________\r\n"
            				 + "_________000__________000__________\r\n"
            				 + "__________ 000______000____________\r\n"
            				 + "______________000000_______________\r\n"
            				 + "________________00_________________");
            currentKonami = 0;
        }
    }
	
	/*
	 * Getter dei comandi
	 */
	public static List<String> getComandi() { return COMANDI; }
}
