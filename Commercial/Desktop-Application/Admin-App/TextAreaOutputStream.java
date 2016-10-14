package adminTool;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
* classe qui permet d'ecrire un flux de sortie dans JTextArea
*
* 
*/
public class TextAreaOutputStream extends OutputStream {
    private JTextArea textControl;
  
    public TextAreaOutputStream( JTextArea control ) {
        textControl = control;
       
        
    }
    
    public void write( int b ) throws IOException {
        textControl.append( String.valueOf( ( char )b ) );
    }   
}