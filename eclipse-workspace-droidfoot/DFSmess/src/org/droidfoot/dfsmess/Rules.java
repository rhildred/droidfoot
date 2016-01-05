package org.droidfoot.dfsmess;
 

import java.util.ArrayList;

public final class Rules {

	public static final int SIEGER_A = -1;

	public static final int UNENTSCHIEDEN = 0;

	public static final int SIEGER_B = 1;

	public static final int KEIN_ZUG_MOEGLICH = 0;

	public static final int BRAIN_GESCHLAGEN = 1;

	public static final int NUR_NOCH_BRAINS = 2;

	Board brett;

	boolean ende;

	int sieger;

	int grund;

	Rules(Board brett) {
		this.brett = brett;
		this.grund = -2;
		this.sieger = -2;
		this.ende = false;
	}

	public boolean spielzugOk(Turn zug, Player spieler) {
		
		if (zug == null) {
			return false;
		}

		// Koordinaten ok
		if (zug.getVonReihe() < 1 || zug.getVonReihe() > 8) {
			return false;
		}
		if (zug.getVonSpalte() < 'a' || zug.getVonSpalte() > 'g') {
			return false;
		}
		if (zug.getNachReihe() < 1 || zug.getNachReihe() > 8) {
			return false;
		}
		if (zug.getNachSpalte() < 'a' || zug.getNachSpalte() > 'g') {
			return false;
		}

		if (this.ende) {
			return false;
		}

		Space vonFeld = this.brett
				.getFeld(zug.getVonReihe(), zug.getVonSpalte());
		Space nachFeld = this.brett.getFeld(zug.getNachReihe(), zug
				.getNachSpalte());

		// keine Piece auf angegebenem Space
		if (!vonFeld.hatFigur()) {
			return false;
		}
		Piece figur = vonFeld.getFigur();

		// Piece des Gegners
		if (figur.isA() != spieler.isSpielerA()) {
			return false;
		}

		// gueltiges Space
		Space[] nachFelder = this.getZugFelder(vonFeld, spieler);
		for (int f = 0; f < nachFelder.length; f++) {
			if (nachFelder[f] == nachFeld) { // == geht hier
				return true;
			}
		}

		return false;
	}

	public Space[] getZugFelder(Space vonFeld, Player aktSpieler) {
		// Space leer
		if (!vonFeld.hatFigur()) {
			return new Space[0];
		}

		// Space mit fremder Piece
		Piece figur = vonFeld.getFigur();
		if (figur.isA() != aktSpieler.isSpielerA()) {
			return new Space[0];
		}

		if (figur.getClass() == Brain.class || figur.getClass() == Ninny.class) {
			return bearbeiteNinnyOderBrainZug(vonFeld, figur);
		} else {
			return bearbeiteNumskullZug(vonFeld, figur);
		}

	}

	Space[] bearbeiteNinnyOderBrainZug(Space vonFeld, Piece figur) {
		ArrayList<Space> nachFelder = new ArrayList<Space>();

		int[] richtungen = vonFeld.getRichtungen();
		Space nachFeld = null;
		for (int r = 0; r < richtungen.length; r++) {
			switch (richtungen[r]) {
			case Direction.NORTH:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() + 1, vonFeld
						.getSpalte());
				break;
			case Direction.NORTHWEST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() + 1,
						(char) (vonFeld.getSpalte() - 1));
				break;
			case Direction.WEST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe(),
						(char) (vonFeld.getSpalte() - 1));
				break;
			case Direction.SOUTHWEST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() - 1,
						(char) (vonFeld.getSpalte() - 1));
				break;
			case Direction.SOUTH:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() - 1, vonFeld
						.getSpalte());
				break;
			case Direction.SOUTHEAST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() - 1,
						(char) (vonFeld.getSpalte() + 1));
				break;
			case Direction.EAST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe(),
						(char) (vonFeld.getSpalte() + 1));
				break;
			case Direction.NORTHEAST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() + 1,
						(char) (vonFeld.getSpalte() + 1));
				break;
			}
			if (!nachFeld.hatFigur()
					|| (nachFeld.hatFigur() && nachFeld.getFigur().isA() != figur
							.isA())) {
				nachFelder.add(nachFeld);
			}
		}

		return nachFelder.toArray(new Space[0]);
	}

	Space[] bearbeiteNumskullZug(Space vonFeld, Piece figur) {
		ArrayList<Space> nachFelder = new ArrayList<Space>();

		int[] richtungen = vonFeld.getRichtungen();
		Space nachFeld = null;
		richtungsSchleife: for (int r = 0; r < richtungen.length; r++) {
			switch (richtungen[r]) {
			case Direction.NORTH:
				for (int reihe = vonFeld.getReihe() + 1; reihe <= 8; reihe++) {
					nachFeld = this.brett.getFeld(reihe, vonFeld.getSpalte());
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			case Direction.NORTHWEST:
				for (int reihe = vonFeld.getReihe() + 1, spalte = vonFeld
						.getSpalte() - 1; reihe <= 8 && (char) spalte >= 'a'; reihe++, spalte--) {
					nachFeld = this.brett.getFeld(reihe, (char) spalte);
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			case Direction.WEST:
				for (int spalte = vonFeld.getSpalte() - 1; (char) spalte >= 'a'; spalte--) {
					nachFeld = this.brett.getFeld(vonFeld.getReihe(),
							(char) spalte);
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			case Direction.SOUTHWEST:
				for (int reihe = vonFeld.getReihe() - 1, spalte = vonFeld
						.getSpalte() - 1; reihe >= 1 && (char) spalte >= 'a'; reihe--, spalte--) {
					nachFeld = this.brett.getFeld(reihe, (char) spalte);
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			case Direction.SOUTH:
				for (int reihe = vonFeld.getReihe() - 1; reihe >= 1; reihe--) {
					nachFeld = this.brett.getFeld(reihe, vonFeld.getSpalte());
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			case Direction.SOUTHEAST:
				for (int reihe = vonFeld.getReihe() - 1, spalte = vonFeld
						.getSpalte() + 1; reihe >= 1 && (char) spalte <= 'g'; reihe--, spalte++) {
					nachFeld = this.brett.getFeld(reihe, (char) spalte);
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			case Direction.EAST:
				for (int spalte = vonFeld.getSpalte() + 1; (char) spalte <= 'g'; spalte++) {
					nachFeld = this.brett.getFeld(vonFeld.getReihe(),
							(char) spalte);
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			case Direction.NORTHEAST:
				for (int reihe = vonFeld.getReihe() + 1, spalte = vonFeld
						.getSpalte() + 1; reihe <= 8 && (char) spalte <= 'g'; reihe++, spalte++) {
					nachFeld = this.brett.getFeld(reihe, (char) spalte);
					if (!nachFeld.hatFigur()) {
						nachFelder.add(nachFeld);
					} else if (nachFeld.getFigur().isA() != figur.isA()) {
						nachFelder.add(nachFeld);
						continue richtungsSchleife;
					} else {
						continue richtungsSchleife;
					}
				}
				break;
			}
		}

		return nachFelder.toArray(new Space[0]);
	}

	// aktSpieler = Player mit letztem Turn
	public boolean spielEnde(Player aktSpieler, Player naechsterSpieler) {
		if (this.ende) {
			return true;
		}

		// Brain geschlagen
		int anzahlBrains = 0;
		for (int r = 1; r <= 8; r++) {
			for (char s = 'a'; s <= 'g'; s++) {
				Space feld = this.brett.getFeld(r, s);
				if (feld.hatFigur()
						&& feld.getFigur().getClass() == Brain.class) {
					anzahlBrains++;
				}
			}
		}
		if (anzahlBrains < 2) {
			if (aktSpieler.isSpielerA()) {
				this.sieger = SIEGER_A;
			} else {
				this.sieger = SIEGER_B;
			}
			this.grund = BRAIN_GESCHLAGEN;
			this.ende = true;
			return true;
		}

		// nur noch 2 Brains
		int anzahlFiguren = 0;
		for (int r = 1; r <= 8; r++) {
			for (char s = 'a'; s <= 'g'; s++) {
				Space feld = this.brett.getFeld(r, s);
				if (feld.hatFigur()) {
					anzahlFiguren++;
				}
			}
		}
		if (anzahlFiguren <= 2) {
			this.sieger = UNENTSCHIEDEN;
			this.grund = NUR_NOCH_BRAINS;
			this.ende = true;
			return true;
		}

		// kein Zug möglich
		for (int r = 1; r <= 8; r++) {
			for (char s = 'a'; s <= 'g'; s++) {
				Space feld = this.brett.getFeld(r, s);
				Space[] nachFelder = getZugFelder(feld, naechsterSpieler);
				if (nachFelder != null && nachFelder.length > 0) {
					return false;
				}
			}
		}
		if (aktSpieler.isSpielerA()) {
			this.sieger = SIEGER_A;
		} else {
			this.sieger = SIEGER_B;
		}
		this.grund = KEIN_ZUG_MOEGLICH;
		this.ende = true;
		return true;
	}

	public int getErgebnis() {
		return this.sieger;
	}

	public int getEndeGrund() {
		return this.grund;
	}

}
