package view;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.table.JTableHeader;

import model.Company;
import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import model.SpadItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import javax.swing.JLabel;

import com.toedter.calendar.JDateChooser;

/**
 * @author MPI
 * @version 23.05.2014/1.2
 */
public class MainGui {

	private JFrame frmSpadViewer;
	private JTable tableData;
	private IDataStrategy ds;
	private SpadTableModel model;
	private ArrayList<SpadItem> data;

	private com.toedter.calendar.JDateChooser dchFrom;
	private com.toedter.calendar.JDateChooser dchTo;

	public static final String[] columnNames = { "Name", "avPrice", "avVolume",
			"dPriceMin", "dPriceMax", "dPriceAvg" };

	public MainGui(IDataStrategy ds) {
		super();
		try {
			this.ds = ds;
			this.data = this.ds.getSpadItems(null, null);
			this.model = new SpadTableModel(MainGui.columnNames, this.data);
			initialize();
			this.frmSpadViewer.setVisible(true);
		} catch (SQLException e) {
			// TODO warning dialog
			e.printStackTrace();
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				IDataStrategy ds;
				try {
					ds = new DerbyStrategy(new DerbyDatabase());
					MainGui window = new MainGui(ds);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws SQLException
	 */
	private void initialize() throws SQLException {
		frmSpadViewer = new JFrame();
		frmSpadViewer.setTitle("SPAD viewer");
		frmSpadViewer.setBounds(100, 100, 800, 600);
		frmSpadViewer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmSpadViewer.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem menu_download_man = new JMenuItem("Manually download");
		mnFile.add(menu_download_man);

		JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
		mnFile.add(mntmNewMenuItem);

		JMenuItem menu_exit = new JMenuItem("Exit");
		menu_exit.addActionListener(new ExitListener());
		mnFile.add(menu_exit);

		JMenu mnStix = new JMenu("Stix");
		menuBar.add(mnStix);

		JMenuItem menu_about = new JMenuItem("About");
		menu_about.addActionListener(new AboutListener());
		mnStix.add(menu_about);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmSpadViewer.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panel_data = new JPanel();
		tabbedPane.addTab("Data", null, panel_data, null);
		panel_data.setLayout(new BorderLayout(0, 0));

		JPanel data_filter = new JPanel();
		panel_data.add(data_filter, BorderLayout.NORTH);

		JButton btn_refresh = new JButton("Refresh");
		btn_refresh.addActionListener(new FilterListener());
		dchFrom = new JDateChooser();
		dchTo = new JDateChooser();
		Date today = new Date(Calendar.getInstance().getTimeInMillis());
		dchFrom.setDate(today);
		dchTo.setDate(today);
		JLabel labelFilterFrom = new JLabel("From:");
		JLabel labelFilterTo = new JLabel("To:");
		data_filter.add(labelFilterFrom);
		data_filter.add(dchFrom);
		data_filter.add(labelFilterTo);
		data_filter.add(dchTo);
		data_filter.add(btn_refresh);

		tableData = new JTable(model);
		JTableHeader header = tableData.getTableHeader();
		header.setBackground(Color.yellow);
		panel_data.add(tableData, BorderLayout.CENTER);

		JPanel data_info = new JPanel();
		panel_data.add(data_info, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("Last date in db:");
		data_info.add(lblNewLabel);

		JLabel label_last_date = new JLabel(ds.getSpadLastDate().toString());
		data_info.add(label_last_date);

		JPanel panel_graph = new JPanel();
		tabbedPane.addTab("Graph", null, panel_graph, null);
	}

	private class FilterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			java.sql.Date fromDate, toDate;
			try {
				SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy");
				java.util.Date parsedFrom = (java.util.Date) format
						.parse(DateFormat.getDateInstance().format(
								dchFrom.getDate()));
				java.util.Date parsedTo = (java.util.Date) format
						.parse(DateFormat.getDateInstance().format(
								dchTo.getDate()));
				fromDate = new java.sql.Date(parsedFrom.getTime());
				toDate = new java.sql.Date(parsedTo.getTime());
			} catch (ParseException ex) {
				fromDate = null;
				toDate = null;
			}
			try {
				data = ds.getSpadItems(fromDate, toDate);
				model = new SpadTableModel(MainGui.columnNames, data);
				tableData.setModel(model);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}
	
	private class ExitListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
		
	}
	
	private class AboutListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			AboutFrame.main(null);
		}
		
	}
}
