package pack;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISO7816;
import javacard.framework.ISOException;
import javacard.framework.OwnerPIN;


public class Test extends Applet {
	private short compteur=(short)0x1122;
	
	private final static byte cls=(byte)0xB0;
	private final static byte inc=(byte)0x00;
	private final static byte dec=(byte)0x01;
	private final static byte check=(byte)0x02;
	
	private final static byte verify=(byte)0x03;
	private final static byte update=(byte)0x04;
	
	private final static byte verpuk=(byte)0x05;
	
	private final static byte shownom=(byte)0x07;
	private final static byte showprenom=(byte)0x08;
	private final static byte showmat=(byte)0x09;
	private final static byte showdate=(byte)0x0A;
	
	private final static byte BUY_LIMIT =(byte)0x64;
	private final static byte DEC_LIMIT =(byte)0x24;
	private final static short COMPT_LIMIT = (short) 0x7FFF;
	
	private final static byte PIN_TRY_LIMIT =(byte)0x05;
	private final static byte PIN_MAX_SIZE=(byte)0x08;
	
	private final static byte PUK_MAX_SIZE= (byte)0x0C;
	
	private final static short SW_VERIFICATION_FAILED=(short)0x6300;
	private final static short SW_PIN_VERIFICATION_REQUIRED=(short)0x6301;
	private final static short SW_PIN_UPDATE_NOT_PERMITTED=(short)0x6302;
	private final static short SW_NO_PIN_TRIES_LEFT=(short)0x6303;
	
	private final static short SW_BUY_LIMIT_REACHED=(short)0x6A00;
	private final static short SW_BALANCE_LIMIT_REACHED=(short)0x6A01;
	private final static short SW_DEC_LIMIT_REACHED=(short)0x6A02;
	private final static short SW_BALANCE_NON_SUFFICIENT=(short)0x6A03;
	private final static short SW_NEGATIVE_AMOUNT=(short)0x6A04;
	
    
	private OwnerPIN pin;
	private OwnerPIN puk;
	
	private byte [] nom;
	private byte [] prenom;
	private byte [] matricule;
	private byte [] date;

	
	private Test(byte bArray[], short bOffset, byte bLength) {
		
		pin = new OwnerPIN(PIN_TRY_LIMIT,PIN_MAX_SIZE);
		puk = new OwnerPIN((byte)0x01,PUK_MAX_SIZE);

		
		byte aidLong = bArray[bOffset]; // Longueur AID 
        bOffset = (short) (bOffset+aidLong+1);
        byte infoLong = bArray[bOffset]; // Longueur Info
        bOffset = (short) (bOffset+infoLong+1);
        byte dataLong = bArray[bOffset]; //Longueur Applet Data 
        
        puk.update(bArray, (short)(bOffset+1), dataLong);
        
        bOffset = (short)(bOffset+dataLong+1);
        byte pukl= bArray[bOffset];
        pin.update(bArray, (short)(bOffset+1), pukl); //Initialisation du PIN

        bOffset = (short)(bOffset+pukl+1);
        byte noml= bArray[bOffset];
        nom= new byte [(short)noml];
        nom=registerinfo(bArray,bOffset,(short)noml);
        
        bOffset=(short)(bOffset+noml+1);
        byte prenoml=bArray[bOffset];
        prenom= new byte[(short)prenoml];
        prenom=registerinfo(bArray,bOffset,(short)prenoml);
        
        bOffset=(short)(bOffset+prenoml+1);
        byte matl=bArray[bOffset];
        matricule= new byte[(short)matl];
        matricule=registerinfo(bArray,bOffset,(short)matl);
        
        bOffset=(short)(bOffset+matl+1);
        byte datel= bArray[bOffset];
        date= new byte[(short)datel];
        date=registerinfo(bArray,bOffset,(short)datel);
        
        
        
        register();
		
	}

	public static void install(byte bArray[], short bOffset, byte bLength)
			throws ISOException {
		new Test(bArray, bOffset, bLength);
	}

	public void process(APDU apdu) throws ISOException {
		byte []buffer=apdu.getBuffer();
		if (this.selectingApplet())return;
		if(buffer[ISO7816.OFFSET_CLA] != cls) 
			ISOException.throwIt(ISO7816.SW_CLA_NOT_SUPPORTED);
			

		switch (buffer[ISO7816.OFFSET_INS]){
		case inc :  inc (apdu,buffer); break;
		
		case dec : dec(apdu,buffer); break;
		
		case check : check(apdu,buffer); break;
		
		case verify : verify(apdu,buffer); break;
		
		case update : update(apdu,buffer); break;
		
		case shownom : getinfo(apdu,buffer,nom); break;
		
		case showprenom : getinfo(apdu,buffer,prenom); break;
		
		case showmat : getinfo(apdu,buffer,matricule); break;
		
		case showdate : getinfo(apdu,buffer,date); break;
		
		case verpuk : verpuk (apdu,buffer); break;
		
		default :
			ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}

			
	}
	
	public boolean select() 
    {
     
        if ( puk.getTriesRemaining() == 0 ) //réfuser la sélection si le nombre des essais restants == 0
           return false;
        
        return true;     
    }

    
    public void deselect() 
    {
        
        pin.reset(); //réinitialisation des paramétres de PIN
    }
	
	
	private byte[] registerinfo(byte bArray[], short bOffset,short len)
	{
		byte [] info = new byte[len];
		for(short i=0; i<len;i++)
		{
			info[i]=bArray[(short)(bOffset+1+i)];
		}
		return info;
	}
	
	
	private void inc(APDU apdu,byte []buffer)
	{
		
		if(!pin.isValidated())
		ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
		
		byte byteRead=(byte)apdu.setIncomingAndReceive();
		byte numBytes = buffer[ISO7816.OFFSET_LC];
		 
		 if ( ( numBytes != 1 ) || (byteRead != 1) )
	            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		 
		byte amount = buffer[ISO7816.OFFSET_CDATA];
		
		if(amount<0)
			ISOException.throwIt(SW_NEGATIVE_AMOUNT);
		
		if(amount > BUY_LIMIT)
			ISOException.throwIt(SW_BUY_LIMIT_REACHED);
		
		if((short)(compteur+(short)amount) > COMPT_LIMIT)
			ISOException.throwIt(SW_BALANCE_LIMIT_REACHED);
		
		compteur+= (short)amount;
	}
	
	private void dec(APDU apdu,byte []buffer)
	{
		
		if(!pin.isValidated())
		ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
		
		byte byteRead=(byte)apdu.setIncomingAndReceive();
		byte numBytes = buffer[ISO7816.OFFSET_LC];
		 
		 if ( ( numBytes != 1 ) || (byteRead != 1) )
	            ISOException.throwIt(ISO7816.SW_WRONG_LENGTH);
		 
		byte amount=buffer[ISO7816.OFFSET_CDATA];
		
		if(amount<0)
			ISOException.throwIt(SW_NEGATIVE_AMOUNT);
		
		if(amount > DEC_LIMIT)
			ISOException.throwIt(SW_DEC_LIMIT_REACHED);
		
		if(compteur<amount )
			ISOException.throwIt(SW_BALANCE_NON_SUFFICIENT);
		
		compteur-= (short)amount;
	}
	
	private void check (APDU apdu,byte []buffer)
	{
		
		if(!pin.isValidated())
			ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
		
		
		buffer[0] = (byte)((compteur >> 8) & 0xff);
		buffer[1] = (byte)(compteur & 0xff) ;
		apdu.setOutgoingAndSend((short) 0, (short) 2);
	}
	
	
	
	private void getinfo(APDU apdu,byte[] buffer,byte[] info)
	{
		if(!pin.isValidated())
		ISOException.throwIt(SW_PIN_VERIFICATION_REQUIRED);
		 
		for(short i=0;i<(short)(info.length);i++)
		{
		buffer[i]=info[i];
		}
		apdu.setOutgoingAndSend((short) 0, (short) (info.length));	
	}
	
	private void verify (APDU apdu,byte []buffer)
	{
		
		byte len = (byte)apdu.setIncomingAndReceive();
		
		if(pin.getTriesRemaining()==0)
			ISOException.throwIt(SW_NO_PIN_TRIES_LEFT);
		
		if(!pin.check(buffer, ISO7816.OFFSET_CDATA, len))
			ISOException.throwIt(SW_VERIFICATION_FAILED);
		
	}
	
	
	private void update (APDU apdu,byte []buffer)
	{
		
		if(!pin.isValidated() && !puk.isValidated())
			ISOException.throwIt(SW_PIN_UPDATE_NOT_PERMITTED);
		
		byte len = (byte)apdu.setIncomingAndReceive();
		
		
		pin.update(buffer, ISO7816.OFFSET_CDATA, len);
			
		pin.check(buffer, ISO7816.OFFSET_CDATA, len);
	}
	
	private void verpuk (APDU apdu,byte []buffer)
	{	
		byte len = (byte)apdu.setIncomingAndReceive();
		
		if(!puk.check(buffer, ISO7816.OFFSET_CDATA, len))
			ISOException.throwIt(SW_VERIFICATION_FAILED);
		
	}
}
		
		

	
		


