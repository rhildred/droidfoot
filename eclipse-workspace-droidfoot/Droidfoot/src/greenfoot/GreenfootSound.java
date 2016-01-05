/*
 This file is part of the Greenfoot program. 
 Copyright (C) 2005-2009,2011  Poul Henriksen and Michael Kolling 
 
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

import org.droidfoot.DroidfootActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

/**
 * Represents audio that can be played in Greenfoot. A GreenfootSound loads the
 * audio from a file. The sound cannot be played several times simultaneously,
 * but can be played several times sequentially.
 * 
 * <p>
 * Most files of the following formats are supported: AIFF, AU, WAV, MP3 and
 * MIDI.
 * 
 * @author Poul Henriksen
 * @version 2.4
 * 
 * @author Dietrich Boles (Modifications for Android)
 * @version 2.0
 */
public class GreenfootSound {

	String filename;
	MediaPlayer mp;
	int volume;
	boolean pause;
	boolean playing;
	boolean looping;

	/**
	 * Creates a new sound from the given file.
	 * 
	 * @param filename
	 *            Typically the name of a file in the sounds directory in the
	 *            project directory.
	 */
	public GreenfootSound(String filename) {
		try {
			this.filename = "sounds/" + filename;
			this.mp = new MediaPlayer();
			this.pause = false;
			this.playing = false;
			this.looping = false;
			AudioManager am = (AudioManager) DroidfootActivity.dfActivity
					.getSystemService(Context.AUDIO_SERVICE);
			int volumeLevel = am.getStreamVolume(AudioManager.STREAM_MUSIC);
			int maxVolumeLevel = am
					.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			this.volume = (int) (100.0 * volumeLevel / maxVolumeLevel);
			AssetFileDescriptor descriptor = WorldManager.context.getAssets()
					.openFd(this.filename);
			mp.setDataSource(descriptor.getFileDescriptor(),
					descriptor.getStartOffset(), descriptor.getLength());
			descriptor.close();
			mp.setOnCompletionListener(new SoundCompletionListener());
		} catch (Exception exc) {
			this.mp = null;
			exc.printStackTrace();
		}
	}

	/**
	 * Get the current volume of the sound, between 0 (off) and 100 (loudest.)
	 */
	public int getVolume() {
		synchronized (this) {
			return this.volume;
		}
	}

	/**
	 * True if the sound is currently playing.
	 * 
	 */
	public boolean isPlaying() {
		synchronized (this) {
			if (this.mp == null) {
				return false;
			}
			return this.playing && !this.pause;
		}
	}

	/**
	 * Pauses the current sound if it is currently playing. If the sound is
	 * played again later, it will resume from the point where it was paused.
	 * <p>
	 * Make sure that this is really the method you want. If possible, you
	 * should always use {@link #stop()}, because the resources can be released
	 * after calling {@link #stop()}. The resources for the sound will not be
	 * released while it is paused.
	 * 
	 * @see #stop()
	 */
	public void pause() {
		synchronized (this) {
			if (mp != null && this.playing) {
				this.mp.pause();
				this.pause = true;
			}
		}
	}

	/**
	 * Start playing this sound. If it is playing already, it will do nothing.
	 * If the sound is currently looping, it will finish the current loop and
	 * stop. If the sound is currently paused, it will resume playback from the
	 * point where it was paused. The sound will be played once.
	 */
	public void play() {
		synchronized (this) {
			if (mp == null) {
				return;
			}
			if (!this.playing) {
				try {
					mp.prepare();
					mp.setLooping(false);
					mp.start();
					SoundManager.addSound(this);
					playing = true;
					pause = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				mp.setLooping(false);
				if (this.pause) {
					this.mp.start();
					this.pause = false;
				}
			}
		}
	}

	/**
	 * Play this sound repeatedly in a loop. If called on an already looping
	 * sound, it will do nothing. If the sound is already playing once, it will
	 * start looping instead. If the sound is currently paused, it will resume
	 * playing from the point where it was paused.
	 */
	public void playLoop() {
		synchronized (this) {
			if (mp == null) {
				return;
			}
			if (!this.playing) {
				try {
					mp.prepare();
					mp.setLooping(true);
					mp.start();
					SoundManager.addSound(this);
					playing = true;
					pause = false;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				mp.setLooping(true);
				if (this.pause) {
					this.mp.start();
					this.pause = false;
				}
			}
		}
	}

	/**
	 * Set the current volume of the sound between 0 (off) and 100 (loudest.)
	 * 
	 * @param level
	 *            the level to set the sound volume to.
	 */
	public void setVolume(int level) {
		synchronized (this) {
			if (level < 0)
				level = 0;
			else if (level > 100)
				level = 100;
			this.volume = level;
			if (mp != null) {
				mp.setVolume(this.volume / 100.0f, this.volume / 100.0f);
			}
		}
	}

	/**
	 * Stop playing this sound if it is currently playing. If the sound is
	 * played again later, it will start playing from the beginning. If the
	 * sound is currently paused it will now be stopped instead.
	 */
	public void stop() {
		synchronized (this) {
			if (mp != null) {
				mp.stop();
				SoundManager.removeSound(this);
			}
			this.pause = false;
			this.playing = false;
		}
	}

	/**
	 * Returns a string representation of this sound containing the name of the
	 * file and whether it is currently playing or not.
	 */
	@Override
	public String toString() {
		String s = super.toString() + " file: " + this.filename + " ";
		s += ". Is playing: " + this.isPlaying();
		return s;
	}

	void resume() {
		synchronized (this) {
			if (mp == null) {
				return;
			}
			if (this.playing && this.pause) {
				this.mp.start();
				this.pause = false;
			}
		}
	}

	class SoundCompletionListener implements OnCompletionListener {
		@Override
		public void onCompletion(MediaPlayer m) {
			synchronized (this) {
				if (mp != null) {
					SoundManager.removeSound(GreenfootSound.this);
				}
				pause = false;
				playing = false;
			}
		}
	}
}
