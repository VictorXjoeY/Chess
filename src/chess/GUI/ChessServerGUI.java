package chess.GUI;


import java.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class ChessServerGUI extends JFrame{
	public final static Dimension OUTER_FRAME_DIMENSION = new Dimension(300,150);
	private final Color lightColor = Color.decode("#FFFACD");

	private IntegerJTextField textFild;

	private static String choice;

	private final MenuButton iniButton;
	private final MenuButton endButton;
	
	public ChessServerGUI(){
		super("Server IP: " + IP.get());
		this.choice = null;
		this.setSize(OUTER_FRAME_DIMENSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel(new GridLayout(3,1));

		//
		this.textFild = new IntegerJTextField("8000");
		JPanel port = new JPanel(new GridLayout(1,2));
		port.add(new JLabel("Porta: "));
		port.add(textFild);
		menuPanel.add(port);	

		this.iniButton = new MenuButton(this, "Iniciar");
		menuPanel.add(this.iniButton);
		this.endButton = new MenuButton(this, "Fechar");
		menuPanel.add(this.endButton);

		this.add(menuPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public String getPort(){
		return this.textFild.getText();
	}

	public String getChoice(){
		String aux = null;

		do{
			aux = ChessServerGUI.choice;
			try{
				Thread.sleep(4);
			}catch(InterruptedException e){;}
		}while(aux == null);

		ChessServerGUI.choice = null;
		
		return aux;
	}

	public void setIniButtonGreen(){
		this.iniButton.setBackground(Color.GREEN);
		this.endButton.setBackground(lightColor);
	}
	public void setIniButtonRed(){
		this.iniButton.setBackground(Color.RED);
		this.endButton.setBackground(lightColor);
	}
	public void setEndButtonGreen(){
		this.iniButton.setBackground(lightColor);
		this.endButton.setBackground(Color.GREEN);
	}
	

	private class MenuButton extends JButton{
		final private String label;
		final private ChessServerGUI myWindow;
		
		public MenuButton(ChessServerGUI myWindow, String label){
			super(label);
			this.label = label;
			this.myWindow = myWindow;
			this.setBackground(lightColor);

			this.addActionListener(new MenuButtonListener(this));

			validate();
		}

		private class MenuButtonListener implements ActionListener{
			
			final private MenuButton myButton;

			public MenuButtonListener(MenuButton myButton){
				this.myButton = myButton;
			}

			public void actionPerformed(ActionEvent e){
				ChessServerGUI.choice = this.myButton.label;
				System.out.println("ChessServerGUI escolhi: " + ChessServerGUI.choice);
			}
		}
	}
}