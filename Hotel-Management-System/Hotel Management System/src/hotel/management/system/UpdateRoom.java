package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class UpdateRoom extends JFrame {
	private JComboBox<String> guestIdComboBox;
	private JTextField roomNumberField;
	private JComboBox<String> availabilityComboBox;
	private JComboBox<String> cleanStatusComboBox;

	public UpdateRoom() {
		setTitle("Update Room Status");
		setBounds(530, 200, 1000, 450);
		setLayout(new BorderLayout());

		JPanel formPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST; // Align labels to the west

		guestIdComboBox = new JComboBox<>();
		loadGuestIds();

		roomNumberField = new JTextField(20);

		// Dropdown for Availability
		availabilityComboBox = new JComboBox<>(new String[]{"Available", "Occupied"});

		// Dropdown for Clean Status
		cleanStatusComboBox = new JComboBox<>(new String[]{"Cleaned", "Dirty"});

		// Add form fields with appropriate gridy increments
		int row = 0; // Initialize row for GridBagConstraints
		addFormField(formPanel, gbc, row++, "Guest ID:", guestIdComboBox);
		addFormField(formPanel, gbc, row++, "Room Number:", roomNumberField);
		addFormField(formPanel, gbc, row++, "Availability:", availabilityComboBox);
		addFormField(formPanel, gbc, row++, "Clean Status:", cleanStatusComboBox);

		JPanel buttonPanel = new JPanel();
		JButton checkButton = new JButton("Check");
		checkButton.addActionListener(e -> checkRoomStatus());
		buttonPanel.add(checkButton);

		JButton updateButton = new JButton("Update");
		updateButton.addActionListener(e -> updateRoomStatus());
		buttonPanel.add(updateButton);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> {
			new Reception().setVisible(true);
			dispose();
		});
		buttonPanel.add(backButton);

		add(formPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);

		ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/seventh.jpg"));
		Image img = icon.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
		JLabel imageLabel = new JLabel(new ImageIcon(img));
		add(imageLabel, BorderLayout.EAST);

		setVisible(true);
	}

	private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component) {
		gbc.gridx = 0;
		gbc.gridy = row; // Set the current row
		panel.add(new JLabel(label), gbc);

		gbc.gridx = 1;
		panel.add(component, gbc);
	}

	private void loadGuestIds() {
		try (Connection conn = DatabaseConnection.getConnection();
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery("SELECT number FROM customer")) {
			while (rs.next()) {
				guestIdComboBox.addItem(rs.getString("number"));
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading guest IDs: " + e.getMessage());
		}
	}

	private void checkRoomStatus() {
		String guestId = (String) guestIdComboBox.getSelectedItem();
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("SELECT room FROM customer WHERE number = ?")) {
			pstmt.setString(1, guestId);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					String roomNumber = rs.getString("room");
					roomNumberField.setText(roomNumber);
					loadRoomStatus(roomNumber);
				} else {
					JOptionPane.showMessageDialog(this, "No room found for this guest.");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error checking room status: " + e.getMessage());
		}
	}

	private void loadRoomStatus(String roomNumber) {
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("SELECT availability, cleaning_status FROM room WHERE roomnumber = ?")) {
			pstmt.setString(1, roomNumber);
			try (ResultSet rs = pstmt.executeQuery()) {
				if (rs.next()) {
					availabilityComboBox.setSelectedItem(rs.getString("availability"));
					cleanStatusComboBox.setSelectedItem(rs.getString("cleaning_status"));
				} else {
					JOptionPane.showMessageDialog(this, "No room status found for this room number.");
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error loading room status: " + e.getMessage());
		}
	}

	private void updateRoomStatus() {
		String roomNumber = roomNumberField.getText();
		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("UPDATE room SET availability = ?, cleaning_status = ? WHERE roomnumber = ?")) {
			pstmt.setString(1, (String) availabilityComboBox.getSelectedItem());
			pstmt.setString(2, (String) cleanStatusComboBox.getSelectedItem());
			pstmt.setString(3, roomNumber);
			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				JOptionPane.showMessageDialog(this, "Room status updated successfully");
			} else {
				JOptionPane.showMessageDialog(this, "No records were updated");
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error updating room status: " + e.getMessage());
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(UpdateRoom::new);
	}
}
