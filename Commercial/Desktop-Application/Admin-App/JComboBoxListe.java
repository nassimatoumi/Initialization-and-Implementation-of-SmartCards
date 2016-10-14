package adminTool;

import java.util.ArrayList;

import javax.swing.JComboBox;

/**
 * JComboBoxListe est la classe qui permet la création d'une liste de type JComboBox.
 *
 */
@SuppressWarnings("rawtypes")
public class JComboBoxListe extends JComboBox
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de JComboBoxListe (type JComboBox)
	 
	 */
	@SuppressWarnings("unchecked")
	public JComboBoxListe (ArrayList<String> liste) 
	{
           
            
            
                try
                {
                    for(int i = 0; i < liste.size(); i++)
                    {
                        this.addItem(liste.get(i));
                    }
                }
                catch(IndexOutOfBoundsException e)
                {
                    this.removeAllItems();
                    this.addItem("Liste non valide !");
                }
            
        }
	
	@SuppressWarnings("unchecked")
	protected static void setItem(JComboBox choix,ArrayList<String> liste)
	{
		try
        {
            for(int i = 0; i < liste.size(); i++)
            {
                choix.addItem(liste.get(i));
            }
        }
        catch(IndexOutOfBoundsException e)
        {
            choix.removeAllItems();
            choix.addItem("Liste non valide !");
        }
	}
}
