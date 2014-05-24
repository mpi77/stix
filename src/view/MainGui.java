package view;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;

import model.Company;
import model.CsvParser;
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

import core.Downloader;

import javax.swing.ListSelectionModel;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;

/**
 * @author MPI
 * @version 24.05.2014/1.3
 */
public class MainGui {

	private JFrame frmSpadViewer;
	private JTable tableData;
	private JLabel label_status;
	private JTabbedPane tabbedPane;
	private com.toedter.calendar.JDateChooser dchFrom;
	private com.toedter.calendar.JDateChooser dchTo;

	private IDataStrategy ds;
	private SpadTableModel model;
	private ArrayList<SpadItem> data;

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
			JOptionPane.showMessageDialog(MainGui.this.frmSpadViewer,
					e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void setStatusLabel(String text){
		label_status.setText(text);
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
		menu_download_man.addActionListener(new ManualDownloaderListener());
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

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
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
		dchFrom.setDate(ds.getSpadFirstDate());
		dchTo.setDate(today);
		JLabel labelFilterFrom = new JLabel("From:");
		JLabel labelFilterTo = new JLabel("To:");
		data_filter.add(labelFilterFrom);
		data_filter.add(dchFrom);
		data_filter.add(labelFilterTo);
		data_filter.add(dchTo);
		data_filter.add(btn_refresh);

		JPanel data_table = new JPanel();
		panel_data.add(data_table, BorderLayout.CENTER);
		data_table.setLayout(new BorderLayout(0, 0));

		tableData = new JTable(model);
		tableData.setShowVerticalLines(false);
		tableData.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel ls = tableData.getSelectionModel();
		ls.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ls.addListSelectionListener(new SelectRowListener());
		data_table.add(tableData, BorderLayout.CENTER);

		JTableHeader header = tableData.getTableHeader();
		header.setBackground(Color.yellow);
		data_table.add(header, BorderLayout.NORTH);

		JPanel data_info = new JPanel();
		panel_data.add(data_info, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("Last date in db:");
		data_info.add(lblNewLabel);

		JLabel label_last_date = new JLabel(ds.getSpadLastDate().toString());
		data_info.add(label_last_date);

		JPanel panel_graph = new JPanel();
		tabbedPane.addTab("Graph", null, panel_graph, null);

		label_status = new JLabel(" ");
		frmSpadViewer.getContentPane().add(label_status, BorderLayout.SOUTH);
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
				JOptionPane.showMessageDialog(MainGui.this.frmSpadViewer,
						e1.getMessage(), "SQL EXCEPTION",
						JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}

	}

	private class ExitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}

	}

	private class AboutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AboutFrame.main(null);
		}

	}
	
	private class SelectRowListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int[] selR = tableData.getSelectedRows();
			tabbedPane.setSelectedIndex(1);
			System.out.println(data.get(selR[0]));
		}
		
	}

	private class ManualDownloaderListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Thread t = new Thread(new Downloader(ds, new CsvParser(),
					Downloader.BCPP_REMOTE_URL, Downloader.BCPP_LOCAL_PATH, MainGui.this));
			t.start();
			MainGui.this.setStatusLabel("Downloading...");
		}

	}
}
