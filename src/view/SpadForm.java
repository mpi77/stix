package view;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 * @author MPI
 * @version 04.05.2014/1.1
 */
public class SpadForm extends JFrame {

	public static final String[] columnNames = { "Name", "avPrice", "avVolume", "dPriceMin",
			"dPriceMax", "dPriceAvg"};

	private JFrame frame;
	private JTable table;
	private SpadTableModel model;
	
	public SpadForm(SpadTableModel model) {
		super();
		this.model = model;
		initPanel();
	}

	public void initPanel() {
		frame = new JFrame("SPAD viewer");
		JPanel panel = new JPanel();
		
		table = new JTable(model);
		
		JTableHeader header = table.getTableHeader();
		header.setBackground(Color.yellow);
		
		JScrollPane scroller = new JScrollPane(table);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		table.setSize(800, 600);
		
        //setLayout(new BorderLayout());
		panel.add(scroller);
		frame.add(panel);
		frame.setSize(800,600);
		frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        //frame.pack();
        frame.setVisible(true);
	}
	
}
