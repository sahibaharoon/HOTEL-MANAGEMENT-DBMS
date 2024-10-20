package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddRoom extends JFrame {
    private JTextField roomNumberField, priceField;
    private JComboBox<String> availabilityComboBox, cleanStatusComboBox, bedTypeComboBox;

    public AddRoom() {
        setTitle("Add Rooms");
        setBounds(450, 200, 1000, 450);
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

        String[] labels = {"Room Number", "Availability", "Cleaning Status", "Price", "Bed Type"};
        JComponent[] inputs = new JComponent[5];

        roomNumberField = new JTextField(15);
        availabilityComboBox = new JComboBox<>(new String[]{"Available", "Occupied"});
        cleanStatusComboBox = new JComboBox<>(new String[]{"Cleaned", "Dirty"});
        priceField = new JTextField(15);
        bedTypeComboBox = new JComboBox<>(new String[]{"Single Bed", "Double Bed"});

        inputs[0] = roomNumberField;
        inputs[1] = availabilityComboBox;
        inputs[2] = cleanStatusComboBox;
        inputs[3] = priceField;
        inputs[4] = bedTypeComboBox;

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            panel.add(inputs[i], gbc);
        }

        JButton addButton = new JButton("Add");
        addButton.addActionListener(e -> addRoom());
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
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/twelve.jpg"));
        Image img = icon.getImage().getScaledInstance(500, 300, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        panel.add(imageLabel);
        return panel;
    }

    private void addRoom() {
        String roomNumber = roomNumberField.getText();
        String availability = (String) availabilityComboBox.getSelectedItem();
        String cleanStatus = (String) cleanStatusComboBox.getSelectedItem();
        String price = priceField.getText();
        String bedType = (String) bedTypeComboBox.getSelectedItem();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO room (roomnumber, availability, cleaning_status, price, bed_type) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, roomNumber);
            pstmt.setString(2, availability);
            pstmt.setString(3, cleanStatus);
            pstmt.setString(4, price);
            pstmt.setString(5, bedType);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Room Successfully Added");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add room", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddRoom::new);
    }
}