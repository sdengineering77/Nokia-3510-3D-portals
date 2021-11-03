package graphics.engine.drawer;

import graphics.engine.data.Rectangle2D;
import graphics.engine.graphics.Display;
import graphics.engine.graphics.DisplayFactory;
import graphics.engine.object.CeilQuad;
import graphics.engine.object.Drawable;
import graphics.engine.object.FloorQuad;
import graphics.engine.object.ViewportWallQuad;
import graphics.engine.object.WallQuad;
import graphics.engine.object.WallRectangle_i_raycast;
import graphics.engine.renderer.Renderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
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
public class PolyDrawer extends JLabel implements Icon, Runnable {

	public	 int 				angle = 1024;
    private int 				height;
    private int 				width;
	private Drawable[]			polys 			= new WallRectangle_i_raycast[0];
	private Renderer			renderer 		= null;
	private int[] 			viewportY_top	= null;
	private int[] 			viewportY_bottom= null;
	private int				viewportX_left	= 0;
	private int 				viewportX_right = 0;
	public  static Display 	display			= null;
	public  static short[] 	buffer			= null;
	public  static ClipArea	clipArea		= null;
	public	int cam_x = -464;
	public	int cam_y = 0;
	public	int cam_z = -256;

	public PolyDrawer() throws Exception {
		
		this.display  = DisplayFactory.getDisplay("graphics.engine.graphics.J2SEBuffer");		
		this.buffer   = display.createBuffer(0,0,96,64);

        this.setHeight( 64 );
        this.setWidth( 96 );

        this.setIcon( this );
        this.setOpaque( false );
        
        this.renderer = new Renderer();
		new Thread(this).start();        

    }

	public void paint() {
		paintIcon( null, getGraphics(), 0, 0 );
	}
	
    public void paintIcon(Component c, Graphics g, int x, int y){
//		Shape s = new Shape();
//		s.getBounds2D().
//    	g.setClip()
        drawPolys( g );
    }

	public void run() {
		int walkAngle = 0;
		while( true ) {
			cam_z = cam_z;
			angle = ((angle+32)&0x000007FF);
System.out.println( "cam_z: " + cam_z );			
//			walkAngle =	((walkAngle+128)&0x000003FF);

			int cam_y = this.cam_y + ((16 * renderer._sin[walkAngle])>>16);

		int size = this.polys.length;
		for( int polyCnt=0; polyCnt < size; polyCnt++ ) {
//System.out.println("------------------------------------" );
//System.out.println("------------------------------------" );
//System.out.println("------------------------------------" );
//System.out.println("------------------------------------" );
			polys[polyCnt].draw(buffer, cam_x, cam_y, cam_z, angle, clipArea);
		}

//			this.repaint();
			this.paintImmediately(0,0,96,64);
			try {
				Thread.sleep(10);
			}	catch( Exception e ) {
			}
		}
	}
	
	public void drawPolys(Graphics g) {
		long millis = System.currentTimeMillis();
//for( angle=2048; angle>=0; angle-- ) {
//		int size = this.polys.length;
//		display.clearBuffer();
//		System.out.println( "extra: " + extra );
//		int angle = this.angle;
//		int	cam_x = 0;
//		int	cam_y = 0;
//		int	cam_z = 256;
//		int cam_x = this.cam_x;
//		int cam_y = this.cam_y;
//		int cam_z = this.cam_z;
		
		
		display.draw(g);
//}		
		g.setColor(Color.black);
		g.drawString( "millis: " + (System.currentTimeMillis() - millis), 5, 5 );
//		Log.print();
	}



	public int getIconHeight(){
        return height;
    }
 	public int getIconWidth(){
        return width;
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
		this.polys = polys;
	}

	public void setViewPort( Rectangle2D rect ) {
		int width  = rect.x2 - rect.x1;
		int currY_top		= (rect.y1<<16);
		int currY_bottom 	= ((rect.y3-1)<<16);
		int dy_top 		= (64<<16) + (int) (((long) (rect.y2 - rect.y1))<<16) / width;
		int dy_bottom 		= (64<<16) + (int) (((long) (rect.y4 - rect.y3))<<16) / width;
	
		viewportX_left		= rect.x1;
		viewportX_right		= rect.x2;
		viewportY_top 		= new int[width+1];
		viewportY_bottom 	= new int[width+1];

		for( int columnCnt=0; columnCnt<=width; columnCnt++ ) {
			viewportY_top[columnCnt] = currY_top;
			viewportY_bottom[columnCnt] = currY_bottom;
			
			currY_top 		+= dy_top;
			currY_bottom 	+= dy_bottom;
		}			
	}
	

	public static void main(String args[]) {
		// create parent frame
System.out.println("Starting" );
        JFrame clientFrame = new JFrame("#D");
System.out.println("Frame created" );

        // implement 'kill' button
		clientFrame.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        }  );

System.out.println("Create renderer" );

		Renderer r = new Renderer();
System.out.println("Create poly" );

//		WallRectangle_i_raycast[] polys_i_raycast = new WallRectangle_i_raycast[] {
/*				new WallRectangle_i_raycast(	-128,  256, -512,
									-128,  258, 0,
									-128, -128, -512,
									-128, -128, 0, r ), 
				new WallRectangle_i_raycast(	-128,  256, 0,
									-128,  258, 512,
									-128, -128, 0,
									-128, -128, 512, r ), 
*/
/*				new WallRectangle_i_raycast(	-128,  256, 512,
									 0,  256, 512,
									-128, -128, 512,
									 0, -128, 512, r ), 

				new WallRectangle_i_raycast(	0,  256, 512,
									 128,  256, 512,
									0, -128, 512,
									 128, -128, 512, r ), 

				new WallRectangle_i_raycast(	128,  256, -512,
									0,  256, -512,
									 128, -128, -512,
									0, -128, -512, r ), 
				new WallRectangle_i_raycast(	0,  256, -512,
									-128,  256, -512,
									 0, -128, -512,
									-128, -128, -512, r ), 
*/
//				new WallRectangle_i_raycast(	 128,  256, 512,
//									 128,  256, 0,
//									 128, -128, 512,
//									 128, -128, 0, r ) };//,
/*				new WallRectangle_i_raycast(	 128,  256, 0,
									 128,  256, -512,
									 128, -128, 0,
									 128, -128, -512, r )
									  };
*/
//for( int polyCnt=0; polyCnt < polys_i_raycast.length; polyCnt++ ) {
//polys_i_raycast[polyCnt].id = polyCnt;
//}

/*		Drawable[] polys_i = new Drawable[] {
				new FloorQuad(	-128,  -128, 512,
									 128, -128, 512,
									 128, -128, 256,
									-128, -128, 256, r ),
				new CeilQuad(	-128,   128, 512,
									 128, 128, 512,
									 128, 128, 256,
									-128, 128, 256, r ),

//				new FloorQuad(	-2048,  -128, -0,
//									 2048, -128, -0,
//									 2048, -128, -4096,
//									-2048, -128, -4096, r ),
				new FloorQuad(	-2048, -96, -256,
					          	 -513, -96, -256,
								 -513, -96, -512,
					 			-2048, -96, -512, r ),

				new CeilQuad(	-2048,  256, -0,
									 2048, 256, -0,
									 2048, 256, -4096,
									-2048, 256, -4096, r )
		};/*,
				new FloorRectangle_i_raycast(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r ),
				new FloorRectangle_i_raycast(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r ),
				new FloorRectangle_i_raycast(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r ),
				new FloorRectangle_i_raycast(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r ),
				new FloorRectangle_i_raycast(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r ),
				new FloorRectangle_i_raycast(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r ),
				new FloorRectangle_i_raycast(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r )
									};*/
//for( int polyCnt=0; polyCnt < polys_i.length; polyCnt++ ) {
//polys_i[polyCnt].id = polyCnt;
//}
									 
/*		WallRectangle_i_nodiv[] polys_i_nodiv = new WallRectangle_i_nodiv[] {
				new WallRectangle_i_nodiv(	-128,  128, -64,
									-128,  128, 512,
									-128, -128, -64,
									-128, -128, 512, r ), 

				new WallRectangle_i_nodiv(	-128,  128, 512,
									 128,  128, 512,
									-128, -128, 512,
									 128, -128, 512, r ), 

				new WallRectangle_i_nodiv(	 128,  128, 512,
									 128,  128, -64,
									 128, -128, 512,
									 128, -128, -64, r ) };

/*		Rectangle3D[] polys = new Rectangle3D[] {
				new Rectangle3D( 	-96,  96, 128,
									-96,  96, 512,
									-96, -96, 128,
									-96, -96, 512 ) };
									
*/		

		Drawable[] polys_i_raycast = new Drawable[] {
				new WallQuad(	-512,  256, -512,
								-512,  256, -256,
								-512,  128, -512,
								-512,  128, -256, r ), 

				new WallQuad(	-512,  -96, -512,
								-512,  -96, -256,
								-512, -128, -512,
								-512, -128, -256, r ), 

				new WallQuad(	-512,  256, -256,
								-512,  256,  512,
								-512, -128, -256,
								-512, -128,  512, r ), 

				new WallQuad(	-512,  256,  512,
								 512,  256,  512,
								-512, -128,  512,
								 512, -128,  512, r ), 

				new WallQuad(	 512,  256, -512,
								-512,  256, -512,
								 512, -128, -512,
								-512, -128, -512, r ), 

				new WallQuad(	 512,  256,  512,
								 512,  256, -512,
								 512, -128,  512,
								 512, -128, -512, r ),
								 
				new FloorQuad(	-512, -128,  512,
					          	 512, -128,  512,
								 512, -128, -512,
					 			-512, -128, -512, r ),
				new CeilQuad(	-512,  256,  512,
					          	 512,  256,  512,
								 512,  256, -512,
					 			-512,  256, -512, r ),
				new ViewportWallQuad(	-512,   129,  -513,
										-512,   129,  -255,
										-512,   -97,  -513,
										-512,   -97,  -255, r ), 
					 			
									 
									  };

		Drawable[] polys_i_raycast2 = new Drawable[] {
				new WallQuad(	-2048,   128,  -512,
								-2048,   128,  -256,
								-2048,   -96,  -512,
								-2048,   -96,  -256, r ), 

				new WallQuad(	-2048,   128,  -256,
								 -513,   128,  -256,
								-2048,   -96,  -256,
								 -513,   -96,  -256, r ), 

				new WallQuad(	-513,  128, -512,
							   -2048,  128, -512,
								-513,  -96, -512,
							   -2048,  -96, -512, r ), 

								 
				new FloorQuad(	-2048, -96, -256,
					          	 -513, -96, -256,
								 -513, -96, -512,
					 			-2048, -96, -512, r ),
				new CeilQuad(	-2048, 128, -256,
					          	 -513, 128, -256,
								 -513, 128, -512,
					 			-2048, 128, -512, r ),
									 
									  };


		((ViewportWallQuad)polys_i_raycast[polys_i_raycast.length-1]).childObjects = polys_i_raycast2;
		try {
			ClipArea clipArea = new ClipArea(96,64);
			
			PolyDrawer polyDrawer = new PolyDrawer(); 
	//		polyDrawer.setPolys(polys_i_nodiv);
//			polyDrawer.setPolys(polys_i);
			polyDrawer.setPolys(polys_i_raycast);
	//		polyDrawer.setPolys(polys);
//			polyDrawer.setViewPort(new Rectangle2D(0, 0, 95, 0, 0, 64, 95, 64));		
//			clipArea.setClipArea(20, 20, 75, 0, 20, 34, 75, 54);
			clipArea.setClipArea(0, 0, 95, 0, 0, 64, 95, 64);
			polyDrawer.setClipArea(clipArea);
			clientFrame.getContentPane().setLayout( new BorderLayout() );
	        clientFrame.getContentPane().add( polyDrawer, BorderLayout.CENTER );
			clientFrame.setBounds( 0, 0, 960, 90 );
			clientFrame.setSize( new Dimension( 960, 90 ) );
	
	        clientFrame.setVisible( true );
		}	catch( Exception e ) {
			System.out.println( "" + e );
			e.printStackTrace();
		}

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

}
