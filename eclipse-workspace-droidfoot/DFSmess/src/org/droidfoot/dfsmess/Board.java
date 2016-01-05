package org.droidfoot.dfsmess;
 

import greenfoot.Greenfoot;
import greenfoot.World;

import java.util.ArrayList;

public final class Board extends World {

	final static int NUMBER_OF_CHOICES = 5;

	final static int PLAYER_CHOICE = 1;
	final static int GAME = 2;
	final static int WAIT = 3;

	Space[][] board;
	Game game;
	boolean end;
	boolean started;

	Brain brainA, brainB;
	ChoiceBrain[] choiceBrainsA;
	ChoiceBrain[] choiceBrainsB;

	int gameState;
	Thread pThread = null;

	public Board() {
		super(7, 8, 75);
		setBackground("board.gif");
		Greenfoot.setSpeed(100);
		initChoiceBoard();
		if (pThread != null) {
			// pThread.stop();
			pThread = null;
		}
		Piece.lastTurn = null;
		gameState = PLAYER_CHOICE;
		Greenfoot.start();
	}

	private void initChoiceBoard() {

		end = false;
		started = false;

		game = new Game(this);

		choiceBrainsA = new ChoiceBrain[NUMBER_OF_CHOICES];
		for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
			choiceBrainsA[i] = new ChoiceBrain(true, i);
		}
		choiceBrainsB = new ChoiceBrain[NUMBER_OF_CHOICES];
		for (int i = 0; i < NUMBER_OF_CHOICES; i++) {
			choiceBrainsB[i] = new ChoiceBrain(false, i);
		}

		board = new Space[8][7];

		// erste Reihe
		board[0][0] = new Space(8, 'a', this, Direction.SOUTH, Direction.EAST);
		board[0][1] = new Space(8, 'b', this, Direction.WEST, Direction.SOUTH,
				Direction.EAST);
		board[0][2] = new Space(8, 'c', this, Direction.WEST, Direction.SOUTH,
				Direction.EAST);
		board[0][3] = new Space(8, 'd', this, Direction.WEST, Direction.SOUTH,
				Direction.EAST);
		board[0][4] = new Space(8, 'e', this, Direction.WEST, Direction.SOUTH,
				Direction.EAST);
		board[0][5] = new Space(8, 'f', this, Direction.WEST, Direction.SOUTH,
				Direction.EAST);
		board[0][6] = new Space(8, 'g', this, Direction.SOUTH);

		// zweite Reihe
		board[1][0] = new Space(7, 'a', this, Direction.NORTH, Direction.SOUTH);
		board[1][1] = new Space(7, 'b', this, Direction.NORTHWEST,
				Direction.SOUTH, Direction.EAST);
		board[1][2] = new Space(7, 'c', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[1][3] = new Space(7, 'd', this, Direction.WEST, Direction.SOUTH,
				Direction.EAST);
		board[1][4] = new Space(7, 'e', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[1][5] = new Space(7, 'f', this, Direction.WEST, Direction.SOUTH);
		board[1][6] = new Space(7, 'g', this, Direction.NORTH, Direction.SOUTH);

		// dritte Reihe
		board[2][0] = new Space(6, 'a', this, Direction.NORTH, Direction.SOUTH,
				Direction.EAST);
		board[2][1] = new Space(6, 'b', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[2][2] = new Space(6, 'c', this, Direction.NORTHWEST,
				Direction.SOUTHWEST, Direction.SOUTHEAST, Direction.NORTHEAST);
		board[2][3] = new Space(6, 'd', this, Direction.WEST, Direction.EAST);
		board[2][4] = new Space(6, 'e', this, Direction.NORTHWEST,
				Direction.SOUTHWEST, Direction.SOUTHEAST, Direction.NORTHEAST);
		board[2][5] = new Space(6, 'f', this, Direction.NORTH, Direction.WEST,
				Direction.EAST);
		board[2][6] = new Space(6, 'g', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH);

		// vierte Reihe
		board[3][0] = new Space(5, 'a', this, Direction.SOUTHEAST,
				Direction.EAST, Direction.NORTHEAST);
		board[3][1] = new Space(5, 'b', this, Direction.EAST);
		board[3][2] = new Space(5, 'c', this, Direction.NORTH, Direction.SOUTH,
				Direction.EAST);
		board[3][3] = new Space(5, 'd', this, Direction.NORTH,
				Direction.NORTHWEST, Direction.WEST, Direction.SOUTHWEST,
				Direction.SOUTH, Direction.SOUTHEAST, Direction.EAST,
				Direction.NORTHEAST);
		board[3][4] = new Space(5, 'e', this, Direction.NORTH, Direction.SOUTH,
				Direction.EAST);
		board[3][5] = new Space(5, 'f', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[3][6] = new Space(5, 'g', this, Direction.NORTH, Direction.SOUTH);

		// fuenfte Reihe
		board[4][0] = new Space(4, 'a', this, Direction.NORTH, Direction.SOUTH);
		board[4][1] = new Space(4, 'b', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[4][2] = new Space(4, 'c', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH);
		board[4][3] = new Space(4, 'd', this, Direction.NORTH,
				Direction.NORTHWEST, Direction.WEST, Direction.SOUTHWEST,
				Direction.SOUTH, Direction.SOUTHEAST, Direction.EAST,
				Direction.NORTHEAST);
		board[4][4] = new Space(4, 'e', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH);
		board[4][5] = new Space(4, 'f', this, Direction.WEST);
		board[4][6] = new Space(4, 'g', this, Direction.NORTHWEST,
				Direction.WEST, Direction.SOUTHWEST);

		// sechste Reihe
		board[5][0] = new Space(3, 'a', this, Direction.NORTH, Direction.SOUTH,
				Direction.EAST);
		board[5][1] = new Space(3, 'b', this, Direction.WEST, Direction.SOUTH,
				Direction.EAST);
		board[5][2] = new Space(3, 'c', this, Direction.NORTHWEST,
				Direction.SOUTHWEST, Direction.SOUTHEAST, Direction.NORTHEAST);
		board[5][3] = new Space(3, 'd', this, Direction.WEST, Direction.EAST);
		board[5][4] = new Space(3, 'e', this, Direction.NORTHWEST,
				Direction.SOUTHWEST, Direction.SOUTHEAST, Direction.NORTHEAST);
		board[5][5] = new Space(3, 'f', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[5][6] = new Space(3, 'g', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH);

		// siebte Reihe
		board[6][0] = new Space(2, 'a', this, Direction.NORTH, Direction.SOUTH);
		board[6][1] = new Space(2, 'b', this, Direction.NORTH, Direction.EAST);
		board[6][2] = new Space(2, 'c', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[6][3] = new Space(2, 'd', this, Direction.NORTH, Direction.WEST,
				Direction.EAST);
		board[6][4] = new Space(2, 'e', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTH, Direction.EAST);
		board[6][5] = new Space(2, 'f', this, Direction.NORTH, Direction.WEST,
				Direction.SOUTHEAST);
		board[6][6] = new Space(2, 'g', this, Direction.NORTH, Direction.SOUTH);

		// achte Reihe
		board[7][0] = new Space(1, 'a', this, Direction.NORTH);
		board[7][1] = new Space(1, 'b', this, Direction.NORTH, Direction.WEST,
				Direction.EAST);
		board[7][2] = new Space(1, 'c', this, Direction.NORTH, Direction.WEST,
				Direction.EAST);
		board[7][3] = new Space(1, 'd', this, Direction.NORTH, Direction.WEST,
				Direction.EAST);
		board[7][4] = new Space(1, 'e', this, Direction.NORTH, Direction.WEST,
				Direction.EAST);
		board[7][5] = new Space(1, 'f', this, Direction.NORTH, Direction.WEST,
				Direction.EAST);
		board[7][6] = new Space(1, 'g', this, Direction.NORTH, Direction.WEST);

		// besetzen
		board[0][1].setFigur(new Numskull(false));
		board[0][2].setFigur(new Numskull(false));
		// board[0][3].setFigur(brainB = new Brain(false));
		board[0][4].setFigur(new Numskull(false));
		board[0][5].setFigur(new Numskull(false));
		board[1][0].setFigur(new Ninny(false));
		board[1][1].setFigur(new Ninny(false));
		board[1][2].setFigur(new Ninny(false));
		board[1][3].setFigur(new Ninny(false));
		board[1][4].setFigur(new Ninny(false));
		board[1][5].setFigur(new Ninny(false));
		board[1][6].setFigur(new Ninny(false));

		board[6][0].setFigur(new Ninny(true));
		board[6][1].setFigur(new Ninny(true));
		board[6][2].setFigur(new Ninny(true));
		board[6][3].setFigur(new Ninny(true));
		board[6][4].setFigur(new Ninny(true));
		board[6][5].setFigur(new Ninny(true));
		board[6][6].setFigur(new Ninny(true));
		board[7][1].setFigur(new Numskull(true));
		board[7][2].setFigur(new Numskull(true));
		// board[7][3].setFigur(brainA = new Brain(true));
		board[7][4].setFigur(new Numskull(true));
		board[7][5].setFigur(new Numskull(true));

		for (int i = 1; i <= NUMBER_OF_CHOICES; i++) {
			board[3][i].setFigur(choiceBrainsB[i - 1]);
			board[4][i].setFigur(choiceBrainsA[i - 1]);
		}

		addObject(new CurrentSpace(), 3, 7);
		addObject(new PossibleSpace(), 3, 0);
	}

	private void initBoard() {

		end = false;
		started = false;

		game = new Game(this, choiceA, choiceB);

		board[0][3].setFigur(brainB = new Brain(false));
		board[7][3].setFigur(brainA = new Brain(true));

		for (int i = 1; i <= NUMBER_OF_CHOICES; i++) {
			board[3][i].setFigur(null);
			board[4][i].setFigur(null);
		}
	}

	public Game getGame() {
		return this.game;
	}

	public Board(Board brett) {
		super(7, 8, 75);
		setBackground("board.gif");
		end = false;
		started = false;
		this.board = new Space[8][7];
		for (int r = 0; r < 8; r++) {
			for (int s = 0; s < 7; s++) {
				this.board[r][s] = new Space(brett.board[r][s]);
				this.board[r][s].setBrett(this);
			}
		}
	}

	public Space getFeld(int reihe, char spalte) {
		if (reihe < 1 || reihe > 8 || spalte < 'a' || spalte > 'g') {
			return null;
		}
		return board[8 - reihe][spalte - 'a'];
	}

	public Space getFeld(int reihe, int spalte) {
		try {
			return board[reihe][spalte];
		} catch (ArrayIndexOutOfBoundsException exc) {
			return null;
		}
	}

	public Space[][] getBrettArray() {
		return board;
	}

	public void fuehreSpielzugAus(Turn zug) {
		if (zug == null) {
			return;
		}
		Space vonFeld = board[8 - zug.getVonReihe()][zug.getVonSpalte() - 'a'];
		Space nachFeld = board[8 - zug.getNachReihe()][zug.getNachSpalte() - 'a'];
		Piece figur = vonFeld.getFigur();
		vonFeld.entferneFigur();
		if (nachFeld.hatFigur()) {
			nachFeld.entferneFigur();
		}
		if (this.checkNinnyUmwandlung(figur, nachFeld)) {
			figur = new Numskull(figur.isA());
		}
		nachFeld.setFigur(figur);
	}

	boolean checkNinnyUmwandlung(Piece figur, Space feld) {
		if (!(figur.getClass() == Ninny.class)) {
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

	ArrayList<Piece> toFront = new ArrayList<Piece>();

	void setFront(Piece piece) {
		toFront.add(piece);
	}

	public void act() {
		switch (gameState) {
		case PLAYER_CHOICE:
			handlePlayerChoice();
			break;
		case GAME:
		case WAIT:
			handleGame();
			break;
		}
	}

	private void handlePlayerChoice() {
		for (Piece piece : toFront) {
			int x = piece.getX();
			int y = piece.getY();
			piece.setLocation(x, y);
			//removeObject(piece);
			//addObject(piece, x, y);
		}
		toFront.clear();

	}

	private void handleGame() {
		if (end) {
			Piece.lastTurn = null;
			Greenfoot.stop();
			return;
		}

		if (!started) {
			started = true;
			Player currentPlayer = this.game.getAktuellerSpieler();
			for (Piece p : getPieces(this.game.getAktuellerSpieler()
					.isSpielerA())) {
				if (currentPlayer.isHuman()) {
					p.setNormalFinger();
				} else {
					p.setDenken();
				}
			}
		}
		for (Piece piece : toFront) {
			int x = piece.getX();
			int y = piece.getY();
			removeObject(piece);
			addObject(piece, x, y);
		}
		toFront.clear();
		this.repaint();
		checkBothPrograms();
	}

	public ArrayList<Piece> getPieces(boolean isA) {
		ArrayList<Piece> list = new ArrayList<Piece>();
		for (int r = 0; r < 8; r++) {
			for (int s = 0; s < 7; s++) {
				Piece p = board[r][s].getFigur();
				if (p != null && p.isA() == isA) {
					list.add(p);
				}
			}
		}
		return list;
	}

	private void checkBothPrograms() {
		Player currentPlayer = game.getAktuellerSpieler();
		Player otherPlayer = game.getOtherPlayer();
		if (!currentPlayer.isHuman() && !otherPlayer.isHuman()) {
			if (currentPlayer.isSpielerA()) {
				brainA.actProgram();
			} else {
				brainB.actProgram();
			}
		}
	}

	int choiceA = -1;

	void setPlayerAChoice(int staerke) {
		choiceA = staerke;
		if (choiceB != -1) {
			startGame();
		}
	}

	int choiceB = -1;

	void setPlayerBChoice(int staerke) {
		choiceB = staerke;
		if (choiceA != -1) {
			startGame();
		}
	}

	void startGame() {
		this.removeObjects(this.getObjects(ChoiceBrain.class));
		this.removeObjects(this.getObjects(CurrentSpace.class));
		this.removeObjects(this.getObjects(PossibleSpace.class));
		initBoard();
		gameState = GAME;
	}

}
