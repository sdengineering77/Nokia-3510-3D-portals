package graphics.engine.graphics;

import javax.microedition.lcdui.Graphics;

import com.nokia.mid.ui.DirectGraphics;
import com.nokia.mid.ui.DirectUtils;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class NokiaBuffer implements Display {
	private short[]	_3DBuffer 		= null;
//	private short[]	_3DBuffer_empty = null;
	private int 		scanWidth		= 0;
	private int		height			= 0;
	private int		y				= 0;
	private int		x				= 0;
	
	public short[]	createBuffer(int x, int y, int scanWidth, int height){
		this.scanWidth 		= scanWidth;
		this.height			= height;
		this.x					= x;
		this.y					= y;
//		this._3DBuffer_empty	= new short[(scanWidth*height)];
		return (_3DBuffer = new short[(scanWidth*height)]);
	}
	
	public void		clearBuffer() {
//		int length = _3DBuffer.length;
//		System.arraycopy(_3DBuffer_empty, 0, _3DBuffer, 0, length);
	}

	public void		setGraphics(Graphics g) {
	}
	
	public void		draw() {
	}

	
	public void		draw(Graphics g) {
		DirectGraphics dg = DirectUtils.getDirectGraphics(g);
		int offset = 0;
		dg.drawPixels(_3DBuffer, false, 0, scanWidth, 0, 0, scanWidth, height, 0, DirectGraphics.TYPE_USHORT_4444_ARGB);
	}
	
}
