package adminTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
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
 
	//final static byte CAISSE_CLA = (byte)0x80 ; 
	final static byte ADMIN_CLA = (byte) 0xB0;
	
	//les instructions 
    final static byte ADMINISTRATION=(byte)0x60;
    final static byte CHANGE_FILIERE =(byte)0x80;
    final static byte GET_FILIERE = (byte)0x7F;

    
    
   
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
	
	   static byte[] test2=new byte[16];
	   
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
	    
	    //méthode de traitement des réponses 
/********************************************************************************************************/
	    protected static void evalSW(ResponseAPDU rep) // méthode pour évaluer la réponse de l'applet
	    {
	    	int sw = rep.getSW();
	    	switch(sw)
	    	{

	    	case 0x9000: break;
	    	case 0x6300: JOptionPane.showMessageDialog(null,"Erreur : Authentification échouée"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6301: JOptionPane.showMessageDialog(null,"Erreur : Authentification requise"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6302: JOptionPane.showMessageDialog(null,"Erreur : Nombre d'essais restants de PIN nul"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6A83: JOptionPane.showMessageDialog(null,"Erreur : Motant de transaction invalide"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6A84: JOptionPane.showMessageDialog(null,"Erreur : La balance maximale dépassée"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6A85: JOptionPane.showMessageDialog(null,"Erreur : Balance insuffisante"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6A86: JOptionPane.showMessageDialog(null,"Erreur : Paramètre non valide"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6E00: JOptionPane.showMessageDialog(null,"Erreur : La classe de la commande est non supportée"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6D00: JOptionPane.showMessageDialog(null,"Erreur : Instruction non supportée"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;
	    	case 0x6700: JOptionPane.showMessageDialog(null,"Erreur : La longueur des données est invalide"+ rep.getSW(),"Problème !",JOptionPane.ERROR_MESSAGE);break;

	    	
	    	}

	    } 
/********************************************************************************************************/

	    private static byte [] decryptAuth(byte [] data) //méthode utilisée pour déchiffrer les données avant le calcul de la clé de session AES (RSA) 
	    {
	    	try
	    	{
	    	CardManagement.C.init(Cipher.DECRYPT_MODE,priv);
	    	return CardManagement.C.doFinal(data,0,data.length);
	    	}
	    	catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException  e)
	    	{	
                JOptionPane.showMessageDialog(null,"Exception par la méthode decrypt()" + e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);
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
						{
							return false;
						}
					} catch (CardException e)
					{
			            JOptionPane.showMessageDialog(null,"Exception par la méthode verifyCardConnection() : " + e.getMessage(), "Problème !", JOptionPane.ERROR_MESSAGE);
						return false;
					}
	    } //fin de la méthode verifyCardConnection()
/********************************************************************************************************/
	    protected static byte select() //début de la méthode select
	    {
	    	byte rep = 0 ;
	    	try {
	    		
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
	                JOptionPane.showMessageDialog(null, "Applet absente  " +selectReponse.getSW(), "Problème !", JOptionPane.INFORMATION_MESSAGE);
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
					JOptionPane.showMessageDialog(null, "Probleme lors l'opération de décryptage !", "Problème !", JOptionPane.ERROR_MESSAGE);
					return false;	
					}
					
					 return true ;
				}

				
								/* fin de la vérification de terminal */

/********************************************************************************************************/
				protected static void setAESKey() // demander les des données de la carte pour calculer la clé AES
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
							JOptionPane.showMessageDialog(null,"Probléme lors la récupération de la clé AES");
							return;
							}
						}
						secretKeySpec = new SecretKeySpec(decryptAuth(AESData), "AES");
						
					} catch (CardException e) {
						e.printStackTrace();
					}
					
				}
/**
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException ******************************************************************************************************/
				protected static byte[] AESEncrypt(byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException // chiffrement AES
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
						e.printStackTrace();
						return null;
					}
				}
/********************************************************************************************************/
				protected static byte[] AESDecrypt(byte[] data) // déchiffrement AES
				{
					try {
						CipherAES = Cipher.getInstance("AES/ECB/NoPadding");
						CipherAES.init(Cipher.DECRYPT_MODE, secretKeySpec);
						return CipherAES.doFinal(data);
						
					} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
						e.printStackTrace();
						return null;
					}
				}
/********************************************************************************************************/				
				 /*vérification de la carte par certificat+message crypté*/
				protected static boolean verifyCard() throws CardException , InvalidKeyException, SignatureException

				{
					
					CreatFile("C:\\E_docs_tool\\E_docs_Admin_tool\\certificat_store\\certificat.cer");
					
				
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
								
						File file = new File("C:\\E_docs_tool\\E_docs_Admin_tool\\certificat_store\\certificat.cer");
					    FileOutputStream fop = new FileOutputStream(file);
				        fop.write(certificat);
                        fop.flush();
                  		fop.close();

								
								//vérification du certificat
								if(!CertificateManagement.verifyCerticat("C:\\E_docs_tool\\E_docs_Admin_tool\\certificat_store\\certificat.cer", "C:\\E_docs_tool\\E_docs_Admin_tool\\USTHB-CA.cer"))
								{
						
									  JOptionPane.showMessageDialog(null, "Certificat de la carte non valide " , "Problème !", JOptionPane.ERROR_MESSAGE);
									  System.exit(1);
								}
								
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
									// dsa.initVerify(pubCard);
									dsa.initVerify(CertificateManagement.getPublicKey("C:\\E_docs_tool\\E_docs_Admin_tool\\certificat_store\\certificat.cer"));
									 dsa.update(dataSign);
									 identique = dsa.verify(messageCrypte);

									if(identique == false)
									{
						            JOptionPane.showMessageDialog(null,"Mauvaise carte", "Problème !", JOptionPane.ERROR_MESSAGE);
									}
									else
									JOptionPane.showMessageDialog(null,"Bonne carte", "", JOptionPane.INFORMATION_MESSAGE);
									return identique ;
											
										
									} catch ( NoSuchAlgorithmException  e) {
										e.printStackTrace();
										return false;
									}
									finally{
										
									}
								
							} catch (CardException | IOException e) {
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
/**
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException ******************************************************************************************************/

				   // méthode utilisée pour l'initialisations ou le renouvelement  
	    protected static boolean init(String PIN,String matricule,String nom,String prenom,String filiere,int type) throws CardException, NoSuchAlgorithmException, NoSuchPaddingException
	    {
	    	String matriculeToUse;
			 
	    	if(type==0)
	    	{
	    		matriculeToUse=matricule;
	    		
			byte [] pinArray = toByte(PIN);
			
			byte [] matriculeArray = toByte(matricule);

			byte [] nomArray = toByte(nom);

			byte [] prenomArray = toByte(prenom);

			byte [] filiereArray = toByte(filiere);
			
			
			int longInfo = 5 + PIN.length() + matricule.length() + nom.length() + 
					           prenom.length() +filiere.length() ;
			
			
			byte [] installParam = new byte[longInfo]; //contient tout les paramétres

			
			/*construction de installParam*/
			int offset = 0 ;		
			installParam[offset] = (byte)pinArray.length ;
			System.arraycopy(pinArray, 0, installParam, offset+1, pinArray.length);
			
			offset = offset + pinArray.length + 1;
			installParam[offset] = (byte)matriculeArray.length ;
			System.arraycopy(matriculeArray, 0, installParam, offset+1, matriculeArray.length);
			
			offset = offset + matriculeArray.length + 1;
			installParam[offset] = (byte)nomArray.length ;
			System.arraycopy(nomArray, 0, installParam, offset+1, nomArray.length);
			
			offset = offset + nomArray.length + 1;
			installParam[offset] = (byte)prenomArray.length ;
			System.arraycopy(prenomArray, 0, installParam, offset+1, prenomArray.length);
			
			offset = offset + prenomArray.length + 1;
			installParam[offset] = (byte)filiereArray.length ;
			System.arraycopy(filiereArray, 0, installParam, offset+1, filiereArray.length);
			
			
			/* envoi des information de l'initialisation */
			byte[] test=AESEncrypt(installParam);
			
			CommandAPDU installApdu = new CommandAPDU(0xB0,0x70,0x00,(short)test.length,test);
				
			ResponseAPDU installReponse = canal.transmit(installApdu);
			
			if (installReponse.getSW() != 0x9000) 
			{
	            JOptionPane.showMessageDialog(null,"Erreur lors de l'initialisation " + installReponse.getSW(), "Problème !", JOptionPane.ERROR_MESSAGE);
	            return false;		
	        } 
	    }//fin de if(type == 0)
	    	else
	    	{
	    		matriculeToUse=toString(byteToChar(dataSign));
	    	}
	    	
	    	CertificateManagement certificatToSend= new CertificateManagement(matriculeToUse);
	    	//RsaPrivateKeyMang clePriv= new RsaPrivateKeyMang("C:\\Users\\oussama\\Desktop\\OpenSSL\\test.txt");
	    	RsaPrivateKeyMang clePriv= new RsaPrivateKeyMang("C:\\E_docs_tool\\E_docs_Admin_tool\\privateKey_store\\myPrivateKey.txt");
	    	
	    	byte[] modulusArray=clePriv.getByteArrayModulo();
	    	
	    	byte[] exponentArray=clePriv.getByteArrayPrivateExponent();
	    	
	    	byte[] certArray=certificatToSend.getCert();
	    	
			EnvoiMultiple envoiLongueurs=new EnvoiMultiple(canal,certArray,0);
			if(!envoiLongueurs.succeeded())
			{
				JOptionPane.showMessageDialog(null,"Longueurs non envoyées."  , "Problème !", JOptionPane.ERROR_MESSAGE);
				return false;
			}
				
			/* envoi du certificat */
			EnvoiMultiple envoiCert=new EnvoiMultiple(canal,certArray);
			if(!envoiCert.succeeded())
			{
				JOptionPane.showMessageDialog(null,"Certificat non envoyé."  , "Problème !", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			/* envoi du modulo de la clé privée */
			EnvoiMultiple envoiModulo=new EnvoiMultiple(canal,modulusArray,(byte)0x73);
			if(!envoiModulo.succeeded())
			{
				JOptionPane.showMessageDialog(null,"Modulo de la clé privée non envoyé."  , "Problème !", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			/* envoi de l'exponent de la clé privée */
			EnvoiMultiple envoiExponent=new EnvoiMultiple(canal,exponentArray,(byte)0x74);
			if(!envoiExponent.succeeded())
			{
				JOptionPane.showMessageDialog(null,"Exposant de la clé privée non envoyé."  , "Problème !", JOptionPane.ERROR_MESSAGE);
				return false;
			}
						
			/* envoi du signal de fin d'initialisation */
			CommandAPDU signApdu = new CommandAPDU(0xB0,0x75,0x00,0x00);
			ResponseAPDU setSignReponse = canal.transmit(signApdu);
			
			if (setSignReponse.getSW() != 0x9000) 
			{
	            JOptionPane.showMessageDialog(null,"Erreur lors de l'initialisation " + setSignReponse.getSW() , "Problème !", JOptionPane.ERROR_MESSAGE);
	            return false;
			}
			
			
			return true ;
	    }//fin de la méthode init()
	    
	    
/**
 * @throws NoSuchPaddingException 
 * @throws NoSuchAlgorithmException ******************************************************************************************************/
	    //méthode pour le changement de PIN
	    protected static void update(String PIN) throws IOException, CardException, NoSuchAlgorithmException, NoSuchPaddingException
	    {
	    	
	    		byte [] pinArray = toByte(PIN);
		    	CommandAPDU updateApdu = new CommandAPDU(ADMIN_CLA,ADMINISTRATION,0,pinArray.length,AESEncrypt(pinArray));
		    	ResponseAPDU updateReponse = canal.transmit(updateApdu);
	    		evalSW(updateReponse);
	    		
	    }//fin de la méthode update();
/********************************************************************************************************/

	    //méthode pour le changement de Filiere
	    protected static void updateFiliere(String newFiliere) throws IOException, CardException, NoSuchAlgorithmException, NoSuchPaddingException
	    {
	    	
	    		byte [] FiliereArray = toByte(newFiliere);
		    	CommandAPDU updateApdu = new CommandAPDU(ADMIN_CLA,CHANGE_FILIERE,0,0,AESEncrypt(FiliereArray));
		    	ResponseAPDU updateReponse = canal.transmit(updateApdu);
	    		evalSW(updateReponse);
	    		
	    }//fin de la méthode update();
/********************************************************************************************************/
	    //méthode pour récupérer la Filiere
	    protected static String getFiliere() throws IOException, CardException, NoSuchAlgorithmException, NoSuchPaddingException
	    {
	    	
	    	CommandAPDU getFiliereApdu = new CommandAPDU(ADMIN_CLA,GET_FILIERE,0,0);
	    	ResponseAPDU getFilierReponse = canal.transmit(getFiliereApdu);

    		evalSW(getFilierReponse);
    		if (getFilierReponse.getSW() != 0x9000) 
			{
				JOptionPane.showMessageDialog(null, "Filiere non récupérée"+" "+getFilierReponse.getSW());
				return null;
			}
			else
			{	byte [] Rep = new byte[getFilierReponse.getData().length];
				Rep = getFilierReponse.getData() ;
				String test = tri(toString(byteToChar(AESDecrypt(Rep))));
				return test;
			}

	    }//fin de la méthode update();
/********************************************************************************************************/
	    // vérifier si un caractère  est un caractère Alphanumérique 
	    public static boolean isAlphanum(char c)
		   {
			   boolean exists=false;
			   if(c=='a' ||c=='b' ||c=='c' ||c=='d' ||c=='e' ||c=='f' ||c=='g' ||c=='h' ||c=='i' ||c=='j' ||c=='k' ||c=='l' ||c=='m' ||c=='n' ||c=='o' ||c=='p' ||c=='q' ||c=='r' ||c=='s' ||c=='t' ||c=='u' ||c=='v' ||c=='w' ||c=='x' ||c=='y' ||c=='z' )
				   exists=true;
			   if(c=='A' ||c=='B' ||c=='C' ||c=='D' ||c=='E' ||c=='F' ||c=='G' ||c=='H' ||c=='I' ||c=='J' ||c=='K' ||c=='L' ||c=='M' ||c=='N' ||c=='O' ||c=='P' ||c=='Q' ||c=='R' ||c=='S' ||c=='T' ||c=='U' ||c=='V' ||c=='W' ||c=='X' ||c=='Y' ||c=='Z' )
				   exists=true;
			   if(c=='0' ||c=='1' ||c=='2' ||c=='3' ||c=='4' ||c=='5' ||c=='6'||c=='7'||c=='8'||c=='9' || c ==  ' ' || c == '.')
				   exists=true;
			   return exists;
		   }
		   
/********************************************************************************************************/
    	// supprimer tout les caractères spéciaux d'une chaine de caractères (supprimer le Padding)
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
/********************************************************************************************************/
            
		   
		   
		   protected static void isReaderConnected() // vérifier la présence du lecteur et ferme le programme si il est absent
		   {
			   TerminalFactory tf = TerminalFactory.getDefault();
				CardTerminals list = tf.terminals();
			  CardTerminal cadTest = list.getTerminal("Gemalto GemPC Pinpad USB Smart Card Read 0");
			//	CardTerminal cadTest=list.getTerminal("OMNIKEY CardMan 3821 0");
				  if(cadTest == null)
					{
		                JOptionPane.showMessageDialog(null, "Branchez le lecteur !", "Problème !", JOptionPane.ERROR_MESSAGE);
		                if(verifyCardConnection()){try {
							c.disconnect(true);
						} catch (CardException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}}
						System.exit(1);
					}
		   }
/********************************************************************************************************/
		   // méthode pour créer un ficher (utilisé pour créer un fichier qui contient le certificat de la carte)

		   public static void CreatFile(String path)
		   {
			   File fichier = new File(path);
			   try {
				fichier.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
		   }

	    
/********************************************************************************************************/
		 
		   // méthode pour supprimer le contenu d'un dossier
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
/********************************************************************************************************/

	    public CardManagement()  throws Exception  {
			
			/* initialisation des ressources cryptographique*/
			            
		 CardManagement.modulo = new BigInteger(CardManagement.rsaModulusString,16);
		 CardManagement.rsaPrivateKeyExponent = new BigInteger(CardManagement.rsaPrivateKeyExponentString,16);
		// CardManagement.rsaPublicKeyExponent  = new BigInteger(CardManagement.rsaPublicKeyExponentString,16);
		 
		 CardManagement.privateSpec = new RSAPrivateKeySpec(CardManagement.modulo,CardManagement.rsaPrivateKeyExponent );
		// CardManagement.publicSpec = new RSAPublicKeySpec(CardManagement.modulo,CardManagement.rsaPublicKeyExponent);
		 try
		 {
			 
		 KeyFactory factory = KeyFactory.getInstance("RSA");
		 CardManagement.priv = factory.generatePrivate(CardManagement.privateSpec);
		 //CardManagement.pub = factory.generatePublic(CardManagement.publicSpec);
		 CardManagement.C = Cipher.getInstance("RSA");
		 

		 }
		 catch( NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException e)
		 {
             JOptionPane.showMessageDialog(null,"Exception dans la méthode CardManagement(), problème lors le calcule des clés" + e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
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
