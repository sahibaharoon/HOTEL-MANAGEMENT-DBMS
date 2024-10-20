package hotel.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Employee extends JFrame {
	private JTable table;

	public Employee() {
		setTitle("Employee Information");
		setBounds(430, 200, 1000, 600);
		setLayout(new BorderLayout());

		JPanel headerPanel = createHeaderPanel();
		JScrollPane tableScrollPane = createTablePanel();
		JPanel buttonPanel = createButtonPanel();

		add(headerPanel, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		setVisible(true);
	}

	private JPanel createHeaderPanel() {
		JPanel headerPanel = new JPanel(new GridLayout(1, 8));
		String[] headers = {"Name", "Age", "Gender", "Job", "Salary", "Phone", "Aadhar", "Email"};
		for (String header : headers) {
			headerPanel.add(new JLabel(header, SwingConstants.CENTER));
		}
		return headerPanel;
	}

	private JScrollPane createTablePanel() {
		table = new JTable();
		return new JScrollPane(table);
	}

	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		JButton loadDataButton = new JButton("Load Data");
		loadDataButton.addActionListener(e -> loadEmployeeData());
		buttonPanel.add(loadDataButton);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			new Reception().setVisible(true);
			dispose();
		});
		buttonPanel.add(backButton);

		return buttonPanel;
	}

	private void loadEmployeeData() {
		try (Connection conn = DatabaseConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT * FROM Employee")) {

			table.setModel(buildTableModel(rs));
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
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
		SwingUtilities.invokeLater(Employee::new);
	}
}