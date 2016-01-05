package org.droidfoot.simulation;

import greenfoot.Greenfoot;
import greenfoot.GreenfootVisitor;

import org.droidfoot.util.Observable;


public class SimulationManager extends Observable {

	public static final String NOTIFY_FINISHED = "FINISHED";
	public static final String NOTIFY_SPEED = "SPEED";

	public static Object syncObject = new Object();

	private static SimulationManager manager = new SimulationManager();

	public static SimulationManager getManager() {
		return manager;
	}

	private volatile SimulationState state;
	private volatile SimulationThread thread;

	private SimulationManager() {
		state = SimulationState.NOT_STARTED;
		thread = null;
	}

	public void onceCalled() {
		state = SimulationState.CANCELED;
	}

	public void setInterruption() {
		if (thread != null) {
			thread.interrupt();
		}
	}

	public int getSpeed() {
		return GreenfootVisitor.getSpeed();
	}

	public void setSpeed(int newSpeed) {
		Greenfoot.setSpeed(newSpeed);
	}

	public void setGFSpeed(int newSpeed) {
		this.notifyObservers(NOTIFY_SPEED);
	}

	public SimulationState getState() {
		return state;
	}

	public void setFinished() {
		state = SimulationState.FINISHED;
		this.notifyObservers(NOTIFY_FINISHED);
	}

	public void start() {
		thread = new SimulationThread();
		state = SimulationState.RUNNING;
		thread.start();
		this.notifyObservers();
	}

	public void stop() {
		state = SimulationState.CANCELED;
		if (thread == null) {
			return;
		}
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
		}
		this.notifyObservers();
	}

	public void stopGF() {
		state = SimulationState.CANCELED;
		thread.interrupt();
	}

	public void halt() {
		state = SimulationState.HALTED;
		thread.interrupt();
		this.notifyObservers();
	}

	public void resume() {
		state = SimulationState.RUNNING;
		synchronized (syncObject) {
			syncObject.notify();
		}
		this.notifyObservers();
	}

}
