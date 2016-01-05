/*
 This file is part of the Greenfoot program. 
 Copyright (C) 2005-2009,2010,2011,2013  Poul Henriksen and Michael Kolling 
 
 This program is free software; you can redistribute it and/or 
 modify it under the terms of the GNU General Public License 
 as published by the Free Software Foundation; either version 2 
 of the License, or (at your option) any later version. 
 
 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 GNU General Public License for more details. 
 
 You should have received a copy of the GNU General Public License 
 along with this program; if not, write to the Free Software 
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
 
 This file is subject to the Classpath exception as provided in the  
 LICENSE.txt file that accompanied this code.
 */

package greenfoot;

import java.util.Collection;

import org.droidfoot.gui.DrawPanel;

import android.content.Context;


/**
 * Class that makes it possible for classes outside the greenfoot package to get
 * access to world methods that are package protected. We need some
 * package-protected methods, because we don't want them to show up in the
 * public interface visible to users.
 * 
 * @author Dietrich Boles (Modifications for Android)
 * @version 2.0
 */
public class WorldManager {

	public static World world = null;
	public static boolean worldSet = false;
	public static boolean startInC = false;
	public static Context context;

	public static void setContext(Context c) {
		context = c;
	}

	public static void setWorld(World w) {
		if (world == null) {
			world = w;
		} else {
			world = w;
			worldSet = true;
			// DrawPanel.canvas.initBitmap();
			// DrawPanel.canvas.repaintStage();
		}
	}

	public static void initWorld(Class<?> worldClass) {
		if (world == null) {
			world = createNewWorld(worldClass);
		} else {
			world = createNewWorld(worldClass);
			DrawPanel.canvas.initBitmap();
			DrawPanel.canvas.repaintStage();
		}
	}

	public static World createNewWorld(Class<?> worldClass) {
		try {
			return (World) (worldClass.newInstance());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public static TreeActorSet getObjectsInPaintOrder() {
		return world.getObjectsInPaintOrder();
	}

	public static TreeActorSet getObjectsInActOrder() {
		return world.getObjectsInActOrder();
	}

	public static Collection<Actor> getObjectsAtPixel(int x, int y) {
		return world.getObjectsAtPixel(x, y);
	}

	public static int getSpeed() {
		return Greenfoot.speed;
	}

	public static void setStartInConstructor() {
		startInC = true;
	}

	public static void checkStartInConstructor() {
		if (startInC) {
			startInC = false;
			Greenfoot.start();
		}
	}

	public static void reset(Class<?> worldClass) {
		WorldManager.initWorld(worldClass);
	}

}
