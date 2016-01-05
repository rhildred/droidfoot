package org.droidfoot.dfballoons;

import org.droidfoot.DroidfootActivity;
import org.droidfoot.Settings;

import android.graphics.Color;
import android.os.Bundle;

public class DFBalloonsActivity extends DroidfootActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DroidfootActivity.worldClass = BalloonWorld.class;

		// help item available in the menu (true/false)?
		Settings.help = true;

		// speed item and speed slider available in the action bar (true/false)?
		Settings.speed = true;

		// background color of the droidfoot activity view
		Settings.backgroundColor = Color.rgb(207, 241, 116);

		// scale option available in the settings (true/false)?
		Settings.scale = true;
		// if true the world will always cover the whole screen
		Settings.scaleDefault = true;

		// orientation option available in the settings (true/false)?
		Settings.orientation = true;
		// if LANDSCAPE always landscape modus
		// if PORTRAIT always portrait modus
		// if SWITCH automatic change between portrait and landscape modus due
		// to device rotations
		Settings.orientationDefault = Settings.SWITCH; // LANDSCAPE, PORTRAIT,
														// SWITCH

		// username option available in the settings (true/false)?
		Settings.username = false;
		// name for the highscore table
		Settings.usernameDefault = "Default";

		super.onCreate(savedInstanceState);
	}

}
