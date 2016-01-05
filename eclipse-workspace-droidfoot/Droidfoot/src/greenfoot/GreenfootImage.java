/*
 This file is part of the Greenfoot program. 
 Copyright (C) 2005-2009,2010,2011,2012  Poul Henriksen and Michael Kolling 
 
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
import greenfoot.awt.Font;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * An image to be shown on screen. The image may be loaded from an image file
 * and/or drawn by using various drawing methods.
 * 
 * <h2>Droidfoot</h2> The class GreenfootImage for Droidfoot differs from the
 * original Greenfoot-class GreenfootImage in the following aspects:
 * <ul>
 * <li>drawShape is not supported
 * <li>fillShape is not supported
 * <li>getAwtImage returns an instance from class android.graphics.Bitmap
 * <li>instead of class java.awt.Color class greenfoot.awt.Color is used
 * <li>instead of class java.awt.Font class greenfoot.awt.Font is used
 * </ul>
 * 
 * @author Poul Henriksen
 * @version 2.4
 * 
 * @author Dietrich Boles (Modifications for Android)
 * @version 2.0
 */
public class GreenfootImage {
	private static final int DEFAULT_BACKGROUND = Color.argb(0, 255, 255, 255);
	private static final int DEFAULT_FOREGROUND = android.graphics.Color.BLACK;

	private Bitmap image;
	private Canvas canvas;

	private int currentColor;
	private Font currentFont;
	private Paint mOutline;

	private Paint mFill;

	private Paint imgPaint;

	/**
	 * Value from 0 to 255, with 0 being completely transparent and 255 being
	 * opaque.
	 */
	private int transparency;

	/**
	 * Create a GreenfootImage from another GreenfootImage.
	 */
	public GreenfootImage(GreenfootImage image) throws IllegalArgumentException {
		this.image = image.image.copy(image.image.getConfig(), true);
		canvas = new Canvas(this.image);
		init();
		GreenfootImage.copyStates(image, this);
	}

	/**
	 * Create an empty (transparent) image with the specified size.
	 * 
	 * @param width
	 *            The width of the image in pixels.
	 * @param height
	 *            The height of the image in pixels.
	 */
	public GreenfootImage(int width, int height) {
		image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(image);
		init();
	}

	/**
	 * Create an image from an image file. Supported file formats are JPEG, GIF
	 * and PNG.
	 * 
	 * <p>
	 * The file name may be an absolute path, or a base name for a file located
	 * in the project directory.
	 * 
	 * @param filename
	 *            Typically the name of a file in the images directory within
	 *            the project directory.
	 * @throws IllegalArgumentException
	 *             If the image can not be loaded.
	 */
	public GreenfootImage(String filename) throws IllegalArgumentException {
		InputStream is;
		try {
			is = WorldManager.context.getAssets().open("images/" + filename);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inMutable = true;

			Bitmap realImage = BitmapFactory.decodeStream(is, null, options);
			if (realImage == null) {
				throw new IllegalArgumentException();
			}

			image = Bitmap.createBitmap(realImage.getWidth(),
					realImage.getHeight(), Bitmap.Config.ARGB_8888);
			canvas = new Canvas(image);
			init();

			canvas.drawBitmap(realImage, 0, 0, imgPaint);

		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Creates an image with the given string drawn as text using the given font
	 * size, with the given foreground color on the given background color. If
	 * the string has newline characters, it is split into multiple lines which
	 * are drawn horizontally-centred.
	 * 
	 * @param string
	 *            the string to be drawn
	 * @param size
	 *            the requested height in pixels of each line of text (the
	 *            actual height may be different by a pixel or so)
	 * @param foreground
	 *            the color of the text. Since Greenfoot 2.2.0, passing null
	 *            will use black.
	 * @param background
	 *            the color of the image behind the text. Since Greenfoot 2.2.0,
	 *            passing null with leave the background transparent.
	 * @since 2.0.1
	 */
	public GreenfootImage(String string, int size, int foreground,
			int background) {
		String[] lines = string.replaceAll("\r", "").split("\n");
		init();

		Paint dFill = new Paint(mFill);
		dFill.setTextSize(size);

		Rect[] bounds = new Rect[lines.length];
		for (int i = 0; i < bounds.length; i++) {
			bounds[i] = new Rect();
		}

		int maxX = 1;
		int y = 0;
		for (int i = 0; i < lines.length; i++) {
			dFill.getTextBounds(lines[i], 0, lines[i].length(), bounds[i]);
			maxX = Math.max(maxX, (int) Math.ceil(bounds[i].width()));
			y += size; // Math.ceil(bounds[i].height());
		}
		y = Math.max(y, 1);
		this.image = Bitmap.createBitmap(maxX, y, Bitmap.Config.ARGB_8888);
		canvas = new Canvas(image);

		dFill.setColor(background);
		canvas.drawRect(
				new Rect(0, 0, this.image.getWidth(), this.image.getHeight()),
				dFill);

		dFill.setColor(foreground);
		y = size; // 0;
		for (int i = 0; i < lines.length; i++) {
			canvas.drawText(lines[i], (maxX - bounds[i].width()) / 2
					- bounds[i].left, y - bounds[i].bottom, dFill);
			y += size; // Math.ceil(bounds[i].height());
		}
	}

	/**
	 * Creates an image with the given string drawn as text using the given font
	 * size, with the given foreground color on the given background color. If
	 * the string has newline characters, it is split into multiple lines which
	 * are drawn horizontally-centred.
	 * 
	 * @param string
	 *            the string to be drawn
	 * @param size
	 *            the requested height in pixels of each line of text (the
	 *            actual height may be different by a pixel or so)
	 * @param foreground
	 *            the color of the text. Since Greenfoot 2.2.0, passing null
	 *            will use black.
	 * @param background
	 *            the color of the image behind the text. Since Greenfoot 2.2.0,
	 *            passing null with leave the background transparent.
	 * @since 2.0.1 (Droidfoot since 1.1)
	 */
	public GreenfootImage(String string, int size, Color foreground,
			Color background) {
		this(string, size, foreground.getColor(), background.getColor());
	}

	/**
	 * Clears the image.
	 * 
	 */
	public void clear() {
		image.eraseColor(DEFAULT_BACKGROUND);
	}

	/**
	 * Draws the given Image onto this image
	 * 
	 * @param image
	 *            The image to draw onto this one.
	 * @param x
	 *            x-coordinate for drawing the image.
	 * @param y
	 *            y-coordinate for drawing the image.
	 */
	public void drawImage(GreenfootImage image, int x, int y) {
		imgPaint.setAlpha(image.getTransparency());
		canvas.drawBitmap(image.image, x, y, imgPaint);
	}

	/**
	 * Draw a line, using the current drawing color, between the points
	 * <code>(x1,&nbsp;y1)</code> and <code>(x2,&nbsp;y2)</code>.
	 * 
	 * @param x1
	 *            the first point's <i>x </i> coordinate.
	 * @param y1
	 *            the first point's <i>y </i> coordinate.
	 * @param x2
	 *            the second point's <i>x </i> coordinate.
	 * @param y2
	 *            the second point's <i>y </i> coordinate.
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		final Path p = new Path();
		p.moveTo(x1, y1);
		p.lineTo(x2, y2);
		canvas.drawPath(p, mOutline);
	}

	/**
	 * Draw an oval bounded by the specified rectangle with the current drawing
	 * color.
	 * 
	 * @param x
	 *            the <i>x </i> coordinate of the upper left corner of the oval
	 *            to be filled.
	 * @param y
	 *            the <i>y </i> coordinate of the upper left corner of the oval
	 *            to be filled.
	 * @param width
	 *            the width of the oval to be filled.
	 * @param height
	 *            the height of the oval to be filled.
	 */
	public void drawOval(int x, int y, int width, int height) {
		canvas.drawOval(new RectF(x, y, x + width, y + height), mOutline);
	}

	/**
	 * Draws a closed polygon defined by arrays of <i>x</i> and <i>y</i>
	 * coordinates. Each pair of (<i>x</i>,&nbsp;<i>y</i>) coordinates defines a
	 * point.
	 * <p>
	 * This method draws the polygon defined by <code>nPoint</code> line
	 * segments, where the first <code>nPoint&nbsp;-&nbsp;1</code> line segments
	 * are line segments from
	 * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code> to
	 * <code>(xPoints[i],&nbsp;yPoints[i])</code>, for
	 * 1&nbsp;&le;&nbsp;<i>i</i>&nbsp;&le;&nbsp;<code>nPoints</code>. The figure
	 * is automatically closed by drawing a line connecting the final point to
	 * the first point, if those points are different.
	 * 
	 * @param xPoints
	 *            an array of <code>x</code> coordinates.
	 * @param yPoints
	 *            an array of <code>y</code> coordinates.
	 * @param nPoints
	 *            the total number of points.
	 */
	public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		if (nPoints <= 0) {
			return;
		}
		Path p = new Path();
		p.moveTo(xPoints[0], yPoints[0]);
		for (int i = 1; i < nPoints; i++) {
			p.lineTo(xPoints[i], yPoints[i]);
		}
		p.lineTo(xPoints[0], yPoints[0]);
		canvas.drawPath(p, mOutline);
	}

	/**
	 * Draw the outline of the specified rectangle. The left and right edges of
	 * the rectangle are at <code>x</code> and <code>x&nbsp;+&nbsp;width</code>.
	 * The top and bottom edges are at <code>y</code> and
	 * <code>y&nbsp;+&nbsp;height</code>. The rectangle is drawn using the
	 * current color.
	 * 
	 * @param x
	 *            the <i>x </i> coordinate of the rectangle to be drawn.
	 * @param y
	 *            the <i>y </i> coordinate of the rectangle to be drawn.
	 * @param width
	 *            the width of the rectangle to be drawn.
	 * @param height
	 *            the height of the rectangle to be drawn.
	 */
	public void drawRect(int x, int y, int width, int height) {
		canvas.drawRect(new Rect(x, y, x + width, y + height), mOutline);
	}

	/**
	 * Draw the text given by the specified string, using the current font and
	 * color. The baseline of the leftmost character is at position ( <i>x
	 * </i>,&nbsp; <i>y </i>).
	 * 
	 * @param string
	 *            the string to be drawn.
	 * @param x
	 *            the <i>x </i> coordinate.
	 * @param y
	 *            the <i>y </i> coordinate.
	 */
	public void drawString(String string, int x, int y) {
		canvas.drawText(string, x, y, mFill);
	}

	/**
	 * Fill the entire image with the current drawing dcolor.
	 * 
	 */
	public void fill() {
		canvas.drawRect(new Rect(0, 0, image.getWidth(), image.getHeight()),
				mFill);
	}

	/**
	 * Fill an oval bounded by the specified rectangle with the current drawing
	 * color.
	 * 
	 * @param x
	 *            the <i>x </i> coordinate of the upper left corner of the oval
	 *            to be filled.
	 * @param y
	 *            the <i>y </i> coordinate of the upper left corner of the oval
	 *            to be filled.
	 * @param width
	 *            the width of the oval to be filled.
	 * @param height
	 *            the height of the oval to be filled.
	 */
	public void fillOval(int x, int y, int width, int height) {
		canvas.drawOval(new RectF(x, y, x + width, y + height), mFill);
	}

	/**
	 * Fill a closed polygon defined by arrays of <i>x </i> and <i>y </i>
	 * coordinates.
	 * <p>
	 * This method draws the polygon defined by <code>nPoint</code> line
	 * segments, where the first <code>nPoint&nbsp;-&nbsp;1</code> line segments
	 * are line segments from
	 * <code>(xPoints[i&nbsp;-&nbsp;1],&nbsp;yPoints[i&nbsp;-&nbsp;1])</code> to
	 * <code>(xPoints[i],&nbsp;yPoints[i])</code>, for 1&nbsp;&le;&nbsp; <i>i
	 * </i>&nbsp;&le;&nbsp; <code>nPoints</code>. The figure is automatically
	 * closed by drawing a line connecting the final point to the first point,
	 * if those points are different.
	 * <p>
	 * The area inside the polygon is defined using an even-odd fill rule, also
	 * known as the alternating rule.
	 * 
	 * @param xPoints
	 *            a an array of <code>x</code> coordinates.
	 * @param yPoints
	 *            a an array of <code>y</code> coordinates.
	 * @param nPoints
	 *            a the total number of points.
	 */
	public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
		if (nPoints <= 0) {
			return;
		}
		Path p = new Path();
		p.moveTo(xPoints[0], yPoints[0]);
		for (int i = 1; i < nPoints; i++) {
			p.lineTo(xPoints[i], yPoints[i]);
		}
		p.lineTo(xPoints[0], yPoints[0]);
		canvas.drawPath(p, mFill);
	}

	/**
	 * Fill the specified rectangle. The left and right edges of the rectangle
	 * are at <code>x</code> and <code>x&nbsp;+&nbsp;width&nbsp;-&nbsp;1</code>.
	 * The top and bottom edges are at <code>y</code> and
	 * <code>y&nbsp;+&nbsp;height&nbsp;-&nbsp;1</code>. The resulting rectangle
	 * covers an area <code>width</code> pixels wide by <code>height</code>
	 * pixels tall. The rectangle is filled using the current color.
	 * 
	 * @param x
	 *            the <i>x </i> coordinate of the rectangle to be filled.
	 * @param y
	 *            the <i>y </i> coordinate of the rectangle to be filled.
	 * @param width
	 *            the width of the rectangle to be filled.
	 * @param height
	 *            the height of the rectangle to be filled.
	 */
	public void fillRect(int x, int y, int width, int height) {
		canvas.drawRect(new Rect(x, y, x + width, y + height), mFill);
	}

	/**
	 * Returns the java.awt.image.BufferedImage that backs this GreenfootImage.
	 * Any changes to the returned image will be reflected in the
	 * GreenfootImage.
	 * 
	 * @return The java.awt.image.BufferedImage backing this GreenfootImage
	 * @since Greenfoot version 1.0.2
	 */
	public Bitmap getAwtImage() {
		return this.image;
	}

	/**
	 * Return the current drawing color.
	 * 
	 * @return The current color.
	 */
	public Color getColor() {
		return new Color(this.currentColor);
	}

	/**
	 * Return the color at the given pixel.
	 * 
	 * @throws IndexOutOfBoundsException
	 *             If the pixel location is not within the image bounds.
	 */
	public Color getColorAt(int x, int y) {
		return new Color(this.getRGBAt(x, y));
	}

	/**
	 * Get the current font.
	 */
	public Font getFont() {
		return this.currentFont;
	}

	/**
	 * Return the height of the image.
	 * 
	 * @return Height of the image.
	 */
	public int getHeight() {
		return this.image.getHeight();
	}

	/**
	 * Return the current transparency of the image.
	 * 
	 * @return A value in the range 0 to 255. 0 is completely transparent
	 *         (invisible) and 255 is completely opaque (the default).
	 */
	public int getTransparency() {
		return this.transparency;
	}

	/**
	 * Return the width of the image.
	 * 
	 * @return Width of the image.
	 */
	public int getWidth() {
		return this.image.getWidth();
	}

	/**
	 * Mirrors the image horizontally (the left of the image becomes the right,
	 * and vice versa).
	 * 
	 */
	public void mirrorHorizontally() {
		Matrix matrix = new Matrix();
		matrix.preScale(-1.0f, 1.0f);
		image = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
				image.getHeight(), matrix, true);
		this.canvas.setBitmap(this.image);
	}

	/**
	 * Mirrors the image vertically (the top of the image becomes the bottom,
	 * and vice versa).
	 * 
	 */
	public void mirrorVertically() {
		Matrix matrix = new Matrix();
		matrix.preScale(1.0f, -1.0f);
		image = Bitmap.createBitmap(image, 0, 0, image.getWidth(),
				image.getHeight(), matrix, true);
		this.canvas.setBitmap(this.image);
	}

	/**
	 * Rotates this image around the center.
	 * 
	 * @param degrees
	 */
	public void rotate(int degrees) {
		Matrix matrix = new Matrix();
		int width = image.getWidth();
		int height = image.getHeight();
		matrix.setRotate(degrees, width / 2, height / 2);
		image = Bitmap.createBitmap(image, 0, 0, width, height, matrix, true);
		// dibo: bei einer Rotation von bspw. 70 Grad entsteht eine Bitmap mit
		// einer größeren Größe als width und height
		// daher wird nochmal skaliert; sieht allerdings anders aus als im
		// Original-Greenfoot :-(
		// image = Bitmap.createScaledBitmap(image, width, height, true);
		if (image.getWidth() > width || image.getHeight() > height) {
			image = Bitmap
					.createBitmap(image, (image.getWidth() - width) / 2,
							(image.getHeight() - height) / 2, width, height,
							null, true);
		}
		this.canvas.setBitmap(image);
	}

	/**
	 * Scales this image to a new size.
	 * 
	 * @param width
	 *            Width of new image
	 * @param height
	 *            Height of new image
	 */
	public void scale(int width, int height) {
		if (width == this.image.getWidth() && height == this.image.getHeight()) {
			return;
		}

		image = Bitmap.createScaledBitmap(image, width, height, true);
		this.canvas.setBitmap(this.image);
	}

	/**
	 * Set the current drawing color. This color will be used for subsequent
	 * drawing operations.
	 * 
	 * @param color
	 *            The color to be used.
	 */
	public void setColor(Color color) {
		this.setColor(color.getColor());
	}

	/**
	 * Set the current drawing color. This color will be used for subsequent
	 * drawing operations.
	 * 
	 * @param color
	 *            The color to be used.
	 */
	public void setColor(int color) {
		this.currentColor = color;
		mOutline.setColor(this.currentColor);
		mFill.setColor(this.currentColor);
	}

	/**
	 * Sets the color at the given pixel to the given color.
	 */
	public void setColorAt(int x, int y, Color color) {
		this.setRGBAt(x, y, color.getColor());
	}

	/**
	 * Sets the color at the given pixel to the given color.
	 */
	public void setColorAt(int x, int y, int color) {
		this.setRGBAt(x, y, color);
	}

	/**
	 * Set the current font. This font will be used for subsequent text
	 * operations.
	 */
	public void setFont(Font f) {
		this.currentFont = f;
		mOutline.setTypeface(f.getTypeface());
		mOutline.setTextSize(f.getSize());
		mFill.setTypeface(f.getTypeface());
		mFill.setTextSize(f.getSize());
	}

	/**
	 * Set the transparency of the image.
	 * 
	 * @param t
	 *            A value in the range 0 to 255. 0 is completely transparent
	 *            (invisible) and 255 is completely opaque (the default).
	 */
	public void setTransparency(int t) {
		if (t < 0 || t > 255) {
			throw new IllegalArgumentException(
					"The transparency value has to be in the range 0 to 255. It was: "
							+ t);
		}

		this.transparency = t;
	}

	/**
	 * Return a text representation of the image.
	 */
	@Override
	public String toString() {
		String superString = super.toString();
		return superString;
	}

	private int getRGBAt(int x, int y) {
		if (x >= this.getWidth()) {
			throw new IndexOutOfBoundsException("X is out of bounds. It was: "
					+ x + " and it should have been smaller than: "
					+ this.getWidth());
		}
		if (y >= this.getHeight()) {
			throw new IndexOutOfBoundsException("Y is out of bounds. It was: "
					+ y + " and it should have been smaller than: "
					+ this.getHeight());
		}
		if (x < 0) {
			throw new IndexOutOfBoundsException("X is out of bounds. It was: "
					+ x + " and it should have been at least: 0");
		}
		if (y < 0) {
			throw new IndexOutOfBoundsException("Y is out of bounds. It was: "
					+ y + " and it should have been at least: 0");
		}

		return this.image.getPixel(x, y);
	}

	private void init() {
		currentColor = GreenfootImage.DEFAULT_FOREGROUND;
		currentFont = new Font();
		transparency = 255;
		mOutline = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		mOutline.setColor(this.currentColor);
		mOutline.setStyle(Paint.Style.STROKE);
		mOutline.setStrokeWidth(1f);
		mOutline.setTypeface(this.currentFont.getTypeface());
		mOutline.setTextAlign(Align.LEFT);
		if (currentFont.getSize() != -1) {
			mOutline.setTextSize(currentFont.getSize());
		}
		mFill = new Paint(mOutline);
		mFill.setStyle(Paint.Style.FILL);
		imgPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
		imgPaint.setStyle(Paint.Style.FILL);
	}

	private void setRGBAt(int x, int y, int color) {
		if (x >= this.getWidth()) {
			throw new IndexOutOfBoundsException("X is out of bounds. It was: "
					+ x + " and it should have been smaller than: "
					+ this.getWidth());
		}
		if (y >= this.getHeight()) {
			throw new IndexOutOfBoundsException("Y is out of bounds. It was: "
					+ y + " and it should have been smaller than: "
					+ this.getHeight());
		}
		if (x < 0) {
			throw new IndexOutOfBoundsException("X is out of bounds. It was: "
					+ x + " and it should have been at least: 0");
		}
		if (y < 0) {
			throw new IndexOutOfBoundsException("Y is out of bounds. It was: "
					+ y + " and it should have been at least: 0");
		}

		this.image.setPixel(x, y, color);
	}

	/**
	 * Copies the states from the src image to dst image.
	 */
	private static void copyStates(GreenfootImage src, GreenfootImage dst) {
		dst.currentColor = src.currentColor;
		dst.currentFont = new Font(src.currentFont);
		dst.transparency = src.transparency;
		dst.mOutline = new Paint(src.mOutline);
		dst.mFill = new Paint(src.mFill);
		dst.imgPaint = new Paint(src.imgPaint);
	}

	/**
	 * Draw a shape directly on the image. Shapes are specified by the <a href=
	 * "http://java.sun.com/javase/6/docs/api/java/awt/Shape.html">shape
	 * interface</a>.
	 * 
	 * @param shape
	 *            the shape to be drawn.
	 */
	// public void drawShape(Shape shape) {
	// Graphics2D g = this.getGraphics();
	// g.draw(shape);
	// g.dispose();
	// }

	/**
	 * Draw a filled shape directly on the image. Shapes are specified by the <a
	 * href="http://java.sun.com/javase/6/docs/api/java/awt/Shape.html">shape
	 * interface</a>.
	 * 
	 * @param shape
	 *            the shape to be drawn.
	 */
	// public void fillShape(Shape shape) {
	// Graphics2D g = this.getGraphics();
	// g.fill(shape);
	// g.dispose();
	// }

}