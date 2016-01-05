package org.droidfoot.dfsmess;
 

import greenfoot.Actor;
import greenfoot.GreenfootImage;
import greenfoot.World;
import greenfoot.awt.Color;
import greenfoot.awt.Font;

public class Info extends Actor {

	private String winner, matter;
	private Color color;

	public Info(String winner, String matter, Color color) {
		this.winner = winner;
		this.matter = matter;
		this.color = color;
	}

	protected void addedToWorld(World world) {
		makeImage();
	}

	private void makeImage() {
		int WIDTH = (getWorld().getWidth() - 2) * getWorld().getCellSize();
		int HEIGHT = (getWorld().getHeight() - 5) * getWorld().getCellSize();
		GreenfootImage image = new GreenfootImage(WIDTH, HEIGHT);

		image.setColor(new Color(0, 0, 0, 100));
		image.fillRect(0, 0, WIDTH, HEIGHT);
		image.setColor(new Color(255, 255, 255, 160));
		image.fillRect(5, 5, WIDTH - 10, HEIGHT - 10);
		image.setColor(color);
		Font font = new Font("Monospaced", Font.BOLD, 24);
		image.setFont(font);
		image.drawString(winner, image.getWidth() / 2
				- (int) (winner.length() / 2.0 * 14.0), HEIGHT / 3);
		font = new Font("Monospaced", Font.BOLD, 18);
		image.setFont(font);
		image.drawString(matter, image.getWidth() / 2
				- (int) (matter.length() / 2.0 * 11.0), 2 * HEIGHT / 3);
		setImage(image);
	}
}
