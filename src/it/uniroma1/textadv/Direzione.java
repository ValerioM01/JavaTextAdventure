package it.uniroma1.textadv;

/*
 * Direzioni prendibili in game.
 * 
 * @author Valerio Mesiti
 */
public enum Direzione {
	
	/*
	 * Nord 
	 */
	n,
	
	/*
	 * Sud 
	 */
	s,
	
	/*
	 * Est 
	 */
	e,
	
	/*
	 * Ovest 
	 */
	o;
	
	/*
	 * Inverte la direzione data in input.
	 * 
	 * @param d direzione da invertire.
	 * @return direzione invertira.
	 */
	public Direzione inversa(Direzione d) {
		switch(d)
		{
			case n: return s; 
			case s: return n; 
			case e: return o;
			default: return e;
		}
	}
}
