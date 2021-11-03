package graphics.engine.renderer;

import graphics.engine.data.Column2D;
import graphics.engine.data.Rectangle2D;
import graphics.engine.data.Rectangle3D;
import graphics.engine.object.WallRectangle;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Renderer {
	private static Renderer renderer = new Renderer();	

	private 	int 	width			 	= 48<<16;
	private 	int 	height				= 32<<16;
	private 	int 	focuspoint_depth	= 48;
	public	static	int[]	tan				= null;  
	public	static	int[]	tan_inv			= null;  
	public	static	int[]	_sin			= null;  
	public	static	int[]	_sin_inv		= null;  
	public	static	int[]	angleByColumn	= null;  
	public	static	int[]	angleByRow		= null;  
	public static boolean	initialized	= false;

	public Renderer() {
		if( !initialized ) {
			createSinusTable();
			createTangentTable();
			angleByColumn 	= createColumnAngleTable(width);
			angleByRow 		= createColumnAngleTable(height);
			initialized	= true;
		}
		
	}

	public void rotateY( int angle, Rectangle3D worldSpaceRect, Rectangle3D cameraSpaceRect ) {
		int sin = _sin[(angle&0x000007FF)];
		int cos = _sin[((angle+512)&0x000007FF)];

		cameraSpaceRect.x1 = (int) ((((long)worldSpaceRect.z1*cos) + ((long)worldSpaceRect.x1*sin))>>16);
		cameraSpaceRect.z1 = (int) ((((long)worldSpaceRect.z1*sin) - ((long)worldSpaceRect.x1*cos))>>16);

		cameraSpaceRect.x2 = (int) ((((long)worldSpaceRect.z2*cos) + ((long)worldSpaceRect.x2*sin))>>16);
		cameraSpaceRect.z2 = (int) ((((long)worldSpaceRect.z2*sin) - ((long)worldSpaceRect.x2*cos))>>16);

	}
	
	
	/** 
	 *  converts from 3D camera space to 2D screen space
	 *  sets y2 screen coord column.y and sets height
	 *  column.y and height is fixedpoint  
	 */
	public void toColumn2D( int y1, int y2, int z, Column2D column ) {
		long z_factor 	= (focuspoint_depth<<16)/z;
		int y 			= (int)(y2*z_factor);
		column.y 		= height - y;
		column.height	= (int)(y1*z_factor - y);
	}
	
	/** 
	 *  converts from 3D camera space to 2D screen space
	 *  sets y2 screen coord column.y and sets height 
	 *  sets x2 screen coord column.x 
	 *  column.y and height is fixedpoint  
	 */
	public void toColumn2D( int x, int y1, int y2, int z, Column2D column ) {
		long z_factor 	= (focuspoint_depth<<16)/z;
		int y 			= (int) (y2*z_factor);
		column.x		= (int) ((width  + (x*z_factor))>>16);
		column.y 		= height - y;
		column.height	= (int) (y1*z_factor - y);
/*		if( x > 0 && column.x < 0 ) {

System.out.println("(focuspoint_depth<<16)/z: " + ((double)z_factor)/(1<<16) );
System.out.println("y: " + ((double)y)/(1<<16) );
System.out.println("y1: " + y1 );
System.out.println("y2: " + y2 );
System.out.println("z: " + z );
System.out.println("x: " + x );
System.out.println("column.x: " + column.x );
System.out.println("column.y: " + ((double)column.y)/(1<<16) );
System.out.println("column.height: " + ((double)column.height)/(1<<16) );
		}
*/
	}
	
	/** 
	 *  converts from 3D camera space to 2D screen space 
	 *  Returns a Rectangle 3D with only x and y set
	 */
	public void toScreenSpace(WallRectangle rect, Rectangle2D returnedRect) {
		// cx/cz = sx/fz --> sx = cx*fz/cz
		// cy/cz = sy/fz --> sy = cy*fz/cz
		int x1 = rect.x1;
		int x2 = rect.x2;
		int x3 = rect.x3;
		int x4 = rect.x4;
		int y1 = rect.y1;
		int y2 = rect.y2;
		int y3 = rect.y3;
		int y4 = rect.y4;
		int z1 = rect.z1;
		int z2 = rect.z2;
		int z3 = rect.z3;
		int z4 = rect.z4;
		
		returnedRect.x1 = width  + x1*focuspoint_depth/z1;
		returnedRect.y1 = height - y1*focuspoint_depth/z1;
		returnedRect.x2 = width  + x2*focuspoint_depth/z2;
		returnedRect.y2 = height - y2*focuspoint_depth/z2;
		returnedRect.x3 = width  + x3*focuspoint_depth/z3;
		returnedRect.y3 = height - y3*focuspoint_depth/z3;
		returnedRect.x4 = width  + x4*focuspoint_depth/z4;
		returnedRect.y4 = height - y4*focuspoint_depth/z4;

	}

	
	/**
	 * Returns the focuspoint_depth.
	 * @return int
	 */
	public int getFocuspoint_depth() {
		return focuspoint_depth;
	}

	/**
	 * Sets the focuspoint_depth.
	 * @param focuspoint_depth The focuspoint_depth to set
	 */
	public void setFocuspoint_depth(int focuspoint_depth) {
		this.focuspoint_depth = focuspoint_depth;
	}
	
	public static Renderer	getRenderer() {
		return renderer;
	}


	private void createSinusTable() {
System.out.println( "SINE: " );
		_sin 		= new int[2048];
		_sin_inv 	= new int[2048];
		for( int index=0; index<512; index++ ) {
			// Henson Float class gets inaccurate for angles above 90 deg... 
			// However, this is faster anyway
			_sin[index] = (int) (new Float((long)(1<<16)).Mul(Float.sin(( (new Float(Float.PImul2).Mul(new Float(index).Div(new Float(2048))))))).toLong() );
//System.out.println( _sin[index] + ", " );
		}				
		_sin[512] = 1<<16;
		
		for( int index=0; index<512; index++ ) {
			_sin[512+index] = _sin[512-index];
		}	

		for( int index=0; index<512; index++ ) {
			_sin[1024+index] = -_sin[index];
		}	

		for( int index=0; index<512; index++ ) {
			_sin[1536+index] = -_sin[512-index];
		}	
		
		for( int index=0; index<2048; index++ ) {
			if( _sin[index] != 0 ) {
				_sin_inv[index] = (int) (((long)1<<32)/_sin[index]);
			}	else {
				_sin_inv[index] = 0;
			}
		}
	}

	
	public int sin( int angle ) {
		return _sin[(angle&0x000007FF)];
	}

	public int cos( int angle ) {
		return _sin[((angle+512)&0x000007FF)];
	}
	
	public int sin_inv( int angle ) {
		return _sin_inv[(angle&0x000007FF)];
	}

	public int cos_inv( int angle ) {
		return _sin_inv[((angle+512)&0x000007FF)];
	}
	
	private void createTangentTable() {
System.out.println( "TANGENT: " );

		tan = new int[2048];
		tan_inv = new int[2048];
		for( int index=0; index<512; index++ ) {
			long tanL = (new Float((long)(1<<16)).Mul(Float.tan(( (new Float(Float.PImul2).Mul(new Float(index).Div(new Float(2048))))))).toLong() );
			// protect against infinite
			if( tanL > (32767<<16) ) {
				tan[index] = (32767<<16);
			}	else {
				tan[index] = (int) tanL;
			}
//System.out.println( tan[index] + ", " );
		}				
		tan[512] = (32767<<16); // infinite
		for( int index=0; index<512; index++ ) {
			tan[512+index] = -tan[512-index];
		}	

		for( int index=0; index<512; index++ ) {
			tan[1024+index] = tan[index];
		}	

		for( int index=0; index<512; index++ ) {
			tan[1536+index] = -tan[512-index];
		}	

		for( int index=0; index<2048; index++ ) {
			if( tan[index] != 0 ) {
				tan_inv[index] = (int) (((long)1<<32)/tan[index]);
			}	else {
				tan_inv[index] = 0;
			}
		}

	}

	public int  tan( int angle ) {
		return tan[(angle&0x000007FF)];
	}
	
	private int[] createColumnAngleTable(int halfScreenWidth) {
		int	halfWidth	= (halfScreenWidth>>16);
		int[]	array		= new int[halfWidth*2+1];
		int	index		= 0;
		Float	PImul2_inv	= (new Float(1).Div(Float.PImul2));
		for( int x=halfWidth; x>=-halfWidth; x-- ) {
			Float 	tan 	= ((new Float(x).Sub(new Float(5, -1))).Div(focuspoint_depth));
			int 	angle 	= (int) (new Float(2048).Mul(Float.atan(tan).Mul(PImul2_inv)).toLong());
			array[index++]  = angle;
		} 
		return array;	
	}
	
	public int	getColumnAngle( int x ) {
		return angleByColumn[x];
	}

	public int	getRowAngle( int y ) {
		return angleByRow[y];
	}

	/**
	 * Returns the _sin.
	 * @return int[]
	 */
	public static int[] get_sin() {
		return _sin;
	}

	/**
	 * Returns the _sin_inv.
	 * @return int[]
	 */
	public static int[] get_sin_inv() {
		return _sin_inv;
	}

	/**
	 * Returns the angleByColumn.
	 * @return int[]
	 */
	public static int[] getAngleByColumn() {
		return angleByColumn;
	}

	/**
	 * Returns the angleByRow.
	 * @return int[]
	 */
	public static int[] getAngleByRow() {
		return angleByRow;
	}

	/**
	 * Returns the initialized.
	 * @return boolean
	 */
	public static boolean isInitialized() {
		return initialized;
	}

	/**
	 * Returns the tan.
	 * @return int[]
	 */
	public static int[] getTan() {
		return tan;
	}

	/**
	 * Returns the height.
	 * @return int
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the width.
	 * @return int
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the height.
	 * @param height The height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Sets the width.
	 * @param width The width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}
