package org.droidfoot.dfsmess;


public final class Space {

	int reihe;

	char spalte;

	int[] richtungen; // speichert die Richtungspfeile des Feldes

	Piece figur;

	Board brett;

	PossibleSpace possibleSpace;
	CurrentSpace currentSpace;

	Space(int reihe, char spalte, Board brett, int... richtungen) {
		this.reihe = reihe;
		this.spalte = spalte;
		this.brett = brett;
		this.richtungen = new int[richtungen.length];
		for (int r = 0; r < richtungen.length; r++) {
			this.richtungen[r] = richtungen[r];
		}
		figur = null;
		possibleSpace = new PossibleSpace();
		currentSpace = new CurrentSpace();
	}

	Space(Space feld) {
		this.reihe = feld.reihe;
		this.spalte = feld.spalte;
		this.richtungen = new int[feld.richtungen.length];
		for (int r = 0; r < feld.richtungen.length; r++) {
			this.richtungen[r] = feld.richtungen[r];
		}
		if (feld.figur == null) {
			this.figur = null;
		} else {
			this.figur = (Piece) (feld.figur.clone());
		}
		this.brett = null;
		possibleSpace = new PossibleSpace();
		currentSpace = new CurrentSpace();
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

	public Piece getFigur() {
		return this.figur;
	}

	void setFigur(Piece figur) {
		this.figur = figur;
		if (figur != null) {
			this.figur.setSpace(this);
			brett.addObject(figur, getWorldColumn(), getWorldRow());
		}
	}

	void entferneFigur() {
		brett.removeObject(figur);
		this.figur.setSpace(null);
		this.figur = null;
	}

	public boolean hatFigur() {
		return this.figur != null;
	}

	Board getBrett() {
		return this.brett;
	}

	void setBrett(Board brett) {
		this.brett = brett;
	}

	int getWorldRow() {
		return 8 - reihe;
	}

	int getWorldColumn() {
		return spalte - 'a';
	}

	public void markPossible() {
		brett.addObject(possibleSpace, getWorldColumn(), getWorldRow());
	}

	public void unmarkPossible() {
		brett.removeObject(possibleSpace);
	}

	public void markCurrent() {
		brett.addObject(currentSpace, getWorldColumn(), getWorldRow());
	}

	public void unmarkCurrent() {
		brett.removeObject(currentSpace);
	}
}
