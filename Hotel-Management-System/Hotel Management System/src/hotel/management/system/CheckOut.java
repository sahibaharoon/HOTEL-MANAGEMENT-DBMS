package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class CheckOut extends JFrame {
	private JComboBox<String> customerIdComboBox;
	private JTextField roomNumberField;

	public CheckOut() {
		setTitle("Check Out");
		setBounds(530, 200, 500, 250);  // Reduced size since image is removed
		setLayout(new BorderLayout());

		JPanel formPanel = createFormPanel();
		JPanel buttonPanel = createButtonPanel();

		add(formPanel, BorderLayout.CENTER);  // Adjusted to use CENTER instead of WEST
		add(buttonPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		setVisible(true);
	}

	private JPanel createFormPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);  // Add padding between components

		// Title
		JLabel titleLabel = new JLabel("Check Out");
		titleLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;  // Center the title
		panel.add(titleLabel, gbc);

		// Reset gridwidth for other components
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;  // Left align other components

		// Customer ID Label
		gbc.gridy++;
		panel.add(new JLabel("Customer ID:"), gbc);

		// Customer ID ComboBox
		customerIdComboBox = new JComboBox<>();
		loadCustomerIds();
		gbc.gridx = 1;
		panel.add(customerIdComboBox, gbc);

		// Room Number Label
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(new JLabel("Room Number:"), gbc);

		// Room Number Field
		roomNumberField = new JTextField(15);
		roomNumberField.setEditable(false);  // Non-editable field
		gbc.gridx = 1;
		panel.add(roomNumberField, gbc);

		// Check Button for Room Number
		gbc.gridx = 2;  // Place the button next to the ComboBox
		gbc.gridy = 1;  // Ensure it's aligned properly with ComboBox
		JButton checkButton = new JButton("Check");  // Replacing the image with text

		checkButton.addActionListener(e -> loadRoomNumber());
		panel.add(checkButton, gbc);  // Correct positioning of the button

		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));  // Center the buttons with spacing

		// Check Out Button
		JButton checkOutButton = new JButton("Check Out");
		checkOutButton.addActionListener(e -> performCheckOut());
		panel.add(checkOutButton);

		// Back Button
		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			new Reception().setVisible(true);
			dispose();
		});
		panel.add(backButton);

		return panel;
	}

	private void loadCustomerIds() {
		try (Connection conn = DatabaseConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT number FROM customer")) {
			while (rs.next()) {
				customerIdComboBox.addItem(rs.getString("number"));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading customer IDs: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void loadRoomNumber() {
		String customerId = (String) customerIdComboBox.getSelectedItem();
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("SELECT room FROM customer WHERE number = ?")) {
			pstmt.setString(1, customerId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					roomNumberField.setText(rs.getString("room"));
				} else {
					JOptionPane.showMessageDialog(this, "Room number not found for this customer.",
							"Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading room number: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void performCheckOut() {
		String customerId = (String) customerIdComboBox.getSelectedItem();
		String roomNumber = roomNumberField.getText();

		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM customer WHERE number = ?");
			 PreparedStatement updateStmt = conn.prepareStatement("UPDATE room SET availability = 'Available' WHERE roomnumber = ?")) {

			conn.setAutoCommit(false);

			// Delete customer record
			deleteStmt.setString(1, customerId);
			deleteStmt.executeUpdate();

			// Mark room as available
			updateStmt.setString(1, roomNumber);
			updateStmt.executeUpdate();

			conn.commit();

			JOptionPane.showMessageDialog(this, "Check Out Successful");
			new Reception().setVisible(true);
			dispose();

		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error during check out: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(CheckOut::new);
	}
}
