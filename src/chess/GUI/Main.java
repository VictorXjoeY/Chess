package chess.GUI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import chess.server.Utils;

/**
 * Classe principal apra execuar o jogo do cliente
 * @author Gabriel Simmel Nasciemnto
 * @param
 * @return
 * @throws
 *
 */
public class Main {

	public static void main(String[] args) {
		String ip = "127.0.0.1";
		int port = 8000;
		GUIClient c1 = null;
		GUIClient c2 = null;

		System.out.println("Menu\n"
						+ "1. IAvsIA\n"
						+ "2. PlayervsIA\n"
						+ "3. PlayervsPlayer\n"
						+ "4. LoadGame");
		
		String option = null;
		StartMenu menu = new StartMenu();

		String in = menu.getChoice();
		
		switch(in){
		case "IA vs AI":
			option = Utils.IAvsIA;
			break;
		case "Single Player":
			option = Utils.IAvsClient;
			break;
		case "Multi Player":
			option = Utils.ClientvsClient;
			break;
		case "Load Game":
			option = Utils.LoadvsIA;
			break;
			
		}

		switch(option){
		case Utils.IAvsIA:
			
			System.out.println("Conectando...");
			c1 = new GUIClientAI(ip, port, "IA1", true, false);
			c2 = new GUIClientAI(ip, port, "IA2", false, false);
			
						
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
			c1 = new GUIClient(ip, port, "USER1", true, true);
			c2 = new GUIClientAI(ip, port, "IA2", false, false);
			
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
			c1 = new GUIClient(ip, port, "USER1", true, true);
			
			if(c1.connect()){
				System.out.println("Conectado com sucesso, esperando outro jogador.");
				while(!c1.inGame());
				System.out.println("O jogo vai começar.");
			} else {
				System.out.println("Impossivel se conectar ao servidor.");			
			}			

			break;

		case Utils.LoadvsIA:	
			System.out.println("Conectando...");
			String FEN = null;
			
			System.out.print("Nome do arquivo: ");

			LoadFrame loadFrame = new LoadFrame();

			BufferedReader file_in = null;
			// Recebendo antigo.
			boolean flag;
			do{
				flag = false;

				//vai ficar esperando clicar em iniciar para ai pegar o texto;
				while(!loadFrame.getChoice().equals("Iniciar"));
				String token = loadFrame.getText();
				//testar se conseguiu dar load, se não vai pedir denovo o texto
	    		try {
					file_in = new BufferedReader(new FileReader(token + ".save"));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					flag = true;
					loadFrame.setIniButtonRed();
				}
			}while(flag);
			loadFrame.hi();

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
			
			c1 = new GUIClient(ip, port, "USER1", true, true);
			c2 = new GUIClientAI(ip, port, "IA2", FEN, false, false);
			
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
