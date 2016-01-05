package org.droidfoot.dfsmess;

public class DiboBrain extends DiboFigur {

	static char A_SYMBOL = 'B';

	static char B_SYMBOL = 'b';

	DiboBrain(boolean isA) {
		super(isA);
	}

	DiboBrain(DiboBrain brain) {
		super(brain);
	}

	protected Object clone() {
		return new DiboBrain(this);
	}

	public String toString() {
		return "" + (this.isA ? A_SYMBOL : B_SYMBOL);
	}

}
