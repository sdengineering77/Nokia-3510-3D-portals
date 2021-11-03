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
public class FloorRectangle_i_raycast_backup implements Drawable {
	public final static int	WALL_ALIGNS_MAP_Y	= 0;
	public final static int	WALL_ALIGNS_MAP_X	= 1;

	public int[] 		align 		= new int[4];//WALL_ALIGNS_MAP_X; 	
	public int[] 		angle_wall	= new int[4];

	public int[]	x_world = new int[4];
	public int[]	z_world = new int[4];

//	public int x_world1, x_world2, x_world3, x_world4;
//	public int z_world1, z_world2, z_world3, z_world4;
	public int y_world;
//	public long 	height_world;
	public int[]	length_world = new int[4];

//	public int length_inv;	
	public int[] length_left_correction 	= new int[4];	
	public int[] length_right_correction 	= new int[4];	
	public long renderer_focuspointDepth	= 48;
	public long renderer_width			= 48;
	public long renderer_height			= 48;
	public int  scanheight				= 0;
//	public int  scanwidth					= 0;
//	public int  scanwidth_inv				= 0;
//	public int texture_offset_h 			= 0;
	public Renderer 		renderer		= null;
	public int 			id				= 0;
	public int[]			textureSpaceSin	= new int[4]; 
	public int[]			textureSpaceCos	= new int[4]; 
	public final static 	int[] x_from_s	= new int[4];
	public final static 	int[] y_from_s	= new int[4];
	public final static 	int[] x_to_s	= new int[4];
	public final static 	int[] y_to_s	= new int[4];
		
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

		
	private final static short[] texture = new short[] { 
		'0','1','0','0','0','1','0','0',
		'1','0','0','0','0','0','1','1',
		'0','1','0','0','0','1','0','0',
		'0','0','1','1','1','0','0','0',
		'0','1','0','0','0','1','0','0',
		'1','0','0','0','0','0','1','1',
		'0','1','0','0','0','1','0','0',
		'0','0','1','1','1','0','0','0'
	};

	
	public FloorRectangle_i_raycast_backup( int x1, int y1, int z1,
						int x2, int y2, int z2,
						int x3, int y3, int z3,
						int x4, int y4, int z4, Renderer renderer) {
	
		x_world[0] = x1; 
		x_world[1] = x2;
		x_world[2] = x3; 
		x_world[3] = x4;
		z_world[0] = z1; 
		z_world[1] = z2;
		z_world[2] = z3; 
		z_world[3] = z4;

/*		x_world1 = x1; 
		x_world2 = x2;
		x_world3 = x3; 
		x_world4 = x4;
		z_world1 = z1; 
		z_world2 = z2;
		z_world3 = z3; 
		z_world4 = z4;
*/		
		y_world = y1;
//		height_world = y1 - y3;

		this.renderer = renderer;
		this.renderer_focuspointDepth = (renderer.getFocuspoint_depth()<<16);
		this.renderer_width			= renderer.getWidth();
		this.renderer_height			= renderer.getHeight();
		this.scanheight				= (int) (renderer_height>>15);
//		this.scanwidth					= (int) (renderer_width>>15);
//		this.scanwidth_inv				= (int) (((1<<16)/((long)scanwidth)));

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

//System.out.println( "vertice: " +  vertice + " next_vertice : " + next_vertice );			

			next_vertice = ((next_vertice+1)&0x00000003);
			
			textureSpaceSin[vertice] 			= (renderer.sin(angle_wall[vertice])>>16); 
			textureSpaceCos[vertice] 			= (renderer.cos(angle_wall[vertice])>>16); 

			length_world[vertice] 				= (int) (Float.sqrt( new Float(xDiff*xDiff + zDiff*zDiff) ).toLong());
			length_left_correction[vertice] 	= -length_world[vertice]/100;	
			length_right_correction[vertice]	= length_world[vertice] - length_left_correction[vertice];	

		}
		

//		length_inv				= ((1<<16)/length_world);	
		

		
	}

	/**
	 * 	Draws an horizontal aligned textured quad.<br><br>
	 * 
	 * Note that this method should be declared synchronized 
	 * but is not due Me wanting speeeeeed!
	 */
	public boolean draw( short[] buffer, int cam_x, int cam_y, int cam_z, int angle, ClipArea clipArea ) {
		boolean retVal = true;

		int[] 	viewportY_top 		= clipArea.viewportY_top_array;
		int[]	viewportY_bottom 	= clipArea.viewportY_bottom_array;
		int	viewportX_left 		= clipArea.viewportX_left;
		int  	viewportX_right 	= clipArea.viewportX_right;

		int[] x_from_right	= this.x_from_right;
		int[] y_from_right	= this.y_from_right;
		int[] x_to_right	= this.x_to_right;
		int[] y_to_right	= this.y_to_right;
		int[] x_from_left	= this.x_from_left;
		int[] y_from_left	= this.y_from_left;
		int[] x_to_left  	= this.x_to_left;
		int[] y_to_left	= this.y_to_left;
		int[] dxdy_left	= this.dxdy_left;
		int[] dxdy_right	= this.dxdy_right;
		int[] x_from_s		= this.x_from_s;
		int[] y_from_s		= this.y_from_s;
		int[] x_to_s		= this.x_to_s;
		int[] y_to_s		= this.y_to_s;
		int   scanheight	= this.scanheight;

/*int x1 = 20; 
		int y1 = 10; 
		int x2 = 50; 
		int y2 = 20; 
		int x3 = 20; 
		int y3 = 45; 
		int x4 = 10; 
		int y4 = 35;
*/

		int 	num_vectors;

		// Transform quad's vectors
		// If a vector falls out due y clipping a horizontal vector  
		// is added at the bottom of the screen. (likely when camera is on
		// that poly)
		
		
/*
		int x1 = 20; 
		int y1 = 10; 
		int x2 = 50; 
		int y2 = 10; 
		int x3 = 60; 
		int y3 = 50; 
		int x4 = 10; 
		int y4 = 50;


		x_from_s[0]	= x1;// right, left
		y_from_s[0]	= y1;
		x_to_s[0]	= x2;
		y_to_s[0]	= y2;

		x_from_s[1]	= x2;// right, left
		y_from_s[1]	= y2;
		x_to_s[1]	= x3;
		y_to_s[1]	= y3;

		x_from_s[2]	= x3;// right, left
		y_from_s[2]	= y3;
		x_to_s[2]	= x4;
		y_to_s[2]	= y4;

		x_from_s[3]	= x4;// right, left
		y_from_s[3]	= y4;
		x_to_s[3]	= x1;
		y_to_s[3]	= y1;
*/		
		num_vectors = transform(angle, cam_x,cam_y,cam_z, viewportX_left, viewportX_right);		


		boolean hasBottom = num_vectors>2;
		if( num_vectors == 2 ) {
			if( (x_to_s[0] < x_from_s[0] && x_from_s[1] < x_to_s[1]) ||
				 (x_from_s[0] < x_to_s[0] && x_to_s[1] < x_from_s[1]) ) {
			 	hasBottom = true;
			}
		}	else 
		if( num_vectors == 3 ) {
			if( x_from_s[0] <= x_to_s[0] &&
				x_from_s[1] <= x_to_s[1] &&
				x_from_s[2] <= x_to_s[2] ) {
				hasBottom = false;
			}
		}
		
		// vectors are expected the following way:
		// y_from > y_to means world left side of floor/ceil rectangle
		// y_from < y_to means world right side
		//
		// directly filter out horizontal vectors
		// directly stores y_top and y_bottom
		int poly_h_left  = viewportX_right;//(x_from_left[0]<<16);
		int poly_h_right = viewportX_left;//(x_from_right[0]<<16);
		int y_top = scanheight;
		int y_bottom = 0;
		int num_vectors_left = 0;
		int num_vectors_right = 0;
		for( int vectors=0; vectors<num_vectors; vectors++ ) {
			// determine left and right
			if( poly_h_left > x_from_s[vectors] ) {
				poly_h_left = x_from_s[vectors];
			}			
			if( poly_h_left > x_to_s[vectors] ) {
				poly_h_left = x_to_s[vectors];
			}			

			if( poly_h_right < x_from_s[vectors] ) {
				poly_h_right = x_from_s[vectors];
			}			
			if( poly_h_right < x_to_s[vectors] ) {
				poly_h_right = x_to_s[vectors];
			}			

			// determine top and bottom
			if( y_top > y_from_s[vectors] ) {
				y_top = y_from_s[vectors];
			}	
			if( y_top > y_to_s[vectors] ) {
				y_top = y_to_s[vectors];
			}

			if( hasBottom ) {
				if( y_bottom < y_from_s[vectors] ) {
					y_bottom = y_from_s[vectors];
				}
				
				if( y_bottom < y_to_s[vectors] ) {
					y_bottom = y_to_s[vectors];
				}
			}

System.out.println( "y_bottom: " +  y_bottom + " y_top : " + y_top );			
System.out.println( "poly_h_left: " +  poly_h_left + " poly_h_right: " + poly_h_right );			
System.out.println( "x_from_s[vectors]: " +  x_from_s[vectors] );			
System.out.println( "x_to_s[vectors]  : " +  x_to_s[vectors] );			
System.out.println( "y_from_s[vectors]: " +  y_from_s[vectors] );			
System.out.println( "y_to_s[vectors]  : " +  y_to_s[vectors] );			

			// sort to left or right array if not completely horizontal
			// else just discard...
			if( y_from_s[vectors] > y_to_s[vectors] ) {
				// they are targeted right, but since drawing process
				// needs them to have y_from < y_to directly switch values..
				x_from_left[num_vectors_left] = x_to_s[vectors];
				x_to_left[num_vectors_left] = x_from_s[vectors];
				y_from_left[num_vectors_left] = y_to_s[vectors];
				y_to_left[num_vectors_left] = y_from_s[vectors];
				num_vectors_left++;
			} 	else
			if( y_from_s[vectors] < y_to_s[vectors] ) {
				x_from_right[num_vectors_right] = x_from_s[vectors];
				x_to_right[num_vectors_right] = x_to_s[vectors];
				y_from_right[num_vectors_right] = y_from_s[vectors];
				y_to_right[num_vectors_right] = y_to_s[vectors];
				num_vectors_right++;
			}   // else it is horizontal... 
		}		
		
		if( y_bottom == 0 ) {
			y_bottom = scanheight;
		}		

		// sort vectors if neccessary
		if( num_vectors_left > 1 && y_from_left[0] > y_from_left[1] ) {
			int y_from = y_from_left[0];
			int y_to   = y_to_left[0];
			y_from_left[0] = y_from_left[1];
			y_from_left[1] = y_from;
			y_to_left[0] = y_to_left[1];
			y_to_left[1] = y_to;
			int x_from = x_from_left[0];
			int x_to   = x_to_left[0];
			x_from_left[0] = x_from_left[1];
			x_from_left[1] = x_from;
			x_to_left[0] = x_to_left[1];
			x_to_left[1] = x_to;
		}

		if( num_vectors_right > 1 && y_from_right[0] > y_from_right[1] ) {
			int y_from = y_from_right[0];
			int y_to   = y_to_right[0];
			y_from_right[0] = y_from_right[1];
			y_from_right[1] = y_from;
			y_to_right[0] = y_to_right[1];
			y_to_right[1] = y_to;
			int x_from = x_from_right[0];
			int x_to   = x_to_right[0];
			x_from_right[0] = x_from_right[1];
			x_from_right[1] = x_from;
			x_to_right[0] = x_to_right[1];
			x_to_right[1] = x_to;
		}

////System.out.println( "x_from_left.length: " +  num_vectors_left + " x_from_right.length: " + num_vectors_right );			
		
		// determine direction coefficients of right and left side
		for( int vectors=0; vectors<num_vectors_left; vectors++ ) {
			int width = x_to_left[vectors] - x_from_left[vectors];
			int height = y_to_left[vectors] - y_from_left[vectors];
			
			dxdy_left[vectors]   = (width<<16)/height;
		}

		for( int vectors=0; vectors<num_vectors_right; vectors++ ) {
			int width = x_to_right[vectors] - x_from_right[vectors];
			int height = y_to_right[vectors] - y_from_right[vectors];
			
			dxdy_right[vectors]   = (width<<16)/height;
		}

		// draw 
		int currVectorIdx_left = 0;
		int currVectorIdx_right = 0;
		int currDxdy_left = 0;//dxdy_left[0];
		int currDxdy_right = 0;//dxdy_right[0];
		poly_h_left  <<= 16;
		poly_h_right <<= 16;
		
		
// must get rewritten with future viewportY_bottom
		if( y_bottom > scanheight ) {
			y_bottom = scanheight;
		}
		for( int row=y_top; row<y_bottom; row++ ) {
			// is row not on a vector? dx should be zero then...
			if(	(currVectorIdx_left < 1 ||
					 row < y_from_left[currVectorIdx_left-1] ||
					 row >=   y_to_left[currVectorIdx_left-1]) 
					 &&
					(currVectorIdx_left >= num_vectors_left ||
			 		 row < y_from_left[currVectorIdx_left] ||
			 		 row >=   y_to_left[currVectorIdx_left]) ) {
				currDxdy_left = 0;
			}	else // if it is bigger than, shift VectorIdx and change dx
			if( currVectorIdx_left < num_vectors_left &&
				row >= y_from_left[currVectorIdx_left] ) {
				currDxdy_left = dxdy_left[currVectorIdx_left];
				poly_h_left = (x_from_left[currVectorIdx_left]<<16);
				currVectorIdx_left++;
			}

			// is row not on a vector? dx should be zero then...
			if(	(currVectorIdx_right < 1 ||
					 row < y_from_right[currVectorIdx_right-1] ||
					 row >=   y_to_right[currVectorIdx_right-1]) 
					 &&
					(currVectorIdx_right >= num_vectors_right ||
			 		 row < y_from_right[currVectorIdx_right] ||
			 		 row >=   y_to_right[currVectorIdx_right]) ) {
				currDxdy_right = 0;
			}	else // if it is bigger than, shift VectorIdx and change dx
			if( currVectorIdx_right < num_vectors_right &&
				row >= y_from_right[currVectorIdx_right] ) {
				currDxdy_right = dxdy_right[currVectorIdx_right];
				poly_h_right = (x_from_right[currVectorIdx_right]<<16);
				currVectorIdx_right++;
			}
System.out.println( "currVectorIdx_left: " +  currVectorIdx_left + " currVectorIdx_right: " + currVectorIdx_right );			

/*			
//System.out.println( "row" +  row );			
//System.out.println( "currDxdy_left: " +  currDxdy_left + " currDxdy_right: " + currDxdy_right );			
//System.out.println( "y_to_left[currVectorIdx_left]:     " +  y_to_left[currVectorIdx_left-1] );			
//System.out.println( "y_to_right[currVectorIdx_right-1]: " +  y_to_right[currVectorIdx_right-1] );			


//System.out.println( (currVectorIdx_right < 1 ||
					(row < y_from_right[currVectorIdx_right-1] &&
					 row >   y_to_right[currVectorIdx_right-1])) );

//System.out.println( (currVectorIdx_right >= y_from_right.length ||
			 		(row < y_from_right[currVectorIdx_right] &&
			 		 row >   y_to_right[currVectorIdx_right])) );
*/
			// draw line
			int yx_left_local  = (poly_h_left >>16)*scanheight + row;
			int yx_right_local = (poly_h_right>>16)*scanheight + row;
			
			int columnCnt = poly_h_left>>16;
			for( int yx=yx_left_local; yx<yx_right_local; yx+=scanheight ) {
//				buffer[yx] = '1';
				int columnAngle = ((renderer.getColumnAngle(columnCnt))&0x000007FF);
				int angle_h     = ((angle + columnAngle)&0x000007FF);
				final int distance_cam_y = (((y_world-cam_y) * renderer.cos_inv(columnAngle))>>16);
//				final int y_viewport = (viewportY_bottom[viewportIdx]>>16);
				int y = row;//y_bottom_local - scan_offset;
				final int cos = renderer.cos(angle_h);
				final int sin = renderer.sin(angle_h);
				final int angle_v = renderer.getRowAngle(y++);
				final int z_ray = ((distance_cam_y * renderer.tan_inv[(angle_v&0x000007FF)])>>16);
				final int floor_h = ((((cam_x<<16) - z_ray * cos)>>21)&0x00000007); 
				final int texture_pos = ((((cam_z<<16) - z_ray * sin)>>18)&0x00000038) + floor_h;
				buffer[yx] = texture[(texture_pos)];
				columnCnt++;
				
			}

			poly_h_left += currDxdy_left;
			poly_h_right += currDxdy_right;

		}


		return retVal;
	}
	
	public int transform(int angle, int cam_x, int cam_y, int cam_z, int viewportX_left, int viewportX_right) {
		int num_vectors_left	= 0;
		int num_vectors_right	= 0;
		int num_vectors		= 0;
		int[] x_from_s			= this.x_from_s;
		int[] y_from_s			= this.y_from_s;
		int[] x_to_s			= this.x_to_s;
		int[] y_to_s			= this.y_to_s;

 		int[] x_from_right		= this.x_from_right;
		int[] y_from_right		= this.y_from_right;
		int[] x_to_right		= this.x_to_right;
		int[] y_to_right		= this.y_to_right;

		int[] x_from_left		= this.x_from_left;
		int[] y_from_left		= this.y_from_left;
		int[] x_to_left  		= this.x_to_left;
		int[] y_to_left		= this.y_to_left;

		for( int vectors=0; vectors<4; vectors++ ) {
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
			long distance_abs;
			long distance;
			int basepoint;
			int isInFront_left;
			int isInFront_right;
	
			int angle_sin_clip_left 	= ((angle + renderer.getColumnAngle(viewportX_left))&0x000007FF);
			int angle_sin_clip_right 	= ((angle + renderer.getColumnAngle(viewportX_right))&0x000007FF);
			int angle_cos_clip_left 	= ((angle + renderer.getColumnAngle(viewportX_left) + 512)&0x000007FF);
			int angle_cos_clip_right 	= ((angle + renderer.getColumnAngle(viewportX_right) + 512)&0x000007FF);
			if( align[vectors] == WALL_ALIGNS_MAP_X ) {
				isInFront_left	= (z_world[vectors] - cam_z)*renderer._sin[angle_sin_clip_left];
				isInFront_right	= (z_world[vectors] - cam_z)*renderer._sin[angle_sin_clip_right];
				distance   		= (z_world[vectors] - cam_z)*textureSpaceSin[vectors];  
				distance_abs   	= Math.abs(distance);  
				basepoint  = (cam_x - x_world[vectors])*textureSpaceSin[vectors];//renderer.sin(angle_wall))>>16); 
			}	else {
				isInFront_left	= (x_world[vectors] - cam_x)*renderer._sin[angle_cos_clip_left];
				isInFront_right	= (x_world[vectors] - cam_x)*renderer._sin[angle_cos_clip_right];
				distance   		= (x_world[vectors] - cam_x)*textureSpaceCos[vectors];  
				distance_abs   	= Math.abs(distance);  
				basepoint  = (z_world[vectors] - cam_z)*textureSpaceCos[vectors]; 
			}
	// WEGHALEN!!
System.out.println( "isInFront_left:  " + isInFront_left );
System.out.println( "isInFront_right: " + isInFront_right );
	//		texture_offset_h = worldSpaceRect.texture_offset_h;
			int poly_in_clip_length			 = length_world[vectors];
	
			// convert view angle to "poly" space
			int angle_y_poly	= ((angle_wall[vectors] - angle)&0x000007FF);
			if( angle_y_poly > 1024 ) {
				angle_y_poly -= 2048;
			}
			// angle of most left and right pixel in screen and position on poly (may be outside the poly)
			int angle_y_clip_left 		= angle_y_poly - renderer.getColumnAngle(viewportX_left); 
//			long position_clip_left	= basepoint + ((distance_abs*renderer.tan(angle_y_clip_left))>>16);
			long position_clip_left	= basepoint + ((distance*renderer.tan(angle_y_clip_left))>>16);
			
			int angle_y_clip_right 	= angle_y_poly - renderer.getColumnAngle(viewportX_right); 
//			long position_clip_right	= basepoint + ((distance_abs*renderer.tan(angle_y_clip_right))>>16);
			long position_clip_right	= basepoint + ((distance*renderer.tan(angle_y_clip_right))>>16);
	
			// angles are valid if between -90 and +90 degrees in relation to the poly
			boolean angle_y_left_inClip 	= 	(angle_y_clip_left >-512  	&& angle_y_clip_left <512);
			boolean angle_y_right_inClip 	= 	(angle_y_clip_right>-512 	&& angle_y_clip_right<512);
	
	System.out.println( "ID===============: " + id );
	System.out.println( "angle_y_clip_left: " + angle_y_clip_left );
	System.out.println( "angle_y_left_inClip: " + angle_y_left_inClip );
	System.out.println( "position_clip_left: " + position_clip_left );
	System.out.println( "angle_y_clip_right: " + angle_y_clip_right );
	System.out.println( "angle_y_right_inClip: " + angle_y_right_inClip );
	System.out.println( "position_clip_right: " + position_clip_right );
//	//System.out.println( "_position_clip_right: " + _position_clip_right );
	System.out.println( "angle_y_poly: " + angle_y_poly );
	System.out.println( "angle_wall: " + angle_wall[vectors] );
	System.out.println( "angle: " + angle );
	System.out.println( "distance===============: " + ((double) distance) );
	System.out.println( "basepoint===============: " + ((double) basepoint) );
	
			
			// parts visible?
//			if(	(position_clip_left >0 && position_clip_left <poly_in_clip_length && angle_y_left_inClip) || 
//					(position_clip_right>0 && position_clip_right<poly_in_clip_length && angle_y_right_inClip) ||
//					(position_clip_right>0 && position_clip_left <poly_in_clip_length && (angle_y_right_inClip || angle_y_left_inClip)) ||
//					(position_clip_right>0 && angle_y_right_inClip && !angle_y_left_inClip) ||
//					(position_clip_left<poly_in_clip_length && angle_y_left_inClip &&!angle_y_right_inClip)
//					) {
			if( (angle_y_left_inClip && isInFront_right<=0 && isInFront_left>0 && position_clip_left<poly_in_clip_length) ||
				(!angle_y_left_inClip && isInFront_right<=0 && isInFront_left>0 && position_clip_left>0) ||
				(!angle_y_right_inClip && isInFront_left<=0 && isInFront_right>0 && position_clip_right<poly_in_clip_length) ||
				(angle_y_right_inClip && isInFront_left<=0 && isInFront_right>0 && position_clip_right>0) ||
				(angle_y_right_inClip && angle_y_left_inClip && isInFront_left>0 && isInFront_right>0 && position_clip_left<poly_in_clip_length && position_clip_right>0) ||
				(!angle_y_right_inClip && !angle_y_left_inClip && isInFront_left>0 && isInFront_right>0 && position_clip_left>0 && position_clip_right<poly_in_clip_length)
								) {
				// translate y
				long  	z_camera_left;
				long  	z_camera_right;
				int 	y_camera  = y_world - cam_y;
System.out.println( "y_camera===============: " + ((double) y_camera) );
System.out.println( "angle: " + angle );
			
				// determine x of left and right side
				// check if poly hits right or left clip. if so calculate point on
				// poly and translate z else determine screen space x
				// left side
				if( (isInFront_left>=0 && position_clip_left>=length_left_correction[vectors] && position_clip_left<=length_right_correction[vectors]) ) {
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
					//				long zm = (distance_abs * renderer.cos_inv(angle_y));
					//				int  z  = (int) ((zm - (((((zm*sin_angle_y)>>16) - (basepoint<<16)) * sin_angle_y)>>16))>>16);
					// translate to camera space
System.out.println( "l1ID===============: " + id );
					z_camera_left  = (((distance * renderer.cos_inv(angle_y_clip_left))*renderer.cos(angle_y_clip_left-angle_y_poly))>>32);
					long z_factor 		= (renderer_focuspointDepth)/z_camera_left;
//					int  height_camera	= (int) (height_world*z_factor);
					
					
					x_from_s[num_vectors]			= viewportX_left; 
					y_from_s[num_vectors]			= (int) ((renderer_height - (y_camera*z_factor))>>16);
//					y_top_left					= y_bottom_left - height_camera;
//					poly_verStepSize_left   	= (int) ((height_world<<32) / height_camera);
	
//					poly_hor_offset= (int) position_clip_left;
//					poly_in_clip_length 						   -= poly_hor_offset; // = (int) position_clip_left (saves cast)
	
				}	else {
					// not on poly, transform to screen space and setup 
					// left poly attributes
					// assume camera space coordinates for x1 etc
System.out.println( "l2ID===============: " + id );
					long	length_r_bp;
					if( distance < 0 ) {
						length_r_bp = -(length_world[vectors]-basepoint);
					}	else {
						length_r_bp = basepoint;
					}
					z_camera_left = ((renderer.cos(angle_y_poly) * distance    - renderer.sin(angle_y_poly) * length_r_bp)>>16);
					long	x_camera = ((renderer.cos(angle_y_poly) * length_r_bp + renderer.sin(angle_y_poly) * distance)>>16); // actual calculation ((-renderer.cos(angle_y) * basepoint - renderer.sin(angle_y) * distance_abs)>>16)
	
					long z_factor 				= (renderer_focuspointDepth)/z_camera_left;
//					int height_camera			= (int) (height_world*z_factor);

					x_from_s[num_vectors]			= (int) ((renderer_width  - (x_camera*z_factor))>>16); // beware x_cam is subtracted due calculation optimalization
									
					y_from_s[num_vectors]			= (int) ((renderer_height - (y_camera*z_factor))>>16);
//					poly_verStepSize_left	= (int) ((height_world<<32) / height_camera);
	
//					poly_hor_offset = 0;
	
				}
				
				// set stepSize the drawer needs to start with
//				poly_verStepsSize 		= poly_verStepSize_left;
		
				// right side (use worldSpaceRect.length)
				if( (isInFront_right>=0 && position_clip_right>=length_left_correction[vectors] && position_clip_right<=length_right_correction[vectors]) ) {
					// it is on poly, determine camera space x, y, z
					// then transform y to screen space  
					//	z2  = cameraSpaceRect.z1 + ((int) (((cameraSpaceRect.z2 - cameraSpaceRect.z1) * (position_clip_right*length_inv))>>16));
	
					// long zm = ((long) distance_abs * renderer.cos_inv(angle_y_clip_right));
					// int  z  = (int) ((zm*renderer.cos(angle_y_clip_right-angle_y))>>32);
System.out.println( "r1ID===============: " + id );
	
					z_camera_right  = (((distance * renderer.cos_inv(angle_y_clip_right))*renderer.cos(angle_y_clip_right-angle_y_poly))>>32);
	
					long z_factor 		= (renderer_focuspointDepth)/z_camera_right;
//					int height_camera	= (int) (height_world*z_factor);
					
					
					x_to_s[num_vectors]				= viewportX_right; 
					y_to_s[num_vectors]				= (int) ((renderer_height - (y_camera*z_factor))>>16);
//					poly_verStepSize_right   = (int) ((height_world<<32) / height_camera);
//					poly_in_clip_length 					   -= (int) (length_world - position_clip_right);
	
				}	else {
					// not on poly, transform to screen space and setup 
					// left poly attributes
System.out.println( "r2ID===============: " + id );
					long	length_r_bp;
					if( distance < 0 ) {
						length_r_bp = -basepoint;
					}	else {
						length_r_bp = (length_world[vectors]-basepoint);
					}
					z_camera_right = ((renderer.cos(angle_y_poly) * distance    + renderer.sin(angle_y_poly) * length_r_bp)>>16);
					long	x_camera = ((renderer.cos(angle_y_poly) * length_r_bp - renderer.sin(angle_y_poly) * distance)>>16);
	
					long z_factor 		= (renderer_focuspointDepth)/z_camera_right;
//					int  height_camera	= (int) (height_world*z_factor);

					x_to_s[num_vectors]			= (int) ((renderer_width  + (x_camera*z_factor))>>16);
					y_to_s[num_vectors]			= (int) ((renderer_height - (y_camera*z_factor))>>16);
//					poly_verStepSize_right  = (int) ((height_world<<32) / height_camera);
				}

				if( distance < 0 ) {
					int x = x_to_s[num_vectors];						
					int y = y_to_s[num_vectors];
					
					x_to_s[num_vectors] = x_from_s[num_vectors];						
					y_to_s[num_vectors] = y_from_s[num_vectors];
					x_from_s[num_vectors] = x;						
					y_from_s[num_vectors] = y;
				}				
				num_vectors++;
			}
		}
		
		return num_vectors;
	}


}
