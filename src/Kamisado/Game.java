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

/// Game osztály

public class Game extends JFrame implements Serializable{
														/// Privát adattagok
	private static final long serialVersionUID = 1L;	/// Szerializálás verziószáma
	
	private GameMenu menu;							/// Játékhoz tartozó menü
	private Button buttons[][] = new Button[8][8];	/// A játék 8x8 gombja
	private Color button_colors[][];				/// A 64 gomb színei
	private Color prev_color;						/// Elõzõ szín, amí alapján lépnie kell a következõ játékosnak
	private Button prev_button;						/// Elõzõ gomb, ahol a játékos lépés után hagyja a tornyát
	private Button next_button;						/// Következõ játékoshoz tartozó mezõ, amelyen lévõ toronnyal kell lépnie
	private String actual_player;					/// Aktuális játékos
	private String last_moved;						/// Elõzõ játékos
	private JTextField text;						/// Szövegdoboz a játék alatt
	private boolean first_move;						/// Értéke, hogy az elsõ lépésnél tartunk-e
	private boolean with_AI;						/// Értéke, hogy AI elleni-e a játék 
	private boolean isGameOver;						/// Értéke, hogy vége van-e a játéknak
	
	private List<Position> positions = new ArrayList<Position>();		/// Játékhoz tartozó 64 pozíció listája
	private List<Tower> towers = new ArrayList<Tower>();				/// Játékhoz tartozó 16 torony listája
	private List<Button> brightened_buttons = new ArrayList<Button>();	/// Aktuális lépési opciót jelzõ, ikonnal elátott gombok
	private Set<Button> stuck_buttons = new HashSet<Button>();			/// Azon gombok, amelyekrõl a torony nem tud lépni,
																		/// patthelyzetnél fontos
	/// Konstruktor 
	///	@param menu - Játékhoz tartozó menü
	/// @param with_AI - AI-jal vagy anélkül van a játék
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
	/// Játékból visszatérünk vele a menübe
	public class BackToMenuListener implements ActionListener, Serializable{
		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent e) {
			dispose();
			menu.setVisible(true);
		}
	}
	
	/// ActionListener
	/// Játék alatt elmentjük vele az adott felállást
	public class SaveGameListener implements ActionListener, Serializable{
		private static final long serialVersionUID = 1L;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			menu.saveGame();
		}
	}
	
	/// Egy AI lépésnél megvizsgálja, hogy van-e gyõztes lépése a gépnek
	/// @return - Ha van, akkor a gyõztes lépés gombja, ha nincs, akkor null
	public Button isWinnable() {
		for(Button b : brightened_buttons) {
			if(b.getPosition().getX() == 0) {
				return b;
			}
		}
		return null;
	}
	
	/// Eltünteti a lépési opciók jelzõ ikont a gombokról	
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
	
	/// Megmutatja a lépési opciókat, vagyis egy jelzõ ikont tesz ezen gombokra
	/// Ha nincs lépési opció, akkor a játékszabálynak megfelelõ módon átadja a lépést a következõ toronynak
	/// Ha irányított kör alakul ki (patthelyzet), akkor kiírja a program, és vége a játéknak, az utolsó lépõ veszít
	/// Rekurziót használva ismétlõ függvényhívás történik, amely báziskritériuma az következõ gomb stuck_buttons-ben való megléte
	/// @param questionerButton - a "kérdezõ gomb", vagyis amelyik gomb tornyához keressük a lépési opciókat
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
			// Irányított kör vizsgálata
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
	
	/// Megadja melyik a következõ gomb, amelynek a tornyával lépni kell.
	/// @param act_player - soron következõ játékos
	/// @param prev_color - elõzõ szín, ami alapján most lépni kell
	/// @return - a keresett gomb a játékos és a szín alapján
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
	
	/// Megvizsgálja, hogy a lépõ torony megnyerte-e a játékot, ha igen kiírja, és vége lesz, a gyõztes az utolsó lépõ
	/// @param t - lépõ torony
	public void endGame(Tower t) {
		if (t.getPosition().getX() == 0 || t.getPosition().getX() == 7) {
			text.setText("Winner: " + t.getType().toUpperCase());
			setIsOver(true);
			deBrigthenButtons();
		}
	}
	
	/// A játék alatti szövegdoboznak állít be szöveget
	/// @param newtext - az új szöveg
	public void setText(String newtext) {
		text.setText(newtext);
	}
	
	/// A játék alatti szövegdoboznak kéri le a szövegét
	/// @return - a szöveg
	public String getText() {
		return text.getText();
	}
	
	/// Elõzõ szín setter
	/// @param color - az új szín, amire beállítjuk
	public void setPrevColor(Color color) {
		prev_color = color;		
	}
	
	/// Elsõ lépés-e getter
	/// @return - a változó true/false értéke
	public boolean isFirst() {
		return first_move;
	}
	
	/// Elsõ lépés-e setter
	/// @param b - a változó új true/false értéke
	public void setFirst(boolean b) {
		first_move = b;
	}
	
	/// Következõ gomb getter
	/// @return - a következõ gomb
	public Button getNextButton() {
		return next_button;
	}
	
	/// Aktuális játékos getter
	/// @return - aktuális játékos
	public String getActualPlayer() {
		return actual_player;
	}
	
	/// Játék gombjai getter
	/// @return - a játék gombjai
	public Button[][] getButtons(){
		return buttons;
	}
	
	/// Az lépési opcióként titulált gombok listája
	/// @return - a gombok listája
	public List<Button> getBrButton(){
		return brightened_buttons;
	}
	
	/// Az elõzõ kattintott gomb setter
	/// @param b - a kattintott gomb
	public void setPreviousButton(Button b) {
		prev_button = b;
	}
	
	/// Az elõzõ kattintott gomb getter
	/// @return - az elõzõ kattintott gomb
	public Button getPreviousButton() {
		return prev_button;
	}
	
	/// Tartalmaz-e AI-t a játék getter
	/// @return - with_AI true/false értéke
	public boolean getAI() {
		return with_AI;
	}
	
	/// Vége-e a játéknak getter
	/// @return - isGameOver true/false értéke
	public boolean isOver() {
		return isGameOver;
	}
	
	/// Vége-e a játéknak setter
	/// @param b - a beállítandó true/false érték
	public void setIsOver(boolean b) {
		isGameOver = b;
	}
	
	/// button_colors kétdimenziós tömbnek ad értéket, a játék leírása szerinti színeket
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
	
	/// Újonnan definiált színek a játékhoz
	public static final Color ORANGE = new Color(255,140,0);
	public static final Color YELLOW = new Color(255,215,0);
	public static final Color PINK = new Color(255,153,204);
	public static final Color PURPLE = new Color(153,0,153);
	public static final Color GREEN = new Color(0,153,76);
	public static final Color BROWN = new Color(139,69,19);
	
	/// A tornyok képként való beolvasásához használt tömb, amely a konstruktorban történik
	private String[] tower_colors = {"ORANGE_BLACK", "RED_BLACK", "GREEN_BLACK", "PINK_BLACK",
									"YELLOW_BLACK", "BLUE_BLACK", "PURPLE_BLACK", "BROWN_BLACK",
									"BROWN_WHITE", "PURPLE_WHITE", "BLUE_WHITE", "YELLOW_WHITE",
									"PINK_WHITE", "GREEN_WHITE", "RED_WHITE", "ORANGE_WHITE"};
}
