package org.droidfoot;

import android.graphics.Color;

public class Settings {

	public static boolean speed = true;

	public static boolean help = true;
	
	public static int backgroundColor = Color.rgb(207, 241, 116);

	public static boolean scale = true;
	public static boolean scaleDefault = true;

	public static String LANDSCAPE = "1";
	public static String PORTRAIT = "2";
	public static String SWITCH = "3";
	public static boolean orientation = true;
	public static String orientationDefault = SWITCH;

	public static boolean username = true;
	public static String usernameDefault = "Default";

	public static boolean withoutSettings() {
		return !scale && !speed && !orientation && !username;
	}

}
