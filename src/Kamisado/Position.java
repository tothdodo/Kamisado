package Kamisado;

import java.io.Serializable;

import javax.swing.Icon;

/// Position osztály

public class Position implements Serializable{
														/// Privát adattagok
	private static final long serialVersionUID = 1L;	/// Szerializálás verziószáma
	
	private int x;					/// X koordináta
	private int y;					/// Y koordináta
	private Button button;			/// Pozíción lévõ gomb
	private Tower tower;			/// Pozícióhoz tarozó torony
	private boolean free = true;	/// Szabad-e a pozíció
	
	/// Konstruktor
	/// @param x - x koordináta
	/// @param y - y koordináta
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/// Pozícióról torony eltüntetése
	public void removeTower() {
		button.setIcon(null);
		tower = null;
		free = true;
	}
	
	/// Pozícióhoz torony hozzáadása és megjelenítése
	/// @param tower - az új torony a pozíción
	/// @param i - az új ikon a pozíción
	public void accept(Tower tower, Icon i) {
		this.tower = tower;
		tower.setPosition(this);
		button.setIcon(i);
		free = false;
	}
	
	/// Pozícióhoz tartozó torony setter
	/// @param t - az új torony
	public void setTower(Tower t) {
		this.tower = t;
	}
	
	/// Pozícióhoz tartozó gomb setter
	/// @param b - az új gomb
	public void setButton(Button b) {
		this.button = b;
	}
	
	/// Pozíció szabad-e getter
	/// @return - a változó true/false értéke
	public boolean getFree() {
		return free;
	}
	
	/// Pozícióhoz szabad-e setter
	/// @param b - a változó true/false értéke
	public void setFree(boolean b) {
		free = b;
	}
	
	/// Pozícióhoz tartozó gomb getter
	/// @return - a gomb
	public Button getButton() {
		return button;
	}
	
	/// Pozícióhoz tartozó torony getter
	/// @return - a torony
	public Tower getTower() {
		return tower;
	}
	
	/// Pozícióhoz X koordinátája getter
	/// @return - x koordináta
	public int getX() {
		return x;
	}
	
	/// Pozícióhoz Y koordinátája getter
	/// @return - y koordináta
	public int getY() {
		return y;
	}
}
