package test;

import game.engine.data.RectangularPortal;
import game.io.MapLoader;
import graphics.engine.drawer.ClipArea;
import graphics.engine.drawer.PolyDrawer;
import graphics.engine.object.CeilQuad;
import graphics.engine.object.Drawable;
import graphics.engine.object.FloorQuad;
import graphics.engine.object.ViewportWallQuad;
import graphics.engine.object.WallQuad;
import graphics.engine.renderer.Renderer;

import java.util.Vector;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

//import com.nokia.mid.impl.isa.ui.MIDletManager;
import com.nokia.mid.ui.DeviceControl;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GameMidlet extends MIDlet {

	public static PolyDrawer polyDrawer = null; 
	
	
	/**
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() throws MIDletStateChangeException {
		// create parent frame
//System.out.println( "mem after add:" + Runtime.getRuntime().freeMemory() );
		
		Renderer r = new Renderer();

		DeviceControl.setLights(0,100);
//System.out.println( "mem after add:" + Runtime.getRuntime().freeMemory() );


		try {
			MapLoader loader = new MapLoader();
			RectangularPortal first = loader.loadMap(r, "/portals.map");

//System.out.println( "=====:" + Runtime.getRuntime().freeMemory() );
			loader = null;
			System.gc();
System.out.println( "=====:" + Runtime.getRuntime().freeMemory() );
			
			polyDrawer = new PolyDrawer(r); 
			ClipArea clipArea = new ClipArea(96,64);
			clipArea.setClipArea(0, 0, 95, 0, 0, 64, 95, 64);		
			
//			polyDrawer.setPolys(polys_i_raycast);
			polyDrawer.setCurrentPortal(first);
			polyDrawer.setClipArea(clipArea);
			polyDrawer.cam_x = first.x_bound_left + 128;
			polyDrawer.cam_z = first.z_bound_bottom + 128;
			polyDrawer.cam_y = first.y_bound_bottom + 128;
			Display.getDisplay(this).setCurrent(polyDrawer);
			polyDrawer.start();
//			Display.getDisplay(this).callSerially(polyDrawer);
		}	catch( Exception e ) {
			e.printStackTrace();
			throw new MIDletStateChangeException("" + e.getMessage());
		}
		
		
	}
	
/*	public static void main(String[] args) {
		System.out.println("running");
//		MIDletManager.main(args);
		GameMidlet m = new GameMidlet();
		try {
			m.startApp();
		}	catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
			
		}
		
	}
*/
	/**
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		polyDrawer.setStop(true);
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
		polyDrawer.setStop(true);
	}


}
