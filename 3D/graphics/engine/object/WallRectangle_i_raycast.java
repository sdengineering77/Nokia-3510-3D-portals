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
public class WallRectangle_i_raycast implements Drawable {
	public final static int	WALL_ALIGNS_MAP_Y	= 0;
	public final static int	WALL_ALIGNS_MAP_X	= 1;

	public int 		align 		= WALL_ALIGNS_MAP_X; 	
	public int 		angle_wall	= 0;

	public int x_world1, x_world2;
	public int z_world1, z_world2;
	public int y_world;
	public long 	height_world;
	public int 	length_world;

	public int length_inv;	
	public int length_left_correction;	
	public int length_right_correction;	
	public long renderer_focuspointDepth	= 48;
	public long renderer_width			= 48;
	public long renderer_height			= 48;
	public int  scanheight				= 0;
	public int  scanwidth					= 0;
	public int  scanwidth_inv				= 0;
//	public int texture_offset_h 			= 0;
	public Renderer 		renderer		= null;
	public int 			id				= 0;
	public int			textureSpaceSin; 
	public int			textureSpaceCos; 
		
	private final static short[] texture = new short[] { 
		'0','0','1','0','0','0','1','0',
		'1','1','1','1','1','1','1','1',
		'1','0','0','0','1','0','0','0',
		'1','1','1','1','1','1','1','1',
		'0','0','1','0','0','0','1','0',
		'1','1','1','1','1','1','1','1',
		'1','0','0','0','1','0','0','0',
		'1','1','1','1','1','1','1','1'
	};

	
	public WallRectangle_i_raycast( int x1, int y1, int z1,
						int x2, int y2, int z2,
						int x3, int y3, int z3,
						int x4, int y4, int z4, Renderer renderer) {
	
		int xDiff = (x2-x1);
		int zDiff = (z2-z1);

		x_world1 = x1; 
		x_world2 = x2;
		z_world1 = z1; 
		z_world2 = z2;
		y_world	 = y3;
		height_world = y1 - y3;
		length_world = (int) (Float.sqrt( new Float(xDiff*xDiff + zDiff*zDiff) ).toLong());

		this.renderer = renderer;
		this.renderer_focuspointDepth = (renderer.getFocuspoint_depth()<<16);
		this.renderer_width			= renderer.getWidth();
		this.renderer_height			= renderer.getHeight();
		this.scanheight				= (int) (renderer_height>>15);
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
		length_inv				= ((1<<16)/length_world);	
		
		length_left_correction 	= -length_world/100;	
		length_right_correction	= length_world - length_left_correction;	

		textureSpaceSin = (renderer.sin(angle_wall)>>16); 
		textureSpaceCos = (renderer.cos(angle_wall)>>16); 
		
	}

	public boolean draw( short[] buffer, int cam_x, int cam_y, int cam_z, int angle, ClipArea clipArea ) {
		boolean retVal = true;

		int[] 	viewportY_top 		= clipArea.viewportY_top_array;
		int[]	viewportY_bottom 	= clipArea.viewportY_bottom_array;
		int	viewportX_left 		= clipArea.viewportX_left;
		int  	viewportX_right 	= clipArea.viewportX_right;
		int	scanheight 			= this.scanheight;
		int	scanwidth 			= this.scanwidth;
		int	scanwidth_f 		= (scanwidth<<16);
		int	scanwidth_inv 		= this.scanwidth_inv;
		int	buffer_offset;

		int	poly_hor_offset;
		int 	poly_verStepsSize;
		int 	poly_verStepSize_left;
		int 	poly_verStepSize_right;
		int 	dy_top;
		int 	dy_bottom;
		int 	y_top_left;
		int	y_bottom_left;
		int 	y_top_right;
		int	y_bottom_right;
		int 	x_left;
		int	x_right;
		int 	poly_h;
		int 	y_top_local;
		int 	y_bottom_local;
		int 	poly_v;
		int 	texture_h;
		int 	poly_in_clip_length;
		
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
		if( align == WALL_ALIGNS_MAP_X ) {
			distance   = Math.abs(z_world1 - cam_z);  
			basepoint  = (cam_x - x_world1)*textureSpaceSin;//renderer.sin(angle_wall))>>16); 
		}	else {
			distance   = Math.abs(cam_x - x_world1);  
			basepoint  = (z_world1 - cam_z)*textureSpaceCos; 
		}
// WEGHALEN!!

//		texture_offset_h = worldSpaceRect.texture_offset_h;
		poly_in_clip_length			 = length_world;

		// convert view angle to "poly" space
		int angle_y_poly	= ((angle_wall - angle)&0x000007FF);
		if( angle_y_poly > 1024 ) {
			angle_y_poly -= 2048;
		}
		// angle of most left and right pixel in screen and position on poly (may be outside the poly)
		int angle_y_clip_left 		= angle_y_poly - renderer.getColumnAngle(viewportX_left); 
		long position_clip_left	= basepoint + ((distance*renderer.tan(angle_y_clip_left))>>16);
		
		int angle_y_clip_right 	= angle_y_poly - renderer.getColumnAngle(viewportX_right); 
		long position_clip_right	= basepoint + ((distance*renderer.tan(angle_y_clip_right))>>16);

		// angles are valid if between -90 and +90 degrees in relation to the poly
		boolean angle_y_left_inClip 	= 	(angle_y_clip_left >-512  	&& angle_y_clip_left <512);
		boolean angle_y_right_inClip 	= 	(angle_y_clip_right>-512 	&& angle_y_clip_right<512);

System.out.println( "ID===============: " + id );
System.out.println( "angle_y_clip_left: " + angle_y_clip_left );
System.out.println( "position_clip_left: " + position_clip_left );
System.out.println( "angle_y_clip_right: " + angle_y_clip_right );
System.out.println( "position_clip_right: " + position_clip_right );
System.out.println( "angle_y_poly: " + angle_y_poly );
System.out.println( "angle_wall: " + angle_wall );
System.out.println( "angle: " + angle );


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
				long  z_camera  = (((distance * renderer.cos_inv(angle_y_clip_left))*renderer.cos(angle_y_clip_left-angle_y_poly))>>32);
				long z_factor 		= (renderer_focuspointDepth)/z_camera;
				int  height_camera	= (int) (height_world*z_factor);
				
				
				x_left 						= viewportX_left; 
				y_bottom_left				= (int) (renderer_height - (y_camera*z_factor));
				y_top_left					= y_bottom_left - height_camera;
				poly_verStepSize_left   	= (int) ((height_world<<32) / height_camera);

				poly_hor_offset= (int) position_clip_left;
				poly_in_clip_length 						   -= poly_hor_offset; // = (int) position_clip_left (saves cast)

			}	else {
				// not on poly, transform to screen space and setup 
				// left poly attributes
				// assume camera space coordinates for x1 etc
				long	z_camera = ((renderer.cos(angle_y_poly) * distance  - renderer.sin(angle_y_poly) * basepoint)>>16);
				long	x_camera = ((renderer.cos(angle_y_poly) * basepoint + renderer.sin(angle_y_poly) * distance)>>16); // actual calculation ((-renderer.cos(angle_y) * basepoint - renderer.sin(angle_y) * distance)>>16)

				long z_factor 				= (renderer_focuspointDepth)/z_camera;
				int height_camera			= (int) (height_world*z_factor);
				x_left						= (int) ((renderer_width  - (x_camera*z_factor))>>16); // beware x_cam is subtracted due calculation optimalization
								
				y_bottom_left				= (int) (renderer_height - (y_camera*z_factor));
				y_top_left					= y_bottom_left - height_camera;
				poly_verStepSize_left	= (int) ((height_world<<32) / height_camera);

				poly_hor_offset = 0;

			}
			
			// set stepSize the drawer needs to start with
			poly_verStepsSize 		= poly_verStepSize_left;
	
			// right side (use worldSpaceRect.length)
			if( (angle_y_right_inClip && position_clip_right>=length_left_correction && position_clip_right<=length_right_correction) ) {
				// it is on poly, determine camera space x, y, z
				// then transform y to screen space  
				//	z2  = cameraSpaceRect.z1 + ((int) (((cameraSpaceRect.z2 - cameraSpaceRect.z1) * (position_clip_right*length_inv))>>16));

				// long zm = ((long) distance * renderer.cos_inv(angle_y_clip_right));
				// int  z  = (int) ((zm*renderer.cos(angle_y_clip_right-angle_y))>>32);

				long z_camera  = (((distance * renderer.cos_inv(angle_y_clip_right))*renderer.cos(angle_y_clip_right-angle_y_poly))>>32);

				long z_factor 		= (renderer_focuspointDepth)/z_camera;
				int height_camera	= (int) (height_world*z_factor);
				
				
				x_right 					= viewportX_right; 
				y_bottom_right				= (int) (renderer_height - (y_camera*z_factor));
				y_top_right					= y_bottom_right - height_camera;
				poly_verStepSize_right   = (int) ((height_world<<32) / height_camera);
				poly_in_clip_length 					   -= (int) (length_world - position_clip_right);

			}	else {
				// not on poly, transform to screen space and setup 
				// left poly attributes
				long	length_r_bp = (length_world-basepoint);
				long	z_camera = ((renderer.cos(angle_y_poly) * distance    + renderer.sin(angle_y_poly) * length_r_bp)>>16);
				long	x_camera = ((renderer.cos(angle_y_poly) * length_r_bp - renderer.sin(angle_y_poly) * distance)>>16);

				long z_factor 		= (renderer_focuspointDepth)/z_camera;
				int  height_camera	= (int) (height_world*z_factor);
				x_right				= (int) ((renderer_width  + (x_camera*z_factor))>>16);
								
				y_bottom_right			= (int) (renderer_height - (y_camera*z_factor));
				y_top_right				= y_bottom_right - height_camera;
				poly_verStepSize_right  = (int) ((height_world<<32) / height_camera);
			}

			/**
			 * Draw the poly from left to right on screen
			 * Furthermore, draw vertical floor and ceiling parts if not clipped out
			 * 
			 */
System.out.println( "xleft: " +  x_left + " xright: " + x_right );			
			// has the poly width? 
			if( x_left < x_right ) {
				// Determine direction coeficients of y top and bottom per step of x
				// on screen
				int x_diff_f 		= ((1<<16) / (x_left - x_right));
//				dy_top				= (scanheight<<16) + (int) ((((long)(y_top_left - y_top_right)) 		* x_diff_f)>>16); 
//				dy_bottom			= (scanheight<<16) + (int) ((((long)(y_bottom_left - y_bottom_right))	* x_diff_f)>>16);
				dy_top				= (int) ((((long)(y_top_left - y_top_right)) 		* x_diff_f)>>16); 
				dy_bottom			= (int) ((((long)(y_bottom_left - y_bottom_right))	* x_diff_f)>>16);
//				int scan_offset	= scanheight * x_left;
//				int scan_offset_f 	= (scan_offset<<16);
//				int xy_bottom   	= y_bottom_left + scan_offset_f;
//				int xy_top	    	= y_top_left + scan_offset_f;
//				int fake_y_top		= 0;
//				int fake_y_bottom	= 0;
//				int scan_offset_x_f= (x_left<<16);
//				long xy_bottom   	= (long)y_bottom_left*scanwidth + scan_offset_x_f;
//				long xy_top	    = (long)y_top_left*scanwidth + scan_offset_x_f;
				
//				int _x_diff_f 		= (x_left - x_right);
//				int dx_top;
//				int dx_bottom;
//				boolean
//				if( y_top_left > y_top_right ) {
//					dx_top			= (scanwidth<<16) + (int) ((((long) x_diff_f)<<32)/(y_top_left - y_top_right)); 
//				}	else 
//				if( y_top_left < y_top_right ) {
//					dx_top			= (scanwidth<<16) + (int) ((((long) x_diff_f)<<32)/(y_top_left - y_top_right)); 
//				}	else {
//				}
				
//				if( y_bottom_left > y_bottom_right ) {
//					dx_bottom		= (scanwidth<<16) + (int) ((((long) x_diff_f)<<32)/(y_bottom_left - y_bottom_right));
//				}	else 
//				if( y_bottom_left < y_bottom_right ) {
//					dx_bottom		= (scanwidth<<16) + (int) ((((long) x_diff_f)<<32)/(y_bottom_left - y_bottom_right));
//				}	else {
//				}
				
				
//				int scan_offset_x_f		= (x_left<<16);
//				int _xy_bottom   			= y_bottom_left*scanwidth + scan_offset_x_f;
//				int _xy_top	    		= y_top_left*scanwidth + scan_offset_x_f;
//				int next_x_top_left_f		= scan_offset_x_f + dx_top;
//				int next_x_bottom_left_f	= scan_offset_x_f + dx_bottom;

				
				// dVerStepSize is a factor that multiplied with the horizontal 
				// position on the poly gives the vertical step size in texture
				// (same relation)
				int dVerStepSize = ((((poly_verStepSize_right-poly_verStepSize_left)))/poly_in_clip_length);
				int viewportIdx = x_left - viewportX_left;

				// loop to draw left to right
				for( int columnCnt=x_left; columnCnt<=x_right; columnCnt++ ) {
					// vertical poly position start
					poly_v 				= 0;
					
					// calculate horizontal poly position
					poly_h				= basepoint + ((int)(distance*renderer.tan((angle_y_poly - renderer.getColumnAngle(columnCnt))))>>16);
					// calculate horizontal texture position
					texture_h 			= ((poly_h>>5)&0x00000007);
					
					// calculate vertical polygon step size per screen pixel
					poly_verStepsSize = poly_verStepSize_left + ((poly_h-poly_hor_offset)*dVerStepSize);
					
					// floor and ceil mapper angle
					int columnAngle = ((renderer.getColumnAngle(columnCnt))&0x000007FF);
					int angle_h     = ((angle + columnAngle)&0x000007FF);

//					int scan_offset_x_f	= (columnCnt<<16);
//					int xy_bottom   		= (y_bottom_left>>16)*scanwidth_f + scan_offset_x_f;
//					int xy_top	    		= (y_top_left>>16)*scanwidth_f + scan_offset_x_f;
					int xy_bottom   		= (y_bottom_left>>16)*scanwidth + columnCnt;
					int xy_top	    		= (y_top_left>>16)*scanwidth + columnCnt;

System.out.println( "xy_top: " +  (xy_top) + " viewportY_top[viewportIdx]: " + (viewportY_top[viewportIdx]) );			
System.out.println( "xy_bottom: " +  (xy_bottom) + " viewportY_bottom[viewportIdx]: " + (viewportY_bottom[viewportIdx]) );			
						
					// clip top and bottom
					if( xy_top < viewportY_top[viewportIdx] ) {
						// calculate new vertical postion on poly after clip
						poly_v += ((((long)poly_verStepsSize) * (viewportY_top[viewportIdx] - xy_top) * scanwidth_inv)>>16);
//						y_top_local = (viewportY_top[viewportIdx]>>16);
						y_top_local = (viewportY_top[viewportIdx]);
					}	else {
//						y_top_local = (int) (xy_top>>16);
						y_top_local = (int) (xy_top);
					}
					
					if( xy_bottom > viewportY_bottom[viewportIdx] ) {
						y_bottom_local = (viewportY_bottom[viewportIdx]);
//						y_bottom_local = (viewportY_bottom[viewportIdx]>>16);
					}	else {

//						y_bottom_local = (int) (xy_bottom>>16);
						y_bottom_local = (int) (xy_bottom);
					}

System.out.println( "xy_top: " +  (y_top_local));			
System.out.println( "xy_bottom: " +  (y_bottom_local) );			
System.out.println( "columnCnt: " + columnCnt );			
					
					// draw vertical pixels
//					int bufPos = (scanheight * columnCnt);
					for( int rowCnt=y_top_local; rowCnt<=y_bottom_local; rowCnt+=scanwidth) {
						// calculate vertical texture position,
						// since texturemap is linear buffer as well (y*width+x)
						// and thus needs to be multiplied by 8 (texture size at time of writing)
						// this means that instead of shifting right 21 bits, do 18
						// and mask at %111000 (0x38)
						final int texture_pos = ((poly_v>>18)&0x00000038) + texture_h;
						
						buffer[rowCnt] = texture[(texture_pos)];
						poly_v+=poly_verStepsSize;
					}
					
					y_top_left    += dy_top;
					y_bottom_left += dy_bottom;
/*					fake_y_top    += dy_top;
					fake_y_bottom += dy_bottom;
					
					while( fake_y_top > (1<<16) ) {
						xy_top+=(scanwidth<<16);
						fake_y_top-= (1<<16);
					}	
					while( fake_y_bottom > (1<<16) ) {
						xy_bottom+=(scanwidth<<16);
						fake_y_bottom-= (1<<16);
					}	
					while( fake_y_top < -(1<<16) ) {
						xy_top-=(scanwidth<<16);
						fake_y_top+= (1<<16);
					}	
					while( fake_y_bottom < -(1<<16) ) {
						xy_bottom-=(scanwidth<<16);
						fake_y_bottom+= (1<<16);
					}	
					
					xy_top+=(1<<16);//=dy_top;
					xy_bottom+=(1<<16);//=dy_bottom;			
*/
//					scan_offset+=scanheight;
					viewportIdx++;
				}
			}
		}	 
		
		return retVal;
	}


}
