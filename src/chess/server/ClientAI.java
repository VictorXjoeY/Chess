package chess.server;


import chess.board.ChessBoard;

/**
 * Classe que avalia as melhores jogadas a serem realizadas pela IA.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 * @author Victor Forbes - 9293394
 */
public class ClientAI extends Client{
	private ChessBoard cb;
	private String play;

	 /** Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 */
	public ClientAI(String ip, int port, String ID){
		super(ip, port, ID, null);
	}

	 /** Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 * @param fen Nome do arquivo para carregar um jogo.
	 */
	public ClientAI(String ip, int port, String ID, String FEN){
		super(ip, port, ID, FEN);
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