package org.droidfoot.dfsmess;


import java.util.ArrayList;


public class DiboSmess extends Player {

	private int spielStaerke;

	private DiboBrett brett;

	private DiboRegeln regeln;
	private String name;

	public DiboSmess(boolean isA, int staerke, String name) {
		super(isA);
		this.spielStaerke = staerke;
		this.name = name;
		this.brett = new DiboBrett();
		this.regeln = new DiboRegeln(this.brett);
	}


	public String getName() {
		return name;
	}
	
	public boolean isHuman() {
		return false;
	}

	public Turn naechsterSpielzug(Turn gegnerZug) {
		if (gegnerZug != null) {
			this.brett.fuehreSpielzugAus(gegnerZug);
		}
		Turn zug = minMax();
		this.brett.fuehreSpielzugAus(zug);
		return zug;

	}

	protected Turn minMax() {

		WertTurn zug = null;
		if (this.isSpielerA()) {
			zug = ermittleBestenAZug(spielStaerke, this.regeln);
		} else {
			zug = ermittleBestenBZug(spielStaerke, this.regeln);
		}

		return zug.getZug();
	}

	protected static WertTurn ermittleBestenAZug(int restTiefe,
			DiboRegeln aktRegeln) {
		Turn besterTurn = null;
		int besteBewertung = DiboBrett.MIN_WERT - 1;
		Turn[] zuege = ermittleFolgeZuege(true, aktRegeln);
		for (int z = 0; z < zuege.length; z++) {
			DiboBrett brettK = new DiboBrett(aktRegeln.getBrett());
			DiboRegeln regelnK = new DiboRegeln(brettK);
			brettK.fuehreSpielzugAus(zuege[z]);
			int ende = regelnK.spielEnde(true);
			int zugWert = 0;
			if (ende != DiboRegeln.KEIN_ENDE) { // also Ende
				zugWert = brettK.bewerteStellung(regelnK);
				if (ende == DiboRegeln.SIEGER_A) {
					// es braucht nicht mehr weiter gesucht werden
					return new WertTurn(zuege[z], zugWert);
				}
			} else if (restTiefe <= 1) {
				zugWert = brettK.bewerteStellung(regelnK);
			} else {
				WertTurn zug = ermittleBestenBZug(restTiefe - 1, regelnK);
				zugWert = zug.getWert();
			}
			int zufall = (int) (Math.random() * 2);
			if (zugWert > besteBewertung
					|| (zugWert == besteBewertung && zufall == 0)) {
				besteBewertung = zugWert;
				besterTurn = zuege[z];
			}
		}
		return new WertTurn(besterTurn, besteBewertung);
	}

	protected static WertTurn ermittleBestenBZug(int restTiefe,
			DiboRegeln aktRegeln) {
		Turn besterTurn = null;
		int besteBewertung = DiboBrett.MAX_WERT + 1;
		Turn[] zuege = ermittleFolgeZuege(false, aktRegeln);
		for (int z = 0; z < zuege.length; z++) {
			DiboBrett brettK = new DiboBrett(aktRegeln.getBrett());
			DiboRegeln regelnK = new DiboRegeln(brettK);
			brettK.fuehreSpielzugAus(zuege[z]);
			int ende = regelnK.spielEnde(false);
			int zugWert = 0;
			if (ende != DiboRegeln.KEIN_ENDE) { // also Ende
				zugWert = brettK.bewerteStellung(regelnK);
				if (ende == DiboRegeln.SIEGER_B) {
					// es braucht nicht mehr weiter gesucht werden
					return new WertTurn(zuege[z], zugWert);
				}
			} else if (restTiefe <= 1) {
				zugWert = brettK.bewerteStellung(regelnK);
			} else {
				WertTurn zug = ermittleBestenAZug(restTiefe - 1, regelnK);
				zugWert = zug.getWert();
			}
			int zufall = (int) (Math.random() * 2);
			if (zugWert < besteBewertung
					|| (zugWert == besteBewertung && zufall == 0)) {
				besteBewertung = zugWert;
				besterTurn = zuege[z];
			}
		}
		return new WertTurn(besterTurn, besteBewertung);
	}

	protected static Turn[] ermittleFolgeZuege(boolean isSpielerA,
			DiboRegeln aktRegeln) {
		ArrayList<Turn> zuege = new ArrayList<Turn>();
		for (int r = 1; r <= 8; r++) {
			for (char s = 'a'; s <= 'g'; s++) {
				for (int r2 = 1; r2 <= 8; r2++) {
					for (char s2 = 'a'; s2 <= 'g'; s2++) {
						Turn zug = new Turn(r, s, r2, s2);
						if (aktRegeln.spielzugOk(zug, isSpielerA)) {
							zuege.add(zug);
						}
					}
				}
			}
		}
		Turn[] moeglicheZuege = zuege.toArray(new Turn[0]);
		return moeglicheZuege;
	}

}

class WertTurn {
	Turn zug;

	int wert;

	WertTurn(Turn zug, int wert) {
		this.zug = zug;
		this.wert = wert;
	}

	Turn getZug() {
		return this.zug;
	}

	int getWert() {
		return this.wert;
	}
}
