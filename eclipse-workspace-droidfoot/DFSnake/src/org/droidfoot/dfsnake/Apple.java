package org.droidfoot.dfsnake;

import greenfoot.Actor;
import greenfoot.World;

// (World, Actor, GreenfootImage, and Greenfoot)

public class Apple extends Actor {
	public Apple() {
		setImage("food.png");
	}

	protected void addedToWorld(World world) {
		if (world.getObjectsAt(getX(), getY(), null).size() > 1) { // occupied
			world.removeObject(this);
		}
	}
}
