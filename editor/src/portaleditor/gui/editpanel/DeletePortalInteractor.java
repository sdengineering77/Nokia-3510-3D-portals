/* Generated by Together */

package portaleditor.gui.editpanel;

import ilog.views.chart.interactor.IlvChartHighlightInteractor;
import ilog.views.chart.IlvDisplayPoint;
import java.awt.event.MouseEvent;
import ilog.views.chart.interactor.IlvChartDataInteractor;
import ilog.views.chart.IlvChart;
import ilog.views.chart.data.IlvDataSet;
import ilog.views.chart.data.IlvDataSource;
import ilog.views.chart.data.IlvDefaultDataSet;
import ilog.views.chart.data.IlvDefaultDataSource;
import ilog.views.chart.data.IlvDataSetList;
import ilog.views.chart.renderer.IlvPolylineChartRenderer;
import ilog.views.chart.IlvDoublePoints;
import javax.swing.JOptionPane;


public class DeletePortalInteractor extends IlvChartHighlightInteractor {
	
    public final static int 			SINGLE_CLICK			 	= 1;
//    private IlvPolylineChartRenderer 	trainPolylineChartRenderer	= null;
	private IlvDefaultDataSource 		selectedTrainDataSource 	= null;
	private IlvDefaultDataSource 		allTrainsDataSource 		= null;
//    public static Traject 			sel_traject 				= null;
//    private final static Traject		emptyTraject 				= new Traject();
    
    public DeletePortalInteractor( IlvDefaultDataSource allTrainsDataSource, IlvDefaultDataSource selectedTrainDataSource ) {
        this.setPickingMode( IlvChartHighlightInteractor.ITEM_PICKING );
        this.selectedTrainDataSource = selectedTrainDataSource;
        this.allTrainsDataSource = allTrainsDataSource;
    }

	public void doIt(IlvDisplayPoint highlightedPoint, boolean isHighlighted, java.awt.event.MouseEvent e) {
  			this.ShowTraject(highlightedPoint);
    }

 	public void processMouseMotionEvent(java.awt.event.MouseEvent evt) {
		super.processMouseMotionEvent(evt);
		IlvDisplayPoint	displayPoint = this.getHighlightedPoint();

        this.ShowTraject(displayPoint);
 	}

 	public void processMouseEvent(java.awt.event.MouseEvent me) {
		switch( me.getID() ) {
	        case MouseEvent.MOUSE_DRAGGED:
                break;
	        case MouseEvent.MOUSE_CLICKED:
                startOperation( me );
                if( me.getClickCount() == SINGLE_CLICK ) {
					IlvDisplayPoint point = getHighlightedPoint();

					// ask user
					if (point != null) {
							
						Object[] options = { "Ja", "Nee" };
						int result = JOptionPane.showOptionDialog(null, 
							"?", "Verwijderen?", 
							JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
							null, options, options[1]);
						if (result == 0) {
							deleteTrain( point );
						}
		            }
            	}
                endOperation( me );
            break;
            default:
            break;
    	}
    	me.consume();
    }
    

   	private void deleteTrain(  IlvDisplayPoint displayPoint ) { 
   		 
		IlvDataSet dataset = null;   		
//   		System.out.println( "deleteTrain " );

		// Get dataset
		dataset = displayPoint.getDataSet();

		if (dataset != null) {
			IlvDataSource dataSource = null;
			// find datasource containing the dataset
			if( allTrainsDataSource.containsDataSet( dataset ) ) {
				dataSource = allTrainsDataSource;
			}	else 
			if( selectedTrainDataSource.containsDataSet( dataset ) ) {
				dataSource = selectedTrainDataSource;
			}
			
			int dsIndex = dataSource.getDataSetIndex( dataset );
			if ( dsIndex >= 0 ) {
				// update screen
				dataSource.setDataSet( dsIndex, null );
				
//				tbtPanel.setTraject( emptyTraject );
				

			}
		}

 	}      
        
	public void ShowTraject( IlvDisplayPoint	displayPoint ) {
		// check if point Deleteed and a TBT available
//    	if ( (displayPoint != null) && (tbtPanel != null) ) {
			// retrieve traject from dataset
//			Traject traject = (Traject) displayPoint.getDataSet().getProperty( "traject" );
			// set this traject on tbt
//			sel_traject = traject;
//			if ( traject != null ) {
//				tbtPanel.setTraject( traject );
//          }
//    	}
//    	else {
//			tbtPanel.setTraject( emptyTraject );
//		}
    }
}