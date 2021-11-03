package graphics.engine.drawer;

import game.engine.data.Movement;
import game.engine.data.RectangularPortal;
import graphics.engine.graphics.Display;
import graphics.engine.graphics.DisplayFactory;
import graphics.engine.object.Drawable;
import graphics.engine.object.WallQuad;
import graphics.engine.renderer.Renderer;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.Icon;
import javax.swing.JLabel;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PolyDrawer extends JLabel implements Icon, Runnable, KeyListener {
	public boolean        painted = false;
	public  static long	frameTime = 0;
	public	 int 			angle = 1024;
    private int 			height;
    private int 			width;
	private Drawable[]		polys = new WallQuad[0];
	private Renderer		renderer = null;
	public  static Display display		= null;
	public 	static short[] buffer		= null;
	public   boolean		stop			= false;
	public static final StringBuffer	sb  = new StringBuffer(50);
	public static final int  num_frames = 512;
	public boolean		start = false;
	public  static ClipArea	clipArea		= null;
	public boolean		up_pressed		= false;
	public boolean		dn_pressed		= false;
	public boolean		lt_pressed		= false;
	public boolean		rt_pressed		= false;
	public boolean		up_released		= false;
	public boolean		dn_released		= false;
	public boolean		lt_released		= false;
	public boolean		rt_released		= false;
	public int 			cam_x 			= -112;
	public int 			cam_y 			= 0;
	public int 			cam_z 			= -256;
	public RectangularPortal currentPortal;
	
	


	public PolyDrawer(Renderer r) throws Exception {
		
		this.display  = DisplayFactory.getDisplay("graphics.engine.graphics.J2SEBuffer");		
		this.buffer   = display.createBuffer(0,0,96,64);

        this.setHeight( 64 );
        this.setWidth( 96 );
		this.stop			= false;
        
        this.renderer = r;
        addKeyListener(this);

    }

	public void start() {
		start = true;
		new Thread(this).start();        
	}

	public void paint(Graphics g) {
		drawPolys( g );
	}
	
	
	public void keyPressed(KeyEvent keycode) {
		
		if( keycode.getKeyCode() == KeyEvent.VK_UP ) {
			up_pressed = true;
		}	else
		if( keycode.getKeyCode() == KeyEvent.VK_LEFT ) {
			lt_pressed = true;
		}	else
		if( keycode.getKeyCode() == KeyEvent.VK_RIGHT ) {
			rt_pressed = true;
		}	else
		if( keycode.getKeyCode() == KeyEvent.VK_DOWN ) {
			dn_pressed = true;
		}
	}

	public void keyReleased(KeyEvent keycode) {
		
		if( keycode.getKeyCode() == KeyEvent.VK_UP ) {
			up_released = true;
		}	else
		if( keycode.getKeyCode() == KeyEvent.VK_LEFT ) {
			lt_released = true;
		}	else
		if( keycode.getKeyCode() == KeyEvent.VK_RIGHT ) {
			rt_released = true;
		}	else
		if( keycode.getKeyCode() == KeyEvent.VK_DOWN ) {
			dn_released = true;
		}
	}

  public void keyTyped(KeyEvent evt) {
  }
	
	public void run() {
		while( !start ) { 
			try {
				Thread.sleep(1000);
			}	catch( Exception e ) {
			}		
		}
//		long millis = System.currentTimeMillis();
//		for( int i=num_frames; i>0; i-- ) {
		int walkAngle = 0;
		Movement movement = new Movement();
		long nextTimeMillis = System.currentTimeMillis() + 64;
		long nextTimeMillis2 = nextTimeMillis;
		int cam_y = this.cam_y;
		while( !stop ) {
			long currTimeMillis = System.currentTimeMillis();
			while( currTimeMillis > nextTimeMillis ) {
//			angle = ((angle+32)&0x000007FF);
			int dx = renderer.cos(angle);
			int dz = renderer.sin(angle);
			int velocity = 16;
			cam_y = this.cam_y;
			
			movement.x_from = cam_x;
			movement.y_from = cam_y;
			movement.z_from = cam_z;
			if( up_pressed ) {
				cam_x += ((dx * velocity)>>16);	
				cam_z += ((dz * velocity)>>16);	
				if( up_released ) {
					up_released = false;
					up_pressed = false;
					walkAngle = 0;
				}	else {
					walkAngle =	((walkAngle+128)&0x000003FF);
				}
			}	else
			if( dn_pressed ) {
				cam_x -= ((dx * velocity)>>16);	
				cam_z -= ((dz * velocity)>>16);	
				if( dn_released ) {
					dn_released = false;
					dn_pressed = false;
					walkAngle = 0;
				}	else {
					walkAngle =	((walkAngle-128)&0x000003FF);
				}
			}
			
			if( lt_pressed ) {
				angle = ((angle+32)&0x000007FF); 
				if( lt_released ) {
					lt_released = false;
					lt_pressed = false;
				}
			}	else
			if( rt_pressed ) {
				angle = ((angle-32)&0x000007FF); 
				if( rt_released ) {
					rt_released = false;
					rt_pressed = false;
				}
			}
			
			
			cam_y += ((16 * renderer._sin[walkAngle])>>16);

			movement.x_to = cam_x;
			movement.y_to = this.cam_y-32;
			movement.z_to = cam_z;
			movement.movement_height = 128; 			

		
			currentPortal = currentPortal.processMovement(movement);
			this.cam_x = movement.x_to;
			this.cam_y = movement.y_to;
			this.cam_z = movement.z_to;
			nextTimeMillis += 62;
			}
			
			if( painted ) {		
//			while( currTimeMillis > nextTimeMillis2 ) {
//				nextTimeMillis2 += 200;
				Drawable[] polies = currentPortal.polygons;
				int size = currentPortal.polygons.length;
				for( int polyCnt=0; polyCnt < size; polyCnt++ ) {
		//System.out.println("polys[polyCnt].draw(): " + polys[polyCnt]);		
					polies[polyCnt].draw(buffer, cam_x, cam_y, cam_z, angle, clipArea);
				}
				painted = false;
				this.repaint();
//			}
			}
//			this.serviceRepaints();
		}
//		this.frameTime = System.currentTimeMillis() - millis;
//		stop = true;

//		this.repaint();
//		this.serviceRepaints();
//		try {
//			Thread.sleep(20);
//		}	catch( Exception e ) {
//		}		
	}
	
	public void drawPolys(Graphics g) {
//		long millis = System.currentTimeMillis();
/*		int size = this.polys.length;
		int angle = this.angle;
		int cam_x = cam_x;
		int cam_y = cam_y;
		int cam_z = cam_z;
		
		for( int polyCnt=0; polyCnt < size; polyCnt++ ) {
//System.out.println("polys[polyCnt].draw(): " + polys[polyCnt]);		
			polys[polyCnt].draw(buffer, cam_x, cam_y, cam_z, angle, clipArea);
		}
*/
		display.draw(g);
		painted = true;
		
/*		if( stop ) {
			g.setColor(0x00FF0000);
			g.setFont(Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL));
			g.drawString("t: "+frameTime,5,1, Graphics.TOP+Graphics.LEFT);
			g.drawString("fps: "+(new Float(num_frames*1000).Div(frameTime).toString()),5,10, Graphics.TOP+Graphics.LEFT);
/*
			for( int polyCnt=0; polyCnt < size; polyCnt++ ) {
				sb.delete(0, sb.length());
				sb.append( "ID:" );
				sb.append( ((WallRectangle_i_raycast) polys[polyCnt]).id );
//				sb.append( " c:" );
//				sb.append( ((WallRectangle_i_raycast) polys[polyCnt]).tCull );
//				sb.append( " s:" );
//				sb.append( ((WallRectangle_i_raycast) polys[polyCnt]).tSetup );
				sb.append( " " );
				sb.append( ((WallRectangle_i_raycast) polys[polyCnt]).tHDraw );
				sb.append( " " );
				sb.append( ((WallRectangle_i_raycast) polys[polyCnt]).tVDraw );
				sb.append( " " );
				sb.append( ((WallRectangle_i_raycast) polys[polyCnt]).tDraw );
				g.drawString(sb.toString(),0,(19 + 9*polyCnt), Graphics.TOP+Graphics.LEFT);
			}
*/
//		}
//		frameTime = (frameTime + (System.currentTimeMillis() - millis))/2;
//		g.drawString(""+frameTime,5,5, Graphics.TOP+Graphics.LEFT);

	}



    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }


	/**
	 * Returns the polys.
	 * @return Rectangle3D[]
	 */
	public Drawable[] getPolys() {
		return polys;
	}

	/**
	 * Sets the polys.
	 * @param polys The polys to set
	 */
	public void setPolys(Drawable[] polys) {
//System.out.println("setPolys(Drawable[] polys)");		
		this.polys = polys;
	}

	/**
	 * Sets the stop.
	 * @param stop The stop to set
	 */
	public void setStop(boolean stop) {
		this.stop = stop;
	}

	/**
	 * Returns the clipArea.
	 * @return ClipArea
	 */
	public static ClipArea getClipArea() {
		return clipArea;
	}

	/**
	 * Sets the clipArea.
	 * @param clipArea The clipArea to set
	 */
	public static void setClipArea(ClipArea clipArea) {
		PolyDrawer.clipArea = clipArea;
	}

	/**
	 * Sets the currentPortal.
	 * @param currentPortal The currentPortal to set
	 */
	public void setCurrentPortal(RectangularPortal currentPortal) {
		this.currentPortal = currentPortal;
	}

	/**
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return this.height;
	}

	/**
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return this.width;
	}

	/**
	 * @see javax.swing.Icon#paintIcon(Component, Graphics, int, int)
	 */
	public void paintIcon(Component arg0, Graphics arg1, int arg2, int arg3) {
		drawPolys(arg1);
	}

}
