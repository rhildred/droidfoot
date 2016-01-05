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
package org.droidfoot.util;

import greenfoot.GreenfootImage;
import greenfoot.UserInfo;
import greenfoot.awt.Color;

import java.util.List;

/**
 * General utility methods for Greenfoot.
 * 
 * @author Davin McCall
 */
public class GreenfootUtil {

	private static GreenfootUtilDelegateIDE delegate = GreenfootUtilDelegateIDE
			.getInstance();

	/**
	 * Find out whether storage is supported in the current setting
	 */
	public static boolean isStorageSupported() {
		return delegate.isStorageSupported();
	}

	/**
	 * null if an error, blank values if no previous storage
	 */
	public static UserInfo getCurrentUserInfo() {
		return delegate.getCurrentUserInfo();
	}

	/**
	 * returns whether it was successful
	 */
	public static boolean storeCurrentUserInfo(UserInfo data) {
		if (data.getUserName().equals(getUserName()))
			return delegate.storeCurrentUserInfo(data);
		else {
			// This message the user should see, because
			// it indicates a programming mistake:
			System.err
					.println("Attempted to store the data for another user, \""
							+ data.getUserName()
							+ "\" (i.e. a user other than the current user, \""
							+ getUserName() + "\")");
			return false;
		}
	}

	/**
	 * null if problem, empty list if simply no data
	 * 
	 * Returns highest data when sorted by integer index 0
	 */
	public static List<UserInfo> getTopUserInfo(int limit) {
		return delegate.getTopUserInfo(limit);
	}

	/**
	 * returns null if storage not supported.
	 */
	public static GreenfootImage getUserImage(String userName) {
		if (userName == null || userName.equals("")) {
			userName = getUserName();
		}

		GreenfootImage r = null;

		if (userName != null) {
			r = delegate.getUserImage(userName);
		}

		if (r == null) {
			// This can be because there was a problem reading from the gallery,
			// or because we're using local storage:
			r = new GreenfootImage(50, 50);
			r.setColor(Color.DARK_GRAY);
			r.fill();

			final int CHARS_PER_LINE = 6; // Heuristic: 15 pixels high, assume 8
											// pixels width per char, 50 / 8 ~=
											// 6

			StringBuilder wrappedName = new StringBuilder();
			if (userName == null)
				userName = "";
			for (int i = 0; i < userName.length(); i += CHARS_PER_LINE)
				wrappedName
						.append(userName.substring(i,
								Math.min(userName.length(), i + CHARS_PER_LINE)))
						.append("\n");

			GreenfootImage textImage = new GreenfootImage(
					wrappedName.toString(), 15, Color.WHITE,
					Color.DARK_GRAY);
			r.drawImage(textImage,
					Math.max(0, (50 - textImage.getWidth()) / 2),
					Math.max(0, (50 - textImage.getHeight()) / 2));
		}
		// Should never return null:
		return r;
	}

	// For local storage, this is the username set via the IDE
	// For remote storage, this is the username got from the applet params
	// For turned off, this is null
	public static String getUserName() {
		return delegate.getUserName();
	}

	/**
	 * Get info for users near the current player when sorted by score
	 * 
	 * @return null if problem, empty list if simply no data.
	 */
	public static List<UserInfo> getNearbyUserData(int maxAmount) {
		return delegate.getNearbyUserInfo(maxAmount);
	}
}
