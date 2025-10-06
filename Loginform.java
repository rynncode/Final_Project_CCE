import java.util.Scanner;

import javax.swing.*;
import java.awt.*;
import java.io.*;


public class Loginform extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    private static cce_final_proj.TransactionalVotingSystem votingSystem = new cce_final_proj.TransactionalVotingSystem();
// constructor for the login form
    public Loginform() {
        setTitle("Sign In");
        setSize(400, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridBagLayout()); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); // padding lol
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        // Row 1 - Username
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        add(usernameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        // Row 2 - Password
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        // Row 3 - Login button (centered)
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        // Action listener
        loginButton.addActionListener(e -> performLogin());
        getRootPane().setDefaultButton(loginButton);
    }
    // performs the login by checking if the fields are empty and then calling the authenticate method
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (authenticate(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            new VotingForm(username, votingSystem).setVisible(true); 
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }
// authenticates the user by checking the users.csv file
    private boolean authenticate(String username, String password) {
        File file = new File("users.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "users.csv file not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                if (data.length >= 5 && data[0].trim().equals(username) && data[4].trim().equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading users.csv: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Loginform().setVisible(true));
    }
}