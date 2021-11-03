package graphics.engine.object;

import graphics.engine.data.Rectangle2D;
import graphics.engine.data.Rectangle3D;
import graphics.engine.renderer.Renderer;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.image.ImageProducer;

import javax.swing.ImageIcon;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class WallRectangle_i extends WallRectangle {
	private static byte[][] texture = new byte[][] { 
		{'1','0','1','0','0','1','0','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','1','1','0','1','0','1','0'},
		{'1','1','1','0','1','0','1','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','0','1','0','1','0','1','0'},
		{'1','0','1','0','0','1','0','0'}
	};

	
	public WallRectangle_i(int x1, int y1, int z1,
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
		byte[]  pixels = new byte[65];

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
		renderer.toScreenSpace(this, screenSpaceRect);
		
		Log.log(new Integer( screenSpaceRect.x1) );
		Log.log(new Integer( screenSpaceRect.y1) );
		Log.log(new Integer( screenSpaceRect.x2) );
		Log.log(new Integer( screenSpaceRect.y2) );
		Log.log(new Integer( screenSpaceRect.x3) );
		Log.log(new Integer( screenSpaceRect.y3) );
		Log.log(new Integer( screenSpaceRect.x4) );
		Log.log(new Integer( screenSpaceRect.y4) );
		
		return retVal;
	}


	public void drawNoPerspective(int x_left, int x_right, int viewportIdx, int textureColumnIdx, int averageHorTextureStepsSize, int verTextureStepsSize, int y_top_left, int y_bottom_left, int dy_top, int dy_bottom, int[] viewportY_top, int[] viewportY_bottom ) {
		boolean retVal = true;


		int y_top_local  = 0;
		int y_bottom_local  = 0;

		for( int columnCnt=x_left; columnCnt<=x_right; columnCnt++ ) {
		byte[]  pixels = new byte[65];
			int 	texture_h 			= (((textureColumnIdx>>16)+texture_offset_h)>>5)&7;
			int 	textureRowIdx 		= 0;
			textureColumnIdx   += averageHorTextureStepsSize;

			// clip top and bottom
			if( y_top_left < viewportY_top[viewportIdx] ) {
				textureRowIdx += (verTextureStepsSize * (viewportY_top[viewportIdx] - y_top_left)>>16);
				y_top_local = (viewportY_top[viewportIdx]>>16);
			}	else {
				y_top_local = y_top_left>>16;
			}
			
			if( y_bottom_left > viewportY_bottom[viewportIdx] ) {
				y_bottom_local = (viewportY_bottom[viewportIdx]>>16);
			}	else {
				y_bottom_local = y_bottom_left>>16;
			}
			
			for( int rowCnt=y_top_local; rowCnt<=y_bottom_local; rowCnt++) {
				int texture_v = ((int) (textureRowIdx>>21))&7;

				pixels[rowCnt] = texture[texture_v][texture_h];
//				g.drawLine(columnCnt, rowCnt, columnCnt, rowCnt);
				textureRowIdx+=verTextureStepsSize;
			}
			
			Log.log(pixels);

			y_top_left+=dy_top;
			y_bottom_left+=dy_bottom;			
			verTextureStepsSize   = (int) ((((long) height)<<32) / ((long) (y_bottom_left - y_top_left)));
			viewportIdx++;
		}
		
	}
	

	public void drawPerspective(int x_left, int x_right, int viewportIdx, int textureColumnIdx, int verTextureStepsSize_left, int verTextureStepsSize, int dVerTextureStepsSize_inv, int y_top_left, int y_bottom_left, int dy_top, int dy_bottom, int[] viewportY_top, int[] viewportY_bottom ) {
		boolean retVal = true;


		int y_top_local  = 0;
		int y_bottom_local  = 0;
//		int dVerTextureStepsSize_inv = (int) ((((long)1)<<32)/dVerTextureStepsSize);
		
		for( int columnCnt=x_left; columnCnt<=x_right; columnCnt++ ) {
		byte[]  pixels = new byte[65];
			textureColumnIdx 	= length * ((Math.abs(verTextureStepsSize-verTextureStepsSize_left)*dVerTextureStepsSize_inv)>>16);
			int 	texture_h 			= (((textureColumnIdx>>16)+texture_offset_h)>>5)&7;
			int 	textureRowIdx 		= 0;
			// clip top and bottom

			if( y_top_left < viewportY_top[viewportIdx] ) {
				textureRowIdx += (verTextureStepsSize * (viewportY_top[viewportIdx] - y_top_left)>>16);
				y_top_local = (viewportY_top[viewportIdx]>>16);
			}	else {
				y_top_local = y_top_left>>16;
			}
			
			if( y_bottom_left > viewportY_bottom[viewportIdx] ) {
				y_bottom_local = (viewportY_bottom[viewportIdx]>>16);
			}	else {
				y_bottom_local = y_bottom_left>>16;
			}
			
			for( int rowCnt=y_top_local; rowCnt<=y_bottom_local; rowCnt++) {
				int texture_v = ((int) (textureRowIdx>>21))&7;

				pixels[rowCnt] = texture[texture_v][texture_h];
				textureRowIdx+=verTextureStepsSize;
			}
//				g.drawBytes(pixels, columnCnt, y_top_local, y_top_local, y_bottom_local-y_top_local);
//				g.drawLine(columnCnt, (int) y_top_local, columnCnt, (int) y_bottom_local);
			Log.log(pixels);
			

			y_top_left+=dy_top;
			y_bottom_left+=dy_bottom;			
			verTextureStepsSize   = (int) ((((long) height)<<32) / ((long) (y_bottom_left - y_top_left)));
			viewportIdx++;
		}

		
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
