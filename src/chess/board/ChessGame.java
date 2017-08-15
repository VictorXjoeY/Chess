package chess.board;
import java.util.Scanner;
import java.io.PrintStream;

/**
 * Classe que executa o Loop principal do jogo de Xadrez.
 * @author Victor Forbes - 9293394
 */
public class ChessGame{
	public static ChessBoard board; // Tabuleiro de Xadrez.
	public static Scanner in; // Entrada.
	public static PrintStream out; // Saída.

	/**
	 * Função que verifica se a casa inserida pelo usuário está na notação correta.
	 * @return True se estiver ou false se não estiver.
	 */
	public static boolean ValidCell(String cell){
		return cell.matches("[a-h][1-8]");
	}

	/**
	 * Função que verifica se a promoção inserida pelo usuário está na notação correta.
	 * @return True se estiver ou false se não estiver.
	 */
	public static boolean ValidPromotion(String promotion){
		return promotion.matches("[NBRQ]");
	}

	/**
	 * Função que converte e concatena os movimentos possíveis.
	 * @return O resultado da concatenação.
	 * @param moves O vetor de casas de destino possíveis.
	 */
	public static String ConcatenateMoves(String[] moves){
		String res = "";

		for (int i = 0; i < moves.length; i++){
			// Concatenando.
			res += moves[i];

			if (i < moves.length - 1){
				res += " ";
			}
		}

		return res;
	}

	/**
	 * Função que converte  a String de movimentos possíveis.
	 * @return Um vetor com todos os inteiros da String.
	 * @param strmoves O vetor de Strings.
	 */
	public static int[] ConvertMoves(String[] strmoves){
		int[] intmoves = new int[strmoves.length];
		int[] coordinates;

		for (int i = 0; i < strmoves.length; i++){
			// Transformando em coordenadas matriciais.
			coordinates = Chess.FromFEN(strmoves[i]);

			// Obtendo a coordenada pseudo-matricial.
			intmoves[i] = coordinates[0] * Chess.NUMBER_OF_FILES + coordinates[1];;
		}

		return intmoves;
	}

	/**
	 * Função que desconcatena as casas de destino possíveis.
	 * @return Vetor de Strings com as casas de destino possíveis.
	 * @param concat A String concatenada.
	 */
	public static String[] UnconcatenateMoves(String concat){
		return concat.split(" ");
	}

	/**
	 * Função que converte coordenadas pseudo-matriciais para coordenadas em FEN.
	 * @return Coordenadas em FEN.
	 * @param pseudo String contendo um inteiro de 0 a 63.
	 */
	public static String FromPseudoToFEN(String pseudo){
		int pos = Integer.parseInt(pseudo);
		int x = pos / Chess.NUMBER_OF_FILES;
		int y = pos % Chess.NUMBER_OF_FILES;

		return Chess.ToFEN(x, y);
	}

	/**
	 * Função principal da execução do Xadrez.
	 * @param args Argumentos.
	 * @throws Exception.
	 */
    public static void main(String[] args) throws Exception{
    	String source, destination, move, promotion;

    	// Criando um novo tabuleiro e a Entrada e a Saída.
    	out = new PrintStream(System.out);
    	in = new Scanner(System.in);

    	board = new ChessBoard(Chess.newGame);
    	
    	// Imprimindo o tabuleiro.
	    // out.println(board.ToFEN());
    	out.println(board);

    	// Enquanto o jogo não acabar.
    	while (!board.Checkmate() && !board.Tie()){
    		// Loop da entrada do movimento.
    		do{
	    		// Recebendo a casa de origem.
	    		do{
	    			out.print("(Time " + (board.GetTurn() == Chess.WHITE ? "Branco" : "Preto") + ") - Mover a peça da casa: ");
	    			source = in.next();
	    		}while (!ValidCell(source) || !board.ValidSource(source));
	    		// Enquanto não for uma casa de origem válida.

	    		// Recebendo a casa de destino.
	    		do{
	    			out.print("(Time " + (board.GetTurn() == Chess.WHITE ? "Branco" : "Preto") + ") - Para a casa: ");
	    			destination = in.next();
	    			move = source + destination;
	    		}while ((!ValidCell(destination) || !board.ValidMove(move)) && !source.equals(destination));
	    		// Enquanto não for uma casa de destino válida (movimento válido).
    		}while (source.equals(destination));
			
			// Executando o movimento.
			if (board.MakeMove(move) == Chess.PROMOTION){
				// Recebendo a promoção.
				do{
					out.print("Promover peão para: ");
					promotion = in.next();
				}while (!ValidPromotion(promotion));
				// Enquanto não for uma promoção válida.

				// Promovendo o peão.
				board.Promote(move, promotion.charAt(0));
			}

			// Imprimindo o tabuleiro.
    		out.println(board);
    	}

    	// Checando qual foi o outcome da partida.
    	switch (board.GetWinner()){
    		case Chess.WHITE_TEAM_WINS:
    			out.println("Time branco venceu!");
    			break;
    		case Chess.BLACK_TEAM_WINS:
    			out.println("Time preto venceu!");
    			break;
    		case Chess.TRIPLE_REPETITION:
    			out.println("Empate por tripla repetição!");
    			break;
    		case Chess.INSUFFICIENT_MATERIAL:
    			out.println("Empate por falta de material!");
    			break;
    		case Chess.FIFTY_MOVEMENTS:
    			out.println("Empate pela regra dos 50 movimentos!");
    			break;
    		case Chess.STALEMATE:
    			out.println("Empate por afogamento!");
    			break;
    		default:
    			break;
    	}
    }
}