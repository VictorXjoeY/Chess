package chess.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Está classe possui defines que serão usados pelo servidor.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 *
 */
public abstract class Utils {
	public static final String EndGame 			= "end_game";
	public static final String WhitePiece 		= "white";
	public static final String BlackPiece 		= "black";
	public static final String Sucess			= "sucess";
	public static final String Promotion		= "promotion";
	public static final String Moves			= "piece_moves";
	public static final String MovementOrigin 	= "player_movement_origin";
	public static final String MovementDestiny 	= "player_movement_destiny";
	public static final String IAvsIA 			= "IAvsIA";	
	public static final String LoadvsIA 		= "LoadvsIA";	
	public static final String IAvsClient		= "IavsClient";	
	public static final String ClientvsClient	= "ClientvcsClient";
	public static final String NULL				= "null";
	public static final String IA1NAME			= "Skynet(IA)";
	public static final String IA2NAME			= "Cartana(IA)";

	private static InputStreamReader isr = new InputStreamReader(System.in);
	private static BufferedReader br = new BufferedReader(isr);
	
		public static String getString(){
			String input = null;
			try {
				input = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return input;
		}
}
