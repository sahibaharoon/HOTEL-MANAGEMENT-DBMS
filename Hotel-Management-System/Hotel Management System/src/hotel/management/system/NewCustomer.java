package hotel.management.system;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class NewCustomer extends JFrame {
	private JPanel contentPane;
	private JTextField t1, t2, t3;
	private JLabel roomPriceLabel, totalRateLabel;
	JComboBox<String> comboBox, roomComboBox;
	JRadioButton r1, r2;
	JDateChooser checkInDateChooser, checkOutDateChooser;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				NewCustomer frame = new NewCustomer();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public NewCustomer() throws SQLException {
		setBounds(530, 200, 850, 600);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblName = new JLabel("NEW CUSTOMER FORM");
		lblName.setFont(new Font("Yu Mincho", Font.PLAIN, 20));
		lblName.setBounds(118, 11, 260, 53);
		contentPane.add(lblName);

		JLabel lblId = new JLabel("ID :");
		lblId.setBounds(35, 76, 200, 14);
		contentPane.add(lblId);
		comboBox = new JComboBox<>(new String[]{"Passport", "Aadhar Card", "Voter Id", "Driving license"});
		comboBox.setBounds(271, 73, 150, 20);
		contentPane.add(comboBox);

		JLabel l2 = new JLabel("Number :");
		l2.setBounds(35, 111, 200, 14);
		contentPane.add(l2);
		t1 = new JTextField();
		t1.setBounds(271, 111, 150, 20);
		contentPane.add(t1);
		t1.setColumns(10);

		JLabel lblName_1 = new JLabel("Name :");
		lblName_1.setBounds(35, 151, 200, 14);
		contentPane.add(lblName_1);
		t2 = new JTextField();
		t2.setBounds(271, 151, 150, 20);
		contentPane.add(t2);
		t2.setColumns(10);

		JLabel lblGender = new JLabel("Gender :");
		lblGender.setBounds(35, 191, 200, 14);
		contentPane.add(lblGender);
		r1 = new JRadioButton("Male");
		r1.setBounds(271, 191, 80, 12);
		contentPane.add(r1);
		r2 = new JRadioButton("Female");
		r2.setBounds(350, 191, 100, 12);
		contentPane.add(r2);

		JLabel lblCountry = new JLabel("Country :");
		lblCountry.setBounds(35, 231, 200, 14);
		contentPane.add(lblCountry);
		t3 = new JTextField();
		t3.setBounds(271, 231, 150, 20);
		contentPane.add(t3);

		JLabel lblRoomNumber = new JLabel("Allocated Room Number :");
		lblRoomNumber.setBounds(35, 274, 200, 14);
		contentPane.add(lblRoomNumber);
		roomComboBox = new JComboBox<>();
		roomComboBox.setBounds(271, 274, 150, 20);
		contentPane.add(roomComboBox);

		// Fetch available rooms from the database
		loadAvailableRooms();

		JLabel lblCheckInStatus = new JLabel("Check-In :");
		lblCheckInStatus.setBounds(35, 316, 200, 14);
		contentPane.add(lblCheckInStatus);
		checkInDateChooser = new JDateChooser();
		checkInDateChooser.setBounds(271, 316, 150, 20);
		checkInDateChooser.setDate(new Date());
		contentPane.add(checkInDateChooser);

		JLabel lblCheckOut = new JLabel("Check-Out :");
		lblCheckOut.setBounds(35, 356, 200, 14);
		contentPane.add(lblCheckOut);
		checkOutDateChooser = new JDateChooser();
		checkOutDateChooser.setBounds(271, 356, 150, 20);
		contentPane.add(checkOutDateChooser);

		// Add listeners to check-in and check-out date choosers
		checkInDateChooser.addPropertyChangeListener(e -> updateTotalRate());
		checkOutDateChooser.addPropertyChangeListener(e -> updateTotalRate());

		roomPriceLabel = new JLabel("Room Price: $0");
		roomPriceLabel.setBounds(450, 274, 200, 14);
		contentPane.add(roomPriceLabel);

		totalRateLabel = new JLabel("Total Rate: $0");
		totalRateLabel.setBounds(450, 356, 200, 14);
		contentPane.add(totalRateLabel);

		roomComboBox.addActionListener(e -> updateRoomPrice());

		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(e -> addCustomer());
		btnNewButton.setBounds(100, 450, 120, 30);
		contentPane.add(btnNewButton);

		JButton btnExit = new JButton("Back");
		btnExit.setBounds(260, 450, 120, 30);
		btnExit.addActionListener(e -> {
			new Reception().setVisible(true);
			setVisible(false);
		});
		contentPane.add(btnExit);
		getContentPane().setBackground(Color.WHITE);
	}

	private void loadAvailableRooms() {
		try {
			Connection conn = DatabaseConnection.getConnection();
			PreparedStatement pst = conn.prepareStatement("SELECT roomnumber FROM room WHERE availability = 'Available'");
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				roomComboBox.addItem(rs.getString("roomnumber"));
			}
			if (roomComboBox.getItemCount() > 0) {
				roomComboBox.setSelectedIndex(0);
			}
			pst.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void updateRoomPrice() {
		String roomNumber = (String) roomComboBox.getSelectedItem();
		if (roomNumber != null) {
			try {
				Connection conn = DatabaseConnection.getConnection();
				PreparedStatement pst = conn.prepareStatement("SELECT price FROM room WHERE roomnumber = ?");
				pst.setString(1, roomNumber);
				ResultSet rs = pst.executeQuery();
				if (rs.next()) {
					int price = rs.getInt("price");
					roomPriceLabel.setText("Room Price: $" + price);
					updateTotalRate(); // Update total rate when room price is fetched
				}
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void updateTotalRate() {
		Date checkInDate = checkInDateChooser.getDate();
		Date checkOutDate = checkOutDateChooser.getDate();
		int roomPrice = 0;

		if (checkInDate != null && checkOutDate != null && checkOutDate.after(checkInDate)) {
			// Calculate the total rate based on number of days
			long differenceInMillis = checkOutDate.getTime() - checkInDate.getTime();
			long daysBetween = (differenceInMillis / (1000 * 60 * 60 * 24)); // Convert milliseconds to days
			roomPrice = Integer.parseInt(roomPriceLabel.getText().replaceAll("[^0-9]", ""));
			int totalRate = (int) (daysBetween * roomPrice);
			totalRateLabel.setText("Total Rate: $" + totalRate);
		} else {
			totalRateLabel.setText("Total Rate: $0");
		}
	}

	private void addCustomer() {
		try {
			Connection conn = DatabaseConnection.getConnection();
			String idDocument = (String) comboBox.getSelectedItem();
			String idNumber = t1.getText();
			String name = t2.getText();
			String gender = r1.isSelected() ? "Male" : "Female";
			String country = t3.getText();
			String roomNumber = (String) roomComboBox.getSelectedItem();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date checkInDate = checkInDateChooser.getDate();
			Date checkOutDate = checkOutDateChooser.getDate();

			if (checkInDate != null && checkOutDate != null && checkOutDate.after(checkInDate)) {
				String formattedCheckInDate = dateFormat.format(checkInDate);
				String formattedCheckOutDate = dateFormat.format(checkOutDate);
				String query = "INSERT INTO customer (document, number, name, gender, country, room, checkintime, checkouttime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pst = conn.prepareStatement(query);
				pst.setString(1, idDocument);
				pst.setString(2, idNumber);
				pst.setString(3, name);
				pst.setString(4, gender);
				pst.setString(5, country);
				pst.setString(6, roomNumber);
				pst.setString(7, formattedCheckInDate);
				pst.setString(8, formattedCheckOutDate);
				pst.executeUpdate();
				pst.close();

				// Update room availability
				PreparedStatement updateRoomStmt = conn.prepareStatement("UPDATE room SET availability = 'Not Available' WHERE roomnumber = ?");
				updateRoomStmt.setString(1, roomNumber);
				updateRoomStmt.executeUpdate();
				updateRoomStmt.close();

				conn.close();

				JOptionPane.showMessageDialog(null, "Customer added successfully!");
				clearFields(); // Method to clear the form fields after adding a customer
			} else {
				JOptionPane.showMessageDialog(null, "Please select valid check-in and check-out dates.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error adding customer: " + e.getMessage());
		}
	}

	private void clearFields() {
		comboBox.setSelectedIndex(0);
		t1.setText("");
		t2.setText("");
		r1.setSelected(false);
		r2.setSelected(false);
		t3.setText("");
		roomComboBox.setSelectedIndex(0);
		checkInDateChooser.setDate(new Date());
		checkOutDateChooser.setDate(null);
		roomPriceLabel.setText("Room Price: $0");
		totalRateLabel.setText("Total Rate: $0");
	}
}

