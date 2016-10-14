
package e_docs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;




/**
 * @author bekkouche oussama
 */
public class Start extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public Start() {
		initComponents();
	  	connectAndVerify(); 
		newConnection = false ; 
		
		
		TimerTask task = new TimerTask() //vérifier si la carte est présente 
    	{
    		@Override
    		public void run() 
    		{
    			CardManagement.isReaderConnected();
    			booleanCard = CardManagement.verifyCardConnection();
    			if(booleanCard && booleanFirstConnect)  // la carte est dans le lecteur et non sélectionée
    				{
    				if(newConnection == true){connectAndVerify();}//MAJ de vaeur de rep
    				if(rep == 1) // si la carte est déja personnalisé 
    				{
    					 // si authenetifcation	() activer le bouton connexion
    					if(CardManagement.authentification())
    					{
    						boutonConnexion.setEnabled(true);
    						labelMessage.setText("Vous pouvez vous connecter");
    					}
    					
    				}
    	
    				booleanFirstConnect = false ;
    				}
    			else if(!booleanCard) 
    				{
    				labelMessage.setText("Ins\u00e9rer votre carte \u00e0 puce dans le lecteur ");
    				booleanFirstConnect = true ; 
    				boutonConnexion.setEnabled(false);
    				newConnection = true; // quand la carte est présent une nouvelle connexion est nécessaire
    				if(booleanEspaceIsSet == true)
    				{
    					monEspace.dispose();
    				}
    				}
    		}	
    	};
    	
    	Timer timer = new Timer();
    	timer.scheduleAtFixedRate(task, 0, 1000);
		
	}

	private void initComponents() {
		labelMessage = new JLabel();
		boutonConnexion = new JButton();
		boutonQuitter = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setBackground(new Color(208,208,208));
		
		contentPane.setLayout(new GridBagLayout());
		JLabel lab= new JLabel(new ImageIcon("Accueil.png"));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(50,0,0,0);
		contentPane.add(lab,c);

		//---- labelMessage ----
		labelMessage.setText("Connexion à la carte");
		labelMessage.setFont(new Font("Tahoma", Font.PLAIN, 16));
		labelMessage.setForeground(new Color(44, 62, 80));

		//---- boutonConnexion ----
		boutonConnexion.setText("Connexion");
		
		boutonConnexion.setBorderPainted(false);
		boutonConnexion.setFocusPainted(false);
		boutonConnexion.setBackground(new Color(44, 62, 80));
		boutonConnexion.setForeground(new Color(208, 208, 208));
		boutonConnexion.setPreferredSize(new Dimension(150,35));
		
		boutonConnexion.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonConnexionActionPerformed(evt);
	           }
	       });
		boutonConnexion.setEnabled(false);

		//---- boutonQuitter ----
		boutonQuitter.setText("Quitter");
		
		boutonQuitter.setBorderPainted(false);
		boutonQuitter.setFocusPainted(false);
		boutonQuitter.setBackground(new Color(44, 62, 80));
		boutonQuitter.setForeground(new Color(208, 208, 208));
		boutonQuitter.setPreferredSize(new Dimension(150,35));
		
		boutonQuitter.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonQuitterActionPerformed(evt);
	           }
	       });
		
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(20,0,0,0);
		contentPane.add(labelMessage,c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 2;
		//c.insets = new Insets(100,0,0,0);
		contentPane.add(boutonConnexion,c);

		c.fill = GridBagConstraints.VERTICAL;
		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(20,0,0,0);
		contentPane.add(boutonQuitter,c);
		
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		contentPane.add(Box.createRigidArea(new Dimension(10,10)),c);

	
		pack();
		setLocationRelativeTo(getOwner());
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("E-Docs USTHB");
	}
	
	private void boutonConnexionActionPerformed(ActionEvent evt)
	{
		monEspace = new Espace();
		booleanEspaceIsSet = true ;
	}
	
	private void boutonQuitterActionPerformed(ActionEvent evt)
	{
		System.exit(0);
	}
	
	private  void connectAndVerify() // connecter au lecteur et selcetion de l'applet et cérification selon la réponse de sélection
 	{
 		try {
			new CardManagement(); //connexion au lecteur
			 rep=CardManagement.select();
			
			if(rep == 2) // si la carte est non initialisée
			{
				JOptionPane.showMessageDialog(null, "Carte non initialisée" , "Problème !", JOptionPane.ERROR_MESSAGE);
				CardManagement.c.disconnect(true);
				System.exit(1);
			}
			else if(rep ==1)
			{
				
			JOptionPane.showMessageDialog(null, "Carte initialisée, vérification..." , "Problème !", JOptionPane.INFORMATION_MESSAGE);
			if(CardManagement.verifyTerminal())
			{
			CardManagement.setAESKey();
			if(CardManagement.verifyCard())
			{
				this.labelMessage.setText("Authentification : Entrez le code PIN");
			}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Terminal non authentifié  " , "Problème !", JOptionPane.ERROR_MESSAGE);
			}
			
			}
			else if(rep == 0)
			{
				//JOptionPane.showMessageDialog(null, "applet non sélectionée " , "Problème !", JOptionPane.ERROR_MESSAGE);
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
	
	private JLabel labelMessage;
	private JButton boutonConnexion;
	private JButton boutonQuitter;
	private Espace monEspace ;
	
    private boolean newConnection = true;
	private boolean booleanCard = false;
	private boolean booleanFirstConnect = true;
    private boolean booleanEspaceIsSet = false;



	private static byte rep ;
}
