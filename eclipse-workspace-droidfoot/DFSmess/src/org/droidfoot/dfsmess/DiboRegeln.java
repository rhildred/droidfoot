package org.droidfoot.dfsmess;

import java.util.ArrayList;


public class DiboRegeln {

	public static final int UNENTSCHIEDEN = 0;

	public static final int SIEGER_A = 1;

	public static final int SIEGER_B = 2;

	public static final int KEIN_ENDE = -1;

	DiboBrett brett;

	DiboRegeln(DiboBrett brett) {
		this.brett = brett;
	}
	
	DiboBrett getBrett() {
		return this.brett;
	}

	public boolean spielzugOk(Turn zug, boolean isA) {

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

		DiboFeld vonFeld = this.brett
				.getFeld(zug.getVonReihe(), zug.getVonSpalte());
		DiboFeld nachFeld = this.brett.getFeld(zug.getNachReihe(), zug
				.getNachSpalte());

		// keine Figur auf angegebenem Feld
		if (!vonFeld.hatFigur()) {
			return false;
		}
		DiboFigur figur = vonFeld.getFigur();

		// Figur des Gegners
		if (figur.isA() != isA) {
			return false;
		}

		// gueltiges Feld
		DiboFeld[] nachFelder = this.getZugFelder(vonFeld, isA);
		for (int f = 0; f < nachFelder.length; f++) {
			if (nachFelder[f] == nachFeld) { // == geht hier
				return true;
			}
		}

		return false;
	}

	public DiboFeld[] getZugFelder(DiboFeld vonFeld, boolean isA) {
		// Feld leer
		if (!vonFeld.hatFigur()) {
			return new DiboFeld[0];
		}

		// Feld mit fremder Figur
		DiboFigur figur = vonFeld.getFigur();
		if (figur.isA() != isA) {
			return new DiboFeld[0];
		}

		if (figur.getClass() == DiboBrain.class || figur.getClass() == DiboNinny.class) {
			return bearbeiteNinnyOderBrainZug(vonFeld, figur);
		} else {
			return bearbeiteNumskullZug(vonFeld, figur);
		}

	}

	DiboFeld[] bearbeiteNinnyOderBrainZug(DiboFeld vonFeld, DiboFigur figur) {
		ArrayList<DiboFeld> nachFelder = new ArrayList<DiboFeld>();

		int[] richtungen = vonFeld.getRichtungen();
		DiboFeld nachFeld = null;
		for (int r = 0; r < richtungen.length; r++) {
			switch (richtungen[r]) {
			case DiboRichtung.NORD:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() + 1, vonFeld
						.getSpalte());
				break;
			case DiboRichtung.NORDWEST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() + 1,
						(char) (vonFeld.getSpalte() - 1));
				break;
			case DiboRichtung.WEST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe(),
						(char) (vonFeld.getSpalte() - 1));
				break;
			case DiboRichtung.SUEDWEST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() - 1,
						(char) (vonFeld.getSpalte() - 1));
				break;
			case DiboRichtung.SUED:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() - 1, vonFeld
						.getSpalte());
				break;
			case DiboRichtung.SUEDOST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe() - 1,
						(char) (vonFeld.getSpalte() + 1));
				break;
			case DiboRichtung.OST:
				nachFeld = this.brett.getFeld(vonFeld.getReihe(),
						(char) (vonFeld.getSpalte() + 1));
				break;
			case DiboRichtung.NORDOST:
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

		return nachFelder.toArray(new DiboFeld[0]);
	}

	DiboFeld[] bearbeiteNumskullZug(DiboFeld vonFeld, DiboFigur figur) {
		ArrayList<DiboFeld> nachFelder = new ArrayList<DiboFeld>();

		int[] richtungen = vonFeld.getRichtungen();
		DiboFeld nachFeld = null;
		richtungsSchleife: for (int r = 0; r < richtungen.length; r++) {
			switch (richtungen[r]) {
			case DiboRichtung.NORD:
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
			case DiboRichtung.NORDWEST:
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
			case DiboRichtung.WEST:
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
			case DiboRichtung.SUEDWEST:
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
			case DiboRichtung.SUED:
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
			case DiboRichtung.SUEDOST:
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
			case DiboRichtung.OST:
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
			case DiboRichtung.NORDOST:
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

		return nachFelder.toArray(new DiboFeld[0]);
	}

	// aktSpieler = Spieler mit naechstem Spielzug
	public int spielEnde(boolean isA) {

		// Brain geschlagen
		int anzahlBrains = 0;
		for (int r = 1; r <= 8; r++) {
			for (char s = 'a'; s <= 'g'; s++) {
				DiboFeld feld = this.brett.getFeld(r, s);
				if (feld.hatFigur()
						&& feld.getFigur().getClass() == DiboBrain.class) {
					anzahlBrains++;
				}
			}
		}
		if (anzahlBrains < 2) {
			if (isA) {
				return SIEGER_A;
			} else {
				return SIEGER_B;
			}
		}

		// nur noch 2 Brains
		int anzahlFiguren = 0;
		for (int r = 1; r <= 8; r++) {
			for (char s = 'a'; s <= 'g'; s++) {
				DiboFeld feld = this.brett.getFeld(r, s);
				if (feld.hatFigur()) {
					anzahlFiguren++;
				}
			}
		}
		if (anzahlFiguren <= 2) {
			return UNENTSCHIEDEN;
		}

		// kein Zug möglich
		for (int r = 1; r <= 8; r++) {
			for (char s = 'a'; s <= 'g'; s++) {
				DiboFeld feld = this.brett.getFeld(r, s);
				DiboFeld[] nachFelder = getZugFelder(feld, !isA);
				if (nachFelder != null && nachFelder.length > 0) {
					return KEIN_ENDE;
				}
			}
		}
		if (isA) {
			return SIEGER_A;
		} else {
			return SIEGER_B;
		}

	}

}
