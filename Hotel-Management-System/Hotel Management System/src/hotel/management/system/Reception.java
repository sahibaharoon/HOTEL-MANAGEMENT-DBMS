package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class Reception extends JFrame {

	private JPanel contentPane;

	public Reception() {
		setTitle("Reception");
		setBounds(530, 200, 850, 570);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		// Updated button labels without "Update Check Status"
		String[] buttonLabels = {
				"New Customer Form", "Room", "All Employee Info",
				"Customer Info", "Manager Info", "Check Out",
				"Update Room Status", "Pick up Service", "Search Room", "Log Out"
		};

		for (String label : buttonLabels) {
			JButton button = new JButton(label);
			button.setPreferredSize(new Dimension(200, 30));
			button.addActionListener(e -> handleButtonClick(label));
			contentPane.add(button, gbc);
			gbc.gridy++;
		}

		ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/fourth.jpg"));
		Image img = icon.getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT);
		JLabel imgLabel = new JLabel(new ImageIcon(img));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.BOTH;
		contentPane.add(imgLabel, gbc);

		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void handleButtonClick(String buttonLabel) {
		setVisible(false);
		try {
			switch (buttonLabel) {
				case "New Customer Form" -> new NewCustomer().setVisible(true);
				case "Room" -> new Room().setVisible(true);
				case "All Employee Info" -> new Employee().setVisible(true);
				case "Customer Info" -> new CustomerInfo().setVisible(true);
				case "Manager Info" -> new ManagerInfo().setVisible(true);
				case "Check Out" -> new CheckOut().setVisible(true);
				// Removed case for "Update Check Status"
				case "Update Room Status" -> new UpdateRoom().setVisible(true);
				case "Pick up Service" -> new PickUp().setVisible(true);
				case "Search Room" -> new SearchRoom().setVisible(true);
				case "Log Out" -> new Login().setVisible(true);
				default -> setVisible(true);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
			setVisible(true);
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(Reception::new);
	}
}