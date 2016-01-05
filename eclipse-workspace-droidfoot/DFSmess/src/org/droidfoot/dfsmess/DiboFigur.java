package org.droidfoot.dfsmess;

public abstract class DiboFigur {

	boolean isA;

	DiboFigur(boolean isA) {
		this.isA = isA;
	}
	
	DiboFigur(DiboFigur figur) {
		this.isA = figur.isA;
	}
	
	protected abstract Object clone();

	public boolean isA() {
		return this.isA;
	}

}
