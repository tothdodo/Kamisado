package Kamisado;

/// Move oszt�ly

public class Move {
									/// Priv�t adattagok
	private Position start;			/// Kiindul�si poz�ci�
	private Position destination;	/// C�lpoz�ci�
	private Game game;				/// J�t�k, amelyben t�rt�nik a l�p�s
	
	/// Konstruktor
	/// @param start - indul�si poz�ci�
	/// @param destination - c�lpoz�ci�
	/// @param game - j�t�k, amelyhez tartozik
	public Move(Position start, Position destination, Game game) {
		this.game = game;
		this.start = start;
		this.destination = destination;
	}
	
	/// Enged�lyezett-e a l�p�s
	/// @return - enged�lyezett-e, vagyis igaz/hamis �rt�k
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
	
	/// Van-e torony a k�t mez� k�z�tt
	/// @return - van-e torony k�ztuk (true/false)
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
	
	/// Vertik�lis-e a l�p�s
	/// @return - vertik�lis-e (true/false)
	public boolean isVertical() {
		return destination.getY() == start.getY();
	}
	
	/// Felfele l�p�s-e
	/// @return - felfele-e a l�p�s (true/false)
	public boolean isNorth() {
		return destination.getX() - start.getX() > 0;
	}
	
	/// Lefele l�p�s-e
	/// @return - lefele-e a l�p�s (true/false)
	public boolean isSouth() {
		return destination.getX() - start.getX() < 0;
	}
	
	/// �tl�s-e a l�p�s
	/// @return - �tl�s-e (true/false)
	public boolean isDiagonal() {
		return Math.abs(destination.getY() - start.getY()) == Math.abs(destination.getX() - start.getX());
	}
}
