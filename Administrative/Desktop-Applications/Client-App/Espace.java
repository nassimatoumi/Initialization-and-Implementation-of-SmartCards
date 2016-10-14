/*
 * 
 */

package e_docs;

import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.smartcardio.CardException;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;


/**
 * @author bekkouche oussama
 */
public class Espace extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	class ItemAction implements ActionListener
	{
		@SuppressWarnings("unchecked")
		public void actionPerformed(ActionEvent e)
		{
		  if(comboDownload.getSelectedItem().equals("Relevé des notes"))
			 {    
			  comboYear.removeAllItems();
			  setComboBoxItem(champFiliere.getText()); 
			  boutonDownload.setEnabled(true);
			  return ;
			 }
			
		   if(comboDownload.getSelectedItem().equals("Emploi du temps"))
		   {     
				  comboYear.removeAllItems();
				  comboYear.addItem(champFiliere.getText());
				  boutonDownload.setEnabled(true);
				  return;

		   }
		   if(comboDownload.getSelectedItem().equals(""))
		   {     
				  comboYear.removeAllItems();
				  boutonDownload.setEnabled(false);
				  return;

		   }
		}
	}
	
	
/*************************************************************************************************************/	
	public Espace() { 
		initComponents();
		champNom.setText(CardManagement.getInfo((byte)0x02)); // remplissage des champs
		champPrenom.setText(CardManagement.getInfo((byte)0x03));
		champMatricule.setText(CardManagement.getInfo((byte)0x01));
		champFiliere.setText(CardManagement.getInfo((byte)0x04));	
		connectToDP();
	}
/*************************************************************************************************************/	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponents() {

		scrollPane1 = new JScrollPane();
		textArea1 = new JTextArea();
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();
		champNom = new JTextField();
		champPrenom = new JTextField();
		champMatricule = new JTextField();
		champFiliere = new JTextField();
		comboDownload = new JComboBox();
		label6 = new JLabel();
		comboYear = new JComboBox();
		boutonDownload = new JButton();
		boutonOpen = new JButton();
		boutonDisconnect = new JButton();

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setBackground(new Color(208,208,208));
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("E-Docs USTHB");

		//======== scrollPane1 ========
		{
			scrollPane1.setViewportView(textArea1);
		}

		//---- label1 ----
		label1.setText("Nom :");

		//---- label2 ----
		label2.setText("Pr\u00e9nom :");

		//---- label3 ----
		label3.setText("Matricule : ");

		//---- label4 ----
		label4.setText("Fili\u00e8re :       ");

		//---- label5 ----
		label5.setText("T\u00e9l\u00e9chargement :");

		//---- label6 ----
		label6.setText("Ann\u00e9e :");

		//---- boutonDownload ----
		boutonDownload.setText("T\u00e9l\u00e9charger");
		boutonDownload.setEnabled(false);
		boutonDownload.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonDownloadActionPerformed(evt);
	           }
	       });

		//---- boutonOpen ----
		boutonOpen.setText("Ouvrir le dossier de destination");
		boutonOpen.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonOpenActionPerformed(evt);
	           }
	       });

		//---- boutonDisconnect ----
		boutonDisconnect.setText("D\u00e9connexion");
		boutonDisconnect.addActionListener(new ActionListener() {
	           public void actionPerformed(ActionEvent evt) {
	               boutonDisconnectActionPerformed(evt);
	           }
	       });
		//-----comboDownload--------
		
		comboDownload.addItem("");
		comboDownload.addItem("Relevé des notes");
		comboDownload.addItem("Emploi du temps");
		comboDownload.addActionListener(new ItemAction ());
		
		champNom.setEditable(false);
		champPrenom.setEditable(false);
		champMatricule.setEditable(false);
		champFiliere.setEditable(false);
		
		JLabel lab= new JLabel(new ImageIcon("Accueil.png"));
		
		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(0, 0, 0)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(boutonDownload, GroupLayout.PREFERRED_SIZE, 130, GroupLayout.PREFERRED_SIZE)
									.addGap(18, 18, 18)
									.addComponent(boutonOpen, GroupLayout.PREFERRED_SIZE, 245, GroupLayout.PREFERRED_SIZE))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(lab, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE)
									.addGap(9, 9, 9)
									.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(label4)
											.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(champFiliere, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE))
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(label5)
											.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(comboDownload, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
											.addGap(18, 18, 18)
											.addComponent(label6, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(comboYear, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(label3)
											.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(champMatricule, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
										.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
											.addGroup(contentPaneLayout.createSequentialGroup()
												.addComponent(label1)
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(champNom, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
											.addGroup(contentPaneLayout.createSequentialGroup()
												.addComponent(label2)
												.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(champPrenom, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE)))))))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(279, 279, 279)
							.addComponent(boutonDisconnect, GroupLayout.PREFERRED_SIZE, 203, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(43, Short.MAX_VALUE))
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(50, 50, 50)
							.addComponent(lab, GroupLayout.PREFERRED_SIZE, 199, GroupLayout.PREFERRED_SIZE))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addGap(47, 47, 47)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label1)
								.addComponent(champNom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label2)
								.addComponent(champPrenom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label3)
								.addComponent(champMatricule, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label4)
								.addComponent(champFiliere, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(18, 18, 18)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(label5)
								.addComponent(comboDownload, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(label6)
								.addComponent(comboYear, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
					.addGap(0, 0, 0)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(boutonOpen, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(boutonDownload, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(42, 42, 42)
					.addComponent(boutonDisconnect)
					.addContainerGap(67, Short.MAX_VALUE))
		);
		pack();
		setLocationRelativeTo(getOwner());
		this.setVisible(true);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
	}
/*************************************************************************************************************/	

 private void boutonDownloadActionPerformed(ActionEvent evt)

    {
	   String Query = "";
	   String mat = champMatricule.getText();
	   String objet = "";
	   
	   if(comboDownload.getSelectedItem().equals("Relevé des notes"))
	   {
		   if(comboYear.getSelectedItem().equals("L1"))
		   {
			   objet = "rel_1";
			   Query = "SELECT + " +objet+"" + " FROM UsthbBDD.dbo.DepINF" + " WHERE mat = "+mat+" ";
		   }
		   else if(comboYear.getSelectedItem().equals("L2"))
		   {
			   objet = "rel_2";
			   Query = "SELECT + " +objet+"" + " FROM UsthbBDD.dbo.DepINF" + " WHERE mat = "+mat+" ";
		   }
		   else if(comboYear.getSelectedItem().equals("L3"))
		   {
			   objet = "rel_3";
			   Query = "SELECT + " +objet+"" + " FROM UsthbBDD.dbo.DepINF" + " WHERE mat = "+mat+" ";
		   }
		   else if(comboYear.getSelectedItem().equals("M1"))
		   {
			   objet = "rel_4";
			   Query = "SELECT + " +objet+"" + " FROM UsthbBDD.dbo.DepINF" + " WHERE mat = "+mat+" ";
		   }
		   else if(comboYear.getSelectedItem().equals("M2"))
		   {
			   objet = "rel_5";
			   Query = "SELECT + " +objet+"" + " FROM UsthbBDD.dbo.DepINF" + " WHERE mat = "+mat+" ";
		   }
		   	   
	   }
	   else if(comboDownload.getSelectedItem().equals("Emploi du temps"))
	   {
		   objet ="EDT";
		   Query = "SELECT + "+objet+"" + " FROM UsthbBDD.dbo.DepINF" + " WHERE mat = "+mat+" ";

	   }
	   	   
	   try
	   {
		   
		ResultSet rs = st.executeQuery(Query);
		while (rs.next())  
        {   
			 byte [] myFile = rs.getBytes(1);
			 try {
				if(myFile != null)
				{
				OutputStream targetFile=  new FileOutputStream("C:\\E-Docs\\E-Docs_File\\Downloads\\" + objet +"_"+mat+".pdf");
				targetFile.write(myFile);
				 targetFile.close();
				}
				else
				{
				JOptionPane.showMessageDialog(null, "Fichier Introuvable" , "Problème !", JOptionPane.ERROR_MESSAGE);
				}
				
			} 
			 catch ( IOException e) {
				
				e.printStackTrace();
			}
        }
		 con.commit();
		
	   } 
	   catch (SQLException e)
	   {
		e.printStackTrace();
	   }
    	
    }

 /*************************************************************************************************************/	

private void boutonOpenActionPerformed(ActionEvent evt)
{
	try {
		Desktop.getDesktop().open(new File("C:\\E-Docs\\E-Docs_File\\Downloads"));
	} catch (IOException e) {
		e.printStackTrace();
	}
}
 
/*************************************************************************************************************/	

private void boutonDisconnectActionPerformed(ActionEvent evt)
{
	try {
		st.close();
		deleteAllFile();
		CardManagement.c.disconnect(true);
		System.exit(1);
		this.dispose();
	} catch (SQLException | CardException e) {
		e.printStackTrace();
	}
}

/*************************************************************************************************************/	

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void loadListToComboBox(JComboBox combo, String [] list) // remplir un comboBox avec un tableau strinf
	{
		combo.removeAllItems();
		for(String i : list)
		{
			combo.addItem(i);
		}
	}
/*************************************************************************************************************/	
	
	private void setComboBoxItem(String Filiere) // Remplie un comboBox selon la Filiere
	{
		String niveau = ""; 
		niveau = niveau + Filiere.charAt(0);
		niveau = niveau + Filiere.charAt(1);
		
	    String [] L2_List = {"L1","L2"};
	    String [] L3_List = {"L1","L2","L3"};
	    String [] M1_List = {"L1","L2","L3","M1"};
	    String [] M2_List = {"L1","L2","L3","M1","M2"};

		if(niveau.equals("L2")){loadListToComboBox(comboYear,L2_List);return;}
		if(niveau.equals("L3")){loadListToComboBox(comboYear,L3_List);return;}
		if(niveau.equals("M1")){loadListToComboBox(comboYear,M1_List);return;}
		if(niveau.equals("M2")){loadListToComboBox(comboYear,M2_List);return;}



		
	}
/*************************************************************************************************************/	
    private void deleteAllFile()
    {
    	  File dossier = new File("C:\\E-Docs\\E-Docs_File\\Downloads\\");
    	  if( dossier.exists() )
    	  {
    		  File[] files = dossier.listFiles();
    		  for( int i = 0 ; i < files.length ; i++ )
    		  {
    			  files[ i ].delete();
    		  }

    	  }
    }
    
 /*************************************************************************************************************/	

	private void connectToDP()
	{
		 
		try 
		 {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectionUrl = "jdbc:sqlserver://localhost;"
					+ "databaseName=UsthbBDD;user=sa;password=PFE2015nchlh;"
					+ "encrypt=true;trustServerCertificate=true;"; 
			  con = DriverManager.getConnection(connectionUrl); 
			  st = con.createStatement();
			

		} 
		 catch (ClassNotFoundException | SQLException e)
		 {
				JOptionPane.showMessageDialog(null, "Problème lors de la connexion" + e.getCause() , "Problème !", JOptionPane.ERROR_MESSAGE);
		 }
		 
	}
/*************************************************************************************************************/	

	private JScrollPane scrollPane1;
	private JTextArea textArea1;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JLabel label4;
	private JLabel label5;
	private JTextField champNom;
	private JTextField champPrenom;
	private JTextField champMatricule;
	private JTextField champFiliere;
	@SuppressWarnings("rawtypes")
	private JComboBox comboDownload;
	private JLabel label6;
	@SuppressWarnings("rawtypes")
	private JComboBox comboYear;
	private JButton boutonDownload;
	private JButton boutonOpen;
	private JButton boutonDisconnect;
	
	private Connection con ;
	private Statement st ;
}

