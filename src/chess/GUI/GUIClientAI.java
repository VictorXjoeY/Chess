package chess.GUI;

import chess.board.ChessBoard;

/**
 * Classe que avalia as melhores jogadas a serem realizadas pela IA.
 * @author Victor Forbes - 9293394
 */
public class GUIClientAI extends GUIClient{
	private ChessBoard cb;
	private String play;

	 /** Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 */
	public GUIClientAI(String ip, int port, String ID, boolean isVisible, boolean willShowMoves){
		super(ip, port, ID, null, isVisible, willShowMoves);
	}

	 /** Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 * @param fen Nome do arquivo para carregar um jogo.
	 */
	public GUIClientAI(String ip, int port, String ID, String FEN, boolean isVisible, boolean willShowMoves){
		super(ip, port, ID, FEN, isVisible, willShowMoves);
	}

	
	/**
	 * Função que recebe a casa a origem do movimento.
	 * @return Movimento lido.
	 */
@Override
	protected String getOrigin(){
		this.cb = new ChessBoard(this.board);
		this.play = this.cb.GetBestMovement();
    	return this.play.substring(0, 2);
    }
    
	/**
	 * Função que recebe o destiny da peça.
	 * @return Movimento lido.
	 */
@Override
    protected String getDestiny(){
		return this.play.substring(2, 4);
    }

	/**
	 * Função que recebe a promoção a ser realizada.
	 * @return Movimento lido.
	 */
@Override
    protected String getPromotion(){
		return this.play.substring(4, 5);
    }

}