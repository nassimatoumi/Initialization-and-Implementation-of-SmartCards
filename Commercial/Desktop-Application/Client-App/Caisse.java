package caisse;

import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;

/**
 * Interface graphique de la caisse
 * 
 *
 */
public class Caisse extends javax.swing.JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cheminListe;
    private int longueurEcran = 30;
    private double total = 0;
    private double prixScan;
    private boolean booleanSupprimer = false;
    private boolean booleanTotal = false;
    @SuppressWarnings("unused")
	private boolean booleanScan = true;
    private boolean booleanPayer = false;
    private boolean booleanImprimer = false;
    private boolean booleanNew = true;
    private boolean booleanCard = false; 
    private boolean booleanFirstConnect = true ; 
    @SuppressWarnings("rawtypes")
	private ArrayList listeArticles;
    private int numTicket = 1;
    private int crntBalance ;
    int year, mois, day, hour, minute;
    private String month;
    
    private String ticket="";
    
    Properties settings;
    private boolean booleanSettings = true;

    
    /** Constructeur de la fenêtre. */
    public Caisse(String cheminListe) {
        this.cheminListe = cheminListe;
        listeArticles = new ArrayListListe(cheminListe);
        initComponents();

        // Chargement du fichier de configuration
        settings = new Properties();
        File cheminSettings = new File(System.getProperty("user.dir") + "/settings.txt");
        try
	{
            settings.load(new FileInputStream(cheminSettings));
            numTicket = Integer.parseInt(settings.getProperty("numTicket"));
            settings.setProperty("numTicket", ""+(numTicket+1)); // incrémenter
            settings.store(new FileOutputStream(cheminSettings), "Paramètres de la caisse"); //MAJ de settings
            tickParam.setSelected(true); //case  cocher
            champTicket.setText(""+numTicket); //champ de text
	}
        catch(IOException e)
        {
            booleanSettings = false;
        }
        
        TimerTask task = new TimerTask() //vérifier si la carte est présente 
    	{
    		@Override
    		public void run() 
    		{
    			booleanCard=CardManagement.verifyCardConnection();
    			if(booleanCard && booleanFirstConnect)  // la carte est dans le lecteur et non sélectionée
    			{
    				cardInfo.setText("Authentification .......");
    				CardManagement.select();
    				if(CardManagement.authentification())
    				{
    				cardInfo.setText(CardManagement.getInfo());
    				crntBalance = CardManagement.bal; 
    				boutonPayer.setEnabled(true);
    				boutonImprimer.setEnabled(true);
    				boutonRecharge.setEnabled(true);
    				boutonTotal.setEnabled(true);
    				}
    				else
    				{
    					cardInfo.setText("carte bloquée");
    					boutonPayer.setEnabled(false);
        				boutonImprimer.setEnabled(false);
        				boutonRecharge.setEnabled(false);
        				boutonTotal.setEnabled(false);
    				}
    				booleanFirstConnect = false ;
    				
    			}
    			else if(!booleanCard)
    				{
    				cardInfo.setText("Insérez une carte"); // la carte n'est pas dans le lecteur 
    				booleanFirstConnect = true ;
    				boutonPayer.setEnabled(false);
    				boutonImprimer.setEnabled(false);
    				boutonRecharge.setEnabled(false);
    				boutonTotal.setEnabled(false);

    				}
    		}	
    	};
    	
    	Timer timer = new Timer();
    	timer.scheduleAtFixedRate(task, 0, 1000);
    	
    
    }

    
    private void initComponents() {
    	setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Caisse (liste : " + cheminListe + ")");
        setName("mainWindow"); // NOI18N
        setResizable(false);
        JComboBoxListe = new JComboBoxListe(new ArrayListListe(cheminListe));
        boutonImprimer = new JButton();
		champTicket = new JTextField();
		labelClient = new JLabel();
		boutonRecharge = new JButton();
		champMontantRecharge = new JTextField();
		boutonPayer = new JButton();
		champArgent = new JTextField();
		boutonTotal = new JButton();
		labelEuro = new JLabel();
		labelEuro2=new JLabel();
		champTotal = new JTextField();
		champStatut = new JTextField();
		boutonSupprimer = new JButton();
		boutonAjouter = new JButton();
		JSpinnerQuantity = new JSpinner(new SpinnerNumberModel(1,1,10,1));
		JFormattedTextField tf = ((JSpinner.DefaultEditor)JSpinnerQuantity.getEditor()).getTextField();
		tf.setHorizontalAlignment(JFormattedTextField.CENTER);
		labelQuantity = new JLabel();
		labelArticle = new JLabel();
		labelParam = new JLabel();
		tickParam = new JCheckBox();
		cardInfo = new JTextArea();
		labLogo = new JLabel();
		labelPanier = new JLabel();
		panelCheckboxes = new JPanel();
		labPanier = new JLabel();
		separator1 = new JSeparator();
		separator2 = new JSeparator();
		
		cardInfo.setEditable(false);
		cardInfo.append("Insérez la carte ..............");
		
		
		JComboBoxListe.setMaximumSize(new java.awt.Dimension(180, 20));
        JComboBoxListe.setMinimumSize(new java.awt.Dimension(200, 20));
        JComboBoxListe.setPreferredSize(new java.awt.Dimension(180, 20));
        
		champTicket.setEditable(false);
		champArgent.setEditable(false);
		 champTotal.setEditable(false);
		 champStatut.setEditable(false);
		 champStatut.setText("En attente...");
		 
		 champMontantRecharge.setFont(new Font("Calibri",0,20));
		 
		 cardInfo.setBackground(new Color(208,208,208));

		//======== this ========
		Container contentPane = getContentPane();
		contentPane.setBackground(new Color(198,156,109));

		//---- boutonImprimer ----
		boutonImprimer.setText("Imprimer ticket");
		boutonImprimer.setBorderPainted(false);
		boutonImprimer.setFocusPainted(false);
		boutonImprimer.setBackground(new Color(44, 62, 80));
		boutonImprimer.setForeground(new Color(208, 208, 208));
		boutonImprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonImprimerActionPerformed(evt);
            }
        });

		//---- labelClient ----
		labelClient.setText("Ticket N\u00b0:");

		//---- boutonRecharge ----
		boutonRecharge.setText("Recharge");
		boutonRecharge.setBorderPainted(false);
		boutonRecharge.setFocusPainted(false);
		boutonRecharge.setBackground(new Color(44, 62, 80));
		boutonRecharge.setForeground(new Color(208, 208, 208));
		boutonRecharge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonRechargeActionPerformed(evt);
            }
        });

		//---- boutonPayer ----
		boutonPayer.setText("D\u00e9biter");
		boutonPayer.setBorderPainted(false);
		boutonPayer.setFocusPainted(false);
		boutonPayer.setBackground(new Color(44, 62, 80));
		boutonPayer.setForeground(new Color(208, 208, 208));
		boutonPayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonPayerActionPerformed(evt);
            }
        });

		//---- boutonTotal ----
		boutonTotal.setText("TOTAL");
		boutonTotal.setBorderPainted(false);
		boutonTotal.setFocusPainted(false);
		boutonTotal.setBackground(new Color(44, 62, 80));
		boutonTotal.setForeground(new Color(208, 208, 208));
		boutonTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonTotalActionPerformed(evt);
            }
        });

		//---- labelEuro ----
		labelEuro.setText("DA");
		labelEuro.setForeground(new Color(30,54,92));
		labelEuro2.setForeground(new Color(30,54,92));

		//---- boutonSupprimer ----
		boutonSupprimer.setText("Annulation");
		boutonSupprimer.setBorderPainted(false);
		boutonSupprimer.setFocusPainted(false);
		boutonSupprimer.setBackground(new Color(44, 62, 80));
		boutonSupprimer.setForeground(new Color(208, 208, 208));
		boutonSupprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonSupprimerActionPerformed(evt);
            }
        });

		//---- boutonAjouter ----
		boutonAjouter.setText("Ajouter au panier");
		boutonAjouter.setBorderPainted(false);
		boutonAjouter.setFocusPainted(false);
		boutonAjouter.setBackground(new Color(44, 62, 80));
		boutonAjouter.setForeground(new Color(208, 208, 208));
		boutonAjouter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                boutonAjouterActionPerformed(evt);
            }
        });

		//---- labelQuantity ----
		labelQuantity.setText("Quantit\u00e9 :");
		labelQuantity.setForeground(new Color(30,54,92));

		//---- labelArticle ----
		labelArticle.setText("Article :");
		labelArticle.setForeground(new Color(30,54,92));

		//---- labelParam ----
		labelParam.setText("Param\u00e8tres actifs :");
		labelParam.setForeground(new Color(30,54,92));

		//---- tickParam ----
		tickParam.setText("text");
		tickParam.setOpaque(false);
		
		
		achatsEff1.setFont(new Font("Calibri",0,20));
		achatsEff1.setForeground(new Color(30,54,92));
		
		labelPanier.setFont(new Font("Calibri",0,20));
		labelPanier.setForeground(new Color(30,54,92));
		
		MontAch.setFont(new Font("Calibri",0,20));
		MontAch.setForeground(new Color(30,54,92));
		montAch.setFont(new Font("Calibri",0,20));
		montAch.setForeground(new Color(30,54,92));
		
		panAchats1.add(achatsEff1);
		panAchats1.setBackground(new Color(198,156,109));
		panAchats2.setBackground(new Color(198,156,109));
		panAchats2.setLayout(new GridLayout(8,1));
		panMontAchat.add(MontAch);
		panMontAchat.add(montAch);
		panMontAchat.setBackground(new Color(198,156,109));
		

		labLogo.setIcon(new ImageIcon("MoneoResto.png"));
		labelPanier.setIcon(new ImageIcon("Panier.png"));
		labelPanier.setText("Aucun produit.");
		//======== panelCheckboxes ========
		{
			panelCheckboxes.setLayout(new BoxLayout(panelCheckboxes, BoxLayout.Y_AXIS));
			panelCheckboxes.add(panMontAchat);
			panelCheckboxes.add(panAchats2);
			
			panelCheckboxes.setBackground(new Color(208,208,208));
		}

		//---- labelEuro2 ----
				labelEuro2.setText("DA");

				GroupLayout contentPaneLayout = new GroupLayout(contentPane);
				contentPane.setLayout(contentPaneLayout);
				contentPaneLayout.setHorizontalGroup(
					contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(contentPaneLayout.createParallelGroup()
								.addComponent(labLogo, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addGap(1, 1, 1)
											.addComponent(labelParam, GroupLayout.PREFERRED_SIZE, 127, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(tickParam, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
										.addComponent(separator2, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE)
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addGroup(contentPaneLayout.createParallelGroup()
												.addComponent(champArgent, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE)
												.addComponent(champStatut, GroupLayout.PREFERRED_SIZE, 146, GroupLayout.PREFERRED_SIZE))
											.addGap(16, 16, 16)
											.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
												.addComponent(boutonPayer, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
												.addComponent(boutonTotal, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
												.addComponent(champTotal, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(labelEuro, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE))
										.addComponent(labelArticle, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE))
									.addGap(0, 0, Short.MAX_VALUE))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(separator1, GroupLayout.PREFERRED_SIZE, 248, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE))
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(boutonSupprimer, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
											.addGap(18, 18, 18)
											.addComponent(boutonAjouter, GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(champMontantRecharge, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(labelEuro2, GroupLayout.PREFERRED_SIZE, 16, GroupLayout.PREFERRED_SIZE)
											.addGap(6, 6, 6)
											.addComponent(boutonRecharge, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(labelQuantity, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
											.addGroup(contentPaneLayout.createParallelGroup()
												.addComponent(JComboBoxListe, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
												.addComponent(JSpinnerQuantity))))
									.addGap(16, 16, 16)))
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createParallelGroup()
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addGap(19, 19, 19)
										.addComponent(labelClient, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(champTicket, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(boutonImprimer, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE)
										.addGap(39, 39, 39))
									.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
										.addGap(6, 6, 6)
										.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
											.addComponent(cardInfo, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
											.addComponent(panelCheckboxes, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
											.addComponent(labelPanier, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
										.addGap(18, 18, 18)))
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addGap(2, 2, 2)
									.addComponent(labPanier, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
									.addGap(109, 109, 109))))
				);
				contentPaneLayout.setVerticalGroup(
					contentPaneLayout.createParallelGroup()
						.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
							.addGap(17, 17, 17)
							.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(labLogo, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
									.addGap(14, 14, 14)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(tickParam)
										.addComponent(labelParam))
									.addGap(18, 18, 18)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(boutonRecharge, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
										.addComponent(champMontantRecharge, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
										.addComponent(labelEuro2, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)))
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addComponent(cardInfo, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addComponent(labelPanier, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
							.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
							.addGroup(contentPaneLayout.createParallelGroup()
								.addGroup(contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addComponent(separator2, GroupLayout.PREFERRED_SIZE, 5, GroupLayout.PREFERRED_SIZE)
											.addGap(18, 18, 18)
											.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(labelArticle, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
												.addComponent(JComboBoxListe, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)))
										.addComponent(labPanier, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
									.addGap(18, 18, 18)
									.addComponent(labelQuantity, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 118, Short.MAX_VALUE)
									.addComponent(boutonTotal, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(champArgent, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
										.addComponent(boutonPayer, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
									.addGap(18, 18, 18))
								.addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
									.addGroup(contentPaneLayout.createParallelGroup()
										.addGroup(contentPaneLayout.createSequentialGroup()
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
											.addComponent(JSpinnerQuantity, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
											.addGap(18, 18, 18)
											.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(boutonSupprimer, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
												.addComponent(boutonAjouter, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(separator1, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
											.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(champTotal, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
												.addComponent(champStatut, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
												.addComponent(labelEuro, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
											.addGap(18, 18, 18))
										.addComponent(panelCheckboxes, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
									.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
									.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(boutonImprimer, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
										.addComponent(champTicket, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
										.addComponent(labelClient, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE))
									.addGap(26, 26, 26))))
				);
		pack();
		setIconImage(new ImageIcon("logo.png").getImage());
		setTitle("Moneo Resto USTHB");
		setLocationRelativeTo(getOwner());
		
	
    }
    
    
    
    
 
    @SuppressWarnings("unchecked")
	private void boutonAjouterActionPerformed(java.awt.event.ActionEvent evt) { // action de bouton ajouter
        if(booleanNew)//new ticket
        {
            Calendar now = new GregorianCalendar();
            year = now.get(Calendar.YEAR);
            mois = (now.get(Calendar.MONTH)+1);
            day = now.get(Calendar.DAY_OF_MONTH);
            hour = now.get(Calendar.HOUR_OF_DAY);
            minute = now.get(Calendar.MINUTE);
            month = ("" + mois);
            if(mois < 10)
            {
                month = ("0" + mois);
            }
            if(booleanSettings)
            {
                if(Boolean.valueOf(settings.getProperty("tickNomMagasin")).booleanValue())
                {
                   ticket+="******************************\r\n";
                   ticket+="* " + settings.getProperty("nomMagasin") + " *\r\n";
                   ticket+="******************************\r\n\r\n";
                }
            }
            booleanNew = false;
        }
        
        if(nbAchats<8)
        {
            if((int)(JSpinnerQuantity.getValue())==0)
            {
                JOptionPane.showMessageDialog(null, "La quantité doit être un nombre entier positif ! !", "Attention !", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
               
            	
            	//if(nbAchats==0)
					labelPanier.setText("");
					
                prixScan = (int)JSpinnerQuantity.getValue() * (Integer.parseInt((String)listeArticles.get(((JComboBoxListe.getSelectedIndex()+1)*2)+1)));
                prixScan = Math.round(prixScan * 100.00)/100.00;
                String articleScan = listeArticles.get((JComboBoxListe.getSelectedIndex()+1)*2) + "\r\n"; //nom de l'article
                String articleScan2 = (int)(JSpinnerQuantity.getValue()) + "x " + listeArticles.get(((JComboBoxListe.getSelectedIndex()+1)*2)+1) + "DA = " + prixScan + "DA";
                
                name.add(listeArticles.get((JComboBoxListe.getSelectedIndex()+1)*2));
                price.add(listeArticles.get(((JComboBoxListe.getSelectedIndex()+1)*2)+1));
				number.add((int)(JSpinnerQuantity.getValue()));
                boxes.add(new Checkbox(articleScan +" : "+ articleScan2 + ""));
                panAchats2.add((Checkbox) boxes.get(nbAchats));
                panAchats2.repaint();
				
				montantAch+=Integer.parseInt((String)price.get(nbAchats))*(int)number.get(nbAchats);
				nbAchats++;
				
				
				montAch.setText(Integer.toString(montantAch)+" DA");

                champStatut.setText(listeArticles.get((JComboBoxListe.getSelectedIndex()+1)*2) + " (x" + (int)(JSpinnerQuantity.getValue()) + ")");
                champTotal.setText(""+prixScan);
                booleanSupprimer = true;
                booleanTotal = true;
                booleanPayer = false;
                champArgent.setText("");
                champArgent.setEditable(false);
            }
        }
        else
        	JOptionPane.showMessageDialog(null,"Vous avez atteint le nombre maximal d'articles.", "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void boutonTotalActionPerformed(java.awt.event.ActionEvent evt) {
        if(booleanTotal)
        {
          total = Math.round(montantAch * 100.00)/100.00;
            String ligneTotal = total + "DA";
            while(ligneTotal.length() < longueurEcran)
                {
                    ligneTotal = " " + ligneTotal;
                }
            
            for(int h=0;h<number.size();h++)
            {
            	ticket+="\r\n "+name.get(h)+" :  "+price.get(h)+"*"+number.get(h)+"= "+Integer.parseInt((String)price.get(h))*(int)number.get(h)+"  DA\r\n";
            }
            ticket+="\r\nTOTAL\r\n" + ligneTotal + "\r\n----------\r\n";
            champStatut.setText("TOTAL");
            champTotal.setText(""+montantAch);
            champArgent.setText(""+montantAch);
            champArgent.setEditable(false);
            if(montantAch > crntBalance)
            {
                booleanPayer = false;

                JOptionPane.showMessageDialog(null, "Balance insuffisante", "Problème !", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                booleanPayer = true;
                booleanTotal = false;


            }
         
            booleanSupprimer = false;
        }
    }
/**
 * Méthode qui gère l'action du bouton Annulation.
 * Si on le peut, le dernier article est supprimé et le prix de l'article est déduit du total.
 */
    private void boutonSupprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonSupprimerActionPerformed
        if(booleanSupprimer)
        {
			
			
			int z=0;
			while(z<boxes.size())
			{
				if(((Checkbox)boxes.get(z)).getState())
				{
					panAchats2.remove((Checkbox)boxes.get(z));
					
					montantAch-=Integer.parseInt((String)price.get(z))*(int)number.get(z);
					price.remove(price.get(z));
					number.remove(number.get(z));
					boxes.remove(boxes.get(z));
					name.remove(name.get(z));
					
					nbAchats--;
					
				}
				else
					z++;
					
			}
			panAchats2.repaint();
			montAch.setText(Integer.toString(montantAch)+" DA");
			
			if(nbAchats==0)
			{
            booleanSupprimer = false;
            labelPanier.setText("Aucun produit.");
			}
        }
    }

    private void boutonPayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_boutonPayerActionPerformed
        if(booleanPayer)
        {
        		// booleanScan = false;
        		byte [] montantArray = shortToByteArray((short)montantAch); 
        		 CardManagement.debite(montantArray);
     			 cardInfo.setText(CardManagement.getInfo());
     			crntBalance = CardManagement.bal;
     			 total = 0;
 
                    // FIN DU TICKET
                    ticket+="______________________________\r\n";
                    if(booleanSettings)
                    {
                        if(Boolean.valueOf(settings.getProperty("tickNomCaisse")).booleanValue())
                        {                 
                            ticket+="Nom du caissier : " + settings.getProperty("nomCaisse") + "\r\n";
                        }
                    }
                    ticket+="Ticket N° : " + numTicket + "\r\n";
                    ticket+=day + "/" + month + "/" + year + " - " + hour + ":" + minute + "\r\n\n";
                    champStatut.setText("Merci et au revoir !");
                    champTotal.setText("");
                    champArgent.setText("");
                    champArgent.setEditable(false);
                    booleanPayer = false;
                    booleanImprimer = true;
                    
                    montantAch=0;
    				labelPanier.setText("Aucun produit");
    				montAch.setText(Integer.toString(montantAch)+" DA");
    				
    				panAchats2.removeAll();
    				panAchats2.repaint();

                }
               
            
        }
    
    
    
    private void boutonRechargeActionPerformed(java.awt.event.ActionEvent evt)
    {
    	short montant;
    	 if(!champMontantRecharge.getText().matches("^\\d+$") ||champMontantRecharge.getText().equals("0"))
         {
             JOptionPane.showMessageDialog(null, "Le Montant doit être un nombre entier positif ! !", "Attention !", JOptionPane.WARNING_MESSAGE);
         }
    	 else
    	 {
    		 int inter = Integer.parseInt(champMontantRecharge.getText());
    		 if(inter > 32767)
    		 {
    	       JOptionPane.showMessageDialog(null, "Montant Invalid", "Problème !", JOptionPane.ERROR_MESSAGE);
    	       return ;
    		 }
    			 montant = (short)inter;
    		 
    		 
    	byte [] montantArray = shortToByteArray(montant);
    	if(montant > 0 && montant <= 32767)
    	{
    		CardManagement.recharge(montantArray);
			cardInfo.setText(CardManagement.getInfo());
			crntBalance = CardManagement.bal; 
			champMontantRecharge.setText("");
			if(crntBalance >= total && total != 0) // après une opération de rechrage on peut débiter si la nouvelleBalance >total && total !=0
			{
                booleanPayer = true;
			}

    	}
    	 
    	else
    	{
           JOptionPane.showMessageDialog(null, "Montant Invalide", "Problème !", JOptionPane.ERROR_MESSAGE);
    	}
    	 }
    }
    

    
     void boutonImprimerActionPerformed(java.awt.event.ActionEvent evt) {
        if(booleanImprimer)
        {
            
                String cheminTicketImpression = null;
                if(booleanSettings)
                    {
                        if(Boolean.valueOf(settings.getProperty("tickCheminTickets")).booleanValue())
                        {                 
                            cheminTicketImpression = settings.getProperty("cheminTickets");
                        }
                        else
                        {
                            cheminTicketImpression = sauverTicket();
                        }
                    }
                else
                {
                    cheminTicketImpression = sauverTicket();
                }
                if(cheminTicketImpression != null)
                {
                    try
                    {
                    cheminTicketImpression += ("/Ticket_" + numTicket + "_" + year + "-" + month + "-" + day + "_" + hour + "H" + minute + ".txt");
                    Writer ticketW = new BufferedWriter(new FileWriter(cheminTicketImpression));
                    ticketW.write(ticket);
                    ticketW.close();
                    numTicket++;
                    champStatut.setText("En attente...");
                    champTotal.setText("");
                    champTicket.setText(""+numTicket);
                    booleanScan = true;
                    booleanImprimer = false;
                    boutonPayer.setText("Payer");
                    booleanNew = true;
                    }
                    catch (IOException e)
                    {
                    JOptionPane.showMessageDialog(null, "Problème lors de l'enregistrement !", "Erreur !", JOptionPane.ERROR_MESSAGE);
                    }
                }
        }
    }

    public String sauverTicket()
    {
        JFileChooser cheminFichier = new JFileChooser();
        cheminFichier.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int retour = cheminFichier.showSaveDialog(null);
        if(retour == JFileChooser.APPROVE_OPTION)
        {
            return cheminFichier.getSelectedFile().getAbsolutePath();
        }
        else
        {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner un dossier pour l'impression du ticket !", "Erreur !", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    
    protected short ByteArrayToShort(byte [] x)
	{
		short res = (short)((x[1]<<8) + (x[0] & 0xFF));
		return res ;
	}
    
    protected static byte [] shortToByteArray(short x)
	{
		byte [] res = new byte[2];
		res[0] = (byte)(x & 0xFF);
		res[1] = (byte)((x >> 8) & 0xFF);
		return res ;
	}
    
    private JButton boutonImprimer;
	private JTextField champTicket;
	private JLabel labelClient;
	private JButton boutonRecharge;
	private JTextField champMontantRecharge;
	private JButton boutonPayer;
	private JTextField champArgent;
	private JButton boutonTotal;
	private JLabel labelEuro;
	private JLabel labelEuro2;
	private JTextField champTotal;
	private JTextField champStatut;
	private JButton boutonSupprimer;
	private JButton boutonAjouter;
	private JSpinner JSpinnerQuantity;
	private JLabel labelQuantity;
	@SuppressWarnings("rawtypes")
	private JComboBox JComboBoxListe;
	private JLabel labelArticle;
	private JLabel labelParam;
	private JCheckBox tickParam;
	private JTextArea cardInfo;
	private JLabel labLogo;
	private JPanel panelCheckboxes;
	private JLabel labPanier;
	private JSeparator separator1;
	private JSeparator separator2;
	private JLabel labelPanier;
	
	private JLabel MontAch=new JLabel("Montant des achats : ");
	private JLabel montAch=new JLabel();
	private JLabel achatsEff1=new JLabel("Aucun produit.");
	
	private JPanel panMontAchat=new JPanel();
	private JPanel panAchats1=new JPanel();
	private JPanel panAchats2=new JPanel();
	
	private int montantAch=0;
	private int nbAchats=0;
	
	@SuppressWarnings("rawtypes")
	private ArrayList boxes=new ArrayList();
	@SuppressWarnings("rawtypes")
	private ArrayList number=new ArrayList();
	@SuppressWarnings("rawtypes")
	private ArrayList name=new ArrayList();
	@SuppressWarnings("rawtypes")
	private ArrayList price=new ArrayList();
	
   


}
