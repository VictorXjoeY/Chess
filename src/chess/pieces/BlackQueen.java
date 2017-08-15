package chess.pieces;
import chess.board.Chess;
import chess.board.ChessBoard;

/**
 * Classe que representa uma Rainha Preta.
 * @author Victor Forbes - 9293394
 */
public class BlackQueen extends Queen{
	/**
	 * Construtor da peça.
	 * @param board Referência para o tabuleiro ao qual pertence a peça.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public BlackQueen(ChessBoard board, int x, int y){
		super(board, x, y);
	}

	/**
	 * Getter que retorna a cor da peça.
	 * @return A cor da peça.
	 */
	public int GetColor(){
		return Chess.BLACK;
	}

	/**
	 * Retorna a representação textual de uma Rainha Preta.
	 * @return Uma string contendo a representação.
	 */
	public String toString(){
		return "q";
	}
}