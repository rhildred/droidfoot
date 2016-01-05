package org.droidfoot.dfsnake;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.MouseInfo;

import java.util.List;
import java.util.Vector;

// (World, Actor, GreenfootImage, and Greenfoot)

/**
 * Write a description of class Head here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Head extends Actor {

	final static int NORTH = 0;
	final static int EAST = 1;
	final static int SOUTH = 2;
	final static int WEST = 3;

	private int direction;
	@SuppressWarnings("rawtypes")
	private Vector body;

	@SuppressWarnings("rawtypes")
	Head() {
		setImage("head.png");
		setDirection(EAST);
		body = new Vector();
	}

	public void act() {
		if (WormWorld.stopped) {
			return;
		}
		optChangeDirection();
		move();
	}

	private void optChangeDirection() {
		if (Greenfoot.mousePressed(null)) {
			MouseInfo mouse = Greenfoot.getMouseInfo();
			int px = mouse.getX();
			int py = mouse.getY();
			int hx = this.getX();
			int hy = this.getY();
			switch (direction) {
			case WEST:
			case EAST:
				if (py <= hy) {
					setDirection(NORTH);
				} else {
					setDirection(SOUTH);
				}
				break;
			case NORTH:
			case SOUTH:
				if (px <= hx) {
					setDirection(WEST);
				} else {
					setDirection(EAST);
				}
				break;
			}
		}
	}

	private void move() {
		switch (direction) {
		case SOUTH:
			setLocation(getX(), getY() + 1);
			break;
		case EAST:
			setLocation(getX() + 1, getY());
			break;
		case NORTH:
			setLocation(getX(), getY() - 1);
			break;
		case WEST:
			setLocation(getX() - 1, getY());
			break;
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void setLocation(int x, int y) {
		if (x == getX() && y == getY()) {
			return;
		}
		if (checkBorderTouch(x, y) || checkBodyTouch(x, y)) {
			return;
		}
		int oldX = getX();
		int oldY = getY();
		super.setLocation(x, y);
		List apples = getWorld().getObjectsAt(x, y, Apple.class);
		if (apples.size() > 0) { // found an apple
			// Greenfoot.playSound("ruelpser.wav");
			getWorld().removeObject((Actor) apples.get(0));
			Actor b = new Body();
			if (Body.getNumberOfBodies() % 10 == 0) {
				((WormWorld) getWorld()).speed += 1;
				Greenfoot.setSpeed(((WormWorld) getWorld()).speed);
			}
			getWorld().addObject(b, oldX, oldY);
			body.add(b);
			((WormWorld) getWorld()).genApple();
		} else {
			moveBody(oldX, oldY);
		}

	}

	private boolean checkBorderTouch(int x, int y) {
		if (x < 0 || x >= getWorld().getWidth() || y < 0
				|| y >= getWorld().getHeight()) {
			((WormWorld) getWorld()).gameOver();
			return true;
		}
		return false;
	}

	private boolean checkBodyTouch(int x, int y) {
		if (getWorld().getObjectsAt(x, y, Body.class).size() > 0) {
			((WormWorld) getWorld()).gameOver();
			return true;
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void moveBody(int oldHeadX, int oldHeadY) {
		if (body.size() > 0) {
			Body lastBody = (Body) body.remove(0);
			body.add(lastBody);
			lastBody.setLocation(oldHeadX, oldHeadY);
		}
	}

	private void setDirection(int dir) {
		direction = dir;
	}
}
