package graphics.engine.renderer;



/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Renderer {
//	private static Renderer renderer = new Renderer();	

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
			
//System.out.println( "memSine:" + Runtime.getRuntime().freeMemory() );
//System.gc();
			createSinusTable();
//System.gc();
//System.out.println( "memSine:" + Runtime.getRuntime().freeMemory() );
//System.gc();
			createTangentTable();
//System.gc();
//System.out.println( "memTang:" + Runtime.getRuntime().freeMemory() );
//System.gc();
			angleByColumn 	= createColumnAngleTable(width);
//System.gc();
//System.out.println( "memCol:" + Runtime.getRuntime().freeMemory() );
//System.gc();
			angleByRow 		= createColumnAngleTable(height);
System.gc();
//System.out.println( "memRow:" + Runtime.getRuntime().freeMemory() );
			initialized	= true;
		}
		
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
	
//	public static Renderer	getRenderer() {
//		return renderer;
//	}


	private void createSinusTable() {
		_sin 		= new int[2048];
		_sin_inv 	= new int[2048];
		for( int index=0; index<512; index++ ) {
			// Henson Float class gets inaccurate for angles above 90 deg... 
			// However, this is faster anyway
			_sin[index] = Sine.sin_f[index];//(int) (new Float((long)(1<<16)).Mul(Float.sin(( (new Float(Float.PImul2).Mul(new Float(index).Div(new Float(2048))))))).toLong() );
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
		tan = new int[2048];
		tan_inv = new int[2048];
		
		for( int index=0; index<512; index++ ) {
			long tanL = Tangent._tan_f[index];//(new Float((long)(1<<16)).Mul(Float.tan(( (new Float(Float.PImul2).Mul(new Float(index).Div(new Float(2048))))))).toLong() );
			// protect against infinite
			if( tanL > (32767<<16) ) {
				tan[index] = (32767<<16);
			}	else {
				tan[index] = (int) tanL;
			}
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
