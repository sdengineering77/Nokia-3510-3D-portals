package graphics.engine.drawer;



/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ClipArea {
	public int 	viewportY_top			= 0;
	public int 	viewportY_bottom		= 0;
	public int[] 	viewportY_top_array		= null;
	public int[] 	viewportY_bottom_array	= null;
//	public int[] 	default_viewportY_top_array		= null;
//	public int[] 	default_viewportY_bottom_array	= null;
	public int	viewportX_left			= 0;
	public int 	viewportX_right 		= 0;
	public int[] 	viewportX_left_array	= null;
	public int[] 	viewportX_right_array	= null;

	public int 	scanwidth				= 0;
	public int 	scanwidth_f				= 0;
	public int 	scanheight				= 0;
	public int 	scanheight_f			= 0;
	
	public ClipArea(int scanwidth, int scanheight) {
		this.scanwidth 		= scanwidth;
		this.scanwidth_f 		= (scanwidth<<16);
		this.scanheight 		= scanheight;
		this.scanheight_f 		= (scanheight<<16);
		viewportY_top_array		= new int[scanwidth+1];
		viewportY_bottom_array	= new int[scanwidth+1];
		viewportX_left_array	= new int[scanheight+1];
		viewportX_right_array	= new int[scanheight+1];

//		default_viewportY_top_array	= new int[scanwidth+1];
//		default_viewportY_bottom_array	= new int[scanwidth+1];
		
//		int bottom_offset = ((scanheight * scanwidth)<<16);
//		for( int column=0; column<=scanwidth; column++ ) {
//			default_viewportY_top_array[column]    = (column<<16);
//			default_viewportY_bottom_array[column] = (column<<16) + bottom_offset;
//		}
	}
	
	
	public void setClipArea( int x1, int y1, 
							  int x2, int y2,
							  int x3, int y3,
							  int x4, int y4) {
		int[] 	viewportY_top_array		= this.viewportY_top_array;
		int[] 	viewportY_bottom_array	= this.viewportY_bottom_array;
		int[] 	viewportX_left_array	= this.viewportX_left_array;
		int[] 	viewportX_right_array	= this.viewportX_right_array;

		int width  		= x2 - x1;
//		int currY_top_f	= (y1<<16);
//		int currY_bottom_f	= (y3<<16);
///		int dy_top_f 		= (int) (((long) (y2 - y1))<<16) / width;
//		int dy_bottom_f	= (int) (((long) (y4 - y3))<<16) / width;

		viewportX_left		= x1;
		viewportX_right		= x2;
		
//		for( int columnCnt=viewportX_left; columnCnt<=viewportX_right; columnCnt++ ) {
//			viewportY_top_array[columnCnt] 		= (currY_top_f>>16)*scanwidth    + columnCnt;
//			viewportY_bottom_array[columnCnt] 	= (currY_bottom_f>>16)*scanwidth + columnCnt;
			
//			currY_top_f 	+= dy_top_f;
//			currY_bottom_f 	+= dy_bottom_f;
//		}	

		int dx_top_f 		= (int) (((long) width)<<16) / (y2 - y1);
		int dx_bottom_f	= (int) (((long) width)<<16) / (y4 - y3);
		int xy_left    = scanwidth*y1;
		int xy_right = scanwidth*y2;
		int currX_f = (x1<<16);
		int prevX   = x1;
		int currX   = 0;
		for( int xy=xy_left; xy<=xy_right; xy+=scanwidth ) {
			currX_f+=dx_top_f;
			currX = (currX_f>>16);
			for( int columnCnt=prevX; columnCnt<currX; columnCnt++ ) {
				viewportY_bottom_array[columnCnt] 	= xy + columnCnt;				
			}
		}

		xy_left    = scanwidth*y3;
		xy_right = scanwidth*y4;
		for( int xy=xy_left; xy<=xy_right; xy+=scanwidth ) {
			currX_f+=dx_bottom_f;
			currX = (currX_f>>16);
			for( int columnCnt=prevX; columnCnt<currX; columnCnt++ ) {
				viewportY_bottom_array[columnCnt] 	= xy + columnCnt;				
			}
		}


		int viewportPos;
		if( y3 > y4 ) {
			int currX_right_f 		= (x4<<16);
			int currXY_right_f;// 	= (x4<<16);
			int dx_right_f 	= ((int) (((long) width)<<16) / (y4 - y3));
			int dxy_right_f 	= dx_right_f + scanwidth_f;

			if( y3 > scanheight ) {
				y3 = scanheight;
				if( y4 > scanheight ) {
					y4 = scanheight;
				}
			 
			}

			if( y4 < 0 ) {
				if( y3 > 0 ) {
					int diff = y4;
					y4 = 0;
					currX_right_f -= diff*dx_right_f;					
				}	else {
					y4 = 0;
					y3 = 0;
				}
			}

			viewportY_bottom	= y3; 
			currXY_right_f = y4*scanwidth_f + currX_right_f;			
			for( int row=y4; row<=y3; row++ ) {
				viewportX_right_array[row] = (currXY_right_f&0xFFFF0000);
				currXY_right_f += dxy_right_f;
			}
		}	else
		if( y3 < y4 ) {
			int currX_left_f 	= (x3<<16);
			int currXY_left_f;// 	= (x3<<16);
			int dx_left_f 		= ((int) (((long) width)<<16) / (y4 - y3));
			int dxy_left_f 	= dx_left_f + scanwidth_f;

			if( y4 > scanheight ) {
				y4 = scanheight;
				if( y3 > scanheight ) {
					y3 = scanheight;
				}
			}	

			if( y3 < 0 ) {
				if( y4 > 0 ) {
					int diff = y3;
					y3 = 0;
					currX_left_f -= diff*dx_left_f;					
				}	else {
					y3 = 0;
					y4 = 0;
				}
			}

			viewportY_bottom	= y4; 
			currXY_left_f = y3*scanwidth_f + currX_left_f;			
			for( int row=y3; row<=y4; row++ ) {
				viewportX_left_array[row] = (currXY_left_f&0xFFFF0000);
				currXY_left_f += dxy_left_f;
			}
		}	else {
			if( y4 > scanheight ) {
				y3 = scanheight;
				y4 = scanheight;
				viewportY_bottom = scanheight;
			}	else {
				viewportY_bottom = y4; 
			}
		}

		if( y1 < y2 ) {
			int currX_right_f 	= (x1<<16);
			int currXY_right_f;// 	= (x1<<16);
			int dx_right_f 	= ((int) (((long) width)<<16) / (y2 - y1));
			int dxy_right_f 	= dx_right_f + scanwidth_f;

			if( y2 > scanheight ) {
				y2 = scanheight;
				if( y1 > scanheight ) {
					y1 = scanheight;
				}
			}	

			if( y1 < 0 ) {
				int diff = y1;
				y1 = 0;
				currXY_right_f = currX_right_f - diff*dx_right_f;			
				if( y2 < 0 ) {
					y2 = 0;
				}	
			}	else {
				currXY_right_f = y1*scanwidth_f + currX_right_f;			
			}
			
			viewportY_top		= y1; 
			for( int row=y1; row<=y2; row++ ) {
				viewportX_right_array[row] = (currXY_right_f&0xFFFF0000);
				currXY_right_f += dxy_right_f;
			}


		}	else
		if( y1 > y2 ) {
			int currX_left_f 		= (x2<<16);
			int currXY_left_f;// 	= (x2<<16);
			int dx_left_f 			= ((int) (((long) width)<<16) / (y2 - y1));
			int dxy_left_f 		= dx_left_f + scanwidth_f;


			if( y1 > scanheight ) {
				y1 = scanheight;
				if( y2 > scanheight ) {
					y2 = scanheight;
				}
			 
			}

			if( y2 < 0 ) {
				int diff = y2;
				y2 = 0;
				currXY_left_f = currX_left_f - diff*dx_left_f;					
				if( y1 < 0 ) {
					y1 = 0;
				}	
			}	else {
				currXY_left_f = y2*scanwidth_f + currX_left_f;					
			}

			viewportY_top		= y2; 
			
			for( int row=y2; row<=y1; row++ ) {
				viewportX_left_array[row] = (currXY_left_f&0xFFFF0000);
				currXY_left_f += dxy_left_f;
			}

			
		}	else {
			if( y2 < 0 ) {
				y1 = 0;
				y2 = 0;
				viewportY_top = 0;
			}	else {
				viewportY_top = y2; 
			}
		}
		
		viewportPos = y1*scanwidth_f + (viewportX_left<<16);					
		for( int row=y1; row<y3; row++ ) {
			viewportX_left_array[row] = viewportPos;
			viewportPos += scanwidth_f;
		}	
		viewportPos = y2*scanwidth_f + (viewportX_right<<16);					
		for( int row=y2; row<y4; row++ ) {
			viewportX_right_array[row] = viewportPos;	
			viewportPos += scanwidth_f;
		}	
	}
	
	public void intersectClip(ClipArea intersectArea) {
		int[]	intersectArea_viewportY_top_array		= intersectArea.viewportY_top_array;
		int[]	intersectArea_viewportY_bottom_array	= intersectArea.viewportY_bottom_array;
		int[]	intersectArea_viewportX_left_array		= intersectArea.viewportX_left_array;
		int[]	intersectArea_viewportX_right_array		= intersectArea.viewportX_right_array;
		int[] 	viewportY_top_array		= this.viewportY_top_array;
		int[] 	viewportY_bottom_array	= this.viewportY_bottom_array;
		int[] 	viewportX_left_array	= this.viewportX_left_array;
		int[] 	viewportX_right_array	= this.viewportX_right_array;
	   
		if( viewportY_top < intersectArea.viewportY_top ) {
			viewportY_top = intersectArea.viewportY_top;
		}
		if( viewportY_bottom > intersectArea.viewportY_bottom ) {
			viewportY_bottom = intersectArea.viewportY_bottom;
		}
		
		if( viewportX_left < intersectArea.viewportX_left ) {
			viewportX_left = intersectArea.viewportX_left;
		}
		if( viewportX_right > intersectArea.viewportX_right ) {
			viewportX_right = intersectArea.viewportX_right;
		}
		
		for( int column=viewportX_left; column<=viewportX_right; column++ ) {
			if( viewportY_top_array[column] < intersectArea_viewportY_top_array[column] ) {
				viewportY_top_array[column] = intersectArea_viewportY_top_array[column];
			}
			if( viewportY_bottom_array[column] > intersectArea_viewportY_bottom_array[column] ) {
				viewportY_bottom_array[column] = intersectArea_viewportY_bottom_array[column];
			}
		}
		
		for( int row=viewportY_top; row<=viewportY_bottom; row++ ) {
			if( viewportX_left_array[row] < intersectArea_viewportX_left_array[row] ) {
				viewportX_left_array[row] = intersectArea_viewportX_left_array[row];
			}
			if( viewportX_right_array[row] > intersectArea_viewportX_right_array[row] ) {
				viewportX_right_array[row] = intersectArea_viewportX_right_array[row];
			}
		}
		
	}

}
