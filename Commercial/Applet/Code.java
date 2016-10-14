package pack_resto;


import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.OwnerPIN;
import javacard.framework.Util;
import javacard.security.AESKey;
import javacard.security.CryptoException;
import javacard.security.KeyBuilder;
import javacard.security.RSAPrivateKey;
import javacard.security.RSAPublicKey;
import javacard.security.RandomData;
import javacard.security.Signature;
import javacardx.crypto.Cipher;

public class JavaResto extends Applet {
	
	
	// La classe  
	final static byte RESTO_CLA = (byte)0x80 ; // pour toutes les commandes de transaction et initialisation
	final static byte ADMIN_CLA = (byte) 0xB0; //la classe de vérification de terminal et de la carte

	//Les instructions  de la RESTO_CLA
	final static byte HIST = (byte) 0x10;
	final static byte GETHIST=(byte) 0x12;
	
    final static byte VERIFY = (byte) 0x20;
    final static byte RECHARGE = (byte)0x30;
    final static byte DEBIT =(byte)0x40;
    final static byte CONSULTATION =(byte)0x50;
    final static byte ADMINISTRATION=(byte)0x60;
    
    //Les instructions de ADMIN_CLA
    
    final static byte SET_INFO = (byte)0x70; //la commande pour initialiser les infos utilisateur
    
    final static byte SET_LENGTHS=(byte)0x71;//Initialiser les tableaux de bytes contenant certificat, modulo et expo
    final static byte SET_CERT =(byte)0x72; //Initialiser le certificat
    final static byte SET_MODULUS=(byte)0x73;//Initialiser le modulo de la clé privée
    final static byte SET_EXPONENT=(byte)0x74;//Initialiser l'expo de la clé privée

    
    final static byte FIN_INIT=(byte)0x75;//Signal de fin d'initialisation
    
    final static byte GET_INFO_SIGN = (byte)0x76;//Récupération d'infos utilisateur
    
    final static byte GET_CERT_LENGTH=(byte)0x77;//Récupération de la longueur du certificat
    final static byte GET_CERT=(byte)0x78;//Récupération du certificat
    
    final static byte GET_AL_M_TER=(byte)0x7A; //Envoi message aléatoire crypté au terminal
    final static byte REP_DEC_M_TER=(byte)0x7B; //Récupération message décrypté
    
    final static byte GET_SIGN=(byte)0x7D; // Envoi de la signature
    
    final static byte GET_AES_KEY=(byte)0x7E;//Récupération de la clé
   
    

    
    //Les constantes
    final static short maxBalance = 0x7FFF;
    final static short maxTransactionMontant = 0x7FFF;
    		static short rechargeMontant =0;
    		static short debiteMontant = 0;
    final static byte nbEssaisPIN = (byte)0x03;
    final static byte maxTaillePIN = (byte)0x08;
    
    //Les réponses
    final static short SW_AUTHENTIFICATION_ECHOUE =  0x6300;
    final static short SW_PIN_AUTHENTIFICATION_REQUISE = 0x6301;
    final static short SW_PIN_ESSAI_NULL = 0x6302;
    final static short SW_INVALID_TRANSACTION_MONTANT = 0x6A83;
    final static short SW_MAXIMUM_BALANCE_DEPASSER = 0x6A84;
    final static short SW_NEGATIF_BALANCE = 0x6A85;
    final static short SW_VERIFY_TERMINAL = 0x6401;
    final static short SW_BAD_TERMINAL = 0x6402;
    
    //Le PIN
    OwnerPIN pin;
    
    //La balance
    short balance ;
    
    //le Montant en format de tableay
    byte [] montantArray;
    
    //Informations de personnalisation
    byte [] nom,prenom,matricule,filiere,certificat;
    byte  PINLong,matLong,nomLong,prenomLong,filiereLong,signLong,longEnvoi;
    
    //indice 
    byte index = ISO7816.OFFSET_CDATA;
    
    byte[] transac=new byte[2];
    
    //la clé public de l'administration : 
    
  //N (le modulo a 2048 bits)
  private static final byte[] rsaPrivateKeyModulus={(byte)0xda, (byte)0xfc, (byte)0x7a, (byte)0xc7, (byte)0xb3, (byte)0x1f, (byte)0xfb, (byte)0xca, (byte)0x91, (byte)0x82, (byte)0xe5, (byte)0x10, (byte)0xd4, (byte)0xf3, (byte)0xae, (byte)0x45, (byte)0xd5, (byte)0xd8, (byte)0x56, (byte)0x66, (byte)0x23, (byte)0xd6, (byte)0x81, (byte)0x9d, (byte)0x3d, (byte)0xa4, (byte)0xa9, (byte)0x76, (byte)0xb5, (byte)0x87, (byte)0xaf, (byte)0x30, (byte)0x02, (byte)0x5e, (byte)0x1e, (byte)0x82, (byte)0x4d, (byte)0x84, (byte)0x14, (byte)0x6b, (byte)0x13, (byte)0x8a, (byte)0x60, (byte)0x46, (byte)0x43, (byte)0x66, (byte)0x5e, (byte)0xb8, (byte)0x1a, (byte)0x5f, (byte)0x17, (byte)0xec, (byte)0x6c, (byte)0xab, (byte)0x8d, (byte)0x63, (byte)0xac, (byte)0xe1, (byte)0xbb, (byte)0x05, (byte)0x5c, (byte)0x4c, (byte)0xfb, (byte)0x83, (byte)0xbf, (byte)0x3b, (byte)0x9f, (byte)0xf4, (byte)0x0e, (byte)0x43, (byte)0x63, (byte)0x71, (byte)0xbf, (byte)0xc4, (byte)0xaf, (byte)0xfd, (byte)0x30, (byte)0x18, (byte)0xe6, (byte)0xc8, (byte)0xd2, (byte)0x12, (byte)0x79, (byte)0x4e, (byte)0xa1, (byte)0xe2, (byte)0x24, (byte)0x36, (byte)0xc4, (byte)0x3b, (byte)0xc8, (byte)0x68, (byte)0xb5, (byte)0x7d, (byte)0x5f, (byte)0x07, (byte)0xba, (byte)0x32, (byte)0xf6, (byte)0x7d, (byte)0x9e, (byte)0xfb, (byte)0x3d, (byte)0x66, (byte)0xea, (byte)0x16, (byte)0x2d, (byte)0x13, (byte)0xe0, (byte)0x91, (byte)0x4e, (byte)0x95, (byte)0xd2, (byte)0x01, (byte)0x98, (byte)0xf2, (byte)0x07, (byte)0x9d, (byte)0x63, (byte)0x08, (byte)0x0f, (byte)0xa3, (byte)0xc8, (byte)0xbb, (byte)0x20, (byte)0x4a, (byte)0x6e, (byte)0xa6, (byte)0x59, (byte)0xe2, (byte)0x09, (byte)0xd0, (byte)0x5b, (byte)0x22, (byte)0x05, (byte)0x1b, (byte)0xa6, (byte)0x10, (byte)0x5e, (byte)0x5e, (byte)0xa9, (byte)0x7c, (byte)0x70, (byte)0x0e, (byte)0x4f, (byte)0xdc, (byte)0xe1, (byte)0xf3, (byte)0xf7, (byte)0x14, (byte)0x2c, (byte)0x34, (byte)0x54, (byte)0xbc, (byte)0x5f, (byte)0x9a, (byte)0xd5, (byte)0xa0, (byte)0xfb, (byte)0xa1, (byte)0xe5, (byte)0xaa, (byte)0x2a, (byte)0x9d, (byte)0x8c, (byte)0xd1, (byte)0xc0, (byte)0x4a, (byte)0x8d, (byte)0xbf, (byte)0xac, (byte)0x31, (byte)0xb6, (byte)0x51, (byte)0x50, (byte)0xc1, (byte)0x51, (byte)0x8e, (byte)0x3a, (byte)0x1a, (byte)0xac, (byte)0x5c, (byte)0xca, (byte)0xf7, (byte)0xff, (byte)0xee, (byte)0x54, (byte)0x3f, (byte)0xa7, (byte)0x65, (byte)0x16, (byte)0xf3, (byte)0x44, (byte)0x74, (byte)0x09, (byte)0x9d, (byte)0xe4, (byte)0xa8, (byte)0xd8, (byte)0x30, (byte)0x22, (byte)0xc1, (byte)0xd1, (byte)0xe2, (byte)0x97, (byte)0xfc, (byte)0x13, (byte)0x6b, (byte)0xa0, (byte)0xd6, (byte)0x0d, (byte)0xea, (byte)0x7f, (byte)0x0e, (byte)0xe4, (byte)0xec, (byte)0xb6, (byte)0xcb, (byte)0x38, (byte)0xf9, (byte)0x92, (byte)0x63, (byte)0x19, (byte)0x47, (byte)0xb6, (byte)0xcd, (byte)0x96, (byte)0x0f, (byte)0x4b, (byte)0xa2, (byte)0xf4, (byte)0x6a, (byte)0x56, (byte)0xdb, (byte)0xb7, (byte)0x51, (byte)0x16, (byte)0x71, (byte)0x52, (byte)0x08, (byte)0xc3, (byte)0x04, (byte)0xa8, (byte)0xb6, (byte)0x38, (byte)0x9c, (byte)0x96, (byte)0xf2, (byte)0x3e, (byte)0x80, (byte)0x31, (byte)0x85, (byte)0x46, (byte)0x2d, (byte)0x9a, (byte)0x7d};
  	//E
    private static final byte[] rsaPublicKeyExponent = {
        (byte) 0x01, (byte) 0x00, (byte) 0x01 };
    
    //tableau qui contient le message aléatoire
    
    byte [] tmp = new byte[(short)10];
    
    //Tableau qui contient les données pour clé AES
    byte[] dataAES=new byte[16];
    
    //Objet pour effectué les opérations de cryptage
    private Cipher cipher, cryptAES;
   
    
    //la clé publique du terminal
  	private static  RSAPublicKey publicKeyTerminal ;
  	
  	//la clé privée de la carte
  	private static RSAPrivateKey privateKeyCard;

    //booléen pour vérifier si le terminal est authentique
    private boolean isGood = false ;
   
    //booléen pour indiquer si l'instance est créer 
    private boolean init = false ;
    
    //générateur de données aléatoires
   private RandomData rand,randomAES ;
   
   //Tableau contenant la signature
   byte[] donneesSignees=new byte[256];
   
   //Tableau contenant le message aléatoire crypté à envoyer au terminal
   byte[] dataEncrypted=new byte[256];
   
   //Tableau contenant la clé de session AES cryptée
   byte[] dataAESEncrypted=new byte[256];
   
   byte[] modulo=new byte[256];
   byte[] exponent=new byte[256];
   
   //Longueur du certificat
   byte[] lengths=new byte[2];
   
   byte[] encryptedD;
   
   byte[] encryptedAES=new byte[64];
   byte[] decryptedAES=new byte[64];
   
   byte[] enc,dec;
   
   byte number,restant;
   
   byte[] temporary=new byte[16];
   AESKey symKey;
    
   byte[] hist1=new byte[8];
   
    private JavaResto (byte[] bArray,short bOffset,byte bLength)  // Constructeur
    {
        pin = new OwnerPIN(nbEssaisPIN,maxTaillePIN);
        
        publicKeyTerminal = (RSAPublicKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PUBLIC, KeyBuilder.LENGTH_RSA_2048, false);
        publicKeyTerminal.setExponent(rsaPublicKeyExponent, (short) 0,(short) rsaPublicKeyExponent.length);
        publicKeyTerminal.setModulus(rsaPrivateKeyModulus, (short) 0,(short) rsaPrivateKeyModulus.length);
        
        cipher = Cipher.getInstance(Cipher.ALG_RSA_PKCS1, false);
        rand = RandomData.getInstance(RandomData.ALG_PSEUDO_RANDOM);
        randomAES = RandomData.getInstance(RandomData.ALG_SECURE_RANDOM);
        
        montantArray = new byte[(short)2];
        
    	cryptAES = Cipher.getInstance(Cipher.ALG_AES_BLOCK_128_ECB_NOPAD, false);

        register(); //Enregistrement de l'applet

    }//Fin du constructeur 
    
    public static void install(byte[] bArray, short bOffset, byte bLength) //Installation de l'applet
    {
    	
        new JavaResto(bArray, bOffset, bLength);
    } 

    public boolean select() 
    {
        return true;     
    }

    
    public void deselect() 
    {
        
        pin.reset(); //Réinitialisation des paramétres du PIN
    }

    
    public void process(APDU apdu)
    {
    	byte [] buffer = apdu.getBuffer();
    	
	   	 if (selectingApplet()) // Répondre avec l'état de l'applet (initialisée ou non)
	   	 {
	   		 if(init)
	   			 buffer[0] = (byte)1 ;
	   		 else
	   			 buffer[0] = (byte)	2;
	   		 
	    		apdu.setOutgoingAndSend((short)0,(short)(1));

	   		 
	    		return;
	   	 }

	   	if(buffer[ISO7816.OFFSET_CLA] != ADMIN_CLA && buffer[ISO7816.OFFSET_CLA] != RESTO_CLA )
	   	{
	   		ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
	   	}
	   	 
    	 if(buffer[ISO7816.OFFSET_CLA] == ADMIN_CLA)
    	 {
    		 switch(buffer[ISO7816.OFFSET_INS])
    		 {
    		 case SET_INFO: 
    			 // initialisation des infos utilisateur
    				  init(buffer);
    			 break;
    			 
    		 case SET_LENGTHS : //Initialisation de la longueur du certificat
    			 setLengthCert(buffer);
 				 break;
    			 
    		 case SET_CERT : // Initialisation du tableau contenant le certificat
    			 setCert(buffer);
 				 break;
 			 case SET_MODULUS : //Initialisation du tableau contenant le modulo de la clé privée de la carte
 				setPrivParam(buffer,modulo);
 				 break;
 			 case SET_EXPONENT : //Initialisation du tableau contenant l'exposant de la clé privée de la carte
 				setPrivParam(buffer,exponent);
 				 break;
    			 
 			 case FIN_INIT : //Fin de l'envoi des infos d'initialisation
 				finInit();
 				 break;
    		 case GET_INFO_SIGN : //Envoi des infos signature    			
    			 getInfosSign(apdu,buffer);
    				 break;
    				 
    		 case GET_SIGN: // Envoi de la signature
    			 getSign(apdu, buffer);
				 break;
				 
    			 case GET_CERT_LENGTH : // Envoi de la longueur du certificat
    				 getCertLength(apdu,buffer);
 	 	    		break;
  			      
    			 case GET_CERT : //Envoi du certificat
    				 getCert(apdu,buffer);
    				 break;
    				 
    				 
    			 case GET_AES_KEY : //Génération et envoi d'un clé de session AES		 
    				 getAESKey(apdu,buffer);
    				 
    				 break;
    				
    		 case GET_AL_M_TER : //Envoi message crypté au terminal
    			 
    			 getAlM(apdu,buffer);
             break ;
             
    		 case REP_DEC_M_TER : //Récupération du message décrypté, comparaison et authentification
    			 repAlM(buffer);
    		break ;
    		
    		 case ADMINISTRATION : //Réinitialisation du PIN
    	         			changePIN(apdu);   		 
    	         	break;
    
    			 default :
    				 ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
    			 
    		 }
    		 	 return ;
    	 }
    	 
    	 
    	 if(isGood == false ) //il faut mettre a jour a chaque connexion
    	 {
    		 ISOException.throwIt(SW_BAD_TERMINAL);
    	 }
    	 
         if (buffer[ISO7816.OFFSET_CLA] != RESTO_CLA)
         {
        	 ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
         }
         
         if ( pin.getTriesRemaining() == 0  )
    	 {
    		 ISOException.throwIt(SW_PIN_ESSAI_NULL);
    	 }
         
         if(!pin.isValidated() && buffer[ISO7816.OFFSET_INS] != VERIFY ) 
         {
    		 ISOException.throwIt(SW_PIN_AUTHENTIFICATION_REQUISE);
         }
         
         
         
         switch(buffer[ISO7816.OFFSET_INS])
         {
        
         case CONSULTATION :
        	 switch(buffer[ISO7816.OFFSET_P1])
        	 {
        	 case 0x00 :
        		 getBalance(apdu);
        		 return;
        	 case 0x01:
        		 getInfo(apdu,encrypt(matricule));
        		 return;
        	 case 0x02 :
        		 getInfo(apdu,encrypt(nom));
        		 return;
        	 case 0x03 :
        		 getInfo(apdu,encrypt(prenom));
        		 return;
        	 case 0x04 :
        		 getInfo(apdu,encrypt(filiere));
        		 return;
        	 case 0x06 :
        		 getInfo(apdu,certificat);
        		 return;
        	 default :
             ISOException.throwIt(ISO7816.SW_INCORRECT_P1P2);

        	 }
        	 return;
         case DEBIT :
        	 debit(apdu);
        	 return;
         case RECHARGE :
        	 recharge(apdu);
        	 return;
         case VERIFY : //Vérification du PIN
        	 verify(apdu);
        	 return;
         case HIST : //Actualisation de l'historique
        	 setHist(buffer);
        	 return;
         case GETHIST : //Envoi de l'historique
        	 getHist(apdu,buffer);
        	 return;
         default :
        	 ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
         }

    } //Fin de la méthode process 
    
/*******************************************************************************************************************************/
    public void setHist(byte[] buffer) //Actualisation de l'historique
    {
    	
    	enc=new byte[buffer[(short)ISO7816.OFFSET_LC]];
    	dec=new byte[buffer[(short)ISO7816.OFFSET_LC]];
    	Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, enc, (short)0, buffer[(short)ISO7816.OFFSET_LC]);
    	dec=decrypt(enc,dec);
    	Util.arrayCopy(dec, (short)(0), hist1, (short)0, (short)8);
    	
    	
    }
    
    /*******************************************************************************************************************************/    
    public void getHist(APDU apdu,byte[] buffer) //Envoi de l'historique
    {
    	enc=new byte[(short)hist1.length];
    	dec=new byte[(short)hist1.length];
    	
    		Util.arrayCopy(hist1, (short)0, dec, (short)0, (short)hist1.length);
    		enc=encrypt(dec);
    		Util.arrayCopy(enc, (short)0, buffer, (short)0, (short)enc.length);
    	apdu.setOutgoingAndSend((short)0,(short)enc.length);
    	
    }
    /*******************************************************************************************************************************/
    public void recharge(APDU apdu) 
    {
    	
    	
    	byte [] buffer = apdu.getBuffer();
    	
    	enc=new byte[buffer[(short)ISO7816.OFFSET_LC]];
    	dec=new byte[buffer[(short)ISO7816.OFFSET_LC]];
    	
    	Util.arrayCopy(buffer, (short)(ISO7816.OFFSET_CDATA), enc, (short)0, buffer[(short)ISO7816.OFFSET_LC]);
    	dec=decrypt(enc,dec);
    	
    	transac[0]=dec[0];
    	transac[1]=dec[1];
    	
    	rechargeMontant = ByteArrayToShort(transac);
    	if((rechargeMontant > maxTransactionMontant) || (rechargeMontant < 0)) //Vérifier que rechargeMontant< 127 et > 0
    	{
    		ISOException.throwIt(SW_INVALID_TRANSACTION_MONTANT);
    	}
    	
    	if((short)(rechargeMontant + balance ) <0) //vérifier que la nouvelle balance est < a maxBalance
    	{
    		ISOException.throwIt(SW_MAXIMUM_BALANCE_DEPASSER);
    	}
    	 
    	balance = (short)(balance + rechargeMontant); // Valider la nouvelle balance
    	
    }//Fin de la méthode recharge()
    
    /*******************************************************************************************************************************/
    private void debit(APDU apdu) 
    {
    	
    	byte [] buffer = apdu.getBuffer();
    	
    	enc=new byte[buffer[(short)ISO7816.OFFSET_LC]];
    	dec=new byte[buffer[(short)ISO7816.OFFSET_LC]];


    	Util.arrayCopy(buffer, (short)(ISO7816.OFFSET_CDATA), enc, (short)0, buffer[(short)ISO7816.OFFSET_LC]);
    	dec=decrypt(enc,dec);
    	
    	transac[0]=dec[0];
    	transac[1]=dec[1];
    			
    	debiteMontant = ByteArrayToShort(transac);
    	if((debiteMontant > maxTransactionMontant) || (debiteMontant < 0))
    	{
    		ISOException.throwIt(SW_INVALID_TRANSACTION_MONTANT);
    	}
    	
    	if ( (short)( balance - debiteMontant ) < (short)0 )
    	{
            ISOException.throwIt(SW_NEGATIF_BALANCE);
    	}
    	
        balance = (short) (balance - debiteMontant);
        
        
    }// Fin de la méthode debit
    
    /*******************************************************************************************************************************/ 
    public void getBalance(APDU apdu) //Envoi du montant de la balance
    {
        byte[] buffer = apdu.getBuffer();
        byte[] balanceArray=new byte[2];
        
        balanceArray[0] = (byte)(balance >> 8);
        balanceArray[1] = (byte)(balance & 0xFF);
        
        byte[] toSend=encrypt(balanceArray);
        Util.arrayCopy(toSend, (short)0, buffer, (short)0, (short)toSend.length);
        apdu.setOutgoingAndSend((short)0,(short)(toSend.length));
   }//Fin de la méthode getBalance
    /*******************************************************************************************************************************/
    private void getInfo(APDU apdu, byte [] tab) //Envoi du tableau 
    {
    	try{
    	byte [] buffer = apdu.getBuffer();
    	Util.arrayCopy(tab, (short)0, buffer, (short)0,(short)(tab.length));
		
    	}catch(NullPointerException e) {
	            ISOException.throwIt((short)0x6800);}
	            catch(ArrayIndexOutOfBoundsException e) {
	                    ISOException.throwIt((short)0x6801);}
	            catch(CryptoException e) {
	                    ISOException.throwIt((short)(0x6810+e.getReason()));}
	            catch(SecurityException e) {
	                    ISOException.throwIt((short)(0x6820));
	            }
    	apdu.setOutgoingAndSend((short)0,(short)(tab.length));
    	
    }//Fin de la méthode getInfo
     
    /*******************************************************************************************************************************/
    private void verify(APDU apdu)  //Vérification du PIN
    {
        byte[] buffer = apdu.getBuffer();
       
        if ( pin.check(buffer, (short)ISO7816.OFFSET_CDATA,(byte)buffer[ISO7816.OFFSET_LC]) == false )
        {
            ISOException.throwIt(SW_AUTHENTIFICATION_ECHOUE);

        }
    }//Fin de la méthode verify
    
    private void changePIN(APDU apdu)
    {
    	byte[] buffer = apdu.getBuffer();
    	enc=new byte[buffer[ISO7816.OFFSET_LC]];
        dec=new byte[buffer[ISO7816.OFFSET_LC]];
        
        byte[] newPIN=new byte[buffer[ISO7816.OFFSET_P2]];
        
    	Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, enc, (short)0, buffer[ISO7816.OFFSET_LC]);
        dec=decrypt(enc,dec);
        
        Util.arrayCopy(dec, (short)0, newPIN, (short)0, (short)newPIN.length);
        pin.update(newPIN, (short)0,(byte)newPIN.length);
        pin.check(newPIN, (short)0,(byte)newPIN.length);
    }//Fin de la méthode changepin
   
    /*******************************************************************************************************************************/
    protected static short ByteArrayToShort(byte [] x)
	{
		short res = (short)((x[1]<<8) + (x[0] & 0xFF));
		return res ;
	}
    
    /*******************************************************************************************************************************/
    
    protected byte[] encrypt(byte[] data) //Cryptage AES
    {
    	try{
    	cryptAES.init(symKey, Cipher.MODE_ENCRYPT);
    	
    	number=(byte)(data.length/16);
    	restant=(byte)(data.length%16);
    	
    	if(restant!=(byte)0)
    		number++;
    	for(byte s=0;s<number;s++)
    	{
    		
    		if((s==(byte)(number-1)) & (restant!=(byte)0))
    		{
    			Util.arrayCopy(data, (short)(16*s), temporary, (short)(16*s), (short)restant);
    			for(byte add=(byte)(restant);add<(byte)16;add++)
    				temporary[add]=(byte)0;
    		}
    		else
    			Util.arrayCopy(data, (short)(16*s), temporary, (short)0, (short)temporary.length);
    			
    		
    		cryptAES.doFinal(temporary, (short)0, (short)temporary.length, encryptedAES, (short)(16*s));
    	}
    	
    	}catch(NullPointerException e) {
            ISOException.throwIt((short)0x6800);}
            catch(ArrayIndexOutOfBoundsException e) {
                    ISOException.throwIt((short)0x6805);}
            catch(CryptoException e) {
                    ISOException.throwIt((short)0x6810);}
            catch(SecurityException e) {
                    ISOException.throwIt((short)(0x6820));}
    	return encryptedAES;
    }
    
    /*******************************************************************************************************************************/
    protected byte[] decrypt(byte[] data, byte[] output) //Décryptage AES
    {
    	try{
    	cryptAES.init(symKey, Cipher.MODE_DECRYPT);
    	cryptAES.doFinal(data, (short)0, (short)data.length, output, (short)0);
    }catch(NullPointerException e) {
        ISOException.throwIt((short)0x6800);}
        catch(ArrayIndexOutOfBoundsException e) {
                ISOException.throwIt((short)0x6805);}
        catch(CryptoException e) {
                ISOException.throwIt((short)0x6810);}
        catch(SecurityException e) {
                ISOException.throwIt((short)(0x6820));
        } 
    	return output;
    }
    
    /*******************************************************************************************************************************/
    private void getAESKey(APDU apdu, byte[] buffer) //Génération et envoi de la clé AES
    {
    	if(buffer[ISO7816.OFFSET_P1]!=(byte)2)
		 {
			 if(buffer[ISO7816.OFFSET_P1]==(byte)0)
			 {
		//Création de la clé de session AES
		 randomAES.generateData(dataAES, (short)0, (short)dataAES.length);
		 symKey = (AESKey) KeyBuilder.buildKey (KeyBuilder.TYPE_AES, KeyBuilder.LENGTH_AES_128, false);
		 symKey.setKey(dataAES, (short)0);
		 
		 //Cryptage de la clé avec la clé publique du terminal
		 cipher.init(publicKeyTerminal, Cipher.MODE_ENCRYPT);
     	 cipher.doFinal(dataAES,(short)0, (short)dataAES.length, dataAESEncrypted, (short) 0);
     	 
			 }
			 
         	 longEnvoi=(byte)127;
			 }
			 else
				 longEnvoi=(byte)2;
		 
		 Util.arrayCopy(dataAESEncrypted, (short)(buffer[ISO7816.OFFSET_P1]*127),buffer,(short)0,longEnvoi); 				 
    		apdu.setOutgoingAndSend((short)0,(short)longEnvoi);
    }
    /*******************************************************************************************************************************/
    private void getSign(APDU apdu, byte[] buffer) //Envoi de la signature
    {
    	byte  longEnvoiJC;
		 if(buffer[ISO7816.OFFSET_P1]!=(byte)2)
		 {
     	 longEnvoiJC=(byte)127;
		 }
		 else
			 longEnvoiJC=(byte)2;
		 Util.arrayCopy(donneesSignees, (short)(buffer[ISO7816.OFFSET_P1]*127),buffer,(short)0,longEnvoiJC); 				 
		apdu.setOutgoingAndSend((short)0,(short)longEnvoiJC);
    }
    /*******************************************************************************************************************************/
    
    private void repAlM(byte[] buffer) //Récupération du message aléatoire décrypté et comparaison
    {
    	if( Util.arrayCompare(buffer, ISO7816.OFFSET_CDATA, tmp, (short)0, (short)tmp.length) == (byte)0)
		 {
			 isGood = true ;
			 
		 }
		 else
		 {
			 ISOException.throwIt(SW_BAD_TERMINAL);
		 }
    	
    }
    /*******************************************************************************************************************************/
    private void getAlM(APDU apdu, byte[] buffer) //génération d'un message aléatoire, cryptage avec la clé publique du terminal et envoi
    {

		 if(buffer[ISO7816.OFFSET_P1]!=(byte)2)
		 {
			 if(buffer[ISO7816.OFFSET_P1]==(byte)0)
			 {
				 rand.generateData (tmp , (short)0 ,(short)tmp.length ) ;
	         	 cipher.init(publicKeyTerminal, Cipher.MODE_ENCRYPT);
	         	 cipher.doFinal(tmp,(short)0, (short)tmp.length, dataEncrypted, (short) 0);
			 }
		 
     	 longEnvoi=(byte)127;
		 }
		 else
			 longEnvoi=(byte)2;
			
		 Util.arrayCopy(dataEncrypted, (short)(buffer[ISO7816.OFFSET_P1]*127),buffer,(short)0,longEnvoi); 				 
		apdu.setOutgoingAndSend((short)0,(short)longEnvoi);
    }
    /*******************************************************************************************************************************/ 
    private void getCert(APDU apdu,byte[] buffer) //Envoi du certificat 
    {
    	short lon;
		 byte number=buffer[ISO7816.OFFSET_P1];
		 
		 if(number!=(byte)(lengths[0]))
		 lon=(short)127;
		 else
		 lon=(short)lengths[1];
		 
		 try{
		 Util.arrayCopy(certificat,(short)(127*number),buffer,(short)0,lon);
		 }catch(NullPointerException e) {
	            ISOException.throwIt((short)0x6800);}
	            catch(ArrayIndexOutOfBoundsException e) {
	                    ISOException.throwIt((short)0x6801);}
	            catch(CryptoException e) {
	                    ISOException.throwIt((short)(0x6810+e.getReason()));}
	            catch(SecurityException e) {
	                    ISOException.throwIt((short)(0x6820));
	            }
		 apdu.setOutgoingAndSend((short)0,(short)lon);
    }
    /*******************************************************************************************************************************/
    private void getCertLength(APDU apdu, byte[] buffer) //Envoi de la longueur du certificat
    {
    	Util.arrayCopy(lengths, (short)(0),buffer,(short)(0),(short)0x02);
  		apdu.setOutgoingAndSend((short)0,(short)0x02);
    }
    /*******************************************************************************************************************************/
    private void getInfosSign(APDU apdu, byte[] buffer) //Envoi des infos de la signature
    {
    	encryptedD=encrypt(matricule);
        Util.arrayCopy(encryptedD, (short)0, buffer, (short)0, (short)encryptedD.length);
        
  		apdu.setOutgoingAndSend((short)0,(short)(encryptedD.length));
    }
    /*******************************************************************************************************************************/ 
    protected void finInit() //Signal de fin d'initialisation
    {
    	privateKeyCard = (RSAPrivateKey) KeyBuilder.buildKey(KeyBuilder.TYPE_RSA_PRIVATE, KeyBuilder.LENGTH_RSA_2048, false);
			privateKeyCard.setExponent(exponent, (short) 0,(short) (exponent.length));
			privateKeyCard.setModulus(modulo, (short) 0,(short) (modulo.length));
			
			
			//Génération de la signature
	        try{
	        Signature signData=Signature.getInstance(Signature.ALG_RSA_SHA_PKCS1, false);
	        signData.init(privateKeyCard, Signature.MODE_SIGN);
	        signData.sign(matricule, (short)0, (short)matricule.length, donneesSignees, (short)0);
	        }catch(NullPointerException e) {
	            ISOException.throwIt((short)0x6800);}
	            catch(ArrayIndexOutOfBoundsException e) {
	                    ISOException.throwIt((short)0x6801);}
	            catch(CryptoException e) {
	                    ISOException.throwIt((short)(0x6810+e.getReason()));}
	            catch(SecurityException e) {
	                    ISOException.throwIt((short)(0x6820));
	            }
	       init = true ;
    }
    /*******************************************************************************************************************************/
    protected void setPrivParam(byte[] buffer,byte[] param) //Initialisation du modulo ou exposant de la clé privée de la carte
    {
    	try{
			 Util.arrayCopy(buffer, (short)ISO7816.OFFSET_CDATA,encryptedAES,(short)0,(byte)0x40);
			decryptedAES=decrypt(encryptedAES,decryptedAES);
			Util.arrayCopy(decryptedAES, (short)0,param,(short)(buffer[ISO7816.OFFSET_P1]*64),(byte)0x40);
		 }catch(NullPointerException e) {
		        ISOException.throwIt((short)0x6800);}
		        catch(ArrayIndexOutOfBoundsException e) {
		                ISOException.throwIt((short)0x6805);}
    }
    
    /*******************************************************************************************************************************/
    protected void setCert(byte[] buffer)
    {
    	 //Initialisation du certificat
    	Util.arrayCopy(buffer, (short)ISO7816.OFFSET_CDATA,certificat,(short)(buffer[ISO7816.OFFSET_P1]*127),(byte)buffer[ISO7816.OFFSET_LC]);
    }
    /*******************************************************************************************************************************/
    protected void setLengthCert(byte[] buffer) //Récupération de la longueur du certificat
    {
    	Util.arrayCopy(buffer,(short)ISO7816.OFFSET_CDATA,lengths,(short)0,(short)2);
		 certificat=new byte[(short)(lengths[0]*127+lengths[1])];
    }
    /*******************************************************************************************************************************/
    protected void init(byte[] buffer) //Initialisation des infos utilisateur
    {
    	try{
			  enc=new byte[(byte)buffer[ISO7816.OFFSET_P2]];
			  dec=new byte[(byte)buffer[ISO7816.OFFSET_P2]];
	Util.arrayCopy(buffer, (short)ISO7816.OFFSET_CDATA,enc,(short)0,(byte)buffer[ISO7816.OFFSET_P2]);
	dec=decrypt(enc,dec);
	
		index = 0;

	
   PINLong = dec[index]; // PIN taille
   pin.update(dec, (short)(index+1), PINLong); //Initialisation de PIN

   index =(byte) (index+PINLong+1);
   matLong = dec[index]; // Longueur matricule 
   matricule = new byte[(short)matLong];
   Util.arrayCopy(dec, (short)(index +1),matricule,(short)0,matLong); //Initialisation de matricule

   index = (byte) (index+matLong+1);
   nomLong = dec[index]; // Longueur nom 
   nom = new byte[(short)nomLong];
   Util.arrayCopy(dec, (short)(index +1),nom,(short)0,nomLong); //Initialisaton du nom
   
   index = (byte) (index+nomLong+1);
   prenomLong = dec[index]; // Longueur prénom 
   prenom = new byte[(short)prenomLong]; 
   Util.arrayCopy(dec, (short)(index +1),prenom,(short)0,prenomLong); //Initialisation du prénom

   index = (byte) (index+prenomLong+1); 
   filiereLong = dec[index]; // Longueur filière
   filiere = new byte[(short)filiereLong];
   Util.arrayCopy(dec, (short)(index +1),filiere,(short)0,filiereLong); //Initialisation de la filière
  
   
		  }catch(NullPointerException e) {
	          ISOException.throwIt((short)0x6800);}
	          catch(ArrayIndexOutOfBoundsException e) {
	                  ISOException.throwIt((short)buffer[ISO7816.OFFSET_P2]);}
	          catch(CryptoException e) {
	                  ISOException.throwIt((short)(0x6810));}
	          catch(SecurityException e) {
	                  ISOException.throwIt((short)(0x6820));
	          }
    }
}
