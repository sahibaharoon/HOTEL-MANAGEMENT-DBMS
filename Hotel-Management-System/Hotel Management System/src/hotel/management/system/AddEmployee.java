package hotel.management.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AddEmployee extends JFrame {
    private JTextField nameField, ageField, salaryField, phoneField, aadharField, emailField;
    private JComboBox<String> jobComboBox;
    private JRadioButton maleRadioButton, femaleRadioButton;

    public AddEmployee() {
        setTitle("ADD EMPLOYEE DETAILS");
        setBounds(530, 200, 900, 600);
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

        String[] labels = {"NAME", "AGE", "GENDER", "JOB", "SALARY", "PHONE", "AADHAR", "EMAIL"};
        JTextField[] fields = {nameField = new JTextField(20), ageField = new JTextField(20), null, null, salaryField = new JTextField(20), phoneField = new JTextField(20), aadharField = new JTextField(20), emailField = new JTextField(20)};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            panel.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            if (i == 2) { // Gender
                panel.add(createGenderPanel(), gbc);
            } else if (i == 3) { // Job
                jobComboBox = new JComboBox<>(new String[]{"Front Desk Clerks", "Porters", "Housekeeping", "Kitchen Staff", "Room Service", "Waiter/Waitress", "Manager", "Accountant", "Chef"});
                panel.add(jobComboBox, gbc);
            } else {
                //fields[i] = new JTextField(20);
                panel.add(fields[i], gbc);
            }
        }

        JButton saveButton = new JButton("SAVE");
        saveButton.addActionListener(e -> saveEmployee());
        gbc.gridx = 1;
        gbc.gridy++;
        panel.add(saveButton, gbc);

        return panel;
    }

    private JPanel createGenderPanel() {
        JPanel genderPanel = new JPanel();
        maleRadioButton = new JRadioButton("MALE");
        femaleRadioButton = new JRadioButton("FEMALE");
        ButtonGroup genderGroup = new ButtonGroup();
        genderGroup.add(maleRadioButton);
        genderGroup.add(femaleRadioButton);
        genderPanel.add(maleRadioButton);
        genderPanel.add(femaleRadioButton);
        return genderPanel;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel();
        ImageIcon icon = new ImageIcon(ClassLoader.getSystemResource("hotel/management/system/icons/tenth.jpg"));
        Image img = icon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(img));
        panel.add(imageLabel);
        return panel;
    }

    private void saveEmployee() {
        String name = nameField.getText();
        String age = ageField.getText();
        String gender = maleRadioButton.isSelected() ? "male" : "female";
        String job = (String) jobComboBox.getSelectedItem();
        String salary = salaryField.getText();
        String phone = phoneField.getText();
        String aadhar = aadharField.getText();
        String email = emailField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO employee (name, age, gender, job, salary, phone, aadhar, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, name);
            pstmt.setString(2, age);
            pstmt.setString(3, gender);
            pstmt.setString(4, job);
            pstmt.setString(5, salary);
            pstmt.setString(6, phone);
            pstmt.setString(7, aadhar);
            pstmt.setString(8, email);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Employee Added Successfully");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add employee", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AddEmployee::new);
    }
}