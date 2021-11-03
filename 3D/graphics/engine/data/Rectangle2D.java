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
public class Rectangle2D implements Cloneable {

	public Rectangle2D() {
	}
		
	public Rectangle2D(int x1, int y1,
						int x2, int y2,
						int x3, int y3,
						int x4, int y4 ) {
	
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.y4 = y4;
		
	}

	
	public int x1, x2, x3, x4;
	public int y1, y2, y3, y4;
}
