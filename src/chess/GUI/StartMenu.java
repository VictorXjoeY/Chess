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

public class StartMenu extends JFrame{
	public final static Dimension OUTER_FRAME_DIMENSION = new Dimension(300,300);
	private final Color lightColor = Color.decode("#FFFACD");
	private final Color darkColor = Color.decode("#593E1A");

	private static String choice; 
	
	public StartMenu(){
		super("Xadreix tela inicial");
		this.choice = null;
		this.setSize(OUTER_FRAME_DIMENSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel(new GridLayout(4,1));
		MenuButton mButton;

		MenuButton button;
		button = new MenuButton(this, "Single Player");
		menuPanel.add(button);
		button = new MenuButton(this, "Multi Player");
		menuPanel.add(button);
		button = new MenuButton(this, "Load Game");
		menuPanel.add(button);
		button = new MenuButton(this, "IA vs AI");
		menuPanel.add(button);


		this.add(menuPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public String getChoice(){
		String aux = null;

		do{
			aux = StartMenu.choice;
			try{
				Thread.sleep(4);
			}catch(InterruptedException e){;}
		}while(aux == null);

		StartMenu.choice = null;
		
		return aux;
	}

	private class MenuButton extends JButton{
		final private String label;
		final private StartMenu myWindow;
		
		public MenuButton(StartMenu myWindow, String label){
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
				StartMenu.choice = this.myButton.label;
				System.out.println("StartMenu escolhi: " + StartMenu.choice);
				this.myButton.myWindow.setVisible(false);
			}
		}
	}
}