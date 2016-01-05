package org.droidfoot.dfsnake;

import greenfoot.Actor;

// (World, Actor, GreenfootImage, and Greenfoot)

public class Body extends Actor {

	static int numberOfBodies = 0;

	Body() {
		numberOfBodies++;
		setImage("body.png");
	}

	public static int getNumberOfBodies() {
		return numberOfBodies;
	}

	public static void reset() {
		numberOfBodies = 0;
	}

}
