package caisse;

import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Cette classe permet la création ou le chargement d'une liste (un fichier .liste).
 * Elle peut aussi appeler une méthode pour ajouter ou supprimer un article.
 *
 */
public class modifier extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Chemin d'accèsà la liste des articles
	private String cheminListe;
	
	// Boutons, labels,...
	private JButton boutonCharger = new JButton("Charger Liste");
        private JButton boutonCreate = new JButton("Créer Liste");
	private JButton boutonAjouter = new JButton("Ajouter Article");
	private JButton boutonSupprimer = new JButton("Supprimer Article");
	private JButton boutonFermer = new JButton("Fermer");
	private JLabel labelInfo = new JLabel("<html>Avant de modifier la liste, sachez que chaque action prend effet immédiatement !</html>");
	private JLabel labelListe = new JLabel("Liste chargée : ");
	private JTextField textFieldCheminInfo = new JTextField(35);
	
	// Panels
	private JPanel panel = new JPanel(new GridLayout(4,1,5,5));
	private JPanel panelListe = new JPanel(new FlowLayout());
	private JPanel panelBoutonsListe = new JPanel(new FlowLayout());
	private JPanel panelBoutons = new JPanel (new FlowLayout());
	private JPanel panelInfo = new JPanel (new FlowLayout());
	
	/**
         * Constructeur de la fenetre
         */
        public modifier()
	{
		// Apparence de la fenetre
		this.setResizable(false);
		this.setTitle("Modification d'une liste d'articles");
		this.setSize(550, 180);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocationRelativeTo(null);
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("Moneo Resto USTHB");

		
		// Gestion des boutons, labels, panels...
		//Panel : liste
		panelListe.add(labelListe);
		panelListe.add(textFieldCheminInfo);
		panelListe.setOpaque(false);
		textFieldCheminInfo.setText("aucune");
		textFieldCheminInfo.setEnabled(false);
		//Panel : boutons liste
		panelBoutonsListe.add(boutonCharger);
		panelBoutonsListe.setOpaque(false);
		boutonCharger.setBorderPainted(false);
		boutonCharger.setFocusPainted(false);
		boutonCharger.setBackground(new Color(44, 62, 80));
		boutonCharger.setForeground(new Color(208, 208, 208));
		
                panelBoutonsListe.add(boutonCreate);
                boutonCreate.setBorderPainted(false);
        		boutonCreate.setFocusPainted(false);
        		boutonCreate.setBackground(new Color(44, 62, 80));
        		boutonCreate.setForeground(new Color(208, 208, 208));
		//Panel : boutons
		panelBoutons.add(boutonAjouter);
		boutonAjouter.setBorderPainted(false);
		boutonAjouter.setFocusPainted(false);
		boutonAjouter.setBackground(new Color(44, 62, 80));
		boutonAjouter.setForeground(new Color(208, 208, 208));
		panelBoutons.add(boutonSupprimer);
		boutonSupprimer.setBorderPainted(false);
		boutonSupprimer.setFocusPainted(false);
		boutonSupprimer.setBackground(new Color(44, 62, 80));
		boutonSupprimer.setForeground(new Color(208, 208, 208));
		panelBoutons.add(boutonFermer);
		boutonFermer.setBorderPainted(false);
		boutonFermer.setFocusPainted(false);
		boutonFermer.setBackground(new Color(44, 62, 80));
		boutonFermer.setForeground(new Color(208, 208, 208));
		//Panel : info
		panelInfo.add(labelInfo);
		labelInfo.setHorizontalAlignment(JLabel.CENTER);
		//Actions des boutons
		boutonCharger.addActionListener(this);
                boutonCreate.addActionListener(this);
		boutonAjouter.addActionListener(this);
		boutonSupprimer.addActionListener(this);
		boutonFermer.addActionListener(this);
		//Panel principal
		panel.add(panelListe);
		panelBoutons.setOpaque(false);
		panelInfo.setOpaque(false);
		panel.add(panelBoutonsListe);
		panel.add(panelBoutons);
		panel.add(panelInfo);
		
		panel.setBackground(new Color(198,156,109));
		this.setContentPane(panel);
		this.setVisible(true);
	}
	
	/**
         * Méthode qui gère l'action des boutons
         *  - boutonCharger (controle certaines conditions)
         *  - boutonCreate
         *  - boutonAjouter / boutonSupprimer (controle certaines conditions)
         *  - boutonFermer
         */
        public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == boutonCharger)
		{
                        cheminListe = chargerListe();
                        ArrayList<String> liste = null;
			if(cheminListe == null)
                        {
                            textFieldCheminInfo.setText("aucune (problème lors du chargement)");
                        }
                        else
                        {
                            liste = new ArrayListListe(cheminListe);
                        }
                        if(liste == null)
                        {
                            textFieldCheminInfo.setText("aucune (problème lors du chargement)");
                        }
                        else if((new JComboBoxListe(liste).getItemAt(0)).equals("Liste non valide !"))
                        {
                            JOptionPane.showMessageDialog(null, "Cette liste n'est pas valide !", "Problème !", JOptionPane.ERROR_MESSAGE);
                            textFieldCheminInfo.setText("aucune (liste non valide)");
                            cheminListe = "nonvalide";
                        }
                        else
			{
                            textFieldCheminInfo.setText(cheminListe);
			}
		}

                if(event.getSource() == boutonCreate)
		{
                        FileWriter newListe;
                        try
                        {
                            cheminListe = createListe();
                            if (cheminListe == null)
                            {
                                textFieldCheminInfo.setText("aucune (problème lors de la création)");
                            }
                            else
                            {
                                if (!cheminListe.contains(".liste"))
                                {
                                    cheminListe += ".liste";
                                }
                                newListe = new FileWriter(cheminListe);
                                newListe.write("_.liste_\r\n**********\r\n");
                                newListe.close();
                                textFieldCheminInfo.setText(cheminListe);
                            }
                        }
                        catch (IOException ex)
                        {
                            JOptionPane.showMessageDialog(null, "Problème lors de la création !", "Erreur !", JOptionPane.ERROR_MESSAGE);
                        }
		}
		
		if(event.getSource() == boutonAjouter || event.getSource() == boutonSupprimer)
		{
			if(cheminListe == null)
			{
				JOptionPane.showMessageDialog(null, "Veuillez charger une liste !", "Attention !", JOptionPane.ERROR_MESSAGE);				
			}
                        else if(cheminListe.equals("nonvalide"))
			{
				JOptionPane.showMessageDialog(null, "Liste non valide !", "Erreur !", JOptionPane.ERROR_MESSAGE);
			}
			else if(event.getSource() == boutonAjouter)
			{
				new ajouterArticle(cheminListe);
			}
			else if(event.getSource() == boutonSupprimer)
			{
				new supprimerArticle(cheminListe);
			}
		}
		
		if(event.getSource() == boutonFermer)
		{
			dispose();
		}
	}
	
        /**
         * Méthode qui ouvre un gestionnaire de fichier et renvoie l'emplacement du fichier sélectionné.
         * @return String
         *      Désigne l'emplacement du fichier .liste sélectionné.
         *      Si erreur, renvoie null.
         */
        public String chargerListe()
	{
		FileFilter extension = new filtreFichier("Liste d'articles",".liste");
		JFileChooser cheminFichier = new JFileChooser();
		cheminFichier.addChoosableFileFilter(extension);
		int retour = cheminFichier.showOpenDialog(null);
		if(retour == JFileChooser.APPROVE_OPTION)
		{
			return cheminFichier.getSelectedFile().getAbsolutePath();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Problème lors du chargement (chemin d'accès non spécifié) !", "Erreur !", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}

        /**
         * Méthode qui ouvre un gestionnaire de fichier et renvoie l'emplacement du fichier sélectionné.
         * 
         *      Désigne l'emplacement du fichier .liste sélectionné.
         *      Si erreur, renvoie null.
         */
        public String createListe()
	{
		FileFilter extension = new filtreFichier("Liste d'articles",".liste");
		JFileChooser cheminFichier = new JFileChooser();
		cheminFichier.addChoosableFileFilter(extension);
		int retour = cheminFichier.showSaveDialog(null);
		if(retour == JFileChooser.APPROVE_OPTION)
		{
			return cheminFichier.getSelectedFile().getAbsolutePath();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Problème lors de la création (chemin d'accès non spécifié) !", "Erreur !", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}
