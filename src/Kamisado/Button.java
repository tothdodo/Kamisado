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

/// Button oszt�ly

public class Button extends JButton implements ActionListener, Serializable{
														/// Priv�t adattagok
	private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
	
	private Position pos;					/// A gomb poz�ci�ja
	private Game game;						/// A gombhoz tartoz� j�t�k
	private Icon i;							/// A gomb ikonja
	private boolean brightened = false;		/// A gomb "csillog�"-e, avagy l�p�si opci�k�nt van-e titul�lva
	private Color color;					/// A gomb sz�ne
	
	/// Konstruktor az ikon n�lk�li gomboknak
	/// @param pos - poz�ci�
	/// @param g - j�t�k
	/// @param color - sz�n
	public Button(Position pos, Game g, Color color) {
		this(pos, g, color, null);	
	}
	
	/// Konstruktor az ikonnal rendelkez� gomboknak
	/// @param pos - poz�ci�
	/// @param g - j�t�k
	/// @param color - sz�n
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
	
	/// Button gombon val� kattint�s vez�rl�se
	/// K�l�n v�lasztott egys�gek, vagyis l�p�si opci�-e, els� l�p�sn�l tartunk-e,
	/// v�ge-e a j�t�knak, van-e AI a j�t�kban, illetve, hogy melyik j�t�kos k�vetkezik 
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
	
	/// Az els� l�p�sn�l val� torony v�laszt�sn�l el�sz�r elt�nteti a t�bbi l�p�si opci�t, majd megmutatja az adott toronyhoz,
	/// ezut�n be�ll�tja el�z� megnyomott gombnak a torony gombj�t
	public void firstBrightening() {
		game.deBrigthenButtons();
		game.showMovingOptions(this);
		game.setPreviousButton(this);
	}
	
	/// Maga a l�ptet�s, �tadj�k egym�st k�zt a gombok az ikont �s a tornyot, minden esetben vizsg�lja, hogy v�ge-e a j�t�knak
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
	
	/// L�p�si opci�k�nt titul�lja a gombot, vagyis ad neki egy ikont
	public void bright() {
		Icon icon = new ImageIcon("src\\img\\glowCircle.png");
		Image img = ((ImageIcon) icon).getImage();  
		Image newimg = img.getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(newimg);

		i = icon;
		setIcon(i);
		setBrightened(true);
	}
	
	/// "Csillog�"-e a gomb setter
	/// @param b - a v�ltoz� true/false �rt�ke
	public void setBrightened(boolean b) {
		brightened = b;
		if(!b) {
			this.setIcon(null);
			i = null;
		}
	}
	
	/// A gomb sz�ne getter
	/// @return - a sz�n
	public Color getColor() {
		return color;
	}
	
	/// Gomb csillog�-e
	/// @return - a v�ltoz� true/false �rt�ke
	public boolean isBrightened() {
		return brightened;
	}
	
	/// A gomb poz�ci�ja getter
	/// @return - a poz�ci�
	public Position getPosition() {
		return pos;
	}
}
