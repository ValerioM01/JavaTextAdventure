package it.uniroma1.textadv;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * Classe di prova e di test per il gioco.
 */
public class Test 
{
	public static void main(String[] args) throws IOException, NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException 
	{
		Gioco g = new Gioco();
		Mondo m = Mondo.fromFile("minizak.game");
		Path scriptDiGioco = Paths.get("minizak.ff");
		g.play(m, scriptDiGioco);
	}
}