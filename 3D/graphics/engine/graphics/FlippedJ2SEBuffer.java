package graphics.engine.graphics;

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
public class FlippedJ2SEBuffer implements Display {
	private short[]	_3DBuffer 		= null;
	private short[]	_3DBuffer_empty = null;
	private int 		scanWidth		= 0;
	private int		height			= 0;
	private int		y				= 0;
	private int		x				= 0;
	
	public short[]	createBuffer(int x, int y, int scanWidth, int height){
		this.scanWidth 		= scanWidth;
		this.height			= height;
		this.x					= x;
		this.y					= y;
		this._3DBuffer_empty	= new short[(scanWidth*height)];
		return (_3DBuffer = new short[(scanWidth*height)]);
	}
	
	public void		clearBuffer() {
		int length = _3DBuffer.length;
		System.arraycopy(_3DBuffer_empty, 0, _3DBuffer, 0, length);
	}

	public void		setGraphics(Graphics g) {
	}
	
	public void		draw() {
	}

	
	public void		draw(Graphics g) {
		int index = 0;
		g.clearRect(0,0,scanWidth,height);

		for( int h=0; h<scanWidth; h++) {
			for( int v=0; v<height; v++) {
				g.setColor(Color.blue);
				switch( _3DBuffer[index++] ) {
					case '0':
					g.setColor(Color.black);
					break;
					case '1':
					g.setColor(Color.green);
					break;
				}
						
				g.drawLine(h, v, h, v);
			}
		}
	}
	
}
