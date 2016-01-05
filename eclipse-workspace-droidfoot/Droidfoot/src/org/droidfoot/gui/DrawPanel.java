package org.droidfoot.gui;

import greenfoot.Actor;
import greenfoot.ActorVisitor;
import greenfoot.GreenfootImage;
import greenfoot.TreeActorSet;
import greenfoot.World;
import greenfoot.WorldManager;

import java.util.Collection;
import java.util.Iterator;

import org.droidfoot.DroidfootActivity;
import org.droidfoot.Settings;
import org.droidfoot.util.Observable;
import org.droidfoot.util.Observer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawPanel extends View implements Observer {

	public final static float STROKE_WIDTH = 2.0f;
	public static DrawPanel canvas;
	public static Object syncObject = new Object();

	private Paint bitmapPaint;
	private Paint surrPaint;
	private Paint imagePaint;

	private Bitmap buffCanvasBitmap;
	private Canvas buffCanvas;

	private Matrix matrix;

	float viewWidth;
	float viewHeight;
	float bitmapWidth;
	float bitmapHeight;
	float bitmapX;
	float bitmapY;
	float scaleFactor;

	Rect surrRect;

	public MousePollingManager mouseListener;

	public DrawPanel(Context context) {
		super(context);
		this.init(context);
	}

	public DrawPanel(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	private void init(Context context) {
		canvas = this;
		this.setBackgroundColor(Settings.backgroundColor);

		this.imagePaint = new Paint();
		this.imagePaint.setAntiAlias(true);

		this.bitmapPaint = new Paint();
		this.bitmapPaint.setAntiAlias(true);

		this.surrPaint = new Paint();
		this.surrPaint.setAntiAlias(true);
		this.surrPaint.setColor(Color.BLACK);
		this.surrPaint.setStyle(Style.STROKE);
		this.surrPaint.setStrokeWidth(STROKE_WIDTH);

		this.surrRect = new Rect();

		matrix = new Matrix();

		initBitmap();

		mouseListener = new MousePollingManager(new WorldLocator() {

			@Override
			public Actor getTopMostActorAt(MotionEvent e) {
				float x = (e.getX() - bitmapX) / scaleFactor;
				float y = (e.getY() - bitmapY) / scaleFactor;
				return getObject(WorldManager.world, (int) x, (int) y);
			}

			@Override
			public int getTranslatedX(MotionEvent e) {
				float x = (e.getX() - bitmapX) / scaleFactor;
				return toCellFloor((int) x);
			}

			@Override
			public int getTranslatedY(MotionEvent e) {
				float y = (e.getY() - bitmapY) / scaleFactor;
				return toCellFloor((int) y);
			}
		});
		this.setOnTouchListener(mouseListener);
	}

	public void initBitmap() {
		// Creating bitmap with attaching it to the buffer-canvas, it means that
		// all the changes done with the canvas are captured into the
		// attached bitmap
		buffCanvasBitmap = Bitmap.createBitmap(
				WorldManager.world.getWidth()
						* WorldManager.world.getCellSize(),
				WorldManager.world.getHeight()
						* WorldManager.world.getCellSize(),
				Bitmap.Config.ARGB_8888);
		buffCanvas = new Canvas();
		buffCanvas.setBitmap(buffCanvasBitmap);
	}

	@Override
	protected void finalize() {
		this.setOnTouchListener(null);
	}

	/**
	 * Returns an object from the given world at the given pixel location. If
	 * multiple objects exist at the one location, this method returns the
	 * top-most one according to paint order.
	 * 
	 * @param x
	 *            The x-coordinate
	 * @param y
	 *            The y-coordinate
	 */
	private static Actor getObject(World world, int x, int y) {

		Collection<?> objectsThere = WorldManager.getObjectsAtPixel(x, y);
		if (objectsThere.isEmpty()) {
			return null;
		}

		Iterator<?> iter = objectsThere.iterator();
		Actor topmostActor = (Actor) iter.next();
		int seq = ActorVisitor.getLastPaintSeqNum(topmostActor);

		while (iter.hasNext()) {
			Actor actor = (Actor) iter.next();
			int actorSeq = ActorVisitor.getLastPaintSeqNum(actor);
			if (actorSeq > seq) {
				topmostActor = actor;
				seq = actorSeq;
			}
		}

		return topmostActor;
	}

	/**
	 * Converts the pixel location into a cell, rounding down.
	 */
	static int toCellFloor(int pixel) {
		return (int) Math.floor((double) pixel
				/ WorldManager.world.getCellSize());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		synchronized (syncObject) {
			try {
				super.onDraw(canvas);

				if (WorldManager.world == null) {
					return;
				}

				// clear canvas
				buffCanvas.drawColor(Color.rgb(255, 227, 160));

				int cellSize = WorldManager.world.getCellSize();
				int x = 0;
				int y = 0;

				// draw background

				GreenfootImage backgroundImage = WorldManager.world
						.getBackground();
				if (backgroundImage != null) {
					matrix.reset();
					imagePaint.setAlpha(backgroundImage.getTransparency());
					buffCanvas.drawBitmap(backgroundImage.getAwtImage(),
							matrix, this.imagePaint);
				}

				// draw actors
				int paintSeq = 0;

				TreeActorSet actors = WorldManager.getObjectsInPaintOrder();
				for (Actor actor : actors) {
					try {
						ActorVisitor.setLastPaintSeqNum(actor, paintSeq++);
						int c = actor.getX();
						int r = actor.getY();
						GreenfootImage img = actor.getImage();
						int ox = x + c * cellSize;
						ox = ox - (img.getWidth() / 2 - cellSize / 2);
						int oy = y + r * cellSize;
						oy = oy - (img.getHeight() / 2 - cellSize / 2);

						if (actor.getRotation() == 0) {
							matrix.reset();
							matrix.postTranslate(ox, oy);
							imagePaint.setAlpha(img.getTransparency());
							buffCanvas.drawBitmap(img.getAwtImage(), matrix,
									this.imagePaint);
						} else {
							matrix.reset();
							matrix.postRotate(actor.getRotation(),
									img.getWidth() / 2, img.getHeight() / 2);
							matrix.postTranslate(ox, oy);

							imagePaint.setAlpha(img.getTransparency());
							buffCanvas.drawBitmap(img.getAwtImage(), matrix,
									this.imagePaint);
						}
					} catch (Exception exc) {
						// sometimes an IllegalStateException is thrown in getX!?
					}
				}

				// then you draw the attached bitmap into the main canvas
				matrix.reset();

				viewWidth = this.getWidth();
				viewHeight = this.getHeight();
				bitmapWidth = buffCanvasBitmap.getWidth();
				bitmapHeight = buffCanvasBitmap.getHeight();

				float factorWidth = viewWidth / bitmapWidth;
				float factorHeight = viewHeight / bitmapHeight;

				// fill the whole display
				// matrix.postScale(factorWidth, factorHeight);

				// stretch relatively
				if (factorWidth > 1.0 && factorHeight > 1.0) {
					if (DroidfootActivity.dfActivity.scalePref) {
						scaleFactor = Math.min(factorWidth, factorHeight);
					} else {
						scaleFactor = 1;
					}
				} else {
					scaleFactor = Math.min(factorWidth, factorHeight);
				}

				matrix.postScale(scaleFactor, scaleFactor);
				bitmapWidth = bitmapWidth * scaleFactor;
				bitmapHeight = bitmapHeight * scaleFactor;
				bitmapX = viewWidth / 2 - bitmapWidth / 2;
				bitmapY = viewHeight / 2 - bitmapHeight / 2;
				matrix.postTranslate(bitmapX, bitmapY);

				// draw
				canvas.drawBitmap(buffCanvasBitmap, matrix, this.bitmapPaint);

				// draw surrounding rectangle
				this.surrRect.set((int) (bitmapX - STROKE_WIDTH / 2),
						(int) (bitmapY - STROKE_WIDTH / 2), (int) (bitmapX
								+ bitmapWidth + STROKE_WIDTH / 2),
						(int) (bitmapY + bitmapHeight + STROKE_WIDTH / 2));
				canvas.drawRect(this.surrRect, surrPaint);
			} finally {
				syncObject.notify();
			}
		}
	}

	@Override
	public void update(Observable observable, Object args) {
		if (args == null) {
			repaintStage();
		}
	}

	public void repaintStage() {
		this.post(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		});
	}

	public Point getCell(MotionEvent event) {
		float x = (event.getX() - bitmapX) / scaleFactor;
		float y = (event.getY() - bitmapY) / scaleFactor;

		int col = (int) (x / WorldManager.world.getCellSize());
		int row = (int) (y / WorldManager.world.getCellSize());

		return new Point(col, row);
	}

}
