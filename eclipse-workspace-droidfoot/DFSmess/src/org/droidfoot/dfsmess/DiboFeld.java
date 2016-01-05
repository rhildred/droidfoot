package org.droidfoot.dfsmess;

public class DiboFeld {

	int reihe;

	char spalte;

	int[] richtungen; // speichert die Richtungspfeile des Feldes

	DiboFigur figur;

	DiboBrett brett;

	DiboFeld(int reihe, char spalte, DiboBrett brett, int... richtungen) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.brett = brett;
		this.richtungen = new int[richtungen.length];
		for (int r = 0; r < richtungen.length; r++) {
			this.richtungen[r] = richtungen[r];
		}
		figur = null;
	}

	DiboFeld(DiboFeld feld) {
		this.reihe = feld.reihe;
		this.spalte = feld.spalte;
		this.richtungen = new int[feld.richtungen.length];
		for (int r = 0; r < feld.richtungen.length; r++) {
			this.richtungen[r] = feld.richtungen[r];
		}
		if (feld.figur == null) {
			this.figur = null;
		} else {
			this.figur = (DiboFigur)(feld.figur.clone());
		}
		this.brett = null;
	}

	public int getReihe() {
		return this.reihe;
	}

	public char getSpalte() {
		return this.spalte;
	}

	public boolean besitztRichtung(int richtung) {
		for (int r : this.richtungen) {
			if (r == richtung) {
				return true;
			}
		}
		return false;
	}

	int[] getRichtungen() {
		return this.richtungen;
	}

	public DiboFigur getFigur() {
		return this.figur;
	}

	void setFigur(DiboFigur figur) {
		this.figur = figur;
	}

	void entferneFigur() {
		this.figur = null;
	}

	public boolean hatFigur() {
		return this.figur != null;
	}

	DiboBrett getBrett() {
		return this.brett;
	}

	void setBrett(DiboBrett brett) {
		this.brett = brett;
	}

}
