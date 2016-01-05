package org.droidfoot.dfsmess;


public class DiboSpiel {

	private DiboBrett brett;

	private DiboRegeln regeln;

	private Player[] spieler;

	private Player aktuellerSpieler;

	private boolean ende;

	public DiboSpiel(Player spielerA, Player spielerB) {
		this.brett = new DiboBrett();
		this.regeln = new DiboRegeln(brett);
		this.spieler = new Player[2];
		this.spieler[0] = spielerA;
		this.spieler[1] = spielerB;
		this.aktuellerSpieler = spielerA;
		this.ende = false;
	}

	public void beenden() {
		this.ende = true;
	}

	public boolean isEnde() {
		return ende;
	}

	public DiboBrett getBrett() {
		return brett;
	}

	public DiboRegeln getRegeln() {
		return regeln;
	}

	public Player[] getSpieler() {
		return spieler;
	}

	public Player getAktuellerSpieler() {
		return aktuellerSpieler;
	}

	public void setAktuellerSpieler(Player aktuellerSpieler) {
		this.aktuellerSpieler = aktuellerSpieler;
	}

}
