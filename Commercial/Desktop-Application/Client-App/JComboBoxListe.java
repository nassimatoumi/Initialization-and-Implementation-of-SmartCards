package caisse;

import java.util.ArrayList;

import javax.swing.JComboBox;

/**
 * JComboBoxListe est la classe qui permet la création d'une liste de type JComboBox.
 *
 */
@SuppressWarnings("rawtypes")
public class JComboBoxListe extends JComboBox
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de JComboBoxListe (type JComboBox)
	 
	 */
	@SuppressWarnings("unchecked")
	public JComboBoxListe (ArrayList<String> liste)
	{
            if(liste.size() == 2)
            {
                this.addItem("Aucun article dans la liste !");
            }
            else if(!liste.get(0).equals("_.liste_"))
            {
                this.addItem("Liste non valide !");
            }
            else
            {
                try
                {
                    for(int i = 2; i < liste.size(); i+=2)
                    {
                        this.addItem(liste.get(i) + " (" + liste.get(i+1) + " DA)");
                    }
                }
                catch(IndexOutOfBoundsException e)
                {
                    this.removeAllItems();
                    this.addItem("Liste non valide !");
                }
            }
        }
}
