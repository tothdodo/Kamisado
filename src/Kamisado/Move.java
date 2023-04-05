package Kamisado;

/// Move osztály

public class Move {
									/// Privát adattagok
	private Position start;			/// Kiindulási pozíció
	private Position destination;	/// Célpozíció
	private Game game;				/// Játék, amelyben történik a lépés
	
	/// Konstruktor
	/// @param start - indulási pozíció
	/// @param destination - célpozíció
	/// @param game - játék, amelyhez tartozik
	public Move(Position start, Position destination, Game game) {
		this.game = game;
		this.start = start;
		this.destination = destination;
	}
	
	/// Engedélyezett-e a lépés
	/// @return - engedélyezett-e, vagyis igaz/hamis érték
	public boolean isAllowed() {
		if(!destination.getFree() || isTowerBetweenThem() || (!isVertical() && !isDiagonal())) {
			return false;
		}
		else if(start.getTower().getType().equals("white") && isSouth() ) {
			return true;
		}
		else if(start.getTower().getType().equals("black") && isNorth()) {
			return true;
		}
		return false;
	}
	
	/// Van-e torony a két mezõ között
	/// @return - van-e torony köztuk (true/false)
	private boolean isTowerBetweenThem() {
		if(game.getActualPlayer().equals("white")) {
			if(isVertical()) {
				for(int i = destination.getX(); i < start.getX(); i++) {
					if(game.getButtons()[i][start.getY()].getPosition().getTower() != null) {
						return true;
					}
				}
			}
			else if(isDiagonal()) {
				int y = destination.getY();
				for(int i = destination.getX(); i < start.getX(); i++) {
					if(game.getButtons()[i][y].getPosition().getTower() != null) {
						return true;
					}
					if(start.getY() > destination.getY()) {
						y++;
					} else y--;
				}
			}
		}
		else if(game.getActualPlayer().equals("black")) {
			if(isVertical()) {
				for(int i = destination.getX(); i > start.getX(); i--) {
					if(game.getButtons()[i][start.getY()].getPosition().getTower() != null) {
						return true;
					}
				}
			}
			else if(isDiagonal()) {
				int y = destination.getY();
				for(int i = destination.getX(); i > start.getX(); i--) {
					if(game.getButtons()[i][y].getPosition().getTower() != null) {
						return true;					
					}
					if(start.getY() > destination.getY()) {
						y++;
					} else y--;
				}
			}
		}	
		return false;
	}
	
	/// Vertikális-e a lépés
	/// @return - vertikális-e (true/false)
	public boolean isVertical() {
		return destination.getY() == start.getY();
	}
	
	/// Felfele lépés-e
	/// @return - felfele-e a lépés (true/false)
	public boolean isNorth() {
		return destination.getX() - start.getX() > 0;
	}
	
	/// Lefele lépés-e
	/// @return - lefele-e a lépés (true/false)
	public boolean isSouth() {
		return destination.getX() - start.getX() < 0;
	}
	
	/// Átlós-e a lépés
	/// @return - átlós-e (true/false)
	public boolean isDiagonal() {
		return Math.abs(destination.getY() - start.getY()) == Math.abs(destination.getX() - start.getX());
	}
}
