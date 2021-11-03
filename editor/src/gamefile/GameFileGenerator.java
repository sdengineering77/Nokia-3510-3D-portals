package gamefile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Category;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import portalconnector.data.ConnectionPort;
import portaleditor.data.PortalObject;
import portaleditor.data.XmlAble;
import texturing.data.Span;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class GameFileGenerator {
	private final static Category log = Category.getInstance(GameFileGenerator.class);
	private ArrayList  portalList		= new ArrayList();
	
	public void load() {
		try {
			File file = new File("portals_textured.xml");
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document document = builder.parse(file);
			NodeList portalObjectNodes = document.getElementsByTagName("portaleditor_data_PortalObject");

			int length = portalObjectNodes.getLength();
			System.out.println( "length: " + length );
			for( int cnt=length-1; cnt>=0; cnt-- ) {
				Node portalObjectNode = portalObjectNodes.item(cnt);
				System.out.println( "name: " + portalObjectNode.getNodeName() );

				Document childDocument = builder.newDocument();
				Node node = childDocument.importNode(portalObjectNode, true);
				System.out.println( "name2: " + node.getNodeName() );
				
				childDocument.appendChild(node);
				PortalObject portal = new PortalObject();
				
				portal.setXml(childDocument);
				add(portal);
				
			}

		}	catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	
	private void add(PortalObject object) {
		portalList.add(object);
		
	} 
	



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

	private void savePortal( OutputStream out, PortalObject portalObject ) throws IOException {
		byte[] data = new byte[PORTAL_DATABLOCKSIZE];
		
		int bottom  = (int) portalObject.bottom;
		int left = (int) portalObject.left;
		int floor= (int) portalObject.floor;
		
		log.debug("Portal:" + portalObject.id);
		// write top-left-floor
		data[PORTAL_BOTTOM_H] = (byte) (bottom>>13);
		data[PORTAL_BOTTOM_L] = (byte) ((bottom>>5)&0x000000FF);
		data[PORTAL_LEFT_H] = (byte) (left>>13);
		data[PORTAL_LEFT_L] = (byte) ((left>>5)&0x000000FF);
		data[PORTAL_FLOOR_H] = (byte) (floor>>13);
		data[PORTAL_FLOOR_L] = (byte) ((floor>>5)&0x000000FF);

		for( int i=0; i<6; i++ ) {
			log.debug("data["+i+"]:" + Integer.toHexString(data[i]));
		}
		
		data[PORTAL_CEIL_HEIGHT] = (byte) ((portalObject.ceil-floor)>>5);
		data[PORTAL_WIDTH] = (byte) ((portalObject.right-left)>>5);
		data[PORTAL_HEIGHT] = (byte) ((portalObject.top-bottom)>>5);

		data[PORTAL_LIGHT] = (byte) (portalObject.light);
		data[PORTAL_LIGHT_TYPE] = (byte) (portalObject.light_type);
		
		String id_str 		= portalObject.id;
		int    id_str_len 	= id_str.length(); 
		int    id_start_idx = "Portal".length();
		data[PORTAL_ID] 	= Byte.parseByte(id_str.substring(id_start_idx, id_str_len));
	
		data[PORTAL_TEXT_FLOOR] = 0;
		data[PORTAL_TEXT_CEIL] = 0;
		data[PORTAL_NUM_REF_PORTALS] = (byte) portalObject.referredPortals.size();
		data[PORTAL_NUM_REF_SPANS] = (byte) portalObject.referredSpans.size();
	
		out.write(data);		
		
		Iterator spans = portalObject.referredSpans.iterator();
		while( spans.hasNext() ) {
			saveSpan(out, (Span) spans.next());
		}
		
		Iterator connectionPorts = portalObject.referredPortals.iterator();
		while( connectionPorts.hasNext() ) {
			saveConnectionPort(out, (ConnectionPort) connectionPorts.next());
		}
		
	}
	

	public void saveSpan(OutputStream out, Span span) throws IOException {
		byte[] data = new byte[SPAN_DATABLOCKSIZE];
		byte align = 0;
		if( span.align.equals(ConnectionPort.ALIGN_LEFT) ) {
			align = _SPAN_ALIGN_LEFT;
		}	else
		if( span.align.equals(ConnectionPort.ALIGN_RIGHT) ) {
			align = _SPAN_ALIGN_RIGHT;
		}	else
		if( span.align.equals(ConnectionPort.ALIGN_BOTTOM) ) {
			align = _SPAN_ALIGN_BOTTOM;
		}	else
		if( span.align.equals(ConnectionPort.ALIGN_TOP) ) {
			align = _SPAN_ALIGN_TOP;
		}
	
		data[SPAN_ALIGN] = align;
		data[SPAN_LEFT] = (byte)(span.left>>5);
		data[SPAN_RIGHT] = (byte)(span.right>>5);
		
		data[SPAN_TOP] = (byte)(span.top>>5);
		data[SPAN_BOTTOM] = (byte)(span.bottom>>5);
		data[SPAN_TEXTURE] = 0;
		data[SPAN_ANIMATED] = 0;
		
		out.write(data);
	}

	
	public void saveConnectionPort(OutputStream out, ConnectionPort connectionPort) throws IOException {
		byte[] data = new byte[CONN_PORT_DATABLOCKSIZE];
		byte align = 0;
		if( connectionPort.align.equals(ConnectionPort.ALIGN_LEFT) ) {
			align = _CONN_PORT_ALIGN_LEFT;
		}	else
		if( connectionPort.align.equals(ConnectionPort.ALIGN_RIGHT) ) {
			align = _CONN_PORT_ALIGN_RIGHT;
		}	else
		if( connectionPort.align.equals(ConnectionPort.ALIGN_BOTTOM) ) {
			align = _CONN_PORT_ALIGN_BOTTOM;
		}	else
		if( connectionPort.align.equals(ConnectionPort.ALIGN_TOP) ) {
			align = _CONN_PORT_ALIGN_TOP;
		}
	
		data[CONN_PORT_ALIGN] = align;
		data[CONN_PORT_LEFT] = (byte)(connectionPort.left>>5);
		data[CONN_PORT_RIGHT] = (byte)(connectionPort.right>>5);
		
		data[CONN_PORT_TOP] = (byte)(connectionPort.top>>5);
		data[CONN_PORT_BOTTOM] = (byte)(connectionPort.bottom>>5);

		String id_str 		= connectionPort.portal_id;
		int    id_str_len 	= id_str.length(); 
		int    id_start_idx = "Portal".length();
		data[CONN_PORT_REL_PORTAL_ID] 	= Byte.parseByte(id_str.substring(id_start_idx, id_str_len));
		
		out.write(data);
	}
	
	public void save() {
		try {
			File file = new File("portals.map");
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			
			Iterator portals = portalList.iterator();
			out.write((portalList.size()>>8));
			out.write((portalList.size()&0x000000FF));
			while(portals.hasNext()) {
				savePortal(out, (PortalObject) portals.next());
			}

		}	catch( Exception e ) {
			e.printStackTrace();
		}	
	}
	
}
