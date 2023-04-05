package Kamisado;

import java.awt.Color;
import java.io.Serializable;

/// Tower osztály

public class Tower implements Serializable{
														/// Privát adattagok
	private static final long serialVersionUID = 1L;	/// Szerializálás verziószáma
	
	private String type;		/// A torony fehér/fekete típusa
	private Position actual;	/// A torony pozíciója
	private Color color;		/// A torony színe
	
	/// Konstruktor
	/// @param type - a fehér/fekete típus
	/// @param color - a szín
	/// @param pos - a pozíció
	public Tower(String type, Color color, Position pos) {
		this.type = type;
		this.color = color;
		this.actual = pos;
		pos.setFree(false);
		pos.setTower(this);
	}
	
	/// Szín getter
	/// @return - a színe
	public Color getColor() {
		return color;
	}
	
	/// Típus getter
	/// @return - a változó fehér/fekete értéke
	public String getType() {
		return type;
	}
	
	/// Pozíció getter
	/// @return - a pozíciója
	public Position getPosition() {
		return actual;
	}
	
	/// Pozíció setter
	/// @param position - az beállítandó pozíció
	public void setPosition(Position position) {
		actual = position;	
	}
}
