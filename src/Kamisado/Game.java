package Kamisado;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.*;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/// Game oszt�ly

public class Game extends JFrame implements Serializable{
														/// Priv�t adattagok
	private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
	
	private GameMenu menu;							/// J�t�khoz tartoz� men�
	private Button buttons[][] = new Button[8][8];	/// A j�t�k 8x8 gombja
	private Color button_colors[][];				/// A 64 gomb sz�nei
	private Color prev_color;						/// El�z� sz�n, am� alapj�n l�pnie kell a k�vetkez� j�t�kosnak
	private Button prev_button;						/// El�z� gomb, ahol a j�t�kos l�p�s ut�n hagyja a torny�t
	private Button next_button;						/// K�vetkez� j�t�koshoz tartoz� mez�, amelyen l�v� toronnyal kell l�pnie
	private String actual_player;					/// Aktu�lis j�t�kos
	private String last_moved;						/// El�z� j�t�kos
	private JTextField text;						/// Sz�vegdoboz a j�t�k alatt
	private boolean first_move;						/// �rt�ke, hogy az els� l�p�sn�l tartunk-e
	private boolean with_AI;						/// �rt�ke, hogy AI elleni-e a j�t�k 
	private boolean isGameOver;						/// �rt�ke, hogy v�ge van-e a j�t�knak
	
	private List<Position> positions = new ArrayList<Position>();		/// J�t�khoz tartoz� 64 poz�ci� list�ja
	private List<Tower> towers = new ArrayList<Tower>();				/// J�t�khoz tartoz� 16 torony list�ja
	private List<Button> brightened_buttons = new ArrayList<Button>();	/// Aktu�lis l�p�si opci�t jelz�, ikonnal el�tott gombok
	private Set<Button> stuck_buttons = new HashSet<Button>();			/// Azon gombok, amelyekr�l a torony nem tud l�pni,
																		/// patthelyzetn�l fontos
	/// Konstruktor 
	///	@param menu - J�t�khoz tartoz� men�
	/// @param with_AI - AI-jal vagy an�lk�l van a j�t�k
	public Game(GameMenu menu, boolean with_AI) {
		this.menu = menu;
		this.with_AI = with_AI;
		actual_player = "black";
		first_move = true;
		isGameOver = false;
		if(menu != null) {
			menu.setGame(this);
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JPanel upper_panel = new JPanel();
		upper_panel.setLayout(new GridLayout(8,8));
		colorizeButtons();
				
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				Position pos = new Position(i, j);
				positions.add(pos);
				int k = 0;
				if(i == 7) {
					k += 8;
				}
				Button b = new Button(pos, this, button_colors[i][j]);
				
				if(i == 0 || i == 7) {
					Icon icon = new ImageIcon("src\\img\\" + tower_colors[j + k] + ".png");
					Image img = ((ImageIcon) icon).getImage();  
					Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
					icon = new ImageIcon(newimg);
					
					b = new Button(pos, this, button_colors[i][j], icon);
					Tower t;
					if(i == 0) {
						t = new Tower("black", button_colors[i][j], pos);
					} else {
						t = new Tower("white", button_colors[i][j], pos);
					}
					towers.add(t);
				}
				
				b.setBackground(button_colors[i][j]);
				b.setPreferredSize(new Dimension(60, 60));
				upper_panel.add(b);
				buttons[i][j] = b;
			}
		}
		JPanel central_panel = new JPanel();		
		central_panel.setLayout(new FlowLayout());
		
		JButton back_to_menu = new JButton("Back to menu");
		back_to_menu.addActionListener(new BackToMenuListener());
		
		JButton save_game = new JButton("Save game");
		save_game.addActionListener(new SaveGameListener());
		
		central_panel.add(back_to_menu);
		central_panel.add(save_game);
		
		JPanel lower_panel = new JPanel();
		lower_panel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 5));
		text = new JTextField();
		Font newTextFieldFont=new Font(text.getFont().getName(),text.getFont().getStyle(), 30);
		text.setFont(newTextFieldFont);
		text.setEditable(false);
		lower_panel.add(text);
		
		add(upper_panel, BorderLayout.NORTH);
		add(central_panel, BorderLayout.CENTER);
		add(lower_panel, BorderLayout.SOUTH);
		setLocation(700,200);
		pack();
	}
	
	/// ActionListener
	/// J�t�kb�l visszat�r�nk vele a men�be
	public class BackToMenuListener implements ActionListener, Serializable{
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			menu.setVisible(true);
		}
	}
	
	/// ActionListener
	/// J�t�k alatt elmentj�k vele az adott fel�ll�st
	public class SaveGameListener implements ActionListener, Serializable{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			menu.saveGame();
		}
	}
	
	/// Egy AI l�p�sn�l megvizsg�lja, hogy van-e gy�ztes l�p�se a g�pnek
	/// @return - Ha van, akkor a gy�ztes l�p�s gombja, ha nincs, akkor null
	public Button isWinnable() {
		for(Button b : brightened_buttons) {
			if(b.getPosition().getX() == 0) {
				return b;
			}
		}
		return null;
	}
	
	/// Elt�nteti a l�p�si opci�k jelz� ikont a gombokr�l	
	public void deBrigthenButtons() {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(buttons[i][j].isBrightened()) {
					buttons[i][j].setBrightened(false);
					buttons[i][j].setIcon(null);
				}
			}
		}
	}
	
	/// Megmutatja a l�p�si opci�kat, vagyis egy jelz� ikont tesz ezen gombokra
	/// Ha nincs l�p�si opci�, akkor a j�t�kszab�lynak megfelel� m�don �tadja a l�p�st a k�vetkez� toronynak
	/// Ha ir�ny�tott k�r alakul ki (patthelyzet), akkor ki�rja a program, �s v�ge a j�t�knak, az utols� l�p� vesz�t
	/// Rekurzi�t haszn�lva ism�tl� f�ggv�nyh�v�s t�rt�nik, amely b�ziskrit�riuma az k�vetkez� gomb stuck_buttons-ben val� megl�te
	/// @param questionerButton - a "k�rdez� gomb", vagyis amelyik gomb torny�hoz keress�k a l�p�si opci�kat
	public void showMovingOptions(Button questionerButton) {
		brightened_buttons.removeAll(brightened_buttons);
		if(!first_move) {
			if(actual_player.equals("white")){
				actual_player = "black";
			}
			else actual_player = "white";
			questionerButton = nextTower(actual_player, prev_color);
		}
		boolean is_there_any_options = false;
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
					Move actual_move = new Move(questionerButton.getPosition(), buttons[i][j].getPosition(), this);
					if(actual_move.isAllowed()) {
						buttons[i][j].bright();
						brightened_buttons.add(buttons[i][j]);
						is_there_any_options = true;
						last_moved = actual_player;
				}
			}
		}
		if(is_there_any_options) {
			stuck_buttons.removeAll(stuck_buttons);
		} else {
			// Ir�ny�tott k�r vizsg�lata
			if(stuck_buttons.contains(questionerButton)) {
				text.setText("Stalemate. " + last_moved.toUpperCase() + " lost.");
				setIsOver(true);
				deBrigthenButtons();
				return;
			}
			stuck_buttons.add(questionerButton);
			prev_color = questionerButton.getColor();
			showMovingOptions(questionerButton);
		}
	}
	
	/// Megadja melyik a k�vetkez� gomb, amelynek a torny�val l�pni kell.
	/// @param act_player - soron k�vetkez� j�t�kos
	/// @param prev_color - el�z� sz�n, ami alapj�n most l�pni kell
	/// @return - a keresett gomb a j�t�kos �s a sz�n alapj�n
	public Button nextTower(String act_player, Color prev_color) {
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 8; j++) {
				if(buttons[i][j].getPosition().getTower() != null) {
					if(buttons[i][j].getPosition().getTower().getType().equals(act_player) &&
						buttons[i][j].getPosition().getTower().getColor().equals(prev_color)) {
						next_button = buttons[i][j];
						return buttons[i][j];
					}
				}
			}
		}
		return null;
	}
	
	/// Megvizsg�lja, hogy a l�p� torony megnyerte-e a j�t�kot, ha igen ki�rja, �s v�ge lesz, a gy�ztes az utols� l�p�
	/// @param t - l�p� torony
	public void endGame(Tower t) {
		if (t.getPosition().getX() == 0 || t.getPosition().getX() == 7) {
			text.setText("Winner: " + t.getType().toUpperCase());
			setIsOver(true);
			deBrigthenButtons();
		}
	}
	
	/// A j�t�k alatti sz�vegdoboznak �ll�t be sz�veget
	/// @param newtext - az �j sz�veg
	public void setText(String newtext) {
		text.setText(newtext);
	}
	
	/// A j�t�k alatti sz�vegdoboznak k�ri le a sz�veg�t
	/// @return - a sz�veg
	public String getText() {
		return text.getText();
	}
	
	/// El�z� sz�n setter
	/// @param color - az �j sz�n, amire be�ll�tjuk
	public void setPrevColor(Color color) {
		prev_color = color;		
	}
	
	/// Els� l�p�s-e getter
	/// @return - a v�ltoz� true/false �rt�ke
	public boolean isFirst() {
		return first_move;
	}
	
	/// Els� l�p�s-e setter
	/// @param b - a v�ltoz� �j true/false �rt�ke
	public void setFirst(boolean b) {
		first_move = b;
	}
	
	/// K�vetkez� gomb getter
	/// @return - a k�vetkez� gomb
	public Button getNextButton() {
		return next_button;
	}
	
	/// Aktu�lis j�t�kos getter
	/// @return - aktu�lis j�t�kos
	public String getActualPlayer() {
		return actual_player;
	}
	
	/// J�t�k gombjai getter
	/// @return - a j�t�k gombjai
	public Button[][] getButtons(){
		return buttons;
	}
	
	/// Az l�p�si opci�k�nt titul�lt gombok list�ja
	/// @return - a gombok list�ja
	public List<Button> getBrButton(){
		return brightened_buttons;
	}
	
	/// Az el�z� kattintott gomb setter
	/// @param b - a kattintott gomb
	public void setPreviousButton(Button b) {
		prev_button = b;
	}
	
	/// Az el�z� kattintott gomb getter
	/// @return - az el�z� kattintott gomb
	public Button getPreviousButton() {
		return prev_button;
	}
	
	/// Tartalmaz-e AI-t a j�t�k getter
	/// @return - with_AI true/false �rt�ke
	public boolean getAI() {
		return with_AI;
	}
	
	/// V�ge-e a j�t�knak getter
	/// @return - isGameOver true/false �rt�ke
	public boolean isOver() {
		return isGameOver;
	}
	
	/// V�ge-e a j�t�knak setter
	/// @param b - a be�ll�tand� true/false �rt�k
	public void setIsOver(boolean b) {
		isGameOver = b;
	}
	
	/// button_colors k�tdimenzi�s t�mbnek ad �rt�ket, a j�t�k le�r�sa szerinti sz�neket
	public void colorizeButtons(){
		Color[][] button_colors = {
			{ ORANGE,Color.RED, GREEN, PINK, YELLOW,Color.BLUE, PURPLE, BROWN },
			{ Color.BLUE, ORANGE, PINK, PURPLE,Color.RED, YELLOW, BROWN, GREEN },
			{ PURPLE, PINK, ORANGE,Color.BLUE, GREEN, BROWN, YELLOW, Color.RED },
			{ PINK, GREEN,Color.RED, ORANGE, BROWN, PURPLE,Color.BLUE, YELLOW },
			{ YELLOW,Color.BLUE, PURPLE, BROWN, ORANGE,Color.RED, GREEN, PINK },
			{ Color.RED, YELLOW, BROWN, GREEN,Color.BLUE, ORANGE, PINK, PURPLE },
			{ GREEN, BROWN, YELLOW,Color.RED, PURPLE, PINK, ORANGE, Color.BLUE },
			{ BROWN, PURPLE,Color.BLUE, YELLOW, PINK, GREEN,Color.RED, ORANGE }
		};
		this.button_colors = button_colors;
	}
	
	/// �jonnan defini�lt sz�nek a j�t�khoz
	public static final Color ORANGE = new Color(255,140,0);
	public static final Color YELLOW = new Color(255,215,0);
	public static final Color PINK = new Color(255,153,204);
	public static final Color PURPLE = new Color(153,0,153);
	public static final Color GREEN = new Color(0,153,76);
	public static final Color BROWN = new Color(139,69,19);
	
	/// A tornyok k�pk�nt val� beolvas�s�hoz haszn�lt t�mb, amely a konstruktorban t�rt�nik
	private String[] tower_colors = {"ORANGE_BLACK", "RED_BLACK", "GREEN_BLACK", "PINK_BLACK",
									"YELLOW_BLACK", "BLUE_BLACK", "PURPLE_BLACK", "BROWN_BLACK",
									"BROWN_WHITE", "PURPLE_WHITE", "BLUE_WHITE", "YELLOW_WHITE",
									"PINK_WHITE", "GREEN_WHITE", "RED_WHITE", "ORANGE_WHITE"};
}
