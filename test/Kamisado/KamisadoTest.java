package Kamisado;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/// KamisadoTest osztály

public class KamisadoTest {
							/// Privát adattagok
	private Game game;		/// Játék, amin végezzük a teszteket
	private GameMenu menu;	/// Menü, amin végezzük a teszteket
	
	/// Minden teszt elõtt létrehozunk egy menüt, és egy hozzá tartozó játékos játékos elleni játékot
	@Before
	public void setGame() {
		menu = new GameMenu();
		game = new Game(menu, false);
	}
	
	/// Teszteljük, hogy a játék elején a fekete játékos következik-e
	@Test
	public void testActualPlayerAtTheBeginning() {
		Assert.assertEquals(game.getActualPlayer(), "black");
	}
	
	/// Teszteljük, hogy a játék elején az elsõ lépésnél tartunk-e
	@Test
	public void testIsFirstAtTheBeginning() {
		Assert.assertEquals(game.isFirst(), true);
	}
	
	/// Teszteljük, hogy a játék elején vége van-e a játéknak
	@Test
	public void testIsGameOverAtTheBeginning() {
		Assert.assertEquals(game.isOver(), false);
	}
	
	/// Teszteljük, hogy egy lépés megtétele után már a fehér játékos-e a következõ
	@Test
	public void testActualPlayerAfterOneMove() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.getActualPlayer(), "white");
	}
	
	/// Teszteljük, hogy egy lépés megtétele után már nem az elsõ körnél tartunk
	@Test
	public void testIsFirstAfterOneMove() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.isFirst(), false);
	}
	
	/// Teszteljük, hogy egy lépés megtétele után még mindig nincs vége a játéknak
	@Test
	public void testIsGameOverAfterOneMove() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.isOver(), false);
	}
	
	/// Teszteljük, hogy két lépés megtétele után ismét a fekete játékos következik-e
	@Test
	public void testActualPlayerAfterTwoMoves() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.getActualPlayer(), "black");
	}
	
	/// Teszteljük, hogy a játék végéig folyamatosan lépve eljutunk a játék végéig
	@Test
	public void testMoveUntilEndOfGame() {
		game.getButtons()[0][0].firstBrightening();
		while(!game.isOver()) {
			game.getBrButton().get(0).move();
		}
		Assert.assertEquals(game.isOver(), true);
	}
	
	/// Teszteljük, hogy a játék mentése után kiírta-e a szövegdobozba a megfelelõ üzenetet a program
	@Test
	public void testSaveGame() {
		menu.saveGame();
		Assert.assertEquals(game.getText(), "Game saved.");
	}
	
	/// Teszteljük, hogy egy lépés, majd a játék mentése és betöltése után továbbra is a fehér játékos következik
	@Test
	public void testLoadGame() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		menu.saveGame();
		menu.loadGame();
		Assert.assertEquals(game.getActualPlayer(), "white");
	}
	
	/// Teszteljük, hogy egy átlós lépés valóban szabályos-e
	@Test
	public void testMoveAllowing1() {
		Position p1 = game.getButtons()[0][0].getPosition();
		Position p2 = game.getButtons()[3][3].getPosition();
		Move move = new Move(p1, p2, game);
		Assert.assertEquals(move.isAllowed(), true);
	}
	
	/// Teszteljük, hogy egy se nem átlós, se nem vertikális lépés valóban szabálytalan-e
	@Test
	public void testMoveAllowing2() {
		Position p1 = game.getButtons()[0][0].getPosition();
		Position p2 = game.getButtons()[4][3].getPosition();
		Move move = new Move(p1, p2, game);
		Assert.assertEquals(move.isAllowed(), false);
	}
	
	/// Teszteljük, hogy egy torony, amely az alapvonalon került elhelyezésre valóban játékvégét okoz-e
	@Test
	public void testIsEndGame() {
		Tower tower = new Tower("white", Color.BLUE, new Position(7,7));
		game.endGame(tower);
		Assert.assertEquals(game.isOver(), true);
	}
	
	/// Teszteljük, hogy egy gomb "csillogóvá" tétele, valóban azzá tette, és lépési opcióként szerepel
	@Test
	public void testButtonBrightened() {
		Button button = new Button(new Position(2, 5), game, Color.BLUE);
		button.bright();
		Assert.assertEquals(button.isBrightened(), true);
	}
}
