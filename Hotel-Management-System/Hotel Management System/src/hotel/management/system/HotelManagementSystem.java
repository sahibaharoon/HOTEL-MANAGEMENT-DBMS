package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class HotelManagementSystem extends JFrame {

        private final JLabel titleLabel;

        public HotelManagementSystem() {
                setTitle("Hotel Management System");
                setSize(1366, 430);
                setLayout(new BorderLayout());
                setLocationRelativeTo(null);

                // Title Label
                titleLabel = new JLabel("HOTEL MANAGEMENT SYSTEM");
                titleLabel.setFont(new Font("serif", Font.PLAIN, 70));
                titleLabel.setForeground(Color.BLACK);  // Set text color to black
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the title

                // Create a panel for the title
                JPanel titlePanel = new JPanel(new BorderLayout());
                titlePanel.setOpaque(false);
                titlePanel.add(titleLabel, BorderLayout.CENTER);

                // Add the title panel to the top
                add(titlePanel, BorderLayout.NORTH);

                // Image between the title and the button
                ImageIcon imageIcon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/hotelpic.c.jpg"));
                Image image = imageIcon.getImage().getScaledInstance(400, 300, Image.SCALE_DEFAULT); // Adjust size as needed
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center the image
                imagePanel.setOpaque(false);
                imagePanel.add(imageLabel);

                // Add the image panel to the center
                add(imagePanel, BorderLayout.CENTER);

                // Next Button
                JButton nextButton = new JButton("Next");
                nextButton.setBackground(Color.WHITE);
                nextButton.setForeground(Color.BLACK);
                nextButton.setFont(new Font("serif", Font.PLAIN, 20));  // Make the button text larger
                nextButton.addActionListener(this::actionPerformed);

                // Button Panel
                JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Center the button
                buttonPanel.setOpaque(false);
                buttonPanel.add(nextButton);

                // Add button panel to the bottom
                add(buttonPanel, BorderLayout.SOUTH);

                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setVisible(true);
        }

        private void actionPerformed(ActionEvent ae) {
                new Login().setVisible(true);
                this.setVisible(false);
        }

        public static void main(String[] args) {
                SwingUtilities.invokeLater(HotelManagementSystem::new);
        }
}
