package e_docs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import javax.swing.JOptionPane;

public class CardManagement {

	final static byte CAISSE_CLA = (byte)0x80 ; 
	final static byte ADMIN_CLA = (byte) 0xB0;
	
	//les instructions 
    final static byte CONSULTATION =(byte)0x50;
    final static byte ADMINISTRATION=(byte)0x60;
    
    final static byte DATE=(byte)0x10;
    final static byte GETDATES=(byte)0x12;
    
  //le modulo pour RSA : n (String) 
  	private static final String rsaModulusString ="dafc7ac7b31ffbca9182e510d4f3ae45d5d8566623d6819d3da4a976b587af30025e1e824d84146b138a604643665eb81a5f17ec6cab8d63ace1bb055c4cfb83bf3b9ff40e436371bfc4affd3018e6c8d212794ea1e22436c43bc868b57d5f07ba32f67d9efb3d66ea162d13e0914e95d20198f2079d63080fa3c8bb204a6ea659e209d05b22051ba6105e5ea97c700e4fdce1f3f7142c3454bc5f9ad5a0fba1e5aa2a9d8cd1c04a8dbfac31b65150c1518e3a1aac5ccaf7ffee543fa76516f34474099de4a8d83022c1d1e297fc136ba0d60dea7f0ee4ecb6cb38f992631947b6cd960f4ba2f46a56dbb75116715208c304a8b6389c96f23e803185462d9a7d";
  //exposant privé pour RSA : d (String)
    private static final String rsaPrivateKeyExponentString ="d37eab1a39deb6f0b24e67eb34b0506cf4166783683360a53b86cf806ddc288f468a1e0f1ccc6667efcc62251a2d4167af588e2371e4d1be2597316800f7fca6064890b8461a416df796ff9d991943b34a683b2e7d5c8a3b3694c27e96620a28cd6530f8ac55dbbc8c727480d10fa5853ef8f9fd2eeb8b5c9ab64544acf01621dbff5ecc8f2e29e0231bbf11ede5324eec098bd8f2fd6dbfe62c1aa6178c212383a867e250494cf7246bcf11899aa2f0b5fb0a7a322dea896b837e135b109f6429f68d3679ec178f2f4ee166348ccbc23517c38f212069c88895cfedb58c148f05c09352d721f0c5842f3984ad5e4a323681ee5385dd798fb1859481d7978845";

    //le modulo pour RSA : n (BigInteger)
    private static BigInteger modulo;
    //exposant privé pour RSA  : d(BigInteger)
	private static BigInteger rsaPrivateKeyExponent;
	
	
	
  
	//spécification de la clé privée
	private static RSAPrivateKeySpec privateSpec;
	
	
	
	//la clé privée de l'administration (d,n)
	private static PrivateKey priv ;
	

	//l'objet Cipher qui effectue le cryptage et le décyptage
	private static Cipher C,CipherAES ;

	//tableau qui contient le message aléatoire crypté par la carte
	private static byte [] msgAlCrpt ;
	
	
	//tableau qui contient le résultat de décryptage de msgAlCrpt
	private static byte [] msgAlClaire ;
	
	//l'objet Signature utilisé pour signer et vérifier les informations
	protected static Signature dsa ;
	
	
	// les infos récupérées pour vérifier la signature
	
	
	// le lecteur
	protected static CardTerminal cad;
		
	//canal 
	protected static  CardChannel canal ;
	
	// la carte 
		static Card c;
		
	// la balance 
		static int bal = 0; 
		
		static byte[] AESData= new byte[256];
		
		static SecretKeySpec secretKeySpec;
		
		static byte[] dataSign;

		
	
/********************************************************************************************************/
		
		//méthode pour convertir un tableau de byte en String 
	    protected static String toString(byte [] tab) 
		{
			String resultat = "" ;
			for(int i = 0; i< tab.length;i++)
			{
				resultat = resultat+tab[i];
			}
			return resultat ;
		} //fin da la méthode toString(byte [] tab)
	    
/********************************************************************************************************/
	    
	    //méthode pour convertir un tableau de char en String 
	    
	    protected static String toString(char [] tab) //Overload
	   	{
	   		String resultat = "" ;
	   		for(int i = 0; i< tab.length;i++)
	   		{
	   			resultat = resultat+tab[i];
	   		}
	   		return resultat ;
	   	} //fin da la méthode toString(char [] tab)
	    
	    
/********************************************************************************************************/
	    
	    //méthode pour convertir un String en tableau de byte
	    protected static byte[] toByte(String str)
	    {
	    	byte resultat [] = new byte[str.length()];
	    	for(int i = 0;i<str.length();i++)
	    	{
	    		resultat[i] = (byte)str.charAt(i);
	    		
	    	}
	    	return resultat ;
	    }
	    
/********************************************************************************************************/
    
	    //méthode pour convertir un tableau de byte en tableau de char ;
	    protected static char [] byteToChar(byte [] byteArray)
	    {
	    	char [] resultat = new char[byteArray.length];
	    	for(int i = 0; i < byteArray.length ; i++)
	    	{
	    		resultat[i] = (char)byteArray[i];
	    	}
	    	return resultat;
	    	
	    }
	    
/********************************************************************************************************/

	    //méthode de traitement des réponses 
	    
	    protected static void evalSW(ResponseAPDU rep)
	    {
	    	int sw = rep.getSW();
	    	switch(sw)
	    	{

	    	case 0x9000: break;
	    	case 0x6300: JOptionPane.showMessageDialog(null,"Erreur : Authentification échouée "+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6301: JOptionPane.showMessageDialog(null,"Erreur : Authentification requise "+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6302: JOptionPane.showMessageDialog(null,"Erreur : Nombre d'essais restants de PIN nul "+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6A86: JOptionPane.showMessageDialog(null,"Erreur : Paramètre non valide "+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6E00: JOptionPane.showMessageDialog(null,"Erreur : La classe de la commande est non supportée "+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6D00: JOptionPane.showMessageDialog(null,"Erreur : Instruction non supportée "+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6700: JOptionPane.showMessageDialog(null,"Erreur : La longueur des données est invalide "+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;

	    	
	    	}

	    }
	    
/********************************************************************************************************/

	    private static byte [] decryptAuth(byte [] data) 
	    {
	    	try
	    	{
	    	CardManagement.C.init(Cipher.DECRYPT_MODE,priv);
	    	return CardManagement.C.doFinal(data,0,data.length);
	    	}
	    	catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException  e)
	    	{	
                JOptionPane.showMessageDialog(null,"Exception par la méthode decryptAuth() " +  e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);
	    		return null;
	    	}
	    	
	    } //fin decrypte
	    
/********************************************************************************************************/
	    
	    protected static boolean verifyCardConnection() //méthode vérifier la présence de la carte
	    {
	    	
	    			try {
						if(CardManagement.cad.isCardPresent())
						{
							return true;
						}
						else
							return false;
					} 
	    			catch (CardException e) {
						JOptionPane.showMessageDialog(null,"Exception par la méthode verifyCardConnection() " +  e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);

						return false;
					}
	    } //fin de la méthode verifyCardConnection()
	    
	   
	    
/********************************************************************************************************/
	    protected static byte select() //début de la méthode select
	    {
	    	byte rep = 0 ;
	    	try
	    	{
				c = cad.connect("T=1");
			} 
	    	catch (CardException e1)
	    	{
	    		return rep;
			} 
	    	try
			{
			    canal = c.getBasicChannel();//création de d'un canal
			    
			    /* Sélection de l'applet */
				CommandAPDU selectApdu = new CommandAPDU(new byte [] {(byte)0x00,(byte) 0xA4,(byte) 0x04,(byte) 0x00,(byte) 0xb,(byte) 0x01,(byte) 0x02,(byte) 0x03,(byte) 0x04,(byte) 0x05,(byte) 0x06,(byte) 0x07,(byte) 0x08,(byte) 0x09,(byte) 0x01,(byte) 0x00,(byte) 0x7F} );
				ResponseAPDU  selectReponse = canal.transmit(selectApdu);
				if (selectReponse.getSW() != 0x9000) 
				{
	                JOptionPane.showMessageDialog(null, "Erreur lors de la sélection de l'applet " +selectReponse.getSW(), "Problème !", JOptionPane.ERROR_MESSAGE);
	                return rep;
				}
				else
				{
				 rep = selectReponse.getData()[0]; 
					return rep ;
				 }
			}
			catch(Exception e)
			{
		            JOptionPane.showMessageDialog(null,"Exception par la méthode select : " + e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);
		            return rep ;
			}
		}// fin de la méthode select()
	    
	    
	    
/********************************************************************************************************/
	    protected static boolean verifyTerminal() throws CardException /*vérification de terminal*/

		{
		
			//Réception de trois blocs
			msgAlCrpt=new byte[256];
			
		for(int q=0;q<3;q++)
		{
		CommandAPDU getMsgApdu =new CommandAPDU(ADMIN_CLA, 0x7A,(byte)q,0x00);
		ResponseAPDU getMsgReponse = canal.transmit(getMsgApdu);
		
		if(getMsgReponse.getSW() == 0x9000) // si l'applet a envoyé un message
			
		{
			byte[] msgAlCrptData = getMsgReponse.getData(); //récupération du message
			
			
			for(int z=0;z<msgAlCrptData.length;z++)
			{
				msgAlCrpt[127*q+z]=msgAlCrptData[z];
				
			}
			
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Problème lors de la demande du message ! Bloc N° "+q+" "+ getMsgReponse.getSW(), "Problème !", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		}
		
		msgAlClaire = decryptAuth(msgAlCrpt); // décrypter le message
		
			if(msgAlClaire != null) // si le message a été décrypté avec succès
			{
		
			CommandAPDU verifyApdu = new CommandAPDU(ADMIN_CLA,0x7B,0x00,0x00,msgAlClaire);
			ResponseAPDU getVerifyReponse = canal.transmit(verifyApdu);
			
				if(getVerifyReponse.getSW() == 0x6402) // réponse de l'applet 
				{
					JOptionPane.showMessageDialog(null, "Ce terminal est considéré comme un mauvais terminal !", "Problème !", JOptionPane.ERROR_MESSAGE);
					return false;
				}
				
		       
			}
			else //un probleme lors l'opération de décryptage
			{
			JOptionPane.showMessageDialog(null, "Problème lors de l'opération de décryptage !", "Problème !", JOptionPane.ERROR_MESSAGE);
			return false;	
			}
			
			 return true ;
		}// fin de la méthode verifyTerminal()
	    
/********************************************************************************************************/
		protected static void setAESKey()
		{
			
			try {
				for(int x=0;x<3;x++)
				{
				CommandAPDU getAESApdu=new CommandAPDU(ADMIN_CLA,0x7E,(byte)x,0x00);
				ResponseAPDU getAESReponse = canal.transmit(getAESApdu);
				
				if(getAESReponse.getSW() == 0x9000) // si l'applet a envoyé un message
					
				{
					byte[] received=getAESReponse.getData();
					
					for(int w=0;w<received.length;w++)
					{
						
						AESData[127*x+w]=received[w];
					}
					
				}
				else{
					JOptionPane.showMessageDialog(null,"Problème lors de la récupération de la clé AES");
					return;
					}
				}
				secretKeySpec = new SecretKeySpec(decryptAuth(AESData), "AES");
				
			} catch (CardException e) {
	            JOptionPane.showMessageDialog(null,"Exception par la méthode setAESKey() ligne 372 : " + e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);

			}
			
		}
		
/********************************************************************************************************/
	
		  public static void CreatFile(String path)
		   {
			   File fichier = new File(path);
			   try {
				fichier.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			   
		   }
/*********************************************************************************************************/	    
		    protected static  PublicKey getPublicKey(String certPath)
			{
				 InputStream inStream = null;
				 try {
				     inStream = new FileInputStream(certPath);
				     CertificateFactory cf = CertificateFactory.getInstance("X.509");
				     X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
				     inStream.close();
				      return cert.getPublicKey();
				 }
				 catch(  CertificateException | IOException e)
				 {
					  JOptionPane.showMessageDialog(null, "Clé publique non récupérée "+ e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
					  return null ;
				 }
				 
				
			}
		    
/*********************************************************************************************************/	    

		    public static void clean(String path)
			   {
				   File dossier = new File(path);
			    	  if( dossier.exists() )
			    	  {
			    		  File[] files = dossier.listFiles();
			    		  for( int i = 0 ; i < files.length ; i++ )
			    		  {
			    			  files[ i ].delete();
			    		  }

			    	  }
			    	  else
			    	  {
			            JOptionPane.showMessageDialog(null,"Le dossier path n'existe pas " + path , "Problème !", JOptionPane.ERROR_MESSAGE);

			    	  }
			   }		    
/*********************************************************************************************************/	    
		    protected static  boolean verifyCerticat(String Cert1, String Cert2)
			{
				 InputStream inStream = null;
				 InputStream inStream2 = null;
				 try {
				     inStream = new FileInputStream(Cert1);
				     CertificateFactory cf = CertificateFactory.getInstance("X.509");
				     X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
				     cert.getPublicKey();
				     
				     inStream2 = new FileInputStream(Cert2);
				     CertificateFactory cf2 = CertificateFactory.getInstance("X.509");
				     X509Certificate cert2 = (X509Certificate)cf2.generateCertificate(inStream2);
				     
				     cert.verify(cert2.getPublicKey());
				     inStream.close();
				     inStream2.close();
				     return true;
				    }
				  catch(InvalidKeyException  | CertificateException | NoSuchAlgorithmException | NoSuchProviderException | SignatureException | IOException e)
				  {
				  JOptionPane.showMessageDialog(null, "Certificat non vérifié " , "Problème !", JOptionPane.ERROR_MESSAGE);
				  
				  return false;
				 }
			  } //fin de la méthode verifyCerticat()   
	/*********************************************************************************************************/	    

	    /*vérification de la carte par certificat+message crypté*/
		protected static boolean verifyCard() throws CardException , InvalidKeyException, SignatureException

		{
		
		/*Demander la logueur du certificat */
		
		CommandAPDU getInfoApdu =new CommandAPDU(ADMIN_CLA,0x77,0x00,0x00);
		ResponseAPDU getInfoReponse = canal.transmit(getInfoApdu);
		
			if(getInfoReponse.getSW() == 0x9000) // si l'applet a envoyé un message
			
			{
				byte[] lenCert=getInfoReponse.getData();
				int nombre=(int)lenCert[0];
				int tailleDern=(int)lenCert[1];
				byte[] certificat=new byte[nombre*127+tailleDern];
				
				// demander le certificat
				
				if(tailleDern!=0)
					nombre++;
				byte[] bloc;
				try {
				for(int s=0;s<nombre;s++)
				{
					
					CommandAPDU certApdu = new CommandAPDU(0xB0,0x78,(byte)(s),0x00);
					ResponseAPDU getCertReponse;
					
						getCertReponse = canal.transmit(certApdu);
						if (getCertReponse.getSW() != 0x9000) 
						{
							
				            JOptionPane.showMessageDialog(null,"Erreur lors de la réception du certificat, bloc N° "+s+"  " + getCertReponse.getSW() , "Problème !", JOptionPane.ERROR_MESSAGE);
				            return false;
						}
						else
						{
							bloc=getCertReponse.getData();
							for(int t=0;t<bloc.length;t++)
							{
							 certificat[127*s+t]=bloc[t];	
							}
						}
					}
				        CreatFile("C:\\E-Docs\\E-Docs_File\\certificat_store\\certificat.cer");
						File file = new File("C:\\E-Docs\\E-Docs_File\\certificat_store\\certificat.cer");
						FileOutputStream fop = new FileOutputStream(file);
						fop.write(certificat);
						fop.flush();
						fop.close();
						
						//Vérification du certificat
						
						
						if(!verifyCerticat("C:\\E-Docs\\E-Docs_File\\certificat_store\\certificat.cer", "C:\\E-Docs\\E-Docs_File\\USTHB-CA.cer"))
						{
							  JOptionPane.showMessageDialog(null, "Certificat de la carte non valide " , "Problème !", JOptionPane.ERROR_MESSAGE);
							  System.exit(1);
						}
					
						//Récupérer la clé publique de la carte à partir du certificat
						
						//Récupérer les données signées
						 byte[] dataSigned;
						 dataSign=new byte[12];
						 CommandAPDU dataApdu = new CommandAPDU(0xB0,0x76,0x00,0x00);
							ResponseAPDU getDataReponse;
							
								getDataReponse = canal.transmit(dataApdu);
								if (getDataReponse.getSW() != 0x9000) 
								{
									
						            JOptionPane.showMessageDialog(null,"Erreur lors de la réception des données de la signature, bloc N° "+ getDataReponse.getSW() , "Problème !", JOptionPane.ERROR_MESSAGE);
						            return false;
								}
								else
								{
									
									dataSigned=AESDecrypt(getDataReponse.getData());
									for(int lon=0;lon<12;lon++)
										dataSign[lon]=dataSigned[lon];
									
								}
						 
						//Récupérer la signature
							byte[] messageCrypte=new byte[256];
							byte[] blocJC;
							try {
							for(int s=0;s<3;s++)
							{
								
								CommandAPDU signApdu = new CommandAPDU(0xB0,0x7D,(byte)(s),0x00);
								ResponseAPDU getSignReponse;
								
									getSignReponse = canal.transmit(signApdu);
									if (getSignReponse.getSW() != 0x9000) 
									{
										
							            JOptionPane.showMessageDialog(null,"Erreur lors de la réception de la signature, bloc N° "+s+"  " + getSignReponse.getSW() , "Problème !", JOptionPane.ERROR_MESSAGE);
							            return false;
									}
									else
									{
										blocJC=getSignReponse.getData();
										for(int t=0;t<blocJC.length;t++)
										{
										 messageCrypte[127*s+t]=blocJC[t];	
										
										}
										
									}
									
								}
							
						//Comparer les données et la signature
							boolean identique = false ;
							dsa = Signature.getInstance("SHA1withRSA");
							 dsa.initVerify(getPublicKey("C:\\E-Docs\\E-Docs_File\\certificat_store\\certificat.cer"));
							 dsa.update(dataSign);
							 identique = dsa.verify(messageCrypte);

							if(identique == false)
							{
				            JOptionPane.showMessageDialog(null,"Mauvaise carte", "Problème !", JOptionPane.ERROR_MESSAGE);
				            System.exit(1);
							}
							else{
							    JOptionPane.showMessageDialog(null,"Bonne carte", "", JOptionPane.INFORMATION_MESSAGE);
								clean("C:\\E-Docs\\E-Docs_File\\certificat_store");
								
							}
							return identique ;
									
								
							} catch ( NoSuchAlgorithmException  e) {
					             JOptionPane.showMessageDialog(null,"Exception dans la méthode verifyCard() " + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
								return false;
							}
							finally{
								
							}
						
					} catch (CardException | IOException e) {
			             JOptionPane.showMessageDialog(null,"Exception dans la méthode verifyCard() " + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);

						return false;
					}
					
		  }
			
		else 
		{
	         JOptionPane.showMessageDialog(null,"Infos non récupérées :( " + getInfoReponse.getSW(), "Problème !", JOptionPane.ERROR_MESSAGE);
	         return false;
		} 
	
		}
//fin de la méthode verifyCard()
	    
/********************************************************************************************************/
		 protected static void isReaderConnected() // vérifier la présence de lecteur et ferme le programme si il est absent
		   {
			   TerminalFactory tf = TerminalFactory.getDefault();
				CardTerminals list = tf.terminals();
			   CardTerminal cadTest = list.getTerminal("Gemalto GemPC Pinpad USB Smart Card Read 0");
				//CardTerminal cadTest = list.getTerminal("OMNIKEY CardMan 3821 0");
				  if(cadTest == null)
					{
		                JOptionPane.showMessageDialog(null, "Branchez le lecteur !", "Problème !", JOptionPane.ERROR_MESSAGE);
		                if(verifyCardConnection()){
		                	try 
		                	{
							c.disconnect(true);
		                	} catch (CardException e)
		                	{
					        JOptionPane.showMessageDialog(null,"Exception dans la méthode isReaderConnected() ligne 626 " + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
		                	}	
		                						}
						System.exit(1);
					}
		   }
	    
/********************************************************************************************************/	    
	    
	    
	    
	    protected static byte[] AESEncrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException
		{
			try {
				
				if(data.length%16==0)
					CipherAES = Cipher.getInstance("AES/ECB/NoPadding");
				else
					CipherAES = Cipher.getInstance("AES");
				
				CipherAES.init(Cipher.ENCRYPT_MODE, secretKeySpec);
				
				byte[] done=CipherAES.doFinal(data);
				return done;
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
		        JOptionPane.showMessageDialog(null,"Exception dans la méthode AESEncrypt() ligne 652 " + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
/********************************************************************************************************/
		protected static byte[] AESDecrypt(byte[] data)
		{
			try {
				CipherAES = Cipher.getInstance("AES/ECB/NoPadding");
				CipherAES.init(Cipher.DECRYPT_MODE, secretKeySpec);
				return CipherAES.doFinal(data);
			} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
		        JOptionPane.showMessageDialog(null,"Exception dans la méthode AESDecrypt() ligne 664," + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
/********************************************************************************************************/

		private static String connectAndListen()
		 {
			 try{
					Socket s1 = new Socket("localhost",27016);		
					BufferedReader br=new BufferedReader(new InputStreamReader(s1.getInputStream()));
					String repC=br.readLine();
					String finalRep = "";
					for(int i = 0 ; i<4;i++)finalRep += repC.charAt(i);//suppression de '\0'		
					System.out.println("la réponse de la carte est "  + finalRep);
					s1.close();
					br.close();
					return finalRep;	
					}
					catch(Exception e){
						System.out.println(e.getMessage());
						return null;
					}
		 }
/********************************************************************************************************/
	    
	 protected static boolean authentification()
	 {		
		 /*	JPanel message=new JPanel();
			JLabel labPIN=new JLabel("PIN : ");
			JPasswordField textPIN=new JPasswordField("");
			textPIN.setPreferredSize(new Dimension(50,25));
			message.add(labPIN);
			message.add(textPIN);
		JOptionPane.showConfirmDialog(null, message, "Authentification", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

			String PIN=new String(textPIN.getPassword());
			byte [] pinArray = toByte(PIN);
			
			
			
			try {
				CommandAPDU verifyApdu = new CommandAPDU(CAISSE_CLA,VERIFY,0x00,(short)pinArray.length,pinArray);
				ResponseAPDU verifyReponse = canal.transmit(verifyApdu);
				evalSW(verifyReponse);
				if(verifyReponse.getSW() == 0x6300){authentification();}
				if(verifyReponse.getSW() == 0x6302){System.exit(1) ;}
				return true ;
					
				} 
			catch (CardException e)
				{
    			JOptionPane.showMessageDialog(null, "Exception dans la méthode authentification() ligne 696 " + e.getMessage(),  "Problème !", JOptionPane.ERROR_MESSAGE);
    			return false ;
				}
		 */
		 String cardRep = connectAndListen();
			if(cardRep.equals("6300")){return authentification();}
			else if(cardRep.equals("6302")){System.exit(1);return false  ;}
			else if(cardRep.equals("9000")){return true ;}
			else {return true;}

	 } // fin de la méthode authentification()
/********************************************************************************************************/

	 private static byte [] consultation(byte p1,CardChannel canal) throws IOException, CardException
	    {
	    	CommandAPDU consultationApdu;
	    	if(p1 == 0x00)
	    		consultationApdu = new CommandAPDU(CAISSE_CLA,CONSULTATION,p1,0x00,0x02);
	    	else
	    		 consultationApdu = new CommandAPDU(CAISSE_CLA,CONSULTATION,p1,0x00);

			ResponseAPDU consultationReponse = canal.transmit(consultationApdu);
			evalSW(consultationReponse);
			if (consultationReponse.getSW() != 0x9000) 
			{
				JOptionPane.showMessageDialog(null, "Info non récupérée"+" "+consultationReponse.getSW());
				return null;
			}
			else
			{	byte [] Rep = new byte[consultationReponse.getData().length];
				Rep = consultationReponse.getData() ;
				return Rep;
			}
	    }//fin de la méthode consultation
	 

	   protected static String getInfo(byte InsCode) 
	   {
		   //matricule : InsCode = 0x01
		   //nom : InsCode = 0x02
		   //prénom : InsCode = 0x03
		   //filiere : InsCode = 0x04
		   String res;
		try {
			res = tri(toString(byteToChar(AESDecrypt(consultation(InsCode,canal)))));	   				 
			} 
		catch (IOException | CardException  e)
		{
	        JOptionPane.showMessageDialog(null,"Exception dans la méthode getInfo() " + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		   
		   return res;
	   } //fin da la méthode getInfo()
	    
	   
	    /* ************************************ */
	    
	   public static boolean isAlphanum(char c)
	   {
		   boolean exists=false;
		   if(c=='a' ||c=='b' ||c=='c' ||c=='d' ||c=='e' ||c=='f' ||c=='g' ||c=='h' ||c=='i' ||c=='j' ||c=='k' ||c=='l' ||c=='m' ||c=='n' ||c=='o' ||c=='p' ||c=='q' ||c=='r' ||c=='s' ||c=='t' ||c=='u' ||c=='v' ||c=='w' ||c=='x' ||c=='y' ||c=='z' )
			   exists=true;
		   if(c=='A' ||c=='B' ||c=='C' ||c=='D' ||c=='E' ||c=='F' ||c=='G' ||c=='H' ||c=='I' ||c=='J' ||c=='K' ||c=='L' ||c=='M' ||c=='N' ||c=='O' ||c=='P' ||c=='Q' ||c=='R' ||c=='S' ||c=='T' ||c=='U' ||c=='V' ||c=='W' ||c=='X' ||c=='Y' ||c=='Z' )
			   exists=true;
		   if(c=='0' ||c=='1' ||c=='2' ||c=='3' ||c=='4' ||c=='5' ||c=='6'||c=='7'||c=='8'||c=='9' || c ==  '.')
			   exists=true;
		   return exists;
	   }
	   
	   public static String tri(String data)
	   {
		   String mat="";
			int o=0;
					while(isAlphanum(data.charAt(o)) & o<data.length())
					{
						mat+=data.charAt(o);
						o++;
					}
					return mat;
	   }

		public CardManagement()  throws Exception  {
			
			/* initialisation des ressources cryptographique*/
			            
		 CardManagement.modulo = new BigInteger(CardManagement.rsaModulusString,16);
		 CardManagement.rsaPrivateKeyExponent = new BigInteger(CardManagement.rsaPrivateKeyExponentString,16);
		 
		 CardManagement.privateSpec = new RSAPrivateKeySpec(CardManagement.modulo,CardManagement.rsaPrivateKeyExponent );
		 try
		 {
		 KeyFactory factory = KeyFactory.getInstance("RSA");
		 CardManagement.priv = factory.generatePrivate(CardManagement.privateSpec);
		 CardManagement.C = Cipher.getInstance("RSA");

		 }
		 catch( NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e)
		 {
		    JOptionPane.showMessageDialog(null,"Exception dans la méthode CardManagement() " + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
			 return ;
		 }
		
		 
		 try
			{
				
			 dsa = Signature.getInstance("SHA1withRSA");
			 dsa.initSign(priv);
			
			}
			catch(NoSuchAlgorithmException| InvalidKeyException  e)
			{
			    JOptionPane.showMessageDialog(null,"Exception dans la méthode CardManagement() " + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
				return ;
			}
		
			/* connexion au lecteur */
			
		 TerminalFactory tf = TerminalFactory.getDefault();
			CardTerminals list = tf.terminals();
			
			cad = list.getTerminal("Gemalto GemPC Pinpad USB Smart Card Read 0");
			//cad=list.getTerminal("OMNIKEY CardMan 3821 0");
			 
			if(cad == null)
			{
             JOptionPane.showMessageDialog(null, "Branchez le lecteur !", "Problème !", JOptionPane.ERROR_MESSAGE);
				System.exit(1);
			}
			
		}
		
}
