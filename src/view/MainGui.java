package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

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
import java.util.Calendar;
import java.util.Timer;
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
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * @author MPI
 * @version 27.05.2014/1.10
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
	private JButton btn_adw_start;
	private JButton btn_adw_stop;
	private JSpinner adw_hour;
	private JSpinner adw_minute;

	private IDataStrategy ds;
	private SpadTableModel model;
	private ArrayList<SpadItem> data;
	private Timer adwTimer, adwShortFailTimer, adwLongFailTimer;
	private int adwFailTicks;

	public static final String[] columnNames = { "Name", "avPrice", "avVolume",
			"dPriceMin", "dPriceMax", "dPriceAvg" };
	public static final int ADW_SHORT_PERIOD = 10 * 60 * 1000; // 10min in ms
	public static final int ADW_LONG_PERIOD = 60 * 60 * 1000; // 1h in ms
	public static final int ADW_DAY_PERIOD = 24 * 60 * 60 * 1000; // 24h in ms
	public static final int ADW_FAIL_TICKS_MAX = 6; // max 6 ticks for short
													// period

	public MainGui(IDataStrategy ds) {
		super();
		try {
			this.ds = ds;
			this.data = this.ds.getSpadItems(null, null);
			this.model = new SpadTableModel(MainGui.columnNames, this.data);
			this.adwTimer = new Timer();
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
		frmSpadViewer.setCursor(Cursor
				.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		JMenuBar menuBar = new JMenuBar();
		frmSpadViewer.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem menu_download_man = new JMenuItem("Manually download");
		menu_download_man.addActionListener(new ManualDownloaderListener());
		mnFile.add(menu_download_man);

		JMenuItem menu_download_auto = new JMenuItem("AutoDownloader");
		menu_download_auto.addActionListener(new AutoDownloaderListener());
		mnFile.add(menu_download_auto);

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
		data_table.add(tableData, BorderLayout.CENTER);

		JTableHeader header = tableData.getTableHeader();
		header.setBackground(Color.yellow);
		data_table.add(header, BorderLayout.NORTH);

		JPanel data_info = new JPanel();
		panel_data.add(data_info, BorderLayout.SOUTH);

		JLabel lblNewLabel = new JLabel("Last date in db:");
		data_info.add(lblNewLabel);

		Date dd = ds.getSpadLastDate();
		label_last_date = new JLabel(((dd != null) ? new SimpleDateFormat(
				"dd.MM.yyyy").format(ds.getSpadLastDate()) : ""));
		data_info.add(label_last_date);

		panel_graph = new JPanel();
		tabbedPane.addTab("Graph", null, panel_graph, null);
		tabbedPane.setEnabledAt(0, true);
		tabbedPane.setEnabledAt(1, false);

		JPanel panel_adw = new JPanel();
		tabbedPane.addTab("AutoDownloader", null, panel_adw, null);
		panel_adw.setLayout(new BorderLayout(0, 0));

		JLabel lblNewLabel_1 = new JLabel(
				"Select time to periodical actualization. Data on remote server are published every working day at 20:15.");
		panel_adw.add(lblNewLabel_1, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		panel_adw.add(panel, BorderLayout.CENTER);

		adw_hour = new JSpinner();
		adw_hour.setModel(new SpinnerNumberModel(0, 0, 23, 1));
		panel.add(adw_hour);

		JLabel lblNewLabel_3 = new JLabel(":");
		panel.add(lblNewLabel_3);

		adw_minute = new JSpinner();
		adw_minute.setModel(new SpinnerNumberModel(0, 0, 59, 1));
		panel.add(adw_minute);

		btn_adw_start = new JButton("Start");
		btn_adw_start.addActionListener(new StartAutoDownloader());
		panel.add(btn_adw_start);

		btn_adw_stop = new JButton("Stop");
		btn_adw_stop.setEnabled(false);
		btn_adw_stop.addActionListener(new StopAutoDownloader());
		panel.add(btn_adw_stop);

		label_status = new JLabel(" ");
		frmSpadViewer.getContentPane().add(label_status, BorderLayout.SOUTH);

		refreshTable();
	}

	private DefaultCategoryDataset createDataset(ArrayList<Item> al,
			Double longTermAvgPrice) {
		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0; i < al.size(); i++) {
			dataset.addValue((al.get(i).getOpen() + al.get(i).getClose()) / 2,
					"avPrice", al.get(i).getDate().toString());
		}
		for (int i = 0; i < al.size(); i++) {
			dataset.addValue(longTermAvgPrice, "longTermAvPrice", al.get(i)
					.getDate().toString());
		}
		return dataset;
	}

	private JFreeChart createChart(final DefaultCategoryDataset dataset,
			String companyId) {
		java.sql.Date fromDate = parseFromDate(), toDate = parseToDate();
		JFreeChart chart = ChartFactory.createLineChart(companyId + " ("
				+ fromDate + " - " + toDate + ")", "Time [day]", "Price [CZK]",
				dataset, PlotOrientation.VERTICAL, true, true, false);
		final CategoryPlot plot = chart.getCategoryPlot();
		// ValueAxis range = plot.getRangeAxis();
		// range.setVisible(false);
		final CategoryAxis categoryAxis = (CategoryAxis) plot.getDomainAxis();
		categoryAxis.setAxisLineVisible(true);
		categoryAxis.setTickMarksVisible(false);
		// categoryAxis.setVisible(false);

		return chart;
	}

	/**
	 * @deprecated
	 */
	private void makeChart(String companyId) {
		ArrayList<Item> al;
		try {
			al = ds.getItems(parseFromDate(), parseToDate(), companyId);
			final DefaultCategoryDataset dataset = createDataset(al, null);
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

	private ChartPanel makeChartPanel(String companyId) {
		ArrayList<Item> al;
		ChartPanel chartPanel = null;
		Double longTermAvgPrice;
		try {
			al = ds.getItems(parseFromDate(), parseToDate(), companyId);
			longTermAvgPrice = ds.getLongTermAveragePrice(companyId);
			final DefaultCategoryDataset dataset = createDataset(al,
					longTermAvgPrice);
			final JFreeChart chart = createChart(dataset, companyId);
			chartPanel = new ChartPanel(chart);
			// chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
		return chartPanel;
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
		for (int i = 0; i < data.size(); i++) {
			for (int j = 0; j < recom.length; j++) {
				if (data.get(i).getCompanyId().equals(recom[j])) {
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

	public synchronized void checkDownloader(int status) {
		if (status == 1) {
			adwFailTicks = 0;
			if (adwShortFailTimer != null) {
				adwShortFailTimer.cancel();
			}
			if (adwLongFailTimer != null) {
				adwLongFailTimer.cancel();
			}
		} else {
			adwFailTicks++;
			if (adwFailTicks == 1) {
				// start short period timer
				if (adwShortFailTimer != null) {
					adwShortFailTimer.cancel();
				}
				adwShortFailTimer = new Timer();
				adwShortFailTimer.scheduleAtFixedRate(new AutoDownloaderTask(),
						0, ADW_SHORT_PERIOD);
			} else if (adwFailTicks > 1 && adwFailTicks < ADW_FAIL_TICKS_MAX) {
				// continue with short period timer
			} else if (adwFailTicks == ADW_FAIL_TICKS_MAX) {
				// close short period timer, start long period timer
				if (adwShortFailTimer != null) {
					adwShortFailTimer.cancel();
				}
				if (adwLongFailTimer != null) {
					adwLongFailTimer.cancel();
				}
				adwLongFailTimer = new Timer();
				adwLongFailTimer.scheduleAtFixedRate(new AutoDownloaderTask(),
						0, ADW_LONG_PERIOD);
			} else {
				// continue with long period timer
			}
		}
	}

	private class FilterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			try {
				frmSpadViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.WAIT_CURSOR));
				refreshTable();
				frmSpadViewer.setCursor(Cursor
						.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
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

		int[] selectedRows = new int[] { 0 };

		@Override
		public void valueChanged(ListSelectionEvent e) {
			selectedRows = tableData.getSelectedRows();
			if (selectedRows.length > 0 && e.getValueIsAdjusting()) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						frmSpadViewer.setCursor(Cursor
								.getPredefinedCursor(Cursor.WAIT_CURSOR));
						ChartPanel chartPanel = makeChartPanel(data.get(
								selectedRows[0]).getCompanyId());
						if (panel_graph.getComponentCount() > 0) {
							panel_graph.removeAll();
						}
						panel_graph.add(chartPanel, false);
						tabbedPane.setSelectedIndex(1);
						tabbedPane.setEnabledAt(1, true);
						frmSpadViewer.setCursor(Cursor
								.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}
				});
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
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MainGui.this.setStatusLabel("Downloading...");
				}
			});
		}
	}

	private class StartAutoDownloader implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			btn_adw_start.setEnabled(false);
			btn_adw_stop.setEnabled(true);
			adw_hour.setEnabled(false);
			adw_minute.setEnabled(false);
			int sel_h = (int) adw_hour.getValue();
			int sel_m = (int) adw_minute.getValue();
			int sel_mm = (sel_h * 60) + sel_m;
			Calendar cal = Calendar.getInstance();
			int now_h = cal.get(Calendar.HOUR_OF_DAY);
			int now_m = cal.get(Calendar.MINUTE);
			int now_mm = (now_h * 60) + now_m;

			long delay = 0;
			if (sel_mm < now_mm) {
				delay = (1440 - now_mm + sel_mm) * 60 * 1000;
			} else {
				delay = (sel_mm - now_mm) * 60 * 1000;
			}

			if (adwTimer != null) {
				adwTimer.cancel();
				adwTimer = new Timer();
				adwTimer.scheduleAtFixedRate(new AutoDownloaderTask(), delay,
						ADW_DAY_PERIOD);
			}
		}
	}

	private class StopAutoDownloader implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (adwTimer != null) {
				adwTimer.cancel();
			}
			btn_adw_start.setEnabled(true);
			btn_adw_stop.setEnabled(false);
			adw_hour.setEnabled(true);
			adw_minute.setEnabled(true);
		}
	}

	private class AutoDownloaderListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			tabbedPane.setSelectedIndex(2);
		}
	}

	private class AutoDownloaderTask extends TimerTask {

		@Override
		public void run() {
			Thread t = new Thread(new Downloader(ds, new CsvParser(),
					Downloader.BCPP_REMOTE_URL, Downloader.BCPP_LOCAL_PATH,
					MainGui.this));
			t.start();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MainGui.this.setStatusLabel("Downloading...");
				}
			});
		}
	}
}
