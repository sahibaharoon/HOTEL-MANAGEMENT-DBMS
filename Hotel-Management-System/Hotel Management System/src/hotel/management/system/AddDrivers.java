package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AddDrivers extends JFrame {
	private JTextField nameField, ageField, carField, locationField;
	private JComboBox<String> genderComboBox, availableComboBox, carTypeComboBox;

	public AddDrivers() {
		setTitle("Add Drivers");
		setBounds(450, 200, 1000, 500);
		setLayout(new BorderLayout());

		JPanel formPanel = createFormPanel();
		JPanel imagePanel = createImagePanel();

		add(formPanel, BorderLayout.WEST);
		add(imagePanel, BorderLayout.EAST);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setBackground(Color.WHITE);
		setVisible(true);
	}

	private JPanel createFormPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		// Updated labels
		String[] labels = {"Name", "Age", "Gender", "Car Type", "Car", "Available", "Location"};
		JComponent[] inputs = new JComponent[7];

		nameField = new JTextField(15);
		ageField = new JTextField(15);
		genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
		carTypeComboBox = new JComboBox<>(new String[]{"SUV", "Sedan", "Hatchback"});  // Car type selection
		carField = new JTextField(15);  // Renamed carBrandField to carField
		availableComboBox = new JComboBox<>(new String[]{"Yes", "No"});
		locationField = new JTextField(15);

		inputs[0] = nameField;
		inputs[1] = ageField;
		inputs[2] = genderComboBox;
		inputs[3] = carTypeComboBox;  // Updated to carTypeComboBox
		inputs[4] = carField;  // Updated to carField
		inputs[5] = availableComboBox;
		inputs[6] = locationField;

		for (int i = 0; i < labels.length; i++) {
			gbc.gridx = 0;
			gbc.gridy = i;
			panel.add(new JLabel(labels[i]), gbc);

			gbc.gridx = 1;
			panel.add(inputs[i], gbc);
		}

		JButton addButton = new JButton("Add");
		addButton.addActionListener(e -> addDriver());
		gbc.gridx = 0;
		gbc.gridy++;
		panel.add(addButton, gbc);

		JButton backButton = new JButton("Back");
		backButton.addActionListener(e -> dispose());
		gbc.gridx = 1;
		panel.add(backButton, gbc);

		return panel;
	}

	private JPanel createImagePanel() {
		JPanel panel = new JPanel();
		ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/eleven.jpg"));
		Image img = icon.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
		JLabel imageLabel = new JLabel(new ImageIcon(img));
		panel.add(imageLabel);
		return panel;
	}

	private void addDriver() {
		String name = nameField.getText();
		String age = ageField.getText();
		String gender = (String) genderComboBox.getSelectedItem();
		String carType = (String) carTypeComboBox.getSelectedItem();  // Get selected car type
		String car = carField.getText();  // Get car
		String available = (String) availableComboBox.getSelectedItem();
		String location = locationField.getText();

		try (Connection conn = DatabaseConnection.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement(
					 "INSERT INTO driver (name, age, gender, company, brand, available, location) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
			pstmt.setString(1, name);
			pstmt.setString(2, age);
			pstmt.setString(3, gender);
			pstmt.setString(4, carType);  // Set car type
			pstmt.setString(5, car);  // Set car
			pstmt.setString(6, available);
			pstmt.setString(7, location);

			int result = pstmt.executeUpdate();
			if (result > 0) {
				JOptionPane.showMessageDialog(this, "Driver Successfully Added");
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Failed to add driver", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(AddDrivers::new);
	}
}
