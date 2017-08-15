package chess.GUI;


import java.net.Socket;
import java.util.Scanner;

import chess.board.Chess;
import chess.server.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;

/**
 * Classe que representa um cliente.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 *
 */
public class GUIClient extends Thread {
	
	protected String ip;
	protected int port;
	protected boolean inGame;
	protected String ID;
	protected String board;
	protected Socket client = null;
	protected PrintStream output = null;
	protected Scanner input = null;
	protected BufferedWriter file_out; // Arquivo de saves.
	private String initialFEN;

	private Table myTable;
	private boolean willShowMoves;

	/**
	 * Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 */
	public GUIClient(String ip, int port, String ID, boolean isVisible, boolean willShowMoves){
		this.ip = ip;
		this.port = port;
		this.inGame = false;
		this.ID = ID;
		this.initialFEN = null;

		this.willShowMoves = willShowMoves;
		this.myTable = new Table(isVisible);
	}

	/**
	 * Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 */
	protected GUIClient(String ip, int port, String ID, String FEN, boolean isVisible, boolean willShowMoves){
		this.ip = ip;
		this.port = port;
		this.inGame = false;
		this.ID = ID;
		this.initialFEN = FEN;

		this.willShowMoves = willShowMoves;
		this.myTable = new Table(isVisible);

	}

	/**
	 * Método que conecta o cliente ao servidor.
	 * @return Booleano contendo true(sucesso) ou false(falha).
	 */
	public boolean connect(){
		try{
			this.client = new Socket(this.ip, this.port);
			this.output = new PrintStream(this.client.getOutputStream());
			this.input = new Scanner(this.client.getInputStream());

		}catch (Exception e){
			return false;
		}
		this.start();
		return true;
	}

	/**
	 * Método que verifica se o cliente está em jogo ou está em espera.
	 * @return Bollean contendo true(em jogo) ou false(em espera).
	 */
	public boolean inGame(){
		return this.inGame;
	}
	
    /**
     * Função que retorna uma pseudo-matriz a partir do tabuleiro em notação FEN.
     * @return A pseudo-matriz.
     * @param fen O tabuleiro em FEN.
     */
    public static String ToPseudoMatrix(String fen){
        String res = "";

        fen = (fen.split(" "))[0].replaceAll("/", "");

        for (int i = 0; i < fen.length(); i++){
            if (Character.isDigit(fen.charAt(i))){
                for (int j = Character.getNumericValue(fen.charAt(i)); j > 0; j--){
                    res += ".";
                }
            }
            else{
                res += ((Character)fen.charAt(i)).toString();
            }
        }

        return res;
    }
    
    /**
     * Função que converte coordenadas pseudo-matriciais para coordenadas em FEN.
     * @return Coordenadas em FEN.
     * @param pseudo String contendo um inteiro de 0 a 63.
     */
    public static String FromPseudoToFEN(String pseudo){
        int pos = Integer.parseInt(pseudo);
        int x = pos / Chess.NUMBER_OF_FILES;
        int y = pos % Chess.NUMBER_OF_FILES;

        return Chess.ToFEN(x, y);
    }

    /**
     * Função que converte uma coordenada matricial em coordenada em FEN.
     * @return A representação FEN dessas coordendadas.
     * @param x A coordenada x.
     * @param y A coordenada y.
     */
    public static String ToFEN(int x, int y){
        return ((Character)((char)(y + 'a'))).toString() + ((Integer)(Chess.NUMBER_OF_RANKS - x)).toString();
    }
    
	/**
	 * Função que recebe a casa a origem do movimento.
	 * @return Movimento lido.
	 */
    protected String getOrigin(){
		String movement = FromPseudoToFEN(myTable.getMovement());
    	return movement;
    }
    
	/**
	 * Função que recebe o destiny da peça.
	 * @return Movimento lido.
	 */
    protected String getDestiny(){
		String movement = FromPseudoToFEN(myTable.getMovement());
		myTable.clearMoves();
    	return movement;
    }

	/**
	 * Função que recebe a promoção a ser realizada.
	 * @return Movimento lido.
	 */
    protected String getPromotion(){
		String movement = myTable.getPromotion();
    	return movement;
    }

	/**
	 * Método run da Thread que executa o cliente, realizando toda troca de informações do cliente.
	 */
@Override
	public void run(){
		String play 	 = null;
		String moves 	 = null;
		String operation = null;
		String turn      = null;
		String color 	 = null;

		output.println(this.ID); // Transmitindo a identificação do usuário.
		play = this.input.nextLine(); // Recebendo uma confirmação de conexão.
		this.inGame = true; // Setando variavel de começo de jogo como true

		//Verificando se é preciso carregar um jogo
		if(this.initialFEN == null){
			output.println(Utils.NULL);
		} else {
			output.println(this.initialFEN);
		}
		// Recebendo a cor das peças do jogador.
		color = this.input.nextLine();
		// Recebendo o tabuleiro inicial.
		board = this.input.nextLine();
		this.myTable.updateTable(ToPseudoMatrix(board));

		
		
    	PrintStream debug = new PrintStream(System.out);

		
		// Aqui começa o jogo
		//Cor das peças do jogador
		debug.println(color);		
		//Imprimindo tabuleiro. 
		debug.println(board);

		try {
			file_out = new BufferedWriter(new FileWriter(this.ID + System.currentTimeMillis() + ".save"));
		} catch (Exception e) {
		}

		// Recebe o turno de quem joga.
		turn = this.input.nextLine();
		
		while(!turn.equals(Utils.EndGame)){
			// salvando jogo.
			try {
				file_out.write(board);
				file_out.newLine();
				file_out.flush();
			} catch (Exception e) {
				debug.println("erro na hora de salvar no arquivo");
			}
				
			// Verifica se quem joga é este cliente
			if(turn.equals(color)){				
				// Recebe a operação esperada.
				operation = this.input.nextLine();
				debug.println("Operation = " + operation); // debug
				do {							
					switch(operation){
					case Utils.MovementOrigin:
						debug.print("Digite a casa de origem: ");
						play = getOrigin();
						this.output.println(play);
						break;
					case Utils.MovementDestiny:
						debug.print("Digite de destino: ");
						play = getDestiny();
						this.output.println(play);
						break;
					case Utils.Moves:
						debug.println("Movimentos disponíveis: ");
						moves = this.input.nextLine();
						if(this.willShowMoves)myTable.showMoves(ConvertMoves(UnconcatenateMoves(moves)));
						debug.println(moves);
						break;
					case Utils.Promotion:
						debug.print("Digite a promoção: ");
						play = getPromotion();
						this.output.println(play);
						break;
					default:

						break;
					}
					// Enquanto a operação for invalida.
					operation = this.input.nextLine();
					debug.println("Operation = " + operation); // debug
				} while(!operation.equals(Utils.Sucess));
								
			}
			board = this.input.nextLine(); // Recebendo estado atual do tabuleiro.
			this.myTable.updateTable(ToPseudoMatrix(board));
			debug.println(board);
			// Recebe o turno de quem joga.
			turn = this.input.nextLine();
		}
		// Recebe o resultado final.
		turn = this.input.nextLine();
		debug.println(turn);
		System.out.println("olha aqui-------------->" + turn);
		myTable.endGame(turn);

		try{
			this.client.close();		
		} catch(Exception e){;}
	}

	/**
     * Função que converte  a String de movimentos possíveis.
     * @return Um vetor com todos os inteiros da String.
     * @param strmoves O vetor de Strings.
     */
    public static int[] ConvertMoves(String[] strmoves){
    	if(strmoves[0].equals(""))return null;
        int[] intmoves = new int[strmoves.length];
        int[] coordinates;

        for (int i = 0; i < strmoves.length; i++){
            // Transformando em coordenadas matriciais.
            coordinates = Chess.FromFEN(strmoves[i]);

            // Obtendo a coordenada pseudo-matricial.
            intmoves[i] = coordinates[0] * Chess.NUMBER_OF_FILES + coordinates[1];;
        }

        return intmoves;
    }

    /**
     * Função que desconcatena as casas de destino possíveis.
     * @return Vetor de Strings com as casas de destino possíveis.
     * @param concat A String concatenada.
     */
    public static String[] UnconcatenateMoves(String concat){
        return concat.split(" ");
    }

	/**
	 * Método main somente para testar o servidor.
	 * @param args
	 */
	public static void main(String[] args) {
		String ip = "127.0.0.1";
		String ID = "Mike";
		int port = 8000;

		System.out.println("Conectando...");
		GUIClient c = new GUIClient(ip, port, ID, true, true);

		
		if(c.connect()){
			System.out.println("Conectado com sucesso, esperando outro jogador.");
			while(!c.inGame()){
				System.out.println("Esperando outro jogador ...");
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("O jogo vai começar.");
		} else {
			System.out.println("Impossivel se conectar ao servidor.");			
		}
		
	}
}