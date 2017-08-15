package chess.board;
/**
 * Classe feita para definir valores constantes e verificações gerais relacionadas ao jogo de Xadrez.
 * @author Victor Forbes - 9293394
 */
public abstract class Chess{
	public static final String newGame = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"; // FEN Board para um novo jogo.
	public static final int INVALID = -1; // Constante para posição ou peça inválida.
	public static final int PAWN = 0; // Peão.
	public static final int KNIGHT = 1; // Cavalo.
	public static final int BISHOP = 2; // Bispo.
	public static final int ROOK = 3; // Torre.
	public static final int QUEEN = 4; // Rainha.
	public static final int KING = 5; // Rei.
	public static final int NONE = 0; // Definindo constante para sem peça.
	public static final int WHITE = 1; // Definindo constante para a cor branca.
	public static final int BLACK = 2; // Definindo constante para a cor preta.
	public static final int NUMBER_OF_RANKS = 8; // Linhas.
	public static final int NUMBER_OF_FILES = 8; // Colunas.
	public static final int PAWN_VALUE = 100; // Definindo o valor de um Peão.
	public static final int KNIGHT_VALUE = 350; // Definindo o valor de um Cavalo.
	public static final int BISHOP_VALUE = 450; // Definindo o valor de um Bispo.
	public static final int ROOK_VALUE = 550; // Definindo o valor de uma Torre.
	public static final int QUEEN_VALUE = 1000; // Definindo o valor de uma Rainha.
	public static final int KING_VALUE = 2000; // Definindo o valor de um Rei.
	public static final int PROMOTION = 1; // Definindo constantes para o movimento especial.
	public static final int CASTLING = 2; // Definindo constantes para o movimento especial.
	public static final int EN_PASSANT = 3; // Definindo constantes para o movimento especial.
	public static final int JUMP = 4; // Definindo constantes para o movimento especial.
	public static final int NO_DIRECTION = 0; // Constante auxiliar para a função de mover em linha.
	public static final int UP = -1; // Constante auxiliar para a função de mover em linha.
	public static final int DOWN = 1; // Constante auxiliar para a função de mover em linha.
	public static final int LEFT = -1; // Constante auxiliar para a função de mover em linha.
	public static final int RIGHT = 1; // Constante auxiliar para a função de mover em linha.
	public static final int GAME_IN_PROGRESS = 0; // Constante auxiliar para representar o status do jogo.
	public static final int WHITE_TEAM_WINS = 1; // Constante auxiliar para representar o status do jogo.
	public static final int BLACK_TEAM_WINS = 2; // Constante auxiliar para representar o status do jogo.
	public static final int TRIPLE_REPETITION = 3; // Constante auxiliar para representar o status do jogo.
	public static final int INSUFFICIENT_MATERIAL = 4; // Constante auxiliar para representar o status do jogo.
	public static final int FIFTY_MOVEMENTS = 5; // Constante auxiliar para representar o status do jogo.
	public static final int STALEMATE = 6; // Constante auxiliar para representar o status do jogo.

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
	 * Função que converte uma coordenada em FEN em coordenada matricial.
	 * @return Uma array com 2 elementos contendo as coordenadas x e y respectivamente.
	 * @param fen A casa do tabuleiro em notação FEN.
	 */
    public static int[] FromFEN(String fen){
    	return new int[]{NUMBER_OF_RANKS - (fen.charAt(1) - '0'), fen.charAt(0) - 'a'};
    }

    /**
	 * Retorna true se as coordenadas (x, y) pertencerem a matriz do tabuleiro.
	 * @return True se estiver dentro do tabuleiro e False se estiver fora.
	 * @param x Coordenada x.
	 * @param y Coordenada y.
	 */
	public static boolean IsInside(int x, int y){
		return x >= 0 && x < Chess.NUMBER_OF_RANKS && y >= 0 && y < Chess.NUMBER_OF_FILES;
	}

    /**
     * Função que verifica se os dois times são diferentes (retorna falso caso algum deles não seja um time).
     * @return True se a e b forem de times diferentes e False caso sejam de times iguais ou se algum deles seja um valor inválido.
     * @param a Time a.
     * @param b Time b.
     */
    public static boolean DifferentTeams(int a, int b){
    	// Se forem times realmente.
    	if ((a == Chess.WHITE || a == Chess.BLACK) && (b == Chess.WHITE || b == Chess.BLACK)){
    		return a != b;
    	}

    	return false;
    }
}