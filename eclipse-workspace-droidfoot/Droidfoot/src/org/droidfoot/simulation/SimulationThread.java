package org.droidfoot.simulation;

import greenfoot.Actor;
import greenfoot.GreenfootVisitor;
import greenfoot.WorldManager;

import org.droidfoot.gui.DrawPanel;

public class SimulationThread extends Thread {

	public static SimulationThread instance;

	public SimulationThread() {
		super();
		SimulationThread.instance = this;
	}

	@Override
	public void run() {
		WorldManager.world.started();
		while ((SimulationManager.getManager().getState() == SimulationState.RUNNING || SimulationManager
				.getManager().getState() == SimulationState.HALTED)) {
			DrawPanel.canvas.mouseListener.newActStarted();
			try {
				WorldManager.world.act();
				checkInterruption();
				if (!WorldManager.worldSet) {
					for (Actor actor : WorldManager.getObjectsInActOrder()) {
						if (actor.getWorld() != null) {
							actor.act();
							checkInterruption();
							if (WorldManager.worldSet) {
								break;
							}
						}
					}
				}
			} catch (CancelException exc) {
				WorldManager.world.stopped();
				DrawPanel.canvas.repaintStage();
				return;
			} catch (Throwable exc) {
				throw new RuntimeException(exc);
			}

			try {
				if (WorldManager.worldSet) {
					WorldManager.worldSet = false;
					DrawPanel.canvas.initBitmap();
				}
				repaintStage();
				this.sleep();
			} catch (Exception exc) {
				this.interrupt();
			}
			try {
				checkInterruption();
			} catch (CancelException e) {
				WorldManager.world.stopped();
				DrawPanel.canvas.repaintStage();
				return;
			}
		}
		WorldManager.world.stopped();
		DrawPanel.canvas.repaintStage();
	}

	private void repaintStage() {
		synchronized (DrawPanel.syncObject) {
			DrawPanel.canvas.repaintStage();
			try {
				DrawPanel.syncObject.wait();
			} catch (InterruptedException e) {
				this.interrupt();
			}
		}
	}

	/**
	 * Returns the delay as a function of the speed.
	 * 
	 * @return The delay in nanoseconds.
	 */
	private long calculateDelay(int speed) {

		// Make the speed into a delay
		long rawDelay = GreenfootVisitor.MAX_SIMULATION_SPEED - speed;

		long min = 30 * 1000L; // Delay at MAX_SIMULATION_SPEED - 1
		long max = 10000 * 1000L * 1000L; // Delay at slowest speed

		double a = Math.pow(max / (double) min,
				1D / (GreenfootVisitor.MAX_SIMULATION_SPEED - 1));
		long delay = 0;
		if (rawDelay > 0) {
			delay = (long) (Math.pow(a, rawDelay - 1) * min);
		}
		return delay / 1000000;
	}

	public void sleep() {
		try {
			long millis = this.calculateDelay(GreenfootVisitor.getSpeed());
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			this.interrupt();
		}
	}

	private void checkInterruption() throws CancelException {
		while (Thread.interrupted()) {
			if (SimulationManager.getManager().getState() == SimulationState.HALTED) {
				try {
					synchronized (SimulationManager.syncObject) {
						SimulationManager.syncObject.wait();
					}
				} catch (InterruptedException e) {
					interrupt();
				}
			} else if (SimulationManager.getManager().getState() == SimulationState.CANCELED) {
				throw new CancelException();
			}
		}
	}
}

class CancelException extends Exception {

	private static final long serialVersionUID = 1L;

}
