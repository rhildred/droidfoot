package org.droidfoot.dfsmess;


public final class Game {

	private Board brett;

	private Rules regeln;

	private Player[] spieler;

	private Player aktuellerSpieler;

	private boolean ende;

	public Game(Board board) {
		this.brett = board;
		this.regeln = new Rules(brett);
		this.spieler = new Player[2];
		this.spieler[0] = new Player(true);
		this.spieler[1] = new Player(false);
		this.aktuellerSpieler = this.spieler[0];
		this.ende = false;
	}

	public Game(Board board, int choiceA, int choiceB) {
		this.brett = board;
		this.regeln = new Rules(brett);
		this.spieler = new Player[2];
		if (choiceA == 0) {
			this.spieler[0] = new Player(true);
		} else {
			this.spieler[0] = new DiboSmess(true, choiceA, "strong" + choiceA);
		}
		if (choiceB == 0) {
			this.spieler[1] = new Player(false);
		} else {
			this.spieler[1] = new DiboSmess(false, choiceB, "strong" + choiceA);
		}
		this.aktuellerSpieler = this.spieler[0];
		this.ende = false;
	}

	public void beenden() {
		this.ende = true;
	}

	public boolean isEnde() {
		return ende;
	}

	public Board getBrett() {
		return brett;
	}

	public Rules getRegeln() {
		return regeln;
	}

	public Player[] getSpieler() {
		return spieler;
	}

	public Player getSpielerA() {
		return spieler[0];
	}

	public Player getSpielerB() {
		return spieler[1];
	}

	public Player getAktuellerSpieler() {
		return aktuellerSpieler;
	}

	public void setAktuellerSpieler(Player aktuellerSpieler) {
		this.aktuellerSpieler = aktuellerSpieler;
	}

	public void changePlayer() {
		if (this.aktuellerSpieler == spieler[0]) {
			this.aktuellerSpieler = spieler[1];
		} else {
			this.aktuellerSpieler = spieler[0];
		}
	}

	public Player getOtherPlayer() {
		if (this.aktuellerSpieler == spieler[0]) {
			return spieler[1];
		} else {
			return spieler[0];
		}
	}

}
