package org.droidfoot.simulation;

public enum SimulationState {
	NOT_STARTED, // only at the beginning
	RUNNING, // scenario is running
	FINISHED, // scenario stopped itself
	CANCELED, // user pressed the stop button
	HALTED // activity onStop is called (smartphone switch)
}
