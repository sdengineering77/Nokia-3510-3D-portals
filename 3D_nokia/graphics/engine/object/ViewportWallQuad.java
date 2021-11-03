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
public class ViewportWallQuad implements Drawable {
	public final static int	WALL_ALIGNS_MAP_Y	= 0;
	public final static int	WALL_ALIGNS_MAP_X	= 1;
	
	public final static int   ONE_F				= (1<<16);

	public static ClipArea[]	clipArea_array		= null;
	public final static int	num_clipAreas		= 11;
	public static int			nextClipAreaIdx		= 0;

	public short 				align 		= WALL_ALIGNS_MAP_X; 	
	public short 				angle_wall	= 0;

	public short 				x_world1, x_world2;
	public short 				z_world1, z_world2;
	public short 				y_world;
	public short 				height_world;
	public short 				length_world;

	public short 				length_left_correction;	
	public short 				length_right_correction;	
	public static long 				renderer_focuspointDepth	= 48;
	public static long 				renderer_width			= 48;
	public static long 				renderer_height			= 48;
	public static int  				scanheight				= 0;
	public static int  				scanheight_f				= 0;
	public static int  				scanwidth					= 0;
	public static int  				scanwidth_inv				= 0;
	public static Renderer 			renderer		= null;
//	public static int 				id				= 0;
	public short				textureSpaceSin; 
	public short				textureSpaceCos; 

	public Drawable[]			childObjects	= null;
	public boolean			useTexture		= false;
	
			
//	private final static short[] texture = null;//new short[] { 
/*		(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,
		(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xF000,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,
		(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xF000,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,
		(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xFF00,(short)0xFF00,(short)0xF000,(short)0xFF00,
		(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000,(short)0xFF00,(short)0xF000
	};
*/
	
	public ViewportWallQuad( int x1, int y1, int z1,
						int x2, int y2, int z2,
						int x3, int y3, int z3,
						int x4, int y4, int z4, Renderer renderer) {
	
		int xDiff = (x2-x1);
		int zDiff = (z2-z1);

		x_world1 = (short) x1; 
		x_world2 = (short) x2;
		z_world1 = (short) z1; 
		z_world2 = (short) z2;
		y_world	 = (short) y3;
		height_world = (short) (y1 - y3);
		length_world = (short) (Float.sqrt( new Float(xDiff*xDiff + zDiff*zDiff) ).toLong());

		this.renderer = renderer;
		this.renderer_focuspointDepth = (renderer.getFocuspoint_depth()<<16);
		this.renderer_width			= renderer.getWidth();
		this.renderer_height			= renderer.getHeight();
		this.scanheight				= (int) (renderer_height>>15);
		this.scanheight_f				= (int) (renderer_height<<1);
		this.scanwidth					= (int) (renderer_width>>15);
		this.scanwidth_inv				= (int) (((1<<16)/((long)scanwidth)));
		if( x1 == x2 ) {
			align 		= WALL_ALIGNS_MAP_Y;
			if( z1 < z2 ) {
				angle_wall = 1024;
			} else {
				angle_wall = 0;
			}
		}	else {
			align 		= WALL_ALIGNS_MAP_X;
			if( x1 < x2 ) {
				angle_wall = 512;
			}	else {
				angle_wall = 1536;
			}
		}
		
		length_left_correction 	= (short) (-length_world/100);	
		length_right_correction	= (short) (length_world - length_left_correction);	

		textureSpaceSin = (short) (renderer.sin(angle_wall)>>16); 
		textureSpaceCos = (short) (renderer.cos(angle_wall)>>16); 
		
		if( clipArea_array == null ) {
			clipArea_array = new ClipArea[num_clipAreas];
			
			for( int cnt=0; cnt<num_clipAreas; cnt++ ){
				clipArea_array[cnt] = new ClipArea( scanwidth, scanheight );
			}
		}
		
	}

	public boolean draw( short[] buffer, int cam_x, int cam_y, int cam_z, int angle, ClipArea clipArea ) {
		boolean retVal = true;

		long distance;
		int basepoint;
		if( align == WALL_ALIGNS_MAP_X ) {
			distance   = (z_world1 - cam_z)*textureSpaceSin;  
			basepoint  = (cam_x - x_world1)*textureSpaceSin;//renderer.sin(angle_wall))>>16); 
		}	else {
			distance   = (x_world1 - cam_x)*textureSpaceCos;  
			basepoint  = (z_world1 - cam_z)*textureSpaceCos; 
		}

		if( distance > 1 ) {
		
		distance = Math.abs(distance);

		int[] 	viewportY_top 		= clipArea.viewportY_top_array;
		int[]	viewportY_bottom 	= clipArea.viewportY_bottom_array;
		int	viewportX_left 		= clipArea.viewportX_left;
		int  	viewportX_right 	= clipArea.viewportX_right;
		
		int 	poly_in_clip_length;
		int	poly_hor_offset;
		int 	poly_verStepsSize;
		int 	poly_verStepSize_left;
		int 	poly_verStepSize_right;
		int 	y_top_left;
		int	y_bottom_left;
		int 	y_top_right;
		int	y_bottom_right;
		int 	x_left;
		int	x_right;

	    int[]	tan = renderer.tan;
	    int[]	angleByColumn 	= renderer.angleByColumn;
		int[]	sin_inv 		= renderer._sin_inv;
		int[]	sin 			= renderer._sin;
		
		long   height_world = this.height_world;
		
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
		poly_in_clip_length			 = length_world;

		// convert view angle to "poly" space
		int angle_y_poly	= ((angle_wall - angle)&0x000007FF);
		if( angle_y_poly > 1024 ) {
			angle_y_poly -= 2048;
		}
		
		// angle of most left and right pixel in screen and position on poly (may be outside the poly)
		int angle_y_clip_left 	= angle_y_poly - angleByColumn[viewportX_left]; 
		long position_clip_left	= basepoint + ((distance*tan[(angle_y_clip_left&0x000007FF)])>>16);
		
		int angle_y_clip_right 	= angle_y_poly - angleByColumn[viewportX_right]; 
		long position_clip_right = basepoint + ((distance*tan[(angle_y_clip_right&0x000007FF)])>>16);

		// angles are valid if between -90 and +90 degrees in relation to the poly
		boolean angle_y_left_inClip 	= 	(angle_y_clip_left >-512  	&& angle_y_clip_left <512);
		boolean angle_y_right_inClip 	= 	(angle_y_clip_right>-512 	&& angle_y_clip_right<512);

		// parts visible?
		if(	(position_clip_left >0 && position_clip_left <poly_in_clip_length && angle_y_left_inClip) || 
				(position_clip_right>0 && position_clip_right<poly_in_clip_length && angle_y_right_inClip) ||
				(position_clip_right>0 && position_clip_left <poly_in_clip_length && (angle_y_right_inClip || angle_y_left_inClip)) ||
				(position_clip_right>0 && angle_y_right_inClip && !angle_y_left_inClip) ||
				(position_clip_left<poly_in_clip_length && angle_y_left_inClip &&!angle_y_right_inClip)
				) {

			// translate y
			int y_camera = y_world - cam_y;
		
			// determine x of left and right side
			// check if poly hits right or left clip. if so calculate point on
			// poly and translate z else determine screen space x
			// left side
			if( (angle_y_left_inClip && position_clip_left>=length_left_correction && position_clip_left<=length_right_correction) ) {
				// it is on poly, determine camera space x, y, z
				// then transform y to screen space  
				// assume camera space coordinates for x1 etc
				// Renderer.rotateY();
				// z1 += (int) (((z2 - z1) * (position_clip_left*length_inv))>>16);
				//
                // can be done with 3 multiplies + 2 subtracts including rotation..
                // instead of 6 mult, 3 subtracts because goal is to get z
                // requires inversed cos and sine
				//				long sin_angle_y = renderer.sin(angle_y); 
				//				long zm = (distance * renderer.cos_inv(angle_y));
				//				int  z  = (int) ((zm - (((((zm*sin_angle_y)>>16) - (basepoint<<16)) * sin_angle_y)>>16))>>16);
				// translate to camera space
				//long  z_camera  = (((distance * renderer.cos_inv(angle_y_clip_left))*renderer.cos(angle_y_clip_left-angle_y_poly))>>32);
				long z_factor 		= (renderer_focuspointDepth)/((distance * sin_inv[((angle_y_clip_left+512)&0x000007FF)]*sin[((angle_y_clip_left-angle_y_poly+512)&0x000007FF)])>>32);
				int  height_camera	= (int) (height_world*z_factor);
				
				x_left 						= viewportX_left; 
				y_bottom_left				= (int) (renderer_height - (y_camera*z_factor));
				y_top_left					= y_bottom_left - height_camera;
				
				if( useTexture ) {
					poly_verStepSize_left   	= (int) ((height_world<<32) / height_camera);
					poly_hor_offset= (int) position_clip_left;
					poly_in_clip_length 						   -= poly_hor_offset; // = (int) position_clip_left (saves cast)
				}	else {
					poly_verStepSize_left  = 0;
					poly_hor_offset=0;
				}

			}	else {
				// not on poly, transform to screen space and setup 
				// left poly attributes
				// assume camera space coordinates for x1 etc
				// long	z_camera = ((renderer.cos(angle_y_poly) * distance  - renderer.sin(angle_y_poly) * basepoint)>>16);
				// long	x_camera = ((renderer.cos(angle_y_poly) * basepoint + renderer.sin(angle_y_poly) * distance)>>16); // actual calculation ((-renderer.cos(angle_y) * basepoint - renderer.sin(angle_y) * distance)>>16)
				int cos_angle_y_poly = sin[((angle_y_poly+512)&0x000007FF)];
				int sin_angle_y_poly = sin[(angle_y_poly&0x000007FF)];

				long z_factor 				= (renderer_focuspointDepth)/((cos_angle_y_poly * distance  - sin_angle_y_poly * basepoint)>>16);
				int height_camera			= (int) (height_world*z_factor);
				x_left						= (int) ((renderer_width  - (((cos_angle_y_poly * basepoint + sin_angle_y_poly * distance)>>16)*z_factor))>>16); // beware x_cam is subtracted due calculation optimalization
								
				y_bottom_left				= (int) (renderer_height - (y_camera*z_factor));
				y_top_left					= y_bottom_left - height_camera;

				poly_hor_offset 			= 0;
				if( useTexture ) {
					poly_verStepSize_left		= (int) ((height_world<<32) / height_camera);
				}	else {
					poly_verStepSize_left  = 0;
				}
			}
			
			// right side (use worldSpaceRect.length)
			if( (angle_y_right_inClip && position_clip_right>=length_left_correction && position_clip_right<=length_right_correction) ) {
				// it is on poly, determine camera space x, y, z
				// then transform y to screen space  
				//	z2  = cameraSpaceRect.z1 + ((int) (((cameraSpaceRect.z2 - cameraSpaceRect.z1) * (position_clip_right*length_inv))>>16));

				// long zm = ((long) distance * renderer.cos_inv(angle_y_clip_right));
				// int  z  = (int) ((zm*renderer.cos(angle_y_clip_right-angle_y))>>32);

				// long z_camera  = (((distance * renderer.cos_inv(angle_y_clip_right))*renderer.cos(angle_y_clip_right-angle_y_poly))>>32);
				long z_factor 		= (renderer_focuspointDepth)/((distance * sin_inv[((angle_y_clip_right+512)&0x000007FF)]*sin[((angle_y_clip_right-angle_y_poly+512)&0x000007FF)])>>32);
//				long z_factor 		= (renderer_focuspointDepth)/(((distance * renderer.cos_inv(angle_y_clip_right))*renderer.cos(angle_y_clip_right-angle_y_poly))>>32);
				int height_camera	= (int) (height_world*z_factor);
				
				
				x_right 				= viewportX_right; 
				y_bottom_right			= (int) (renderer_height - (y_camera*z_factor));
				y_top_right				= y_bottom_right - height_camera;

				if( useTexture ) {
					poly_verStepSize_right  = (int) ((height_world<<32) / height_camera);
					poly_in_clip_length    -= (int) (length_world - position_clip_right);
				}	else {
					poly_verStepSize_right  = 0;
				}

			}	else {
				// not on poly, transform to screen space and setup 
				// left poly attributes
				long	length_r_bp = (length_world-basepoint);
				// long	z_camera = ((renderer.cos(angle_y_poly) * distance    + renderer.sin(angle_y_poly) * length_r_bp)>>16);
				// long	x_camera = ((renderer.cos(angle_y_poly) * length_r_bp - renderer.sin(angle_y_poly) * distance)>>16);
				int cos_angle_y_poly = sin[((angle_y_poly+512)&0x000007FF)];
				int sin_angle_y_poly = sin[(angle_y_poly&0x000007FF)];
				long z_factor 		= (renderer_focuspointDepth)/((cos_angle_y_poly * distance    + sin_angle_y_poly * length_r_bp)>>16);
				int  height_camera	= (int) (height_world*z_factor);
				x_right				= (int) ((renderer_width  + (((cos_angle_y_poly * length_r_bp - sin_angle_y_poly * distance)>>16)*z_factor))>>16);
								
				y_bottom_right			= (int) (renderer_height - (y_camera*z_factor));
				y_top_right				= y_bottom_right - height_camera;
				if( useTexture )  {
					poly_verStepSize_right  = (int) ((height_world<<32) / height_camera);
				}	else {
					poly_verStepSize_right = 0;
				}
			}
			
			// has the poly width? 
			if( x_left < x_right ) {
				/**
				 * Fetch a viewport and draw child polies
				 */
				if( nextClipAreaIdx < num_clipAreas ) {
					ClipArea intersectClipArea = clipArea_array[nextClipAreaIdx++];
					
					intersectClipArea.setClipArea(	x_left, (y_top_left>>16), 
													x_right, (y_top_right>>16),
													x_left, (y_bottom_left>>16),
													x_right, (y_bottom_right>>16) );
					intersectClipArea.intersectClip(clipArea);
	
					if( childObjects != null ) {
						Drawable[] childObjects = this.childObjects;
						int size = childObjects.length;				
						for( int polyCnt=0; polyCnt < size; polyCnt++ ) {
							childObjects[polyCnt].draw(buffer, cam_x, cam_y, cam_z, angle, intersectClipArea);
						}
					}
					nextClipAreaIdx--;
				}
			
	
				/**
				 * Draw the poly from left to right on screen
				 * Furthermore, draw vertical floor and ceiling parts if not clipped out
				 * 
				 */
/*				if( useTexture ) {	
					int	scanheight 		= this.scanheight;
					int	scanheight_f	= this.scanheight_f;
					int	scanwidth 		= this.scanwidth;
					int	scanwidth_inv 	= this.scanwidth_inv;
			
			
					int 	dy_top;
					int 	dy_bottom;
					int 	poly_h;
					int 	y_top_local;
					int 	y_bottom_local;
					int 	poly_v;
					int 	texture_h;
					short[] texture		= this.texture;
						
	
					// Determine direction coeficients of y top and bottom per step of x
					// on screen
					int x_diff_f 		= (ONE_F / (x_left - x_right));
					dy_top				= (int) ((((long)(y_top_left - y_top_right)) 		* x_diff_f)>>16); 
					dy_bottom			= (int) ((((long)(y_bottom_left - y_bottom_right))	* x_diff_f)>>16);
	
					// dVerStepSize is a factor that multiplied with the horizontal 
					// position on the poly gives the vertical step size in texture
					// (same relation)
					int dVerStepSize = ((((poly_verStepSize_right-poly_verStepSize_left)))/poly_in_clip_length);
	
					// loop to draw left to right
					for( int columnCnt=x_left; columnCnt<x_right; columnCnt++ ) {
						// vertical poly position start
						poly_v 				= 0;
						
						// calculate horizontal poly position
						poly_h				= (int)((tan[((angle_y_poly-angleByColumn[columnCnt])&0x000007FF)]*distance)>>16) + basepoint;
						// calculate horizontal texture position
						texture_h 			= ((poly_h>>5)&0x00000007);
						
						// calculate vertical polygon step size per screen pixel
						poly_verStepsSize = (poly_h-poly_hor_offset)*dVerStepSize + poly_verStepSize_left;
							
						// clip top and bottom
						int xy_bottom   		= (y_bottom_left>>16)*scanwidth + columnCnt;
						int xy_top	    		= (y_top_left>>16)*scanwidth + columnCnt;
	
						// clip top and bottom
						if( xy_top < viewportY_top[columnCnt] ) {
							// calculate new vertical postion on poly after clip
							poly_v += ((((long)poly_verStepsSize) * (viewportY_top[columnCnt] - xy_top) * scanwidth_inv)>>16);
							y_top_local = (viewportY_top[columnCnt]);
						}	else {
							y_top_local = (int) (xy_top);
						}
						
						if( xy_bottom > viewportY_bottom[columnCnt] ) {
							y_bottom_local = (viewportY_bottom[columnCnt]);
						}	else {
							y_bottom_local = (int) (xy_bottom);
						}
	
						// draw vertical pixels use manual alpha
						for( int rowCnt=y_top_local; rowCnt<y_bottom_local; rowCnt+=scanwidth) {
							// calculate vertical texture position,
							// since texturemap is linear buffer as well (y*width+x)
							// and thus needs to be multiplied by 8 (texture size at time of writing)
							// this means that instead of shifting right 21 bits, do 18
							// and mask at %111000 (0x38)
							// increase texture_h with this value to optimize????
							// int texture_pos = ((poly_v>>18)&0x00000038) + texture_h;
							
							int   text_argb 	= texture[(((poly_v>>18)&0x00000038) + texture_h)];
							int   textureAlpha	= (text_argb>>12);
							int   buff_argb 	= buffer[rowCnt];
							int   buff_r    	= (buff_argb&0x00000F00);
							int   buff_g    	= (buff_argb&0x000000F0);
							int   buff_b    	= (buff_argb&0x0000000F);
							int   new_r     	= (buff_r + (((((text_argb&0x00000F00) - buff_r) * textureAlpha)>>4))&0x80000F00);
							int   new_g     	= (buff_g + (((((text_argb&0x000000F0) - buff_g) * textureAlpha)>>4))&0x800000F0);
							int   new_b     	= (buff_b + (((((text_argb&0x0000000F) - buff_b) * textureAlpha)>>4))&0x8000000F);
							buffer[rowCnt]  	= (short)(new_r|new_g|new_b);
							poly_v+=poly_verStepsSize;
						}
						y_top_left+=dy_top;
						y_bottom_left+=dy_bottom;			
					}
				}
*/					
			}	 
		}			
			
		}		
		return retVal;
	}


}
