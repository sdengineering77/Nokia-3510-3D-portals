package portalconnector.data;

import org.w3c.dom.Document;

import portaleditor.data.AbstractXmlAbleObject;
import portaleditor.data.XmlAble;

/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class ConnectionPort extends AbstractXmlAbleObject {
	public final static transient String ALIGN_LEFT = "LEFT";
	public final static transient String ALIGN_RIGHT = "RIGHT";
	public final static transient String ALIGN_TOP = "TOP";
	public final static transient String ALIGN_BOTTOM = "BOTTOM";
	public String portal_id;
	public int left;
	public int right;
	public int top;
	public int bottom;
	public String align;
	public String type;
	
}
