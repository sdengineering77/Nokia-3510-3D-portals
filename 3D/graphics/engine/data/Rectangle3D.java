package graphics.engine.data;

import graphics.engine.data.Rectangle2D;
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
public class Rectangle3D implements Cloneable {
	
	public Rectangle3D(int x1, int y1, int z1,
						int x2, int y2, int z2,
						int x3, int y3, int z3,
						int x4, int y4, int z4 ) {
	
		this.x1 = x1;
		this.x2 = x2;
		this.x3 = x3;
		this.x4 = x4;
		this.y1 = y1;
		this.y2 = y2;
		this.y3 = y3;
		this.y4 = y4;
		this.z1 = z1;
		this.z2 = z2;
		this.z3 = z3;
		this.z4 = z4;

		int xDiff = (x2-x1);
		int zDiff = (z2-z1);
		this.length = (int) Math.sqrt( (double) (xDiff*xDiff + zDiff*zDiff) );
		this.height = Math.abs(y1 - y3);			
		
	}

	
	public int x1, x2, x3, x4;
	public int y1, y2, y3, y4;
	public int z1, z2, z3, z4;
	public int texture_offset_h = 0;
	public int length;	
	public int height;
	
	
}
