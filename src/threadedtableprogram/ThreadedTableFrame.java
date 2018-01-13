import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * ThreadedTableFrame is a self configuring JFrame. It fills itself with a table
 * It also has a panel in the south region of the Frame that shows a 
 * running total of the edits made to the table
 *
 * The ThreadedTableFrame takes a TableModel as an argument, and uses it to 
 * populate the table
 */
 
public class ThreadedTableFrame extends JFrame {
	
	public ThreadedTableFrame(TableModel model) {
		super("Table Frame");
		
		JTable table = new JTable(model);
		JPanel panel = new ChangeCountPanel(model);
		
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(table, BorderLayout.NORTH);
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		pack();
		setVisible(true);
	}
	
	
	/**
	 * A JPanel that listens for tableModelChanges, and displays a 
	 * running count of the changes that moves accross the panel.
	 */
	
	private class ChangeCountPanel extends JPanel implements TableModelListener {
		private int changeCount;
		private int textOffset;
		private JLabel label;
		
		private static final int INDENT = 20;
		private static final int MOVEMENT = 3;
		
		// Initialize the label, and register as a listener with
		// the table model
		
		public ChangeCountPanel(TableModel m) {
			changeCount = 0;
			label = new JLabel("" + changeCount);
			
			m.addTableModelListener(this);
			
			add(label);
			
			// start the thread that will periodicaly call moveThread
			(new Thread(new ChangeCountPanelRunnable(this))).start();
		}
		
		// every time the table model changes, increment
		// the change count and the offset, then repaint
		
		public void tableChanged(TableModelEvent e) {
			changeCount++;
			repaint();
		}
		
		public void moveText() {
			// ignore moveText invocations if the panel is not visible
			if (!isVisible()) return;
			
			textOffset += MOVEMENT;
			repaint();
		}
		
		public void paint(Graphics g) {
			int xPosition = INDENT + textOffset;
			
			// wrap back to the left side if the text has reached
			// the right margin
			
			if (xPosition > getWidth() - INDENT) {
				xPosition = INDENT;
				textOffset = 0;
			}
			
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
			
			g.setColor(Color.BLUE);
			g.drawString("" + changeCount, xPosition, 15); 
		}
	}		

	
	/**
	 * The runnable for a thread that will periodically invoke
	 * moveText on a changeCountPanel
	 */
	 
	private class ChangeCountPanelRunnable implements Runnable {
		private ChangeCountPanel panel;
		private static final int SLEEP_COUNT = 60;
		
		ChangeCountPanelRunnable(ChangeCountPanel p) {
			panel = p;
		}
		
		public void run() {
			
			// periodically invoke moveText
			while (true) {
				panel.moveText();
				
				try {
					Thread.sleep(SLEEP_COUNT);
				} catch (InterruptedException e) {}
			}
		}
	}		  
	
	
	/**
	 * Simple Test harness, launches a ThreadedTableFrame with a toy
	 * TableModel
	 */
	 
	 public static void main(String args[]) {
		new ThreadedTableFrame(new SimpleTableModel(6, 8));
	 }
	
	
	/**
	 * Just for testing purposes, a minimal TableModel
	 */
	 
	 private static class SimpleTableModel extends AbstractTableModel {
		int startValue;
		int rowCount;
		int columnCount;
		
		public SimpleTableModel(int rc, int cc) {
			startValue = 1;
			rowCount = rc;
			columnCount = cc;
		}
	
		public int getColumnCount() {
			return rowCount;
		}
		
		public int getRowCount() {
			return columnCount;
		}
		
		public Object getValueAt(int r, int c) {
			return "" + (startValue + r + c);
		}
		
		public void setValueAt(Object string, int r, int c) {
			int editedValue = Integer.parseInt((String)string);
			
			startValue = editedValue - r - c;
			
			fireTableDataChanged();
		}
		
		public boolean isCellEditable(int r, int c) {
			return true;
		}
	}
	
}