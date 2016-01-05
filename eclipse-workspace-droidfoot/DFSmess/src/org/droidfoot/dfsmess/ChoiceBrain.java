package org.droidfoot.dfsmess;
 

import greenfoot.Greenfoot;
import greenfoot.MouseInfo;

public final class ChoiceBrain extends Piece {

	int staerke;

	public ChoiceBrain(boolean isA, int staerke) {
		super(isA);
		this.staerke = staerke;
		setImage("choicebrain-" + getColor() + "-" + staerke + ".gif");
	} // staerke = 0 --> human

	String getKuerzel() {
		return "choicebrain";
	}

	public void act() {
		if (Greenfoot.mouseDragged(this)) {
			if (!scaled) {
				board.setFront(this);
				scaled = true;
				scale();
			}
			MouseInfo info = Greenfoot.getMouseInfo();
			toSpace = board.getFeld(info.getY(), info.getX());
			if (toSpace != null) {
				setLocation(info.getX(), info.getY());
			}
		} else if (Greenfoot.mouseDragEnded(this)) {
			this.getImage().scale(origWidth, origHeight);
			scaled = false;
			if (isA && toSpace != null && toSpace.getReihe() == 1
					&& toSpace.getSpalte() == 'd') {
				board.setPlayerAChoice(staerke);
			} else if (!isA && toSpace != null && toSpace.getReihe() == 8
					&& toSpace.getSpalte() == 'd') {
				board.setPlayerBChoice(staerke);
			} else {
				setLocation(space.getWorldColumn(), space.getWorldRow());
			}
		}
	}
}
