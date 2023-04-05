package Kamisado;

import java.awt.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/// KamisadoTest oszt�ly

public class KamisadoTest {
							/// Priv�t adattagok
	private Game game;		/// J�t�k, amin v�gezz�k a teszteket
	private GameMenu menu;	/// Men�, amin v�gezz�k a teszteket
	
	/// Minden teszt el�tt l�trehozunk egy men�t, �s egy hozz� tartoz� j�t�kos j�t�kos elleni j�t�kot
	@Before
	public void setGame() {
		menu = new GameMenu();
		game = new Game(menu, false);
	}
	
	/// Tesztelj�k, hogy a j�t�k elej�n a fekete j�t�kos k�vetkezik-e
	@Test
	public void testActualPlayerAtTheBeginning() {
		Assert.assertEquals(game.getActualPlayer(), "black");
	}
	
	/// Tesztelj�k, hogy a j�t�k elej�n az els� l�p�sn�l tartunk-e
	@Test
	public void testIsFirstAtTheBeginning() {
		Assert.assertEquals(game.isFirst(), true);
	}
	
	/// Tesztelj�k, hogy a j�t�k elej�n v�ge van-e a j�t�knak
	@Test
	public void testIsGameOverAtTheBeginning() {
		Assert.assertEquals(game.isOver(), false);
	}
	
	/// Tesztelj�k, hogy egy l�p�s megt�tele ut�n m�r a feh�r j�t�kos-e a k�vetkez�
	@Test
	public void testActualPlayerAfterOneMove() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.getActualPlayer(), "white");
	}
	
	/// Tesztelj�k, hogy egy l�p�s megt�tele ut�n m�r nem az els� k�rn�l tartunk
	@Test
	public void testIsFirstAfterOneMove() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.isFirst(), false);
	}
	
	/// Tesztelj�k, hogy egy l�p�s megt�tele ut�n m�g mindig nincs v�ge a j�t�knak
	@Test
	public void testIsGameOverAfterOneMove() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.isOver(), false);
	}
	
	/// Tesztelj�k, hogy k�t l�p�s megt�tele ut�n ism�t a fekete j�t�kos k�vetkezik-e
	@Test
	public void testActualPlayerAfterTwoMoves() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		game.getBrButton().get(0).move();
		Assert.assertEquals(game.getActualPlayer(), "black");
	}
	
	/// Tesztelj�k, hogy a j�t�k v�g�ig folyamatosan l�pve eljutunk a j�t�k v�g�ig
	@Test
	public void testMoveUntilEndOfGame() {
		game.getButtons()[0][0].firstBrightening();
		while(!game.isOver()) {
			game.getBrButton().get(0).move();
		}
		Assert.assertEquals(game.isOver(), true);
	}
	
	/// Tesztelj�k, hogy a j�t�k ment�se ut�n ki�rta-e a sz�vegdobozba a megfelel� �zenetet a program
	@Test
	public void testSaveGame() {
		menu.saveGame();
		Assert.assertEquals(game.getText(), "Game saved.");
	}
	
	/// Tesztelj�k, hogy egy l�p�s, majd a j�t�k ment�se �s bet�lt�se ut�n tov�bbra is a feh�r j�t�kos k�vetkezik
	@Test
	public void testLoadGame() {
		game.getButtons()[0][0].firstBrightening();
		game.getBrButton().get(0).move();
		menu.saveGame();
		menu.loadGame();
		Assert.assertEquals(game.getActualPlayer(), "white");
	}
	
	/// Tesztelj�k, hogy egy �tl�s l�p�s val�ban szab�lyos-e
	@Test
	public void testMoveAllowing1() {
		Position p1 = game.getButtons()[0][0].getPosition();
		Position p2 = game.getButtons()[3][3].getPosition();
		Move move = new Move(p1, p2, game);
		Assert.assertEquals(move.isAllowed(), true);
	}
	
	/// Tesztelj�k, hogy egy se nem �tl�s, se nem vertik�lis l�p�s val�ban szab�lytalan-e
	@Test
	public void testMoveAllowing2() {
		Position p1 = game.getButtons()[0][0].getPosition();
		Position p2 = game.getButtons()[4][3].getPosition();
		Move move = new Move(p1, p2, game);
		Assert.assertEquals(move.isAllowed(), false);
	}
	
	/// Tesztelj�k, hogy egy torony, amely az alapvonalon ker�lt elhelyez�sre val�ban j�t�kv�g�t okoz-e
	@Test
	public void testIsEndGame() {
		Tower tower = new Tower("white", Color.BLUE, new Position(7,7));
		game.endGame(tower);
		Assert.assertEquals(game.isOver(), true);
	}
	
	/// Tesztelj�k, hogy egy gomb "csillog�v�" t�tele, val�ban azz� tette, �s l�p�si opci�k�nt szerepel
	@Test
	public void testButtonBrightened() {
		Button button = new Button(new Position(2, 5), game, Color.BLUE);
		button.bright();
		Assert.assertEquals(button.isBrightened(), true);
	}
}
