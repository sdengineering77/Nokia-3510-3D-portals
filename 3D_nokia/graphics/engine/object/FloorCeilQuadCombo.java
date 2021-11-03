package graphics.engine.object;

import graphics.engine.drawer.ClipArea;
import graphics.engine.renderer.Float;
import graphics.engine.renderer.Renderer;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FloorCeilQuadCombo implements Drawable {
	public final static int	WALL_ALIGNS_MAP_Y	= 0;
	public final static int	WALL_ALIGNS_MAP_X	= 1;
	public final static int	ITERATION_CEIL		= 1;

	public static int[] 		align 		= new int[4];//WALL_ALIGNS_MAP_X; 	
	public static int[] 		angle_wall	= new int[4];

	public int[]	x_world = new int[4];
	public int[]	z_world = new int[4];

	public int 	y_world;
	public int 	y_ceil;
	public int[]	length_world = new int[4];

	public int[] length_left_correction 	= new int[4];	
	public int[] length_right_correction 	= new int[4];	
	public static long renderer_focuspointDepth_f;
	public static long renderer_width_f;
	public static long renderer_height_f;
	public static int  scanheight;
	public static int  scanwidth;
	public static Renderer 		renderer;
	public int 			id				= 0;
	public static int[]			textureSpaceSin	= new int[4]; 
	public static int[]			textureSpaceCos	= new int[4]; 
		
	public final static 	int[] y_ceil_from_right		= new int[2];
	public final static 	int[] x_ceil_from_right		= new int[2];
	public final static 	int[] y_ceil_to_right	    	= new int[2];
	public final static 	int[] x_ceil_to_right	    	= new int[2];
	public final static 	long[] z_ceil_to_right	    	= new long[2];
	public final static 	int[] x_from_right				= new int[2];// { x1, x2 };
	public final static 	int[] y_from_right				= new int[2];// { y1, y2 };
	public final static 	int[] x_to_right				= new int[2];// { x2, x3 };
	public final static 	int[] y_to_right				= new int[2];// { y2, y3 };
	public final static 	int[] y_ceil_from_left			= new int[2];
	public final static 	int[] x_ceil_from_left			= new int[2];
	public final static 	int[] y_ceil_to_left	    	= new int[2];
	public final static 	int[] x_ceil_to_left	    	= new int[2];
	public final static 	long[] z_ceil_to_left	    	= new long[2];
	public final static 	int[] x_from_left				= new int[2];// { x4, x1 };
	public final static 	int[] y_from_left				= new int[2];// { y4, y1 };
	public final static 	int[] x_to_left  				= new int[2];// { x3, x4 };
	public final static 	int[] y_to_left				= new int[2];// { y3, y4 };
	public final static 	int[] dxdy_left				= new int[2];
	public final static 	int[] dxdy_right				= new int[2];
	public final static 	long[] z_to_right 				= new long[2];
	public final static 	long[] z_to_left				= new long[2];

	private final static short[] texture_floor = new short[] { 
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000
	};

	private final static short[] texture_ceil = new short[] { 
		(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,
		(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xF000,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,
		(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xF000,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,
		(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xF000,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000
	};

		
	public FloorCeilQuadCombo( int x1, int z1,
						int x2, int z2,
						int x3, int z3,
						int x4, int z4, 
						int floor, int ceil, Renderer renderer) {
	
		x_world[0] = x1; 
		x_world[1] = x2;
		x_world[2] = x3; 
		x_world[3] = x4;
		z_world[0] = z1; 
		z_world[1] = z2;
		z_world[2] = z3; 
		z_world[3] = z4;

		y_world = floor;
//		this.ceil_height = -ceil_height; // !!!!!
		this.y_ceil = ceil; // !!!!!
		this.renderer = renderer;
		this.renderer_focuspointDepth_f = (renderer.getFocuspoint_depth()<<16);
		this.renderer_width_f			= renderer.getWidth();
		this.renderer_height_f			= renderer.getHeight();
		this.scanheight				= (int) (renderer_height_f>>15);
		this.scanwidth					= (int) (renderer_width_f>>15);
		int next_vertice = 1;
		for( int vertice=0; vertice<4; vertice++ ) {
			int xDiff = (x_world[next_vertice]-x_world[vertice]);
			int zDiff = (z_world[next_vertice]-z_world[vertice]);
			if( x_world[vertice] == x_world[next_vertice] ) {
				align[vertice]	= WALL_ALIGNS_MAP_Y;
				if( z_world[vertice] < z_world[next_vertice] ) {
					angle_wall[vertice] = 1024;
				} else {
					angle_wall[vertice] = 0;
				}
			}	else {
				align[vertice]	= WALL_ALIGNS_MAP_X;
				if( x_world[vertice] < x_world[next_vertice] ) {
					angle_wall[vertice] = 512;
				}	else {
					angle_wall[vertice] = 1536;
				}
			}

			next_vertice = ((next_vertice+1)&0x00000003);
			
			textureSpaceSin[vertice] 			= (renderer.sin(angle_wall[vertice])>>16); 
			textureSpaceCos[vertice] 			= (renderer.cos(angle_wall[vertice])>>16); 

			length_world[vertice] 				= (int) (Float.sqrt( new Float(xDiff*xDiff + zDiff*zDiff) ).toLong());
			length_left_correction[vertice] 	= -length_world[vertice]/100;	
			length_right_correction[vertice]	= length_world[vertice] - length_left_correction[vertice];	

		}
		
	}

	/**
	 * 	Draws an horizontal aligned textured quad.<br><br>
	 * 
	 * Note that this method should be declared synchronized 
	 * but is not due Me wanting speeeeeed!
	 */
	public boolean draw( short[] buffer, int cam_x, int cam_y, int cam_z, int angle, ClipArea clipArea ) {
		boolean retVal = true;

		boolean floorMayBeVisible;
		boolean ceilMayBeVisible;
		int y_camera_floor  = y_world - cam_y;
		int y_camera_ceil   = y_ceil - cam_y;

// TODO: BELOW METHOD FOR BOTH FLOOR AND CEIL, SET A BOOLEAN
//		 COMPARE BELOW MUST BE TRUE IF EITHER FLOOR OR CEIL 
//		 CAN BE SEEN...
// 		 PERHAPS TODO2: ROTATE CEIL Y POINT FULLY AND ONLY RENDER 
//		 IF VISIBLE?
//
// DONE (TODO2 left open)
		if( y_camera_floor < 0 ) {
			floorMayBeVisible = true;
		}	else {
			floorMayBeVisible = false;
		}
		if( y_camera_ceil > 0 ) {
			ceilMayBeVisible = true;
		}	else {
			ceilMayBeVisible = false;
		}



		if( floorMayBeVisible || ceilMayBeVisible ) {

		int	viewportX_left 			= clipArea.viewportX_left;
		int  	viewportX_right 		= clipArea.viewportX_right;

		int[] 	viewportX_left_array 	= clipArea.viewportX_left_array;
		int[]	viewportX_right_array 	= clipArea.viewportX_right_array;
		int	viewportY_top 			= clipArea.viewportY_top;
		int  	viewportY_bottom 		= clipArea.viewportY_bottom;

		int[] x_from_right		= this.x_from_right;
		int[] y_from_right		= this.y_from_right;
		int[] x_to_right		= this.x_to_right;
		int[] y_to_right		= this.y_to_right;
		int[] x_from_left		= this.x_from_left;
		int[] y_from_left		= this.y_from_left;
		int[] x_to_left  		= this.x_to_left;
		int[] y_to_left		= this.y_to_left;
// CHECK HERE
		int[] y_ceil_from_left = this.y_ceil_from_left;
		int[] y_ceil_to_left = this.y_ceil_to_left;
		int[] y_ceil_from_right = this.y_ceil_from_right;
		int[] y_ceil_to_right = this.y_ceil_to_right;
		int[] x_ceil_from_left = this.x_ceil_from_left;
		int[] x_ceil_to_left = this.x_ceil_to_left;
		int[] x_ceil_from_right = this.x_ceil_from_right;
		int[] x_ceil_to_right = this.x_ceil_to_right;
		long[] z_ceil_to_left = this.z_ceil_to_left;
		long[] z_ceil_to_right = this.z_ceil_to_right;
		
		long[] z_to_right		= this.z_to_right;
		long[] z_to_left		= this.z_to_left;
		int[] dxdy_left		= this.dxdy_left;
		int[] dxdy_right		= this.dxdy_right;
		int   scanheight		= this.scanheight;
	    int[]	angleByColumn 	= renderer.angleByColumn;
	    int[]	angleByRow 		= renderer.angleByRow;
		int[]	sin_inv 		= renderer._sin_inv;
		int[]	sin 			= renderer._sin;
		int[]	tan 			= renderer.tan;
		int[]	tan_inv 		= renderer.tan_inv;
		int   scanwidth		= this.scanwidth;
		int   scanwidth_f		= (scanwidth<<16);


/**
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
		int poly_x_left_f  = viewportX_right;//(x_from_left[0]<<16);
		int poly_x_right_f = viewportX_left;//(x_from_right[0]<<16);
		int y_floor_top = viewportY_bottom;
		int y_floor_bottom = viewportY_top;
		int y_ceil_top = viewportY_bottom;
		int y_ceil_bottom = viewportY_top;

		int num_vectors_left		= 0;
		int num_vectors_right		= 0;
		int num_vectors			= 0;
		int num_linear_vectors		= 0;
		int num_nonlinear_vectors	= 0;

		for( int vectors=0; vectors<4; vectors++ ) {
//			int x_from_screen;
//			int y_from_screen;
//			int x_to_screen;
//			int y_to_screen;
			/** 
			 * Clip poly in horizontal viewport (also discarts non visible poly)
			 * Determine left and right x_screen coordinates in perspective
			 * Determine base y screen coordinate and height in perspective
			 * Determine left and right vertical texture step size per screen pixel
			 */
	
			// determine distance and basepoint int poly space
			// distance is distance between camera location in world perpenticular to poly
			// basepoint is distance between first point of poly (x1,z1) and
			//           the point that lies perpenticular with camera location
			long distance;
			int basepoint;
			int isInFront_left;
			int isInFront_right;
			
			int columnAngleX_left 	= angleByColumn[viewportX_left];
			int columnAngleX_right	= angleByColumn[viewportX_right];
			int angle_cos			= angle + 512;
			int angle_sin_clip_left 	= ((angle + columnAngleX_left)&0x000007FF);
			int angle_sin_clip_right 	= ((angle + columnAngleX_right)&0x000007FF);
			int angle_cos_clip_left 	= ((angle_cos + columnAngleX_left)&0x000007FF);
			int angle_cos_clip_right 	= ((angle_cos + columnAngleX_right)&0x000007FF);
			if( align[vectors] == WALL_ALIGNS_MAP_X ) {
				int dz 		= z_world[vectors] - cam_z;
				isInFront_left	= dz*sin[angle_sin_clip_left];
				isInFront_right	= dz*sin[angle_sin_clip_right];
				distance   		= dz*textureSpaceSin[vectors];  
				basepoint  		= (cam_x - x_world[vectors])*textureSpaceSin[vectors];//renderer.sin(angle_wall))>>16); 
			}	else {
				int dx 		= x_world[vectors] - cam_x;
				isInFront_left	= dx*sin[angle_cos_clip_left];
				isInFront_right	= dx*sin[angle_cos_clip_right];
				distance   		= dx*textureSpaceCos[vectors];  
				basepoint  		= (z_world[vectors] - cam_z)*textureSpaceCos[vectors]; 
			}

			if( distance > 1 || distance < -1 ) {
			int poly_in_clip_length			 = length_world[vectors];
	
			// convert view angle to "poly" space
			int angle_y_poly	= ((angle_wall[vectors] - angle)&0x000007FF);
			if( angle_y_poly > 1024 ) {
				angle_y_poly -= 2048;
			}
			// angle of most left and right pixel in screen and position on poly (may be outside the poly)
			int angle_y_clip_left 		= angle_y_poly - columnAngleX_left; 
			long position_clip_left	= basepoint + ((distance*tan[angle_y_clip_left&0x000007FF])>>16);
			
			int angle_y_clip_right 	= angle_y_poly - columnAngleX_right; 
			long position_clip_right	= basepoint + ((distance*tan[angle_y_clip_right&0x000007FF])>>16);
	
			// angles are valid if between -90 and +90 degrees in relation to the poly
			boolean angle_y_left_inClip 	= 	(angle_y_clip_left >-512  	&& angle_y_clip_left <512);
			boolean angle_y_right_inClip 	= 	(angle_y_clip_right>-512 	&& angle_y_clip_right<512);
	
			if( (angle_y_left_inClip && isInFront_right<=0 && isInFront_left>0 && position_clip_left<poly_in_clip_length) ||
				(!angle_y_left_inClip && isInFront_right<=0 && isInFront_left>0 && position_clip_left>0) ||
				(!angle_y_right_inClip && isInFront_left<=0 && isInFront_right>0 && position_clip_right<poly_in_clip_length) ||
				(angle_y_right_inClip && isInFront_left<=0 && isInFront_right>0 && position_clip_right>0) ||
				(angle_y_right_inClip && angle_y_left_inClip && isInFront_left>0 && isInFront_right>0 && position_clip_left<poly_in_clip_length && position_clip_right>0) ||
				(!angle_y_right_inClip && !angle_y_left_inClip && isInFront_left>0 && isInFront_right>0 && position_clip_left>0 && position_clip_right<poly_in_clip_length)
								) {
				int  x_to;
				int  y_to;
				long z_to;
				int  y_ceil_to;
				int  x_from;
				int  y_from;
				long z_from;
				int  y_ceil_from;

				// determine x of left and right side
				// check if poly hits right or left clip. if so calculate point on
				// poly and translate z else determine screen space x
				// left side
				if ( distance < 0 ) {
					if( (isInFront_left>=0 && position_clip_left>=length_left_correction[vectors] && position_clip_left<=length_right_correction[vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						z_to   			= (((distance * sin_inv[(angle_y_clip_left+512)&0x000007FF]) * sin[(angle_y_clip_left-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor	= (renderer_focuspointDepth_f)/z_to;
						x_to   			= viewportX_left;						
						y_to   			= (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_to		= (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_to  		= (int) ((ceil_height*z_factor)>>16) + y_to;
					}	else {
						// not on poly, transform to screen space and setup 
						// left poly attributes
						// assume camera space coordinates for x1 etc
						long length_r_bp = -(length_world[vectors]-basepoint);

						int cos_angle_y_poly 	= sin[((angle_y_poly + 512)&0x000007FF)];
						int sin_angle_y_poly 	= sin[(angle_y_poly&0x000007FF)];
						z_to 					= ((cos_angle_y_poly * distance    - sin_angle_y_poly * length_r_bp)>>16);
						long x_camera 			= ((cos_angle_y_poly * length_r_bp + sin_angle_y_poly * distance)>>16); // actual calculation ((-renderer.cos(angle_y) * basepoint - renderer.sin(angle_y) * distance_abs)>>16)
						long z_factor 			= (renderer_focuspointDepth_f)/z_to;

						x_to   = (int) ((renderer_width_f  - (x_camera*z_factor))>>16);						
						y_to   = (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_to   = (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_to  = (int) ((ceil_height*z_factor)>>16) + y_to;
					}
					
					// right side (use worldSpaceRect.length)
					if( (isInFront_right>=0 && position_clip_right>=length_left_correction[vectors] && position_clip_right<=length_right_correction[vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						// from behind?
						z_from   = (((distance * sin_inv[(angle_y_clip_right+512)&0x000007FF])*sin[(angle_y_clip_right-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor 	= (renderer_focuspointDepth_f)/z_from;
						x_from   = viewportX_right;						
						y_from   = (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_from   = (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_from  = (int) ((ceil_height*z_factor)>>16) + y_from;
					}	else {
						// not on poly, transform to screen space and setup 
						// left poly attributes
						long length_r_bp 		= -basepoint;
						int cos_angle_y_poly 	= sin[((angle_y_poly + 512)&0x000007FF)];
						int sin_angle_y_poly 	= sin[(angle_y_poly&0x000007FF)];
						z_from 					= ((cos_angle_y_poly * distance    + sin_angle_y_poly * length_r_bp)>>16);
						long x_camera 		= ((cos_angle_y_poly * length_r_bp - sin_angle_y_poly * distance)>>16);
						long z_factor 		= (renderer_focuspointDepth_f)/z_from;
						x_from 				= (int) ((renderer_width_f  + (x_camera*z_factor))>>16);						
						y_from 				= (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_from 		= (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_from    		= (int) ((ceil_height*z_factor)>>16) + y_from;
					}
				}	else {
					if( (isInFront_left>=0 && position_clip_left>=length_left_correction[vectors] && position_clip_left<=length_right_correction[vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						z_from = (((distance * sin_inv[(angle_y_clip_left+512)&0x000007FF]) * sin[(angle_y_clip_left-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor		= (renderer_focuspointDepth_f)/z_from;
						x_from = viewportX_left;						
						y_from = (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_from = (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_from  = (int) ((ceil_height*z_factor)>>16) + y_from;
					}	else {
						// not on poly, transform to screen space and setup 
						// left poly attributes
						// assume camera space coordinates for x1 etc
						long length_r_bp = basepoint;

						int cos_angle_y_poly 	= sin[((angle_y_poly + 512)&0x000007FF)];
						int sin_angle_y_poly 	= sin[(angle_y_poly&0x000007FF)];
						z_from   				= ((cos_angle_y_poly * distance    - sin_angle_y_poly * length_r_bp)>>16);
						long x_camera 	  	  	= ((cos_angle_y_poly * length_r_bp + sin_angle_y_poly * distance)>>16); // actual calculation ((-renderer.cos(angle_y) * basepoint - renderer.sin(angle_y) * distance_abs)>>16)
						long z_factor 			= (renderer_focuspointDepth_f)/z_from;
	
						x_from = (int) ((renderer_width_f  - (x_camera*z_factor))>>16);						
						y_from = (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_from = (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_from  = (int) ((ceil_height*z_factor)>>16) + y_from;
	
					}
					
					// right side (use worldSpaceRect.length)
					if( (isInFront_right>=0 && position_clip_right>=length_left_correction[vectors] && position_clip_right<=length_right_correction[vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						z_to   = (((distance * sin_inv[(angle_y_clip_right+512)&0x000007FF])*sin[(angle_y_clip_right-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor 	= (renderer_focuspointDepth_f)/z_to;
						x_to   = viewportX_right;						
						y_to   = (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_to   = (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_to  = (int) ((ceil_height*z_factor)>>16) + y_to;
					}	else {
						// not on poly, transform to screen space and setup 
						// left poly attributes
						long length_r_bp 		= (length_world[vectors]-basepoint);
						int cos_angle_y_poly 	= sin[((angle_y_poly + 512)&0x000007FF)];
						int sin_angle_y_poly 	= sin[(angle_y_poly&0x000007FF)];
						z_to 					= ((cos_angle_y_poly * distance    + sin_angle_y_poly * length_r_bp)>>16);
						long x_camera 			= ((cos_angle_y_poly * length_r_bp - sin_angle_y_poly * distance)>>16);
						long z_factor 	= (renderer_focuspointDepth_f)/z_to;
						x_to   			= (int) ((renderer_width_f  + (x_camera*z_factor))>>16);						
						y_to   			= (int) ((renderer_height_f - (y_camera_floor*z_factor))>>16);
						y_ceil_to   		= (int) ((renderer_height_f - (y_camera_ceil*z_factor))>>16);
//						y_ceil_to  		= (int) ((ceil_height*z_factor)>>16) + y_to;
					}

				}

				// if z_to value bigger(more depth) than
				// z_from value, the vector is targetted left
				// otherwise it is targetted right.
				// If z values are equal it is discarded, but used for poly stuff.
				if( z_to > (z_from+1) ) {
					// if y from equals y to this is a horizontal line
					// and must be interpreted as a horizontal offset jump
					// (and a dxdy of 0). This means that x_from must contain
					// the original x_to.
					if( y_to == y_from ) {
						x_from_left[num_vectors_left] = x_from;
						y_from_left[num_vectors_left] = y_from;
						z_to_left[num_vectors_left] = ((z_from + z_to)>>1);
						x_to_left[num_vectors_left] = x_to;
						y_to_left[num_vectors_left] = y_to;
						
//						num_vectors_left++;
						
					}	else {
						// must be targetted left, but quad drawing
						// requires y_to to be bigger than y_from
						// since this is a floor, and bigger z_to means smaller y_to
						// they must be swapped
						x_from_left[num_vectors_left] = x_to;
						y_from_left[num_vectors_left] = y_to;
						z_to_left[num_vectors_left] = ((z_from + z_to)>>1);
						x_to_left[num_vectors_left] = x_from;
						y_to_left[num_vectors_left] = y_from;
//						num_vectors_left++;
					}

					if( y_ceil_to == y_ceil_from ) {
						x_ceil_from_left[num_vectors_left] = x_from;
						y_ceil_from_left[num_vectors_left] = y_ceil_from;
						z_ceil_to_left[num_vectors_left] = ((z_from + z_to)>>1);
						x_ceil_to_left[num_vectors_left] = x_to;
						y_ceil_to_left[num_vectors_left] = y_ceil_to;
						
						num_vectors_left++;
						
					}	else {
						// must be targetted left, but quad drawing
						// requires y_to to be bigger than y_from
						// since this is a floor, and bigger z_to means smaller y_to
						// they must be swapped
						x_ceil_from_left[num_vectors_left] = x_to;
						y_ceil_from_left[num_vectors_left] = y_ceil_to;
						z_ceil_to_left[num_vectors_left] = ((z_from + z_to)>>1);
						x_ceil_to_left[num_vectors_left] = x_from;
						y_ceil_to_left[num_vectors_left] = y_ceil_from;
						num_vectors_left++;
					}

				}	else
				if( z_to < (z_from-1) ) {
					// if y from equals previous y from
					// just adjust first
					if( y_from == y_to ) {
						// find most left x
						x_from_right[num_vectors_right] = x_to;
						y_from_right[num_vectors_right] = y_to;
						
						z_to_right[num_vectors_right] = ((z_from + z_to)>>1);
						x_to_right[num_vectors_right] = x_from;
						y_to_right[num_vectors_right] = y_from;
//						num_vectors_right++;
					}	else {
						x_from_right[num_vectors_right] = x_from;
						y_from_right[num_vectors_right] = y_from;
						z_to_right[num_vectors_right] = ((z_from + z_to)>>1);
						x_to_right[num_vectors_right] = x_to;
						y_to_right[num_vectors_right] = y_to;
//						num_vectors_right++;
					}

					if( y_ceil_from == y_ceil_to ) {
						// find most left x
						x_ceil_from_right[num_vectors_right] = x_to;
						y_ceil_from_right[num_vectors_right] = y_ceil_to;
						z_ceil_to_right[num_vectors_right] = ((z_from + z_to)>>1);
						x_ceil_to_right[num_vectors_right] = x_from;
						y_ceil_to_right[num_vectors_right] = y_ceil_from;
						num_vectors_right++;
					}	else {
						x_ceil_from_right[num_vectors_right] = x_from;
						y_ceil_from_right[num_vectors_right] = y_ceil_from;
						z_ceil_to_right[num_vectors_right] = ((z_from + z_to)>>1);
						x_ceil_to_right[num_vectors_right] = x_to;
						y_ceil_to_right[num_vectors_right] = y_ceil_to;
						num_vectors_right++;
					}

				}
				
				num_vectors++;				
				// if x_to > x_from increase num_linear_vectors
				if( x_to >= x_from ) {
					num_linear_vectors++;
				}

				// determine left and right
				if( poly_x_left_f > x_from) {
					poly_x_left_f = x_from;
				}			
				if( poly_x_left_f > x_to) {
					poly_x_left_f = x_to;
				}			
	
				if( poly_x_right_f < x_from) {
					poly_x_right_f = x_from;
				}			
				if( poly_x_right_f < x_to) {
					poly_x_right_f = x_to;
				}			
	
				// determine top and bottom
				if( y_floor_top > y_from) {
					y_floor_top = y_from;
				}	
				if( y_floor_top > y_to) {
					y_floor_top = y_to;
				}
	
				if( y_floor_bottom < y_from) {
					y_floor_bottom = y_from;
				}
				
				if( y_floor_bottom < y_to) {
					y_floor_bottom = y_to;
				}

				// determine top and bottom
				if( y_ceil_top > y_ceil_from) {
					y_ceil_top = y_ceil_from;
				}	
				if( y_ceil_top > y_ceil_to) {
					y_ceil_top = y_ceil_to;
				}
	
				if( y_ceil_bottom < y_ceil_from) {
					y_ceil_bottom = y_ceil_from;
				}
				
				if( y_ceil_bottom < y_ceil_to) {
					y_ceil_bottom = y_ceil_to;
				}

			}
			}
		}

		// if num_linear_vectors equals total number of vectors
		// the quad is not convex and has no bottom
		// and the bottom will be equal to viewport bottom
		if( num_linear_vectors >= num_vectors ) {
			// do bottom stuff
			y_floor_bottom = viewportY_bottom;
			y_ceil_top = viewportY_top;
		}

		for( int iteration=2; iteration>0; iteration-- ) {
			short[] texture;
			int y_bottom;
			int y_top;
			int y_camera;

			if( (iteration == ITERATION_CEIL && ceilMayBeVisible) ||
				(iteration != ITERATION_CEIL && floorMayBeVisible)) {
			
				if( iteration == ITERATION_CEIL ) {

	
	// TODO: IF NO CEIL VISIBLE, BREAK	
	// DONE			
					texture		= this.texture_ceil;
					y_camera = y_camera_ceil;
					y_top = y_ceil_top;
					y_bottom = y_ceil_bottom;

					y_from_left = y_ceil_from_left;
					y_to_left = y_ceil_to_left;
					y_from_right = y_ceil_from_right;
					y_to_right = y_ceil_to_right;
					x_from_left = x_ceil_from_left;
					x_to_left = x_ceil_to_left;
					x_from_right = x_ceil_from_right;
					x_to_right = x_ceil_to_right;
	
					z_to_left = z_ceil_to_left;
					z_to_right = z_ceil_to_right;
	
	// TODO: DO CEIL SORT
	// DONE
					/**
					 * sort vectors on depth if neccessary
					 * for floors the greater the z value, the smaller the
					 * y value will be. This means if second entry in array
					 * has bigger depth it must be swapped with first entry,
					 * which must have the lowest y value.
					 */
					if( num_vectors_left > 1 ) {
						if( z_to_left[1] > z_to_left[0] ) {
							int y_from 	= y_from_left[0];
							int y_to   	= y_to_left[0];
							y_from_left[0] 	= y_from_left[1];
							y_from_left[1] 	= y_from;
							y_to_left[0] 	= y_to_left[1];
							y_to_left[1] 	= y_to;
							int x_from 	= x_from_left[0];
							int x_to   	= x_to_left[0];
							x_from_left[0] 	= x_from_left[1];
							x_from_left[1] 	= x_from;
							x_to_left[0] 	= x_to_left[1];
							x_to_left[1] 	= x_to;
						}
						/**
						 * determine direction coefficients of right and left side vectors
						 */
						int height 		= y_from_left[0] - y_to_left[0];
						if( height != 0 ) {  
							int width		= x_to_left[0] - x_from_left[0];
							dxdy_left[0]  	= (width<<16)/height;
						}	else {
							dxdy_left[0]  	= 0;
						}
						height 		= y_from_left[1] - y_to_left[1];
						if( height != 0 ) {  
							int width 		= x_to_left[1] - x_from_left[1];
							dxdy_left[1]  	= (width<<16)/height;
						}	else {
							dxdy_left[1]  	= 0;
						}
					}	else {
						int height 		= y_from_left[0] - y_to_left[0];
						if( height != 0 ) {  
							int width 		= x_to_left[0] - x_from_left[0];
							dxdy_left[0]  	= (width<<16)/height;
						}	else {
							dxdy_left[0]  	= 0;
						}
					}
			
					if( num_vectors_right > 1 ) {
						if( z_to_right[1] > z_to_right[0] ) {
							int y_from 	= y_from_right[0];
							int y_to   	= y_to_right[0];
							y_from_right[0] = y_from_right[1];
							y_from_right[1] = y_from;
							y_to_right[0] 	= y_to_right[1];
							y_to_right[1] 	= y_to;
							int x_from 	= x_from_right[0];
							int x_to   	= x_to_right[0];
							x_from_right[0] = x_from_right[1];
							x_from_right[1] = x_from;
							x_to_right[0] 	= x_to_right[1];
							x_to_right[1] 	= x_to;
						}
						/**
						 * determine direction coefficients of right and left side vectors
						 */
						int height = y_from_right[0] - y_to_right[0];
						if( height != 0 ) {  
							int width 		= x_to_right[0] - x_from_right[0];
							dxdy_right[0]   = (width<<16)/height;
						}	else {
							dxdy_right[0]  	= 0;
						}
						height = y_from_right[1] - y_to_right[1];
						if( height != 0 ) {  
							int width 		= x_to_right[1] - x_from_right[1];
							dxdy_right[1]   = (width<<16)/height;
						}	else {
							dxdy_right[1]  	= 0;
						}
					}	else {
						int height = y_from_right[0] - y_to_right[0];
						if( height != 0 ) {  
							int width 		= x_to_right[0] - x_from_right[0];
							dxdy_right[0]   = (width<<16)/height;
						}	else {
							dxdy_right[0]  	= 0;
						}
					}
	
				}	else {
					texture		= this.texture_floor;
					
					y_camera = y_camera_floor;
					y_top = y_floor_top;
					y_bottom = y_floor_bottom;
					
					/**
					 * sort vectors on depth if neccessary
					 * for floors the greater the z value, the smaller the
					 * y value will be. This means if second entry in array
					 * has bigger depth it must be swapped with first entry,
					 * which must have the lowest y value.
					 */
					if( num_vectors_left > 1 ) {
						if( z_to_left[1] > z_to_left[0] ) {
							int y_from 	= y_from_left[0];
							int y_to   	= y_to_left[0];
							y_from_left[0] 	= y_from_left[1];
							y_from_left[1] 	= y_from;
							y_to_left[0] 	= y_to_left[1];
							y_to_left[1] 	= y_to;
							int x_from 	= x_from_left[0];
							int x_to   	= x_to_left[0];
							x_from_left[0] 	= x_from_left[1];
							x_from_left[1] 	= x_from;
							x_to_left[0] 	= x_to_left[1];
							x_to_left[1] 	= x_to;
						}
						/**
						 * determine direction coefficients of right and left side vectors
						 */
						int height 		= y_to_left[0] - y_from_left[0];
						if( height > 0 ) {  
							int width		= x_to_left[0] - x_from_left[0];
							dxdy_left[0]  	= (width<<16)/height;
						}	else {
							dxdy_left[0]  	= 0;
						}
						height 				= y_to_left[1] - y_from_left[1];
						if( height > 0 ) {  
							int width 		= x_to_left[1] - x_from_left[1];
							dxdy_left[1]  	= (width<<16)/height;
						}	else {
							dxdy_left[1]  	= 0;
						}
					}	else {
						int height 		= y_to_left[0] - y_from_left[0];
						if( height > 0 ) {  
							int width 		= x_to_left[0] - x_from_left[0];
							dxdy_left[0]  	= (width<<16)/height;
						}	else {
							dxdy_left[0]  	= 0;
						}
					}
			
					if( num_vectors_right > 1 ) {
						if( z_to_right[1] > z_to_right[0] ) {
							int y_from 	= y_from_right[0];
							int y_to   	= y_to_right[0];
							y_from_right[0] = y_from_right[1];
							y_from_right[1] = y_from;
							y_to_right[0] 	= y_to_right[1];
							y_to_right[1] 	= y_to;
							int x_from 	= x_from_right[0];
							int x_to   	= x_to_right[0];
							x_from_right[0] = x_from_right[1];
							x_from_right[1] = x_from;
							x_to_right[0] 	= x_to_right[1];
							x_to_right[1] 	= x_to;
						}
						/**
						 * determine direction coefficients of right and left side vectors
						 */
						int height = y_to_right[0] - y_from_right[0];
						if( height > 0 ) {  
							int width 		= x_to_right[0] - x_from_right[0];
							dxdy_right[0]   = (width<<16)/height;
						}	else {
							dxdy_right[0]  	= 0;
						}
						height = y_to_right[1] - y_from_right[1];
						if( height > 0 ) {  
							int width 		= x_to_right[1] - x_from_right[1];
							dxdy_right[1]   = (width<<16)/height;
						}	else {
							dxdy_right[1]  	= 0;
						}
					}	else {
						int height = y_to_right[0] - y_from_right[0];
						if( height > 0 ) {  
							int width 		= x_to_right[0] - x_from_right[0];
							dxdy_right[0]   = (width<<16)/height;
						}	else {
							dxdy_right[0]  	= 0;
						}
					}
				}	
				
		// must get rewritten with future viewportY_bottom
				// Very late y_bottom clip.... Must be optimized to front...
				// pref directly after vector calculation
				if( y_top < viewportY_top ) {
					y_top = viewportY_top;
				}
				if( y_bottom > viewportY_bottom ) {
					y_bottom = viewportY_bottom;
				}
		
				if( y_bottom > y_top ) {
					/**
					 * Draw the quad. The left and right arrays contain the vertices
					 * and direction coefficients of each vector.
					 * 
					 * For each row, the row value is compared to both vector arrays.
					 * If row value between vector top and bottom y values for first time,
					 * the left or right direction coefficients are set to the array coefficient
					 * value. The x left and right values are set to the "from" vertex of the
					 * vector...
					 */
					int renderer_width_f;
					int offset_y;
					int currDxdy_left;
					int currDxdy_right;
	
					if( iteration == ITERATION_CEIL ) {
						renderer_width_f	= y_bottom*scanwidth_f + ((int)this.renderer_width_f);
						offset_y			= y_bottom*scanwidth_f;
						currDxdy_left 		= -scanwidth_f;
						currDxdy_right 		= -scanwidth_f;
					}	else {
						renderer_width_f	= y_top*scanwidth_f + ((int)this.renderer_width_f);
						offset_y			= y_top*scanwidth_f;
						currDxdy_left 		= scanwidth_f;
						currDxdy_right 		= scanwidth_f;
					}				
	
					// calculate 1/z values for y_top and y_bottom... These
					// are used for texture step size (texels per pixel) interpolation
					int cam_x_f 			= (cam_x<<16); 
					int cam_z_f 			= (cam_z<<16);
		
					int angle_v_y_top	  	= angleByRow[y_top];
					int z_ray_y_top    	= (((y_camera) * tan_inv[(angle_v_y_top&0x000007FF)])>>16);
					int dt_y_top_inv_f 	= (z_ray_y_top<<16)/((int)(renderer_focuspointDepth_f>>16));
					
					int angle_v_y_bottom	= angleByRow[y_bottom];
					int z_ray_y_bottom    	= (((y_camera) * tan_inv[(angle_v_y_bottom&0x000007FF)])>>16);
					int dt_y_bottom_inv_f 	= (z_ray_y_bottom<<16)/((int)(renderer_focuspointDepth_f>>16));
		
					int d_dt_f 			= ((((dt_y_top_inv_f-dt_y_bottom_inv_f)))/(z_ray_y_top-z_ray_y_bottom));
					int cos_center			= sin[((angle+512)&0x000007FF)];
					int sin_center			= sin[angle];
	
					int currVectorIdx_left	= 0;
					int currVectorIdx_right = 0;
	
					// convert x positions to screen buffer positions
					int x_left_f  		  	= (poly_x_left_f<<16) + offset_y;
					int x_right_f 		  	= (poly_x_right_f<<16) + offset_y;
					int curr_y_from_left;
					int curr_y_from_right;
					int curr_y_to_left;
					int curr_y_to_right;
					if( num_vectors_left > 0 ) {
						curr_y_from_left = y_from_left[0];
						curr_y_to_left   = y_to_left[0];
					}	else {
						curr_y_from_left	= 0;
						curr_y_to_left		= 0;
					}
					if( num_vectors_right > 0 ) {
						curr_y_from_right = y_from_right[0];
						curr_y_to_right   = y_to_right[0];
					}	else {
						curr_y_from_right	= 0;
						curr_y_to_right	= 0;
					}
					
					int yx_left_local;
					int yx_right_local;
					int z_ray_center;
					int poly_u_center_f;
					int poly_v_center_f;
					
					int width_neg;
					long texelsPerPoly_f;
					int du_f;
					int dv_f;
					int poly_u_left_f;
					int poly_v_left_f;
									
					if( iteration == ITERATION_CEIL ) {
						for( int row=y_bottom; row!=y_top; ) {
							// is row not on a vector? dx should be zero then...
			
							// left side
							// check if on current vector; if not dx is zero
							if( currVectorIdx_left >= num_vectors_left ||
								row > curr_y_from_left ||
								row < curr_y_to_left ) {
								currDxdy_left = -scanwidth_f;
							
							}	else 
							// if it is on the end of the current vector, 
							// it should loop until it gets a vector on which
							// it isn't on the end
							if( row == curr_y_to_left ) {
								while( currVectorIdx_left < num_vectors_left && row == curr_y_to_left ) {
									currVectorIdx_left++;
									if( currVectorIdx_left < num_vectors_left ) {
										curr_y_from_left = y_from_left[currVectorIdx_left];
										curr_y_to_left   = y_to_left[currVectorIdx_left];
									}//	else {
									//	currDxdy_left = scanwidth_f;
									//}
								}
								if( row != curr_y_to_left && row == curr_y_from_left ) {
									currDxdy_left = dxdy_left[currVectorIdx_left] - scanwidth_f;
									x_left_f = row*scanwidth_f + (x_from_left[currVectorIdx_left]<<16);
								}	else {
									currDxdy_left = -scanwidth_f;
								}
								
							}	else
							// if it is on the starting of the current vector, 
							// set left dxdy
							if( row == curr_y_from_left ) {
								currDxdy_left = dxdy_left[currVectorIdx_left] - scanwidth_f;
								x_left_f = row*scanwidth_f + (x_from_left[currVectorIdx_left]<<16);
								
							}				
			
			
							// right side
							// check if on current vector; if not dx is zero
							if( currVectorIdx_right >= num_vectors_right ||
								row > curr_y_from_right ||
								row < curr_y_to_right ) {
								currDxdy_right = -scanwidth_f;
							
							}	else 
							// if it is on the end of the current vector, 
							// it should loop until it gets a vector on which
							// it isn't on the end
							if( row == curr_y_to_right ) {
								while( currVectorIdx_right < num_vectors_right && row == curr_y_to_right ) {
									currVectorIdx_right++;
									if( currVectorIdx_right < num_vectors_right ) {
										curr_y_from_right = y_from_right[currVectorIdx_right];
										curr_y_to_right   = y_to_right[currVectorIdx_right];
									}//	else {
									//	currDxdy_right = scanwidth_f;
									//}
								}
								if( row != curr_y_to_right && row == curr_y_from_right ) {
									currDxdy_right = dxdy_right[currVectorIdx_right] - scanwidth_f;
									x_right_f = row*scanwidth_f + (x_from_right[currVectorIdx_right]<<16);
								}	else {
									currDxdy_right = -scanwidth_f;
								}
								
							}	else
							// if it is on the starting of the current vector, 
							// set right dxdy
							if( row == curr_y_from_right ) {
								currDxdy_right = dxdy_right[currVectorIdx_right] - scanwidth_f;
								x_right_f = row*scanwidth_f + (x_from_right[currVectorIdx_right]<<16);
								
							}				
			
			
							// determine first next "stop drawing" row
							int endRow = y_top;
							// in case draw direction is bottom to top
							// endRow is lowest from or to value of either left or right
								if( row > curr_y_from_left && endRow < curr_y_from_left ) {
									endRow = curr_y_from_left;
								}
								if( row > curr_y_to_left && endRow < curr_y_to_left ) {
									endRow = curr_y_to_left;
								}
								if( row > curr_y_from_right && endRow < curr_y_from_right ) {
									endRow = curr_y_from_right;
								}
								if( row > curr_y_to_right && endRow < curr_y_to_right ) {
									endRow = curr_y_to_right;
								}
			
							
							for( ;row!=endRow; row-- ) {
			
								if( x_right_f > x_left_f ) {
									if( x_left_f < viewportX_left_array[row] ) {
										yx_left_local  = (viewportX_left_array[row]>>16);
									}	else {
										yx_left_local  = (x_left_f >>16);// + row*scanwidth;
									}
				
									if( x_right_f > viewportX_right_array[row] ) {
										yx_right_local  = (viewportX_right_array[row]>>16);
									}	else {
										yx_right_local = (x_right_f>>16);// + row*scanwidth;
									}
				
									
									z_ray_center 	= ((y_camera * tan_inv[(angleByRow[row]&0x000007FF)])>>16);
									poly_u_center_f = (z_ray_center * cos_center + cam_x_f); 
									poly_v_center_f = (z_ray_center * sin_center + cam_z_f);
									
									width_neg 			= yx_left_local-(renderer_width_f>>16);
									texelsPerPoly_f	= (((z_ray_center-z_ray_y_bottom)*d_dt_f) + dt_y_bottom_inv_f);
									du_f 				= (int)  ((texelsPerPoly_f * sin_center)>>16); // cant be optimized due precision
									dv_f 				= (int) -((texelsPerPoly_f * cos_center)>>16);
									poly_u_left_f		= du_f * width_neg + poly_u_center_f;
									poly_v_left_f		= dv_f * width_neg + poly_v_center_f;
									
									for( int yx=yx_left_local; yx<yx_right_local; yx++ ) {
										buffer[yx] = texture[((poly_v_left_f>>18)&0x00000038) + ((poly_u_left_f>>21)&0x00000007)];
					
										poly_u_left_f += du_f; 
										poly_v_left_f += dv_f;
										
									}
								}
				
								x_left_f 		+= currDxdy_left;
								x_right_f 		+= currDxdy_right;
								renderer_width_f	-= scanwidth_f;
							}
			
						}
	
					}	else {
						for( int row=y_top; row!=y_bottom; ) {
							// left side
							// check if on current vector; if not dx is zero
							if( currVectorIdx_left >= num_vectors_left ||
								row < curr_y_from_left ||
								row > curr_y_to_left ) {
								currDxdy_left = scanwidth_f;
							
							}	else 
							// if it is on the end of the current vector, 
							// it should loop until it gets a vector on which
							// it isn't on the end
							if( row == curr_y_to_left ) {
								while( currVectorIdx_left < num_vectors_left && row == curr_y_to_left ) {
									currVectorIdx_left++;
									if( currVectorIdx_left < num_vectors_left ) {
										curr_y_from_left = y_from_left[currVectorIdx_left];
										curr_y_to_left   = y_to_left[currVectorIdx_left];
									}//	else {
									//	currDxdy_left = scanwidth_f;
									//}
								}
								if( row != curr_y_to_left && row == curr_y_from_left ) {
									currDxdy_left = dxdy_left[currVectorIdx_left] + scanwidth_f;
									x_left_f = row*scanwidth_f + (x_from_left[currVectorIdx_left]<<16);
								}	else {
									currDxdy_left = scanwidth_f;
								}
								
							}	else
							// if it is on the starting of the current vector, 
							// set left dxdy
							if( row == curr_y_from_left ) {
								currDxdy_left = dxdy_left[currVectorIdx_left] + scanwidth_f;
								x_left_f = row*scanwidth_f + (x_from_left[currVectorIdx_left]<<16);
								
							}				
			
			
							// right side
							// check if on current vector; if not dx is zero
							if( currVectorIdx_right >= num_vectors_right ||
								row < curr_y_from_right ||
								row > curr_y_to_right ) {
								currDxdy_right = scanwidth_f;
							
							}	else 
							// if it is on the end of the current vector, 
							// it should loop until it gets a vector on which
							// it isn't on the end
							if( row == curr_y_to_right ) {
								while( currVectorIdx_right < num_vectors_right && row == curr_y_to_right ) {
									currVectorIdx_right++;
									if( currVectorIdx_right < num_vectors_right ) {
										curr_y_from_right = y_from_right[currVectorIdx_right];
										curr_y_to_right   = y_to_right[currVectorIdx_right];
									}//	else {
									//	currDxdy_right = scanwidth_f;
									//}
								}
								if( row != curr_y_to_right && row == curr_y_from_right ) {
									currDxdy_right = dxdy_right[currVectorIdx_right] + scanwidth_f;
									x_right_f = row*scanwidth_f + (x_from_right[currVectorIdx_right]<<16);
								}	else {
									currDxdy_right = scanwidth_f;
								}
								
							}	else
							// if it is on the starting of the current vector, 
							// set right dxdy
							if( row == curr_y_from_right ) {
								currDxdy_right = dxdy_right[currVectorIdx_right] + scanwidth_f;
								x_right_f = row*scanwidth_f + (x_from_right[currVectorIdx_right]<<16);
								
							}				
			
			
							// determine first next "stop drawing" row
							int endRow = y_bottom;
							// in case draw direction is top to bottom
							// endRow is highest from or to value of either left or right
								if( row < curr_y_from_left && endRow > curr_y_from_left ) {
									endRow = curr_y_from_left;
								}
								if( row < curr_y_to_left && endRow > curr_y_to_left ) {
									endRow = curr_y_to_left;
								}
								if( row < curr_y_from_right && endRow > curr_y_from_right ) {
									endRow = curr_y_from_right;
								}
								if( row < curr_y_to_right && endRow > curr_y_to_right ) {
									endRow = curr_y_to_right;
								}
			
							
							for( ;row!=endRow; row++ ) {
								// draw line
								if( x_right_f > x_left_f ) {
									if( x_left_f < viewportX_left_array[row] ) {
										yx_left_local  = (viewportX_left_array[row]>>16);
									}	else {
										yx_left_local  = (x_left_f >>16);// + row*scanwidth;
									}
				
									if( x_right_f > viewportX_right_array[row] ) {
										yx_right_local  = (viewportX_right_array[row]>>16);
									}	else {
										yx_right_local = (x_right_f>>16);// + row*scanwidth;
									}
				
									
									z_ray_center 	= ((y_camera * tan_inv[(angleByRow[row]&0x000007FF)])>>16);
									poly_u_center_f = (z_ray_center * cos_center + cam_x_f); 
									poly_v_center_f = (z_ray_center * sin_center + cam_z_f);
									
									width_neg 			= yx_left_local-(renderer_width_f>>16);
									texelsPerPoly_f	= (((z_ray_center-z_ray_y_bottom)*d_dt_f) + dt_y_bottom_inv_f);
									du_f 				= (int)  ((texelsPerPoly_f * sin_center)>>16); // cant be optimized due precision
									dv_f 				= (int) -((texelsPerPoly_f * cos_center)>>16);
									poly_u_left_f		= du_f * width_neg + poly_u_center_f;
									poly_v_left_f		= dv_f * width_neg + poly_v_center_f;
									
									for( int yx=yx_left_local; yx<yx_right_local; yx++ ) {
										buffer[yx] = texture[((poly_v_left_f>>18)&0x00000038) + ((poly_u_left_f>>21)&0x00000007)];
					
										poly_u_left_f += du_f; 
										poly_v_left_f += dv_f;
										
									}
								}
				
								x_left_f 		+= currDxdy_left;
								x_right_f 		+= currDxdy_right;
								renderer_width_f	+= scanwidth_f;
							}
						}
					}
	
				}   
			}	
		} // END FOR ITERATION
		
		
		}
		
		return retVal;
	}
	


}