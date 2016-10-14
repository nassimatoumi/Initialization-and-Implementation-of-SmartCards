package adminTool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Start extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Start() {
		
		initComponents();
		connectAndVerify(); // première connexion
		newConnection = false ; // pas besoin d'une nouvelle conexion a la carte
		

		
		TimerTask task = new TimerTask() //vérifier si la carte est présente 
    	{
    		@Override
    		public void run() 
    		{
    			CardManagement.isReaderConnected();
    			booleanCard = CardManagement.verifyCardConnection();
    			if(booleanCard && booleanFirstConnect)  // la carte est dans le lecteur et non sélectionée
    				{
    				if(newConnection == true){connectAndVerify();}
    				if(rep == 1) // si la carte est déja personnalisé en désactive le bouton Personnalise
    				{
    				boutonPersonnaliser.setEnabled(false);
    				boutonRenouveler.setEnabled(true);
    				boutonReinitialiser.setEnabled(true);
    				boutonListe.setEnabled(true);

    				}
    				else if (rep == 2)
    				{
        			boutonPersonnaliser.setEnabled(true);
    				boutonRenouveler.setEnabled(false);
    				boutonReinitialiser.setEnabled(false);
    				boutonListe.setEnabled(true);
    				}
    				else if (rep == 0)
    				{
    					boutonPersonnaliser.setEnabled(false);
        				boutonRenouveler.setEnabled(false);
        				boutonReinitialiser.setEnabled(false);
        				boutonListe.setEnabled(true);
    				}
    				booleanFirstConnect = false ;
    				}
    			else if(!booleanCard) 
    				{
    				booleanFirstConnect = true ; 			/* désactiver les boutons*/
    				boutonListe.setEnabled(false);
    				boutonPersonnaliser.setEnabled(false);
    				boutonRenouveler.setEnabled(false);
    				boutonReinitialiser.setEnabled(false);
    				newConnection = true; // quand la carte est présente une nouvelle connexion est nécessaire
    				if(booleanEcranIsSet)
    				{
    				ecran.frame.dispose(); // fermer l'objet ecran
    				booleanEcranIsSet = false ;
    				}
    				if(booleanFormulaireIsSet)
    				{
    				form.dispose(); // fermer l'objet formulaire
    				booleanFormulaireIsSet = false ;
    				}
    				if(booleanNewPINIsSet)
    				{
    				RePIN.dispose(); // fermer l'objet NewPIN
    				booleanNewPINIsSet = false ;
    				}
    				if(booleanRenouvelerIsSet)
    				{
    				Re.dispose(); // fermer l'objet Renouvelement
    				booleanRenouvelerIsSet = false ;
    				}
    				

    				}
    		}	
    	};
    	
    	Timer timer = new Timer();
    	timer.scheduleAtFixedRate(task, 0, 1000);
		
	} // fin de constructeur

	private void initComponents() {
		labelBienVenue = new JLabel();
		labelListApplet = new JLabel();
		boutonListe = new JButton();
		boutonRenouveler = new JButton();
		labelPersonnaliser = new JLabel();
		boutonPersonnaliser = new JButton();
		labelReinitialiser = new JLabel();
		boutonReinitialiser = new JButton();
		labelRenouveler = new JLabel();
		
		//======== this ========
		setResizable(false);
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("Administration E-Docs Resto USTHB");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(400, 400);
		setIconImage(new ImageIcon("logo.png").getImage());

		
		setLayout(new GridLayout(1,2));
		JLabel lab= new JLabel(new ImageIcon("AdminTool.png"));
		Container contentPane = getContentPane();
		
		//---- labelBienVenue ----
		labelBienVenue.setText(" ");
		labelBienVenue.setBackground(Color.white);
		labelBienVenue.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labelBienVenue.setForeground(Color.white);

		//---- labelListApplet ----
		labelListApplet.setText(" ");
		

		//---- boutonListe ----
		boutonListe.setBorderPainted(false);
		boutonListe.setFocusPainted(false);
		boutonListe.setContentAreaFilled(false);
		try {
			File abc=new File("Gérer.png");
		    Image img = ImageIO.read(abc);
		    boutonListe.setIcon(new ImageIcon(img));
		    
		    File def=new File("GererHover.png");
		    Image img2 = ImageIO.read(def);
		    boutonListe.setRolloverIcon(new ImageIcon(img2));
		  } catch (IOException ex) {
		  }

		//---- boutonRenouveler ----
		boutonRenouveler.setBorderPainted(false);
		boutonRenouveler.setFocusPainted(false);
		boutonRenouveler.setContentAreaFilled(false);
		try {
			File abc=new File("Renouveler.png");
		    Image img = ImageIO.read(abc);
		    boutonRenouveler.setIcon(new ImageIcon(img));
		    
		    File def=new File("RenewHover.png");
		    Image img2 = ImageIO.read(def);
		    boutonRenouveler.setRolloverIcon(new ImageIcon(img2));
		  } catch (IOException ex) {
		  }
		//---- labelPersonnaliser ----
		labelPersonnaliser.setText(" ");

		

		//---- boutonPersonnaliser ----
		boutonPersonnaliser.setBorderPainted(false);
		boutonPersonnaliser.setFocusPainted(false);
		boutonPersonnaliser.setContentAreaFilled(false);
		
		try {
			File abc=new File("Personnaliser.png");
		    Image img = ImageIO.read(abc);
		    boutonPersonnaliser.setIcon(new ImageIcon(img));
		    
		    File def=new File("PersoHover.png");
		    Image img2 = ImageIO.read(def);
		    boutonPersonnaliser.setRolloverIcon(new ImageIcon(img2));
		  } catch (IOException ex) {
		  }
		

		//---- labelReinitialiser ----
		labelReinitialiser.setText(" ");


		//---- boutonReinitialiser ----
		boutonReinitialiser.setBorderPainted(false);
		boutonReinitialiser.setFocusPainted(false);
		boutonReinitialiser.setContentAreaFilled(false);
		
		try {
			File abc=new File("Reinitialiser.png");
		    Image img = ImageIO.read(abc);
		    boutonReinitialiser.setIcon(new ImageIcon(img));
		    
		    File def=new File("PINHover.png");
		    Image img2 = ImageIO.read(def);
		    boutonReinitialiser.setRolloverIcon(new ImageIcon(img2));
		  } catch (IOException ex) {
		  }

		//---- labelRenouveler ----
		labelRenouveler.setText(" ");
		
		GridLayout gaps=new GridLayout(3,1);
		GridLayout gaps2=new GridLayout(1,2);
		gaps.setHgap(10);
		gaps.setVgap(20);
		gaps2.setHgap(10);
		gaps2.setVgap(20);
		contentPane.setLayout(gaps);
		contentPane.add(lab);
		JPanel test1=new JPanel();
		JPanel test2=new JPanel();
		contentPane.add(test1);
		contentPane.add(test2);
		contentPane.setBackground(new Color(44, 62, 80));
		
		test1.setBackground(new Color(44, 62, 80));
		test1.setLayout(gaps2);
		test1.add(boutonListe);
		test1.add(boutonPersonnaliser);
		test2.setBackground(new Color(44, 62, 80));
		test2.setLayout(gaps2);
		test2.add(boutonRenouveler);
		test2.add(boutonReinitialiser);
		
		boutonListe.addActionListener(this);
    	boutonPersonnaliser.addActionListener(this);
    	boutonRenouveler.addActionListener(this);
    	boutonReinitialiser.addActionListener(this);
		
		pack();
		setLocationRelativeTo(getOwner());
		this.setVisible(true);
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}
	
	
	public void actionPerformed(ActionEvent event)
	{
	 if(event.getSource() == boutonListe)
	 {
		 new ecran();
		 booleanEcranIsSet = true ;
	 	 redirect();//rediriger la sortie standard vers l'objet ecran
		 String[] commande = { "cmd.exe", "/C", "gp -l" }; // la commande a exécuter 
		 cmd(commande,"La carte contient les packages suivants : \n"); 
	 }
	 
	 else if(event.getSource() == boutonPersonnaliser)
	 {
		 form = new Formulaire();
		 booleanFormulaireIsSet = true ;
	 }
	 else if(event.getSource() == boutonRenouveler)
	 {
		
		 
		 Re = new Renouveler();
		 booleanRenouvelerIsSet = true ;
		 
	 }
	 else if(event.getSource() == boutonReinitialiser)
	 {
		RePIN = new  NewPIN();
		booleanNewPINIsSet = true ;
	 }
		 
	 
	 
	}


 	private void redirect()// méthode qui redirige la sortie standard vers l'objet ecran
 	{
		PrintStream out = new PrintStream( new TextAreaOutputStream(ecran.jTextArea1) );
		ecran.jTextArea1.setText("");
		System.setOut(out);
		System.setErr(out);
 	}
 	
 	private void cmd(String [] commande,String message)//méthode qui exécute des commande en shell
 	{
 		Runtime runtime = Runtime.getRuntime();
 		try {
 			
 			runtime = Runtime.getRuntime();
 			final Process process = runtime.exec(commande);
 			System.out.println(message);
 			// Consommation de la sortie standard de l'application externe dans un Thread separe
 			new Thread() {
 				public void run() {
 					try {
 						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
 						String line = "";
 						try {
 							while((line = reader.readLine()) != null) {
 								// Traitement du flux de sortie de l'application si besoin est
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

 			
 			// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
 			new Thread() {
 				public void run() {
 					try {
 						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
 						String line = "";
 						try {
 							while((line = reader.readLine()) != null) {
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
 		

 	}

 	private static void connectAndVerify() // connecter au lecteur et selcetion de l'applet et cérification selon la réponse de sélection
 	{
 		try {
			new CardManagement(); //connexion au lecteur
			 rep=CardManagement.select();
			
			if(rep == 2) // si la carte est non initialisée
			{
				JOptionPane.showMessageDialog(null, "Carte non initialisée." , "Problème !", JOptionPane.INFORMATION_MESSAGE);
				if(CardManagement.verifyTerminal())
				{
				CardManagement.setAESKey();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Pas de confiance en ce terminal  " , "Problème !", JOptionPane.ERROR_MESSAGE);
				}

			}
			else if(rep ==1)
			{
				
			JOptionPane.showMessageDialog(null, "Carte initialisée, vérification..." , "Problème !", JOptionPane.INFORMATION_MESSAGE);
			if(CardManagement.verifyTerminal())
			{
			CardManagement.setAESKey();
			if(CardManagement.verifyCard() == false ){System.exit(1);}
			CardManagement.clean("C:\\E_docs_tool\\E_docs_Admin_tool\\certificat_store");
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Pas de confiance en ce terminal  " , "Problème !", JOptionPane.ERROR_MESSAGE);
			}
			
			}
			
			

		} 
    	catch (Exception e) {
			e.printStackTrace();
		 }
 	}
 	
	 public static void main(String[] args)
	 {
		 new Start();
	 }

	
	private JLabel labelBienVenue;
	private JLabel labelListApplet;
	private JButton boutonListe;
	private JButton boutonRenouveler;
	private JLabel labelPersonnaliser;
	private JButton boutonPersonnaliser;
	private JLabel labelReinitialiser;
	private JButton boutonReinitialiser;
	private JLabel labelRenouveler;
	
	private boolean booleanCard = false;  // true si la carte est présente dans le lecteur
	private boolean booleanFirstConnect = true; //true si c'est la 1ere connexion de la carte au lecteur
	private boolean booleanEcranIsSet = false; // true si un objet ecran est déja créé
	private boolean booleanFormulaireIsSet = false; // true si un objet Formulaire est déja créé
    private boolean booleanNewPINIsSet = false ;// true si un objet NewPIN est déja créé
    private boolean booleanRenouvelerIsSet = false; // true si un objet Renouveler est déja créé
    private boolean newConnection = true;// true si le lecteur est débranché

    
	private Formulaire form ;
	private NewPIN RePIN;
	private Renouveler Re;
    private static byte rep ; //contient la reponse de la selection de l'applet
}
