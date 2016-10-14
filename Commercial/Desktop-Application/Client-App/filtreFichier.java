package caisse;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Cette classe permet d'ajouter un filtre aux JFileChooser afin de ne montrer qu'un certain type de fichiers dans le gestionnaire de fichiers.
 *
 * 
 */
public class filtreFichier extends FileFilter{
        /**
         * Description du type de fichier
         */
        private String description;

        /**
         * Extension du type de fichier
         */
        private String extension;
   
        /**
         * Constructeur du filtre de fichiers
         *      String contenant la description du type de fichier
         *      String contenant l'extension du type de fichier
         */
        public filtreFichier(String description, String extension)
        {
        if(description == null || extension == null)
        {
            throw new NullPointerException("La description ou l'extension ne peuvent etre nulles.");
        }
        this.description = description;
        this.extension = extension;
        }
   
        public boolean accept(File file){
        if(file.isDirectory())
        {
             return true;
        }
        String nomFichier = file.getName().toLowerCase();
        return nomFichier.endsWith(extension);
        }

        /**
         * Renvoie la description du type de fichier
         * 
         *      Un String contenant la description du fichier suivie de l'extension du fichier entre parenthèses.
         */
        public String getDescription()
        {
            return (description + " (fichier " + extension + ")");
        }
}
