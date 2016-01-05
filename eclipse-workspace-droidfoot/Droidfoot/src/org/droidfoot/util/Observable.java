package org.droidfoot.util;

import java.util.ArrayList;

public class Observable {

	private ArrayList<Observer> observers;
	private boolean notification;

	public Observable() {
		this.observers = new ArrayList<Observer>();
		this.notification = true;
	}

	public void addObserver(Observer o) {
		this.observers.add(o);
	}

	public void deleteObserver(Observer o) {
		this.observers.remove(o);
	}

	public void deleteObservers() {
		this.observers.clear();
	}

	public ArrayList<Observer> getObservers() {
		return this.observers;
	}

	public void activateNotification() {
		this.notification = true;
		this.notifyObservers();
	}

	public void deactivateNotification() {
		this.notification = false;
	}

	public void notifyObservers() {
		this.notifyObservers(null);
	}

	public void notifyObservers(Object args) {
		if (notification) {
			for (Observer o : this.observers) {
				o.update(this, args);
			}
		}
	}

}
