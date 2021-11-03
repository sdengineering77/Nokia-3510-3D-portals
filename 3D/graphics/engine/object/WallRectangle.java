package graphics.engine.object;

import graphics.engine.data.Rectangle2D;
import graphics.engine.data.Rectangle3D;
import graphics.engine.renderer.Renderer;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class WallRectangle {
	
	private static char[][] texture = new char[][] { 
		{'1','0','1','0','0','1','0','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','1','1','0','1','0','1','0'},
		{'1','1','1','0','1','0','1','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','0','1','0','0','1','0','0'}
	};
	
	public WallRectangle(int x1, int y1, int z1,
						int x2, int y2, int z2,
						int x3, int y3, int z3,
						int x4, int y4, int z4, Renderer renderer) {
	
		worldSpaceRect	= new Rectangle3D(	x1, y1, z1,
											x2, y2, z2,
											x3, y3, z3,
											x4, y4, z4);
		
		this.renderer = renderer;
		this.renderer_focuspointDepth = renderer.getFocuspoint_depth();
//System.out.println("textureColumnIdx: " + textureColumnIdx + " texture_h: " + texture_h );

	}

	public boolean draw( Graphics g, 	int	angle,
										int[] 	viewportY_top,
										int[]	viewportY_bottom,
										int	viewportX_left,
										int  	viewportX_right ) {
		boolean retVal = true;

		double verTextureStepsSize;
		double verTextureStepsSize_left;
		double verTextureStepsSize_right;
		double dy_top;
		double dy_bottom;
		double y_top_left;
		double	y_bottom_left;
		double y_top_right;
		double	y_bottom_right;
		int 	x_left;
		int	x_right;
		int	width;
		
		// to camera
		x1 = worldSpaceRect.x1;
		x2 = worldSpaceRect.x2;
		x3 = worldSpaceRect.x3;
		x4 = worldSpaceRect.x4;
		y1 = worldSpaceRect.y1;
		y2 = worldSpaceRect.y2;
		y3 = worldSpaceRect.y3;
		y4 = worldSpaceRect.y4;
		z1 = worldSpaceRect.z1;
		z2 = worldSpaceRect.z2;
		z3 = worldSpaceRect.z3;
		z4 = worldSpaceRect.z4;
		texture_offset_h = worldSpaceRect.texture_offset_h;
		length			 = worldSpaceRect.length;
		height			 = worldSpaceRect.height;
		
		clipZ();
		
		// to screen
		renderer.toScreenSpace((WallRectangle)this, screenSpaceRect);
		
		if( (screenSpaceRect.x1 > viewportX_left && screenSpaceRect.x1 < viewportX_right ) ||
			 (screenSpaceRect.x2 > viewportX_left && screenSpaceRect.x2 < viewportX_right ) ) {
			if( screenSpaceRect.x1 > screenSpaceRect.x2 ) {
				dy_top			= (double) (screenSpaceRect.y2 - screenSpaceRect.y1) / (double) (screenSpaceRect.x2 - screenSpaceRect.x1);
				dy_bottom		= (double) (screenSpaceRect.y4 - screenSpaceRect.y3) / (double) (screenSpaceRect.x4 - screenSpaceRect.x3);
				x_left 			= screenSpaceRect.x2;
				x_right 		= screenSpaceRect.x1;
				y_top_left 		= screenSpaceRect.y2;
				y_bottom_left 	= screenSpaceRect.y4;
				y_top_right		= screenSpaceRect.y1;
				y_bottom_right 	= screenSpaceRect.y3;
				verTextureStepsSize_left   = (double) height / (double) (y_bottom_right - y_top_right);
				verTextureStepsSize_right  = (double) height / (double) (y_bottom_left - y_top_left);
		
				verTextureStepsSize = verTextureStepsSize_right;
			}	else {
				dy_top			= (double) (screenSpaceRect.y2 - screenSpaceRect.y1) / (double) (screenSpaceRect.x2 - screenSpaceRect.x1);
				dy_bottom		= (double) (screenSpaceRect.y4 - screenSpaceRect.y3) / (double) (screenSpaceRect.x4 - screenSpaceRect.x3);
				x_left 			= screenSpaceRect.x1;
				x_right 		= screenSpaceRect.x2;
				y_top_left 		= screenSpaceRect.y1;
				y_bottom_left 	= screenSpaceRect.y3;
				y_top_right		= screenSpaceRect.y2;
				y_bottom_right 	= screenSpaceRect.y4;
				verTextureStepsSize_left   = (double) height / (double) (y_bottom_left - y_top_left);
				verTextureStepsSize_right  = (double) height / (double) (y_bottom_right - y_top_right);
		
				verTextureStepsSize = verTextureStepsSize_left;
	
			}

//System.out.println("wr dy_top: " + (double) dy_top);
//System.out.println("wr dy_bottom: " + (double) dy_bottom);
	
			
	//		width					   = (x2-x1);
			double averageHorTextureStepsSize 	= 0;
			double textureColumnIdx 			= 0;
			double dVerTextureStepsSize_inv 	= 0;
	
			double	dVerTextureStepsSize = Math.abs(verTextureStepsSize_right-verTextureStepsSize_left);
			
			if ( dVerTextureStepsSize != 0 ) {
				dVerTextureStepsSize_inv = 1d/Math.abs(dVerTextureStepsSize);

				// clip left
				if( x_left < viewportX_left ) {
					textureColumnIdx = averageHorTextureStepsSize * (viewportX_left - x_left);
					int diff = viewportX_left - x_left;
					x_left = viewportX_left;

					y_top_left		    += dy_top * diff;
					y_bottom_left		+= dy_bottom * diff;			
					verTextureStepsSize  = (double) height / (double) (y_bottom_left - y_top_left);

				}

			}	else {
				averageHorTextureStepsSize = (double) length / (double) (x_right-x_left);

				// clip left
				if( x_left < viewportX_left ) {
					textureColumnIdx = averageHorTextureStepsSize * (viewportX_left - x_left);
					x_left = viewportX_left;
				}
				
			}
			
			// clip right
			if( x_right > viewportX_right ) {
				x_right = viewportX_right;
			}
			
			int viewportIdx = x_left - viewportX_left;
			for( int columnCnt=x_left; columnCnt<=x_right; columnCnt++ ) {
				if ( dVerTextureStepsSize != 0 ) {
					textureColumnIdx 	= ((double) length * Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv);
				}	else {
					textureColumnIdx   += averageHorTextureStepsSize;
				}
				int 	texture_h 			= ((int) ((textureColumnIdx+texture_offset_h)/24d))%8;
				double textureRowIdx 		= 0;
	//System.out.println("textureColumnIdx: " + textureColumnIdx + " texture_h: " + texture_h );
				
				int y_top_local  = 0;
				int y_bottom_local  = 0;

//System.out.println("viewportIdx: " + viewportIdx);	
					
				// clip top and bottom
//System.out.println("y_top_left: " + y_top_left + " viewportY_top[viewportIdx]: " + viewportY_top[viewportIdx] );
				if( y_top_left < viewportY_top[viewportIdx] ) {
					textureRowIdx += verTextureStepsSize * (viewportY_top[viewportIdx] - y_top_left);
					y_top_local = viewportY_top[viewportIdx];
				}	else {
					y_top_local = (int) y_top_left;
				}
				
//System.out.println("y_bottom_left: " + y_bottom_left + " viewportY_bottom[viewportIdx]: " + viewportY_bottom[viewportIdx] );
				if( y_bottom_left > viewportY_bottom[viewportIdx] ) {
					y_bottom_local = viewportY_bottom[viewportIdx];
				}	else {
					y_bottom_local = (int) y_bottom_left;
				}
				
				for( int rowCnt=(int) y_top_local; rowCnt<=(int) y_bottom_local; rowCnt++) {
					int texture_v = ((int) (textureRowIdx/24))%8;
	
	//System.out.println("textureRowIdx: " + textureRowIdx + " texture_v: " + texture_v );
					
					char texture_pixel = texture[texture_v][texture_h];
					switch(texture_pixel) {
						case '0':
						g.setColor(Color.black);
						break;
						case '1':
						g.setColor(Color.green);
						break;
					}
					g.drawLine(columnCnt, rowCnt, columnCnt, rowCnt);
					textureRowIdx+=verTextureStepsSize;
				}
	//			g.drawLine(columnCnt, (int) y_top_left, columnCnt, (int) y_bottom_left);
				
				y_top_left+=dy_top;
				y_bottom_left+=dy_bottom;			
				verTextureStepsSize   = (double) height / (double) (y_bottom_left - y_top_left);
				viewportIdx++;
			}
		}	 
		
		return retVal;
	}
	
	private boolean clipZ() {
//System.out.println("CLIP----" );
		
		if( z1 >= renderer_focuspointDepth && z2 >= renderer_focuspointDepth ) {
//System.out.println("z1>z2>" );
			return true;
		}	else
		if( z1 < renderer_focuspointDepth ) {
//System.out.println("z1<" );
//System.out.println("length=" + length );
//System.out.println("texture_offset_h=" + texture_offset_h );
			int x	= (x1*(renderer_focuspointDepth-z2) + x2*(z1-renderer_focuspointDepth))/(z1-z2);
			double lengthFactor = (double) (z2-renderer_focuspointDepth)/(double) (z2-z1); 
//			length 	= (int) ((double)length * (1d-lengthFactor));
			texture_offset_h += length * (1d-lengthFactor);
			length 	= (int) ((double)length * lengthFactor);
			z1 = renderer_focuspointDepth;
			z3 = renderer_focuspointDepth;
			x1 = x;
			x2 = x;
//System.out.println("z1<" );
//System.out.println("length=" + length );
//System.out.println("texture_offset_h=" + texture_offset_h );

			return true;
		}	else
		if( z2 < renderer_focuspointDepth ) {
//System.out.println("length=" + length );
//System.out.println("texture_offset_h=" + texture_offset_h );
			int x	= (x1*(renderer_focuspointDepth-z2) + x2*(z1-renderer_focuspointDepth))/(z1-z2);
			length 	= length * (z1-renderer_focuspointDepth)/(z1-z2);
			z2 = renderer_focuspointDepth;
			z4 = renderer_focuspointDepth;
			x2 = x;
			x4 = x;
//System.out.println("z2<" );
//System.out.println("length=" + length );
//System.out.println("texture_offset_h=" + texture_offset_h );
			return true;
		}	else {
			return false;
		}
	}


	public int x1, x2, x3, x4;
	public int y1, y2, y3, y4;
	public int z1, z2, z3, z4;
	public int length;	
	public int height;
	public int renderer_focuspointDepth	= 48;
	public int texture_offset_h 			= 0;
	public Renderer 		renderer		= null;
	public Rectangle2D 	screenSpaceRect = new Rectangle2D();
	public Rectangle3D 	worldSpaceRect 	= null;
}
