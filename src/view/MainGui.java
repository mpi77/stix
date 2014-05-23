package view;

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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MainGui {

	private JFrame frmSpadViewer;
	private JTable table_data;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainGui window = new MainGui();
					window.frmSpadViewer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainGui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSpadViewer = new JFrame();
		frmSpadViewer.setTitle("SPAD viewer");
		frmSpadViewer.setBounds(100, 100, 450, 300);
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
		menu_exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(menu_exit);
		
		JMenu mnStix = new JMenu("Stix");
		menuBar.add(mnStix);
		
		JMenuItem menu_about = new JMenuItem("About");
		mnStix.add(menu_about);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmSpadViewer.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Data", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.NORTH);
		
		JButton btn_refresh = new JButton("Refresh");
		panel_2.add(btn_refresh);
		
		table_data = new JTable();
		panel.add(table_data, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Graph", null, panel_1, null);
	}

}
