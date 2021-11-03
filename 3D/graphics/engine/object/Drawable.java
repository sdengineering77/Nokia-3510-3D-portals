package graphics.engine.object;

import graphics.engine.drawer.ClipArea;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface Drawable {

	boolean draw( short[] buffer, int cam_x, int cam_y, int cam_z, int angle, ClipArea clipArea );
	
}
