package chess.pieces;
import chess.board.Chess;
import chess.board.ChessBoard;

/**
 * Classe que representa um Rei Preto.
 * @author Victor Forbes - 9293394
 */
public class BlackKing extends King{
	/**
	 * Construtor da peça.
	 * @param board Referência para o tabuleiro ao qual pertence a peça.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public BlackKing(ChessBoard board, int x, int y){
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
	 * Retorna a representação textual de um Rei Preto.
	 * @return Uma string contendo a representação.
	 */
	public String toString(){
		return "k";
	}
}