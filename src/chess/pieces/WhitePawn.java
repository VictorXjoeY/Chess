package chess.pieces;
import chess.board.Chess;
import chess.board.ChessBoard;

/**
 * Classe que representa um Peão Branco.
 * @author Victor Forbes - 9293394
 */
public class WhitePawn extends Pawn{
	/**
	 * Construtor da peça.
	 * @param board Referência para o tabuleiro ao qual pertence a peça.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public WhitePawn(ChessBoard board, int x, int y){
		super(board, x, y);
	}

	/**
	 * Getter que retorna a cor da peça.
	 * @return A cor da peça.
	 */
	public int GetColor(){
		return Chess.WHITE;
	}

	/**
	 * Gera os movimentos possíveis da peça.
	 */
	public void GenerateMoves(){
		// Apagando os movimentos antigos.
		this.movements.clear();		

		// 1 para frente.
		if (this.board.GetColor(this.x - 1, this.y) == Chess.NONE){
			if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(this.x - 1, this.y))){
				this.movements.add(String.format("%s", Chess.ToFEN(this.x - 1, this.y)));
			}

			// 2 para frente.
			if (this.x == 6 && this.board.GetColor(this.x - 2, this.y) == Chess.NONE){
				if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(this.x - 2, this.y))){
					this.movements.add(String.format("%s", Chess.ToFEN(this.x - 2, this.y)));
				}
			}
		}

		// Ataque na diagonal esquerda.
		if (this.board.GetColor(this.x - 1, this.y - 1) == Chess.BLACK || (this.board.GetEnPassant().equals(Chess.ToFEN(this.x - 1, this.y - 1)) && this.board.GetTurn() == Chess.WHITE)){
			if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(this.x - 1, this.y - 1))){
				this.movements.add(String.format("%s", Chess.ToFEN(this.x - 1, this.y - 1)));
			}
		}

		// Ataque na diagonal direita.
		if (this.board.GetColor(this.x - 1, this.y + 1) == Chess.BLACK || (this.board.GetEnPassant().equals(Chess.ToFEN(this.x - 1, this.y + 1)) && this.board.GetTurn() == Chess.WHITE)){
			if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(this.x - 1, this.y + 1))){
				this.movements.add(String.format("%s", Chess.ToFEN(this.x - 1, this.y + 1)));
			}
		}
	}

	/* Representação de uma Peão Preto. */
	public String toString(){
		return "P";
	}
}