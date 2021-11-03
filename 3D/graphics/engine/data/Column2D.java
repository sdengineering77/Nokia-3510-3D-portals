package graphics.engine.data;

import graphics.engine.renderer.Renderer;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Column2D implements Cloneable {

	public Column2D() {
	}
		
	public Column2D(int x, int y, int height ) {
	

		this.x = x;
		this.y= y;
		this.height = height;		
	}

	
	public int x;
	public int y;
	public int height;
}
