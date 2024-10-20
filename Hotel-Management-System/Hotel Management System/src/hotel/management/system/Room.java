package hotel.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Room extends JFrame {
	private JTable table;

	public Room() {
		setTitle("Room Information");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(450, 200, 1100, 600);
		setLayout(new BorderLayout());

		JPanel tablePanel = new JPanel(new BorderLayout());
		table = new JTable();
		JScrollPane scrollPane = new JScrollPane(table);
		tablePanel.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton loadDataButton = new JButton("Load Data");
		loadDataButton.addActionListener(e -> loadRoomData());
		buttonPanel.add(loadDataButton);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			new Reception().setVisible(true);
			dispose();
		});
		buttonPanel.add(backButton);

		add(tablePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/eight.jpg"));
		Image img = icon.getImage().getScaledInstance(600, 600, Image.SCALE_SMOOTH);
		JLabel imageLabel = new JLabel(new ImageIcon(img));
		add(imageLabel, BorderLayout.EAST);

		setVisible(true);
	}

	private void loadRoomData() {
		try (Connection conn = DatabaseConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT * FROM Room")) {

			ResultSetMetaData metaData = rs.getMetaData();
			int columnCount = metaData.getColumnCount();
			DefaultTableModel model = new DefaultTableModel();

			for (int i = 1; i <= columnCount; i++) {
				model.addColumn(metaData.getColumnName(i));
			}

			while (rs.next()) {
				Object[] row = new Object[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					row[i - 1] = rs.getObject(i);
				}
				model.addRow(row);
			}

			table.setModel(model);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading room data: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Room::new);
	}
}