package adminTool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.swing.JOptionPane;


/*
 * classe utilisée pour demander un certificat */
public class CertificateManagement {
	
    byte[] certificate; //tableau contient le ficher certificat 
	
public byte [] getCert() 
{
	return certificate;
}
public CertificateManagement(String matricule)
{
	String[] demande = { "cmd.exe", "/C", "openssl req -newkey rsa:2048 -keyout C:\\Resto_tool\\Resto_Admin_tool\\privateKey_store\\myPrivateKey.pem -nodes -out C:\\Resto_tool\\Resto_Admin_tool\\request_store\\myReq.req -subj /CN="+matricule+"/O=USTHB/C=DZ/L=Alger" };
	String[] envoiDemande = { "cmd.exe", "/C", "certreq -submit -binary -attrib \"CertificateTemplate:SmartCardUserPFE\" -config WIN-NRUPPPRU6AV\\USTHB-WIN-NRUPPPRU6AV-CA C:\\Resto_tool\\Resto_Admin_tool\\request_store\\myReq.req C:\\Resto_tool\\Resto_Admin_tool\\certificat_store\\certificat.cer" };
	String[] privKey= {"cmd.exe","/C","openssl rsa -in C:\\Resto_tool\\Resto_Admin_tool\\privateKey_store\\myPrivateKey.pem -text -out C:\\Resto_tool\\Resto_Admin_tool\\privateKey_store\\myPrivateKey.txt"};
	
	try {
		ecran.cmd(demande,"");
		File req = new File("C:\\Resto_tool\\Resto_Admin_tool\\request_store\\myReq.req");
		while(!req.exists())
		{
			
		}
		Thread.sleep(2000);
		ecran.cmd(envoiDemande,"");
		Thread.sleep(1000);
		ecran.cmd(privKey,"");
		
		File certif=new File("C:\\Resto_tool\\Resto_Admin_tool\\certificat_store\\certificat.cer");
		
		while(!certif.exists())
		{
			
		}
		Thread.sleep(4000);
	} catch (InterruptedException e1) {
		e1.printStackTrace();
	}
	
	//ecran.redirect();
	
	
	
	
	FileInputStream fis1;
	
	try {
		
		
		File cert=new File("C:\\Resto_tool\\Resto_Admin_tool\\certificat_store\\certificat.cer");
		
		fis1 = new FileInputStream(cert);
		@SuppressWarnings("unused")
		int n1=0;
	    byte[] buf1 = new byte[8];
	    
	    int h=0;
	    while((n1 = fis1.read(buf1)) >= 0)
	    {
	    	for(@SuppressWarnings("unused") byte bit1 : buf1)
	    	{
	    		h++;
	    	}
	    }
	    
	    fis1.close();
	    
	    certificate=new byte[(int)h];
	    
	    fis1 = new FileInputStream(cert);
		n1=0;
		int n2=0;
		while((n1 = fis1.read(buf1)) >= 0)
	    {
	    	for(byte bit1 : buf1)
	    	{
	    		certificate[n2]=bit1; //ecriture de ficher certificat dans un tableau (tableau envoyé a la carte)
	    		n2++;
	    	}
	    }
		fis1.close();
	   
	} catch ( IOException e)
	{
		e.printStackTrace();
	}
	
	
    
	
}
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
		  JOptionPane.showMessageDialog(null, "Certificat non vérifié la méthode verifyCerticat la classe CertificateManagement  " , "Problème !", JOptionPane.ERROR_MESSAGE);
		  
		  return false;
		 }
	  } //fin de la méthode verifyCerticat()
	
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
			  JOptionPane.showMessageDialog(null, "clé public non récupéré la méthode getPublicKey la classe CertificateManagement "+ e.getMessage() , "Problème !", JOptionPane.ERROR_MESSAGE);
			  return null ;
		 }
		 
		
	}
}
