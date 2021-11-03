package test;

import game.engine.data.RectangularPortal;
import game.io.MapLoader;
import graphics.engine.drawer.ClipArea;
import graphics.engine.drawer.PolyDrawer;
import graphics.engine.renderer.Renderer;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GameMidlet {

	public static PolyDrawer polyDrawer = null; 
	
	
	/**
	 * @see javax.microedition.midlet.MIDlet#startApp()
	 */
	protected void startApp() {
		// create parent frame
		Renderer r = new Renderer();

		
//		DeviceControl.setLights(0,100);

		System.gc();

		try {
	        JFrame clientFrame = new JFrame("#D");
	
	        // implement 'kill' button
			clientFrame.addWindowListener( new WindowAdapter() {
	            public void windowClosing(WindowEvent we) {
	                System.exit(0);
	            }
	        }  );
	

			MapLoader loader = new MapLoader();
			RectangularPortal first = loader.loadMap(r, "/portals.map");
			
			polyDrawer = new PolyDrawer(r); 
			clientFrame.getContentPane().setLayout( new BorderLayout() );
	        clientFrame.getContentPane().add( polyDrawer, BorderLayout.CENTER );
			clientFrame.setBounds( 0, 0, 960, 90 );
			clientFrame.setSize( new Dimension( 960, 90 ) );
	        clientFrame.setVisible( true );
			clientFrame.addKeyListener(polyDrawer);			
			ClipArea clipArea = new ClipArea(96,64);
			clipArea.setClipArea(0, 0, 95, 0, 0, 64, 95, 64);		
			
//			polyDrawer.setPolys(polys_i_raycast);
			polyDrawer.setCurrentPortal(first);
			polyDrawer.setClipArea(clipArea);
			polyDrawer.cam_x = first.x_bound_left + 128;
			polyDrawer.cam_z = first.z_bound_bottom + 128;
			polyDrawer.cam_y = first.y_bound_bottom + 128;
//			Display.getDisplay(this).setCurrent(polyDrawer);
			polyDrawer.start();
//			Display.getDisplay(this).callSerially(polyDrawer);
		}	catch( Exception e ) {
			e.printStackTrace();
//			throw new MIDletStateChangeException("" + e.getMessage());
		}
		
		
	}
	
	public static void main(String[] args) {
		System.out.println("running");

		GameMidlet m = new GameMidlet();
		try {
			m.startApp();
		}	catch( Exception e ) {
			System.out.println(e);
			e.printStackTrace();
			
		}
		
	}
	/**
	 * @see javax.microedition.midlet.MIDlet#pauseApp()
	 */
	protected void pauseApp() {
		polyDrawer.setStop(true);
	}

	/**
	 * @see javax.microedition.midlet.MIDlet#destroyApp(boolean)
	 */
	protected void destroyApp(boolean arg0) {
		polyDrawer.setStop(true);
	}

}
