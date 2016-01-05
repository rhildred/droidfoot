package org.droidfoot.dfsmess;

public class DiboNinny extends DiboFigur {

	static char A_SYMBOL = 'Y';

	static char B_SYMBOL = 'y';

	DiboNinny(boolean isA) {
		super(isA);
	}
	
	DiboNinny(DiboNinny ninny) {
		super(ninny);
	}

	protected Object clone() {
		return new DiboNinny(this);
	}

	public String toString() {
		return "" + (this.isA ? A_SYMBOL : B_SYMBOL);
	}

}
