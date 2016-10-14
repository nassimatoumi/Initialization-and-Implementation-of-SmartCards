/*
 * Created by JFormDesigner on Fri May 01 21:35:49 GMT+01:00 2015
 */

package adminTool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * @author bekkouche oussama
 */
public class NewPIN extends JFrame {
	/**
	 * Permet de modifier le PIN de la carte
	 */
	private static final long serialVersionUID = 3827938566768766396L;
	public NewPIN() 
	{
		initComponents(); //initialisation des objets graphique
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - bekkouche oussama
		labelNewPIN = new JLabel();
		passwordFirst = new JPasswordField();
		labelNewPINConfirm = new JLabel();
		passwordConfirme = new JPasswordField();
		boutonReinitialiser = new JButton();
		boutonAnnuler = new JButton();

		//======== this ========
		setTitle("Reinitialiser le PIN");
		Container contentPane = getContentPane();
		contentPane.setBackground(new Color(1,152,117));
		this.setTitle("Réinitialisation de PIN");
		setIconImage(new ImageIcon("logo.png").getImage());

		
		passwordFirst.setHorizontalAlignment(JTextField.CENTER);
		passwordConfirme.setHorizontalAlignment(JTextField.CENTER);

		//---- labelNewPIN ----
		labelNewPIN.setText("         Entrez le nouveau PIN :         ");
		labelNewPIN.setForeground(new Color (208,208,208));
		labelNewPIN.setFont(new Font("Aleo-Regular", 0, 20));

		//---- labelNewPINConfirm ----
		labelNewPINConfirm.setText("         Confirmez le nouveau PIN :");
		labelNewPINConfirm.setForeground(new Color (208,208,208));
		labelNewPINConfirm.setFont(new Font("Aleo-Regular", 0, 20));

		//---- boutonReinitialiser ----
		boutonReinitialiser.setText("Reinitialiser");
		boutonReinitialiser.setBorderPainted(false);
		boutonReinitialiser.setFocusPainted(false);
		boutonReinitialiser.setBackground(new Color(44, 62, 80));
		boutonReinitialiser.setForeground(new Color(208, 208, 208));
		boutonReinitialiser.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonReinitialiserActionPerformed(evt);
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

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(labelNewPIN)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(passwordFirst, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(labelNewPINConfirm)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(passwordConfirme, GroupLayout.PREFERRED_SIZE, 65, GroupLayout.PREFERRED_SIZE))))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(125, 125, 125)
							.addComponent(boutonReinitialiser)
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addComponent(boutonAnnuler)))
					.addContainerGap(45, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(labelNewPIN)
						.addComponent(passwordFirst, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(labelNewPINConfirm)
						.addComponent(passwordConfirme, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(boutonReinitialiser)
						.addComponent(boutonAnnuler))
					.addContainerGap(12, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
		setVisible(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
	}
	
	/***********************************************************************************************/	 

	 private void boutonReinitialiserActionPerformed(ActionEvent evt) //action de bouton Reinitialiser
	 {
		 if(!siVide())
	    	{
		JOptionPane.showMessageDialog(null, "Remplissez tous les deux champs SVP", "Erreur !", JOptionPane.ERROR_MESSAGE);
		return;
	    	}
	    	if(!champMatches())
	    	{
	    		passwordFirst.setText("");
	   		 passwordConfirme.setText("");
	    JOptionPane.showMessageDialog(null, "Caractères non valides dans les champs indiqués", "Erreur !", JOptionPane.ERROR_MESSAGE);
	    	return ;
	    	}
	    	if(!PINIdentique())
	    	{
	    		passwordFirst.setText("");
	   		 passwordConfirme.setText("");
	   JOptionPane.showMessageDialog(null, "Entrez le même PIN SVP", "Erreur !", JOptionPane.ERROR_MESSAGE);
	   		return ;
	    	}
	    	if(!longueurPINValide())
	    	{
	    		passwordFirst.setText("");
	   		 passwordConfirme.setText("");
	    		JOptionPane.showMessageDialog(null, "La longueur du PIN ne doit pas dépasser 8 chiffres", "Erreur !", JOptionPane.ERROR_MESSAGE);
	    		return;
	    	}
	    	
	 	   /************** le formulaire est correct *******************/
	    	
	    try {
	    		String PIN = new String(passwordFirst.getPassword());
			    CardManagement.update(PIN);
			    JOptionPane.showMessageDialog(null, "Réinitialisation du PIN réussie","Réussi",JOptionPane.INFORMATION_MESSAGE);
			  } 
	    	catch (Exception e) 
			{
               JOptionPane.showMessageDialog(null,"Exception par la méthode boutonPersonnaliserActionPerformed() " + e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);
			}
	    	

	 }
	 
/***********************************************************************************************/	 

	 private void boutonAnnulerActionPerformed(ActionEvent evt) //action de bouton Annuler
	 {
		 passwordFirst.setText("");
		 passwordConfirme.setText("");
	 }

/***********************************************************************************************/	 

	 private boolean siVide() //return true ssi tt les champs sont remplis
	 {
		 String pwd1 = new String(passwordFirst.getPassword());
		 String pwd2 = new String(passwordConfirme.getPassword());
		 if(pwd1.equals("") || pwd2.equals("") )
		 {
			 return false ;
		 }
		 else
		 {
			 return true;
		 }
			 
	 }// fin de la méthode siVide()
	 
 /***********************************************************************************************/	 

	 
	 private boolean longueurPINValide() //  return true ssi les deux PINs sont identique
	 {
		 String pwd1=new String(passwordFirst.getPassword());
		 if(pwd1.length()>8)
			 return false;
		 else
			 return true;
	 }
	 
/***********************************************************************************************/	 

	 private boolean champMatches() // return true ssi tt les champs sont bien remplis
	 {
		 String pwd1 = new String(passwordFirst.getPassword());
		 String pwd2 = new String(passwordConfirme.getPassword());
		 boolean bool1 = pwd1.matches("^[\\d]+$"); // les chiffres seulement
		 boolean bool2 = pwd2.matches("^[\\d]+$");
		
		 if(bool1 && bool2)
		 {
			 
			 passwordFirst.setBorder(BorderFactory.createLineBorder(Color.black));
			 passwordConfirme.setBorder(BorderFactory.createLineBorder(Color.black));
			 
			 return true;
		 }
		 else
		 { 
	    if(!bool1)passwordFirst.setBorder(BorderFactory.createLineBorder(Color.RED)); //colorer les champs invalide avec le rouge
		 if(!bool2)passwordConfirme.setBorder(BorderFactory.createLineBorder(Color.RED));
		 
			 return false;
		 }
		 
	 }
 /***********************************************************************************************/	 
 
	 private boolean PINIdentique()  //  return true ssi les deux PINs sont identique
		{
			String pwd1 = new String(passwordFirst.getPassword());
			 String pwd2 = new String(passwordConfirme.getPassword());
			 if(pwd1.equals(pwd2))
			 {
				 return true;
			 }
			 else
			 {
				 passwordFirst.setBorder(BorderFactory.createLineBorder(Color.RED)); //colorer les champs invalide avec le rouge
				 passwordConfirme.setBorder(BorderFactory.createLineBorder(Color.RED));
				 return false ;
			 }
		}
 /***********************************************************************************************/	 


	private JLabel labelNewPIN;
	private JPasswordField passwordFirst;
	private JLabel labelNewPINConfirm;
	private JPasswordField passwordConfirme;
	private JButton boutonReinitialiser;
	private JButton boutonAnnuler;
}

