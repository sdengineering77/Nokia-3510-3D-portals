package game.engine.data;

import graphics.engine.drawer.ClipArea;
import graphics.engine.object.Drawable;
import graphics.engine.object.ViewportWallQuad;
import graphics.engine.renderer.Renderer;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class RectangularPortal {
	public Drawable[]			polygons	= null;
	public int[]				viewport_left_bound = new int[0];
	public int[]				viewport_right_bound = new int[0];
	public int[]				viewport_top_bound = new int[0];
	public int[]				viewport_bottom_bound = new int[0];
	public int[]				viewport_distance_to_axis = new int[0];
	public int[]				viewport_angle = new int[0];
	public int[]				align = new int[0];
	public int				num_viewports = 0;
	public RectangularPortal[]	rectangularPortal = new RectangularPortal[0];
	public int				x_bound_left;
	public int				x_bound_right;
	public int				z_bound_top;
	public int				z_bound_bottom;
	public int				y_bound_top;
	public int				y_bound_bottom;
	
	public static int			max_y_climb = 64;


	public RectangularPortal(	int x_bound_left, int x_bound_right,
								int y_bound_top, int y_bound_bottom,
								int z_bound_top, int z_bound_bottom ) {
		this.num_viewports = 0;
		this.x_bound_left = x_bound_left;
		this.x_bound_right = x_bound_right;
		this.z_bound_top = z_bound_top;
		this.z_bound_bottom = z_bound_bottom;
		this.y_bound_top = y_bound_top;
		this.y_bound_bottom = y_bound_bottom;
	}
	public void addRectangularRoom2( RectangularPortal rectangularPortal, ViewportWallQuad viewportWallQuad, boolean isReachablePortal ) {
		int num_polygons = polygons.length - num_viewports - 1;
		
//		Drawable[] newPolygons = new Drawable[num_polygons+1];
//		//System.arraycopy(polygons, 0, newPolygons, 0, num_polygons );
//		polygons = newPolygons;
//System.out.println("trace1");
		polygons[num_polygons] = viewportWallQuad;
		
		if( isReachablePortal ) {
//System.out.println("trace2");
			int new_num_viewports = num_viewports + 1;
			int[]				new_viewport_left_bound = new int[new_num_viewports];
			int[]				new_viewport_right_bound = new int[new_num_viewports];
			int[]				new_viewport_top_bound = new int[new_num_viewports];
			int[]				new_viewport_bottom_bound = new int[new_num_viewports];
			int[]				new_viewport_distance_to_axis = new int[new_num_viewports];
			int[]				new_viewport_angle = new int[new_num_viewports];
			int[]				new_align = new int[new_num_viewports];
			RectangularPortal[]	new_rectangularPortal = new RectangularPortal[new_num_viewports];
			
			System.arraycopy(viewport_left_bound, 0, new_viewport_left_bound, 0, num_viewports );
			System.arraycopy(viewport_right_bound, 0, new_viewport_right_bound, 0, num_viewports );
			System.arraycopy(viewport_top_bound, 0, new_viewport_top_bound, 0, num_viewports );
			System.arraycopy(viewport_bottom_bound, 0, new_viewport_bottom_bound, 0, num_viewports );
			System.arraycopy(viewport_distance_to_axis, 0, new_viewport_distance_to_axis, 0, num_viewports );
			System.arraycopy(viewport_angle, 0, new_viewport_angle, 0, num_viewports );
			System.arraycopy(align, 0, new_align, 0, num_viewports );
			System.arraycopy(this.rectangularPortal, 0, new_rectangularPortal, 0, num_viewports );

			new_rectangularPortal[num_viewports] = rectangularPortal;
			new_viewport_top_bound[num_viewports] = (int)viewportWallQuad.height_world + viewportWallQuad.y_world;
			new_viewport_bottom_bound[num_viewports] = viewportWallQuad.y_world;
			new_viewport_angle[num_viewports] = viewportWallQuad.angle_wall;
			new_align[num_viewports] = viewportWallQuad.align;

//System.out.println("trace3");
			
			if( new_align[num_viewports] == ViewportWallQuad.WALL_ALIGNS_MAP_X ) {
				new_viewport_left_bound[num_viewports] = viewportWallQuad.x_world1;	
				new_viewport_right_bound[num_viewports] = viewportWallQuad.x_world2;	
				new_viewport_distance_to_axis[num_viewports] = viewportWallQuad.z_world1;
			}	else {
				new_viewport_left_bound[num_viewports] = viewportWallQuad.z_world1;	
				new_viewport_right_bound[num_viewports] = viewportWallQuad.z_world2;	
				new_viewport_distance_to_axis[num_viewports] = viewportWallQuad.x_world1;
			}

//System.out.println("trace4");
			viewport_left_bound 	= new_viewport_left_bound;
			viewport_right_bound 	= new_viewport_right_bound;
			viewport_top_bound   	= new_viewport_top_bound;
			viewport_bottom_bound	= new_viewport_bottom_bound;
			viewport_distance_to_axis=new_viewport_distance_to_axis;
			viewport_angle			= new_viewport_angle;
			align					= new_align;
			this.rectangularPortal	= new_rectangularPortal;

			System.gc();
			num_viewports++;
		}
	}


	/**
	 * @see graphics.engine.object.Drawable#draw(short[], int, int, int, int, ClipArea)
	 */
	public boolean draw(
		short[] buffer,
		int cam_x,
		int cam_y,
		int cam_z,
		int angle,
		ClipArea clipArea) {
		return false;
	}

	public RectangularPortal 	processMovement( Movement movement ) {
		RectangularPortal rectangularPortal = this;		
		boolean keepInPortalNorthBound = true;
		boolean keepInPortalSouthBound = true;
		boolean keepInPortalWestBound  = true;
		boolean keepInPortalEastBound  = true;
		int 	num_viewports 	= this.num_viewports-1;
		int 	x_from 			= movement.x_from;
		int 	y_from 			= movement.y_from;
		int 	z_from 			= movement.z_from;
		int 	x_to 			= movement.x_to;
		int 	y_to 			= movement.y_to;
		int 	z_to 			= movement.z_to;
		int 	distance 		= movement.movement_distance;
		int 	angle    		= movement.movement_angle;
		int 	camera_height 	= movement.movement_height;
		
		for( int viewport=num_viewports; viewport>=0; viewport-- ) {
			int viewport_left_bound 		= this.viewport_left_bound[viewport];
		 	int viewport_right_bound 		= this.viewport_right_bound[viewport];
	 		int viewport_top_bound			= this.viewport_top_bound[viewport];
	 		int viewport_bottom_bound		= this.viewport_bottom_bound[viewport];
	 		int viewport_distance_to_axis	= this.viewport_distance_to_axis[viewport];
	 		int viewport_angle 			= this.viewport_angle[viewport];
	 		int align						= this.align[viewport];
			
			if( align == ViewportWallQuad.WALL_ALIGNS_MAP_X ) {
				if( viewport_angle == 512 ) {
					// no bound detection needed?
					if( x_to > viewport_left_bound && x_to < viewport_right_bound ) {
						keepInPortalNorthBound = false;
					}
					
					// is viewport intersected on X axis?
					if( z_from < viewport_distance_to_axis && z_to >= viewport_distance_to_axis ) {
						// was it between viewport z bounds?
						if( ((x_from > viewport_left_bound && x_to < viewport_right_bound) ||
							 (x_to > viewport_left_bound && x_from < viewport_right_bound)) &&
							 (y_from >= (viewport_bottom_bound+camera_height-max_y_climb) && y_from < viewport_top_bound) ) {
							rectangularPortal = this.rectangularPortal[viewport].processMovement(movement);
							if( z_to == viewport_distance_to_axis ) {
								movement.z_to++;
							}
							break;
						}
							
					}
				}	else {
					// no bound detection needed?
					if( x_to < viewport_left_bound && x_to > viewport_right_bound ) {
						keepInPortalSouthBound = false;
					}
					
					// is viewport intersected on X axis?
					if( z_from > viewport_distance_to_axis && z_to <= viewport_distance_to_axis ) {
						// was it between viewport z bounds?
						if( (x_from < viewport_left_bound && x_to > viewport_right_bound) ||
							 (x_to < viewport_left_bound && x_from > viewport_right_bound) &&
							 (y_from >= (viewport_bottom_bound+camera_height-max_y_climb) && y_from < viewport_top_bound)) {
							rectangularPortal = this.rectangularPortal[viewport].processMovement(movement);
							if( z_to == viewport_distance_to_axis ) {
								movement.z_to--;
							}
							break;
						}
							
					}
				}
			}	else {
				if( viewport_angle == 1024 ) {
					// no bound detection needed?
					if( z_to > viewport_left_bound && z_to < viewport_right_bound ) {
						keepInPortalWestBound = false;
					}
					
					// is viewport intersected on X axis?
					if( x_from > viewport_distance_to_axis && x_to <= viewport_distance_to_axis ) {
						// was it between viewport z bounds?
						if( (z_from > viewport_left_bound && z_to < viewport_right_bound) ||
							 (z_to > viewport_left_bound && z_from < viewport_right_bound) &&
							 (y_from >= (viewport_bottom_bound+camera_height-max_y_climb) && y_from < viewport_top_bound)) {
							rectangularPortal = this.rectangularPortal[viewport].processMovement(movement);
							if( x_to == viewport_distance_to_axis ) {
								movement.x_to--;
							}
							break;
						}
							
					}
				}	else {
					// no bound detection needed?
					if( z_to < viewport_left_bound && z_to > viewport_right_bound ) {
						keepInPortalEastBound = false;
					}
					
					// is viewport intersected on X axis?
					if( x_from < viewport_distance_to_axis && x_to >= viewport_distance_to_axis ) {
						// was it between viewport z bounds?
						if( (z_from < viewport_left_bound && z_to > viewport_right_bound) ||
							 (z_to < viewport_left_bound && z_from > viewport_right_bound) &&
							 (y_from >= (viewport_bottom_bound+camera_height-max_y_climb) && y_from < viewport_top_bound)) {
							rectangularPortal = this.rectangularPortal[viewport].processMovement(movement);
							if( x_to == viewport_distance_to_axis ) {
								movement.x_to++;
							}
							break;
						}
							
					}
				}
			}
			
		}

		if( rectangularPortal == this ) {
			if( keepInPortalWestBound && (x_to < x_bound_left) ) { 
				movement.x_to = x_bound_left;
			}
			if( keepInPortalEastBound && (x_to > x_bound_right) ) { 
				movement.x_to = x_bound_right;
			}
			if( keepInPortalSouthBound && (z_to < z_bound_bottom) ) { 
				movement.z_to = z_bound_bottom;
			}
			if( keepInPortalNorthBound && (z_to > z_bound_top) ) { 
				movement.z_to = z_bound_top;
			}
			
			if( y_to > y_bound_top ) { 
				movement.y_to = y_bound_top;
			}	else
			if( y_to < (y_bound_bottom+camera_height) ) { 
				movement.y_to = (y_bound_bottom+camera_height);
			}
		}

		return rectangularPortal;
	}
	
/*
 * 				// determine distance and basepoint int poly space
				// distance is distance between camera location in world perpenticular to poly
				// basepoint is distance between first point of poly (x1,z1) and
				//           the point that lies perpenticular with camera location
			if( viewport_angle == 512 || viewport_angle == 1536 ) {
				perpendicular_distance   = (viewportZ_bottomleft - z_from)*align;  
				basepoint  = (x_from - viewportX_bottomleft)*align; 
			}	else {
				perpendicular_distance   = (viewportX_bottomleft - x_from)*align;  
				basepoint  = (viewportZ_bottomleft - z_from)*align; 
			}
	
			if( perpendicular_distance >= 0 ) {
			
				perpendicular_distance = Math.abs(perpendicular_distance);
		
				
				/** 
				 * Clip poly in horizontal viewport (also discarts non visible poly)
				 * Determine left and right x_screen coordinates in perspective
				 * Determine base y screen coordinate and height in perspective
				 * Determine left and right vertical texture step size per screen pixel
				 */
		
/*				// convert view angle to "poly" space
				int angle_y_poly;
				if( distance == 0 ) {
					angle_y_poly = 0;
				}	else {
					angle_y_poly	= ((viewport_angle - angle)&0x000007FF);
					if( angle_y_poly > 1024 ) {
						angle_y_poly -= 2048;
					}
				}
				
				// angles are valid if between -90 and +90 degrees in relation to the poly
				if( angle_y_poly >-512 && angle_y_poly <512 ) {
					// angle of most left and right pixel in screen and position on poly (may be outside the poly)
					int position_clip			= basepoint + ((perpendicular_distance*tan[(angle_y_poly&0x000007FF)])>>16);
					if( position_clip > 0 && position_clip < viewport_width ) {
//						keepInPortalHorizontalBounds = false;
						int real_distance = perpendicular_distance * sin_inv[((angle_y_poly+512)&0x000007FF)];
						if( distance >= real_distance ) {
							rectangularPortal = this.rectangularPortal[viewport].processMovement(movement);
							break;
						}
							
					}
				
				}
			}
*/

}
