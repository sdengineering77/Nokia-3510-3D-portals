package game.io;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;

import game.engine.data.RectangularPortal;
import graphics.engine.object.CeilQuad;
import graphics.engine.object.Drawable;
import graphics.engine.object.FloorCeilQuadCombo;
import graphics.engine.object.FloorQuad;
import graphics.engine.object.ViewportWallQuad;
import graphics.engine.object.WallQuad;
import graphics.engine.renderer.Renderer;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class MapLoader {
	public final static int SPAN_DATABLOCKSIZE	= 7;
	public final static int SPAN_LEFT				= 0;
	public final static int SPAN_RIGHT				= 1;
	public final static int SPAN_TOP					= 2;
	public final static int SPAN_BOTTOM				= 3;
	public final static int SPAN_ALIGN				= 4;
	public final static 	int _SPAN_ALIGN_LEFT			= 0;
	public final static 	int _SPAN_ALIGN_RIGHT			= 1;
	public final static 	int _SPAN_ALIGN_TOP			= 2;
	public final static 	int _SPAN_ALIGN_BOTTOM			= 3;
	public final static int SPAN_TEXTURE				= 5;
	public final static int SPAN_ANIMATED			= 6;
	
	public final static int CONN_PORT_DATABLOCKSIZE	= 6;
	public final static int CONN_PORT_LEFT			= 0;
	public final static int CONN_PORT_RIGHT			= 1;
	public final static int CONN_PORT_TOP			= 2;
	public final static int CONN_PORT_BOTTOM			= 3;
	public final static int CONN_PORT_ALIGN			= 4;
	public final static 	int _CONN_PORT_ALIGN_LEFT		= 0;
	public final static 	int _CONN_PORT_ALIGN_RIGHT		= 1;
	public final static 	int _CONN_PORT_ALIGN_TOP		= 2;
	public final static 	int _CONN_PORT_ALIGN_BOTTOM	= 3;
	public final static int CONN_PORT_REL_PORTAL_ID	= 5;
	

	public final static int PORTAL_DATABLOCKSIZE		= 16;
	public final static int PORTAL_BOTTOM_H 			= 0;
	public final static int PORTAL_BOTTOM_L 			= 1;
	public final static int PORTAL_LEFT_H  			= 2;
	public final static int PORTAL_LEFT_L  			= 3;
	public final static int PORTAL_FLOOR_H 			= 4;
	public final static int PORTAL_FLOOR_L 			= 5;
	public final static int PORTAL_CEIL_HEIGHT 		= 6;
	public final static int PORTAL_WIDTH		 		= 7;
	public final static int PORTAL_HEIGHT	 		= 8;
	public final static int PORTAL_TEXT_FLOOR		= 9;
	public final static int PORTAL_TEXT_CEIL	    	= 10;
	public final static int PORTAL_LIGHT	    		= 11;
	public final static int PORTAL_LIGHT_TYPE   		= 12;
	public final static int PORTAL_ID	    		= 13;
	public final static int PORTAL_NUM_REF_PORTALS	= 14;
	public final static int PORTAL_NUM_REF_SPANS		= 15;

	public final static byte[] portal_data 	= new byte[PORTAL_DATABLOCKSIZE];
	public final static byte[] span_data 	= new byte[SPAN_DATABLOCKSIZE];

	public RectangularPortal loadMap( Renderer r, String name ) {
		RectangularPortal first = null;
		Hashtable	portalHash 		= new Hashtable();
		Hashtable 	connPortHash 	= new Hashtable();
		try { 
			InputStream in = this.getClass().getResourceAsStream(name);
			
			if( in != null ) {
				int num_portals;
				num_portals  = (in.read()<<8);
				num_portals |= (in.read());

//System.out.println( "num_portals:" + num_portals );
//System.out.println( "mem:" + Runtime.getRuntime().freeMemory() );
//System.gc();
//System.out.println( "mem:" + Runtime.getRuntime().freeMemory() );
				
				if( num_portals > 0 ) {
					for( int portalCnt=num_portals; portalCnt>0; portalCnt-- ) {
						loadPortal(r, connPortHash, portalHash, in);
					}
					
//System.out.println( "mem:" + Runtime.getRuntime().freeMemory() );
					in.close();
					in = null;
					System.gc();
//System.out.println( "mem:" + Runtime.getRuntime().freeMemory() );
//System.out.println( "SOLVE:");
					// solve connections
					solveConnectionPorts(r, connPortHash, portalHash);
				}				
			}	else {
				//System.out.println( "FILE NOT FOUND" );
			}
		}	catch( Exception e ) {
//System.out.println(e.getMessage()); 	
			e.printStackTrace();	
		}	finally {
			first = (RectangularPortal) portalHash.get(new Integer(1));
			
			connPortHash.clear();
			connPortHash = null;
			
			portalHash.clear();
			portalHash = null;
			
			System.gc();
		}
		return first;
	}
	
	private void solveConnectionPorts( Renderer r, Hashtable connPortHash, Hashtable portalHash ) {
		
//System.out.println( "portalHash.size(): " + portalHash.size());	
//System.gc();	
//System.out.println( "mem:" + Runtime.getRuntime().freeMemory() );
		Enumeration portalIDs = portalHash.keys();
		while(portalIDs.hasMoreElements()) {
			Integer id = (Integer) portalIDs.nextElement();

//System.out.println( "Integer id: " + id.intValue());		
			
			byte[] connPortData = (byte[]) connPortHash.get(id);
//System.out.println( "connPortData: " + connPortData);		

			RectangularPortal portal = (RectangularPortal) portalHash.get(id);
//System.out.println( "portal: " + portal);		
			
			int numConnPorts	= connPortData[connPortData.length-1];
//			int numPolies 	  	= portal.polygons.length;
			int align, left, right, top, bottom;

//System.out.println( "numConnPorts: " + numConnPorts);		
////System.out.println( "numPolies: " + numPolies);		
			
			for(int cpIdx=((numConnPorts-1)*CONN_PORT_DATABLOCKSIZE); cpIdx>=0; cpIdx-=CONN_PORT_DATABLOCKSIZE ) {
//System.out.println( "cpIdx: " + cpIdx);		
				align 	= (connPortData[cpIdx+CONN_PORT_ALIGN]);
				left  	= (((int)connPortData[cpIdx+CONN_PORT_LEFT])<<5);
				right 	= (((int)connPortData[cpIdx+CONN_PORT_RIGHT])<<5);
				
				top	  	= (((int)connPortData[cpIdx+CONN_PORT_TOP])<<5);
				bottom	= (((int)connPortData[cpIdx+CONN_PORT_BOTTOM])<<5);
//System.out.println( "trace0: ");		

				Integer ref_id = new Integer(connPortData[cpIdx+CONN_PORT_REL_PORTAL_ID]);
				RectangularPortal refPortal = (RectangularPortal) portalHash.get(ref_id);
//System.out.println( "refPortal: " + refPortal);		
//System.out.println( "ref_id: " + ref_id.intValue());		
				
//System.out.println( "mem:" + Runtime.getRuntime().freeMemory() );
				connPortHash.remove(id);
				System.gc();
//System.out.println( "mem:" + Runtime.getRuntime().freeMemory() );

				ViewportWallQuad quad = null;

//System.out.println( "trace1: ");		

				if( align == _CONN_PORT_ALIGN_LEFT ) {
					int x_bound_left = portal.x_bound_left-1;
					quad = new ViewportWallQuad(
									x_bound_left, top, portal.z_bound_bottom + left,
									x_bound_left, top, portal.z_bound_bottom + right,								 	
									x_bound_left, bottom, portal.z_bound_bottom + left,
									x_bound_left, bottom, portal.z_bound_bottom + right, r
										);
				}	else
				if( align == _CONN_PORT_ALIGN_RIGHT ) {
					int x_bound_right = portal.x_bound_right+1;
					quad = new ViewportWallQuad(
									x_bound_right, top, portal.z_bound_top - left,
									x_bound_right, top, portal.z_bound_top - right,								 	
									x_bound_right, bottom, portal.z_bound_top - left,
									x_bound_right, bottom, portal.z_bound_top - right, r
										);
				}	else
				if( align == _CONN_PORT_ALIGN_TOP ) {
					int z_bound_top = portal.z_bound_top + 1;
					quad = new ViewportWallQuad(
									portal.x_bound_left + left, top, z_bound_top,
									portal.x_bound_left + right, top, z_bound_top,								 	
									portal.x_bound_left + left, bottom, z_bound_top,
									portal.x_bound_left + right, bottom, z_bound_top, r
										);
				}	else
				if( align == _CONN_PORT_ALIGN_BOTTOM ) {
					int z_bound_bottom = portal.z_bound_bottom - 1;
					quad = new ViewportWallQuad(
									portal.x_bound_right - left, top, z_bound_bottom,
									portal.x_bound_right - right, top, z_bound_bottom,								 	
									portal.x_bound_right - left, bottom, z_bound_bottom,
									portal.x_bound_right - right, bottom, z_bound_bottom, r
										);
				}

//System.out.println( "trace2: ");		
				
				quad.childObjects = refPortal.polygons;
				// TODO IMPLEMENT IS_VIRTUAL
//System.out.println( "trace3: ");		

//System.out.println( "mem1:" + Runtime.getRuntime().freeMemory() );

				portal.addRectangularRoom2(refPortal, quad, true);				
//System.out.println( "mem2:" + Runtime.getRuntime().freeMemory() );

								
			}
			
			// adapt new room constraints!
			portal.x_bound_left+=32;
			portal.x_bound_right-=32;
			portal.z_bound_top-=32;
			portal.z_bound_bottom+=32;
			
		}
		
	
	}
	
	public short convertPair( byte byte_h, byte byte_l ) {
		short value = 0;
		value   |= (((short)byte_h)<<8);
		value	|= byte_l < 0 ? (short) 0x0100 + (short) byte_l : byte_l; 
		return value;

	}
	 	
	private void loadPortal( Renderer r, Hashtable connPortHash, Hashtable portalHash, InputStream in ) throws Exception {
		byte[] data = portal_data;

		in.read(data, 0, PORTAL_DATABLOCKSIZE);
		
//		short bottom_h = 0,bottom_l = 0, left_h = 0, left_l = 0, floor_h = 0, floor_l = 0;
		int bottom, left, floor, ceil_height, width, height, light, 
			light_type, id, text_floor, text_ceil, num_ref_portals, num_ref_spans;

		// read top-left-floor
		bottom = (convertPair(data[PORTAL_BOTTOM_H], data[PORTAL_BOTTOM_L])<<5);
		left   = (convertPair(data[PORTAL_LEFT_H], data[PORTAL_LEFT_L])<<5);
		floor  = (convertPair(data[PORTAL_FLOOR_H], data[PORTAL_FLOOR_L])<<5);
//		bottom_h	|= (((short)data[PORTAL_BOTTOM_H])<<8);
//		bottom_l	|= data[PORTAL_BOTTOM_L] < 0 ? (short) 0x0100 + (short) data[PORTAL_BOTTOM_L] : data[PORTAL_BOTTOM_L];
//		left_h		|= (((short)data[PORTAL_LEFT_H])<<8);
//		left_l		|= data[PORTAL_LEFT_L] < 0 ? (short) 0x0100 + (short) data[PORTAL_LEFT_L] : data[PORTAL_LEFT_L];
//		floor_h		|= (((short)data[PORTAL_FLOOR_H])<<8);
//		floor_l		|= data[PORTAL_FLOOR_L] < 0 ? (short) 0x0100 + (short) data[PORTAL_FLOOR_L] : data[PORTAL_FLOOR_L];
		
//		bottom = ((bottom_h|bottom_l)<<5);
//		left   = ((left_h|left_l)<<5);
//		floor  = ((floor_h|floor_l)<<5);
		
		ceil_height  = (((int)data[PORTAL_CEIL_HEIGHT])<<5);
		width  		 = (((int)data[PORTAL_WIDTH])<<5);
		height       = (((int)data[PORTAL_HEIGHT])<<5);
		
		light		 = (((int)data[PORTAL_LIGHT]));
		light_type   = (((int)data[PORTAL_LIGHT_TYPE]));
		
		id			 = (((int)data[PORTAL_ID]));
	
		text_floor   = (((int)data[PORTAL_TEXT_FLOOR]));
		text_ceil    = (((int)data[PORTAL_TEXT_CEIL]));
		num_ref_portals = (((int)data[PORTAL_NUM_REF_PORTALS]));
		num_ref_spans   = (((int)data[PORTAL_NUM_REF_SPANS]));

//System.out.println("=====================" + ((int)(((char)data[PORTAL_LEFT_L])<<5)));
//System.out.println("id: " + id);
//System.out.println("left: " + left);
//System.out.println("bottom: " + bottom);
//System.out.println("bottom_h: " + Integer.toHexString(bottom_h));
//System.out.println("bottom_l: " + Integer.toHexString(bottom_l));
//System.out.println("floor: " + floor);
//System.out.println("ceil_height: " + ceil_height);
//System.out.println("width: " + width);
//System.out.println("height: " + height);
//		for( int i=0; i<6; i++ ) {
//			System.out.println("data["+i+"]:" + Integer.toHexString(data[i]));
//		}

	
//System.out.println( "bef portal:" + Runtime.getRuntime().freeMemory() );
		RectangularPortal portal = new RectangularPortal(
											left,
											left+width,
											floor+ceil_height,
											floor,
											bottom+height,
											bottom);
//System.out.println( "after portal:" + Runtime.getRuntime().freeMemory() );
											
		// load spans
//System.out.println("loading spans: " + num_ref_spans);
	Drawable[]	quads = new Drawable[num_ref_spans+num_ref_portals + 2]; // last 2 for floor and ceil
//		Drawable[]	quads = new Drawable[num_ref_spans+num_ref_portals + 1]; // last 2 for floor and ceil
		for( int spanCnt=(num_ref_spans-1); spanCnt>=0; spanCnt-- ) {
			quads[spanCnt] = loadSpan(r, in, portal);
		}
//System.out.println( "after quads:" + Runtime.getRuntime().freeMemory() );

		quads[num_ref_spans] = new FloorQuad(	left, 		floor,  bottom+height,
					          	 				left+width, floor,  bottom+height,
								 				left+width, floor,  bottom,
					 			 				left, 		floor,  bottom, r );
		quads[num_ref_spans+1] = new CeilQuad(	left, 		floor+ceil_height,  bottom+height,
					          	 				left+width, floor+ceil_height,  bottom+height,
								 				left+width, floor+ceil_height,  bottom,
					 			 				left, 		floor+ceil_height,  bottom, r );

//		quads[num_ref_spans] = new FloorCeilQuadCombo(	left, 		bottom+height,
//					          	 						left+width, bottom+height,
//								 						left+width, bottom,
//					 			 						left, 		bottom, floor, floor+ceil_height, r );

//System.out.println( "after floor/ceil quads:" + Runtime.getRuntime().freeMemory() );
		
		portal.polygons = quads;
		Integer id_int = new Integer(id);		
		portalHash.put(id_int, portal);

//System.out.println("loading conportData: " + num_ref_portals);

		// cache conn port data
		int    numConnPortBytes = (CONN_PORT_DATABLOCKSIZE*num_ref_portals)+1;
		byte[] cpData = new byte[numConnPortBytes];
		in.read(cpData, 0, (numConnPortBytes-1));
		cpData[numConnPortBytes-1] = (byte) num_ref_portals;
		connPortHash.put(id_int, cpData);
//System.out.println( "after connPortBArray:" + Runtime.getRuntime().freeMemory() );

	}

	private WallQuad loadSpan( Renderer r, InputStream in, RectangularPortal portal ) throws Exception {
		byte[]	data = span_data;

		int align, left, right, top, bottom, text, anim;
		in.read(data, 0, SPAN_DATABLOCKSIZE);

		align   = data[SPAN_ALIGN];
		left    = (((int)data[SPAN_LEFT])<<5);
		right   = (((int)data[SPAN_RIGHT])<<5);
		
		top     = (((int)data[SPAN_TOP])<<5);
		bottom  = (((int)data[SPAN_BOTTOM])<<5);
		text    = (((int)data[SPAN_TEXTURE])<<5);
		anim    = (((int)data[SPAN_ANIMATED])<<5);
		
		WallQuad quad = null;
		
		if( align == _SPAN_ALIGN_LEFT ) {
			quad = new WallQuad(
							portal.x_bound_left, top, portal.z_bound_bottom + left,
							portal.x_bound_left, top, portal.z_bound_bottom + right,								 	
							portal.x_bound_left, bottom, portal.z_bound_bottom + left,
							portal.x_bound_left, bottom, portal.z_bound_bottom + right, r
								);
		}	else
		if( align == _SPAN_ALIGN_RIGHT ) {
			quad = new WallQuad(
							portal.x_bound_right, top, portal.z_bound_top - left,
							portal.x_bound_right, top, portal.z_bound_top - right,								 	
							portal.x_bound_right, bottom, portal.z_bound_top - left,
							portal.x_bound_right, bottom, portal.z_bound_top - right, r
								);
		}	else
		if( align == _SPAN_ALIGN_TOP ) {
			quad = new WallQuad(
							portal.x_bound_left + left, top, portal.z_bound_top,
							portal.x_bound_left + right, top, portal.z_bound_top,								 	
							portal.x_bound_left + left, bottom, portal.z_bound_top,
							portal.x_bound_left + right, bottom, portal.z_bound_top, r
								);
		}	else
		if( align == _SPAN_ALIGN_BOTTOM ) {
			quad = new WallQuad(
							portal.x_bound_right - left, top, portal.z_bound_bottom,
							portal.x_bound_right - right, top, portal.z_bound_bottom,								 	
							portal.x_bound_right - left, bottom, portal.z_bound_bottom,
							portal.x_bound_right - right, bottom, portal.z_bound_bottom, r
								);
		}
		
		return quad;
	}

	

}




