package Kamisado;

import java.io.Serializable;

import javax.swing.Icon;

/// Position oszt�ly

public class Position implements Serializable{
														/// Priv�t adattagok
	private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
	
	private int x;					/// X koordin�ta
	private int y;					/// Y koordin�ta
	private Button button;			/// Poz�ci�n l�v� gomb
	private Tower tower;			/// Poz�ci�hoz taroz� torony
	private boolean free = true;	/// Szabad-e a poz�ci�
	
	/// Konstruktor
	/// @param x - x koordin�ta
	/// @param y - y koordin�ta
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/// Poz�ci�r�l torony elt�ntet�se
	public void removeTower() {
		button.setIcon(null);
		tower = null;
		free = true;
	}
	
	/// Poz�ci�hoz torony hozz�ad�sa �s megjelen�t�se
	/// @param tower - az �j torony a poz�ci�n
	/// @param i - az �j ikon a poz�ci�n
	public void accept(Tower tower, Icon i) {
		this.tower = tower;
		tower.setPosition(this);
		button.setIcon(i);
		free = false;
	}
	
	/// Poz�ci�hoz tartoz� torony setter
	/// @param t - az �j torony
	public void setTower(Tower t) {
		this.tower = t;
	}
	
	/// Poz�ci�hoz tartoz� gomb setter
	/// @param b - az �j gomb
	public void setButton(Button b) {
		this.button = b;
	}
	
	/// Poz�ci� szabad-e getter
	/// @return - a v�ltoz� true/false �rt�ke
	public boolean getFree() {
		return free;
	}
	
	/// Poz�ci�hoz szabad-e setter
	/// @param b - a v�ltoz� true/false �rt�ke
	public void setFree(boolean b) {
		free = b;
	}
	
	/// Poz�ci�hoz tartoz� gomb getter
	/// @return - a gomb
	public Button getButton() {
		return button;
	}
	
	/// Poz�ci�hoz tartoz� torony getter
	/// @return - a torony
	public Tower getTower() {
		return tower;
	}
	
	/// Poz�ci�hoz X koordin�t�ja getter
	/// @return - x koordin�ta
	public int getX() {
		return x;
	}
	
	/// Poz�ci�hoz Y koordin�t�ja getter
	/// @return - y koordin�ta
	public int getY() {
		return y;
	}
}
