import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.table.*;

public class ClubTableTester extends JFrame {

	public ClubTableTester() {
		super("Club Table Tester");

		JTable table;
		ClubTableModel model = new ClubTableModel();
		
		table = new JTable(model);
		
		getContentPane().add(table);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		pack();
		setVisible(true);
	}
	
	public static void main(String args[]) {
		new ClubTableTester();
	}
	
}