package org.droidfoot.dfsnake;

import greenfoot.Greenfoot;
import greenfoot.UserInfo;
import greenfoot.World;
import greenfoot.awt.Color;

import java.util.List;

/**
 * Well knoe worm game
 * 
 * @author Dietrich Boles
 * @version 1.0
 */
public class WormWorld extends World {

	int speed;
	static boolean stopped;

	public WormWorld() {
		this(21, 21);
	}

	public WormWorld(int columns, int rows) {
		super(columns, rows, 30);
		setBackground("tile.gif");
		setPaintOrder(Text.class, Highscore.class, ScoreBoard.class,
				Head.class, Body.class, Apple.class);
		setActOrder(Body.class, Head.class);
		generateTerritory();
		stopped = false;
	}

	public void started() {
		speed = 30;
		Greenfoot.setSpeed(speed);
		if (stopped) {
			Greenfoot.setWorld(new WormWorld());
		}
	}

	public void generateTerritory() {
		Head head = new Head();
		addObject(head, getWidth() / 2, getHeight() / 2);
		genApple();
		Body.reset();
	}

	public void genApple() {
		Apple apple = new Apple();
		int x = Greenfoot.getRandomNumber(getWidth());
		int y = Greenfoot.getRandomNumber(getHeight());
		while (this.getObjectsAt(x, y, null).size() > 0) { // occupied
			x = Greenfoot.getRandomNumber(getWidth());
			y = Greenfoot.getRandomNumber(getHeight());
		}
		addObject(apple, x, y);
	}

	@SuppressWarnings("unchecked")
	public void gameOver() {
		Greenfoot.playSound("bombe.wav");

		if (UserInfo.isStorageAvailable()) {
			UserInfo currentUser = UserInfo.getMyInfo();
			int points = Body.getNumberOfBodies();
			int oldScore = currentUser.getScore();

			Text text1 = null;
			Text text2 = null;
			Text text3 = null;

			text1 = new Text("Points: " + points);
			if (currentUser.getRank() == -1) {
				currentUser.setScore(points);
				currentUser.store();
				text2 = new Text("Your first result!");
			} else if (points > oldScore) {
				currentUser.setScore(points);
				currentUser.store();
				text2 = new Text("New personal best!");
			} else {
				text2 = new Text("Try it again!");
			}
			text3 = new Text("Highscore table");

			addObject(text1, getWidth() / 2, 1);
			addObject(text2, getWidth() / 2, 2);
			addObject(text3, getWidth() / 2, 5);

			List<UserInfo> topList = UserInfo.getTop(6);
			List<UserInfo> nearbyList = UserInfo.getNearby(6);
			List<UserInfo> list = Highscore.merge(topList, nearbyList, 12);

			Highscore hs = new Highscore(list, // All UserInfos that will be
												// shown in the list
					(this.getWidth() - 6) * this.getCellSize() - 20, // width of
																		// the
																		// highscore-field
					(this.getHeight() - 6) * this.getCellSize() - 20, // height
																		// of
																		// the
																		// highscore-field
					12, // maximal number of elements in the highscore
					currentUser, // UserInfo of the current user
					new Color(100, 197, 32, 90), // Highscore-background
					new Color(100, 197, 32, 90), // Highscore-frame

					Color.BLACK, // Highscore-text
					new Color(0, 0, 0, 100), // Frame of the personal
												// highscore-field
					new Color(200, 200, 200, 150), // Field of the personal
													// highscore
					new Color(0, 0, 0, 100), // Frame of the general
												// highscore-field
					new Color(100, 150, 100, 150), // Field of the general
													// highscore
					new Color(150, 200, 150, 150)); // Background of score-part
			addObject(hs, getWidth() / 2, getHeight() / 2 + 3);
		} else {
			addObject(new ScoreBoard(Body.getNumberOfBodies()), getWidth() / 2,
					getHeight() / 2);
		}

		stopped = true;
		Greenfoot.stop();
	}

}
