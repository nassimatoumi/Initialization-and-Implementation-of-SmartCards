package caisse;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Cette classe est une fenetre permettant d'ajouter des articles dans un fichier .liste.
 * 
 * 
 * 
 */
public class ajouterArticle extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Emplacement fichier liste
	private String cheminListe;
	
	// Boutons, TextFields,...
	private JButton boutonAjouter = new JButton("Ajouter Article");
	private JButton boutonFermer = new JButton("Fermer");
	private JTextField textFieldNom = new JTextField(10);
	private JTextField textFieldPrix = new JTextField(5);
	private JLabel labelNom = new JLabel ("Nom de l'article : ");
	private JLabel labelPrix = new JLabel ("Prix de l'article (DA) : ");
	private JLabel labelListe = new JLabel();
	
	// Panels
	private JPanel panel = new JPanel(new GridLayout(4,1,5,5));
	private JPanel panelListe = new JPanel(new FlowLayout());
	private JPanel panelTextFields = new JPanel (new FlowLayout());
	
	/**
         * Constructeur de la fenetre
         
         */
        public ajouterArticle(String cheminListe)
	{
		this.cheminListe = cheminListe;
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("Moneo Resto USTHB");
		
		// Apparence de la fenetre
		this.setResizable(false);
		this.setTitle("Ajouter un article");
		this.setSize(420, 180);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		// Gestion des boutons, labels, panels...
		panel.setBackground(Color.LIGHT_GRAY);
		panelListe.setBackground(Color.LIGHT_GRAY);
		labelListe.setText("Liste : " + cheminListe);
		panelListe.add(labelListe);
		panelTextFields.add(labelNom);
		panelTextFields.add(textFieldNom);
		panelTextFields.add(labelPrix);
		panelTextFields.add(textFieldPrix);
		panel.add(panelListe);
		panel.add(panelTextFields);
		panel.add(boutonAjouter);
		panel.add(boutonFermer);
		
		boutonAjouter.addActionListener(this);
		boutonFermer.addActionListener(this);
		
		this.setContentPane(panel);
		this.setVisible(true);
	}
	
	/**
         * Méthode qui gère l'action des boutons
         *  - boutonAjouter (controle certaines conditions)
         *  - boutonFermer
         * 
         */
        public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == boutonAjouter)
		{
			String nom = textFieldNom.getText();
			String prix = textFieldPrix.getText();
			// Si le champ "nom" est vide
			if(nom.equals(""))
			{
				JOptionPane.showMessageDialog(null, "Erreur avec le nom de l'article !\n - Veuillez indiquer le nom de l'article", "Attention !", JOptionPane.WARNING_MESSAGE);				
			}
			// Si le champ "prix" est vide
			else if(!prix.matches("^[-+]?\\d+(\\.\\d+)?$"))
			{
				JOptionPane.showMessageDialog(null, "Erreur avec le prix de l'article !\n - Veuillez indiquer le prix de l'article\n - Vérifiez si ce sont bien des chiffres et non des lettres\n - Vérifiez si vous avez utilisé un point et non une virgule pour les décimales", "Attention !", JOptionPane.WARNING_MESSAGE);
			}
			// Si tout est bon
			else
			{
				processAjoutArticle(nom, prix, cheminListe);
				textFieldNom.setText("");
				textFieldPrix.setText("");
			}
		}
		
		if(event.getSource() == boutonFermer)
		{
			dispose();
		}		
	}
	
	/**
         * Méthode qui ajoute un article à  la fin d'un fichier .liste (si aucun problème n'a été détecté par la méthode actionPerformed)
         *
         */
        private void processAjoutArticle (String nom, String prix, String cheminListe)
	{
		try
		{
			FileWriter ecriture = new FileWriter(cheminListe,true);
			ecriture.write(nom + "\r\n");
			ecriture.write(prix + "\r\n");
			ecriture.close();			
		}
		catch (IOException e)
		{
			JOptionPane.showMessageDialog(null, "Problème lors de l'ajout de l'article.", "Problème !", JOptionPane.ERROR_MESSAGE);
		}
	}

}
