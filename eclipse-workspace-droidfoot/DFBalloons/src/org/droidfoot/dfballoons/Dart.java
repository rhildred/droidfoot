package org.droidfoot.dfballoons;

import greenfoot.Actor;
import greenfoot.Greenfoot;
import greenfoot.MouseInfo;

import java.util.List;
// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A dart is used to pop balloons.
 * 
 * @author Poul Henriksen
 */
public class Dart extends Actor
{
    
    public Dart() {
        setImage("dart.png");
    }
    /**
     * Make the dart check for mouseclicks.
     */
    public void act() 
    {
        // Pop
        if(Greenfoot.mousePressed(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            int x = mouse.getX();
            int y = mouse.getY();
            setLocation(x + getImage().getWidth() / 2, y - getImage().getHeight() / 2);
            List balloons = getWorld().getObjectsAt(x, y, Balloon.class);
            if (balloons.size() > 0) {
                Balloon balloon = (Balloon)balloons.get(0);
                balloon.pop();
            }
        }
        
    }    
}
