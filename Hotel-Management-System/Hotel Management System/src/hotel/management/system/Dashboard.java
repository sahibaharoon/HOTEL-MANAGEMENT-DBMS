package hotel.management.system;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    public Dashboard() {
        super("HOTEL MANAGEMENT SYSTEM");
        setLayout(new BorderLayout());

        // Background Image Setup
        ImageIcon backgroundIcon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/third.jpg"));
        Image backgroundImage = backgroundIcon.getImage().getScaledInstance(1950, 1000, Image.SCALE_DEFAULT);
        JLabel backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        add(backgroundLabel, BorderLayout.CENTER);

        // Welcome Label
        JLabel welcomeLabel = new JLabel("THE DBMS GROUP 10 WELCOMES YOU");
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setFont(new Font("Tahoma", Font.PLAIN, 46));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(welcomeLabel, BorderLayout.NORTH);

        // Panel for Centering Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // Make panel transparent to see the background
        buttonPanel.setLayout(new GridBagLayout());
        backgroundLabel.add(buttonPanel, BorderLayout.CENTER); // Add button panel to center

        // GridBagConstraints for centering buttons
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing between buttons
        gbc.anchor = GridBagConstraints.CENTER;

        // Reception Button
        JButton receptionButton = new JButton("RECEPTION");
        receptionButton.setPreferredSize(new Dimension(200, 50));
        receptionButton.addActionListener(e -> {
            new Reception().setVisible(true);
            dispose(); // Close the dashboard after opening reception
        });
        gbc.gridx = 0; // Positioning for reception button
        gbc.gridy = 0;
        buttonPanel.add(receptionButton, gbc);

        // Admin Button
        JButton adminButton = new JButton("ADMIN");
        adminButton.setPreferredSize(new Dimension(200, 50));
        adminButton.addActionListener(e -> handleAdminAction()); // Handle admin action
        gbc.gridy = 1; // Positioning for admin button
        buttonPanel.add(adminButton, gbc);

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void handleAdminAction() {
        String[] adminItems = {"ADD EMPLOYEE", "ADD ROOMS", "ADD DRIVERS"};
        String action = (String) JOptionPane.showInputDialog(this, "Select an action:", "Admin Actions",
                JOptionPane.PLAIN_MESSAGE, null, adminItems, adminItems[0]);
        if (action != null) {
            try {
                switch (action) {
                    case "ADD EMPLOYEE" -> new AddEmployee().setVisible(true);
                    case "ADD ROOMS" -> new AddRoom().setVisible(true);
                    case "ADD DRIVERS" -> new AddDrivers().setVisible(true);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Dashboard().setVisible(true));
    }
}
