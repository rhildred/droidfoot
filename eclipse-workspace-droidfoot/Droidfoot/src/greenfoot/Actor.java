/*
 This file is part of the Greenfoot program. 
 Copyright (C) 2005-2009,2010,2011,2013  Poul Henriksen and Michael Kolling 
 
 This program is free software; you can redistribute it and/or 
 modify it under the terms of the GNU General Public License 
 as published by the Free Software Foundation; either version 2 
 of the License, or (at your option) any later version. 
 
 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 GNU General Public License for more details. 
 
 You should have received a copy of the GNU General Public License 
 along with this program; if not, write to the Free Software 
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA. 
 
 This file is subject to the Classpath exception as provided in the  
 LICENSE.txt file that accompanied this code.
 */

package greenfoot;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;

/**
 * An Actor is an object that exists in the Greenfoot world. Every Actor has a
 * location in the world, and an appearance (that is: an icon).
 * 
 * <p>
 * An Actor is not normally instantiated, but instead used as a superclass to
 * more specific objects in the world. Every object that is intended to appear
 * in the world must extend Actor. Subclasses can then define their own
 * appearance and behaviour.
 * 
 * <p>
 * One of the most important aspects of this class is the 'act' method. This
 * method is called when the 'Act' or 'Run' buttons are activated in the
 * Greenfoot interface. The method here is empty, and subclasses normally
 * provide their own implementations.
 * 
 * <h2>Droidfoot</h2>
 * The class Actor for Droidfoot differs from the original Greenfoot-class Actor
 * in the following aspects:
 * <ul>
 * <li>You have always to call setImage for an Actor-object
 * </ul>
 * 
 * 
 * @author Poul Henriksen
 * @version 2.5
 * 
 * @author Dietrich Boles (Modifications for Android)
 * @version 2.0
 */
public class Actor {

	GreenfootImage image;
	int rotation;
	World world;
	int x;
	int y;

	private static int sequenceNumber = 0;
	private int mySequenceNumber;
	private int lastPaintSequenceNumber;

	/**
	 * Construct an Actor. The object will have a default image.
	 */
	public Actor() {
		this.world = null;
		this.rotation = 0;
		this.x = -1;
		this.y = -1;
		this.image = new GreenfootImage(1, 1);
		mySequenceNumber = sequenceNumber++;
		lastPaintSequenceNumber = 0;
	}

	/**
	 * The act method is called by the greenfoot framework to give actors a
	 * chance to perform some action. At each action step in the environment,
	 * each object's act method is invoked, in unspecified order.
	 * 
	 * <p>
	 * The default implementation does nothing. This method should be overridden
	 * in subclasses to implement an actor's action.
	 */
	public void act() {

	}

	/**
	 * Returns the image used to represent this actor. This image can be
	 * modified to change the actor's appearance.
	 * 
	 * @return The object's image.
	 */
	public GreenfootImage getImage() {
		return this.image;
	}

	/**
	 * Return the current rotation of this actor. Rotation is expressed as a
	 * degree value, range (0..359). Zero degrees is towards the east
	 * (right-hand side of the world), and the angle increases clockwise.
	 * 
	 * @see #setRotation(int)
	 * 
	 * @return The rotation in degrees.
	 */
	public int getRotation() {
		return this.rotation;
	}

	/**
	 * Return the world that this actor lives in.
	 * 
	 * @return The world.
	 */
	public World getWorld() {
		return this.world;
	}

	/**
	 * Return the x-coordinate of the actor's current location. The value
	 * returned is the horizontal index of the actor's cell in the world.
	 * 
	 * @return The x-coordinate of the object's current location.
	 * @throws IllegalStateException
	 *             If the actor has not been added into a world.
	 */
	public int getX() throws IllegalStateException {
		this.failIfNotInWorld();
		return this.x;
		// return this.world.getX(this);
	}

	/**
	 * Return the y-coordinate of the object's current location. The value
	 * returned is the vertical index of the actor's cell in the world.
	 * 
	 * @return The y-coordinate of the actor's current location
	 * @throws IllegalStateException
	 *             If the actor has not been added into a world.
	 */
	public int getY() {
		this.failIfNotInWorld();
		return this.y;
		// return this.world.getY(this);
	}

	/**
	 * Move this actor the specified distance in the direction it is currently
	 * facing.
	 * 
	 * <p>
	 * The direction can be set using the {@link #setRotation(int)} method.
	 * 
	 * @param distance
	 *            The distance to move (in cell-size units); a negative value
	 *            will move backwards
	 * 
	 * @see #setLocation(int, int)
	 */
	public void move(int distance) {
		double radians = Math.toRadians(this.rotation);

		// We round to the nearest integer, to allow moving one unit at an angle
		// to actually move.
		int dx = (int) Math.round(Math.cos(radians) * distance);
		int dy = (int) Math.round(Math.sin(radians) * distance);
		this.setLocation(this.x + dx, this.y + dy);
	}

	/**
	 * Set the image for this actor to the specified image.
	 * 
	 * @see #setImage(String)
	 * @param image
	 *            The image.
	 */
	public void setImage(GreenfootImage image) {
		this.image = image;
	}

	/**
	 * Set an image for this actor from an image file. The file may be in jpeg,
	 * gif or png format. The file should be located in the project directory.
	 * 
	 * @param filename
	 *            The name of the image file.
	 * @throws IllegalArgumentException
	 *             If the image can not be loaded.
	 */
	public void setImage(String filename) throws IllegalArgumentException {
		setImage(new GreenfootImage(filename));
	}

	/**
	 * Assign a new location for this actor. This moves the actor to the
	 * specified location. The location is specified as the coordinates of a
	 * cell in the world.
	 * 
	 * <p>
	 * If this method is overridden it is important to call this method as
	 * "super.setLocation(x,y)" from the overriding method, to avoid infinite
	 * recursion.
	 * 
	 * @param x
	 *            Location index on the x-axis
	 * @param y
	 *            Location index on the y-axis
	 * 
	 * @see #move(int)
	 */
	public void setLocation(int x, int y) {
		if (world != null) {
			if (world.isBounded()) {
				this.x = limitValue(x, world.width);
				this.y = limitValue(y, world.height);
			} else {
				this.x = x;
				this.y = y;
			}
		}
	}

	/**
	 * Set the rotation of this actor. Rotation is expressed as a degree value,
	 * range (0..359). Zero degrees is to the east (right-hand side of the
	 * world), and the angle increases clockwise.
	 * 
	 * @param rotation
	 *            The rotation in degrees.
	 * 
	 * @see #turn(int)
	 */
	public void setRotation(int rotation) {
		// First normalize
		if (rotation >= 360) {
			// Optimize the usual case: rotation has adjusted to a value greater
			// than
			// 360, but is still within the 360 - 720 bound.
			if (rotation < 720) {
				rotation -= 360;
			} else {
				rotation = rotation % 360;
			}
		} else if (rotation < 0) {
			// Likwise, if less than 0, it's likely that the rotation was
			// reduced by
			// a small amount and so will be >= -360.
			if (rotation >= -360) {
				rotation += 360;
			} else {
				rotation = 360 + rotation % 360;
			}
		}

		if (this.rotation != rotation) {
			this.rotation = rotation;
		}
	}

	/**
	 * Turn this actor by the specified amount (in degrees).
	 * 
	 * @param amount
	 *            the number of degrees to turn; positive values turn clockwise
	 * 
	 * @see #setRotation(int)
	 */
	public void turn(int amount) {
		this.setRotation(this.rotation + amount);
	}

	/**
	 * Turn this actor to face towards a certain location.
	 * 
	 * @param x
	 *            The x-coordinate of the cell to turn towards
	 * @param y
	 *            The y-coordinate of the cell to turn towards
	 */
	public void turnTowards(int x, int y) {
		double a = Math.atan2(y - this.y, x - this.x);
		this.setRotation((int) Math.toDegrees(a));
	}

	/**
	 * This method is called by the Greenfoot system when this actor has been
	 * inserted into the world. This method can be overridden to implement
	 * custom behaviour when the actor is inserted into the world.
	 * <p>
	 * The default implementation does nothing.
	 * 
	 * @param world
	 *            The world the object was added to.
	 */
	protected void addedToWorld(World world) {
	}

	/**
	 * Return all the objects that intersect this object. This takes the
	 * graphical extent of objects into consideration. <br>
	 * 
	 * @param cls
	 *            Class of objects to look for (passing 'null' will find all
	 *            objects).
	 */
	@SuppressWarnings("rawtypes")
	protected List getIntersectingObjects(Class cls) {
		this.failIfNotInWorld();
		ArrayList<Actor> actors = new ArrayList<Actor>();
		for (Actor actor : this.world.getAllActors()) {
			if (this != actor && (cls == null || cls.isInstance(actor))
					&& this.intersects(actor)) {
				actors.add(actor);
			}
		}
		return actors;
	}

	/**
	 * Return the neighbours to this object within a given distance. This method
	 * considers only logical location, ignoring extent of the image. Thus, it
	 * is most useful in scenarios where objects are contained in a single cell.
	 * <p>
	 * 
	 * All cells that can be reached in the number of steps given in 'distance'
	 * from this object are considered. Steps may be only in the four main
	 * directions, or may include diagonal steps, depending on the 'diagonal'
	 * parameter. Thus, a distance/diagonal specification of (1,false) will
	 * inspect four cells, (1,true) will inspect eight cells.
	 * <p>
	 * 
	 * @param distance
	 *            Distance (in cells) in which to look for other objects.
	 * @param diagonal
	 *            If true, include diagonal steps.
	 * @param cls
	 *            Class of objects to look for (passing 'null' will find all
	 *            objects).
	 * @return A list of all neighbours found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List getNeighbours(int distance, boolean diagonal, Class cls) {
		this.failIfNotInWorld();
		ArrayList<Actor> actors = new ArrayList<Actor>();

		for (int c = this.x - distance; c <= this.x + distance; c++) {
			for (int r = this.y - distance; r <= this.y + distance; r++) {
				if (c >= 0 && c < this.world.getWidth() && r >= 0
						&& r < this.world.getHeight()) {
					if (diagonal || c == this.x || r == this.y) {
						List<Actor> a = this.world.getObjectsAtCell(c, r, cls);
						actors.addAll(a);
					}
				}
			}
		}

		actors.remove(this);
		return actors;
	}

	/**
	 * Return all objects that intersect the center of the given location
	 * (relative to this object's location). <br>
	 * 
	 * @return List of objects at the given offset. The list will include this
	 *         object, if the offset is zero.
	 * @param dx
	 *            X-coordinate relative to this objects location.
	 * @param dy
	 *            y-coordinate relative to this objects location.
	 * @param cls
	 *            Class of objects to look for (passing 'null' will find all
	 *            objects).
	 */
	@SuppressWarnings({ "rawtypes" })
	protected List getObjectsAtOffset(int dx, int dy, Class cls) {
		failIfNotInWorld();
		return world.getObjectsAt(x + dx, y + dy, cls);
	}

	/**
	 * Return all objects within range 'radius' around this object. An object is
	 * within range if the distance between its centre and this object's centre
	 * is less than or equal to 'radius'.
	 * 
	 * @param radius
	 *            Radius of the circle (in cells)
	 * @param cls
	 *            Class of objects to look for (passing 'null' will find all
	 *            objects).
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List getObjectsInRange(int radius, Class cls) {
		this.failIfNotInWorld();
		ArrayList<Actor> actors = new ArrayList<Actor>();

		Point tCenter = world.getCellCenterInPixels(this.x, this.y);

		for (int c = 0; c < this.world.getWidth(); c++) {
			for (int r = 0; r < this.world.getHeight(); r++) {
				List<Actor> acts = this.world.getObjectsAtCell(c, r, cls);
				Point aCenter = world.getCellCenterInPixels(c, r);

				int dx = (tCenter.x - aCenter.x) * (tCenter.x - aCenter.x);
				int dy = (tCenter.y - aCenter.y) * (tCenter.y - aCenter.y);

				double dist = Math.sqrt(dx + dy);
				if (dist <= radius) {
					actors.addAll(acts);
				}
			}
		}
		actors.remove(this);
		return actors;
	}

	/**
	 * Return an object that intersects this object. This takes the graphical
	 * extent of objects into consideration. <br>
	 * 
	 * @param cls
	 *            Class of objects to look for (passing 'null' will find all
	 *            objects).
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Actor getOneIntersectingObject(Class cls) {
		this.failIfNotInWorld();
		List<Actor> actors = this.getIntersectingObjects(cls);
		if (actors.size() == 0) {
			return null;
		} else {
			return actors.get(0);
		}
	}

	/**
	 * Return one object that is located at the specified cell (relative to this
	 * objects location). Objects found can be restricted to a specific class
	 * (and its subclasses) by supplying the 'cls' parameter. If more than one
	 * object of the specified class resides at that location, one of them will
	 * be chosen and returned.
	 * 
	 * @param dx
	 *            X-coordinate relative to this objects location.
	 * @param dy
	 *            y-coordinate relative to this objects location.
	 * @param cls
	 *            Class of objects to look for (passing 'null' will find all
	 *            objects).
	 * @return An object at the given location, or null if none found.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Actor getOneObjectAtOffset(int dx, int dy, Class cls) {
		this.failIfNotInWorld();
		List<Actor> actors = this.getObjectsAtOffset(dx, dy, cls);
		if (actors.size() == 0) {
			return null;
		} else {
			return actors.get(0);
		}
	}

	/**
	 * Check whether this object intersects with another given object.
	 * 
	 * @return True if the object's intersect, false otherwise.
	 */
	protected boolean intersects(Actor other) {
		this.failIfNotInWorld();
		Rect tr = this.getWorldCoordsInPixels();
		Rect or = other.getWorldCoordsInPixels();
		return Rect.intersects(tr, or);
	}

	/**
	 * Checks whether this actor is touching any other objects of the given
	 * class.
	 * 
	 * @param cls
	 *            Class of objects to look for (passing 'null' will check for
	 *            all actors).
	 */
	@SuppressWarnings("rawtypes")
	protected boolean isTouching(Class cls) {
		failIfNotInWorld();
		return getOneIntersectingObject(cls) != null;
	}

	/**
	 * Removes one object of the given class that this actor is currently
	 * touching (if any exist).
	 * 
	 * @param cls
	 *            Class of objects to remove (passing 'null' will remove any
	 *            actor).
	 */
	@SuppressWarnings("rawtypes")
	protected void removeTouching(Class cls) {
		this.failIfNotInWorld();
		Actor a = this.getOneIntersectingObject(cls);
		if (a != null) {
			this.world.removeObject(a);
		}
	}

	/**
	 * Sets the world, and the initial location. The location is adjusted
	 * according to the world's bounding rules. The cached collision checking
	 * bounds, if any, are cleared.
	 */
	void addToWorld(int x, int y, World world) {
		if (world.isBounded()) {
			x = limitValue(x, world.getWidth());
			y = limitValue(y, world.getHeight());
		}

		this.x = x;
		this.y = y;

		this.setWorld(world);

		// This call is not necessary, however setLocation may be overridden
		// so it must still be called. (Asteroids scenario relies on setLocation
		// being called when the object is added to the world...)
		this.setLocation(x, y);
	}
    
    public boolean atEdge(){
        World world = this.getWorld();
        return(x <= 0 || x >= world.getWidth() || y <= 0 || y >= world.getHeight());
    }

	/**
	 * Checks whether the specified point (specified in pixel co-ordinates) is
	 * within the area covered by the graphical representation of this actor.
	 * 
	 * @param px
	 *            The (world relative) x pixel co-ordinate
	 * @param py
	 *            The (world relative) y pixel co-ordinate
	 * @return true if the pixel is within the actor's bounds; false otherwise
	 */
	boolean containsPoint(int px, int py) {
		Rect rect = getWorldCoordsInPixels();
		return rect.contains(px, py);

	}

	/**
	 * Get the sequence number of this actor from the last paint operation.
	 * (Returns whatever was set using the setLastPaintSeqNum method).
	 */
	final int getLastPaintSeqNum() {
		return lastPaintSequenceNumber;
	}

	/**
	 * Get the sequence number of this actor. This can be used as a hash value,
	 * which is not overridable by the user.
	 */
	final int getSequenceNumber() {
		return mySequenceNumber;
	}

	Rect getWorldCoordsInPixels() {
		if (rotation != 0) {
			Matrix matrix = new Matrix();
			int width = image.getWidth();
			int height = image.getHeight();
			matrix.setRotate(rotation, width / 2, height / 2);
			Bitmap dummyImage = Bitmap.createBitmap(image.getAwtImage(), 0, 0,
					width, height, matrix, true);
			// dibo: bei einer Rotation von bspw. 70 Grad entsteht eine Bitmap
			// mit einer gr&#xfffd;&#xfffd;eren Gr&#xfffd;&#xfffd;e als width und height: passt ideal!
			if (dummyImage.getWidth() > width
					|| dummyImage.getHeight() > height) {
				Point center = world.getCellCenterInPixels(x, y);
				int ox = Math.max(0, center.x - dummyImage.getWidth() / 2);
				int oy = Math.max(0, center.y - dummyImage.getHeight() / 2);
				int w = ox + dummyImage.getWidth() <= world.getWidthInPixels() ? dummyImage
						.getWidth() : world.getWidthInPixels() - ox;
				int h = oy + dummyImage.getHeight() <= world
						.getHeightInPixels() ? dummyImage.getHeight() : world
						.getHeightInPixels() - oy;
				return new Rect(ox, oy, ox + w, oy + h);
			}
		}

		Point center = world.getCellCenterInPixels(x, y);
		int ox = Math.max(0, center.x - image.getWidth() / 2);
		int oy = Math.max(0, center.y - image.getHeight() / 2);
		int w = ox + image.getWidth() <= world.getWidthInPixels() ? image
				.getWidth() : world.getWidthInPixels() - ox;
		int h = oy + image.getHeight() <= world.getHeightInPixels() ? image
				.getHeight() : world.getHeightInPixels() - oy;
		return new Rect(ox, oy, ox + w, oy + h);

	}

	/**
	 * Set the sequence number of this actor from the last paint operation.
	 */
	final void setLastPaintSeqNum(int num) {
		lastPaintSequenceNumber = num;
	}

	void setWorld(World world) {
		this.world = world;
	}

	private void failIfNotInWorld() {
		if (this.world == null) {
			throw new IllegalStateException();
		}
	}

	/**
	 * Limits the value v to be less than limit and large or equal to zero.
	 */
	private int limitValue(int v, int limit) {
		if (v < 0) {
			v = 0;
		}
		if (limit <= v) {
			v = limit - 1;
		}
		return v;
	}

}
