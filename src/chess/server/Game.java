package chess.server;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

import chess.board.Chess;
import chess.board.ChessBoard;


/**
 * Classe que realiza a partida entre dois jogadores.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 *
 */
public class Game extends Thread{

	private Socket pOne = null, pTwo = null;
	private PrintStream pOneOutput	 = null;
	private Scanner pOneInput		 = null;
	private PrintStream pTwoOutput 	 = null;
	private Scanner pTwoInput 		 = null;

	
	/**
	 * Função que verifica se o movimento inserido pelo usuário está na notação correta.
	 * @param move String com o movimento a ser verificado
	 * @return Verdadeiro ou falso.
	 */
	public static boolean ValidCell(String cell){
		return cell.matches("[a-h][1-8]");
	}

	/**
	 * Função que verifica se a promoção inserida pelo usuário está na notação correta. 
	 * @param promotion String com a peça promovida.
	 * @return Verdadeiro ou falso.
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
	 * Construtor para a classe que controla o jogo entre dois jogadores.
	 * @param playerOne Socket com a conexão do primeiro jogador.
	 * @param playerTwo Socket com a conexão do segundo jogador.
	 */
	public Game(Socket playerOne, Socket playerTwo){
		this.pOne = playerOne;
		this.pTwo = playerTwo;
		try {
			this.pOneOutput = new PrintStream(this.pOne.getOutputStream());
			this.pOneInput = new Scanner(this.pOne.getInputStream());
			this.pTwoOutput = new PrintStream(this.pTwo.getOutputStream());
			this.pTwoInput = new Scanner(this.pTwo.getInputStream());
		} catch (IOException e) {;}
		
	}
	
	
	/**
	 * Método run da Thread, realiza a comunicação entre o jogo acontecendo no servidor
	 * 	e os dois clientes.
	 */
	public void run(){
		ChessBoard board = null;
		String initialBoard = Chess.newGame;
		String move = null, promotion = null, source = null, destination = null;
		String p1 = null, p2 = null;
		int pOneTurn, pTwoTurn, turn;
    	
    	// debug
    	PrintStream debug = new PrintStream(System.out);

    	
    	// Enviando mensagem de boas vindas.
    	try {
			p1 = pOneInput.nextLine(); 
			pOneOutput.println("Bem Vindo " + p1);
			p2 = pTwoInput.nextLine(); 
			pTwoOutput.println("Bem Vindo " + p2);
		} catch (Exception e){
			debug.println("fechado inesperadamente.");
			return;
		}

    	// Vendo se é preciso carregar o jogo
    	source = pOneInput.nextLine();
    	if(!source.equals(Utils.NULL)){
    		initialBoard = source;
    	}
    	source = pTwoInput.nextLine();
    	if(!source.equals(Utils.NULL)){
    		initialBoard = source;    		
    	}
    	
		// Informando os clientes com quais peças eles vão jogar.
       	pOneOutput.println(Utils.WhitePiece); // Peças brancas.
       	pOneTurn = Chess.WHITE;
       	pTwoOutput.println(Utils.BlackPiece); // Peças pretas.
       	pTwoTurn = Chess.BLACK;

       	// Criando um novo tabuleiro.
    	board = new ChessBoard(initialBoard);

       	// Informando o tabuleiro inicial para os clientes.
       	pOneOutput.println(board.ToFEN());
       	pTwoOutput.println(board.ToFEN());

		
    	// Imprimindo o tabuleiro.
		debug.println(p1 + " vs " + p2);
	    debug.println(board.ToFEN());
	    debug.println(board);		

       	
    	// Enquanto o jogo não acabar.
    	while (!board.Checkmate() && !board.Tie()){
			turn = board.GetTurn();    		
    		// Enviando o turno aos clientes.
			pOneOutput.println((turn == Chess.WHITE ? Utils.WhitePiece : Utils.BlackPiece ));
			pTwoOutput.println((turn == Chess.WHITE ? Utils.WhitePiece : Utils.BlackPiece ));
    		// Recebendo o movimento do cliente.
    		do{
    			if(turn == pOneTurn){
    				// Lendo a casa de origem
    				do {
        				pOneOutput.println(Utils.MovementOrigin); // Informando ao cliente o tipo de resposta esperada.
    					try{
    						source = pOneInput.nextLine(); // Lendo resposta do cliente.
    					} catch (Exception e){
    						debug.println("fechado inesperadamente.");
    						return;
    					}
    					debug.println(source);

    				}while(!ValidCell(source) || !board.ValidSource(source));
    				// Enviando para o cliente todas as jogadas possiveis para a casa selecionada.
    				pOneOutput.println(Utils.Moves);
    				String arg = ConcatenateMoves(board.GetPossibleMoves(source));
       			    pOneOutput.println(arg);
    				// Lendo a casa de destino.
    				//do {
        				pOneOutput.println(Utils.MovementDestiny); // Informando ao cliente o tipo de resposta esperada.
    					try {
    						destination = pOneInput.nextLine(); // Lendo resposta do cliente.
    					} catch (Exception e){
    						debug.println("fechado inesperadamente.");
    						return;
    					}

    					move = source+destination;
    				//} while ((!ValidCell(destination) || !board.ValidMove(move)) && !source.equals(destination));

    			} else if(turn == pTwoTurn){
    				// Lendo a casa de origem.
    				do {
        				pTwoOutput.println(Utils.MovementOrigin); // Informando ao cliente o tipo de resposta esperada.
    					try {
    						source = pTwoInput.nextLine(); // Lendo resposta do cliente.
    					} catch (Exception e){
    						debug.println("fechado inesperadamente.");
    						return;
    					}
    				}while(!ValidCell(source) || !board.ValidSource(source));
    				// Enviando para o cliente todas as jogadas possiveis para a casa selecionada.
    				pTwoOutput.println(Utils.Moves);
    				String arg = ConcatenateMoves(board.GetPossibleMoves(source));
       			    pTwoOutput.println(arg);
    				// Lendo a casa de destino.
    				//do {
        				pTwoOutput.println(Utils.MovementDestiny); // Informando ao cliente o tipo de resposta esperada.
    					try {
    						destination = pTwoInput.nextLine(); // Lendo resposta do cliente.
    					} catch (Exception e){
    						System.out.println("fechado inesperadamente.");
    						return;
    					}

    					move = source+destination;

    				//}while ((!ValidCell(destination) || !board.ValidMove(move)) && !source.equals(destination));
    			}

    			debug.println("Movimento lido = " + move);
    		}while (!board.ValidMove(move));
    		// Enquanto não for um movimento válido.
    		debug.println("saiu do loop vou realizar o movimento");	
			// Executando o movimento.
			if (board.MakeMove(move) == Chess.PROMOTION){
				// Recebendo a promoção.
				do{
	    			if(turn == pOneTurn){
						pOneOutput.println(Utils.Promotion); // Informando ao cliente o tipo de resposta esperada.
						try {
							promotion = pOneInput.nextLine(); // Lendo a resposta do cliente.
						} catch (Exception e){
    						debug.println("fechado inesperadamente.");
    						return;
    					}

	    			} else if(turn == pTwoTurn){
						pTwoOutput.println(Utils.Promotion); // Informando ao cliente o tipo de resposta esperada.
						try {
							promotion = pTwoInput.nextLine(); // Lendo a resposta do cliente.
						} catch (Exception e){
    						debug.println("fechado inesperadamente.");
    						return;
    					}

					}
					
				}while (!ValidPromotion(promotion));
				// Enquanto não for uma promoção válida.
				// Promovendo o peão.
				board.Promote(move, promotion.charAt(0));
			}
			if(turn == pOneTurn){
				pOneOutput.println(Utils.Sucess); // Informando ao cliente que o comando foi executado com sucesso.
				debug.println("sucesso para o p1");
			} else if(turn == pTwoTurn){
				pTwoOutput.println(Utils.Sucess); // Informando ao cliente que o comando foi executado com sucesso.
				debug.println("sucesso para o p2");
			}

	       	// Informando o tabuleiro atual para os clientes.
	       	pOneOutput.println(board.ToFEN());
	       	pTwoOutput.println(board.ToFEN());
			
   						
			// Imprimindo o tabuleiro no servidor.
	       	debug.println(board);
    		
    	}
    	// Informando que o jogo acabou.
    	pOneOutput.println(Utils.EndGame);
       	pTwoOutput.println(Utils.EndGame); 		

    	switch (board.GetWinner()){
		case Chess.WHITE_TEAM_WINS:
        	pOneOutput.println("Você venceu!");
           	pTwoOutput.println("Você perdeu.");    			
			break;
		case Chess.BLACK_TEAM_WINS:
        	pOneOutput.println("Você perdeu.");
           	pTwoOutput.println("Você venceu!");    			    			
			break;
		case Chess.TRIPLE_REPETITION:
			pOneOutput.println("Empate por tripla repetição!");
			pTwoOutput.println("Empate por tripla repetição!");
			break;
		case Chess.INSUFFICIENT_MATERIAL:
			pOneOutput.println("Empate por falta de material!");
			pTwoOutput.println("Empate por falta de material!");
			break;
		case Chess.FIFTY_MOVEMENTS:
			pOneOutput.println("Empate pela regra dos 50 movimentos!");
			pTwoOutput.println("Empate pela regra dos 50 movimentos!");
			break;
		case Chess.STALEMATE:
			pOneOutput.println("Empate por afogamento!");
			pTwoOutput.println("Empate por afogamento!");
			break;
		default:
			break;
    	}

	}
	
}



		

