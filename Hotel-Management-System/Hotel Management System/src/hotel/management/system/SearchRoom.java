package hotel.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SearchRoom extends JFrame {
	private JTable table;
	private JComboBox<String> bedTypeComboBox;
	private JCheckBox availableOnlyCheckBox;

	public SearchRoom() {
		setTitle("Search Rooms");
		setBounds(530, 200, 700, 500);
		setLayout(new BorderLayout());

		JPanel searchPanel = createSearchPanel();
		JScrollPane tableScrollPane = createTablePanel();
		JPanel buttonPanel = createButtonPanel();

		add(searchPanel, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private JPanel createSearchPanel() {
		JPanel searchPanel = new JPanel(new FlowLayout());
		bedTypeComboBox = new JComboBox<>(new String[]{"Single Bed", "Double Bed"});
		searchPanel.add(new JLabel("Room Bed Type:"));
		searchPanel.add(bedTypeComboBox);

		availableOnlyCheckBox = new JCheckBox("Only display Available");
		searchPanel.add(availableOnlyCheckBox);

		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(e -> searchRooms());
		searchPanel.add(searchButton);

		return searchPanel;
	}

	private JScrollPane createTablePanel() {
		table = new JTable();
		return new JScrollPane(table);
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			new Reception().setVisible(true);
			dispose();
		});
		buttonPanel.add(backButton);
		return buttonPanel;
	}

	private void searchRooms() {
		String bedType = (String) bedTypeComboBox.getSelectedItem();
		boolean availableOnly = availableOnlyCheckBox.isSelected();

		StringBuilder query = new StringBuilder("SELECT * FROM Room WHERE bed_type = ?");
		if (availableOnly) {
			query.append(" AND availability = 'Available'");
		}

		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(query.toString())) {
			pstmt.setString(1, bedType);
			try (ResultSet rs = pstmt.executeQuery()) {
				table.setModel(buildTableModel(rs));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error searching rooms: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}


	private DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		DefaultTableModel model = new DefaultTableModel();

		// Add column names
		for (int i = 1; i <= columnCount; i++) {
			model.addColumn(metaData.getColumnName(i));
		}

		// Add row data
		while (rs.next()) {
			Object[] row = new Object[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				row[i - 1] = rs.getObject(i);
			}
			model.addRow(row);
		}

		return model;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(SearchRoom::new);
	}
}