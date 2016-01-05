package org.droidfoot.dfsmess;
 

/**
 * Realisiert Spielzuege im Smess-Game; Schnittstellenklasse zwischen
 * Smess-Programmen!
 * 
 * @author Dietrich Boles
 * @version 1.0
 * 
 */
public final class Turn {

	private int vonReihe;

	private char vonSpalte;

	private int nachReihe;

	private char nachSpalte;

	/**
	 * Initialisiert einen neuen Turn
	 * 
	 * @param vonReihe
	 *            von welcher Reihe (1-8)
	 * @param vonSpalte
	 *            von welcher Spalte ('a' bis 'g')
	 * @param nachReihe
	 *            nach welcher Reihe (1-8)
	 * @param nachSpalte
	 *            nach welcher Spalte ('a' bis 'g')
	 */
	public Turn(int vonReihe, char vonSpalte, int nachReihe, char nachSpalte) {
		this.vonReihe = vonReihe;
		this.vonSpalte = vonSpalte;
		this.nachReihe = nachReihe;
		this.nachSpalte = nachSpalte;
	}
	
	public Turn(Space from, Space to) {
		this.vonReihe = from.getReihe();
		this.vonSpalte = from.getSpalte();
		this.nachReihe = to.getReihe();
		this.nachSpalte = to.getSpalte();
	}

	/**
	 * @return die Reihe, von der eine Piece gezogen werden soll
	 */
	public int getVonReihe() {
		return this.vonReihe;
	}

	/**
	 * @return die Spalte, von der eine Piece gezogen werden soll
	 */
	public char getVonSpalte() {
		return this.vonSpalte;
	}

	/**
	 * @return die Reihe, in die eine Piece gezogen werden soll
	 */
	public int getNachReihe() {
		return this.nachReihe;
	}

	/**
	 * @return die Spalte, in die eine Piece gezogen werden soll
	 */
	public char getNachSpalte() {
		return this.nachSpalte;
	}

	/**
	 * return true, falls es sich um wertegleiche Spielzuege handelt
	 * 
	 * @param spielzug
	 *            der zu vergleichende Turn (muss Objekt vom Typ Turn
	 *            sein!)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object zug) {
		Turn spielzug = (Turn) zug;
		return this.vonReihe == spielzug.vonReihe
				&& this.vonSpalte == spielzug.vonSpalte
				&& this.nachReihe == spielzug.nachReihe
				&& this.nachSpalte == spielzug.nachSpalte;
	}

	/**
	 * @return String-Repraesentation des Spielzugs
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(" + this.vonReihe + "," + this.vonSpalte + ") --> ("
				+ this.nachReihe + "," + this.nachSpalte + ")";
	}

}
