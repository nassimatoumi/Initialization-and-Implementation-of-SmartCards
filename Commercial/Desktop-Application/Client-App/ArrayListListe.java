package caisse;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;


public class ArrayListListe extends ArrayList<String>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de ArrayListListe (type ArrayList<String>)
	 *
	 */
	public ArrayListListe (String chemin)
	{
		BufferedReader lecture;
		try
		{
			lecture = new BufferedReader(new InputStreamReader(new FileInputStream(chemin)));
			String ligne;
			while((ligne = lecture.readLine()) != null)
			{
				this.add(ligne);
			}
			lecture.close();
		}
		catch(IOException e)
		{
			JOptionPane.showMessageDialog(null, "Problème lors de la constitution de la liste des articles.", "Problème !", JOptionPane.ERROR_MESSAGE);
		}
	}
}
