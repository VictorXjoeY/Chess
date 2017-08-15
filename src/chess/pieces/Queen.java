package chess.pieces;
import chess.board.Chess;
import chess.board.ChessBoard;

/**
 * Classe que representa uma Rainha.
 * @author Victor Forbes - 9293394
 */
public abstract class Queen extends ChessPiece{
	/**
	 * Construtor da peça.
	 * @param board Referência para o tabuleiro ao qual pertence a peça.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public Queen(ChessBoard board, int x, int y){
		super(board, x, y);
	}

	/**
	 * Getter que retorna a cor da peça.
	 * @return A cor da peça.
	 */
	public abstract int GetColor();

	/**
	 * Getter que retorna o valor da peça.
	 * @return O valor.
	 */
	public int GetValue(){
		return Chess.QUEEN_VALUE;
	}

	/**
	 * Gera os movimentos possíveis da peça.
	 */
	public void GenerateMoves(){
		// Apagando os movimentos antigos.
		this.movements.clear();

		// Diagonal esquerda acima.
		this.GenerateLineMoves(Chess.UP, Chess.LEFT);
		
		// Diagonal esquerda abaixo.
		this.GenerateLineMoves(Chess.DOWN, Chess.LEFT);
		
		// Diagonal direita acima.
		this.GenerateLineMoves(Chess.UP, Chess.RIGHT);
		
		// Diagonal direita abaixo.
		this.GenerateLineMoves(Chess.DOWN, Chess.RIGHT);

		// Cima.
		this.GenerateLineMoves(Chess.UP, Chess.NO_DIRECTION);
		
		// Baixo.
		this.GenerateLineMoves(Chess.DOWN, Chess.NO_DIRECTION);
		
		// Esquerda.
		this.GenerateLineMoves(Chess.NO_DIRECTION, Chess.LEFT);
		
		// Direita.
		this.GenerateLineMoves(Chess.NO_DIRECTION, Chess.RIGHT);
	}
}