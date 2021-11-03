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
public class WallRectangle_i_nodiv extends WallRectangle {
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

	
	public WallRectangle_i_nodiv(int x1, int y1, int z1,
						int x2, int y2, int z2,
						int x3, int y3, int z3,
						int x4, int y4, int z4, Renderer renderer) {
	
		super(	x1, y1, z1,
				x2, y2, z2,
				x3, y3, z3,
				x4, y4, z4, renderer);
		

	}

	public boolean draw( Graphics g, 	int	angle,
										int[] 	viewportY_top,
										int[]	viewportY_bottom,
										int	viewportX_left,
										int  	viewportX_right ) {
		boolean retVal = true;

//		super.draw(g, viewportY_top,
//										viewportY_bottom,
//										viewportX_left,
//										viewportX_right);
		int	verTextureStepsSize;
		int	verTextureStepsSize_left;
		int	verTextureStepsSize_right;
		int 	dy_top;
		int 	dy_bottom;
		int 	y_top_left;
		int	y_bottom_left;
		int 	y_top_right;
		int	y_bottom_right;
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
				dy_top			= (int) ((((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16) / (long) (screenSpaceRect.x2 - screenSpaceRect.x1));
				dy_bottom		= (int) ((((long) (screenSpaceRect.y4 - screenSpaceRect.y3))<<16) / (long) (screenSpaceRect.x4 - screenSpaceRect.x3));
/*System.out.println("((long) (screenSpaceRect.x2 - screenSpaceRect.x1)): " + ((long) (screenSpaceRect.x2 - screenSpaceRect.x1)));
System.out.println("(((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16 / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1))): " + ((((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16) / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1))));
System.out.println("((double) (((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16 / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1)))): " + ((double) ((((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16) / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1)))) / (1<<16));  

System.out.println("((long) (screenSpaceRect.y2 - screenSpaceRect.y1)): " + ((long) (screenSpaceRect.y2 - screenSpaceRect.y1)));
System.out.println("((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16: " + (((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16));
System.out.println("(double) ((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16: " + ((double) (((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16)) / (1<<16));  
*/
				x_left 			= screenSpaceRect.x2;
				x_right 		= screenSpaceRect.x1;
				y_top_left 		= screenSpaceRect.y2<<16;
				y_bottom_left 	= screenSpaceRect.y4<<16;
				y_top_right		= screenSpaceRect.y1<<16;
				y_bottom_right 	= screenSpaceRect.y3<<16;
				verTextureStepsSize_left   = (int) ((((long) height)<<32) / (long) (y_bottom_right - y_top_right));
				verTextureStepsSize_right  = (int) ((((long) height)<<32) / (long) (y_bottom_left - y_top_left));
		
				verTextureStepsSize = verTextureStepsSize_right;
			}	else {
				dy_top			= (int) ((((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16) / (long) (screenSpaceRect.x2 - screenSpaceRect.x1));
				dy_bottom		= (int) ((((long) (screenSpaceRect.y4 - screenSpaceRect.y3))<<16) / (long) (screenSpaceRect.x4 - screenSpaceRect.x3));
/*System.out.println("((long) (screenSpaceRect.x2 - screenSpaceRect.x1)): " + ((long) (screenSpaceRect.x2 - screenSpaceRect.x1)));
System.out.println("(((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16 / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1))): " + ((((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16) / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1))));
System.out.println("((double) (((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16 / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1)))): " + ((double) ((((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16) / ((long) (screenSpaceRect.x2 - screenSpaceRect.x1)))) / (1<<16));  

System.out.println("((long) (screenSpaceRect.y2 - screenSpaceRect.y1)): " + ((long) (screenSpaceRect.y2 - screenSpaceRect.y1)));
System.out.println("((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16: " + (((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16));
System.out.println("(double) ((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16: " + ((double) (((long) (screenSpaceRect.y2 - screenSpaceRect.y1))<<16)) / (1<<16));  
*/
				x_left 			= screenSpaceRect.x1;
				x_right 		= screenSpaceRect.x2;
				y_top_left 		= screenSpaceRect.y1<<16;
				y_bottom_left 	= screenSpaceRect.y3<<16;
				y_top_right		= screenSpaceRect.y2<<16;
				y_bottom_right 	= screenSpaceRect.y4<<16;
				verTextureStepsSize_left   = (int) ((((long) height)<<32) / (long) (y_bottom_left - y_top_left));
				verTextureStepsSize_right  = (int) ((((long) height)<<32) / (long) (y_bottom_right - y_top_right));
		
				verTextureStepsSize = verTextureStepsSize_left;
	
			}
	
//System.out.println("dy_top: " + ((double) dy_top)/(1<<16));
//System.out.println("dy_bottom: " + ((double) dy_bottom)/(1<<16));
			int perspective_length = (int)((((long)(verTextureStepsSize_left + verTextureStepsSize_right)))/(2 * length)) * (x_right - x_left);
			long perspective_length_inv = ((((long)1)<<32)/perspective_length);
			int d_perspective      = (int) (((long)((verTextureStepsSize_right - verTextureStepsSize_left)/(x_right - x_left) * perspective_length_inv))>>16);
			long perspective_pos    = 0;
			int perspective_stepsize= (int) (((long)(verTextureStepsSize * perspective_length_inv))>>16);
/*System.out.println("perspective_length: " + ((double) perspective_length)/(1<<16));
System.out.println("perspective_length_inv: " + ((double) perspective_length_inv)/(1<<16));
System.out.println("d_perspective: " + ((double) d_perspective)/(1<<16));
System.out.println("perspective_stepsize: " + ((double) perspective_stepsize)/(1<<16));
*/						
	//		width					   = (x2-x1);
			int averageHorTextureStepsSize	= 0;
			int textureColumnIdx 			= 0;
			int dVerTextureStepsSize_inv 	= 0;
	
			int dVerTextureStepsSize = Math.abs(verTextureStepsSize_right-verTextureStepsSize_left);
//System.out.println("dVerTextureStepsSize: " + ((double) dVerTextureStepsSize)/(1<<16));

			
			if ( dVerTextureStepsSize != 0 ) {
				dVerTextureStepsSize_inv = (int) ((((long)1)<<32)/dVerTextureStepsSize);
//System.out.println("dVerTextureStepsSize_inv: " + ((double) dVerTextureStepsSize_inv)/(1<<16));

				// clip left
				if( x_left < viewportX_left ) {
					int diff = viewportX_left - x_left;

//					textureColumnIdx = averageHorTextureStepsSize * diff;

					y_top_left		    += dy_top * diff;
					y_bottom_left		+= dy_bottom * diff;			
//					y_top_left		    += ((double) dy_top)/(1<<16) * diff;
//					y_bottom_left		+= ((double) dy_bottom)/(1<<16) * diff;			
					verTextureStepsSize  = (int) ((((long) height)<<32) / (long) (y_bottom_left - y_top_left));
					perspective_pos 	 = (x_right-x_left) * ((Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv)>>16);
					perspective_stepsize =  (int) (((long)(verTextureStepsSize * perspective_length_inv))>>16);

					x_left = viewportX_left;

//System.out.println("verTextureStepsSize: " + ((double) verTextureStepsSize)/(1<<16));

				}

			}	else {
				averageHorTextureStepsSize	= (int) ((((long) length)<<16) / (x_right-x_left));
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
			int y_top_local  = 0;
			int y_bottom_local  = 0;
//			int y_top_left_i = (int) (y_top_left * (1<<16));
//			int y_bottom_left_i = (int) (y_bottom_left * (1<<16));

			for( int columnCnt=x_left; columnCnt<=x_right; columnCnt++ ) {
				if ( dVerTextureStepsSize != 0 ) {
//System.out.println("((Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv)>>16): " + ((Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv)>>16) );
//System.out.println("(double) ((Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv)>>16): " + ((double)((Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv)>>16))/(1<<16) );

//					textureColumnIdx = (int) (((long)(perspective_pos * perspective_length_inv))>>16);
					textureColumnIdx = (int) perspective_pos;
					perspective_pos += perspective_stepsize;
					perspective_stepsize +=d_perspective;
//					textureColumnIdx 	= length * ((Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv)>>16);
/*System.out.println("perspective_pos: " + ((double) perspective_pos)/(1<<16));
System.out.println("perspective_stepsize: " + ((double) perspective_stepsize)/(1<<16));
System.out.println("(((long)(perspective_pos * perspective_length_inv))>>16): " + ((double) (((long)(perspective_pos * perspective_length_inv))>>16))/(1<<16));
*/
				}	else {
					textureColumnIdx   += averageHorTextureStepsSize;
				}
				int 	texture_h 			= (((textureColumnIdx>>16)+texture_offset_h)>>5)&7;
				int 	textureRowIdx 		= 0;
/*System.out.println("texture_offset_h: " + texture_offset_h );
System.out.println("textureColumnIdx>>16: " + (textureColumnIdx>>16) + " (textureColumnIdx)d: " + ((double)(textureColumnIdx))/(1<<16) );
System.out.println("((textureColumnIdx>>16+texture_offset_h)>>5): " + (((textureColumnIdx>>16)+texture_offset_h)) + " ((textureColumnIdx>>16+texture_offset_h)>>5)d: " + ((double)((textureColumnIdx>>16+texture_offset_h)>>5))/(1<<16) );
System.out.println("textureColumnIdx: " + textureColumnIdx + " texture_h: " + texture_h );
*/				
//System.out.println("viewportIdx: " + viewportIdx);	
					
				// clip top and bottom
//System.out.println("y_top_left: " + y_top_left + " viewportY_top[viewportIdx]: " + viewportY_top[viewportIdx] );
				if( y_top_left < viewportY_top[viewportIdx]<<16 ) {
					textureRowIdx += verTextureStepsSize * (viewportY_top[viewportIdx] - y_top_left>>16);
					y_top_local = viewportY_top[viewportIdx];
				}	else {
					y_top_local = (int) y_top_left>>16;
				}
				
//System.out.println("y_bottom_left: " + y_bottom_left + " viewportY_bottom[viewportIdx]: " + viewportY_bottom[viewportIdx] );
				if( y_bottom_left > viewportY_bottom[viewportIdx]<<16 ) {
					y_bottom_local = viewportY_bottom[viewportIdx];
				}	else {
					y_bottom_local = (int) y_bottom_left>>16;
				}
				
				for( int rowCnt=y_top_local; rowCnt<=y_bottom_local; rowCnt++) {
					int texture_v = ((int) (textureRowIdx>>21))&7;
	
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
//					textureRowIdx+=verTextureStepsSize;

					textureRowIdx+=perspective_stepsize;
				}
				
//				g.drawLine(columnCnt, (int) y_top_local, columnCnt, (int) y_bottom_local);
				
				y_top_left+=dy_top;
				y_bottom_left+=dy_bottom;			
//				verTextureStepsSize   = perspective_stepsize;//(int) ((((long) height)<<32) / ((long) (y_bottom_left - y_top_left)));
//System.out.println("verTextureStepsSize: " + ((double)(verTextureStepsSize))/(1<<16) );

				viewportIdx++;
			}
		}	 
		
		return retVal;
	}
	
/*	private int divide( int a, int b ) throws RuntimeException {
		if( b != 0 ) {
			if( a < b ) {
				return	a%(b)
		}
		throw new RuntimeException( "Divide By Zero!" );
	}
*/	
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
			int  x				= (x1*(renderer_focuspointDepth-z2) + x2*(z1-renderer_focuspointDepth))/(z1-z2);
			long lengthFactor 	= (((long) (z2-renderer_focuspointDepth))<<16)/(z2-z1); 
//			length 	= (int) ((double)length * (1d-lengthFactor));
			texture_offset_h += ((length * ((1<<16)-lengthFactor))>>16);
			length 	= (int) ((length * lengthFactor)>>16);
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


}
