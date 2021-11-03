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
public class FloorQuad implements Drawable {
	public final static int	WALL_ALIGNS_MAP_Y	= 0;
	public final static int	WALL_ALIGNS_MAP_X	= 1;

	public short[]	data = new short[36];
	public final static int DATA_ALIGN = 0;
	public final static int DATA_ANGLE_WALL = 4;
	public final static int DATA_X_WORLD = 8;
	public final static int DATA_Z_WORLD = 12;
	public final static int DATA_LENGTH_WORLD = 16;
	public final static int DATA_LENGTH_LEFT_CORRECTION = 20;
	public final static int DATA_LENGTH_RIGHT_CORRECTION = 24;
	public final static int DATA_TEXTURE_SPACE_SIN = 28;
	public final static int DATA_TEXTURE_SPACE_COS = 32;

//	public int[] 		align 		= new int[4];//WALL_ALIGNS_MAP_X; 	
//	public int[] 		angle_wall	= new int[4];

//	public int[]	x_world = new int[4];
//	public int[]	z_world = new int[4];

	public int 	y_world;
//	public int[]	length_world = new int[4];

//	public int[] length_left_correction 	= new int[4];	
//	public int[] length_right_correction 	= new int[4];	
	public static long renderer_focuspointDepth_f	= 48;
	public static long renderer_width_f			= 48;
	public static long renderer_height_f			= 48;
	public static int  scanheight				= 0;
	public static int  scanwidth					= 0;
	public static Renderer 		renderer		= null;
//	public int 			id				= 0;
//	public int[]			textureSpaceSin	= new int[4]; 
//	public int[]			textureSpaceCos	= new int[4]; 
		
	public final static 	int[] x_from_right	= new int[2];// { x1, x2 };
	public final static 	int[] y_from_right	= new int[2];// { y1, y2 };
	public final static 	int[] x_to_right	= new int[2];// { x2, x3 };
	public final static 	int[] y_to_right	= new int[2];// { y2, y3 };
	public final static 	int[] x_from_left	= new int[2];// { x4, x1 };
	public final static 	int[] y_from_left	= new int[2];// { y4, y1 };
	public final static 	int[] x_to_left  	= new int[2];// { x3, x4 };
	public final static 	int[] y_to_left	= new int[2];// { y3, y4 };
	public final static 	int[] dxdy_left	= new int[2];
	public final static 	int[] dxdy_right	= new int[2];
	public final static 	long[] z_to_right = new long[2];
	public final static 	long[] z_to_left	= new long[2];

	private final static short[] texture = new short[] { 
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,
		(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF000,(short)0xF000,
		(short)0xF000,(short)0xF000,(short)0xF0F0,(short)0xF0F0,(short)0xF0F0,(short)0xF000,(short)0xF000,(short)0xF000
	};

		
	public FloorQuad( int x1, int y1, int z1,
						int x2, int y2, int z2,
						int x3, int y3, int z3,
						int x4, int y4, int z4, Renderer renderer) {
	
		data[DATA_X_WORLD+0] = (short) x1; 
		data[DATA_X_WORLD+1] = (short) x2;
		data[DATA_X_WORLD+2] = (short) x3; 
		data[DATA_X_WORLD+3] = (short) x4;
		data[DATA_Z_WORLD+0] = (short) z1; 
		data[DATA_Z_WORLD+1] = (short) z2;
		data[DATA_Z_WORLD+2] = (short) z3; 
		data[DATA_Z_WORLD+3] = (short) z4;

		y_world = y1;
		this.renderer = renderer;
		this.renderer_focuspointDepth_f = (renderer.getFocuspoint_depth()<<16);
		this.renderer_width_f			= renderer.getWidth();
		this.renderer_height_f			= renderer.getHeight();
		this.scanheight				= (int) (renderer_height_f>>15);
		this.scanwidth					= (int) (renderer_width_f>>15);
		int next_vertice = 1;
		for( int vertice=0; vertice<4; vertice++ ) {
			int xDiff = (data[DATA_X_WORLD+next_vertice]-data[DATA_X_WORLD+vertice]);
			int zDiff = (data[DATA_Z_WORLD+next_vertice]-data[DATA_Z_WORLD+vertice]);
			if( data[DATA_X_WORLD+vertice] == data[DATA_X_WORLD+next_vertice] ) {
				data[DATA_ALIGN+vertice]	= WALL_ALIGNS_MAP_Y;
				if( data[DATA_Z_WORLD+vertice] < data[DATA_Z_WORLD+next_vertice] ) {
					data[DATA_ANGLE_WALL+vertice] = 1024;
				} else {
					data[DATA_ANGLE_WALL+vertice] = 0;
				}
			}	else {
				data[DATA_ALIGN+vertice]	= WALL_ALIGNS_MAP_X;
				if( data[DATA_X_WORLD+vertice] < data[DATA_X_WORLD+next_vertice] ) {
					data[DATA_ANGLE_WALL+vertice] = 512;
				}	else {
					data[DATA_ANGLE_WALL+vertice] = 1536;
				}
			}

			next_vertice = ((next_vertice+1)&0x00000003);
			
			data[DATA_TEXTURE_SPACE_SIN+vertice] 			= (short) (renderer.sin(data[DATA_ANGLE_WALL+vertice])>>16); 
			data[DATA_TEXTURE_SPACE_COS+vertice] 			= (short) (renderer.cos(data[DATA_ANGLE_WALL+vertice])>>16); 

			data[DATA_LENGTH_WORLD+vertice] 				= (short) (Float.sqrt( new Float(xDiff*xDiff + zDiff*zDiff) ).toLong());
			data[DATA_LENGTH_LEFT_CORRECTION+vertice] 	= (short)(-data[DATA_LENGTH_WORLD+vertice]/100);	
			data[DATA_LENGTH_RIGHT_CORRECTION+vertice]	= (short) (data[DATA_LENGTH_WORLD+vertice] - data[DATA_LENGTH_LEFT_CORRECTION+vertice]);	

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

		int y_camera  = y_world - cam_y;
		if( y_camera < 0 ) {

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
		short[] texture		= this.texture;
		int   scanwidth		= this.scanwidth;
		int   scanwidth_f		= (scanwidth<<16);
		short[] data				= this.data;


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
		int y_top = viewportY_bottom;
		int y_bottom = viewportY_top;

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
			if( data[DATA_ALIGN+vectors] == WALL_ALIGNS_MAP_X ) {
				int dz 		= data[DATA_Z_WORLD+vectors] - cam_z;
				isInFront_left	= dz*sin[angle_sin_clip_left];
				isInFront_right	= dz*sin[angle_sin_clip_right];
				distance   		= dz*data[DATA_TEXTURE_SPACE_SIN+vectors];  
				basepoint  		= (cam_x - data[DATA_X_WORLD+vectors])*data[DATA_TEXTURE_SPACE_SIN+vectors];//renderer.sin(angle_wall))>>16); 
			}	else {
				int dx 		= data[DATA_X_WORLD+vectors] - cam_x;
				isInFront_left	= dx*sin[angle_cos_clip_left];
				isInFront_right	= dx*sin[angle_cos_clip_right];
				distance   		= dx*data[DATA_TEXTURE_SPACE_COS+vectors];  
				basepoint  		= (data[DATA_Z_WORLD+vectors] - cam_z)*data[DATA_TEXTURE_SPACE_COS+vectors]; 
			}

			if( distance > 1 || distance < -1 ) {
			int poly_in_clip_length			 = data[DATA_LENGTH_WORLD+vectors];
	
			// convert view angle to "poly" space
			int angle_y_poly	= ((data[DATA_ANGLE_WALL+vectors] - angle)&0x000007FF);
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
				int  x_from;
				int  y_from;
				long z_from;

				// determine x of left and right side
				// check if poly hits right or left clip. if so calculate point on
				// poly and translate z else determine screen space x
				// left side
				if ( distance < 0 ) {
					if( (isInFront_left>=0 && position_clip_left>=data[DATA_LENGTH_LEFT_CORRECTION+vectors] && position_clip_left<=data[DATA_LENGTH_RIGHT_CORRECTION+vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						z_to   			= (((distance * sin_inv[(angle_y_clip_left+512)&0x000007FF]) * sin[(angle_y_clip_left-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor	= (renderer_focuspointDepth_f)/z_to;
						x_to   			= viewportX_left;						
						y_to   			= (int) ((renderer_height_f - (y_camera*z_factor))>>16);
					}	else {
						// not on poly, transform to screen space and setup 
						// left poly attributes
						// assume camera space coordinates for x1 etc
						long length_r_bp = -(data[DATA_LENGTH_WORLD+vectors]-basepoint);

						int cos_angle_y_poly 	= sin[((angle_y_poly + 512)&0x000007FF)];
						int sin_angle_y_poly 	= sin[(angle_y_poly&0x000007FF)];
						z_to 					= ((cos_angle_y_poly * distance    - sin_angle_y_poly * length_r_bp)>>16);
						long x_camera 			= ((cos_angle_y_poly * length_r_bp + sin_angle_y_poly * distance)>>16); // actual calculation ((-renderer.cos(angle_y) * basepoint - renderer.sin(angle_y) * distance_abs)>>16)
						long z_factor 			= (renderer_focuspointDepth_f)/z_to;

						x_to   = (int) ((renderer_width_f  - (x_camera*z_factor))>>16);						
						y_to   = (int) ((renderer_height_f - (y_camera*z_factor))>>16);
					}
					
					// right side (use worldSpaceRect.length)
					if( (isInFront_right>=0 && position_clip_right>=data[DATA_LENGTH_LEFT_CORRECTION+vectors] && position_clip_right<=data[DATA_LENGTH_RIGHT_CORRECTION+vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						// from behind?
						z_from   = (((distance * sin_inv[(angle_y_clip_right+512)&0x000007FF])*sin[(angle_y_clip_right-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor 	= (renderer_focuspointDepth_f)/z_from;
						x_from   = viewportX_right;						
						y_from   = (int) ((renderer_height_f - (y_camera*z_factor))>>16);
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
						y_from 				= (int) ((renderer_height_f - (y_camera*z_factor))>>16);
					}
				}	else {
					if( (isInFront_left>=0 && position_clip_left>=data[DATA_LENGTH_LEFT_CORRECTION+vectors] && position_clip_left<=data[DATA_LENGTH_RIGHT_CORRECTION+vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						z_from = (((distance * sin_inv[(angle_y_clip_left+512)&0x000007FF]) * sin[(angle_y_clip_left-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor		= (renderer_focuspointDepth_f)/z_from;
						x_from = viewportX_left;						
						y_from = (int) ((renderer_height_f - (y_camera*z_factor))>>16);
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
						y_from = (int) ((renderer_height_f - (y_camera*z_factor))>>16);
	
					}
					
					// right side (use worldSpaceRect.length)
					if( (isInFront_right>=0 && position_clip_right>=data[DATA_LENGTH_LEFT_CORRECTION+vectors] && position_clip_right<=data[DATA_LENGTH_RIGHT_CORRECTION+vectors]) ) {
						// it is on poly, determine camera space x, y, z
						// then transform y to screen space  
						z_to   = (((distance * sin_inv[(angle_y_clip_right+512)&0x000007FF])*sin[(angle_y_clip_right-angle_y_poly+512)&0x000007FF])>>32);
						long z_factor 	= (renderer_focuspointDepth_f)/z_to;
						x_to   = viewportX_right;						
						y_to   = (int) ((renderer_height_f - (y_camera*z_factor))>>16);
					}	else {
						// not on poly, transform to screen space and setup 
						// left poly attributes
						long length_r_bp 		= (data[DATA_LENGTH_WORLD+vectors]-basepoint);
						int cos_angle_y_poly 	= sin[((angle_y_poly + 512)&0x000007FF)];
						int sin_angle_y_poly 	= sin[(angle_y_poly&0x000007FF)];
						z_to 					= ((cos_angle_y_poly * distance    + sin_angle_y_poly * length_r_bp)>>16);
						long x_camera 			= ((cos_angle_y_poly * length_r_bp - sin_angle_y_poly * distance)>>16);
						long z_factor 	= (renderer_focuspointDepth_f)/z_to;
						x_to   			= (int) ((renderer_width_f  + (x_camera*z_factor))>>16);						
						y_to   			= (int) ((renderer_height_f - (y_camera*z_factor))>>16);
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
						
						num_vectors_left++;
						
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
						num_vectors_right++;
					}	else {
						x_from_right[num_vectors_right] = x_from;
						y_from_right[num_vectors_right] = y_from;
						z_to_right[num_vectors_right] = ((z_from + z_to)>>1);
						x_to_right[num_vectors_right] = x_to;
						y_to_right[num_vectors_right] = y_to;
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
				if( y_top > y_from) {
					y_top = y_from;
				}	
				if( y_top > y_to) {
					y_top = y_to;
				}
	
				if( y_bottom < y_from) {
					y_bottom = y_from;
				}
				
				if( y_bottom < y_to) {
					y_bottom = y_to;
				}

			}
			}
		}

		// if num_linear_vectors equals total number of vectors
		// the quad is not convex and has no bottom
		// and the bottom will be equal to viewport bottom
		if( num_linear_vectors >= num_vectors ) {
			// do bottom stuff
			y_bottom = viewportY_bottom;
		}

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

		
// must get rewritten with future viewportY_bottom
		// Very late y_bottom clip.... Must be optimized to front...
		// pref directly after vector calculation
		if( y_bottom > viewportY_bottom ) {
			y_bottom = viewportY_bottom;
		}
		if( y_top < viewportY_top ) {
			y_top = viewportY_top;
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
			int renderer_width_f	= y_top*scanwidth_f + ((int)this.renderer_width_f);

			int currVectorIdx_left	= 0;
			int currVectorIdx_right = 0;
			int currDxdy_left 		= scanwidth_f;
			int currDxdy_right 	= scanwidth_f;
			
			// convert x positions to screen buffer positions
			int offset_y			= y_top*scanwidth_f;
			poly_x_left_f  		  	= offset_y + (poly_x_left_f<<16);
			poly_x_right_f 		  	= offset_y + (poly_x_right_f<<16);
			int prev_y_from_left	= 0;
			int prev_y_from_right	= 0;
			int prev_y_to_left		= 0;
			int prev_y_to_right	= 0;
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
			
			for( int row=y_top; row<y_bottom; row++ ) {
				// is row not on a vector? dx should be zero then...
				if(	(currVectorIdx_left < 1 ||
						 row < prev_y_from_left ||
						 row >=   prev_y_to_left) 
						 &&
						(currVectorIdx_left >= num_vectors_left ||
				 		 row < curr_y_from_left ||
				 		 row >  curr_y_to_left) ) {
					currDxdy_left = scanwidth_f;
				}	else // if it is bigger than, shift VectorIdx and change dx
				while( currVectorIdx_left < num_vectors_left &&
					row >= curr_y_from_left ) {
					currDxdy_left = dxdy_left[currVectorIdx_left] + scanwidth_f;
					poly_x_left_f = row*scanwidth_f + (x_from_left[currVectorIdx_left]<<16);
					prev_y_from_left = y_from_left[currVectorIdx_left];
					prev_y_to_left   = y_to_left[currVectorIdx_left];
					currVectorIdx_left++;
					if( currVectorIdx_left < num_vectors_left ) {
						curr_y_from_left = y_from_left[currVectorIdx_left];
						curr_y_to_left   = y_to_left[currVectorIdx_left];
					}
				}
	
				// is row not on a vector? dx should be zero then...
				if(	(currVectorIdx_right < 1 ||
						 row < prev_y_from_right ||
						 row >=   prev_y_to_right) 
						 &&
						(currVectorIdx_right >= num_vectors_right ||
				 		 row < curr_y_from_right ||
				 		 row >  curr_y_to_right) ) {
					currDxdy_right = scanwidth_f;
				}	else // if it is bigger than, shift VectorIdx and change dx
				while( currVectorIdx_right < num_vectors_right &&
					row >= curr_y_from_right ) {
					currDxdy_right = dxdy_right[currVectorIdx_right] + scanwidth_f;
					poly_x_right_f = row*scanwidth_f + (x_from_right[currVectorIdx_right]<<16);
					prev_y_from_right = y_from_right[currVectorIdx_right];
					prev_y_to_right   = y_to_right[currVectorIdx_right];
					currVectorIdx_right++;
					if( currVectorIdx_right < num_vectors_right ) {
						curr_y_from_right = y_from_right[currVectorIdx_right];
						curr_y_to_right   = y_to_right[currVectorIdx_right];
					}
				}

	
				// draw line
				if( poly_x_right_f > poly_x_left_f ) {
					int yx_left_local;
					int yx_right_local;
					if( poly_x_left_f < viewportX_left_array[row] ) {
						yx_left_local  = (viewportX_left_array[row]>>16);
					}	else {
						yx_left_local  = (poly_x_left_f >>16);// + row*scanwidth;
					}

					if( poly_x_right_f > viewportX_right_array[row] ) {
						yx_right_local  = (viewportX_right_array[row]>>16);
					}	else {
						yx_right_local = (poly_x_right_f>>16);// + row*scanwidth;
					}

					
					int z_ray_center 	= ((y_camera * tan_inv[(angleByRow[row]&0x000007FF)])>>16);
					int poly_u_center_f = (z_ray_center * cos_center + cam_x_f); 
					int poly_v_center_f = (z_ray_center * sin_center + cam_z_f);
					
					int width_neg 		= yx_left_local-(renderer_width_f>>16);
//					int width_neg 		= ((poly_x_left_f-renderer_width_f)>>16);
					long texelsPerPoly_f	= (((z_ray_center-z_ray_y_bottom)*d_dt_f) + dt_y_bottom_inv_f);
					int du_f 			= (int)  ((texelsPerPoly_f * sin_center)>>16); // cant be optimized due precision
					int dv_f 			= (int) -((texelsPerPoly_f * cos_center)>>16);
					int poly_u_left_f	= du_f * width_neg + poly_u_center_f;
					int poly_v_left_f	= dv_f * width_neg + poly_v_center_f;
					
					for( int yx=yx_left_local; yx<yx_right_local; yx++ ) {
						buffer[yx] = texture[((poly_v_left_f>>18)&0x00000038) + ((poly_u_left_f>>21)&0x00000007)];
	
						poly_u_left_f += du_f; 
						poly_v_left_f += dv_f;
						
					}
				}

				poly_x_left_f 		+= currDxdy_left;
				poly_x_right_f 		+= currDxdy_right;
				renderer_width_f	+= scanwidth_f;
			}
		}


		}
		
		return retVal;
	}
	

	

}
