/* Generated by Together */

package portaleditor.gui.editpanel;
import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;

import portaleditor.data.DefaultPortalValues;
import portaleditor.data.PortalObject;

import ilog.views.chart.interactor.IlvChartDataInteractor;
import ilog.views.chart.IlvChart;
import ilog.views.chart.data.IlvDataSet;
import ilog.views.chart.data.IlvDefaultDataSet;
import ilog.views.chart.data.IlvDefaultDataSource;
import ilog.views.chart.data.IlvDataSetList;
import ilog.views.chart.renderer.IlvPolylineChartRenderer;
import ilog.views.chart.IlvDoublePoints;



public class AddPortalInteractor extends IlvChartDataInteractor {

    public final static int SINGLE_CLICK		=   1;
    public final static int DOUBLE_CLICK		=   2;

    public final static int	ADD_FIRST_NODE		=	1;
    public final static int	ADD_SECND_TH_NODE	=	2;
	private int addingState						=   ADD_FIRST_NODE;

	private IlvDefaultDataSet			newPortalDataSet			= null;
	private IlvPolylineChartRenderer 	selectPortalPolylineChartRenderer	= null;
	private IlvPolylineChartRenderer 	allTrainsPolylineChartRenderer	= null;
	private Hashtable					xAxisToCheckpointMapping		= null;
	private IllegalJViewsPortalPanel 	panel							= null;

	public AddPortalInteractor( IllegalJViewsPortalPanel panel, IlvPolylineChartRenderer selectPortalPolylineChartRenderer) {
        super( 0, MouseEvent.BUTTON1_MASK );
        this.enableEvents( AWTEvent.MOUSE_EVENT_MASK|AWTEvent.MOUSE_MOTION_EVENT_MASK|AWTEvent.KEY_EVENT_MASK );
        this.setPickingMode( IlvChartDataInteractor.NEARESTPOINT_PICKING );
        this.selectPortalPolylineChartRenderer = selectPortalPolylineChartRenderer;
        this.panel = panel;
//        this.addingState = ADD_FIRST_NODE;
    }

	public void setXAxisToCheckpointMapping( Hashtable xAxisToCheckpointMapping ) {
        this.xAxisToCheckpointMapping = xAxisToCheckpointMapping;
    }

    public boolean isHandling( int x, int y ) {
        return true;
    }

    public void processMouseEvent( MouseEvent me ) {
		if( me.getID() == MouseEvent.MOUSE_CLICKED ) {
	        IlvDoublePoints dataCoordinates = new IlvDoublePoints( me.getX(), me.getY() );
	        selectPortalPolylineChartRenderer.toData( dataCoordinates );
			int x = ((((int) dataCoordinates.getX(0))>>5)<<5);
			int y = ((((int) dataCoordinates.getY(0))>>5)<<5);

	        // add this to datasource
	        IlvDataSet dataSet = createDataSet(x, y, DefaultPortalValues.width, DefaultPortalValues.height, DefaultPortalValues.floor, DefaultPortalValues.ceil);
	        selectPortalPolylineChartRenderer.getDataSource().addDataSet( dataSet );
			
			panel.setPortalObject((PortalObject)dataSet.getProperty("PortalObject"));
        }
        me.consume();
    }

    public void processMouseMotionEvent( MouseEvent me ) {
        me.consume();
    }

	public static IlvDataSet createDataSet(int x, int y, int width, int height, int floor, int ceil) {
		IlvDefaultDataSet			newPortalDataSet;
		PortalObject portal;
		
		// create new portal object
		portal = new PortalObject();
		
		portal.setLeft(x);
		portal.setRight(x+width);		
		portal.setTop(y);	
		portal.setBottom(y-height);
		portal.setFloor(floor);		
		portal.setCeil(ceil);		

		portal.light  = DefaultPortalValues.light;
		portal.light_type = DefaultPortalValues.light_type;
		portal.security_type = DefaultPortalValues.security_type != null ? new String( DefaultPortalValues.security_type  ) : null;
		portal.type = DefaultPortalValues.type != null ? new String(DefaultPortalValues.type) : null;

        // create new dataset for this train
	    newPortalDataSet = new IlvDefaultDataSet(portal.toString());
		newPortalDataSet.putProperty("PortalObject", portal, false);
		portal.setIlvDataSet(newPortalDataSet);

		newPortalDataSet.addData(portal.getRight(), portal.getBottom());
		newPortalDataSet.addData(portal.getLeft(), portal.getTop());
		newPortalDataSet.addData(portal.getRight(), portal.getTop());
		newPortalDataSet.addData(portal.getRight(), portal.getBottom());
		newPortalDataSet.addData(portal.getLeft(), portal.getBottom());
		newPortalDataSet.addData(portal.getLeft(), portal.getTop());
		
		return newPortalDataSet;
	}
	
}
