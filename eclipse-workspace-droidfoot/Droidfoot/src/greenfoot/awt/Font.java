/*
 This file is part of the Droidfoot program. 
 
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

package greenfoot.awt;

import android.graphics.Typeface;

/**
 * compensation for java.awt.Font; a Font-object contains of a
 * android.graphics.Typeface-object and a size-value
 * 
 * @author Dietrich Boles (Modifications for Android)
 * @version 1.0
 */
public class Font {

	private Typeface typeface;
	private int size;

	public static final int NORMAL = Typeface.NORMAL;
	public static final int BOLD = Typeface.BOLD;
	public static final int ITALIC = Typeface.ITALIC;
	public static final int BOLD_ITALIC = Typeface.BOLD_ITALIC;

	/**
	 * Default-Constructor
	 */
	public Font() {
		typeface = Typeface.DEFAULT;
		size = -1;
	}

	/**
	 * Copy-Constructor
	 */
	public Font(Font f) {
		typeface = f.typeface;
		size = f.size;
	}

	/**
	 * Creates a new <code>Font</code> from the specified name, style and point
	 * size.
	 * 
	 * @param name
	 *            the font name. This can be a font face name or a font family
	 *            name, and may represent either a logical font or a physical
	 *            font found in this {@code GraphicsEnvironment}. The family
	 *            names for logical fonts are: Dialog, DialogInput, Monospaced,
	 *            Serif, or SansSerif. Pre-defined String constants exist for
	 *            all of these names, for example, {@code DIALOG}. If
	 *            {@code name} is {@code null}, the <em>logical font name</em>
	 *            of the new {@code Font} as returned by {@code getName()} is
	 *            set to the name "Default".
	 * @param style
	 *            The style (normal, bold, italic) of the typeface. e.g. NORMAL,
	 *            BOLD, ITALIC, BOLD_ITALIC
	 * @param size
	 *            the point size of the {@code Font}
	 */
	public Font(String name, int style, int size) {
		typeface = Typeface.create(name, style);
		this.size = size;
	}

	/**
	 * Creates a new <code>Font</code> from the specified typeface and point
	 * size.
	 */
	public Font(Typeface f, int s) {
		typeface = f;
		size = s;
	}

	/**
	 * Creates a new <code>Font</code> object by replicating this
	 * <code>Font</code> object and applying a new size.
	 * 
	 * @param size
	 *            the size for the new <code>Font</code>
	 * @return a new <code>Font</code> object.
	 */
	public Font deriveFont(float size) {
		Font f = new Font(this);
		f.size = (int) size;
		return f;
	}

	/**
	 * Returns the point size of this <code>Font</code>, rounded to an integer.
	 * 
	 * @return the point size of this <code>Font</code>
	 */
	public int getSize() {
		return size;
	}

	/**
	 * returns the typeface-object for this font
	 */
	public Typeface getTypeface() {
		return typeface;
	}

	/**
	 * Changes the point size of this <code>Font</code>
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * changes the typeface-object for this font
	 */
	public void setTypeface(Typeface typeface) {
		this.typeface = typeface;
	}

}
