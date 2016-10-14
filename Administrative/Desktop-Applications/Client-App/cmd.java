package e_docs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class cmd {
	
	public cmd(String [] commande)//méthode qui exécute des commande en shell
 	{
 			try {
 				Runtime runtime = Runtime.getRuntime();
				@SuppressWarnings("unused")
				final Process process = runtime.exec(commande);
			} catch (IOException e) {
				e.printStackTrace();
			}
		   
 		
 	}
	public cmd(String [] commande,String message)//méthode qui exécute des commande en shell
 	{
 		
 		try {
 			Runtime runtime = Runtime.getRuntime();
 			final Process process = runtime.exec(commande);

 			// Consommation de la sortie standard de l'application externe dans un Thread séparé
 			new Thread() {
 				public void run() {
 					try {
 						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
 						String line = "";
 						@SuppressWarnings("unused")
						String res = "";
 						try {
 							while((line = reader.readLine()) != null) {
 								// Traitement du flux de sortie de l'application si besoin est
 								res += line + '\n';
 							}
 							
 						} finally {
 							reader.close();
 						}
 					} catch(IOException ioe) {
 						ioe.printStackTrace();
 					}
 				}
 			}.start();	 							

 			
 			// Consommation de la sortie d'erreur de l'application externe dans un Thread separe
 			new Thread() {
 				public void run() {
 					try {
 						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
 						@SuppressWarnings("unused")
						String line = "";
 						try {
 							while((line = reader.readLine()) != null) {
 								// Traitement du flux d'erreur de l'application si besoin est

 							}
 						} finally {
 							reader.close();
 						}
 					} catch(IOException ioe) {
 						ioe.printStackTrace();
 					}
 				}
 			}.start();
 			
 		} 
 		catch (IOException e) {
 			e.printStackTrace();
 		}
 	} 

}
