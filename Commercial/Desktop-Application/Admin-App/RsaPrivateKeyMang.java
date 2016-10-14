package adminTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

public class RsaPrivateKeyMang {
	
	/*
	 * permet d'extraire le clé privé a partire d'un fichier*/
	
	// le modulo en string format
	private String stringModulo;
	
	// l'exposant privé en string format
	private String stringPrivateExponent;
	
	// le modulo en byte format
  private byte []  byteArrayModulo;
  
	// l'exposant privé en byte format
	private byte [] byteArrayPrivateExponent;
	
	// le modulo en byte format final
	byte[] arrayModulo=new byte[256];
	
	// l'exposant privé en byte format final
	byte[] arrayPrivateExponent=new byte[256];
	
	
	
	public RsaPrivateKeyMang(String myPrivateKeyFileName) // initialisation de stringModulo et stringPrivateExponent
	{
		stringModulo = "" ;
		stringPrivateExponent= "" ;
		
		try
		{
			File keyFile=new File(myPrivateKeyFileName);
			InputStream ips=new FileInputStream(keyFile);
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			HexBinaryAdapter adapter = new HexBinaryAdapter();
		
			int ligneNum = 0 ;
		
			while ((ligne=br.readLine())!=null && ligneNum <= 40)
			{
				ligneNum ++ ;
				if(ligneNum >= 3 && ligneNum <= 20) //initialisation de stringModulo
				{
					for(int i = 0 ; i<ligne.length();i++)
					{
						if(ligne.charAt(i) != ':' && ligne.charAt(i) != ' ')
						   stringModulo += ligne.charAt(i);
					}
				}
				
				
				if(ligneNum >= 23 && ligneNum <= 40) //initialisation de stringPrivateExponent
				{
					for(int i = 0 ; i<ligne.length();i++)
					{
						if(ligne.charAt(i) != ':' && ligne.charAt(i) != ' ')
							stringPrivateExponent += ligne.charAt(i);
					}
				}
				
				
			}
			
			byteArrayModulo = adapter.unmarshal(stringModulo);
			byteArrayPrivateExponent = adapter.unmarshal(stringPrivateExponent);
			
			int decalageM=0;
			if(byteArrayModulo[0]==0)
				decalageM=1;
			
			int decalageE=0;
			if(byteArrayPrivateExponent[0]==0)
				decalageE=1;
			
			for(int h=0;h<256;h++)
			{
				arrayModulo[h]=byteArrayModulo[h+decalageM];
				arrayPrivateExponent[h]=byteArrayPrivateExponent[h+decalageE];
			}


			br.close();
			ips.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	} //fin de constructeur
	
	
	protected String getStringModulo()
	{
		return stringModulo;
	}
	
	protected String getStringPrivateExponent()
	{
		return stringPrivateExponent;
	}
	
	protected byte[] getByteArrayModulo()
	{
		return arrayModulo;
	}
	
	protected byte[] getByteArrayPrivateExponent()
	{
		return arrayPrivateExponent;
	}
	
	
	
	

}
