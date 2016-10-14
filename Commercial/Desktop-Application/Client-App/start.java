package caisse;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Interface graphique qui gère le démarrage du programme
 */
public class start extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


    private String version = "version 1.0";

      // Chemin d'accès à  la liste des articles
	private String cheminListe;
	
	// Panel, boutons
	private JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
	private JButton boutonDemarrer = new JButton("Ouvrir la caisse");
	private JButton boutonModifier = new JButton("Modifier une liste");
        private JButton boutonParam = new JButton("Paramètres");
	private JButton boutonQuitter = new JButton("Quitter");
	
	/**
         * Constructeur de la fenetre.
         */
        public start()
	{
		// Apparence de la fenetre
		this.setResizable(false);
		this.setTitle("Démarrage de la caisse (" + version + ")");
		this.setSize(400, 90);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("Moneo Resto USTHB");
		
		// Gestion des boutons, labels, panels...
		boutonDemarrer.setBorderPainted(false);
		boutonDemarrer.setFocusPainted(false);
		boutonDemarrer.setBackground(new Color(44, 62, 80));
		boutonDemarrer.setForeground(new Color(208, 208, 208));
		
		boutonModifier.setBorderPainted(false);
		boutonModifier.setFocusPainted(false);
		boutonModifier.setBackground(new Color(44, 62, 80));
		boutonModifier.setForeground(new Color(208, 208, 208));
		
		boutonParam.setBorderPainted(false);
		boutonParam.setFocusPainted(false);
		boutonParam.setBackground(new Color(44, 62, 80));
		boutonParam.setForeground(new Color(208, 208, 208));
		
		boutonQuitter.setBorderPainted(false);
		boutonQuitter.setFocusPainted(false);
		boutonQuitter.setBackground(new Color(44, 62, 80));
		boutonQuitter.setForeground(new Color(208, 208, 208));
		
		panel.add(boutonDemarrer);
		panel.add(boutonModifier);
        panel.add(boutonParam);
		panel.add(boutonQuitter);
		panel.setBackground(new Color(198,156,109));
		boutonDemarrer.addActionListener(this);
		boutonModifier.addActionListener(this);
        boutonParam.addActionListener(this);
		boutonQuitter.addActionListener(this);
		
		this.setContentPane(panel);
		this.setVisible(true);
	}
	
	
        public static void main (String args[])
	{
      
		new start();
	}
	
	/**
         * Méthode qui gére l'action des boutons
         *  - boutonDemarrer
         *  - boutonModifier
         *  - boutonParam
         *  - boutonQuitter
         * @param event
         */
        public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == boutonDemarrer)
		{
                    // Chargement du fichier de configuration
                    Properties settings = new Properties();
                    File cheminSettings = new File(System.getProperty("user.dir") + "/settings.txt");
                    try
                    {
                        settings.load(new FileInputStream(cheminSettings));
                        if(Boolean.valueOf(settings.getProperty("tickListe")).booleanValue())
                        {
                            cheminListe = settings.getProperty("cheminListe");
                        }
                        else
                        {
                            cheminListe = chargerListe();
                        }
                    }
                        catch(IOException e)
                        {
                            cheminListe = chargerListe();
                        }

                    if(cheminListe != null)
                    {
                        ArrayList<String> liste = new ArrayListListe(cheminListe);
                        if(liste.size() == 0)
                        {
                            JOptionPane.showMessageDialog(null, "La liste sélectionnée est vide !", "Problème !", JOptionPane.ERROR_MESSAGE);
                        }
                        else if((new JComboBoxListe(liste).getItemAt(0)).equals("Liste non valide !"))
                        {
                            JOptionPane.showMessageDialog(null, "Cette liste n'est pas valide !", "Problème !", JOptionPane.ERROR_MESSAGE);
                        }
                        else
                        {	try {
                        	
							new CardManagement();
							    } 
                        	catch (Exception e)
                        		{
                        		e.printStackTrace();
                        		}
                            new Caisse(cheminListe).setVisible(true);
                        }
                    }
		}
		
		if(event.getSource() == boutonModifier)
		{
			new modifier();
		}

                if(event.getSource() == boutonParam)
		{
			new param().setVisible(true);
		}
		
		if(event.getSource() == boutonQuitter)
		{
			System.exit(0);
		}
	}
	
	/**
         * Méthode qui ouvre un gestionnaire de fichier et renvoie l'emplacement du fichier sélectionné.
         * 
         *      Désigne l'emplacement du fichier .liste sélectionné.
         *      Si erreur, renvoie null.
         */
        private String chargerListe()
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
			JOptionPane.showMessageDialog(null, "Problème lors du chargement !", "Erreur !", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
}
