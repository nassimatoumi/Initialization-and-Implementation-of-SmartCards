package adminTool;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.swing.JOptionPane;

/*calsse utilisée pour envoyer un APDU > 127 byte */

public class EnvoiMultiple {
	
	boolean reussi;
	
	public EnvoiMultiple(CardChannel canal, byte[] certificat,int y)
	{
		byte[] lengths=new byte[2];
		lengths[0]=(byte)(certificat.length/127);
		lengths[1]=(byte)(certificat.length%127);
		
		
		CommandAPDU certApdu = new CommandAPDU(0xB0,0x71,0x00,0x00,lengths);
		ResponseAPDU setCertReponse;
		try {
			setCertReponse = canal.transmit(certApdu);
			if (setCertReponse.getSW() != 0x9000) 
			{
	            JOptionPane.showMessageDialog(null,"Erreur lors de l'envoi des longueurs" + setCertReponse.getSW() , "Problème !", JOptionPane.ERROR_MESSAGE);
	            reussi=false;
			} 
			else
				reussi=true;
			
		} catch (CardException e) {
			
		}
	}
	
	
	public EnvoiMultiple(CardChannel canal,byte[] sendArray)
	{
		byte [] bloc;
		
		int nombre=sendArray.length/127;
		int tailleDern= sendArray.length%127;
		
		if(tailleDern!=0)
			nombre++;
		
		for(int s=0;s<nombre;s++)
		{
				if(tailleDern!=0 && s==nombre-1)
					bloc=new byte[tailleDern];
				else
					bloc=new byte[127];
				
			
			for(int t=0;t<bloc.length;t++)
			{
					bloc[t]=sendArray[127*s+t];
					
			}
			
			CommandAPDU certApdu = new CommandAPDU(0xB0,0x72,(byte)(s),0x00,bloc);
			ResponseAPDU setCertReponse;
			try {
				setCertReponse = canal.transmit(certApdu);
				if (setCertReponse.getSW() != 0x9000) 
				{
		            JOptionPane.showMessageDialog(null,"Erreur lors de l'envoi"+0x72+", bloc N° "+s+"  " + setCertReponse.getSW() , "Problème !", JOptionPane.ERROR_MESSAGE);
		            reussi=false;
				} 
				else
				{
					reussi=true;
				}
			} catch (CardException e) {
				
				
			}
			
			
		}
	}
	
	
	public EnvoiMultiple(CardChannel canal,byte[] sendArray,byte parametre) throws NoSuchAlgorithmException, NoSuchPaddingException
	{
		byte [] bloc=new byte[64];
		
		int nombre=sendArray.length/64;
		
		for(int s=0;s<nombre;s++)
		{
			for(int t=0;t<bloc.length;t++)
			{
					bloc[t]=sendArray[64*s+t];
					
			}
			
			CommandAPDU certApdu = new CommandAPDU(0xB0,parametre,(byte)(s),0x00,CardManagement.AESEncrypt(bloc));
			ResponseAPDU setCertReponse;
			try {
				setCertReponse = canal.transmit(certApdu);
				if (setCertReponse.getSW() != 0x9000) 
				{
		            JOptionPane.showMessageDialog(null,"Erreur lors de l'envoi"+parametre+", bloc N° "+s+"  " + setCertReponse.getSW() , "Problème !", JOptionPane.ERROR_MESSAGE);
		            reussi=false;
				} 
				else
				{
					reussi=true;
				}
			} catch (CardException e) {
				
				
			}
			
			
		}
	}
	
	public boolean succeeded()
	{
		return reussi;
	}

}
