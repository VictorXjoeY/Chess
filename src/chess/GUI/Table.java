package chess.GUI;

import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class Table{
	private static String movement;
	private static String choice;

	private final JFrame chessFrame;
	private final BoardPanel boardPanel;

	private TileButton sourceTile;

	public final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
	public final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
	public final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);
	public final static Dimension END_FRAME_DIMENSION = new Dimension(300,100);

	final static String pieceIconPath = "art/";

	private static final Color lightColor = Color.decode("#FFFACD");
	private static final Color darkColor = Color.decode("#593E1A");

	private int[] move = null;

	private static PromotionWindow proWin;

	private boolean isVisible;

	public Table(boolean isVisible){
		this.chessFrame = new JFrame("Xadreix versão 2.4.3");
		
		this.chessFrame.setSize(OUTER_FRAME_DIMENSION);
		this.chessFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Board
		this.chessFrame.setLayout(new BorderLayout());

		this.boardPanel = new BoardPanel();
		this.chessFrame.add(this.boardPanel, BorderLayout.CENTER);

		this.isVisible = isVisible;
		this.chessFrame.setVisible(isVisible);
		System.out.println("Criei a table !! ");
		Table.proWin = new PromotionWindow();
	}

	public void endGame(String mensage){
		System.out.println("Fim de jogo!");
		this.chessFrame.setVisible(false);
		if(this.isVisible){
			JFrame endFrame = new JFrame("Fim de Jogo!");
			endFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JLabel label =  new JLabel(mensage);
			label.setBackground(lightColor);
			endFrame.add(label);
			endFrame.setSize(END_FRAME_DIMENSION);
			endFrame.setVisible(true);
		}
	}

	public String getMovement(){
		String aux = null;

		do{
			aux = Table.movement;
			try{
				Thread.sleep(4);
			}catch(InterruptedException e){;}
		}while(aux == null);

		this.clearMoves();
		Table.movement = null;
		
		return aux;
	}

	public String getPromotion(){
		String aux = null;
		Table.proWin.setVisible(true);
		do{
			aux = Table.choice;
			try{
				Thread.sleep(4);
			}catch(InterruptedException e){;}
		}while(aux == null);

		Table.choice = null;
		
		return aux;
	}

	public void showMoves(int[] move){
		if(move == null)return;
		this.move = move;
		int i;
		if(this.sourceTile != null)this.sourceTile.setBackground(Color.BLUE);
		for(i = 0; i < move.length ; i++){
			this.boardPanel.tileList.get(move[i]).setBackground(Color.BLUE);
		}
	}

	public void clearMoves(){
		int i;

		// devolvendo a cor da tile origem
		if(this.sourceTile == null)return;
		this.sourceTile.setBackground(Table.tileColor(this.sourceTile.tileID));
		
		if (this.move != null){
			for(i = 0; i < this.move.length ; i++){
				// Colorindo de volta o a tile para sua cor original
				TileButton aux = this.boardPanel.tileList.get(this.move[i]);
				
				aux.setBackground(Table.tileColor(aux.tileID));
				
			}
		}

		Table.movement = null;
		this.sourceTile = null;
	}

	private static Color tileColor(int id){
		if((id/8) % 2 == 0){
			return id % 2 == 0 ? lightColor : darkColor;
		}else{
			return id % 2 != 0 ? lightColor : darkColor;
		}
	}

	
	private class BoardPanel extends JPanel{
		final List<TileButton> tileList;

		BoardPanel(){
			super(new GridLayout(8,8));
			this.tileList = new ArrayList<>();
			int i;
			for(i = 0; i < 8 * 8; i++){
				TileButton tilePanel = new TileButton(this, i);
				this.tileList.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_PANEL_DIMENSION);
			validate();
		}
	}

	private class TileButton extends JButton{
		public final int tileID;

		TileButton(final BoardPanel boardPanel, final int tileID){
			this.tileID = tileID;
			setPreferredSize(TILE_PANEL_DIMENSION);
			this.setBackground(Table.tileColor(this.tileID));
			addActionListener(new TileButtonListener(this));

			validate();
		}

		private class TileButtonListener implements ActionListener{
			TileButton myButton;
			TileButtonListener(final TileButton myButton){
				super();
				this.myButton = myButton;
			}

			@Override
			public synchronized void actionPerformed(ActionEvent e){
				System.out.println("cliquei no " + this.myButton.tileID);
				
				// Salvando a tile de origem
				if(Table.this.sourceTile == null)Table.this.sourceTile = this.myButton;
				Table.movement = "" + this.myButton.tileID;
			}
		}
	}

	private class PromotionWindow extends JFrame{
		public final Dimension PROMO_WINDOW_DIMENSION = new Dimension(200,200);
		
		public PromotionWindow(){
			super("Escolha a peça que sera promovida");
			Table.choice = null;
		
			this.setSize(PROMO_WINDOW_DIMENSION);
			this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

			//Board
			this.setLayout(new BorderLayout());

			JPanel promoPanel = new JPanel(new GridLayout(2,2));
			PromotionButtom promoButtom;

			promoButtom = new PromotionButtom('Q', this);
			promoPanel.add(promoButtom);
			promoButtom = new PromotionButtom('N', this);
			promoPanel.add(promoButtom);
			promoButtom = new PromotionButtom('B', this);
			promoPanel.add(promoButtom);
			promoButtom = new PromotionButtom('R', this);
			promoPanel.add(promoButtom);

			this.add(promoPanel, BorderLayout.CENTER);
		}

		private class PromotionButtom extends JButton{
			final private char typePiece;
			final private PromotionWindow myWindow;
			
			public PromotionButtom(char typePiece, PromotionWindow myWindow){
				super();
				this.typePiece = typePiece;
				this.myWindow = myWindow;
				this.setBackground(lightColor);
				this.setIcon(getImage(this.typePiece));

				this.addActionListener(new PromotionButtomListener(this));

				validate();
			}

			private class PromotionButtomListener implements ActionListener{

				final private PromotionButtom myButton;

				public PromotionButtomListener(PromotionButtom myButton){
					this.myButton = myButton;
				}

				public void actionPerformed(ActionEvent e){
					Table.choice = "" + this.myButton.typePiece;
					System.out.println("escolhi promover para " + Table.choice);
					this.myButton.myWindow.setVisible(false);
				}
			}
		}
	}

	public static ImageIcon getImage(char c){
		BufferedImage image = null;

		try{
			if(c == 'K')image = ImageIO.read(new File(Table.pieceIconPath + "WK" + ".png"));
			else if(c == 'Q')image = ImageIO.read(new File(Table.pieceIconPath + "WQ" + ".png"));
			else if(c == 'N')image = ImageIO.read(new File(Table.pieceIconPath + "WH" + ".png"));
			else if(c == 'B')image = ImageIO.read(new File(Table.pieceIconPath + "WB" + ".png"));
			else if(c == 'R')image = ImageIO.read(new File(Table.pieceIconPath + "WR" + ".png"));
			else if(c == 'P')image = ImageIO.read(new File(Table.pieceIconPath + "WP" + ".png"));
			else if(c == 'k')image = ImageIO.read(new File(Table.pieceIconPath + "BK" + ".png"));
			else if(c == 'q')image = ImageIO.read(new File(Table.pieceIconPath + "BQ" + ".png"));
			else if(c == 'n')image = ImageIO.read(new File(Table.pieceIconPath + "BH" + ".png"));
			else if(c == 'b')image = ImageIO.read(new File(Table.pieceIconPath + "BB" + ".png"));
			else if(c == 'r')image = ImageIO.read(new File(Table.pieceIconPath + "BR" + ".png"));
			else if(c == 'p')image = ImageIO.read(new File(Table.pieceIconPath + "BP" + ".png"));
			else image = ImageIO.read(new File(Table.pieceIconPath + "N" + ".png"));
		}catch(IOException e){
			e.printStackTrace();
			System.out.println("ERRO! no carregamento da imagem da pessa: " + c);
		}
		if(image != null)return new ImageIcon(image);
		return null;
	}

	public void updateTable(String pseudoFEN){
		System.out.println("atualizando o tabuleiro com o novo tabuleiro:");
		System.out.println(pseudoFEN);

		int i;
		for(i = 0; i < 8 * 8 ;i++){
			this.boardPanel.tileList.get(i).setIcon(this.getImage(pseudoFEN.charAt(i)));
		}
	}
}