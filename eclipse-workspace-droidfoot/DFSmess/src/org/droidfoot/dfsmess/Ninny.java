package org.droidfoot.dfsmess;
 

public final class Ninny extends Piece {

	static char A_SYMBOL = 'Y';

	static char B_SYMBOL = 'y';

	Ninny(boolean isA) {
		super(isA);
		setImage("ninny-" + getColor() + ".gif");
	}
	
	Ninny(Ninny ninny) {
		super(ninny);
		setImage("ninny-" + getColor() + ".gif");
	}

	protected Object clone() {
		return new Ninny(this);
	}

	public String toString() {
		return "" + (this.isA ? A_SYMBOL : B_SYMBOL);
	}
	
	String getKuerzel() {
		return "ninny";
	}

}
