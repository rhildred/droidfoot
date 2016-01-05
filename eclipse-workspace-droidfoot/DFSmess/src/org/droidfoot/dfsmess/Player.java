package org.droidfoot.dfsmess;
/**
 * Realisiert einen Smess-Player; Smess-Programme müssen von dieser Klasse
 * abgeleitet sein; Schnittstellenklasse fuer Smess-Programme
 * 
 * @author dibo
 * @version 1.0
 * 
 */
public class Player {

	private boolean spielerA;

	public Player(boolean spielerA) {
		this.spielerA = spielerA;
	}

	/**
	 * @return true, falls es sich um Player A handelt
	 */
	public final boolean isSpielerA() {
		return this.spielerA;
	}

	/**
	 * @return true, falls es sich um Player B handelt
	 */
	public final boolean isSpielerB() {
		return !this.isSpielerA();
	}
	
	public boolean isHuman() {
		return true;
	}

	/**
	 * return true, falls es sich beim aufgerufenen Objekt um denselben Sieler
	 * handelt wie beim Parameter-Objekt; Achtung: ueberprueft
	 * Referenzgleichheit!
	 * 
	 * @param spieler
	 *            das zu vergleichende Objekt
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public final boolean equals(Object spieler) {
		return this == spieler;
	}

	/**
	 * @return String-Repraesentation des Spielers
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return (spielerA ? "A" : "B");
	}

	/**
	 * @return Name des Spielers; muss ueberschrieben werden!
	 */
	public String getName() {
		return "Mensch";
	}

	/**
	 * erfragt den naechsten Turn des Player
	 * 
	 * @param gegnerZug
	 *            der letzte Turn des Gegners oder null, falls der erste Turn
	 *            erfragt wird; muss ueberschrieben werden!
	 * @return der naechste Turn des Spielers
	 */
	public Turn naechsterSpielzug(Turn gegnerZug) {
		return null;
	}
	

}
