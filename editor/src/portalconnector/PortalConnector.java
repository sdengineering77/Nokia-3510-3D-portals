package portalconnector;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import portalconnector.data.ConnectionPort;
import portaleditor.data.PortalObject;
import portaleditor.data.XmlAble;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PortalConnector {
	private Hashtable	xIntersections	= new Hashtable();
	private Hashtable	zIntersections	= new Hashtable();
	private ArrayList  portalList		= new ArrayList();
	
	public void load() {
		try {
			File file = new File("portals.xml");
			
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
				if( portal.getReferredPortals() != null && portal.getReferredPortals().size() > 0 ) {
					System.out.println( "Portal: " + portal.getId() + " had " + portal.getReferredPortals().size() + " childs... cleared..." );
					portal.getReferredPortals().clear();
				}
				add(portal);
				
			}

		}	catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	
	private void add(PortalObject object) {
		portalList.add(object);
		addXInterSection(object);
		addZInterSection(object);
		
		
	} 
	
	private void addXInterSection( PortalObject object ) {
		Integer left  = new Integer(object.getLeft());
		Integer right = new Integer(object.getRight());
		
		ArrayList xIntersectionsList_left = (ArrayList) xIntersections.get(left);
		ArrayList xIntersectionsList_right = (ArrayList) xIntersections.get(right);
		
		if( xIntersectionsList_left == null ) {
			xIntersectionsList_left = new ArrayList();
			xIntersections.put(left, xIntersectionsList_left);
		}

		if( xIntersectionsList_right == null ) {
			xIntersectionsList_right = new ArrayList();
			xIntersections.put(right, xIntersectionsList_right);
		}

		xIntersectionsList_left.add(object);
		xIntersectionsList_right.add(object);		
	}

	private void addZInterSection( PortalObject object ) {
		Integer top  = new Integer(object.getTop());
		Integer bottom = new Integer(object.getBottom());
		
		ArrayList zIntersectionsList_top    = (ArrayList) zIntersections.get(top);
		ArrayList zIntersectionsList_bottom = (ArrayList) zIntersections.get(bottom);
		
		if( zIntersectionsList_top == null ) {
			zIntersectionsList_top = new ArrayList();
			zIntersections.put(top, zIntersectionsList_top);
		}

		if( zIntersectionsList_bottom == null ) {
			zIntersectionsList_bottom = new ArrayList();
			zIntersections.put(bottom, zIntersectionsList_bottom);
		}

		zIntersectionsList_top.add(object);
		zIntersectionsList_bottom.add(object);		
	}
	
	



	/**
	 * 	X INTERSECTION SOLVER
	 * 
	 */
	private void	solveXIntersections( PortalObject object ) {
		Integer x_left  = new Integer(object.getLeft());
		Integer x_right = new Integer(object.getRight());
		
		ArrayList xIntersectionList;
		// get left side intersections
		xIntersectionList = (ArrayList) xIntersections.get(x_left);
		// got any?
		if( xIntersectionList != null ) {
			// loop thru all of them, and check right side may intersect x
			int x = object.getLeft();
			Iterator xIntersections = xIntersectionList.iterator();
			while( xIntersections.hasNext() ) {
				PortalObject currIntersectingPortalObect = (PortalObject) xIntersections.next();
				
				if( x == currIntersectingPortalObect.getRight() ) {
					// yep, it may intersects x
					// check if they really connect in space
					if( checkXPortalsConnectZY(object, currIntersectingPortalObect) ) {
						// Ok, this IS an intersection... find exact points now :(
						ConnectionPort connector = solveXPortalConnector(object, currIntersectingPortalObect, true);
						// Ok, so far for left side :)
						if( connector != null ) {
							object.getReferredPortals().add(connector);
						}
						
					}

				}
			}
		}

		// do the same for right side
		xIntersectionList = (ArrayList) xIntersections.get(x_right);
		// got any?
		if( xIntersectionList != null ) {
			// loop thru all of them, and check right side may intersect x
			int x = object.getRight();
			Iterator xIntersections = xIntersectionList.iterator();
			while( xIntersections.hasNext() ) {
				PortalObject currIntersectingPortalObect = (PortalObject) xIntersections.next();
				
				if( x == currIntersectingPortalObect.getLeft() ) {
					// yep, it may intersects x
					// check if they really connect in space
					if( checkXPortalsConnectZY(object, currIntersectingPortalObect) ) {
						// Ok, this IS an intersection... find exact points now :(
						ConnectionPort connector = solveXPortalConnector(object, currIntersectingPortalObect, false);
						// Ok, so far for left side :)
						if( connector != null ) {
							object.getReferredPortals().add(connector);
						}
						
					}

				}
			}
		}

	}

	
	private boolean checkXPortalsConnectZY(PortalObject a, PortalObject b) {
		// intersect on x?
		if(	(a.getBottom() <= b.getTop()) &&
				(a.getTop() >= b.getBottom()) ) {
			// intersect in Y? 
			if(	(a.getFloor() <= b.getCeil()) &&
					(a.getCeil() >= b.getFloor()) ) {
				return true;				
			}
			
		}
		
		return false;
	}
	
	private ConnectionPort solveXPortalConnector(PortalObject from, PortalObject to, boolean isLeftOtherwiseRight) {
		int vp_bottom, vp_top, vp_floor, vp_ceil;
		// find highest bottom value
		if( from.getBottom() > to.getBottom() ) {
			vp_bottom = from.getBottom();
		}	else {
			vp_bottom = to.getBottom();
		}
		
		// find lowest top value
		if( from.getTop() < to.getTop() ) {
			vp_top = from.getTop();
		}	else {
			vp_top = to.getTop();
		}
	
		// find lowest ceil value
		if( from.getCeil() < to.getCeil() ) {
			vp_ceil = from.getCeil();
		}	else {
			vp_ceil = to.getCeil();
		}
		
		// find highest floor value
		if( from.getFloor() > to.getFloor() ) {
			vp_floor = from.getFloor();
		}	else {
			vp_floor = to.getFloor();
		}
		
		ConnectionPort port = new ConnectionPort();
		port.portal_id = to.getId();
		port.type	= to.getType();
		port.bottom = vp_floor;
		port.top    = vp_ceil;
		
		// create left side insersecting connector
		if( isLeftOtherwiseRight ) {
			port.align = ConnectionPort.ALIGN_LEFT;
			port.left   = vp_bottom - from.bottom;
			port.right  = vp_top - from.bottom;
			
		}	else {
			port.align = ConnectionPort.ALIGN_RIGHT;
			port.left   = from.top - vp_top;
			port.right  = from.top - vp_bottom;
		}
		
		if( port.left == port.right ||
		 	 port.top == port.bottom  ) {
			port = null;
		}
		
		return port;
	}



	/**
	 * 	Z INTERSECTION SOLVER
	 * 
	 */
	private void	solveZIntersections( PortalObject object ) {
		Integer z_bottom  	= new Integer(object.getBottom());
		Integer z_top 		= new Integer(object.getTop());
		
		ArrayList zIntersectionList;
		// get bottom side intersections
		zIntersectionList = (ArrayList) zIntersections.get(z_bottom);
		// got any?
		if( zIntersectionList != null ) {
			// loop thru all of them, and check right side may intersect x
			int z = object.getBottom();
			Iterator zIntersections = zIntersectionList.iterator();
			while( zIntersections.hasNext() ) {
				PortalObject currIntersectingPortalObect = (PortalObject) zIntersections.next();
				
				if( z == currIntersectingPortalObect.getTop() ) {
					// yep, it may intersects x
					// check if they really connect in space
					if( checkZPortalsConnectXY(object, currIntersectingPortalObect) ) {
						// Ok, this IS an intersection... find exact points now :(
						ConnectionPort connector = solveZPortalConnector(object, currIntersectingPortalObect, true);
						// Ok, so far for left side :)
						if( connector != null ) {
							object.getReferredPortals().add(connector);
						}
						
					}

				}
			}
		}

		// do the same for top side
		zIntersectionList = (ArrayList) zIntersections.get(z_top);
		// got any?
		if( zIntersectionList != null ) {
			// loop thru all of them, and check right side may intersect x
			int z = object.getTop();
			Iterator zIntersections = zIntersectionList.iterator();
			while( zIntersections.hasNext() ) {
				PortalObject currIntersectingPortalObect = (PortalObject) zIntersections.next();
				
				if( z == currIntersectingPortalObect.getBottom() ) {
					// yep, it may intersects x
					// check if they really connect in space
					if( checkZPortalsConnectXY(object, currIntersectingPortalObect) ) {
						// Ok, this IS an intersection... find exact points now :(
						ConnectionPort connector = solveZPortalConnector(object, currIntersectingPortalObect, false);
						// Ok, so far for left side :)
						if( connector != null ) {
							object.getReferredPortals().add(connector);
						}
						
					}

				}
			}
		}

	}


	private boolean checkZPortalsConnectXY(PortalObject a, PortalObject b) {
		// intersect on z?
		if(	(a.getLeft() <= b.getRight()) &&
				(a.getRight() >= b.getLeft()) ) {
			// intersect in Y? 
			if(	(a.getFloor() <= b.getCeil()) &&
					(a.getCeil() >= b.getFloor()) ) {
				return true;				
			}
			
		}
		
		return false;
	}

	private ConnectionPort solveZPortalConnector(PortalObject from, PortalObject to, boolean isBottomOtherwiseTop) {
		int vp_left, vp_right, vp_floor, vp_ceil;
		// find highest left value
		if( from.getLeft() > to.getLeft() ) {
			vp_left = from.getLeft();
		}	else {
			vp_left = to.getLeft();
		}
		
		// find lowest top value
		if( from.getRight() < to.getRight() ) {
			vp_right = from.getRight();
		}	else {
			vp_right = to.getRight();
		}
	
		// find lowest ceil value
		if( from.getCeil() < to.getCeil() ) {
			vp_ceil = from.getCeil();
		}	else {
			vp_ceil = to.getCeil();
		}
		
		// find highest floor value
		if( from.getFloor() > to.getFloor() ) {
			vp_floor = from.getFloor();
		}	else {
			vp_floor = to.getFloor();
		}
		
		ConnectionPort port = new ConnectionPort();
		port.portal_id = to.getId();
		port.type	= to.getType();
		port.bottom = vp_floor;
		port.top    = vp_ceil;
		
		// create left side insersecting connector
		if( isBottomOtherwiseTop ) {
			port.align = ConnectionPort.ALIGN_BOTTOM;
			port.left   = from.right - vp_right;
			port.right  = from.right - vp_left;
			
			
		}	else {
			port.align = ConnectionPort.ALIGN_TOP;
			port.left   = vp_left  - from.left;
			port.right  = vp_right - from.left;
		}

		if( port.left == port.right ||
		 	 port.top == port.bottom  ) {
			port = null;
		}
		
		return port;
	}


	
	/**
	 * 	SOLVE INTERSECTIONS
	 */
	public void solve() {
		// run thru all portals in list, select current p
		Iterator portals = portalList.iterator();
		
		while( portals.hasNext() ) {
			PortalObject currPortalObject = (PortalObject) portals.next();
	
			//   get array of left intersections, select current ap
			//     check if ap.right equals a.left
			//       do full xintersection check
			//   get array of right intersections, select current ap
			//     check if ap.left equals a.right
			//       do full xintersection check
			solveXIntersections(currPortalObject);
			//   get array of top intersections, select current ap
			//     check if ap.bottom equals a.top
			//       do full zintersection check

			//   get array of bottom intersections, select current ap
			//     check if ap.top equals a.bottom
			//       do full zintersection check
			solveZIntersections(currPortalObject);
			
		}
		

		// full xintersection check
		//   check p.bottom between ap.top and ap.bottom or
		//         p.top    between 
		
		// save portals
		save();
		
	}


	public void save() {
		try {
			File file = new File("portals_connected.xml");
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
