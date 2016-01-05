package org.droidfoot.dfsmess;
 

import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.MouseInfo;
import greenfoot.World;
import greenfoot.awt.Color;

public class Piece extends Actor {

	boolean isA;
	Space space = null;
	Space toSpace;
	int origWidth, origHeight;
	boolean scaled;

	Board board;
	Game game;
	Rules rules;
	ProgramThread programThread = null;

	static Turn lastTurn = null;

	Piece(boolean isA) {
		this.isA = isA;
		scaled = false;
		programThread = null;
	}

	Piece(Piece figur) {
		this.isA = figur.isA;
		scaled = false;
		programThread = null;

	}

	protected void addedToWorld(World world) {
		board = (Board) getWorld();
		game = board.getGame();
		rules = game.getRegeln();
	}

	protected Object clone() {
		return null;
	}

	public boolean isA() {
		return this.isA;
	}

	public void setSpace(Space space) {
		this.space = space;
	}

	public void act() {

		board = (Board) getWorld();
		game = board.getGame();
		rules = game.getRegeln();

		if (board.end) {
			return;
		}
		if (board.gameState == Board.PLAYER_CHOICE) {
			return;
		}
		if (board.gameState == Board.WAIT && programThread == null) {
			return;
		}
		Player currentPlayer = game.getAktuellerSpieler();
		Player otherPlayer = game.getOtherPlayer();
		if (!currentPlayer.isHuman() && !otherPlayer.isHuman()) {
			return; // wird durch Board gehandhabt
		}
		if (currentPlayer.isHuman()) {
			actHuman();
		} else {
			actProgram();
		}
	}

	public void actHuman() {
		Player currentPlayer = game.getAktuellerSpieler();
		Space[] zugFelder = rules.getZugFelder(space, currentPlayer);
		if (Greenfoot.mouseDragged(this)) {
			if (currentPlayer.isSpielerA() != this.isA) {
				return;
			}
			if (!scaled) {
				board.setFront(this);
				scaled = true;
				scale();
			}
			this.space.markCurrent();
			for (Space feld : zugFelder) {
				feld.markPossible();
			}
			MouseInfo info = Greenfoot.getMouseInfo();
			toSpace = board.getFeld(info.getY(), info.getX());
			if (toSpace != null) {
				setLocation(info.getX(), info.getY());
			}
			if (in(zugFelder, toSpace) || toSpace == space) {
				setNormalFinger();
				scale();
			} else {
				setSadFinger();
				scale();
			}
		} else if (Greenfoot.mouseDragEnded(this)) {
			if (currentPlayer.isSpielerA() != this.isA) {
				return;
			}
			this.getImage().scale(origWidth, origHeight);
			setNormalFinger();
			scaled = false;
			for (Space feld : zugFelder) {
				feld.unmarkPossible();
			}
			this.space.unmarkCurrent();
			if (in(zugFelder, toSpace)) {
				lastTurn = new Turn(space, toSpace);
				board.fuehreSpielzugAus(lastTurn);
				checkEnd(currentPlayer);
			} else {
				setLocation(space.getWorldColumn(), space.getWorldRow());
			}
		}
	}

	public void actProgram() {
		if (this.getClass() != Brain.class) {
			return;
		}
		Player currentPlayer = game.getAktuellerSpieler();
		if (board.gameState == Board.GAME) {
			programThread = new ProgramThread(currentPlayer, lastTurn);
			programThread.start();
			board.pThread = programThread;
			board.gameState = Board.WAIT;
			return;
		} else if (board.gameState == Board.WAIT && programThread.finished) {
			lastTurn = programThread.newTurn;
			programThread = null;
			board.pThread = null;
			board.gameState = Board.GAME;

			// lastTurn = currentPlayer.naechsterSpielzug(lastTurn);
			// System.out.println(currentPlayer.getName() + " Turn: " +
			// lastTurn);
			board.fuehreSpielzugAus(lastTurn);
			checkEnd(currentPlayer);
		}
	}

	private void checkEnd(Player currentPlayer) {
		if (rules.spielEnde(currentPlayer, game.getOtherPlayer())) {
			int ergebnis = rules.getErgebnis();
			int grund = rules.getEndeGrund();
			String winner, matter;
			Color color;
			Info info;
			if (ergebnis == Rules.SIEGER_A) {
				winner = "Winner is Player BLUE";
				color = Color.BLUE;
				for (Piece p : board.getPieces(true)) {
					p.setJubel();
				}
				for (Piece p : board.getPieces(false)) {
					p.setSad();
				}
			} else if (ergebnis == Rules.SIEGER_B) {
				winner = "Winner is Player RED";
				color = Color.RED;
				for (Piece p : board.getPieces(false)) {
					p.setJubel();
				}
				for (Piece p : board.getPieces(true)) {
					p.setSad();
				}
			} else { // Unentschieden
				winner = "Game ended in a tie";
				color = Color.BLACK;
				for (Piece p : board.getPieces(true)) {
					p.setNormal();
				}
				for (Piece p : board.getPieces(false)) {
					p.setNormal();
				}
			}
			if (grund == Rules.KEIN_ZUG_MOEGLICH) {
				matter = "Matter: No possible turn";
			} else if (grund == Rules.BRAIN_GESCHLAGEN) {
				matter = "Matter: Brain captured";
			} else {
				matter = "Matter: Only brains left";
			}
			info = new Info(winner, matter, color);
			board.addObject(info, board.getWidth() / 2, board.getHeight() / 2);
			board.end = true;
		} else {
			game.changePlayer();
			currentPlayer = this.game.getAktuellerSpieler();
			for (Piece p : board.getPieces(currentPlayer.isSpielerA())) {
				if (currentPlayer.isHuman()) {
					p.setNormalFinger();
				} else {
					p.setDenken();
				}
			}
			for (Piece p : board.getPieces(this.game.getOtherPlayer()
					.isSpielerA())) {
				p.setNormal();
			}
		}
	}

	private boolean in(Space[] spaces, Space space) {
		for (Space s : spaces) {
			if (s == space) {
				return true;
			}
		}
		return false;
	}

	String getColor() {
		if (isA) {
			return "blue";
		} else {
			return "red";
		}
	}

	String getKuerzel() {
		return null;
	}

	void setNormal() {
		setImage(getKuerzel() + "-" + getColor() + ".gif");
	}

	void setSad() {
		setImage(getKuerzel() + "-" + getColor() + "-sad.gif");
	}

	void setNormalFinger() {
		setImage(getKuerzel() + "-" + getColor() + "-finger.gif");
	}

	void setSadFinger() {
		setImage(getKuerzel() + "-" + getColor() + "-sad-finger.gif");
	}

	void setJubel() {
		setImage(getKuerzel() + "-" + getColor() + "-jubel.gif");
	}

	void setDenken() {
		setImage(getKuerzel() + "-" + getColor() + "-denken.gif");
	}

	void scale() {
		origWidth = getImage().getWidth();
		origHeight = getImage().getHeight();
		this.getImage()
				.scale((int) (origWidth * 1.2), (int) (origHeight * 1.2));
	}

}

class ProgramThread extends Thread {

	Player currentPlayer;
	Turn lastTurn;
	Turn newTurn;
	boolean finished;

	ProgramThread(Player c, Turn l) {
		this.currentPlayer = c;
		this.lastTurn = l;
		this.newTurn = null;
		finished = false;
	}

	public void run() {
		newTurn = currentPlayer.naechsterSpielzug(lastTurn);
		finished = true;
	}
}
