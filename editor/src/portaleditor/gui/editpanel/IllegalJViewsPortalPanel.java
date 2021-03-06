/* Generated by Together */

package portaleditor.gui.editpanel;

import gamefile.GameFileGenerator;
import ilog.views.chart.IlvAxis;
import ilog.views.chart.IlvChart;
import ilog.views.chart.IlvChartInteractor;
import ilog.views.chart.IlvColor;
import ilog.views.chart.IlvDefaultStepsDefinition;
import ilog.views.chart.IlvScale;
import ilog.views.chart.IlvStyle;
import ilog.views.chart.data.IlvDataSet;
import ilog.views.chart.data.IlvDefaultDataSet;
import ilog.views.chart.data.IlvDefaultDataSource;
import ilog.views.chart.event.ChartInteractionEvent;
import ilog.views.chart.event.ChartInteractionListener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import portalconnector.PortalConnector;
import portaleditor.data.PortalObject;
import portaleditor.data.XmlAble;
import texturing.Texturer;


public class IllegalJViewsPortalPanel extends JPanel implements Zoomable {

    final static String[] station = {
        "Bda", "Ddt", "Zwt", "Bdt", "Rdm", "Dhg", "Gda", "Ldn", "Hlm", "Adm", "Tst" };

	private 		IlvDefaultDataSource	ilvDefaultDataSourceSelectedTrain	= null;
	private 		IlvDefaultDataSource	ilvDefaultDataSourceNewTrain		= null;
	private 		IlvDefaultDataSource	ilvDefaultDataSourceAllTrains		= null;
	private 		IlvChart 				ilvChart 							= null;
	private 		JPanel 					ilvChartPanel 						= null;
    private 		YAxisScrollInteractor   yAxisScrollInteractor				= null;
    private        IlvScale 				ilvXscale							= null;
	private 		IlvChartInteractor		currInteractor						= null;
	public 		AddPortalInteractor		addPortalInteractor 				= null;
	public 		EditPortalInteractor		editTrainInteractor 				= null;
    public 		SelectPortalInteractor	selectTrainInteractor 				= null;
    public 		DeletePortalInteractor	deleteTrainInteractor 				= null;
    public 		ZoomInteractor 			zoomInteractor 						= null;

	private		JScrollBar 				scrollBarY							= null;
    private        Calendar 				calendar_from 						= null;
    private        Calendar 				calendar_to 						= null;
	private		ScrollbarYListener		scrollbarYListener					= null;
	private 		Hashtable 				checkpointMapper					= new Hashtable();
	private		XAxisGrid
	xAxisGrid							= null;
	private 		PortalTableDataModel 	model								= null;
	private final static double 			MAX_ZOOM 						= 0.03125;
	private final static double 			MIN_ZOOM 						= 1;
	private final static int 			ZOOM_IN 						= 0;
	private final static int 			ZOOM_OUT 						= 1;
	private 		int					zoomMode						= ZOOM_OUT;
	private  		double					zoomFactor 						= 1;

    public IllegalJViewsPortalPanel() throws Exception {

		this.zoomFactor = 1d/8d;
        this.setLayout( new BorderLayout() );
		this.setMinimumSize( new Dimension( 1020, 700 ) );
		this.setPreferredSize( new Dimension( 1020, 700 ) );

        createPortalDetails();
        createChart();
        customizeAxises();
        customizeScales();
		setRenderersAndDataSources();
        createTWDEditStateButtons();
//		addTrains();

    }

	public void	createTWDEditStateButtons() {
		JPanel	buttonPanel = new JPanel();

        buttonPanel.setLayout( new FlowLayout() );
		buttonPanel.setSize( new Dimension( 60, 250 ) );
		buttonPanel.setMinimumSize( new Dimension( 60, 250 ) );
		buttonPanel.setPreferredSize( new Dimension( 60, 250 ) );
		buttonPanel.setBounds( 0, 0, 60, 250 );

        final JToggleButton	createTrainButton 		= new JToggleButton( "new portal", false );
        final JToggleButton	editTrainButton 		= new JToggleButton( "edit portal", false );
        final JToggleButton	selectTrainButton 		= new JToggleButton( "sel portal", true );
        final JToggleButton	deleteTrainButton 		= new JToggleButton( "del portal", false );
        final JButton			savePortalsButton 		= new JButton( "save" );
        final JButton			loadPortalsButton 		= new JButton( "load" );
        final JButton			connectPortalsButton 	= new JButton( "Conn" );
		final JToggleButton	zoomInButton			= new JToggleButton( "+" );
		final JToggleButton	zoomOutButton			= new JToggleButton( "-" );

		connectPortalsButton.setSize( new Dimension( 60, 50 ) );
		connectPortalsButton.setMaximumSize( new Dimension( 60, 50 ) );
		connectPortalsButton.setMinimumSize( new Dimension( 60, 50 ) );
		connectPortalsButton.setPreferredSize( new Dimension( 60, 50 ) );
		connectPortalsButton.setActionCommand( "save" );
		connectPortalsButton.setFont(new Font(null,Font.BOLD, 9));

		savePortalsButton.setSize( new Dimension( 60, 50 ) );
		savePortalsButton.setMaximumSize( new Dimension( 60, 50 ) );
		savePortalsButton.setMinimumSize( new Dimension( 60, 50 ) );
		savePortalsButton.setPreferredSize( new Dimension( 60, 50 ) );
		savePortalsButton.setActionCommand( "save" );
		savePortalsButton.setFont(new Font(null,Font.BOLD, 9));

		loadPortalsButton.setSize( new Dimension( 60, 50 ) );
		loadPortalsButton.setMaximumSize( new Dimension( 60, 50 ) );
		loadPortalsButton.setMinimumSize( new Dimension( 60, 50 ) );
		loadPortalsButton.setPreferredSize( new Dimension( 60, 50 ) );
		loadPortalsButton.setActionCommand( "load" );
		loadPortalsButton.setFont(new Font(null,Font.BOLD, 9));

		createTrainButton.setSize( new Dimension( 60, 50 ) );
		createTrainButton.setMaximumSize( new Dimension( 60, 50 ) );
		createTrainButton.setMinimumSize( new Dimension( 60, 50 ) );
		createTrainButton.setPreferredSize( new Dimension( 60, 50 ) );
		createTrainButton.setActionCommand( "createMode" );
		createTrainButton.setFont(new Font(null,Font.BOLD, 9));

		editTrainButton.setSize( new Dimension( 60, 50 ) );
		editTrainButton.setMaximumSize( new Dimension( 60, 50 ) );
		editTrainButton.setMinimumSize( new Dimension( 60, 50 ) );
		editTrainButton.setPreferredSize( new Dimension( 60, 50 ) );
		editTrainButton.setActionCommand( "editMode" );
		editTrainButton.setFont(new Font(null,Font.BOLD, 9));

		selectTrainButton.setSize( new Dimension( 60, 50 ) );
		selectTrainButton.setMaximumSize( new Dimension( 60, 50 ) );
		selectTrainButton.setMinimumSize( new Dimension( 60, 50 ) );
		selectTrainButton.setPreferredSize( new Dimension( 60, 50 ) );
		selectTrainButton.setActionCommand( "selectMode" );
		selectTrainButton.setFont(new Font(null,Font.BOLD, 9));

		deleteTrainButton.setSize( new Dimension( 60, 50 ) );
		deleteTrainButton.setMaximumSize( new Dimension( 60, 50 ) );
		deleteTrainButton.setMinimumSize( new Dimension( 60, 50 ) );
		deleteTrainButton.setPreferredSize( new Dimension( 60, 50 ) );
		deleteTrainButton.setActionCommand( "deleteMode" );
		deleteTrainButton.setFont(new Font(null,Font.BOLD, 9));

		zoomInButton.setSize( new Dimension( 60, 50 ) );
		zoomInButton.setMaximumSize( new Dimension( 60, 50 ) );
		zoomInButton.setMinimumSize( new Dimension( 60, 50 ) );
		zoomInButton.setPreferredSize( new Dimension( 60, 50 ) );
		zoomInButton.setActionCommand( "zoomIn" );
		zoomInButton.setFont(new Font(null,Font.BOLD, 10));

		zoomOutButton.setSize( new Dimension( 60, 50 ) );
		zoomOutButton.setMaximumSize( new Dimension( 60, 50 ) );
		zoomOutButton.setMinimumSize( new Dimension( 60, 50 ) );
		zoomOutButton.setPreferredSize( new Dimension( 60, 50 ) );
		zoomOutButton.setActionCommand( "zoomOut" );
		zoomOutButton.setFont(new Font(null,Font.BOLD, 10));

        buttonPanel.add( createTrainButton, BorderLayout.CENTER );
        buttonPanel.add( editTrainButton, BorderLayout.CENTER );
        buttonPanel.add( selectTrainButton, BorderLayout.CENTER );
        buttonPanel.add( deleteTrainButton, BorderLayout.CENTER );
        buttonPanel.add( deleteTrainButton, BorderLayout.CENTER );
        buttonPanel.add( zoomInButton, BorderLayout.CENTER );
        buttonPanel.add( zoomOutButton, BorderLayout.CENTER );
        buttonPanel.add( savePortalsButton, BorderLayout.CENTER );
        buttonPanel.add( loadPortalsButton, BorderLayout.CENTER );
        buttonPanel.add( connectPortalsButton, BorderLayout.CENTER );
		
		// add interactor setInteractorMode
		final	IlvChartInteractor 		final_editTrainInteractor = editTrainInteractor;
		final	IlvChartInteractor 		final_selectTrainInteractor = selectTrainInteractor;
		final	IlvChartInteractor 		final_deleteTrainInteractor = deleteTrainInteractor;



		connectPortalsButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				connect();
            }
   		} );
		savePortalsButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				save();
            }
   		} );
		loadPortalsButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				load();
            }
   		} );

		zoomInButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				setZoomMode(ZOOM_IN);
				setInteractorMode( zoomInteractor );
				
                zoomOutButton.setSelected( false );
                zoomInButton.setSelected( true );
                deleteTrainButton.setSelected( false  );
                selectTrainButton.setSelected( false );
                editTrainButton.setSelected  ( false );
                createTrainButton.setSelected( false );
            }
   		} );

		zoomOutButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				setZoomMode(ZOOM_OUT);
				setInteractorMode( zoomInteractor );
				
                zoomOutButton.setSelected( true );
                zoomInButton.setSelected( false );
                deleteTrainButton.setSelected( false  );
                selectTrainButton.setSelected( false );
                editTrainButton.setSelected  ( false );
                createTrainButton.setSelected( false );
            }
   		} );

		createTrainButton.addActionListener( new ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				// detoggle other buttons
                zoomOutButton.setSelected( false );
                zoomInButton.setSelected( false );
                deleteTrainButton.setSelected( false );
                selectTrainButton.setSelected( false );
                editTrainButton.setSelected  ( false );
                createTrainButton.setSelected( true  );
				
				setInteractorMode( addPortalInteractor );
				
            }
   		} );

		selectTrainButton.addActionListener( new ActionListener() {
			private IlvChartInteractor inner_selectTrainInteractor = final_selectTrainInteractor;
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				// detoggle other buttons
                zoomOutButton.setSelected( false );
                zoomInButton.setSelected( false );
                deleteTrainButton.setSelected( false );
                selectTrainButton.setSelected( true  );
                editTrainButton.setSelected  ( false );
                createTrainButton.setSelected( false );

				setInteractorMode( inner_selectTrainInteractor );

            }
   		} );

		editTrainButton.addActionListener( new ActionListener() {
			private IlvChartInteractor inner_editTrainInteractor = final_editTrainInteractor;
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				// detoggle other buttons
                zoomOutButton.setSelected( false );
                zoomInButton.setSelected( false );
                deleteTrainButton.setSelected( false );
                selectTrainButton.setSelected( false );
                editTrainButton.setSelected  ( true  );
                createTrainButton.setSelected( false );

				setInteractorMode( inner_editTrainInteractor );
            }
   		} );

		deleteTrainButton.addActionListener( new ActionListener() {
			private IlvChartInteractor inner_deleteTrainInteractor = final_deleteTrainInteractor;
            public void actionPerformed(java.awt.event.ActionEvent ae) {
				// detoggle other buttons
                zoomOutButton.setSelected( false );
                zoomInButton.setSelected( false );
                deleteTrainButton.setSelected( true  );
                selectTrainButton.setSelected( false );
                editTrainButton.setSelected  ( false );
                createTrainButton.setSelected( false );

				setInteractorMode( inner_deleteTrainInteractor );

            }
   		} );

        this.add( buttonPanel, BorderLayout.EAST );
    }

    public void createPortalDetails() {
		JPanel	buttonPanel = new JPanel();
//		JPanel	buttonPanel2 = new JPanel();
		buttonPanel.setSize( new Dimension( 180, 700 ) );
		buttonPanel.setMaximumSize( new Dimension( 180, 700 ) );
		buttonPanel.setPreferredSize( new Dimension( 180, 700 ) );
		buttonPanel.setBounds( 0, 0, 180, 700 );

        buttonPanel.setLayout( new BorderLayout() );
        JTable table = new JTable() {
        	/**
			 * @see javax.swing.JTable#getCellEditor(int, int)
			 */
			public TableCellEditor getCellEditor(int row, int column) {
				if( column == 0 ) {
					return super.getCellEditor(row, column);
				}	else {
					Object type = model.getValueAt(row, 1);
System.out.println( "ROW: " + row + " CONTAINS: " + type );					
					if( type instanceof Boolean ) {
						return new DefaultCellEditor(new JCheckBox());
					}	else {
						return new DefaultCellEditor(new JTextField());
					}
					
				}
			}
			
			/**
			 * @see javax.swing.JTable#getCellRenderer(int, int)
			 */
			public TableCellRenderer getCellRenderer(int row, int column) {
System.out.println( "ROW: " + row );					
				if( column == 0 ) {
					return super.getCellRenderer(row, column);
				}	else {
					Object type = model.getValueAt(row, 1);
//System.out.println( "ROW: " + row + " CONTAINS: " + type );					
					if( type instanceof Boolean ) {
						TableCellRenderer renderer = new TableCellRenderer() {
							/**
							 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(JTable, Object, boolean, boolean, int, int)
							 */
							public Component getTableCellRendererComponent(
								JTable arg0,
								Object arg1,
								boolean arg2,
								boolean arg3,
								int arg4,
								int arg5) {
								JCheckBox cb = new JCheckBox("", ((Boolean)arg1).booleanValue());
								return cb;
							}

						};
//						renderer.set
						return renderer;
					}	else {
						return super.getCellRenderer(row, column);
					}
					
				}
			}


        };
        model = new PortalTableDataModel(table);
        table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);

		buttonPanel.add(table, BorderLayout.CENTER);
        this.add( buttonPanel, BorderLayout.WEST );
    }

    public void createChart() {
		ilvChartPanel = new JPanel();
        ilvChartPanel.setLayout( new BorderLayout() );
		
		ilvChart = new IlvChart();
		ilvChart.getChartArea().setPlotBackground(new Color[] {IlvColor.darkSlateGray}, false);
		ilvChart.setSize( new Dimension( 700, 700 ) );
		ilvChart.setMaximumSize( new Dimension( 700, 700 ) );
		ilvChart.setPreferredSize( new Dimension( 700, 700 ) );
		ilvChart.setBounds( 0, 0, 700, 700 );

		ilvChartPanel.add(ilvChart, BorderLayout.CENTER);
        this.add( ilvChartPanel, BorderLayout.CENTER );
    }

	public void customizeAxises() throws Exception {
        if( ilvChart != null ) {
			// get X axis to customize
        	IlvAxis ilvXAxis = ilvChart.getXAxis();

			// set datarange (now: 1000 units)
            ilvXAxis.setDataRange( -32767, 32767 );

            // set Visible datarange
            ilvXAxis.setVisibleRange( -32767 * zoomFactor, 32767 * zoomFactor );

            // set scrollbar
            JScrollBar xScrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
			ilvChart.setXScrollBar( xScrollBar );
			ilvChart.setFooter( xScrollBar );

			// get Y axis
            IlvAxis ilvYAxis = ilvChart.getYAxis(0);

            // set scale for this axis

			ilvYAxis.setDataRange( -32767, 32767 );
			ilvYAxis.setVisibleRange( -32767 * zoomFactor, 32767 * zoomFactor );

            // create Y scrollbar (that would be a working version)
			yAxisScrollInteractor = new YAxisScrollInteractor( 0, YAxisScrollInteractor.NEGATIVE_DIR,
																  YAxisScrollInteractor.POSITIVE_DIR,
                                                                  1 );
			ilvChart.addInteractor(yAxisScrollInteractor);
//yAxisScrollInteractor.addChartInteractionListener(new ChartInteractionListener() {
//	public void interactionPerformed(ChartInteractionEvent arg0) {
		// TODO Auto-generated method stub
//		arg0.
//	}
//});

            scrollBarY 			= new JScrollBar( JScrollBar.VERTICAL, 0, 1, 0, 2001 - (int) Math.floor(2000 * zoomFactor) );
            scrollBarY.setValue( scrollBarY.getMaximum() );

            scrollbarYListener	= new ScrollbarYListener( scrollBarY.getMaximum(), yAxisScrollInteractor );
			scrollBarY.addAdjustmentListener( scrollbarYListener );

			ilvChartPanel.add( scrollBarY, BorderLayout.EAST );

            // add additional axis for 1 hour steps
        }
        else {
            throw new Exception( "null Chart" );
        }
    }

    public void customizeScales() throws Exception{
        if( ilvChart != null ) {
        	// note that substep must be set to make custom zoom method work
			IlvDefaultStepsDefinition ilvDefaultStepsDefinition_x = new IlvDefaultStepsDefinition();
			ilvDefaultStepsDefinition_x.setStepUnit( 256 );

			//  get x scale
            ilvXscale = ilvChart.getXScale();

			ilvXscale.setLabelVisible(false);
			ilvXscale.setMajorTickVisible(false);
			ilvXscale.setMinorTickVisible(false);
			ilvXscale.setStepsDefinition( ilvDefaultStepsDefinition_x );
			ilvChart.getXGrid().setMajorLineVisible(true);
			ilvChart.getXGrid().setMinorLineVisible(false);

			// note that substep must be set to make custom zoom method work
			IlvDefaultStepsDefinition ilvDefaultStepsDefinition_y = new IlvDefaultStepsDefinition();
			ilvDefaultStepsDefinition_y.setStepUnit( 256 );

            // get Y axis
            IlvScale ilvYscale = ilvChart.getYScale(0);

            // set grid step definition
			ilvYscale.setLabelVisible(false);
			ilvYscale.setMajorTickVisible(false);
			ilvYscale.setMinorTickVisible(false);
			ilvYscale.setStepsDefinition( ilvDefaultStepsDefinition_y );
			ilvChart.getYGrid(0).setMajorLineVisible(true);
			ilvChart.getYGrid(0).setMinorLineVisible(false);

        }
        else {
            throw new Exception( "null Chart" );
        }
    }

    public void setRenderersAndDataSources() throws Exception{
        if( ilvChart != null ) {

			// create renderers
			PortalPolylineChartRenderer allTrainsRenderer 		= new PortalPolylineChartRenderer(); // use derfault TWD style
			PortalPolylineChartRenderer newTrainRenderer 		= new PortalPolylineChartRenderer(); // use derfault TWD style
			PortalPolylineChartRenderer selectedTrainRenderer 	= new PortalPolylineChartRenderer(new IlvStyle( 2.0f, IlvColor.greenYellow ));

            // create datasources
			ilvDefaultDataSourceNewTrain 		= new IlvDefaultDataSource();
    		ilvDefaultDataSourceAllTrains 		= new IlvDefaultDataSource();
    		ilvDefaultDataSourceSelectedTrain 	= new IlvDefaultDataSource();

			// add datasources renderers
			newTrainRenderer.setDataSource( ilvDefaultDataSourceNewTrain );
			allTrainsRenderer.setDataSource( ilvDefaultDataSourceAllTrains );
			selectedTrainRenderer.setDataSource( ilvDefaultDataSourceSelectedTrain );

			// add renderes to chart
			ilvChart.addRenderer( selectedTrainRenderer );
			ilvChart.addRenderer( newTrainRenderer );
			ilvChart.addRenderer( allTrainsRenderer );

			// create interactors
			editTrainInteractor = new EditPortalInteractor( this, selectedTrainRenderer);
			addPortalInteractor = new AddPortalInteractor( this, selectedTrainRenderer );
            selectTrainInteractor = new SelectPortalInteractor( ilvDefaultDataSourceAllTrains, ilvDefaultDataSourceSelectedTrain );
			deleteTrainInteractor = new DeletePortalInteractor( ilvDefaultDataSourceAllTrains, ilvDefaultDataSourceSelectedTrain );
			zoomInteractor = new ZoomInteractor( this );

			this.setInteractorMode( selectTrainInteractor );

        }	else {
            throw new Exception( "null Chart" );
        }
    }

	public void repaint() {
		super.repaint();
    }

	public final void setInteractorMode( ilog.views.chart.IlvChartInteractor interactor ) {
		if( currInteractor != null ) {
			ilvChart.removeInteractor( currInteractor );
        }

		ilvChart.addInteractor( interactor );
	    currInteractor = interactor;

    }






    public void scroll( int direction ) {
        yAxisScrollInteractor.scroll( direction );
    }

    public void draw() {
        this.repaint();
    }

	
	public void zoom() {
		// get Y axis
		IlvAxis ilvYAxis = ilvChart.getYAxis(0);
		IlvAxis ilvXAxis = ilvChart.getXAxis();

		double maxVisible_y 		= ilvYAxis.getVisibleMax();
		double minVisible_y 		= ilvYAxis.getVisibleMin();
        double diff_y	  			= (maxVisible_y - minVisible_y);
        double middle_y	  		= minVisible_y + diff_y/2d;

		double maxVisible_x 		= ilvYAxis.getVisibleMax();
		double minVisible_x 		= ilvYAxis.getVisibleMin();
        double diff_x	  			= (maxVisible_x - minVisible_x);
        double middle_x	  		= minVisible_x + diff_x/2d;

		this.doZoom( middle_x, middle_y );
	}
	
	public void zoom( double x, double y ) {
		if( zoomMode == ZOOM_IN ) {
			doZoomIn();
		}	else {
			doZoomOut();
		}
		doZoom( x, y );
	}
		
	private void doZoom( double middle_x, double middle_y) {
		// get Y axis
		IlvAxis ilvYAxis = ilvChart.getYAxis(0);
		IlvAxis ilvXAxis = ilvChart.getXAxis();

		double maxData_y			= ilvYAxis.getDataMax();
		double minData_y			= ilvYAxis.getDataMin();
        double dataRange_y			= maxData_y - minData_y;

        double zoomRange_y			= 0.5d * dataRange_y * zoomFactor;
        double newMinVisible_y		= middle_y - zoomRange_y;
        double newMaxVisible_y		= middle_y + zoomRange_y;
        double newVisibleRange_y	= newMaxVisible_y - newMinVisible_y;

		double maxData_x			= ilvYAxis.getDataMax();
		double minData_x			= ilvYAxis.getDataMin();
        double dataRange_x			= maxData_x - minData_x;

        double zoomRange_x			= 0.5d * dataRange_x * zoomFactor;
        double newMinVisible_x		= middle_x - zoomRange_x;
        double newMaxVisible_x		= middle_x + zoomRange_x;
        double newVisibleRange_x	= newMaxVisible_x - newMinVisible_x;


		// check if not zoomed past chart
		if ( newMinVisible_y < minData_y ) {
			newMinVisible_y = minData_y;
			newMaxVisible_y = minData_y + newVisibleRange_y;
        }	else if ( newMaxVisible_y > maxData_y ) {
			newMaxVisible_y = maxData_y;
			newMinVisible_y = maxData_y - newVisibleRange_y;
        }

		if ( newMinVisible_x < minData_x ) {
			newMinVisible_x = minData_x;
			newMaxVisible_x = minData_x + newVisibleRange_x;
        }	else if ( newMaxVisible_x > maxData_x ) {
			newMaxVisible_x = maxData_x;
			newMinVisible_x = maxData_x - newVisibleRange_x;
        }

        double currScrollRange_y	= dataRange_y - newVisibleRange_y;
		if ( currScrollRange_y > 0 ) {
	        double	ratio_y = (newMinVisible_y-minData_y)/currScrollRange_y;
			double y_step = ilvChart.getYScale(0).getStepsDefinition().incrementStep(0);
	
	        int maximumScrollBarValue 	= (int) Math.ceil( currScrollRange_y/y_step );
	        int newScrollBarValue		= (int) Math.round(maximumScrollBarValue * (1-ratio_y)) + 1;
	
			scrollbarYListener.setPreviousValue( newScrollBarValue );
	        scrollBarY.setMaximum( maximumScrollBarValue + 2 );
	        scrollBarY.setValue( newScrollBarValue );
	        scrollBarY.repaint();
        }	else {
			scrollbarYListener.setPreviousValue( 0 );
	        scrollBarY.setMaximum( 0 );
	        scrollBarY.setValue( 0 );
	        scrollBarY.repaint();
		}

		
		ilvYAxis.setVisibleRange( newMinVisible_y, newMaxVisible_y );
		ilvXAxis.setVisibleRange( newMinVisible_x, newMaxVisible_x );

    }

	public void zoomIn() {
		doZoomIn();
        zoom();
	}


	public void zoomOut() {
		doZoomOut();
        zoom();
	}

	public void zoomIn( double x, double y ) {
		doZoomIn();
        zoom( x, y );
	}


	public void zoomOut( double x, double y ) {
		doZoomOut();
        zoom( x, y );
	}

	private void doZoomIn() {
		zoomFactor /= 2d;
        if( zoomFactor < MAX_ZOOM ) {
            zoomFactor = MAX_ZOOM;
        }
	}

	private void doZoomOut() {
		zoomFactor *= 2d;
        if( zoomFactor > MIN_ZOOM ) {
            zoomFactor = MIN_ZOOM;
        }
	}
	
	public void setZoomMode( int zoomMode ) {
		this.zoomMode = zoomMode;
	}



    

	/**
	 * Returns the model.
	 * @return PortalTableDataModel
	 */
	public void setPortalObject(PortalObject object) {
		model.setPortalObject(object);		
		selectPortalObject(object);
	}

	/**
	 * Returns the ilvChart.
	 * @return IlvChart
	 */
	public IlvChart getIlvChart() {
		return ilvChart;
	}


	public void save() {
		try {
			File file = new File("portals.xml");
			file.createNewFile();
			FileWriter writer = new FileWriter(file);
			
			Iterator dataSets;
			dataSets = this.ilvDefaultDataSourceAllTrains.getDataSetIterator();
			
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
			writer.write("<portals>\r\n");
			while(dataSets.hasNext()) {
				IlvDataSet dataSet = (IlvDataSet) dataSets.next();
				XmlAble object = (XmlAble) dataSet.getProperty("PortalObject");
				writer.write(object.getXml(1).toString());
			}
			dataSets = this.ilvDefaultDataSourceSelectedTrain.getDataSetIterator();
			
			while(dataSets.hasNext()) {
				IlvDataSet dataSet = (IlvDataSet) dataSets.next();
				XmlAble object = (XmlAble) dataSet.getProperty("PortalObject");
				writer.write(object.getXml(1).toString());
			}
			dataSets = this.ilvDefaultDataSourceNewTrain.getDataSetIterator();
			
			while(dataSets.hasNext()) {
				IlvDataSet dataSet = (IlvDataSet) dataSets.next();
				XmlAble object = (XmlAble) dataSet.getProperty("PortalObject");
				writer.write(object.getXml(1).toString());
			}

			writer.write("</portals>\r\n");
			writer.close();
			
		}	catch( Exception e ) {
			e.printStackTrace();
		}	
	}
	
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
				add( childDocument );
				
			}

		}	catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void add(Document portalObjectDocument) {
		IlvDefaultDataSet			newPortalDataSet;
		PortalObject portal;
		
		// create new portal object
		portal = new PortalObject();
		
		portal.setXml(portalObjectDocument);

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
		
		this.ilvDefaultDataSourceAllTrains.addDataSet(newPortalDataSet);		
	}
	
	public void selectPortalObject( PortalObject object ) {
		// check if point selected and a TBT available
    	if ( (object != null) ) {
    		// move dataset to selectedTrains datasource
    		IlvDataSet dataset = object.getIlvDataSet();

    		// Constraint only one train can be selected for the time 
    		int numSelectedTrains = ilvDefaultDataSourceSelectedTrain.getDataSetCount();
    		for( int selectedTrainsCnt=0; selectedTrainsCnt<numSelectedTrains; selectedTrainsCnt++ ) {
    			IlvDataSet removedDataset = ilvDefaultDataSourceSelectedTrain.getDataSet(0);

				// if this dataset already was selected, keep it... 
				// will be removed in next iteration
//				if( removedDataset != dataset ) { 
					ilvDefaultDataSourceSelectedTrain.setDataSet( 0, null);    			
	    			ilvDefaultDataSourceAllTrains.addDataSet(removedDataset);
//				}
    		}

    		
    		// already selected? deselect and move to allTrains dataset
//    		if( ilvDefaultDataSourceSelectedTrain.containsDataSet(dataset) ) {
//				ilvDefaultDataSourceAllTrains.addDataSet(dataset);
//				ilvDefaultDataSourceSelectedTrain.setDataSet( ilvDefaultDataSourceSelectedTrain.getDataSetIndex(dataset), null);    			
  //  		} else {
    			// asume not selected yet, move to selectedTrains dataset
				ilvDefaultDataSourceSelectedTrain.addDataSet(dataset);
				ilvDefaultDataSourceAllTrains.setDataSet( ilvDefaultDataSourceAllTrains.getDataSetIndex(dataset), null);    			
//    		}
    		
    	}	else {
	        
	        // move selected trains to all trains dataset
	        while( ilvDefaultDataSourceSelectedTrain.getDataSetCount() > 0 ) {
    			IlvDataSet dataset = ilvDefaultDataSourceSelectedTrain.getDataSet(0);
				ilvDefaultDataSourceSelectedTrain.setDataSet( 0, null);    			
    			ilvDefaultDataSourceAllTrains.addDataSet(dataset);
    		}
    	}

    }

	public void connect() {
		PortalConnector pc = new PortalConnector();
		System.out.println("loading...");
		pc.load();
		System.out.println("connection solving...");
		pc.solve();
		
		Texturer	texturer = new Texturer();
		System.out.println("loading for texturing...");
		texturer.load();
		System.out.println("connection texturing...");
		texturer.solve();
		
		GameFileGenerator gen = new GameFileGenerator();
		System.out.println("loading for map gen...");
		gen.load();
		System.out.println("Generating map...");
		gen.save();
		
	}	
}
