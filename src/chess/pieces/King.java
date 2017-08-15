package chess.pieces;
import chess.board.Chess;
import chess.board.ChessBoard;

/**
 * Classe que representa um Rei.
 * @author Victor Forbes - 9293394
 */
public abstract class King extends ChessPiece{
	/**
	 * Construtor da peça.
	 * @param board Referência para o tabuleiro ao qual pertence a peça.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public King(ChessBoard board, int x, int y){
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
		return Chess.KING_VALUE;
	}

	/**
	 * Função que verifica se é possível realizar o Roque.
	 * @return True se for possível realizar o Roque ou false caso contrário.
	 * @param side Lado (da Rainha ou do Rei).
	 */
	private boolean Castling(int side){
		int step = side == Chess.KING ? Chess.RIGHT : Chess.LEFT;
		int corner = side == Chess.KING ? 7 : 0;
		int i = this.y + step;

		// Caso o Rei esteja sob ataque.
		if (this.board.CellUnderAttack(this.x, this.y, (this.GetColor() == Chess.WHITE ? Chess.BLACK : Chess.WHITE))){
			return false;
		}

		// Verificando se há alguma peça entre o Rei e a Torre e se o caminho está sob ataque.
		while (this.board.GetColor(this.x, i) == Chess.NONE){
			i += step;
		}

		// Se não houver nenhuma peça entre o Rei e a Torre e as casas por onde o rei
		// passaria não estiverem sob ataque, retorne true.
		return i == corner && !this.board.CellUnderAttack(this.x, this.y + step, (this.GetColor() == Chess.WHITE ? Chess.BLACK : Chess.WHITE)) && !this.board.CellUnderAttack(this.x, this.y + 2 * step, (this.GetColor() == Chess.WHITE ? Chess.BLACK : Chess.WHITE));
	}

	/**
	 * Gera os movimentos possíveis da peça.
	 */
	public void GenerateMoves(){
		String king = this.toString();
		String queen = ((Character)((char)(this.toString().charAt(0) + ('Q' - 'K')))).toString();

		// Apagando os movimentos antigos.
		this.movements.clear();

		// Tentando o Roque pelo lado do rei.
		if (this.board.GetCastling().contains(king) && this.Castling(Chess.KING)){
			this.movements.add(String.format("%s", Chess.ToFEN(this.x, this.y + 2)));
		}

		// Tentando o Roque pelo lado da rainha.
		if (this.board.GetCastling().contains(queen) && this.Castling(Chess.QUEEN)){
			this.movements.add(String.format("%s", Chess.ToFEN(this.x, this.y - 2)));
		}

		// Movimentos possíveis do rei.
		for (int i = this.x - 1; i <= this.x + 1; i++){
			for (int j = this.y - 1; j <= this.y + 1; j++){
				if (this.board.GetColor(i, j) == Chess.NONE || Chess.DifferentTeams(this.board.GetColor(i, j), this.GetColor())){
					if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(i, j))){
						this.movements.add(String.format("%s", Chess.ToFEN(i, j)));
					}
				}
			}
		}
	}
}