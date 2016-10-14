
package adminTool;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class Renouveler extends JFrame {
	/**
	 * permet de renouveler le cetificat de la carte et la clé privée et la filiere
	 */
	private static final long serialVersionUID = 1L;
	public Renouveler() {
		try{
			
			Filiere = CardManagement.getFiliere();
			}
		catch(Exception e)
			{
			e.printStackTrace();
			}
		initComponents();
		
		
	}// fin de constrecteur

	@SuppressWarnings("rawtypes")
	private void initComponents() { //initialisation des objets graphique
		labelLastYear = new JLabel();
		labelThisYear = new JLabel();
		comboBox1 = new JComboBox();
		separator1 = new JSeparator();
		labelWelcome = new JLabel();
		separator2 = new JSeparator();
		separator3 = new JSeparator();
		boutonRenouveler = new JButton();
		boutonAnnuler = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		setIconImage(new ImageIcon("logo.png").getImage());
		this.getContentPane().setBackground(new Color(103,65,114));
		this.setResizable(false);
		this.setTitle("Renouveler une carte E-docs");



		//---- labelLastYear ----
		String aff = "Niveau et sp\u00e9cialit\u00e9 actuels :  " + Filiere ;
		labelLastYear.setText(aff);
		labelLastYear.setForeground(new Color (208,208,208));
		labelLastYear.setFont(new Font("Calibri", 0, 20));


		//---- labelThisYear ----
		labelThisYear.setText("Sp\u00e9cialit\u00e9s possibles pour cette ann\u00e9e :  ");
		labelThisYear.setForeground(new Color (208,208,208));
		labelThisYear.setFont(new Font("Calibri", 0, 20));
		
		setComboBoxItem(Filiere);
		//---- labelWelcome ----
		labelWelcome.setText("Renouvellement");
		labelWelcome.setForeground(new Color (208,208,208));
		labelWelcome.setFont(new Font("Aleo-Regular",0,35));

		//---- boutonRenouveler ----
		boutonRenouveler.setText("Renouveler");
		boutonRenouveler.setBorderPainted(false);
		boutonRenouveler.setFocusPainted(false);
		boutonRenouveler.setBackground(new Color(44, 62, 80));
		boutonRenouveler.setForeground(new Color(208, 208, 208));
		 boutonRenouveler.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonRenouvelerActionPerformed(evt);
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
	               boutonAnuulerActionPerformed(evt);
	           }
	       });

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
					.addContainerGap(0, Short.MAX_VALUE)
					.addComponent(labelWelcome, GroupLayout.PREFERRED_SIZE, 265, GroupLayout.PREFERRED_SIZE)
					.addGap(160, 160, 160))
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(22, 22, 22)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(separator1, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(labelThisYear)
									.addGap(18, 18, 18)
									.addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE))
								.addComponent(labelLastYear, GroupLayout.PREFERRED_SIZE, 395, GroupLayout.PREFERRED_SIZE))
							.addGap(35, 35, 35))
						.addComponent(separator3, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
						.addComponent(separator2, GroupLayout.PREFERRED_SIZE, 550, GroupLayout.PREFERRED_SIZE)
						.addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
							.addGap(104, 104, 104)
							.addComponent(boutonRenouveler, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addGap(36, 36, 36)
							.addComponent(boutonAnnuler, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(23, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(labelWelcome)
					.addGap(13, 13, 13)
					.addComponent(separator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
					.addGap(3, 3, 3)
					.addComponent(labelLastYear)
					.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
					.addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(labelThisYear)
						.addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18, 18, 18)
					.addComponent(separator3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(boutonAnnuler)
						.addComponent(boutonRenouveler))
					.addContainerGap(21, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
		this.setVisible(true);

	}
	
	
	private void boutonRenouvelerActionPerformed(ActionEvent evt) //Action de bouton Renouveler
	{
		try {
			
			CardManagement.updateFiliere((String) comboBox1.getSelectedItem());
			
			if(CardManagement.init("","","","","",1))
				JOptionPane.showMessageDialog(null, "Renouvellement effectué." , "Réussi !", JOptionPane.INFORMATION_MESSAGE);
			 CardManagement.clean("C:\\E_docs_tool\\E_docs_Admin_tool\\certificat_store");
		  	CardManagement.clean("C:\\E_docs_tool\\E_docs_Admin_tool\\request_store");
			CardManagement.clean("C:\\E_docs_tool\\E_docs_Admin_tool\\privateKey_store");	
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
/***********************************************************************************************/	
	private void boutonAnuulerActionPerformed(ActionEvent evt) //Action de bouton Anuler
	{
		this.dispose();
	}
	
/***********************************************************************************************/	

	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadListToComboBox(JComboBox combo, String [] list) //ajouter les éléments d'une tableau e string a un comboBox
	{
		combo.removeAllItems();
		for(String i : list)
		{
			combo.addItem(i);
		}
	}
/***********************************************************************************************/	

	private void setComboBoxItem(String niveau) // MAJ de comboBox
	{
		String [] L2_GTR = {"L2.GTR","L3.GTR"};
		String [] L2_ACAD = {"L2.ACAD","L3.ACAD"};
		String [] L2_ISIL = {"L2.ISIL","L3.ISIL"};
		
		String [] L3_GTR = {"L3.GTR","M1.RSD","M1.SSI","M1.MIND","M1.MIV"};
		String [] L3_ACAD = {"L3.ACAD","M1.RSD","M1.SSI","M1.MIND","M1.MIV","M1.SII","M1.IL","M1.APCI"};
		String [] L3_ISIL = {"L3.ISIL","M1.RSD","M1.SSI","M1.MIND","M1.MIV","M1.SII","M1.IL","M1.APCI"};
		
		String [] M1_RSD = {"M1.RSD","M2.RSD"};
		String [] M1_SSI = {"M1.SSI","M2.SSI"};
		String [] M1_MIND = {"M1.MIND","M2.MIND"};
		String [] M1_MIV = {"M1.MIV","M2.MIV"};
		String [] M1_SII = {"M1.SII","M2.SII"};
		String [] M1_IL = {"M1.IL","M2.IL"};
		String [] M1_APCI = {"M1.APCI","M2.APCI"};
		
		String [] M2_RSD = {"M2.RSD"};
		String [] M2_SSI = {"M2.SSI"};
		String [] M2_MIND = {"M2.MIND"};
		String [] M2_MIV = {"M2.MIV"};
		String [] M2_SII = {"M2.SII"};
		String [] M2_IL = {"M2.IL"};
		String [] M2_APCI = {"M2.APCI"};


		if(niveau.equals("L2.GTR")){loadListToComboBox(comboBox1,L2_GTR);}
		if(niveau.equals("L2.ACAD")){loadListToComboBox(comboBox1,L2_ACAD);}
		if(niveau.equals("L2.ISIL")){loadListToComboBox(comboBox1,L2_ISIL);}
		
		if(niveau.equals("L3.GTR")){loadListToComboBox(comboBox1,L3_GTR);}
		if(niveau.equals("L3.ACAD")){loadListToComboBox(comboBox1,L3_ACAD);}
		if(niveau.equals("L3.ISIL")){loadListToComboBox(comboBox1,L3_ISIL);}
		
		if(niveau.equals("M1.RSD")){loadListToComboBox(comboBox1,M1_RSD);}
		if(niveau.equals("M1.SSI")){loadListToComboBox(comboBox1,M1_SSI);}
		if(niveau.equals("M1.MIND")){loadListToComboBox(comboBox1,M1_MIND);}
		if(niveau.equals("M1.MIV")){loadListToComboBox(comboBox1,M1_MIV);}
		if(niveau.equals("M1.SII")){loadListToComboBox(comboBox1,M1_SII);}
		if(niveau.equals("M1.IL")){loadListToComboBox(comboBox1,M1_IL);}
		if(niveau.equals("M1.APCI")){loadListToComboBox(comboBox1,M1_APCI);}

		if(niveau.equals("M2.RSD")){loadListToComboBox(comboBox1,M2_RSD);}
		if(niveau.equals("M2.SSI")){loadListToComboBox(comboBox1,M2_SSI);}
		if(niveau.equals("M2.MIND")){loadListToComboBox(comboBox1,M2_MIND);}
		if(niveau.equals("M2.MIV")){loadListToComboBox(comboBox1,M2_MIV);}
		if(niveau.equals("M2.SII")){loadListToComboBox(comboBox1,M2_SII);}
		if(niveau.equals("M2.IL")){loadListToComboBox(comboBox1,M2_IL);}
		if(niveau.equals("M2.APCI")){loadListToComboBox(comboBox1,M2_APCI);}
	
	}
/***********************************************************************************************/	

	private JLabel labelLastYear;
	private JLabel labelThisYear;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBox1;
	private JSeparator separator1;
	private JLabel labelWelcome;
	private JSeparator separator2;
	private JSeparator separator3;
	private JButton boutonRenouveler;
	private JButton boutonAnnuler;
	private String Filiere;
}
