package org.droidfoot.dfsmess;
 

public final class Numskull extends Piece {

	static char A_SYMBOL = 'N';

	static char B_SYMBOL = 'n';

	Numskull(boolean isA) {
		super(isA);
		setImage("numskull-" + getColor() + ".gif");
	}
	
	Numskull(Numskull numskull) {
		super(numskull);
		setImage("numskull-" + getColor() + ".gif");
	}

	protected Object clone() {
		return new Numskull(this);
	}

	public String toString() {
		return "" + (this.isA ? A_SYMBOL : B_SYMBOL);
	}
	
	String getKuerzel() {
		return "numskull";
	}

}
