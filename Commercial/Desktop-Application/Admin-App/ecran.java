package adminTool;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * affiche le résultat des commandes  GP dans JTextArea
 * */


public class ecran  {
	
	protected static JFrame frame;
    protected static  javax.swing.JTextArea jTextArea1;
    private javax.swing.JScrollPane jScrollPane1;
    private JTextField textFieldAID;
    private JSeparator separator_1;
    private JSeparator separator;
    private JLabel labelSupprimer;
    private JLabel labelInstaller;
    private JButton boutonChoix;
    private JButton boutonSupprimer;
    private boolean textFieldAIDVide = true ; //indique si le champ de AID contient le message par défaut ou non 
    private JTextField champChemin;
    private JButton boutonInstaller;
    private JButton boutonActualiser;
    
    public ecran()
    {
    	initialize();
    }
    
    
    private void initialize()  //initialisation des objets graphique
    {
    	
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 494);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setTitle("Gérer le contenue de la carte E-DOCS");
		frame.setIconImage(new ImageIcon("logo.png").getImage());

		jTextArea1 = new JTextArea();
		jTextArea1.setColumns(20);
	    jTextArea1.setEditable(false);
	    jTextArea1.setFont(new java.awt.Font("Lucida Console", 0, 13));
	    jTextArea1.setRows(6);
	    jTextArea1.setTabSize(20);
	    jTextArea1.setForeground(new Color(44, 62, 80));
	    jTextArea1.setCaretColor(new Color(44, 62, 80));
	    
	    
	    jScrollPane1 = new JScrollPane();
        jScrollPane1.setViewportView(jTextArea1);
		
	   separator = new JSeparator();
	   separator_1 = new JSeparator();

		
	   
	   textFieldAID = new JTextField();
	   textFieldAID.setColumns(10);
	   textFieldAID.setFont(new Font("font",Font.ITALIC,15));
	   textFieldAID.setForeground(Color.gray);
	   textFieldAID.setText("Entrez l'AID du package ");
	   textFieldAID.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	textFieldAID.setText("");
		    	textFieldAID.setFont(new Font("font",Font.ITALIC,16));
		 	    textFieldAID.setForeground(Color.black);
		    	textFieldAIDVide = false ;
		    }
		    public void focusLost(FocusEvent e) {
		    	//rien
		    }
		});
	   
	   labelSupprimer = new JLabel("Supprimer un package :");
	   labelSupprimer.setFont(new Font("Aleo-Regular", 0, 20));
	   labelSupprimer.setForeground(new Color(44, 62, 80));

	   boutonSupprimer = new JButton("Supprimer");
	   boutonSupprimer.setBorderPainted(false);
	   boutonSupprimer.setFocusPainted(false);
	   boutonSupprimer.setBackground(new Color(44, 62, 80));
	   boutonSupprimer.setForeground(new Color(208, 208, 208));
	   
	   boutonSupprimer.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt) {
               boutonSupprimerActionPerformed(evt);
           }
       });
		
	   labelInstaller = new JLabel("Installer un package (fichier .cap) :");
	   labelInstaller.setFont(new Font("Aleo-Regular", 0, 20));
	   labelInstaller.setForeground(new Color(44, 62, 80));
	   
	   boutonChoix = new JButton("Choisir un fichier");
	   boutonChoix.setBorderPainted(false);
	   boutonChoix.setFocusPainted(false);
	   boutonChoix.setBackground(new Color(44, 62, 80));
	   boutonChoix.setForeground(new Color(208, 208, 208));
	   boutonChoix.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent evt) {
               boutonChoixActionPerformed(evt);
           }
       });
		
		champChemin = new JTextField();
		champChemin.setColumns(10);
		champChemin.setEnabled(false);
		
		boutonInstaller = new JButton("Installer");
		boutonInstaller.setBorderPainted(false);
		boutonInstaller.setFocusPainted(false);
		boutonInstaller.setBackground(new Color(44, 62, 80));
		boutonInstaller.setForeground(new Color(208, 208, 208));

		  boutonInstaller.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonInstallerActionPerformed(evt);
	           }
	       });
		
		boutonActualiser = new JButton("Actualiser");
		boutonActualiser.setBorderPainted(false);
		boutonActualiser.setFocusPainted(false);
		boutonActualiser.setBackground(new Color(44, 62, 80));
		boutonActualiser.setForeground(new Color(208, 208, 208));
		boutonActualiser.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonActualiserActionPerformed(evt);
	           }
	       });
		
		Container contentPane=frame.getContentPane();
		contentPane.setBackground(new Color(108, 122, 137));
		GroupLayout groupLayout = new GroupLayout(contentPane);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(12)
							.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 420, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(labelSupprimer)))
					.addContainerGap(12, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(188, Short.MAX_VALUE)
					.addComponent(boutonInstaller)
					.addGap(177))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(boutonChoix)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(champChemin, GroupLayout.PREFERRED_SIZE, 286, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(labelInstaller)
					.addContainerGap(231, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(21, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(textFieldAID, GroupLayout.PREFERRED_SIZE, 289, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(boutonSupprimer)
					.addContainerGap(43, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, 411, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(21, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(168)
					.addComponent(boutonActualiser)
					.addContainerGap(179, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(13)
					.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
					.addGap(8)
					.addComponent(boutonActualiser)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelSupprimer)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(textFieldAID, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
						.addComponent(boutonSupprimer, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(labelInstaller)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(champChemin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(boutonChoix))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(boutonInstaller)
					.addGap(21))
		);
		
		frame.getContentPane().setLayout(groupLayout);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}
    

    private void boutonSupprimerActionPerformed(ActionEvent evt)  //action de bouton supprimer
    {
    	if(!textFieldAID.getText().matches("^[a-fA-F\\d]+$") ||textFieldAID.getText().equals("0") || textFieldAIDVide == true)
        {
            JOptionPane.showMessageDialog(null, "Entrez un AID valide", "Attention !", JOptionPane.WARNING_MESSAGE);
        }
    	else
    	{
    		String AID = textFieldAID.getText();
			String[] commande = { "cmd.exe", "/C", "gp -delete " + AID + " -deletedeps" }; // la commande a exécuter 
			redirect();
			cmd(commande,"Suppression...");
			textFieldAID.setFont(new Font("font",Font.ITALIC,15));
			textFieldAID.setForeground(Color.gray);
			textFieldAID.setText("Entrez le AID de package ");
	    	textFieldAIDVide = true ;


			
    	}
    }
    
    private void boutonChoixActionPerformed(ActionEvent evt) //action de bouton choix
    {
    	String cheminCAP = chargerCAP();
    	champChemin.setText(cheminCAP);
    }
    
    private void boutonInstallerActionPerformed(ActionEvent evt) //action de bouton Installer
    {
		
     String cheminCAP = champChemin.getText();
     if(!cheminCAP.equals(""))
     {
	 String[] commande = { "cmd.exe", "/C", "gp -install " + cheminCAP}; // la commande a exécuter 
	 redirect();
	 cmd(commande,"Installation...");
	 champChemin.setText("");
     }
     else
     {
	 JOptionPane.showMessageDialog(null, "Séléctionnez un fichier SVP", "Erreur !", JOptionPane.ERROR_MESSAGE);
     }

    }
    
    
    private void boutonActualiserActionPerformed(ActionEvent evt) //action de bouton Actualiser
    {
    	redirect();//rediriger la sortie standard vers l'objet ecran
		String[] commande = { "cmd.exe", "/C", "gp -l" }; // la commande a exécuter 
		cmd(commande,"La carte contient les packages suivants : \n"); 
    }
    
    
    
    public String chargerCAP() // filtrer les fichiers cap
   	{
    	FileNameExtensionFilter extension = new FileNameExtensionFilter("Fichier CAP","cap");
   		JFileChooser cheminFichier = new JFileChooser();
   		cheminFichier.addChoosableFileFilter(extension);
   		cheminFichier.setAcceptAllFileFilterUsed(false);

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
    
    
    
    private void redirect()// méthode qui redirige la sortie standard vers l'objet ecran
 	{
		PrintStream out = new PrintStream( new TextAreaOutputStream(ecran.jTextArea1) );
		ecran.jTextArea1.setText("");
		System.setOut(out);
		System.setErr(out);
 	}
    
    
    static void cmd(String [] commande)//méthode qui exécute des commande en shell
 	{
 			try {
 				Runtime runtime = Runtime.getRuntime();
				@SuppressWarnings("unused")
				final Process process = runtime.exec(commande);
			} catch (IOException e) {
				e.printStackTrace();
			}
		   
 		
 	}
    static void cmd(String [] commande,String message)//méthode qui exécute des commande en shell
 	{
 		Runtime runtime = Runtime.getRuntime();
 		try {
 			
 			 runtime = Runtime.getRuntime();
 			final Process process = runtime.exec(commande);
		    System.out.println(message + "\n");

 			// Consommation de la sortie standard de l'application externe dans un Thread separe
 			new Thread() {
 				public void run() {
 					try {
 						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
 						String line = "";
 						String res = "";
 						try {
 							while((line = reader.readLine()) != null) {
 								// Traitement du flux de sortie de l'application si besoin est
 								System.out.println(line);
 								res += line + '\n';
 							}
 							if(res.equals(""))
 							{
 								System.out.println("Operation reussie");

 							}


 						} finally {
 							reader.close();
 						}
 					} catch(IOException ioe) {
 						ioe.printStackTrace();
 					}
 				}
 			}.start();	 							

 			
 			// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
 			new Thread() {
 				public void run() {
 					try {
 						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
 						String line = "";
 						try {
 							while((line = reader.readLine()) != null) {
 								// Traitement du flux d'erreur de l'application si besoin est
 								System.out.println(line);

 							}
 						} finally {
 							reader.close();
 						}
 					} catch(IOException ioe) {
 						ioe.printStackTrace();
 					}
 				}
 			}.start();
 			
 		} 
 		catch (IOException e) {
 			e.printStackTrace();
 		}
 		
 		System.setOut(System.out);
 		System.setErr(System.out);
 	} 
    
    
}




