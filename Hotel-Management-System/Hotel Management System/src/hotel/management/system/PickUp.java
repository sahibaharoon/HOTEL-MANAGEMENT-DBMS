package hotel.management.system;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PickUp extends JFrame {
	private JTable table;
	private JComboBox<String> carTypeComboBox;
	private DefaultTableModel tableModel;  // Keep reference to table model

	public PickUp() {
		setTitle("Pick Up Service");
		setBounds(530, 200, 800, 600);
		setLayout(new BorderLayout());

		JPanel topPanel = createTopPanel();
		JScrollPane tableScrollPane = createTablePanel();
		JPanel buttonPanel = createButtonPanel();

		add(topPanel, BorderLayout.NORTH);
		add(tableScrollPane, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		setVisible(true);
	}

	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(new JLabel("Type of Car Company:"));
		carTypeComboBox = new JComboBox<>();
		loadCarCompanies();  // Load car companies into the dropdown
		panel.add(carTypeComboBox);
		return panel;
	}

	private JScrollPane createTablePanel() {
		tableModel = new DefaultTableModel();  // Initialize the table model
		table = new JTable(tableModel);  // Use the model in the table
		return new JScrollPane(table);
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel();
		JButton displayButton = new JButton("Display");
		displayButton.addActionListener(e -> displayDrivers());
		panel.add(displayButton);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			// Pass any required parameters to the Reception constructor here
			new Reception().setVisible(true);
			dispose();
		});

		panel.add(backButton);

		return panel;
	}

	// Load distinct car companies from the 'company' attribute of the driver table
	private void loadCarCompanies() {
		try (Connection conn = DatabaseConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT DISTINCT company FROM driver")) {
			while (rs.next()) {
				carTypeComboBox.addItem(rs.getString("company"));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading car companies: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Display drivers based on the selected company
	private void displayDrivers() {
		String selectedCompany = (String) carTypeComboBox.getSelectedItem();
		if (selectedCompany == null) {
			JOptionPane.showMessageDialog(this, "Please select a car company.", "Input Error", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM driver WHERE company = ?")) {
			pstmt.setString(1, selectedCompany);
			try (ResultSet rs = pstmt.executeQuery()) {
				tableModel.setRowCount(0);  // Clear existing rows
				tableModel.setColumnCount(0); // Clear existing columns
				tableModel = buildTableModel(rs);  // Build new model
				table.setModel(tableModel);  // Set the model to the table
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error displaying drivers: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	// Build the table model to display driver details
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
		SwingUtilities.invokeLater(PickUp::new);
	}
}
