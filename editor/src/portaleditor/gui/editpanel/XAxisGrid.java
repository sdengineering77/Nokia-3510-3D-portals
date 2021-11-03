package portaleditor.gui.editpanel;

import java.util.Hashtable;

import ilog.views.chart.IlvGrid;
import ilog.views.chart.util.IlvDoubleArray;

/**
 * @author sedneyd
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class XAxisGrid extends IlvGrid {
	private Hashtable xAxisMapping = null;
	
	public XAxisGrid( Hashtable xAxisMapping ) {
		if( xAxisMapping != null ) {
			this.xAxisMapping = xAxisMapping;
		}	else {
			xAxisMapping = new Hashtable();
		}
	}
	
	
	protected void draw( java.awt.Graphics g, IlvDoubleArray values, boolean major ) {
		if( major && (xAxisMapping != null) ) { // major grid only
			IlvDoubleArray gridValues = new IlvDoubleArray();
			
			for( int valueCnt=0; valueCnt<values.size(); valueCnt++ ) {
				Integer iXAxisIndex = new Integer( (int) values.get(valueCnt) );
				if( xAxisMapping.get( iXAxisIndex ) != null ) {
					gridValues.add(values.get(valueCnt));
				}			
			}
			
			super.draw( g, gridValues, true );
		}
		
	} 
	
	/**
	 * Returns the xAxisMapping.
	 * @return Hashtable
	 */
	public Hashtable getXAxisMapping() {
		return xAxisMapping;
	}

	/**
	 * Sets the xAxisMapping.
	 * @param xAxisMapping The xAxisMapping to set
	 */
	public void setXAxisMapping(Hashtable xAxisMapping) {
		this.xAxisMapping = xAxisMapping;
	}

}
