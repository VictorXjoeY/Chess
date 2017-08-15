package chess.pieces;
import chess.board.Chess;
import chess.board.ChessBoard;

/**
 * Classe que representa um Cavalo.
 * @author Victor Forbes - 9293394
 */
public abstract class Knight extends ChessPiece{
	/**
	 * Construtor da peça.
	 * @param board Referência para o tabuleiro ao qual pertence a peça.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public Knight(ChessBoard board, int x, int y){
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
		return Chess.KNIGHT_VALUE;
	}

	/**
	 * Gera os movimentos possíveis da peça.
	 */
	public void GenerateMoves(){
		// As 8 posições possíveis para um cavalo.
		int[] xf = new int[]{this.x + 1, this.x - 1, this.x + 2, this.x - 2, this.x + 2, this.x - 2, this.x + 1, this.x - 1};
		int[] yf = new int[]{this.y - 2, this.y - 2, this.y - 1, this.y - 1, this.y + 1, this.y + 1, this.y + 2, this.y + 2};

		// Apagando os movimentos antigos.
		this.movements.clear();

		// Iterando sobre as posições finais possíveis.
		for (int i = 0; i < xf.length; i++){
			if (this.board.GetColor(xf[i], yf[i]) == Chess.NONE || Chess.DifferentTeams(this.board.GetColor(xf[i], yf[i]), this.GetColor())){
				// Verificando se o movimento não deixa o Rei vulnerável.
				if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(xf[i], yf[i]))){
					this.movements.add(String.format("%s", Chess.ToFEN(xf[i], yf[i])));
				}
			}
		}
	}
}