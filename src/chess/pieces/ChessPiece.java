package chess.pieces;
import java.util.TreeSet;

import chess.board.Chess;
import chess.board.ChessBoard;

/**
 * Classe que representa uma peça de Xadrez genérica.
 * @author Victor Forbes - 9293394
 */
public abstract class ChessPiece{
	protected TreeSet<String> movements; // Movimentos possíveis.
	protected ChessBoard board; // Referência para o tabuleiro ao qual pertence essa peça.
	protected int x, y; // Coordenadas da peça no tabuleiro.

	/**
	 * Construtor da peça.
	 * @param board Tabuleiro ao qual essa peça pertence.
	 * @param x Coordenada inicial x da peça.
	 * @param y Coordenada inicial y da peça.
	 */
	public ChessPiece(ChessBoard board, int x, int y){
		this.movements = new TreeSet<String>();
		this.board = board;
		this.x = x;
		this.y = y;
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
	public abstract int GetValue();

	/**
	 * Gera os movimentos possíveis da peça.
	 */
	public abstract void GenerateMoves();

	/**
	 * Função auxiliar para tentar mover essa peça em uma das 8 direções.
	 * @param xStep Passo vertical.
	 * @param yStep Passo horizontal.
	 */
	protected void GenerateLineMoves(int xStep, int yStep){
		int xf = this.x + xStep;
		int yf = this.y + yStep;

		// Movendo na direção enquanto tiver casas vazias.
		while (this.board.GetColor(xf, yf) == Chess.NONE){
			if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(xf, yf))){
				this.movements.add(String.format("%s", Chess.ToFEN(xf, yf)));
			}

			// Andando.
			xf += xStep;
			yf += yStep;
		}

		// Se a casa não vazia em que parou for o inimigo.
		if (Chess.DifferentTeams(this.board.GetColor(xf, yf), this.GetColor())){
			if (!this.board.LeavesKingUnderAttack(Chess.ToFEN(this.x, this.y) + Chess.ToFEN(xf, yf))){
				this.movements.add(String.format("%s", Chess.ToFEN(xf, yf)));
			}
		}
	}

	/**
	 * Verifica se essa peça possui algum movimento.
	 * @return True se possuir ou false se não possuir.
	 */
	public boolean HasMovements(){
		return !this.movements.isEmpty();
	}

	/**
	 * Função que retorna os movimentos possíveis da peça em um vetor. 
	 * @return O vetor dos movimentos possíveis.
	 */
	public String[] GetMoves(){
		return this.movements.toArray(new String[0]);
	}
	
	/**
	 * Getter para retornar a coordenada x da peça.
	 * @return A coordenada x.
	 */
	public int GetX(){
		return this.x;
	}

	/**
	 * Getter para retornar a coordenada y da peça.
	 * @return A coordenada y.
	 */
	public int GetY(){
		return this.y;
	}

	/**
	 * Setter para setar as coordenadas da peça.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public void SetPosition(int x, int y){
		this.x = x;
		this.y = y;
	}

	/**
	 * Verifica se essa peça pode se mover para a casa passada por parâmetro.
	 * @return True caso possa ou false caso não possa.
	 * @param destination Casa de destino.
	 */
	public boolean ValidMove(String destination){
		return this.movements.contains(destination);
	}

	/**
	 * Imprime os movimentos dessa peça.
	 */
	public void PrintMoves(){
		for (String it : this.movements){
			System.out.println(Chess.ToFEN(this.x, this.y) + it);
		}
	}
}
