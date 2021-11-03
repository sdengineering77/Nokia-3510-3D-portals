package portaleditor.data;

import ilog.views.chart.data.IlvDataSet;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * @author DaSedney
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class PortalObject extends AbstractXmlAbleObject {

	public 	boolean virtual;	
	public 	int left;
	public 	int right;
	public 	int top;
	public 	int bottom;
	public 	int floor;
	public 	int ceil;
	public 	String type;
	public 	String security_type;
	public 	String id;
	public 	int light;
	public 	int light_type;
	public 	ArrayList	referredPortals = new ArrayList();
	public 	ArrayList	referredSpans	= new ArrayList();
	
	public     transient	IlvDataSet	ilvDataSet;
	

	private 	transient static int	nextID = 1;
	private 	transient static String xml_name = null;
	public PortalObject() {
		id = "Portal"+nextID();
		if( xml_name == null ) {
			xml_name =  this.getClass().getName().replace('.', '_');		
		}
	}
	
	
	public static synchronized int nextID() {
		return nextID++;
	}
	
	/**
	 * Returns the bottom.
	 * @return int
	 */
	public int getBottom() {
		return bottom;
	}

	/**
	 * Returns the ceil.
	 * @return int
	 */
	public int getCeil() {
		return ceil;
	}

	/**
	 * Returns the floor.
	 * @return int
	 */
	public int getFloor() {
		return floor;
	}

	/**
	 * Returns the left.
	 * @return int
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * Returns the referredPortals.
	 * @return ArrayList
	 */
	public ArrayList getReferredPortals() {
		return referredPortals;
	}

	/**
	 * Returns the right.
	 * @return int
	 */
	public int getRight() {
		return right;
	}

	/**
	 * Returns the top.
	 * @return int
	 */
	public int getTop() {
		return top;
	}

	/**
	 * Sets the bottom.
	 * @param bottom The bottom to set
	 */
	public void setBottom(int bottom) {
		this.bottom = bottom;
	}

	/**
	 * Sets the ceil.
	 * @param ceil The ceil to set
	 */
	public void setCeil(int ceil) {
		this.ceil = ceil;
	}

	/**
	 * Sets the floor.
	 * @param floor The floor to set
	 */
	public void setFloor(int floor) {
		this.floor = floor;
	}

	/**
	 * Sets the left.
	 * @param left The left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}

	/**
	 * Sets the referredPortals.
	 * @param referredPortals The referredPortals to set
	 */
	public void setReferredPortals(ArrayList referredPortals) {
		this.referredPortals = referredPortals;
	}

	/**
	 * Sets the right.
	 * @param right The right to set
	 */
	public void setRight(int right) {
		this.right = right;
	}

	/**
	 * Sets the top.
	 * @param top The top to set
	 */
	public void setTop(int top) {
		this.top = top;
	}

	/**
	 * Returns the virtual.
	 * @return boolean
	 */
	public boolean isVirtual() {
		return virtual;
	}

	/**
	 * Sets the virtual.
	 * @param virtual The virtual to set
	 */
	public void setVirtual(boolean virtual) {
		this.virtual = virtual;
	}

	/**
	 * Returns the id.
	 * @return String
	 */
	public String getId() {
		return id;
	}

	/**
	 * Returns the light.
	 * @return int
	 */
	public int getLight() {
		return light;
	}

	/**
	 * Returns the light_type.
	 * @return int
	 */
	public int getLight_type() {
		return light_type;
	}

	/**
	 * Returns the security_type.
	 * @return String
	 */
	public String getSecurity_type() {
		return security_type;
	}

	/**
	 * Returns the type.
	 * @return String
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the id.
	 * @param id The id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the light.
	 * @param light The light to set
	 */
	public void setLight(int light) {
		this.light = light;
	}

	/**
	 * Sets the light_type.
	 * @param light_type The light_type to set
	 */
	public void setLight_type(int light_type) {
		this.light_type = light_type;
	}

	/**
	 * Sets the security_type.
	 * @param security_type The security_type to set
	 */
	public void setSecurity_type(String security_type) {
		this.security_type = security_type;
	}

	/**
	 * Sets the type.
	 * @param type The type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Returns the ilvDataSet.
	 * @return IlvDataSet
	 */
	public IlvDataSet getIlvDataSet() {
		return ilvDataSet;
	}

	/**
	 * Sets the ilvDataSet.
	 * @param ilvDataSet The ilvDataSet to set
	 */
	public void setIlvDataSet(IlvDataSet ilvDataSet) {
		this.ilvDataSet = ilvDataSet;
	}



	/**
	 * Returns the referredSpans.
	 * @return ArrayList
	 */
	public ArrayList getReferredSpans() {
		return referredSpans;
	}

	/**
	 * Sets the referredSpans.
	 * @param referredSpans The referredSpans to set
	 */
	public void setReferredSpans(ArrayList referredSpans) {
		this.referredSpans = referredSpans;
	}

}
