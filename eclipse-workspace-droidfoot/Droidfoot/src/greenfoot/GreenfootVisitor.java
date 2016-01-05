/*
 This file is part of the Greenfoot program. 
 Copyright (C) 2005-2009,2010,2011,2012  Poul Henriksen and Michael Kolling 
 
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

/**
 * Class that makes it possible for classes outside the greenfoot package to get
 * access to Greenfoot methods that are package protected. We need some
 * package-protected methods, because we don't want them to show up in the
 * public interface visible to users.
 * 
 * @author Dietrich Boles (Modifications for Android)
 * @version 2.0
 */
public class GreenfootVisitor {

	public static int MIN_SIMULATION_SPEED = Greenfoot.MIN_SIMULATION_SPEED;
	public static int MAX_SIMULATION_SPEED = Greenfoot.MAX_SIMULATION_SPEED;

	public static volatile boolean stopFlag = false;
	public static volatile boolean startFlag = false;

	public static int getSpeed() {
		return Greenfoot.getSpeed();
	}
}
