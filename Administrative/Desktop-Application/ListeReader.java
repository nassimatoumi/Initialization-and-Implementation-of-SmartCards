package adminTool;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * ArrayListListe est la classe qui lit un fichier .liste et cr�e une ArrayList<String> contenant � la r�f�rence :
 *
 */
public class ListeReader extends ArrayList<String>
{
	private static final long serialVersionUID = 1L;

	
	public ListeReader (String chemin)
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
			JOptionPane.showMessageDialog(null, "Probl�me lors de la constitution de la liste des articles.", "Probl�me !", JOptionPane.ERROR_MESSAGE);
		}
	}
}
