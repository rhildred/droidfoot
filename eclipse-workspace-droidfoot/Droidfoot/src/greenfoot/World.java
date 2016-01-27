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

import greenfoot.awt.Color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.droidfoot.gui.DrawPanel;

import android.graphics.Point;
import android.graphics.Rect;


/**
 * World is the world that Actors live in. It is a two-dimensional grid of
 * cells.
 * 
 * <p>
 * All Actor are associated with a World and can get access to the world object.
 * The size of cells can be specified at world creation time, and is constant
 * after creation. Simple scenarios may use large cells that entirely contain
 * the representations of objects in a single cell. More elaborate scenarios may
 * use smaller cells (down to single pixel size) to achieve fine-grained
 * placement and smoother animation.
 * 
 * <p>
 * The world background can be decorated with drawings or images.
 * 
 * <h2>Droidfoot</h2>
 * The class World for Droidfoot differs from the original Greenfoot-class World
 * in the following aspects:
 * <ul>
 * <li>You have always to call setBackground for a World-object
 * <li>You have to define a default-constructor
 * <li>instead of class java.awt.Color class greenfoot.awt.Color is used
 * </ul>
 * 
 * @see greenfoot.Actor
 * @author Poul Henriksen
 * @author Michael Kolling
 * @version 2.4
 * 
 * @author Dietrich Boles (Modifications for Android)
 * @version 2.0
 */
public class World {

	private static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;

	GreenfootImage backgroundImage;
	int cellSize;
	int height;
	int width;
	boolean isBounded;

	// One or two sets can be used to store objects in different orders.
	// Initially only the disordered set will be used, if later we need
	// ordering, the disordered set might be used for an ordered set. If two
	// orderings are used at the same time a new set will be created for the
	// second set.
	private TreeActorSet objectsDisordered = new TreeActorSet();
	private TreeActorSet objectsInPaintOrder;
	private TreeActorSet objectsInActOrder;

	/**
	 * Construct a new world. The size of the world (in number of cells) and the
	 * size of each cell (in pixels) must be specified.
	 * 
	 * @param worldWidth
	 *            The width of the world (in cells).
	 * @param worldHeight
	 *            The height of the world (in cells).
	 * @param cellSize
	 *            Size of a cell in pixels.
	 */
	public World(int worldWidth, int worldHeight, int cellSize) {
		this(worldWidth, worldHeight, cellSize, true);
	}

	/**
	 * Construct a new world. The size of the world (in number of cells) and the
	 * size of each cell (in pixels) must be specified. This constructor allows
	 * the option of creating an unbounded world, which actors can move outside
	 * the boundaries of.
	 * 
	 * @param worldWidth
	 *            The width of the world (in cells).
	 * @param worldHeight
	 *            The height of the world (in cells).
	 * @param cellSize
	 *            Size of a cell in pixels.
	 * @param bounded
	 *            Should actors be restricted to the world boundary?
	 */
	public World(int worldWidth, int worldHeight, int cellSize, boolean bounded) {
		this.width = worldWidth;
		this.height = worldHeight;
		this.cellSize = cellSize;
		this.isBounded = bounded;
		this.backgroundImage = null; // new GreenfootImage(1, 1);
		objectsInPaintOrder = null;
		objectsInActOrder = null;
		GreenfootVisitor.startFlag = false;
	}

	/**
	 * Act method for world. The act method is called by the greenfoot framework
	 * at each action step in the environment. The world's act method is called
	 * before the act method of any objects in the world.
	 * <p>
	 * 
	 * This method does nothing. It should be overridden in subclasses to
	 * implement an world's action.
	 */
	public void act() {

	}

	/**
	 * Add an Actor to the world.
	 * 
	 * @param object
	 *            The new object to add.
	 * @param x
	 *            The x coordinate of the location where the object is added.
	 * @param y
	 *            The y coordinate of the location where the object is added.
	 */
	public void addObject(Actor object, int x, int y) {

		if (object.world != null) {
			if (object.world == this) {
				return; // Actor is already in the world
			}
			object.world.removeObject(object);
		}

		objectsDisordered.add(object);
		addInPaintOrder(object);
		addInActOrder(object);

		// Note we must call this before adding the object to the collision
		// checker,
		// so that the cached bounds are cleared:
		object.addToWorld(x, y, this);

		object.addedToWorld(this);
	}

	/**
	 * Return the world's background image. The image may be used to draw onto
	 * the world's background.
	 * 
	 * @return The background image
	 */
	public GreenfootImage getBackground() {
		if (backgroundImage == null) {
			backgroundImage = new GreenfootImage(getWidthInPixels(),
					getHeightInPixels());
			backgroundImage.setColor(DEFAULT_BACKGROUND_COLOR);
			backgroundImage.fill();
		}

		return backgroundImage;
	}

	/**
	 * Return the size of a cell (in pixels).
	 */
	public int getCellSize() {
		return this.cellSize;
	}

	/**
	 * Return the color at the centre of the cell. To paint a color, you need to
	 * get the background image for the world and paint on that.
	 * 
	 * @param x
	 *            The x coordinate of the cell.
	 * @param y
	 *            The y coordinate of the cell.
	 * @see #getBackground()
	 * @throws IndexOutOfBoundsException
	 *             If the location is not within the world bounds. If there is
	 *             no background image at the location it will return
	 *             Color.WHITE.
	 */
	public Color getColorAt(int x, int y) {

		ensureWithinXBounds(x);
		ensureWithinYBounds(y);

		int xPixel = getCellCenter(x);
		int yPixel = getCellCenter(y);

		if (xPixel >= backgroundImage.getWidth()) {
			return new Color(DEFAULT_BACKGROUND_COLOR);
		}
		if (yPixel >= backgroundImage.getHeight()) {
			return DEFAULT_BACKGROUND_COLOR;
		}

		return backgroundImage.getColorAt(xPixel, yPixel);
	}

	/**
	 * Return the height of the world (in number of cells).
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Get all the objects in the world, or all the objects of a particular
	 * class.
	 * <p>
	 * If a class is specified as a parameter, only objects of that class (or
	 * its subclasses) will be returned.
	 * <p>
	 * 
	 * @param cls
	 *            Class of objects to look for ('null' will find all objects).
	 * 
	 * @return A list of objects.
	 */
	@SuppressWarnings({ "rawtypes" })
	public List getObjects(Class cls) {
		List<Actor> result = new ArrayList<Actor>();

		Iterator<Actor> i = objectsDisordered.iterator();
		while (i.hasNext()) {
			Actor actor = i.next();
			if (cls == null || cls.isInstance(actor)) {
				result.add(actor);
			}
		}

		return result;
	}

	/**
	 * Return all objects at a given cell.
	 * <p>
	 * 
	 * An object is defined to be at that cell if its graphical representation
	 * overlaps the center of the cell.
	 * 
	 * @param x
	 *            X-coordinate of the cell to be checked.
	 * @param y
	 *            Y-coordinate of the cell to be checked.
	 * @param cls
	 *            Class of objects to look return ('null' will return all
	 *            objects).
	 */
	@SuppressWarnings({ "rawtypes" })
	public List getObjectsAt(int x, int y, Class cls) {
		ArrayList<Actor> res = new ArrayList<Actor>();
		Point center = this.getCellCenterInPixels(x, y);
		for (Actor actor : this.objectsDisordered) {
			Rect rect = actor.getWorldCoordsInPixels();
			if ((cls == null || cls.isInstance(actor))
					&& rect.contains(center.x, center.y)) {
				res.add(actor);
			}
		}
		return res;
	}

	/**
	 * Return the width of the world (in number of cells).
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Get the number of actors currently in the world.
	 * 
	 * @return The number of actors
	 */
	public int numberOfObjects() {
		return objectsDisordered.size();
	}

	/**
	 * Remove an object from the world.
	 * 
	 * @param object
	 *            the object to remove
	 */
	public void removeObject(Actor object) {
		if (object == null || object.world != this) {
			return;
		}

		objectsDisordered.remove(object);
		if (objectsDisordered != objectsInActOrder && objectsInActOrder != null) {
			objectsInActOrder.remove(object);
		} else if (objectsDisordered != objectsInPaintOrder
				&& objectsInPaintOrder != null) {
			objectsInPaintOrder.remove(object);
		}
		object.setWorld(null);
	}

	/**
	 * Remove a list of objects from the world.
	 * 
	 * @param objects
	 *            A list of Actors to remove.
	 */
	@SuppressWarnings("rawtypes")
	public void removeObjects(Collection objects) {
		for (Iterator iter = objects.iterator(); iter.hasNext();) {
			Actor actor = (Actor) iter.next();
			removeObject(actor);
		}
	}

	/**
	 * Repaints the world.
	 */
	public void repaint() {
		DrawPanel.canvas.repaintStage();
	}

	/**
	 * Set the act order of objects in the world. Act order is specified by
	 * class: objects of one class will always act before objects of some other
	 * class. The order of objects of the same class cannot be specified.
	 * 
	 * <p>
	 * Objects of classes listed first in the parameter list will act before any
	 * objects of classes listed later.
	 * 
	 * <p>
	 * Objects of a class not explicitly specified inherit the act order from
	 * their superclass.
	 * 
	 * <p>
	 * Objects of classes not listed will act after all objects whose classes
	 * have been specified.
	 * 
	 * @param classes
	 *            The classes in desired act order
	 */
	@SuppressWarnings("rawtypes")
	public void setActOrder(Class... classes) {
		if (classes == null) {
			// Allow null as an argument, to specify no paint order
			if (objectsInActOrder == objectsDisordered) {
				if (objectsInPaintOrder == null) {
					classes = new Class[0];
					objectsDisordered.setClassOrder(false, classes);
				} else {
					objectsDisordered = objectsInPaintOrder;
				}
			}
			objectsInActOrder = null;
			return;
		}

		if (objectsInActOrder != null) {
			// Just reuse existing set
		} else if (objectsInPaintOrder == objectsDisordered) {
			// Use new set because existing disordered set is in use
			// already.
			objectsInActOrder = new TreeActorSet();
			objectsInActOrder.setClassOrder(false, classes);
			objectsInActOrder.addAll(objectsDisordered);
			return;
		} else {
			// Reuse disordered set, since it is not already in use by the
			// paint ordering
			objectsInActOrder = objectsDisordered;
		}
		objectsInActOrder.setClassOrder(false, classes);
	}

	/**
	 * Set a background image for the world. If the image size is larger than
	 * the world in pixels, it is clipped. If it is smaller than the world, it
	 * is tiled. A pattern showing the cells can easily be shown by setting a
	 * background image with a size equal to the cell size.
	 * 
	 * @see #setBackground(String)
	 * @param image
	 *            The image to be shown
	 */
	final public void setBackground(GreenfootImage image) {
		if (image != null) {
			int imgWidth = image.getWidth();
			int imgHeight = image.getHeight();
			int worldWidth = getWidthInPixels();
			int worldHeight = getHeightInPixels();
			boolean tile = imgWidth < worldWidth || imgHeight < worldHeight;

			if (tile) {
				backgroundImage = new GreenfootImage(worldWidth, worldHeight);
				backgroundImage.setColor(DEFAULT_BACKGROUND_COLOR);
				backgroundImage.fill();

				for (int x = 0; x < worldWidth; x += imgWidth) {
					for (int y = 0; y < worldHeight; y += imgHeight) {
						backgroundImage.drawImage(image, x, y);
					}
				}
			} else {
				// To make it behave exactly the same way as when tiling we
				// should make a clone here. But it performs better when not
				// cloning.
				// Performance will be an issue for people changing the
				// background image all the time for animated backgrounds
				backgroundImage = image;
			}
		} else {
			backgroundImage = null;
		}
	}

	/**
	 * Set a background image for the world from an image file. Images of type
	 * 'jpeg', 'gif' and 'png' are supported. If the image size is larger than
	 * the world in pixels, it is clipped. A pattern showing the cells can
	 * easily be shown by setting a background image with a size equal to the
	 * cell size.
	 * 
	 * @see #setBackground(GreenfootImage)
	 * @param filename
	 *            The file holding the image to be shown
	 * @throws IllegalArgumentException
	 *             If the image can not be loaded.
	 */
	final public void setBackground(String filename)
			throws IllegalArgumentException {
		GreenfootImage bg = new GreenfootImage(filename);
		setBackground(bg);
	}
    
    /**
    * put text on the screen
    **/
    
    public void setText(String sText, int x, int y){
        this.getBackground().drawString(sText, x, y);
    }

    public void showText(String sText, int x, int y){
        this.getBackground().drawString(sText, x, y);
    }

	/**
	 * Set the paint order of objects in the world. Paint order is specified by
	 * class: objects of one class will always be painted on top of objects of
	 * some other class. The order of objects of the same class cannot be
	 * specified. Objects of classes listed first in the parameter list will
	 * appear on top of all objects of classes listed later.
	 * <p>
	 * Objects of a class not explicitly specified effectively inherit the paint
	 * order from their superclass.
	 * <p>
	 * Objects of classes not listed will appear below the objects whose classes
	 * have been specified.
	 * 
	 * @param classes
	 *            The classes in desired paint order
	 */
	@SuppressWarnings("rawtypes")
	public void setPaintOrder(Class... classes) {
		if (classes == null) {
			// Allow null as an argument, to specify no paint order
			if (objectsInPaintOrder == objectsDisordered) {
				if (objectsInActOrder == null) {
					classes = new Class[0];
					objectsDisordered.setClassOrder(true, classes);
				} else {
					objectsDisordered = objectsInActOrder;
				}
			}
			objectsInPaintOrder = null;
			return;
		}

		if (objectsInPaintOrder != null) {
			// Just reuse existing set
		} else if (objectsInActOrder == objectsDisordered) {
			// Use new set because existing disordered set is in use
			// already.
			objectsInPaintOrder = new TreeActorSet();
			objectsInPaintOrder.setClassOrder(true, classes);
			objectsInPaintOrder.addAll(objectsDisordered);
			return;
		} else {
			// Reuse disordered set, since it is not already in use by the
			// act ordering
			objectsInPaintOrder = objectsDisordered;
		}
		objectsInPaintOrder.setClassOrder(true, classes);
	}

	/**
	 * This method is called by the Greenfoot system when the execution has
	 * started. This method can be overridden to implement custom behaviour when
	 * the execution is started.
	 * <p>
	 * This default implementation is empty.
	 */
	public void started() {
		// by default, do nothing
	}

	/**
	 * This method is called by the Greenfoot system when the execution has
	 * stopped. This method can be overridden to implement custom behaviour when
	 * the execution is stopped.
	 * <p>
	 * This default implementation is empty.
	 */
	public void stopped() {
		// by default, do nothing
	}

	TreeActorSet getAllActors() {
		return this.objectsDisordered;
	}

	/**
	 * Returns the centre of the cell.
	 * 
	 * @param l
	 *            Cell location.
	 * @return Absolute location of the cell centre in pixels.
	 */
	int getCellCenter(int l) {
		double cellCenter = l * cellSize + cellSize / 2.;
		return (int) Math.floor(cellCenter);
	}

	Point getCellCenterInPixels(int x, int y) {
		return new Point(getCellCenter(x), getCellCenter(y));
	}

	/**
	 * Get the height of the world in pixels.
	 */
	int getHeightInPixels() {
		return height * cellSize;
	}

	@SuppressWarnings({ "rawtypes" })
	List getObjectsAtCell(int x, int y, Class cls) {
		ArrayList<Actor> res = new ArrayList<Actor>();
		for (Actor actor : this.objectsDisordered) {
			if ((cls == null || cls.isInstance(actor)) && actor.x == x
					&& actor.y == y) {
				res.add(actor);
			}
		}
		return res;
	}

	Collection<Actor> getObjectsAtPixel(int x, int y) {
		// This is a very naive and slow way of getting the objects at a given
		// pixel.
		// However, it makes sure that it doesn't use the collision checker
		// which we want to keep optimised.
		// It will be very slow with a lot of rotated objects. It is only used
		// when using the mouse to select objects, which is not a time-critical
		// task.

		List<Actor> result = new LinkedList<Actor>();
		TreeActorSet objects = getObjectsInPaintOrder();
		for (Actor actor : objects) {
			if (actor.containsPoint(x, y)) {
				result.add(actor);
			}
		}

		return result;
	}

	/**
	 * Get the list of all objects in the world. This returns a live list which
	 * should not be modified by the caller. The world lock must be held while
	 * iterating over this list.
	 */
	TreeActorSet getObjectsInActOrder() {
		if (objectsInActOrder != null) {
			return objectsInActOrder;
		} else {
			return objectsDisordered;
		}
	}

	/**
	 * Get the list of all objects in the world. This returns a live list which
	 * should not be modified by the caller. If iterating over this list, it
	 * should be synchronized on itself or the World to avoid concurrent
	 * modifications.
	 */
	TreeActorSet getObjectsInPaintOrder() {
		if (objectsInPaintOrder != null) {
			return objectsInPaintOrder;
		} else {
			return objectsDisordered;
		}
	}

	/**
	 * Get the width of the world in pixels.
	 */
	int getWidthInPixels() {
		return width * cellSize;
	}

	/**
	 * Test whether this world is bounded.
	 */
	boolean isBounded() {
		return this.isBounded;
	}

	private void addInActOrder(Actor object) {
		if (objectsInActOrder != null) {
			objectsInActOrder.add(object);
		}
	}

	private void addInPaintOrder(Actor object) {
		if (objectsInPaintOrder != null) {
			objectsInPaintOrder.add(object);
		}
	}

	/**
	 * Methods that throws an exception if the location is out of bounds.
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	private void ensureWithinXBounds(int x) throws IndexOutOfBoundsException {
		if (x >= this.getWidth()) {
			throw new IndexOutOfBoundsException("The x-coordinate is: " + x
					+ ". It must be smaller than: " + this.getWidth());
		}
		if (x < 0) {
			throw new IndexOutOfBoundsException("The x-coordinate is: " + x
					+ ". It must be larger than: 0");
		}
	}

	/**
	 * Methods that throws an exception if the location is out of bounds.
	 * 
	 * @throws IndexOutOfBoundsException
	 */
	private void ensureWithinYBounds(int y) throws IndexOutOfBoundsException {
		if (y >= this.getHeight()) {
			throw new IndexOutOfBoundsException("The y-coordinate is: " + y
					+ ". It must be smaller than: " + this.getHeight());
		}
		if (y < 0) {
			throw new IndexOutOfBoundsException("The x-coordinate is: " + y
					+ ". It must be larger than: 0");
		}
	}

}
