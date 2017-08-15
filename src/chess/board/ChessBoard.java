package chess.board;

import java.lang.Math;
import java.util.HashMap;
import java.util.Random;

import chess.pieces.*;
 
/**
 * Essa classe é a classe principal do jogo de Xadrez. Ela integra todos seus componentes principais,
 * sendo eles regras, constantes, funcionalidades e as peças.
 * @author Victor Forbes - 9293394
 */
public class ChessBoard implements Cloneable{
    private HashMap<String, Integer> count; // Usado para a regra da tripla repetição.
    private ChessPiece[][] matrix; // Matriz de peças.
    private WhiteKing whiteKing; // Referência direta para o rei branco.
    private BlackKing blackKing; // Referência direta para o rei preto.
    private int whoseTurn; // De quem é a vez.
    private String castling; // Possibilidades para o Roque.
    private String enPassant; // Casa do movimento En Passant.
    private int halfTurns; // Número de meios-turnos desde a última captura ou o último movimento de um peão.
    private int turns; // Número de turnos completos.
    private boolean tripleRepetition; // Flag que é setada para true caso haja tripla repetição de um estado do tabuleiro.
    private int status;
    private Random rand;
 
    /**
     * Construtor do tabuleiro que recebe a string em FEN para montar o tabuleiro.
     * @param fen A String e notação FEN para construir o tabuleiro.
     */
    public ChessBoard(String fen){
        String[] str = fen.split(" "); // Separando a notação em partes.
        int x = 0;
 
        // Instanciando uma nova classe random.
        rand = new Random();
 
        // Criando a matriz de peças do tabuleiro.
        this.matrix = new ChessPiece[Chess.NUMBER_OF_RANKS][Chess.NUMBER_OF_FILES];
 
        // Lendo o tabuleiro.
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++, x++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++, x++){
                // Se for um número.
                if (Character.isDigit(str[0].charAt(x))){
                    j += Character.getNumericValue(str[0].charAt(x)) - 1;
                }
                else{
                    // Instanciando uma peça.
                    switch (str[0].charAt(x)){
                        case 'p':
                            this.matrix[i][j] = new BlackPawn(this, i, j);
                            break;
                        case 'n':
                            this.matrix[i][j] = new BlackKnight(this, i, j);
                            break;
                        case 'b':
                            this.matrix[i][j] = new BlackBishop(this, i, j);
                            break;
                        case 'r':
                            this.matrix[i][j] = new BlackRook(this, i, j);
                            break;
                        case 'q':
                            this.matrix[i][j] = new BlackQueen(this, i, j);
                            break;
                        case 'k':
                            this.matrix[i][j] = this.blackKing = new BlackKing(this, i, j);
                            break;
                        case 'P':
                            this.matrix[i][j] = new WhitePawn(this, i, j);
                            break;
                        case 'N':
                            this.matrix[i][j] = new WhiteKnight(this, i, j);
                            break;
                        case 'B':
                            this.matrix[i][j] = new WhiteBishop(this, i, j);
                            break;
                        case 'R':
                            this.matrix[i][j] = new WhiteRook(this, i, j);
                            break;
                        case 'Q':
                            this.matrix[i][j] = new WhiteQueen(this, i, j);
                            break;
                        case 'K':
                            this.matrix[i][j] = this.whiteKing = new WhiteKing(this, i, j);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
 
        // Setando a variável que indica de quem é a vez.
        this.whoseTurn = (str[1].charAt(0) == 'w' ? Chess.WHITE : Chess.BLACK);
 
        // Setando a disponibilidade de Roque.
        this.castling = str[2];
 
        // Setando a disponibilidade de En Passant.
        this.enPassant = str[3];
 
        // Setando a quantidade de turnos e meio-turnos.
        this.halfTurns = Integer.parseInt(str[4]);
        this.turns = Integer.parseInt(str[5]);
 
        // Inicializando mapa.
        this.count = new HashMap<String, Integer>();
 
        // Primeira jogada.
        this.count.put(this.ToFEN().split(" ")[0], 1);
 
        // Gerando a primeira leva de movimentos.
        this.GenerateMoves();
    }
 
    /**
     * Função que promove um peão que acabou de realizar o movimento passado por parâmetro.
     * @param fen Movimento que resultou em promoção.
     * @param newPiece Código da peça promovida.
     */
    public void Promote(String fen, char newPiece){
        int[] coordinates = Chess.FromFEN(fen.substring(2));
        int x = coordinates[0];
        int y = coordinates[1];
 
        switch (newPiece){
            case 'N':
                this.matrix[x][y] = (this.whoseTurn == Chess.WHITE ? new BlackKnight(this, x, y) : new WhiteKnight(this, x, y));
                break;
            case 'B':
                this.matrix[x][y] = (this.whoseTurn == Chess.WHITE ? new BlackBishop(this, x, y) : new WhiteBishop(this, x, y));
                break;
            case 'R':
                this.matrix[x][y] = (this.whoseTurn == Chess.WHITE ? new BlackRook(this, x, y) : new WhiteRook(this, x, y));
                break;
            case 'Q':
                this.matrix[x][y] = (this.whoseTurn == Chess.WHITE ? new BlackQueen(this, x, y) : new WhiteQueen(this, x, y));
                break;
            default:
                break;
        }
 
        // Gerando os movimentos de todas as peças considerando a promoção que ocorreu.
        this.GenerateMoves();
    }
 
    /**
     * Função que realiza o movimento descrito pelo parâmetro passado.
     * @return Um código indicando que tipo de movimento foi feito.
     * @param fen O movimento a ser realizado.
     */
    public int MakeMove(String fen){
        int[] coordinates = Chess.FromFEN(fen); // Recuperando as coordenadas iniciais.
        int flag = Chess.NONE; // Flag para indicar que tipo de movimento ocorreu.
        int xi = coordinates[0];
        int yi = coordinates[1];
        String bigFEN;
        int xf, yf;
        Integer aux;
 
        // Recuperando as coordenadas finais.
        coordinates = Chess.FromFEN(fen.substring(2));
        xf = coordinates[0];
        yf = coordinates[1];
 
        // Atualizando os Half-Turns.
        if (this.matrix[xi][yi] instanceof Pawn || Chess.DifferentTeams(this.whoseTurn, this.GetColor(xf, yf))){
            // Se houve movimento de peão ou ataque.
            this.halfTurns = 0;
        }
        else{
            // Caso contrário.
            this.halfTurns++;
        }
 
        // Executando o movimento.
        if (this.matrix[xi][yi] instanceof Pawn && Chess.ToFEN(xf, yf).equals(this.enPassant)){ // En Passant.
            // Movendo a peça.
            this.matrix[xf][yf] = this.matrix[xi][yi];
            this.matrix[xi][yi] = null;
 
            // Matando a peça.
            if (this.whoseTurn == Chess.WHITE){
                this.matrix[xf + 1][yf] = null;
            }
            else{
                this.matrix[xf - 1][yf] = null;
            }
 
            flag = Chess.EN_PASSANT;
        }
        else if (this.matrix[xi][yi] instanceof King && Math.abs(yf - yi) == 2){ // Roque.
            // Movendo a peça.
            this.matrix[xf][yf] = this.matrix[xi][yi];
            this.matrix[xi][yi] = null;
 
            if (yf > yi){ // Roque para a direita.
                this.matrix[xf][yf - 1] = this.matrix[xf][7];
                this.matrix[xf][yf - 1].SetPosition(xf, yf - 1);
                this.matrix[xf][7] = null;
            }
            else{ // Roque para a esquerda.
                this.matrix[xf][yf + 1] = this.matrix[xf][0];
                this.matrix[xf][yf + 1].SetPosition(xf, yf + 1);
                this.matrix[xf][0] = null;
            }
 
            flag = Chess.CASTLING;
        }
        else{
            if (this.matrix[xi][yi] instanceof Pawn && (xf == 0 || xf == 7)){ // Promoção.
                flag = Chess.PROMOTION;
            }
 
            if (this.matrix[xi][yi] instanceof Pawn && Math.abs(xf - xi) == 2){ // Jump.
                flag = Chess.JUMP;
            }
 
            // Movendo (e matando) a peça.
            this.matrix[xf][yf] = this.matrix[xi][yi];
            this.matrix[xi][yi] = null;
        }
 
        // Setando as novas coordenadas da peça.
        this.matrix[xf][yf].SetPosition(xf, yf);
 
        // Atualizando En Passant.
        if (flag == Chess.JUMP){
            this.enPassant = (this.whoseTurn == Chess.WHITE ? Chess.ToFEN(xf + 1, yf) : Chess.ToFEN(xf - 1, yf));
        }
        else{
            this.enPassant = "-";
        }
 
        // Atualizando Roque caso o Rei tenha sido movido.
        if (this.matrix[xf][yf] instanceof WhiteKing){
            this.castling = this.castling.replaceAll("[KQ]", "");
        }
        else if (this.matrix[xf][yf] instanceof BlackKing){
            this.castling = this.castling.replaceAll("[kq]", "");
        }
 
        // Atualizando Roque caso algo tenha acontecido nos cantos do tabuleiro (com as torres).
        if ((xi == 0 && yi == 0) || (xf == 0 && yf == 0)){ // Canto superior esquerdo.
            this.castling = this.castling.replaceAll("q", "");
        }
        else if ((xi == 0 && yi == 7) || (xf == 0 && yf == 7)){ // Canto superior direito.
            this.castling = this.castling.replaceAll("k", "");
        }
        else if ((xi == 7 && yi == 0) || (xf == 7 && yf == 0)){ // Canto inferior esquerdo.
            this.castling = this.castling.replaceAll("Q", "");
        }
        else if ((xi == 7 && yi == 7) || (xf == 7 && yf == 7)){ // Canto inferior direito.
            this.castling = this.castling.replaceAll("K", "");
        }
 
        if (this.castling.equals("")){
            this.castling = "-";
        }
 
        // Incrementando contador de turnos.
        if (this.whoseTurn == Chess.BLACK){
            this.turns++;
        }
 
        // Trocando o turno.
        this.whoseTurn = (this.whoseTurn == Chess.WHITE ? Chess.BLACK : Chess.WHITE);
 
        bigFEN = this.ToFEN().split(" ")[0];
        aux = this.count.get(bigFEN);
 
        if (aux == null){
            this.count.put(bigFEN, 1);
        }
        else{
            this.count.put(bigFEN, aux + 1);
 
            if (aux + 1 >= 3){
                this.tripleRepetition = true;
            }
        }
 
        // Gerando os movimentos do próximo jogador.
        this.GenerateMoves();
 
        // Retornando a flag.
        return flag;
    }
 
    /**
     * Função que verifica se uma casa do tabuleiro está sob ataque de um determinado time.
     * @return True caso esteja ou false caso não esteja.
     * @param x Coordenada x.
     * @param y Coordenada y.
     * @param team O time atacante.
     */
    public boolean CellUnderAttack(int x, int y, int team){
        int[] nx = new int[]{x + 1, x - 1, x + 2, x - 2, x + 2, x - 2, x + 1, x - 1};
        int[] ny = new int[]{y - 2, y - 2, y - 1, y - 1, y + 1, y + 1, y + 2, y + 2};
        int pawnX, i, j;
 
        // Altura do peão inimigo.
        pawnX = (team == Chess.WHITE ? x + 1 : x - 1);
 
        // Checando se está sob ataque de um peão à esquerda.
        if (this.GetColor(pawnX, y - 1) == team && this.matrix[pawnX][y - 1] instanceof Pawn){
            return true;
        }
 
        // Checando se está sob ataque de um peão à esquerda.
        if (this.GetColor(pawnX, y + 1) == team && this.matrix[pawnX][y + 1] instanceof Pawn){
            return true;
        }
 
        // Checando se está sob ataque de um cavalo.
        for (i = 0; i < 8; i++){
            if (this.GetColor(nx[i], ny[i]) == team && this.matrix[nx[i]][ny[i]] instanceof Knight){
                return true;
            }
        }
 
        // Diagonal esquerda acima.
        if (this.UnderAttackInLine(x, y, Chess.UP, Chess.LEFT, team)){
            return true;
        }
 
        // Diagonal esquerda abaixo.
        if (this.UnderAttackInLine(x, y, Chess.DOWN, Chess.LEFT, team)){
            return true;
        }
 
        // Diagonal direita acima.
        if (this.UnderAttackInLine(x, y, Chess.UP, Chess.RIGHT, team)){
            return true;
        }
 
        // Diagonal direita abaixo.
        if (this.UnderAttackInLine(x, y, Chess.DOWN, Chess.RIGHT, team)){
            return true;
        }
 
        // Cima.
        if (this.UnderAttackInLine(x, y, Chess.UP, Chess.NO_DIRECTION, team)){
            return true;
        }
 
        // Baixo.
        if (this.UnderAttackInLine(x, y, Chess.DOWN, Chess.NO_DIRECTION, team)){
            return true;
        }
 
        // Esquerda.
        if (this.UnderAttackInLine(x, y, Chess.NO_DIRECTION, Chess.LEFT, team)){
            return true;
        }
         
        // Direita.
        if (this.UnderAttackInLine(x, y, Chess.NO_DIRECTION, Chess.RIGHT, team)){
            return true;
        }
 
        // Checando se está sob ataque de um Rei.
        for (i = x - 1; i <= x + 1; i++){
            for (j = y - 1; j <= y + 1; j++){
                if (i != x || j != y){
                    if (this.GetColor(i, j) == team && this.matrix[i][j] instanceof King){
                        return true;
                    }
                }
            }
        }
 
        // Se não estiver sob ataque, retorne false.
        return false;
    }
 
    /**
     * Função auxiliar para verificar se a casa (x, y) está sob ataque do time team.
     * @return True caso esteja ou false caso não esteja.
     * @param x Coordenada x.
     * @param y Coordenada y.
     * @param xStep O passo vertical na matriz.
     * @param yStep O passo horizontal na matriz.
     * @param team O time a procurar.
     */
    private boolean UnderAttackInLine(int x, int y, int xStep, int yStep, int team){
        x += xStep;
        y += yStep;
 
        // Caminhando com o passo passado por parâmetro.
        while (this.GetColor(x, y) == Chess.NONE){
            x += xStep;
            y += yStep;
        }
 
        // Verificando se achou uma peça inimiga de um tipo contido na str passada por parâmetro.
        if (this.GetColor(x, y) == team){
            // Caso seja uma diagonal.
            if (xStep != Chess.NO_DIRECTION && yStep != Chess.NO_DIRECTION){
                // Caso seja uma rainha ou um bispo.
                if (this.matrix[x][y] instanceof Queen || this.matrix[x][y] instanceof Bishop){
                    return true;
                }
            }
            else{
                // Caso seja uma rainha ou uma torre.
                if (this.matrix[x][y] instanceof Queen || this.matrix[x][y] instanceof Rook){
                    return true;
                }
            }
        }
 
        return false;
    }
 
    /**
     * Função que verifica se um movimento deixará o Rei em Xeque.
     * @return True se deixar o rei em Xeque ou falso caso contrário.
     * @param movement O movimento em FEN.
     */
    public boolean LeavesKingUnderAttack(String movement){
        int[] coordinates;
        int xi, yi, xf, yf;
        ChessPiece aux, ep;
        boolean res = false;
 
        // Recuperando casas iniciais.
        coordinates = Chess.FromFEN(movement);
        xi = coordinates[0];
        yi = coordinates[1];
 
        // Recuperando casas finais.
        coordinates = Chess.FromFEN(movement.substring(2));
        xf = coordinates[0];
        yf = coordinates[1];
 
        ep = null;
 
        // Caso seja um En Passant.
        if (this.matrix[xi][yi] instanceof Pawn && Chess.ToFEN(xf, yf).equals(this.enPassant)){ // En Passant.
            ep = this.matrix[xi][yf];
            this.matrix[xi][yf] = null;
        }
 
        // Simulando o movimento.
        aux = this.matrix[xf][yf];
        this.matrix[xf][yf] = this.matrix[xi][yi];
        this.matrix[xi][yi] = null;
        this.matrix[xf][yf].SetPosition(xf, yf);
 
        // Verificando se o rei ficou em Xeque.
        res = this.KingInCheck();
         
        // Recuperando o estado inicial o movimento.
        this.matrix[xi][yi] = this.matrix[xf][yf];
        this.matrix[xf][yf] = aux;
        this.matrix[xi][yi].SetPosition(xi, yi);
 
        // Caso seja um En Passant.
        if (this.matrix[xi][yi] instanceof Pawn && Chess.ToFEN(xf, yf).equals(this.enPassant)){ // En Passant.
            this.matrix[xi][yf] = ep;
        }
 
        return res;
    }
 
    /**
     * Função que verifica se ocorreu Xeque-mate.
     * @return True caso o time atual tenha recebido Xeque-mate ou false caso contrário.
     */
    public boolean Checkmate(){
        // Caso o Rei do time atual esteja em Xeque e o time atual não possua mais jogadas.
        if (this.KingInCheck() && this.NoMovementsLeft()){
            if (this.whoseTurn == Chess.WHITE){
                this.status = Chess.BLACK_TEAM_WINS;
            }
            else{
                this.status = Chess.WHITE_TEAM_WINS;
            }
 
            return true;
        }
 
        return false;
    }
 
    /**
     * Função que verifica se ocorreu algum tipo de empate.
     * @return True caso tenha ocorrido empate ou false caso contrário.
     */
    public boolean Tie(){
        // Caso não haja mais movimentos e o rei não esteja em cheque.
        if (!this.KingInCheck() && this.NoMovementsLeft()){
            this.status = Chess.STALEMATE;
            return true;
        }
 
        // Regra dos 50 movimentos.
        if (this.halfTurns >= 50){
            this.status = Chess.FIFTY_MOVEMENTS;
            return true;
        }
 
        // Caso não haja peças o suficiente para realizar um cheque-mate.
        if (this.NoMaterial()){
            this.status = Chess.INSUFFICIENT_MATERIAL;
            return true;
        }
 
        // Caso tenha ocorrido uma Tripla Repetição.
        if (this.tripleRepetition){
            this.status = Chess.TRIPLE_REPETITION;
            return true;
        }
 
        return false;
    }
 
    /**
     * Verifica se o Rei do time atual está em Xeque.
     * @return True caso o rei do time atual esteja em Xeque ou false caso contrário.
     */
    private boolean KingInCheck(){
        if (this.whoseTurn == Chess.WHITE){
            return this.CellUnderAttack(this.whiteKing.GetX(), this.whiteKing.GetY(), Chess.BLACK);
        }
        else{
            return this.CellUnderAttack(this.blackKing.GetX(), this.blackKing.GetY(), Chess.WHITE);
        }
    }
 
    /**
     * Verifica se o time atual possui movimentos.
     * @return True caso o time atual não possua mais movimentos ou false caso contrário.
     */
    private boolean NoMovementsLeft(){
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                // Verificando se cada peça do time atual possui algum movimento.
                if (this.GetColor(i, j) == this.whoseTurn){
                    if (this.matrix[i][j].HasMovements()){
                        return false;
                    }
                }
            }
        }
 
        return true;
    }
 
    /**
     * Função que verifica se houve empate por falta de material.
     * @return True caso tenha ocorrido o empate ou falso caso contrário.
     */
    private boolean NoMaterial(){
        int whiteCounter, blackCounter, team;
        boolean whiteKnight, whiteBishop;
        boolean blackKnight, blackBishop;
 
        // Inicializando.
        whiteKnight = false;
        whiteBishop = false;
        blackKnight = false;
        blackBishop = false;
        whiteCounter = 0;
        blackCounter = 0;
 
        // Percorrendo o tabuleiro inteiro contando as peças brancas e pretas
        // e marcando se tem cavalo/bispo em ambos os times.
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                team = this.GetColor(i, j);
 
                if (team == Chess.WHITE){
                    if (this.matrix[i][j] instanceof WhiteKnight){
                        whiteKnight = true;
                    }
                    else if (this.matrix[i][j] instanceof WhiteBishop){
                        whiteBishop = true;
                    }
 
                    whiteCounter++;
                }
                else if (team == Chess.BLACK){
                    if (this.matrix[i][j] instanceof BlackKnight){
                        blackKnight = true;
                    }
                    else if (this.matrix[i][j] instanceof BlackBishop){
                        blackBishop = true;
                    }
 
                    blackCounter++;
                }
            }
        }
 
        // Caso Rei contra Rei.
        if (whiteCounter == 1 && blackCounter == 1){
            return true;
        }
 
        // Caso Rei branco e (Bispo ou Cavalo) contra Rei preto.
        if (whiteCounter == 2 && blackCounter == 1 && (whiteKnight || whiteBishop)){
            return true;
        }
 
        // Caso Rei preto e (Bispo ou Cavalo) contra Rei branco.
        if (whiteCounter == 1 && blackCounter == 2 && (blackKnight || blackBishop)){
            return true;
        }
 
        return false;
    }
 
    /**
     * Funão que dá uma pontuação para o estado atual do tabuleiro na visão do time "team".
     * @return A pontuação.
     * @param team O time aliado.
     */
    private double EvaluateBoard(int team){
        double num = 0.0;
        double den = 0.0;
        int enemy = (team == Chess.WHITE ? Chess.BLACK : Chess.WHITE);
 
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                if (this.GetColor(i, j) == team){ // Se for aliado.
                    num += (this.CellUnderAttack(i, j, team) ? (double)this.matrix[i][j].GetValue() / 2.0 : 0.0);
                    den += (this.CellUnderAttack(i, j, enemy) ? (double)this.matrix[i][j].GetValue() : 0.0);
                }
                else if (this.GetColor(i, j) == enemy){ // Se for inimigo.
                    num += (this.CellUnderAttack(i, j, team) ? (double)this.matrix[i][j].GetValue() : 0.0);
                    den += (this.CellUnderAttack(i, j, enemy) ? (double)this.matrix[i][j].GetValue() / 2.0 : 0.0);
                }
                else{ // Casa vazia.
                    num += this.CellUnderAttack(i, j, team) ? 50.0 : 0.0;
                    den += this.CellUnderAttack(i, j, enemy) ? 50.0 : 0.0;
                }
            }
        }
 
        return num / (den + 1.0);
    }
 
    /**
     * Retorna o melhor movimento para o time atual.
     * @return O movimento em FEN.
     */
    public String GetBestMovement(){
        ChessBoard aux;
        String[] moves;
        String bestPlay = "";
        double bestVal = 0.0;
        String play;
        double val;
 
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                // Recuperando os movimentos possíveis do time atual.
                if (this.GetColor(i, j) == this.whoseTurn){
                    moves = this.matrix[i][j].GetMoves();
 
                    // Movimentos dessa peça.
                    for (int k = 0; k < moves.length; k++){
                        play = Chess.ToFEN(i, j) + moves[k];
 
                        // Clonando o tabuleiro atual.
                        aux = this.clone();
 
                        // Realizando o movimento.
                        aux.MakeMove(play);
 
                        // Dando um score.
                        val = aux.EvaluateBoard(this.whoseTurn);
 
                        // Trocando a melhor jogada.
                        if (bestPlay.equals("") || (val > bestVal && rand.nextInt(100) >= 30)){
                            bestPlay = play;
                            bestVal = val;
                        }
                    }
 
                }
            }
        }
 
        return bestPlay + "Q";
    }
 
    /**
     * Função que retorna todos os movimentos possíveis a partir de uma casa de origem.
     * @return Um vetor de Strings contendo as casas de destino.
     * @param source A casa de origem.
     */
    public String[] GetPossibleMoves(String source){
        int[] coordinates = Chess.FromFEN(source);
        int x = coordinates[0];
        int y = coordinates[1];
 
        return this.matrix[x][y].GetMoves();
    }
 
    /**
     * Função que retorna o veredito final do jogo.
     * @return Um código representando o status do jogo.
     */
    public int GetWinner(){
        return this.status;
    }
 
    /**
     * Getter para a disponibilidade do En Passant.
     * @return A String que representa o En Passant.
     */
    public String GetEnPassant(){
        return this.enPassant;
    }
 
    /**
     * Getter para a disponibilidade do Roque.
     * @return A String que representa o Roque.
     */
    public String GetCastling(){
        return this.castling;
    }
 
    /**
     * Getter para saber de quem é a vez.
     * @return De quem é a vez.
     */
    public int GetTurn(){
        return this.whoseTurn;
    }
 
    /**
     * Getter para saber a cor da peça na posição (x, y).
     * @return O código da cor daquela coordenada. NONE caso não tenha peça e INVALID caso esteja fora do tabuleiro.
     * @param x Coordenada x.
     * @param y Coordenada y.
     */
    public int GetColor(int x, int y){
        // Se estiver dentro do tabuleiro.
        if (Chess.IsInside(x, y)){
            // Se não houver peça.
            if (this.matrix[x][y] == null){
                return Chess.NONE;
            }
 
            // Retornando a cor da peça.
            return this.matrix[x][y].GetColor();
        }
 
        // Retornando uma flag inválida.
        return Chess.INVALID;
    }
 
    /**
     * Função que verifica se o movimento inserido pelo usuário é válido.
     * @return True se for um movimento possível ou false caso não seja.
     * @param fen O movimento.
     */
    public boolean ValidMove(String fen){
        int[] coordinates = Chess.FromFEN(fen); // Transformando em coordenadas matriciais.
        int xi = coordinates[0];
        int yi = coordinates[1];
 
        // Verdadeiro se a peça que estiver tentando mover for do seu time e for um movimento possível para essa peça.
        return this.GetColor(xi, yi) == this.whoseTurn && this.matrix[xi][yi].ValidMove(fen.substring(2));
    }
 
    public boolean ValidSource(String source){
        int[] coordinates = Chess.FromFEN(source);
        int xi = coordinates[0];
        int yi = coordinates[1];
 
        // Verdadeiro se a peça na casa (xi, yi) for do time atual.
        return this.GetColor(xi, yi) == this.whoseTurn;
    }
 
    /**
     * Função que gera os movimentos de todas as peças do time que está jogando.
     */
    public void GenerateMoves(){
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                // Gerando os movimentos de todas as peças.
                if (this.GetColor(i, j) != Chess.NONE){
                    this.matrix[i][j].GenerateMoves();
                }
            }
        }
    }
 
    /**
     * Função auxiliar que imprime os movimentos possíveis de todas as peças do time que está jogando.
     */
    public void PrintMoves(){
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                // Imprimindo os movimentos apenas das peças do time que está jogando.
                if (this.GetColor(i, j) == this.whoseTurn){
                    this.matrix[i][j].PrintMoves();
                }
            }
        }
    }
 
    /**
     * Função que retorna a representação do tabuleiro em FEN.
     * @return FEN Board.
     */
    public String ToFEN(){
        char counter = '0';
        String fen = "";
 
        // Preenchendo a parte que se refere ao tabuleiro.
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                if (this.matrix[i][j] == null){
                    counter++;
                }
                else{
                    fen += (counter == '0' ? "" : ((Character)counter).toString());
                    fen += this.matrix[i][j].toString();
                    counter = '0';
                }
            }
 
            // Preenchendo casas restantes com um número e a barra.
            fen += (counter == '0' ? "" : ((Character)counter).toString());
            fen += (i == Chess.NUMBER_OF_RANKS - 1 ? "" : "/");
            counter = '0';
        }
 
        // Preenchendo as outras partes.
        fen += (this.whoseTurn == Chess.WHITE ? " w " : " b ");
        fen += this.castling + " " + this.enPassant + " " + ((Integer)this.halfTurns).toString() + " " + ((Integer)this.turns).toString();
 
        return fen;
    }
 
    /**
     * Função que retorna uma representação textual do tabuleiro.
     * @return O tabuleiro em texto.
     */
    @Override
    public String toString(){
        String str = "";
 
        // Concatenando as casas do tabuleiro.
        for (int i = 0; i < Chess.NUMBER_OF_RANKS; i++){
            // Imprimindo os índices de cada linha.
            str += ((Integer)(Chess.NUMBER_OF_RANKS - i)).toString() + " - ";
 
            for (int j = 0; j < Chess.NUMBER_OF_FILES; j++){
                if (this.matrix[i][j] == null){
                    str += ".";
                }
                else{
                    str += matrix[i][j].toString();
                }
 
                str += " ";
            }
 
            str += "\n";
        }
 
        str += "\n    ";
 
        // Imprimindo os índices de cada coluna.
        for (char c = 'a'; c < 'a' + Chess.NUMBER_OF_FILES; c++){
            str += ((Character)c).toString() + " ";
        }
 
        str += "\n";
 
        return str;
    }
 
    @Override
    protected ChessBoard clone(){
        return new ChessBoard(this.ToFEN());
    }
}