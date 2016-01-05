package org.droidfoot.dfsnake;
import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.awt.Color;

/**
 * Write a description of class Text here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Text extends Actor {
	public Text(String text) {
		setImage(new GreenfootImage(text, 24, Color.BLACK,
				new Color(0, 0, 0, 0)));
	}
}
