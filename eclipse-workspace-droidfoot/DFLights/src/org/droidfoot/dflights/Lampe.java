package org.droidfoot.dflights;
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Lampe  extends Actor
{

    private boolean lampeAus;

    Lampe() {
        lampeAus = true;
        aendereIcon();
    }
    
    boolean istAus() {
        return lampeAus;
    }
    
    @Override
    public void act() 
    {
        if (Greenfoot.mousePressed(this)) {
            zustaendeWechseln();
            if (((LampenWelt)getWorld()).alleLampenAn()) {
               Greenfoot.stop();
            }
        }
    }    
    
    void aendereIcon() {
        if (lampeAus) {
            setImage("lampe-aus.gif");
        } else {
            setImage("lampe-an.gif");
        }
    }
    
    void zustandWechseln() {
        lampeAus = !lampeAus;
        aendereIcon();
    }
    
    void zustaendeWechseln() {
        this.zustandWechseln();
        
        // Nachbarn
        int x = getX();
        int y = getY();
        if (x > 0) {
            ((Lampe)getWorld().getObjectsAt(x - 1, y, Lampe.class).get(0)).zustandWechseln();
        }
        if (x < getWorld().getWidth() - 1) {
            ((Lampe)getWorld().getObjectsAt(x + 1, y, Lampe.class).get(0)).zustandWechseln();
        }
        if (y > 0) {
            ((Lampe)getWorld().getObjectsAt(x, y - 1, Lampe.class).get(0)).zustandWechseln();
        }
        if (y < getWorld().getHeight() - 1) {
            ((Lampe)getWorld().getObjectsAt(x, y + 1, Lampe.class).get(0)).zustandWechseln();
        }
    }
    
}
