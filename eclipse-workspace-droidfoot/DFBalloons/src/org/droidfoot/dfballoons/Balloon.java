package org.droidfoot.dfballoons;

import greenfoot.Actor;
import greenfoot.Greenfoot;
// (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A balloon flows from bottom to top.
 * 
 * @author Poul Henriksen
 */
public class Balloon extends Actor
{
    
    public Balloon() {
        setImage("balloon1.gif");
    }
    /**
     * Make the balloon go up. 
     */ 
    public void act() 
    {
        setLocation(getX(), getY() -1);
        if (getY() == 0) {
            ((BalloonWorld) getWorld()).gameOver();
        }
    }    
    
    /**
     * Pop this balloon.
     */
    public void pop() 
    {
        Greenfoot.playSound("pop.wav");
        ((BalloonWorld) getWorld()).countPop();
        getWorld().removeObject(this);
    }
}
