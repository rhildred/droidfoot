package org.droidfoot.dfsmess;
 

public final class Brain extends Piece {

	static char A_SYMBOL = 'B';

	static char B_SYMBOL = 'b';

	Brain(boolean isA) {
		super(isA);
		setImage("brain-" + getColor() + ".gif");
	}

	Brain(Brain brain) {
		super(brain);
		setImage("brain-" + getColor() + ".gif");
	}

	protected Object clone() {
		return new Brain(this);
	}

	public String toString() {
		return "" + (this.isA ? A_SYMBOL : B_SYMBOL);
	}
	
	String getKuerzel() {
		return "brain";
	}

}
