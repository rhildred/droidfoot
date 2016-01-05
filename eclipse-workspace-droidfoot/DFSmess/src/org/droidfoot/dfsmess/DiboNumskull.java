package org.droidfoot.dfsmess;

public class DiboNumskull extends DiboFigur {

	static char A_SYMBOL = 'N';

	static char B_SYMBOL = 'n';

	DiboNumskull(boolean isA) {
		super(isA);
	}
	
	DiboNumskull(DiboNumskull numskull) {
		super(numskull);
	}

	protected Object clone() {
		return new DiboNumskull(this);
	}

	public String toString() {
		return "" + (this.isA ? A_SYMBOL : B_SYMBOL);
	}

}
