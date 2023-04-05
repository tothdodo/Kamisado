package Kamisado;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/// Button osztály

public class Button extends JButton implements ActionListener, Serializable{
														/// Privát adattagok
	private static final long serialVersionUID = 1L;	/// Szerializálás verziószáma
	
	private Position pos;					/// A gomb pozíciója
	private Game game;						/// A gombhoz tartozó játék
	private Icon i;							/// A gomb ikonja
	private boolean brightened = false;		/// A gomb "csillogó"-e, avagy lépési opcióként van-e titulálva
	private Color color;					/// A gomb színe
	
	/// Konstruktor az ikon nélküli gomboknak
	/// @param pos - pozíció
	/// @param g - játék
	/// @param color - szín
	public Button(Position pos, Game g, Color color) {
		this(pos, g, color, null);	
	}
	
	/// Konstruktor az ikonnal rendelkezõ gomboknak
	/// @param pos - pozíció
	/// @param g - játék
	/// @param color - szín
	/// @param i - ikon
	public Button(Position pos, Game g, Color color, Icon i) {
		setIcon(i);
		this.i = i;
		this.game = g;
		this.pos = pos;
		this.color = color;
		pos.setButton(this);
		addActionListener(this);
	}
	
	/// Button gombon való kattintás vezérlése
	/// Külön választott egységek, vagyis lépési opció-e, elsõ lépésnél tartunk-e,
	/// vége-e a játéknak, van-e AI a játékban, illetve, hogy melyik játékos következik 
	@Override
	public void actionPerformed(ActionEvent e) {
		if(!pos.getFree() && game.isFirst() && pos.getTower().getType().equals(game.getActualPlayer())) {
			firstBrightening();
		} else if(brightened) {
			move();
		}
		if(game.getAI() && game.getActualPlayer().equals("white") && !game.isOver()) {
			Button chosen;
			if(game.isWinnable() != null) {
				chosen = game.isWinnable();
			} else {
				Random rand = new Random();
				int a = rand.nextInt(game.getBrButton().size());
				chosen = game.getBrButton().get(a);
			}
			chosen.move();
		}
	}
	
	/// Az elsõ lépésnél való torony választásnál elõször eltünteti a többi lépési opciót, majd megmutatja az adott toronyhoz,
	/// ezután beállítja elõzõ megnyomott gombnak a torony gombját
	public void firstBrightening() {
		game.deBrigthenButtons();
		game.showMovingOptions(this);
		game.setPreviousButton(this);
	}
	
	/// Maga a léptetés, átadják egymást közt a gombok az ikont és a tornyot, minden esetben vizsgálja, hogy vége-e a játéknak
	public void move() {
		Tower tower = null;
		Icon icon = null;
		if(game.isFirst()) {
			tower = game.getPreviousButton().getPosition().getTower();
			icon = game.getPreviousButton().getIcon();
			game.getPreviousButton().getPosition().removeTower();
			game.setFirst(false);
		}else {
			tower = game.getNextButton().getPosition().getTower();
			icon = game.getNextButton().getIcon();
			game.getNextButton().getPosition().removeTower();
		}
		game.setPrevColor(pos.getButton().getColor());
		game.deBrigthenButtons();
		pos.accept(tower, icon);
		
		game.showMovingOptions(this);
		game.setPreviousButton(this);
		game.endGame(getPosition().getTower());
	}
	
	/// Lépési opcióként titulálja a gombot, vagyis ad neki egy ikont
	public void bright() {
		Icon icon = new ImageIcon("src\\img\\glowCircle.png");
		Image img = ((ImageIcon) icon).getImage();  
		Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		i = icon;
		setIcon(i);
		setBrightened(true);
	}
	
	/// "Csillogó"-e a gomb setter
	/// @param b - a változó true/false értéke
	public void setBrightened(boolean b) {
		brightened = b;
		if(!b) {
			this.setIcon(null);
			i = null;
		}
	}
	
	/// A gomb színe getter
	/// @return - a szín
	public Color getColor() {
		return color;
	}
	
	/// Gomb csillogó-e
	/// @return - a változó true/false értéke
	public boolean isBrightened() {
		return brightened;
	}
	
	/// A gomb pozíciója getter
	/// @return - a pozíció
	public Position getPosition() {
		return pos;
	}
}
