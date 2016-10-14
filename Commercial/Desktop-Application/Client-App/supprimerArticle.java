package caisse;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
 


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Cette classe permet la suppression d'un article.
 * C'est une interface graphique contenant une liste qui permet de sélectionner l'article à  supprimer.
 * 
 * 
 */
public class supprimerArticle extends JFrame implements ActionListener
{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// L'emplacement du fichier .liste
	private String cheminListe;
	
        //  Création de tout ce qui concerne la liste (ArrayListListe + JComboBoxListe)
	private ArrayList<String> articlesListe;
	@SuppressWarnings("rawtypes")
	private JComboBox JComboBoxListe;
	
	// Boutons (Supprimer et Fermer) et label (indication de la liste concernée)
	private JButton boutonSupprimer = new JButton("Supprimer Article");
	private JButton boutonFermer = new JButton("Fermer");
	private JLabel labelListe = new JLabel();
	
	/*
	 *  Panels
	 *  Panel : le panel principal
	 *  PanelListe : le panel permettant l'affichage de la liste concernée
	 */
	private JPanel panel = new JPanel(new GridLayout(4,1,5,5));
	private JPanel panelListe = new JPanel(new FlowLayout());
	 
	/**
	 * Constructeur de la fenetre "Supprimer un article"
	 * 	String désignant l'emplacement du fichier .liste
	 */
	public supprimerArticle(String cheminListe)
	{
		this.cheminListe = cheminListe;
		
		// Apparence de la fenetre
		this.setResizable(false);
		this.setTitle("Supprimer un article");
		this.setSize(420, 180);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);  
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("Moneo Resto USTHB");
		
		// Liste (ArrayList + JComboBox)
		articlesListe = new ArrayListListe(cheminListe);
		JComboBoxListe = new JComboBoxListe(articlesListe);
		
		// Gestion des boutons, labels, panels...
		panel.setBackground(Color.ORANGE);
		panelListe.setBackground(Color.ORANGE);
		labelListe.setText("Liste : " + cheminListe);
		panelListe.add(labelListe);
		panel.add(panelListe);
		panel.add(JComboBoxListe);
		panel.add(boutonSupprimer);
		panel.add(boutonFermer);
		
		boutonSupprimer.addActionListener(this);
		boutonFermer.addActionListener(this);
		
		this.setContentPane(panel);
		this.setVisible(true);
	}
	
	/**
         * Méthode qui gère l'action des boutons
         *  - boutonSupprimer
         *  - boutonFermer
         */
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == boutonSupprimer)
		{
			if(articlesListe.size() != 0)
			processSuppressionArticle(articlesListe, ((JComboBoxListe.getSelectedIndex())*2)+1, cheminListe);
		}
		
		if(event.getSource() == boutonFermer)
		{
			dispose();
		}
	}
	
	/**
	 * Méthode permettant la supression d'un article
	 *
	 */
	private void processSuppressionArticle(ArrayList<String> articlesListe, int article, String cheminListe)
	{
		BufferedWriter ecriture;
		try
		{
                    ecriture = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(cheminListe)));
                    for(int i = 0; i < articlesListe.size(); i++)
                    {
    	        	if(i == article)
	        	{
                            i++;
	        	}
	        	else
	        	{
                            ecriture.write(""+articlesListe.get(i)+"\n");
	        	}
                    }
                    ecriture.close();
                    dispose();
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "Problème lors de la suppression de l'article.", "Problème !", JOptionPane.ERROR_MESSAGE);
		}
                catch (IOException e)
                {
			JOptionPane.showMessageDialog(null, "Problème lors de la suppression de l'article.", "Problème !", JOptionPane.ERROR_MESSAGE);
		}
	}
}	
