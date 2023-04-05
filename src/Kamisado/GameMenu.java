package Kamisado;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

/// GameMenu oszt�ly

public class GameMenu extends JFrame{
														/// Priv�t adattagok
	private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
	
	private Game game;													/// Men�h�z tartoz� j�t�k
	private JButton pvp_button, pvai_button, load_button, exit_button;	/// Men�ben szerepl� 4 gomb
	private JLabel title_label;											/// Men�ben fel�l szerepl� c�m
	
	/// Konstruktor
	public GameMenu() {
		setSize(500, 350);
		
		Container main_panel = getContentPane();
		main_panel.setLayout(null);
		main_panel.setBackground(Color.ORANGE);
        
        title_label = new JLabel("KAMISADO");
        title_label.setFont(new Font("Chiller",Font.BOLD,50));
        title_label.setBounds(150, 30, 200, 50);
        main_panel.add(title_label);
		
        main_panel.add(pvp_button = new JButton("Player vs Player"));
		pvp_button.addActionListener(new NewGameListener());
		pvp_button.setBounds(170, 90, 145, 40);
		pvp_button.setBackground(Color.WHITE);
		
		main_panel.add(pvai_button = new JButton("Player vs AI"));
		pvai_button.addActionListener(new NewAIGameListener());
		pvai_button.setBounds(170, 140, 145, 40);
		pvai_button.setBackground(Color.WHITE);
		
		main_panel.add(load_button = new JButton("Load saved game"));
		load_button.addActionListener(new LoadGameListener());
		load_button.setBounds(170, 190, 145, 40);
		load_button.setBackground(Color.WHITE);
		
		main_panel.add(exit_button = new JButton("Exit game"));
		exit_button.addActionListener(new ExitListener());
		exit_button.setBounds(170, 240, 145, 40);
		exit_button.setBackground(Color.WHITE);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocation(700,300);
		setResizable(false);
	}
	
	/// ActionListener, amely az �j 2 j�t�kosmod� j�t�k ind�t�sn�l l�trehoz egy j�t�kot, l�that�v� teszi, a men�t pedig eldobja
	public class NewGameListener implements ActionListener, Serializable{
		
		private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			game = new Game(new GameMenu(), false);
			game.setVisible(true);
		}
	}
	
	/// ActionListener, amely j�t�kos az AI ellen j�t�kot hoz l�tre, amelyet l�that�v� tesz, a men�t pedig eldobja
	public class NewAIGameListener implements ActionListener, Serializable{
		
		private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
		
		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			game = new Game(new GameMenu(), true);
			game.setVisible(true);
		}
	}
	
	/// ActionListener, amely kattint�sn�l bet�lti a legut�bb elmentett j�t�kot, a men�t pedig eldobja
	public class LoadGameListener implements ActionListener, Serializable{
		
		private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
		
		@Override
		public void actionPerformed(ActionEvent e) {
			File temp = new File("kamisado.ser");
			if(temp.exists()) {
				loadGame();
				dispose();
				game.setVisible(true);
			} else {
				System.out.println("There is no previous game to load.");
			}
		}		
	}
	
	/// ActionListener, amely kattint�sn�l kil�p a programb�l
	public class ExitListener implements ActionListener, Serializable{
		
		private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/// Legut�bb elmentett j�t�k bet�lt�se kamisado.ser f�jlb�l
	public void loadGame() {
		try{
			FileInputStream f = new FileInputStream("kamisado.ser");
			ObjectInputStream in = new ObjectInputStream(f);	
			game = (Game) in.readObject();
			in.close();
		}catch(IOException e){
			System.out.println(e);
		}catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
		}
	}
	
	/// Aktu�lis j�t�k elment�se kamisado.ser n�ven
	public void saveGame() {
		try{
			FileOutputStream fos = new FileOutputStream("kamisado.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(game);
            game.setText("Game saved.");
            oos.close();
            fos.close();

		}catch(IOException e){
			System.out.println(e);
		}
	}
	
	/// Men�h�z tartoz� j�t�k setter
	/// @param game - a be�ll�tand� j�t�k
	public void setGame(Game game) {
		this.game = game;
	}
	
	public static void main(String[] args) {
		GameMenu menu = new GameMenu();
		menu.setVisible(true);
	}
}
