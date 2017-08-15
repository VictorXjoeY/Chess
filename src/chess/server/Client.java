package chess.server;


import java.net.Socket;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import chess.board.Chess;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintStream;

/**
 * Classe que representa um cliente.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 *
 */
public class Client extends Thread {
	
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


	/**
	 * Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 */
	public Client(String ip, int port, String ID){
		this.ip = ip;
		this.port = port;
		this.inGame = false;
		this.ID = ID;
		this.initialFEN = null;
	}

	/**
	 * Construtor que recebe o ip, a porta e a idêntificação do cliente.
	 * @param ip IP para se conectar ao servidor.
	 * @param port Porta para se conectar ao servidor.
	 * @param ID Idêntificador do cliente.
	 * @param FEN String com o jogo inicial.
	 */
	protected Client(String ip, int port, String ID, String FEN){
		this.ip = ip;
		this.port = port;
		this.inGame = false;
		this.ID = ID;
		this.initialFEN = FEN;
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
	 * Função que recebe a casa a origem do movimento.
	 * @return Movimento lido.
	 */
    protected String getOrigin(){
		String movement = Utils.getString();
    	return movement;
    }
    
	/**
	 * Função que recebe o destiny da peça.
	 * @return Movimento lido.
	 */
    protected String getDestiny(){
		String movement = Utils.getString();
    	return movement;
    }

	/**
	 * Função que recebe a promoção a ser realizada.
	 * @return Movimento lido.
	 */
    protected String getPromotion(){
		String movement = Utils.getString();
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
		
		
    	PrintStream debug = new PrintStream(System.out);

		
		// Aqui começa o jogo
		//Cor das peças do jogador
		debug.println(color);		
		//Imprimindo tabuleiro. 
		debug.println(board);

		try {
			Calendar c = Calendar.getInstance();
			Date d = c.getTime();
			String date = d.toString();
			date = date.replace("BRT", "");
			date = date.replaceAll("[ ]+", "-");
			
			file_out = new BufferedWriter(new FileWriter(this.ID +"-" + date + ".save"));
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
			debug.println(board);
			// Recebe o turno de quem joga.
			turn = this.input.nextLine();
		}
		// Recebe o resultado final.
		turn = this.input.nextLine();
		debug.println(turn);

		try{
			this.client.close();		
		} catch(Exception e){;}
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
		Client c = new Client(ip, port, ID);

		
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