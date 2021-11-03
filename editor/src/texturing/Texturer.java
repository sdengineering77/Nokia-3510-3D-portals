package texturing;

import java.io.File;
import java.io.FileWriter;
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
public class Texturer {
	private final static Category log = Category.getInstance(Texturer.class);
	private ArrayList  portalList		= new ArrayList();
	
	public void load() {
		try {
			File file = new File("portals_connected.xml");
			
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
	
	private Collection solveTextures(String align, int left, int right, int top, int bottom, Hashtable viewpointHorOffsetHash) {
		ArrayList spanList = new ArrayList();
		
		// walk over wall and search for viewport startpoints
		// if a startpoint is found it is checked against the left, right, top and
		// bottom boundaries. If it is within, the area is processed
		// Processing an area involves creating a span from beginning point to
		// found startpoint, and recursifly solve the textures of the remaining area
		// if no viewports are found, only one span must be returned, that 
		// spans the entire area.
		int direction;
		if( left > right ) {
			direction = -1;
		}	else {
			direction = 1;
		}
			

		log.debug( "starting at x=" + left );
		int last_leftX = left;
		for( int x=left; x<right; x+=direction ) {
			ArrayList connectionPortList = (ArrayList) viewpointHorOffsetHash.get(new Integer(x));
			
			// any viewport startpoints
			if( connectionPortList != null ) {
				// walk thru viewport list, check within boundaries
				// and if so, find most upper (highest "top" value) one
				// to create next span
				log.debug( "hit at x=" + x );

				ConnectionPort mostTopConnectionPort = null;
				
				Iterator connectionPorts = connectionPortList.iterator();
				while( connectionPorts.hasNext() ) {
					ConnectionPort connectionPort = (ConnectionPort) connectionPorts.next();
					
					// check boundaries
					if( connectionPort.top    <= top &&
					    connectionPort.bottom >= bottom ) {
					
						// within boundaries, check most Top Value
						if( mostTopConnectionPort == null ||
							mostTopConnectionPort.top < connectionPort.top ) {
						
							mostTopConnectionPort = connectionPort;
						}
					}
				}
				
				// most top viewport value found for this area
				// this means
				// * we have a span before this viewport
				// * area above viewport must be textured
				// * area below viewport must be textured
				// if no viewport was found this is skipped..
				if( mostTopConnectionPort != null ) {
					// create span before viewport
					log.debug( "====================================================: " );
					log.debug( "ConnectionPort: " + mostTopConnectionPort.getXml() );

					Span span = new Span();
				
					span.align  = mostTopConnectionPort.align;
					span.bottom = bottom;
					span.left	= left;
					span.right	= mostTopConnectionPort.left;
					span.textureID = "";
					span.top	= top;
					
					// just add if it has volume
					if( span.top != span.bottom &&
						span.left != span.right ) {
						spanList.add( span );
						log.debug("ADDED: " + span.getXml());
					}
					
					// area above viewport
					spanList.addAll(solveTextures(align, mostTopConnectionPort.left,
													mostTopConnectionPort.right,
													top,
													mostTopConnectionPort.top,
													viewpointHorOffsetHash));

					// area below viewport
					spanList.addAll(solveTextures(align, mostTopConnectionPort.left,
													mostTopConnectionPort.right,
													mostTopConnectionPort.bottom,
													bottom,
													viewpointHorOffsetHash));
					
					last_leftX = left = x = mostTopConnectionPort.right;
					x-=direction;
				}
			}
			
		}
		
		// no spans? add entire area as span
		if( last_leftX != right ) {
			Span span = new Span();
		
			span.align  = align;
			span.bottom = bottom;
			span.left	= left;
			span.right	= right;
			span.textureID = "";
			span.top	= top;

			// just add if it has volume
			if( span.top != span.bottom &&
				span.left != span.right ) {
				spanList.add( span );
				log.debug("ADDED: " + span.getXml());
			}
		}
		
		return spanList;
	}


	private Collection solveLeft(PortalObject currPortalObject) {
		Collection spanList = new ArrayList();
		Hashtable viewportHorOffsetHash = new Hashtable();
		log.debug("Portal: " + currPortalObject.id);
		if( currPortalObject.getReferredPortals() != null ) {
			Iterator connectionPorts = currPortalObject.getReferredPortals().iterator();
			
			while( connectionPorts.hasNext() ) {
				ConnectionPort connectionPort = (ConnectionPort) connectionPorts.next();
				
				if( connectionPort.align.equals(ConnectionPort.ALIGN_LEFT) ) {
					ArrayList connectionPortList;
					if( (connectionPortList=(ArrayList)viewportHorOffsetHash.get(new Integer(connectionPort.left))) == null ) {
						connectionPortList = new ArrayList();
						viewportHorOffsetHash.put(new Integer(connectionPort.left), connectionPortList);
					}
					connectionPortList.add(connectionPort);
					log.debug("added connport" + connectionPort.getXml());
				}
			}
		}	
			
		spanList.addAll(		solveTextures(ConnectionPort.ALIGN_LEFT,
											0,
											currPortalObject.top-currPortalObject.bottom,
											currPortalObject.ceil,
											currPortalObject.floor,
											viewportHorOffsetHash));
			
		
		return spanList;		
		
	}


	private Collection solveRight(PortalObject currPortalObject) {
		Collection spanList = new ArrayList();
		Hashtable viewportHorOffsetHash = new Hashtable();
		log.debug("Portal: " + currPortalObject.id);
		if( currPortalObject.getReferredPortals() != null ) {
			Iterator connectionPorts = currPortalObject.getReferredPortals().iterator();
			
			while( connectionPorts.hasNext() ) {
				ConnectionPort connectionPort = (ConnectionPort) connectionPorts.next();
				
				if( connectionPort.align.equals(ConnectionPort.ALIGN_RIGHT) ) {
					ArrayList connectionPortList;
					if( (connectionPortList=(ArrayList)viewportHorOffsetHash.get(new Integer(connectionPort.left))) == null ) {
						connectionPortList = new ArrayList();
						viewportHorOffsetHash.put(new Integer(connectionPort.left), connectionPortList);
					}
					connectionPortList.add(connectionPort);
					log.debug("added connport" + connectionPort.getXml());
				}
			}
		}	
			
		spanList.addAll(		solveTextures(ConnectionPort.ALIGN_RIGHT,
											0,
											currPortalObject.top-currPortalObject.bottom,
											currPortalObject.ceil,
											currPortalObject.floor,
											viewportHorOffsetHash));
			
		
		return spanList;		
		
	}

	private Collection solveTop(PortalObject currPortalObject) {
		Collection spanList = new ArrayList();
		Hashtable viewportHorOffsetHash = new Hashtable();
		log.debug("Portal: " + currPortalObject.id);
		if( currPortalObject.getReferredPortals() != null ) {
			Iterator connectionPorts = currPortalObject.getReferredPortals().iterator();
			
			while( connectionPorts.hasNext() ) {
				ConnectionPort connectionPort = (ConnectionPort) connectionPorts.next();
				
				if( connectionPort.align.equals(ConnectionPort.ALIGN_TOP) ) {
					ArrayList connectionPortList;
					if( (connectionPortList=(ArrayList)viewportHorOffsetHash.get(new Integer(connectionPort.left))) == null ) {
						connectionPortList = new ArrayList();
						viewportHorOffsetHash.put(new Integer(connectionPort.left), connectionPortList);
					}
					connectionPortList.add(connectionPort);
					log.debug("added connport" + connectionPort.getXml());
				}
			}
		}	
			
		spanList.addAll(		solveTextures(ConnectionPort.ALIGN_TOP,
											0,
											currPortalObject.right-currPortalObject.left,
											currPortalObject.ceil,
											currPortalObject.floor,
											viewportHorOffsetHash));
			
		
		return spanList;		
		
	}

	private Collection solveBottom(PortalObject currPortalObject) {
		Collection spanList = new ArrayList();
		Hashtable viewportHorOffsetHash = new Hashtable();
		log.debug("Portal: " + currPortalObject.id);
		if( currPortalObject.getReferredPortals() != null ) {
			Iterator connectionPorts = currPortalObject.getReferredPortals().iterator();
			
			while( connectionPorts.hasNext() ) {
				ConnectionPort connectionPort = (ConnectionPort) connectionPorts.next();
				
				if( connectionPort.align.equals(ConnectionPort.ALIGN_BOTTOM) ) {
					ArrayList connectionPortList;
					if( (connectionPortList=(ArrayList)viewportHorOffsetHash.get(new Integer(connectionPort.left))) == null ) {
						connectionPortList = new ArrayList();
						viewportHorOffsetHash.put(new Integer(connectionPort.left), connectionPortList);
					}
					connectionPortList.add(connectionPort);
					log.debug("added connport" + connectionPort.getXml());
				}
			}
		}	
			
		spanList.addAll(		solveTextures(ConnectionPort.ALIGN_BOTTOM,
											0,
											currPortalObject.right-currPortalObject.left,
											currPortalObject.ceil,
											currPortalObject.floor,
											viewportHorOffsetHash));
			
		
		return spanList;		
		
	}


	
	public void solve() {
		Iterator portals = portalList.iterator();
				
		while( portals.hasNext() ) {
			PortalObject portalObject = (PortalObject) portals.next();
			ArrayList	spans = new ArrayList();
					
			// solve left
			spans.addAll(this.solveLeft(portalObject));
			// solve right
			spans.addAll(this.solveRight(portalObject));
			
			// solve top
			spans.addAll(this.solveTop(portalObject));
			// solve bottom
			spans.addAll(this.solveBottom(portalObject));

			portalObject.setReferredSpans(spans);

		}
		
		save();
	}
	
	public void save() {
		try {
			File file = new File("portals_textured.xml");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			
			Iterator portals = portalList.iterator();
			
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
			writer.write("<portals>\r\n");
			while(portals.hasNext()) {
				XmlAble object = (XmlAble) portals.next();
				writer.write(object.getXml(1).toString());
			}

			writer.write("</portals>\r\n");
			writer.close();
			
		}	catch( Exception e ) {
			e.printStackTrace();
		}	
	}
	
}
