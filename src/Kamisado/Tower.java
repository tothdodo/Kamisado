package Kamisado;

import java.awt.Color;
import java.io.Serializable;

/// Tower oszt�ly

public class Tower implements Serializable{
														/// Priv�t adattagok
	private static final long serialVersionUID = 1L;	/// Szerializ�l�s verzi�sz�ma
	
	private String type;		/// A torony feh�r/fekete t�pusa
	private Position actual;	/// A torony poz�ci�ja
	private Color color;		/// A torony sz�ne
	
	/// Konstruktor
	/// @param type - a feh�r/fekete t�pus
	/// @param color - a sz�n
	/// @param pos - a poz�ci�
	public Tower(String type, Color color, Position pos) {
		this.type = type;
		this.color = color;
		this.actual = pos;
		pos.setFree(false);
		pos.setTower(this);
	}
	
	/// Sz�n getter
	/// @return - a sz�ne
	public Color getColor() {
		return color;
	}
	
	/// T�pus getter
	/// @return - a v�ltoz� feh�r/fekete �rt�ke
	public String getType() {
		return type;
	}
	
	/// Poz�ci� getter
	/// @return - a poz�ci�ja
	public Position getPosition() {
		return actual;
	}
	
	/// Poz�ci� setter
	/// @param position - az be�ll�tand� poz�ci�
	public void setPosition(Position position) {
		actual = position;	
	}
}
