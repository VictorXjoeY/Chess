package chess.GUI;


import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;


/**
 * This class is an JTextField that only accept digits.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 * @author Otávio Luis de Aguiar - 9293518
 *
 */
public class IntegerJTextField extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1630122567128613747L;

	
	/**
	 * Empty constructor.
	 */
	public IntegerJTextField() {
        super();
    }
	
	/**
	 * Constructor with text.
	 * @param text
	 */
	public IntegerJTextField(String text) {
        super(text);
    }

    protected Document createDefaultModel() {
 	      return new IntegerDocument();
    }
    /**
     * Class to insert only integers in JTextField
     * @author Marcos Cesar Ribeiro de Camargo - 9278045
     * @author Otávio Luis de Aguiar - 9293518
     *
     */
    static class IntegerDocument extends PlainDocument {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4693760124252798554L;

		/**
		 * This method insert a string in JTexTField
		 */
		public void insertString(int offs, String str, AttributeSet a) 
 	          throws BadLocationException {
 	          if (str == null) {
 	        	  return;
 	          }
 	          for (int i = 0; i < str.length(); i++) {
 	        	  if(Character.isDigit(str.charAt(i)) == false){
 	        		  return;
 	        	  }
 	          }
 	          super.insertString(offs, new String(str), a);
 	      }
     }
 }
