package chess.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Está classe é apenas para rodar o cliente e suas variações.
 * @author Marcos Cesar Ribeiro de Camargo - 9278045
 *
 */
public class Main {
/**
 * Methodo mais para execução.
 * @param args
 */
	public static void main(String[] args) {
		String ip = "127.0.0.1";
		int port = 8000;
		Client c1 = null;
		Client c2 = null;

		System.out.println("Menu\n"
						+ "1. IAvsIA\n"
						+ "2. PlayervsIA\n"
						+ "3. PlayervsPlayer\n"
						+ "4. LoadGame");
		
		String option = null;
		String in = Utils.getString();
		
		switch(in){
		case "1":
			option = Utils.IAvsIA;
			break;
		case "2":
			option = Utils.IAvsClient;
			break;
		case "3":
			option = Utils.ClientvsClient;
			break;
		case "4":
			option = Utils.LoadvsIA;
			break;
			
		}
		 
		switch(option){
		case Utils.IAvsIA:
			
			System.out.println("Conectando...");
			c1 = new ClientAI(ip, port, "IA1");
			c2 = new ClientAI(ip, port, "IA2");
			
						
			if(c1.connect() && c2.connect()){
				System.out.println("Conectado com sucesso, esperando outro jogador.");
				while(!c1.inGame() && !c2.inGame());
				System.out.println("O jogo vai começar.");
			} else {
				System.out.println("Impossivel se conectar ao servidor.");			
			}
			
			break;
			
		case Utils.IAvsClient:
			System.out.println("Conectando...");
			c1 = new Client(ip, port, "USER1");
			c2 = new ClientAI(ip, port, "IA2");
			
			if(c1.connect() && c2.connect()){
				System.out.println("Conectado com sucesso, esperando outro jogador.");
				while(!c1.inGame() && !c2.inGame());
				System.out.println("O jogo vai começar.");
			} else {
				System.out.println("Impossivel se conectar ao servidor.");			
			}			
			break;
			
		case Utils.ClientvsClient:	
			System.out.println("Conectando...");
			c1 = new Client(ip, port, "USER1");
			c2 = new Client(ip, port, "USER2");
			
			if(c1.connect() && c2.connect()){
				System.out.println("Conectado com sucesso, esperando outro jogador.");
				while(!c1.inGame() && !c2.inGame());
				System.out.println("O jogo vai começar.");
			} else {
				System.out.println("Impossivel se conectar ao servidor.");			
			}			

			break;

		case Utils.LoadvsIA:	
			System.out.println("Conectando...");
			String FEN = null;
			
			System.out.print("Nome do arquivo: ");
			String token = Utils.getString();

			BufferedReader file_in = null;
			// Recebendo antigo.
    		try {
				file_in = new BufferedReader(new FileReader(token + ".save"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		String line = null;
    		do {
    			FEN = line;
    			try {
					line = file_in.readLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}while(line != null);
	    	try {
				file_in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			c1 = new Client(ip, port, "USER1");
			c2 = new ClientAI(ip, port, "IA2", FEN);
			
			if(c1.connect() && c2.connect()){
				System.out.println("Conectado com sucesso, esperando outro jogador.");
				while(!c1.inGame() && !c2.inGame());
				System.out.println("O jogo vai começar.");
			} else {
				System.out.println("Impossivel se conectar ao servidor.");			
			}			

			break;

		}
	}

}
