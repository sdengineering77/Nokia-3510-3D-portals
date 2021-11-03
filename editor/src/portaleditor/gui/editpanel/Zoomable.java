package portaleditor.gui.editpanel;

/**
 * @author sedneyd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface Zoomable {
	void zoom( double x, double y );
	void zoomIn( double x, double y );
	void zoomOut( double x, double y );
	void zoom();
	void zoomIn();
	void zoomOut();
	
}
