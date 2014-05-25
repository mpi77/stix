package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.List;

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
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import model.CsvParser;
import model.DerbyDatabase;
import model.DerbyStrategy;
import model.IDataStrategy;
import model.Item;
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
import java.util.TimerTask;

import javax.swing.JLabel;

import com.toedter.calendar.JDateChooser;

import core.Downloader;

import javax.swing.ListSelectionModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * @author MPI
 * @version 25.05.2014/1.7
 */
public class MainGui {

	private JFrame frmSpadViewer;
	private JTable tableData;
	private JLabel label_status;
	private JLabel label_last_date;
	private JTabbedPane tabbedPane;
	private JPanel panel_data;
	private JPanel panel_graph;
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
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public void setStatusLabel(String text) {
		label_status.setText(text);
	}

	public void setLastDateLabel(String text) {
		label_last_date.setText(text);
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
					JOptionPane.showMessageDialog(null, e.getMessage(),
							"Error", JOptionPane.ERROR_MESSAGE);
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
		tabbedPane.addChangeListener(new TabbedChangeListener());
		frmSpadViewer.getContentPane().add(tabbedPane, BorderLayout.CENTER);

		panel_data = new JPanel();
		tabbedPane.addTab("Data", null, panel_data, null);
		panel_data.setLayout(new BorderLayout(0, 0));

		JPanel data_filter = new JPanel();
		panel_data.add(data_filter, BorderLayout.NORTH);

		JButton btn_refresh = new JButton("Refresh");
		btn_refresh.addActionListener(new FilterListener());
		dchFrom = new JDateChooser();
		dchTo = new JDateChooser();
		setDefaultFilter();
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
		TableColumnModel tcm = tableData.getColumnModel();
		TableColumn tm = tcm.getColumn(0);
		tm.setCellRenderer(new RecommendedTableCellRenderer(null));
		data_table.add(tableData, BorderLayout.CENTER);

		JTableHeader header = tableData.getTableHeader();
		header.setBackground(Color.yellow);
		data_table.add(header, BorderLayout.NORTH);

		JPanel data_info = new JPanel();
		panel_data.add(data_info, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("Last date in db:");
		data_info.add(lblNewLabel);

		Date dd = ds.getSpadLastDate();
		label_last_date = new JLabel(((dd != null) ? ds.getSpadLastDate()
				.toString() : ""));
		data_info.add(label_last_date);

		panel_graph = new JPanel();
		tabbedPane.addTab("Graph", null, panel_graph, null);
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, false);

		JPanel panel_adw = new JPanel();
		tabbedPane.addTab("AutoDownloader", null, panel_adw, null);

		label_status = new JLabel(" ");
		frmSpadViewer.getContentPane().add(label_status, BorderLayout.SOUTH);
	}

	private DefaultCategoryDataset createDataset(ArrayList<Item> al) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < al.size(); i++) {
			dataset.addValue((al.get(i).getOpen() + al.get(i).getClose()) / 2,
					"price", al.get(i).getDate().toString());
		}
		return dataset;
	}

	private JFreeChart createChart(final DefaultCategoryDataset dataset, String companyId) {
		java.sql.Date fromDate = parseFromDate(), toDate = parseToDate();
		JFreeChart chart = ChartFactory.createLineChart(companyId + " ("
				+ fromDate + " - " + toDate + ")", "Time", "Price", dataset,
				PlotOrientation.VERTICAL, true, true, false);
		final CategoryPlot plot = chart.getCategoryPlot();
		// ValueAxis range = plot.getRangeAxis();
		// range.setVisible(false);
		final CategoryAxis categoryAxis = (CategoryAxis) plot.getDomainAxis();
		categoryAxis.setAxisLineVisible(true);
		categoryAxis.setTickMarksVisible(false);
		// categoryAxis.setVisible(false);

		return chart;
	}

	private void makeChart(String companyId) {
		ArrayList<Item> al;
		try {
			al = ds.getItems(parseFromDate(), parseToDate(), companyId);
			final DefaultCategoryDataset dataset = createDataset(al);
			final JFreeChart chart = createChart(dataset, companyId);
			final ChartPanel chartPanel = new ChartPanel(chart);
			// chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
			panel_graph.removeAll();
			panel_graph.add(chartPanel);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private Date parseFromDate() {
		java.sql.Date fromDate;
		try {
			SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy");
			java.util.Date parsedFrom = (java.util.Date) format
					.parse(DateFormat.getDateInstance().format(
							dchFrom.getDate()));
			fromDate = new java.sql.Date(parsedFrom.getTime());
		} catch (NullPointerException | ParseException ex) {
			fromDate = null;
		}
		return fromDate;
	}

	private Date parseToDate() {
		java.sql.Date toDate;
		try {
			SimpleDateFormat format = new SimpleDateFormat("d.M.yyyy");
			java.util.Date parsedFrom = (java.util.Date) format
					.parse(DateFormat.getDateInstance().format(dchTo.getDate()));
			toDate = new java.sql.Date(parsedFrom.getTime());
		} catch (NullPointerException | ParseException ex) {
			toDate = null;
		}
		return toDate;
	}

	public void refreshTable() throws SQLException {
		java.sql.Date fromDate = parseFromDate(), toDate = parseToDate();
		data = ds.getSpadItems(fromDate, toDate);
		model = new SpadTableModel(MainGui.columnNames, data);
		tableData.setModel(model);
		String[] recom = ds.getPurchaseRecommendation();
		ArrayList<Integer> rec = new ArrayList<Integer>();
		for(int i = 0; i < data.size(); i++){
			for(int j = 0; j < recom.length; j++){
				if(data.get(i).getCompanyId().equals(recom[j])){
					rec.add(i);
					break;
				}
			}
		}
		TableColumnModel tcm = tableData.getColumnModel();
		TableColumn tm = tcm.getColumn(0);
		tm.setCellRenderer(new RecommendedTableCellRenderer(rec));
	}

	public void setDefaultFilter() throws SQLException {
		Date today = new Date(Calendar.getInstance().getTimeInMillis());
		dchFrom.setDate(ds.getSpadFirstDate());
		dchFrom.setMinSelectableDate(ds.getSpadFirstDate());
		dchFrom.setMaxSelectableDate(today);
		dchTo.setDate(today);
		dchTo.setMinSelectableDate(ds.getSpadFirstDate());
		dchTo.setMaxSelectableDate(today);
	}

	private class FilterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				refreshTable();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), "Error",
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

	private class SelectRowListener implements ListSelectionListener {

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int[] selectedRows = tableData.getSelectedRows();
			if (selectedRows.length > 0) {
				tabbedPane.setSelectedIndex(1);
				tabbedPane.setEnabledAt(1, true);
				makeChart(data.get(selectedRows[0]).getCompanyId());
			}
		}

	}

	private class TabbedChangeListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			if (tabbedPane.getComponentCount() > 1
					&& (tabbedPane.getSelectedIndex() == 0 | tabbedPane
							.getSelectedIndex() == 2)) {
				tabbedPane.setEnabledAt(1, false);
			}
		}

	}

	private class ManualDownloaderListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Thread t = new Thread(new Downloader(ds, new CsvParser(),
					Downloader.BCPP_REMOTE_URL, Downloader.BCPP_LOCAL_PATH,
					MainGui.this));
			t.start();
			MainGui.this.setStatusLabel("Downloading...");
		}

	}

	private class AutoDownloaderTask extends TimerTask {

		@Override
		public void run() {
			Thread t = new Thread(new Downloader(ds, new CsvParser(),
					Downloader.BCPP_REMOTE_URL, Downloader.BCPP_LOCAL_PATH,
					MainGui.this));
			t.start();
			MainGui.this.setStatusLabel("Downloading...");
		}

	}
}
