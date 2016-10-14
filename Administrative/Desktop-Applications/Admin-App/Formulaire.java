
package adminTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;

public class Formulaire extends JFrame {
	
	class ItemAction implements ActionListener //action de comboBox
	{
		public void actionPerformed(ActionEvent e)
		{
			 if(comboBox2.getSelectedItem().equals(""))
			 {    
				   comboBox3.removeAllItems();
			       comboBox4.removeAllItems();				  
			 }
			
		 if(comboBox2.getSelectedItem().equals("Licence"))
		 {     comboBox3.removeAllItems();
		       comboBox4.removeAllItems();
			  JComboBoxListe.setItem(comboBox3, (new ListeReader("licence.list")));
			  JComboBoxListe.setItem(comboBox4, (new ListeReader("niveauLicence.list")));

		 }
		 else if (comboBox2.getSelectedItem().equals("Master"))
		 {	 comboBox3.removeAllItems();
	         comboBox4.removeAllItems();
			 JComboBoxListe.setItem(comboBox3, (new ListeReader("master.list")));
		     JComboBoxListe.setItem(comboBox4, (new ListeReader("niveauMaster.list")));

		 }
		}
	}
	
	private static final long serialVersionUID = 1L;
/******************************************************************************************************/

	public Formulaire() {
		initComponents();
	}
/******************************************************************************************************/
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponents() { //initialisation des objets graphique
		labelPIN = new JLabel();
		passwordFirst = new JPasswordField();
		labelConfirmPIN = new JLabel();
		passwordConfirm = new JPasswordField();
		labelMatricule = new JLabel();
		champMatricule = new JTextField();
		labelNom = new JLabel();
		champNom = new JTextField();
		labelPrenom = new JLabel();
		champPrenom = new JTextField();
		//labelDep = new JLabel();
		comboBox1 = new JComboBox();
		labelCycle = new JLabel();
		comboBox2 = new JComboBox();
		labelFiliere = new JLabel();
		comboBox3 = new JComboBox();
		labelNiveau = new JLabel();
		comboBox4 = new JComboBox();
		boutonPersonnaliser = new JButton();
		boutonAnnuler = new JButton();

		//======== this ========
		setLayout(new BorderLayout());
		JLabel background=new JLabel(new ImageIcon("Formulaire.png"));
		background.setBackground(new Color(44, 62, 80));
		add(background);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle("Formulaire");
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("Administration E-Docs USTHB");

				//---- labelPIN ----
				labelPIN.setText("PIN ( 6 chiffres) :   ");
				labelPIN.setFont(new Font("Aleo-Regular", 0, 20));
				labelPIN.setOpaque(false);
				//---- labelConfirmPIN ----
				labelConfirmPIN.setText("Confirmation PIN :       ");
				labelConfirmPIN.setFont(new Font("Aleo-Regular", 0, 20));
				labelConfirmPIN.setOpaque(false);
				//---- labelMatricule ----
				labelMatricule.setText("Matricule :       ");
				labelMatricule.setFont(new Font("Aleo-Regular", 0, 20));
				labelMatricule.setOpaque(false);
				//---- labelNom ----
				labelNom.setText("Nom :              ");
				labelNom.setFont(new Font("Aleo-Regular", 0, 20));
				labelNom.setOpaque(false);
				//---- labelPrenom ----
				labelPrenom.setText("Pr\u00e9nom(s) :   ");
				labelPrenom.setFont(new Font("Aleo-Regular", 0, 20));
				labelPrenom.setOpaque(false);

				//---- labelCycle ----
				labelCycle.setText("Cycle :      ");
				labelCycle.setFont(new Font("Aleo-Regular", 0, 20));
				labelCycle.setOpaque(false);
				
				//---- labelFiliere ----
				labelFiliere.setText("Fili\u00e8re :    ");
				labelFiliere.setFont(new Font("Aleo-Regular", 0, 20));
				labelFiliere.setOpaque(false);
				
				//---- labelNiveau ----
				labelNiveau.setText("Niveau :  ");
				labelNiveau.setFont(new Font("Aleo-Regular", 0, 20));
				labelNiveau.setOpaque(false);

		//---- comboBox1 ----
		comboBox1.addItem("Informatique");
		
		//---- comboBox2 ----
		comboBox2.addItem("");
		comboBox2.addItem("Licence");
		comboBox2.addItem("Master");
		comboBox2.addActionListener(new ItemAction ());
		
		
		
		//---- boutonPersonnaliser ----
				boutonPersonnaliser.setText("Personnaliser");
				boutonPersonnaliser.setBorderPainted(false);
				boutonPersonnaliser.setFocusPainted(false);
				boutonPersonnaliser.setBackground(new Color(44, 62, 80));
				boutonPersonnaliser.setForeground(new Color(208, 208, 208));
				 boutonPersonnaliser.addActionListener(new ActionListener() {
			           public void actionPerformed(ActionEvent evt) {
			               boutonPersonnaliserActionPerformed(evt);
			           }
			       });

				//---- boutonAnnuler ----
				boutonAnnuler.setText("Annuler");
				boutonAnnuler.setBorderPainted(false);
				boutonAnnuler.setFocusPainted(false);
				boutonAnnuler.setBackground(new Color(44, 62, 80));
				boutonAnnuler.setForeground(new Color(208, 208, 208));
				boutonAnnuler.addActionListener(new ActionListener() {
			           public void actionPerformed(ActionEvent evt) {
			               boutonAnnulerActionPerformed(evt);
			           }
			       });


				GridLayout gaps=new GridLayout(1,2);
				gaps.setHgap(20);
				gaps.setVgap(20);
				
				JPanel panPin=new JPanel(new GridBagLayout());
				panPin.setOpaque(false);
				panPin.add(labelPIN);
				passwordFirst.setPreferredSize(new Dimension(60,25));
				passwordFirst.setHorizontalAlignment(JTextField.CENTER);
				panPin.add(passwordFirst);
				
				JPanel panPconf=new JPanel(new GridBagLayout());
				panPconf.setOpaque(false);
				panPconf.add(labelConfirmPIN);
				passwordConfirm.setPreferredSize(new Dimension(60,25));
				passwordConfirm.setHorizontalAlignment(JTextField.CENTER);
				panPconf.add(passwordConfirm);
				
				JPanel panNom=new JPanel(new GridBagLayout());
				panNom.setOpaque(false);
				panNom.add(labelNom);
				champNom.setPreferredSize(new Dimension(150,25));
				champNom.setFont(new Font("Calibri", 0, 17));
				champNom.setHorizontalAlignment(JTextField.CENTER);
				panNom.add(champNom);
				
				JPanel panPrenom=new JPanel(new GridBagLayout());
				panPrenom.setOpaque(false);
				panPrenom.add(labelPrenom);
				champPrenom.setPreferredSize(new Dimension(150,25));
				champPrenom.setFont(new Font("Calibri", 0, 17));
				champPrenom.setHorizontalAlignment(JTextField.CENTER);
				
				panPrenom.add(champPrenom);
				
				JPanel panMatricule=new JPanel(new GridBagLayout());
				panMatricule.setOpaque(false);
				panMatricule.add(labelMatricule);
				champMatricule.setPreferredSize(new Dimension(130,25));
				champMatricule.setFont(new Font("Calibri", 0, 20));
				champMatricule.setHorizontalAlignment(JTextField.CENTER);
				panMatricule.add(champMatricule);
				
				JPanel panFiliere=new JPanel(new GridBagLayout());
				panFiliere.setOpaque(false);
				
				
				JPanel panButtons=new JPanel(new GridBagLayout());
				JPanel panButtons2=new JPanel(gaps);
				panButtons.setOpaque(false);
				
				panButtons2.add(Box.createRigidArea(new Dimension(100,0)));
				panButtons2.add(Box.createRigidArea(new Dimension(100,0)));
				panButtons2.setOpaque(false);
				
				GridBagConstraints c = new GridBagConstraints();
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				boutonPersonnaliser.setPreferredSize(new Dimension(115,35));
				boutonAnnuler.setPreferredSize(new Dimension(105,35));
				panButtons.add(boutonPersonnaliser,c);
				
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 1;
				c.gridy = 0;
				c.insets = new Insets(0,10,0,0);
				panButtons.add(boutonAnnuler,c);
				
				JLabel padding=new JLabel("                                                                            ");
				padding.setOpaque(false);
				padding.setFont(new Font("Aleo-Regular", 0, 40));
				JPanel panPadding=new JPanel(new GridLayout(4,1));
				panPadding.setOpaque(false);
				
				GridBagLayout gaps2=new GridBagLayout();
			
				JPanel cyclePan=new JPanel(new GridBagLayout());
				cyclePan.setOpaque(false);
				cyclePan.add(labelCycle);
				comboBox2.setPreferredSize(new Dimension(100,20));
				cyclePan.add(comboBox2);
				
				JPanel filierePan=new JPanel(new GridBagLayout());
				filierePan.setOpaque(false);
				filierePan.add(labelFiliere);
				comboBox3.setPreferredSize(new Dimension(100,20));
				filierePan.add(comboBox3);
				
				JPanel niveauPan=new JPanel(new GridBagLayout());
				niveauPan.setOpaque(false);
				niveauPan.add(labelNiveau);
				comboBox4.setPreferredSize(new Dimension(100,20));
				niveauPan.add(comboBox4);
				
			
				JPanel pan2=new JPanel(gaps2);
				pan2.setOpaque(false);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 0;
				c.insets = new Insets(18,0,0,0);
				pan2.add(panPin,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 1;
				c.insets = new Insets(15,0,0,0);
				pan2.add(panPconf,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 2;
				c.insets = new Insets(15,0,0,0);
				pan2.add(panNom,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 3;
				c.insets = new Insets(15,0,0,0);
				pan2.add(panPrenom,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 4;
				c.insets = new Insets(15,0,0,0);
				pan2.add(panMatricule,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 5;
				c.insets = new Insets(15,0,0,0);
				pan2.add(new JSeparator(JSeparator.HORIZONTAL));
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 6;
				c.insets = new Insets(20,0,0,0);
				pan2.add(cyclePan,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 7;
				c.insets = new Insets(15,0,0,0);
				pan2.add(filierePan,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 8;
				c.insets = new Insets(15,0,0,0);
				pan2.add(niveauPan,c);
				c.fill = GridBagConstraints.HORIZONTAL;
				c.gridx = 0;
				c.gridy = 9;
				c.insets = new Insets(25,0,0,0);
				pan2.add(panButtons,c);
				
				background.setLayout(new FlowLayout());
				

				panPadding.add(padding);
				
				background.add(panPadding);
				
				background.add(pan2);
		pack();
		setLocationRelativeTo(getOwner());
		this.setVisible(true);
	} //fin de la méthode initComponent();
	
/******************************************************************************************************/

	
	 private void boutonAnnulerActionPerformed(ActionEvent evt)  //Action de bouton Annuler
	 {
		 passwordFirst.setText("");
		 passwordConfirm.setText("");
		 champMatricule.setText("");
		 champNom.setText("");
		 champPrenom.setText("");
		 comboBox3.removeAllItems();
	     comboBox4.removeAllItems();
		 
	 }
	 
/******************************************************************************************************/

	 private void boutonPersonnaliserActionPerformed(ActionEvent evt) // action de bouton Personnaliser
	    {
	    	if(!siVide())
	    	{
		JOptionPane.showMessageDialog(null, "Remplissez tout le formulaire SVP", "Erreur !", JOptionPane.ERROR_MESSAGE);
		return;
	    	}
	    	if(!champMatches())
	    	{
	    JOptionPane.showMessageDialog(null, "Caractères non valide dans les champs indiqués", "Erreur !", JOptionPane.ERROR_MESSAGE);
	    	return ;
	    	}
	    	if(!PINIdentique())
	    	{
	    		passwordFirst.setText("");
	   		    passwordConfirm.setText("");
	   JOptionPane.showMessageDialog(null, "Entrez le même PIN SVP", "Erreur !", JOptionPane.ERROR_MESSAGE);
	   		return ;
	    	}
	    	
	    	if(!longueurPINValide())
	    	{
	    		passwordFirst.setText("");
	   		    passwordConfirm.setText("");
	    		JOptionPane.showMessageDialog(null, "La longueur du PIN doit être de 6 chiffres", "Erreur !", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
	    	if(!matriculeValide())
	    	{
	    		JOptionPane.showMessageDialog(null, "Matricule invalide", "Erreur !", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
	   /************** le formulaire est correct *******************/ 	
	    	
	    	try {
	    			String pwd = new String(passwordFirst.getPassword());
	    			if(CardManagement.init(pwd, champMatricule.getText(), champNom.getText(), champPrenom.getText(), comboBox4.getSelectedItem() + "." + comboBox3.getSelectedItem(),0))
	    			{
	    				JOptionPane.showMessageDialog(null,"La carte a été personnalisée avec succès", "opération réussie", JOptionPane.INFORMATION_MESSAGE);
	    				CardManagement.clean("C:\\E_docs_tool\\E_docs_Admin_tool\\privateKey_store");
	    				CardManagement.clean("C:\\E_docs_tool\\E_docs_Admin_tool\\request_store");
	    				CardManagement.clean("C:\\E_docs_tool\\E_docs_Admin_tool\\certificat_store");
	    				
	    			}
			  } 
	    	catch (Exception e) 
			{
            JOptionPane.showMessageDialog(null,"Exception par la méthode boutonPersonnaliserActionPerformed()" + e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);
            
			}
	    	
	    	
	    }
	 
/******************************************************************************************************/
	 
	 private boolean siVide() // return true ssi tt les champs sont remplis 
	 {
		 String pwd1 = new String(passwordFirst.getPassword());
		 String pwd2 = new String(passwordConfirm.getPassword());
		 if(
		    pwd1.equals("") ||
	        pwd2.equals("") ||
		    champMatricule.getText().equals("")||
		    champNom.getText().equals("")||
		    champPrenom.getText().equals("")||
		    comboBox2.getSelectedItem().equals("")||
		    comboBox3.getSelectedItem().equals("") ||
		    comboBox4.getSelectedItem().equals("")
		 )
		 {
			 return false ;
		 }
		 else
		 {
			 return true;
		 }
			 
	 }// fin de la méthode siVide()
	 
/******************************************************************************************************/

	 
	 private boolean champMatches() // return true ssi tt les champs sont bien remplis
	 {
		 String pwd1 = new String(passwordFirst.getPassword());
		 String pwd2 = new String(passwordConfirm.getPassword());
		 boolean bool1 = pwd1.matches("^[\\d]+$"); // les chiffres seulement
		 boolean bool2 = pwd2.matches("^[\\d]+$");
		 boolean bool3 = champMatricule.getText().matches("^[\\d]+$");
		 boolean bool4 = champNom.getText().matches("^[a-zA-Z]+$"); // alphabet seulement
		 boolean bool5 = champPrenom.getText().matches("^[a-zA-Z]+$");
		 if(bool1 && bool2 && bool3 && bool4 && bool5)
		 {
			 
			 passwordFirst.setBorder(BorderFactory.createLineBorder(Color.black));
			 passwordConfirm.setBorder(BorderFactory.createLineBorder(Color.black));
			 champMatricule.setBorder(BorderFactory.createLineBorder(Color.black));
			 champNom.setBorder(BorderFactory.createLineBorder(Color.black));
			 champPrenom.setBorder(BorderFactory.createLineBorder(Color.black));
			 return true;
		 }
		 else
		 { 
			 if(!bool1){
			    	passwordFirst.setBorder(BorderFactory.createLineBorder(Color.RED));
			    	passwordFirst.setText("");
			    	}//colorer les champs invalides avec le rouge
				 if(!bool2){
					 passwordConfirm.setBorder(BorderFactory.createLineBorder(Color.RED));
					 passwordConfirm.setText("");
					 }
		 if(!bool3)champMatricule.setBorder(BorderFactory.createLineBorder(Color.RED));
		 if(!bool4)champNom.setBorder(BorderFactory.createLineBorder(Color.RED));
		 if(!bool5)champPrenom.setBorder(BorderFactory.createLineBorder(Color.RED));
			 return false;
		 }
		 
	 }
/******************************************************************************************************/

	private boolean PINIdentique() //  return true ssi les deux PINs sont identique
	{
		String pwd1 = new String(passwordFirst.getPassword());
		 String pwd2 = new String(passwordConfirm.getPassword());
		 if(pwd1.equals(pwd2))
		 {
			 return true;
			 
		 }
		 else
		 {
			 passwordFirst.setBorder(BorderFactory.createLineBorder(Color.RED)); //colorer les champs invalide avec le rouge
			 passwordConfirm.setBorder(BorderFactory.createLineBorder(Color.RED));
			 return false ;
		 }
	}
/******************************************************************************************************/
 
	private boolean longueurPINValide() // vérifier si la longueur de PIN est valide (=6)
	 {
		 String pwd1=new String(passwordFirst.getPassword());
		 if(pwd1.length()!=6)
			 return false;
		 else
			 return true;
	 }
/******************************************************************************************************/

	 
	 private boolean matriculeValide() // vérifier si le matricule est valide
	 {
		 Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
		 
		 String mat=new String(champMatricule.getText());
		 if(mat.length()<12) //Vérifier la longueur du matricule
			 return false;

		 //Récupérer l'année du BAC et vérifier
		 String matAnn="";
		 for(int i=0;i<4;i++)
		 matAnn+=(char)(mat.charAt(i));
		 
		 int matAnnC=Integer.parseInt(matAnn);
		 
		 if(matAnnC>cal.get(Calendar.YEAR) || matAnnC <(cal.get(Calendar.YEAR)-10))
			 return false;
			 
		 return true;
		 
	 }

	/******************************************************************************************************/

	
	private JLabel labelFiliere;
	
	private JLabel labelPIN;
	private JPasswordField  passwordFirst;
	private JLabel labelConfirmPIN;
	private JPasswordField  passwordConfirm;
	private JLabel labelMatricule;
	private JTextField champMatricule;
	private JLabel labelNom;
	private JTextField champNom;
	private JLabel labelPrenom;
	private JTextField champPrenom;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox1;
	private JLabel labelCycle;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox2;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox3;
	private JLabel labelNiveau;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox4;
	private JButton boutonPersonnaliser;
	private JButton boutonAnnuler;
}
