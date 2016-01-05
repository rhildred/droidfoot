package org.droidfoot.dfsmess;

public class DiboBrett {

	public final static int MIN_WERT = -10000000;

	public final static int MITTELWERT = 0;

	public final static int MAX_WERT = 10000000;

	DiboFeld[][] brett;

	public DiboBrett() {
		brett = new DiboFeld[8][7];

		// erste Reihe
		brett[0][0] = new DiboFeld(8, 'a', this, DiboRichtung.SUED, DiboRichtung.OST);
		brett[0][1] = new DiboFeld(8, 'b', this, DiboRichtung.WEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[0][2] = new DiboFeld(8, 'c', this, DiboRichtung.WEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[0][3] = new DiboFeld(8, 'd', this, DiboRichtung.WEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[0][4] = new DiboFeld(8, 'e', this, DiboRichtung.WEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[0][5] = new DiboFeld(8, 'f', this, DiboRichtung.WEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[0][6] = new DiboFeld(8, 'g', this, DiboRichtung.SUED);

		// zweite Reihe
		brett[1][0] = new DiboFeld(7, 'a', this, DiboRichtung.NORD, DiboRichtung.SUED);
		brett[1][1] = new DiboFeld(7, 'b', this, DiboRichtung.NORDWEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[1][2] = new DiboFeld(7, 'c', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[1][3] = new DiboFeld(7, 'd', this, DiboRichtung.WEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[1][4] = new DiboFeld(7, 'e', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[1][5] = new DiboFeld(7, 'f', this, DiboRichtung.WEST, DiboRichtung.SUED);
		brett[1][6] = new DiboFeld(7, 'g', this, DiboRichtung.NORD, DiboRichtung.SUED);

		// dritte Reihe
		brett[2][0] = new DiboFeld(6, 'a', this, DiboRichtung.NORD, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[2][1] = new DiboFeld(6, 'b', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[2][2] = new DiboFeld(6, 'c', this, DiboRichtung.NORDWEST,
				DiboRichtung.SUEDWEST, DiboRichtung.SUEDOST, DiboRichtung.NORDOST);
		brett[2][3] = new DiboFeld(6, 'd', this, DiboRichtung.WEST, DiboRichtung.OST);
		brett[2][4] = new DiboFeld(6, 'e', this, DiboRichtung.NORDWEST,
				DiboRichtung.SUEDWEST, DiboRichtung.SUEDOST, DiboRichtung.NORDOST);
		brett[2][5] = new DiboFeld(6, 'f', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.OST);
		brett[2][6] = new DiboFeld(6, 'g', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED);

		// vierte Reihe
		brett[3][0] = new DiboFeld(5, 'a', this, DiboRichtung.SUEDOST, DiboRichtung.OST,
				DiboRichtung.NORDOST);
		brett[3][1] = new DiboFeld(5, 'b', this, DiboRichtung.OST);
		brett[3][2] = new DiboFeld(5, 'c', this, DiboRichtung.NORD, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[3][3] = new DiboFeld(5, 'd', this, DiboRichtung.NORD, DiboRichtung.NORDWEST,
				DiboRichtung.WEST, DiboRichtung.SUEDWEST, DiboRichtung.SUED,
				DiboRichtung.SUEDOST, DiboRichtung.OST, DiboRichtung.NORDOST);
		brett[3][4] = new DiboFeld(5, 'e', this, DiboRichtung.NORD, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[3][5] = new DiboFeld(5, 'f', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[3][6] = new DiboFeld(5, 'g', this, DiboRichtung.NORD, DiboRichtung.SUED);

		// fuenfte Reihe
		brett[4][0] = new DiboFeld(4, 'a', this, DiboRichtung.NORD, DiboRichtung.SUED);
		brett[4][1] = new DiboFeld(4, 'b', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[4][2] = new DiboFeld(4, 'c', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED);
		brett[4][3] = new DiboFeld(4, 'd', this, DiboRichtung.NORD, DiboRichtung.NORDWEST,
				DiboRichtung.WEST, DiboRichtung.SUEDWEST, DiboRichtung.SUED,
				DiboRichtung.SUEDOST, DiboRichtung.OST, DiboRichtung.NORDOST);
		brett[4][4] = new DiboFeld(4, 'e', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED);
		brett[4][5] = new DiboFeld(4, 'f', this, DiboRichtung.WEST);
		brett[4][6] = new DiboFeld(4, 'g', this, DiboRichtung.NORDWEST, DiboRichtung.WEST,
				DiboRichtung.SUEDWEST);

		// sechste Reihe
		brett[5][0] = new DiboFeld(3, 'a', this, DiboRichtung.NORD, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[5][1] = new DiboFeld(3, 'b', this, DiboRichtung.WEST, DiboRichtung.SUED,
				DiboRichtung.OST);
		brett[5][2] = new DiboFeld(3, 'c', this, DiboRichtung.NORDWEST,
				DiboRichtung.SUEDWEST, DiboRichtung.SUEDOST, DiboRichtung.NORDOST);
		brett[5][3] = new DiboFeld(3, 'd', this, DiboRichtung.WEST, DiboRichtung.OST);
		brett[5][4] = new DiboFeld(3, 'e', this, DiboRichtung.NORDWEST,
				DiboRichtung.SUEDWEST, DiboRichtung.SUEDOST, DiboRichtung.NORDOST);
		brett[5][5] = new DiboFeld(3, 'f', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[5][6] = new DiboFeld(3, 'g', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED);

		// siebte Reihe
		brett[6][0] = new DiboFeld(2, 'a', this, DiboRichtung.NORD, DiboRichtung.SUED);
		brett[6][1] = new DiboFeld(2, 'b', this, DiboRichtung.NORD, DiboRichtung.OST);
		brett[6][2] = new DiboFeld(2, 'c', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[6][3] = new DiboFeld(2, 'd', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.OST);
		brett[6][4] = new DiboFeld(2, 'e', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUED, DiboRichtung.OST);
		brett[6][5] = new DiboFeld(2, 'f', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.SUEDOST);
		brett[6][6] = new DiboFeld(2, 'g', this, DiboRichtung.NORD, DiboRichtung.SUED);

		// achte Reihe
		brett[7][0] = new DiboFeld(1, 'a', this, DiboRichtung.NORD);
		brett[7][1] = new DiboFeld(1, 'b', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.OST);
		brett[7][2] = new DiboFeld(1, 'c', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.OST);
		brett[7][3] = new DiboFeld(1, 'd', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.OST);
		brett[7][4] = new DiboFeld(1, 'e', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.OST);
		brett[7][5] = new DiboFeld(1, 'f', this, DiboRichtung.NORD, DiboRichtung.WEST,
				DiboRichtung.OST);
		brett[7][6] = new DiboFeld(1, 'g', this, DiboRichtung.NORD, DiboRichtung.WEST);

		// besetzen
		brett[0][1].setFigur(new DiboNumskull(false));
		brett[0][2].setFigur(new DiboNumskull(false));
		brett[0][3].setFigur(new DiboBrain(false));
		brett[0][4].setFigur(new DiboNumskull(false));
		brett[0][5].setFigur(new DiboNumskull(false));
		brett[1][0].setFigur(new DiboNinny(false));
		brett[1][1].setFigur(new DiboNinny(false));
		brett[1][2].setFigur(new DiboNinny(false));
		brett[1][3].setFigur(new DiboNinny(false));
		brett[1][4].setFigur(new DiboNinny(false));
		brett[1][5].setFigur(new DiboNinny(false));
		brett[1][6].setFigur(new DiboNinny(false));

		brett[6][0].setFigur(new DiboNinny(true));
		brett[6][1].setFigur(new DiboNinny(true));
		brett[6][2].setFigur(new DiboNinny(true));
		brett[6][3].setFigur(new DiboNinny(true));
		brett[6][4].setFigur(new DiboNinny(true));
		brett[6][5].setFigur(new DiboNinny(true));
		brett[6][6].setFigur(new DiboNinny(true));
		brett[7][1].setFigur(new DiboNumskull(true));
		brett[7][2].setFigur(new DiboNumskull(true));
		brett[7][3].setFigur(new DiboBrain(true));
		brett[7][4].setFigur(new DiboNumskull(true));
		brett[7][5].setFigur(new DiboNumskull(true));
	}

	public DiboBrett(DiboBrett brett) {
		this.brett = new DiboFeld[8][7];
		for (int r = 0; r < 8; r++) {
			for (int s = 0; s < 7; s++) {
				this.brett[r][s] = new DiboFeld(brett.brett[r][s]);
				this.brett[r][s].setBrett(this);
			}
		}
	}

	public DiboFeld getFeld(int reihe, char spalte) {
		if (reihe < 1 || reihe > 8 || spalte < 'a' || spalte > 'g') {
			return null;
		}
		return brett[8 - reihe][spalte - 'a'];
	}

	public DiboFeld[][] getBrettArray() {
		return brett;
	}

	public void fuehreSpielzugAus(Turn zug) {
		DiboFeld vonFeld = brett[8 - zug.getVonReihe()][zug.getVonSpalte() - 'a'];
		DiboFeld nachFeld = brett[8 - zug.getNachReihe()][zug.getNachSpalte() - 'a'];
		DiboFigur figur = vonFeld.getFigur();
		vonFeld.entferneFigur();
		if (nachFeld.hatFigur()) {
			nachFeld.entferneFigur();
		}
		if (this.checkNinnyUmwandlung(figur, nachFeld)) {
			figur = new DiboNumskull(figur.isA());
		}
		nachFeld.setFigur(figur);
	}

	boolean checkNinnyUmwandlung(DiboFigur figur, DiboFeld feld) {
		if (!(figur.getClass() == DiboNinny.class)) {
			return false;
		}
		boolean figurA = figur.isA();
		int reihe = feld.getReihe();
		char spalte = feld.getSpalte();
		if (figurA) {
			return reihe == 8
					&& (spalte == 'b' || spalte == 'c' || spalte == 'e' || spalte == 'f');
		} else {
			return reihe == 1
					&& (spalte == 'b' || spalte == 'c' || spalte == 'e' || spalte == 'f');
		}
	}

	public int bewerteStellung(DiboRegeln regeln) {
		boolean aBrain = false;
		boolean bBrain = false;
		int aNumskulls = 0;
		int bNumskulls = 0;
		int aNinnys = 0;
		int bNinnys = 0;
		int aZugMoeglichkeiten = 0;
		int bZugMoeglichkeiten = 0;
		for (int r = 0; r < 8; r++) {
			for (int s = 0; s < 7; s++) {
				if (brett[r][s].hatFigur()) {
					DiboFigur figur = brett[r][s].getFigur();
					if (figur.isA()) {
						DiboFeld[] felder = regeln.getZugFelder(brett[r][s], true);
						if (felder != null) {
							aZugMoeglichkeiten += felder.length;
						}
						if (figur.getClass() == DiboBrain.class) {
							aBrain = true;
						} else if (figur.getClass() == DiboNumskull.class) {
							aNumskulls++;
						} else {
							aNinnys++;
						}
					} else { // isB
						DiboFeld[] felder = regeln.getZugFelder(brett[r][s], false);
						if (felder != null) {
							bZugMoeglichkeiten += felder.length;
						}
						if (figur.getClass() == DiboBrain.class) {
							bBrain = true;
						} else if (figur.getClass() == DiboNumskull.class) {
							bNumskulls++;
						} else {
							bNinnys++;
						}
					}
				}
			}
		}
		if (!aBrain) {
			return MIN_WERT;
		}
		if (!bBrain) {
			return MAX_WERT;
		}

		return aNumskulls * 9000 + aNinnys * 1000
				- (bNumskulls * 9000 + bNinnys * 1000)
				+ (aZugMoeglichkeiten - bZugMoeglichkeiten);
	}

}
