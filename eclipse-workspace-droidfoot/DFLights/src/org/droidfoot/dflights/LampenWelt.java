package org.droidfoot.dflights;
import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


public class LampenWelt  extends World
{

    static int N = 5;

    public LampenWelt()
    {    
        super(N, N, 50); 
        
        // Lampen erzeugen und platzieren
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                addObject(new Lampe(), x, y);
            }
        }
        
        Greenfoot.setSpeed(100);
        Greenfoot.start();
    }
    
    boolean alleLampenAn() {
        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                if (((Lampe)getObjectsAt(x, y, Lampe.class).get(0)).istAus()) {
                    return false;
                }
            }
        }
        return true;
    }
}
