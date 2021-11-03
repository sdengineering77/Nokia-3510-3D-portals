package graphics.engine.graphics;

import javax.microedition.lcdui.Graphics;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface Display {
	short[]	createBuffer(int x, int y, int scanlength, int size);
	void		clearBuffer();
	void		draw(Graphics g);
	void		setGraphics(Graphics g);
	void		draw();
}
