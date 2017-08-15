package chess.server;


import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.Queue;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Classe que representa o servidor, que recebe as conecções dos clientes.
 * Quando há duas conecções o servidor começa uma partida.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 *
 */
public class Server extends Thread{
	private ServerSocket ss = null;
	private Socket client = null;
	private Queue<Socket> q = null;

	private int port;

	/**
	 * Construtor que cria o servidor na porta passada por parâmetro.
	 * @param port Inteiro contendo a porta para abrir a conexão.
	 */
	public Server(int port){
		this.port = port;
		this.q = new LinkedList<Socket>();
	}

	/**
	 * Metodo que abre o servidor.
	 * @return Booleano contendo true(sucesso) ou false(falha).
	 */
	public boolean open(){
		try{
			this.ss = new ServerSocket(this.port);		
			this.ss.setSoTimeout(100);
		} catch (Exception e){e.printStackTrace();return false;}
		this.start();
		return true;
	}
	
	/**
	 * Método que fecha o servidor.
	 */
	public void close(){
		try {
			ss.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método run da Thread que executa o servidor. Espera a coneção de dois clientes e inicia um game entre eles.
	 */
@Override
	public void run(){
		
		while(true){
			client = null;
			try {
				this.client = this.ss.accept();
			}catch (SocketTimeoutException a){
				
			} catch (IOException e){;break;}
			if(client != null){
				q.add(client);
			}
			if(q.size() >= 2){
				Socket playerOne = q.poll();
				if(playerOne.isClosed()){
					break;
				}
				Socket playerTwo = q.poll();
				if(playerTwo.isClosed()){
					q.add(playerOne);
					break;
				}

				Game g = new Game(playerOne, playerTwo);
				g.start();
			}
			
		}
		while(!q.isEmpty()){
			Socket aux = q.poll();
			try {
				aux.close();
			} catch (IOException e) {}
		}
	}


	/**
	 * Método main somente para testar o servidor.
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
	
		int port = 8000; // pode modificar.
	
		System.out.println("Criando Servidor...");
		Server s = new Server(port);
		if(s.open()){
			System.out.println("Servidor iniciado na porta "+ port +" com sucesso!");
		} else {
			System.out.println("Não foi possivel iniciar o servidor na port " + port+". :(");
		}
	
		System.out.println("Digite 'exit' para sair.");
		String exit;
		do {
			exit = Utils.getString();
		}while(!exit.equals("exit"));
	
		s.close();
		
	}
}