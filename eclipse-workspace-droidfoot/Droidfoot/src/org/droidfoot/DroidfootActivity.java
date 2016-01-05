package org.droidfoot;

import greenfoot.GreenfootVisitor;
import greenfoot.SoundManager;
import greenfoot.World;
import greenfoot.WorldManager;

import org.droidfoot.gui.DrawPanel;
import org.droidfoot.simulation.SimulationManager;
import org.droidfoot.simulation.SimulationState;
import org.droidfoot.util.Observable;
import org.droidfoot.util.Observer;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnActionExpandListener;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DroidfootActivity extends Activity implements Observer,
		OnSharedPreferenceChangeListener {

	private MenuItem startStopItem;
	private MenuItem speedButton;
	private SeekBar speedSlider;

	public boolean scalePref;
	private String orientationPref;
	public String username;

	public static Class<? extends World> worldClass = null;

	public static DroidfootActivity dfActivity = null;

	public DroidfootActivity() {
		super();
		startStopItem = null;
		dfActivity = this;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (SimulationManager.getManager().getState() == SimulationState.HALTED) {
			SimulationManager.getManager().resume();
		}
		SoundManager.resume();
	}

	@Override
	public void onStop() {
		super.onStop();
		if (SimulationManager.getManager().getState() == SimulationState.RUNNING) {
			SimulationManager.getManager().halt();
		}
		GreenfootVisitor.startFlag = false;
		SoundManager.pause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handlePreferences();

		if (SimulationManager.getManager().getState() == SimulationState.NOT_STARTED) {
			WorldManager.setContext(this);
			WorldManager.initWorld(worldClass);
			SimulationManager.getManager().onceCalled();
		}

		setContentView(R.layout.activity_droidfoot);

		SimulationManager.getManager().addObserver(this);

		if (SimulationManager.getManager().getState() == SimulationState.HALTED) {
			SimulationManager.getManager().resume();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SimulationManager.getManager().deleteObserver(this);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.menu_droidfoot, menu);

		if (Settings.withoutSettings()) {
			menu.removeItem(R.id.settings);
		}

		if (!Settings.help) {
			menu.removeItem(R.id.help);
		}

		startStopItem = menu.findItem(R.id.start_stop);
		switch (SimulationManager.getManager().getState()) {
		case FINISHED:
		case NOT_STARTED:
		case CANCELED:
			startStopItem.setIcon(R.drawable.start);
			startStopItem.setTitle(R.string.start);
			break;
		case RUNNING:
		case HALTED:
			startStopItem.setIcon(R.drawable.stop);
			startStopItem.setTitle(R.string.stop);
			break;
		}

		if (!Settings.speed) {
			menu.removeItem(R.id.speed);
		} else {
			handleSpeed(menu);
		}

		handleGFStart();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == android.R.id.home) {
			return true;
		} else if (id == R.id.speed) {
			return true;
		} else if (id == R.id.start_stop) {
			handleStartStop(item);
			return true;
		} else if (id == R.id.reset) {
			handleReset();
			return true;
		} else if (id == R.id.help) {
			Intent intentH = new Intent(this, HelpActivity.class);
			startActivity(intentH);
			return true;
		} else if (id == R.id.settings) {
			Intent intentS = new Intent(this, SettingsActivity.class);
			startActivity(intentS);
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}

	}

	private void handleStartStop(MenuItem item) {
		switch (SimulationManager.getManager().getState()) {
		case FINISHED:
		case NOT_STARTED:
		case CANCELED:
			SimulationManager.getManager().start();
			item.setIcon(R.drawable.stop);
			item.setTitle(R.string.stop);
			break;
		case RUNNING:
			SimulationManager.getManager().stop();
			item.setIcon(R.drawable.start);
			item.setTitle(R.string.start);
			break;
		case HALTED:
			break;
		}
	}

	public void handleGFStart() {
		if (GreenfootVisitor.startFlag) {
			GreenfootVisitor.startFlag = false;
			switch (SimulationManager.getManager().getState()) {
			case FINISHED:
			case NOT_STARTED:
			case CANCELED:
				SimulationManager.getManager().start();
				startStopItem.setIcon(R.drawable.stop);
				startStopItem.setTitle(R.string.stop);
				break;
			default:
			}
		}
	}

	public void handleGFStop() {
		switch (SimulationManager.getManager().getState()) {
		case RUNNING:
			SimulationManager.getManager().stopGF();
			startStopItem.setIcon(R.drawable.start);
			startStopItem.setTitle(R.string.start);
			break;
		default:
		}
		GreenfootVisitor.stopFlag = false;
	}

	private void handleReset() {
		SimulationManager.getManager().stop();
		SoundManager.stop();
		startStopItem.setIcon(R.drawable.start);
		startStopItem.setTitle(R.string.start);
		WorldManager.reset(worldClass);
		handleGFStart();
	}

	@Override
	public void update(Observable observable, Object args) {
		if (args != null && args instanceof String
				&& ((String) args).equals(SimulationManager.NOTIFY_SPEED)) {
			if (speedSlider != null) {
				speedSlider.setProgress(SimulationManager.getManager()
						.getSpeed());
			}
		} else if (SimulationManager.getManager().getState() == SimulationState.FINISHED
				&& args != null
				&& args instanceof String
				&& ((String) args).equals(SimulationManager.NOTIFY_FINISHED)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					startStopItem.setIcon(R.drawable.start);
					startStopItem.setTitle(R.string.start);
				}
			});
		}
	}

	private void handlePreferences() {
		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		sp.registerOnSharedPreferenceChangeListener(this);

		scalePref = sp.getBoolean("scale", Settings.scaleDefault);
		orientationPref = sp.getString("orientation",
				Settings.orientationDefault);
		handleOrientation();
		username = sp.getString("username", Settings.usernameDefault);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals("scale")) {
			scalePref = sharedPreferences.getBoolean("scale",
					Settings.scaleDefault);
			if (DrawPanel.canvas != null) {
				DrawPanel.canvas.repaintStage();
			}
		} else if (key.equals("orientation")) {
			orientationPref = sharedPreferences.getString("orientation",
					Settings.orientationDefault);
			handleOrientation();
		} else if (key.equals("username")) {
			username = sharedPreferences.getString("username",
					Settings.usernameDefault);
		}

	}

	private void handleOrientation() {
		if (orientationPref.equals("1")) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		} else if (orientationPref.equals("2")) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
		}
	}

	private void handleSpeed(Menu menu) {
		speedButton = menu.findItem(R.id.speed);
		speedSlider = (SeekBar) menu.findItem(R.id.speed).getActionView();
		LayoutParams params = new ActionBar.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		speedSlider.setLayoutParams(params);
		speedSlider.setMax(GreenfootVisitor.MAX_SIMULATION_SPEED);
		speedSlider.setProgress(SimulationManager.getManager().getSpeed());
		speedSlider.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				SimulationManager.getManager().setSpeed(progress);
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				SimulationManager.getManager().setInterruption();
			}
		});

		speedButton.setOnActionExpandListener(new OnActionExpandListener() {
			@Override
			public boolean onMenuItemActionCollapse(MenuItem item) {
				return true;
			}

			@Override
			public boolean onMenuItemActionExpand(MenuItem item) {
				return true;
			}
		});
	}
}
