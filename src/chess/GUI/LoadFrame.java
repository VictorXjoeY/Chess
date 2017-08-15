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

public class LoadFrame extends JFrame{
	public final static Dimension OUTER_FRAME_DIMENSION = new Dimension(500,150);
	private final Color lightColor = Color.decode("#FFFACD");

	private JTextField textFild;

	private static String choice;

	private final MenuButton iniButton;
	
	public LoadFrame(){
		super("Janela de Load");
		this.choice = null;
		this.setSize(OUTER_FRAME_DIMENSION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		this.setLayout(new BorderLayout());
		
		JPanel menuPanel = new JPanel(new GridLayout(2,1));

		//
		this.textFild = new JTextField("Digite o nome do save aqui");
		JPanel panel = new JPanel(new GridLayout(1,2));
		panel.add(new JLabel("Save name:"));
		panel.add(textFild);
		menuPanel.add(panel);	

		this.iniButton = new MenuButton(this, "Iniciar");
		menuPanel.add(this.iniButton);


		this.add(menuPanel, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public String getText(){
		System.out.println("olha o text que cosnegui debug: " + this.textFild.getText());
		return this.textFild.getText();
	}

	public void hi(){
		this.setVisible(false);
	}

	public void setIniButtonRed(){
		this.iniButton.setBackground(Color.RED);
	}

	public String getChoice(){
		String aux = null;

		do{
			aux = LoadFrame.choice;
			try{
				Thread.sleep(4);
			}catch(InterruptedException e){;}
		}while(aux == null);

		LoadFrame.choice = null;
		
		return aux;
	}

	private class MenuButton extends JButton{
		final private String label;
		final private LoadFrame myWindow;
		
		public MenuButton(LoadFrame myWindow, String label){
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
				LoadFrame.choice = this.myButton.label;
			}
		}
	}
}