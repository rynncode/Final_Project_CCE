import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

public class cce_final_proj {

    public static class OnlineVotingSystem extends JFrame {
        private JPanel sidebarPanel, headerPanel, mainContentPanel;
        private JButton homeButton, signInButton, registerButton;
        private JLabel welcomeLabel, forgotPasswordLabel, profileLabel;

        public OnlineVotingSystem() {
            setTitle("Online Voting System");
            setSize(1080, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            initializeComponents();
            setupLayout();
            configureComponents();
        }

        private void initializeComponents() {
            sidebarPanel = new JPanel();
            headerPanel = new JPanel();
            mainContentPanel = new JPanel();

            homeButton = new JButton("Home Page");
            signInButton = new JButton("Sign in");
            registerButton = new JButton("Register");

            welcomeLabel = new JLabel("WELCOME!");
            forgotPasswordLabel = new JLabel("Forgot password?");
            profileLabel = new JLabel("PROFILE ▼");
        }

        private void setupLayout() {
            setLayout(new BorderLayout());

            // Sidebar
            sidebarPanel.setLayout(new GridLayout(5, 1, 10, 10));
            sidebarPanel.setBackground(Color.WHITE);
            sidebarPanel.add(homeButton);
            sidebarPanel.add(new JButton("TBF"));
            sidebarPanel.add(new JButton("TBF"));
            sidebarPanel.add(new JButton("TBF"));
            sidebarPanel.add(new JButton("TBF"));
            add(sidebarPanel, BorderLayout.WEST);

            // Header
            headerPanel.setLayout(new BorderLayout());
            headerPanel.setBackground(new Color(66, 133, 244));
            JLabel titleLabel = new JLabel("Online Voting System");
            titleLabel.setForeground(Color.WHITE);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            headerPanel.add(titleLabel, BorderLayout.WEST);

            profileLabel.setForeground(Color.WHITE);
            profileLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            profileLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            headerPanel.add(profileLabel, BorderLayout.EAST);

            add(headerPanel, BorderLayout.NORTH);

            // Main content area
            mainContentPanel.setBackground(Color.LIGHT_GRAY);
            mainContentPanel.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.insets = new Insets(15, 10, 15, 10);

            // Welcome label
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 72));
            gbc.gridy = 0;
            mainContentPanel.add(welcomeLabel, gbc);

            // Description text
            JLabel descriptionLabel = new JLabel("<html><div style='text-align: center; width:600px;'>"
                    + "This site is intended for providing a secure, transparent, and easy-to-use platform for voting. "
                    + "Whether for community decisions, elections, or surveys, this site allows users to cast their votes confidently "
                    + "and ensure their voices are heard. Our goal is to make the voting process seamless, accessible, "
                    + "and trustworthy for all participants."
                    + "</div></html>");
            descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            gbc.gridy = 1;
            mainContentPanel.add(descriptionLabel, gbc);

            // Sign in button
            signInButton.setPreferredSize(new Dimension(200, 40));
            gbc.gridy = 2;
            mainContentPanel.add(signInButton, gbc);

            // Register button
            registerButton.setPreferredSize(new Dimension(200, 40));
            gbc.gridy = 3;
            mainContentPanel.add(registerButton, gbc);

            // Forgot password link
            forgotPasswordLabel.setForeground(Color.RED);
            forgotPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            gbc.gridy = 4;
            mainContentPanel.add(forgotPasswordLabel, gbc);

            add(mainContentPanel, BorderLayout.CENTER);
        }

        private void configureComponents() {
            // ActionListener for Register button
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    RegisterForm registerForm = new RegisterForm();
                    registerForm.setVisible(true);

                    // Optionally hide the main window:
                    // OnlineVotingSystem.this.setVisible(false);
                }
            });
        }

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                OnlineVotingSystem frame = new OnlineVotingSystem();
                frame.setVisible(true);
            });
        }
    }

    // Register Form class
    public static class RegisterForm extends JFrame {
        private JTextField usernameField, emailField, firstNameField, lastNameField;
        private JPasswordField passwordField, confirmPasswordField;

        public RegisterForm() {
            setTitle("Register");
            setSize(600, 400);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            getContentPane().setBackground(new Color(220, 220, 220));
            setLayout(new BorderLayout());

            // Title
            JPanel titlePanel = new JPanel();
            titlePanel.setBackground(new Color(220, 220, 220));
            JLabel titleLabel = new JLabel("REGISTER");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
            titlePanel.add(titleLabel);
            add(titlePanel, BorderLayout.NORTH);

            // Form
            JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
            formPanel.setBackground(new Color(220, 220, 220));
            formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

            usernameField = new JTextField();
            emailField = new JTextField();
            firstNameField = new JTextField();
            lastNameField = new JTextField();
            passwordField = new JPasswordField();
            confirmPasswordField = new JPasswordField();

            formPanel.add(new JLabel("USERNAME"));
            formPanel.add(usernameField);
            formPanel.add(new JLabel("EMAIL"));
            formPanel.add(emailField);
            formPanel.add(new JLabel("FIRST NAME"));
            formPanel.add(firstNameField);
            formPanel.add(new JLabel("LAST NAME"));
            formPanel.add(lastNameField);
            formPanel.add(new JLabel("CREATE PASSWORD"));
            formPanel.add(passwordField);
            formPanel.add(new JLabel("RE-ENTER PASSWORD"));
            formPanel.add(confirmPasswordField);

            // Buttons
            JButton submitButton = new JButton("Submit");
            JButton backButton = new JButton("Back");
            formPanel.add(backButton);
            formPanel.add(submitButton);

            add(formPanel, BorderLayout.CENTER);

            // Action for submit button
            submitButton.addActionListener(e -> {
                String username = usernameField.getText().trim();
                String email = emailField.getText().trim();
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());

                if (username.isEmpty() || email.isEmpty() || firstName.isEmpty() ||
                        lastName.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Save to file
                try (FileWriter fw = new FileWriter("users.csv", true)) {
                    fw.write(username + "," + email + "," + firstName + "," + lastName + "," + password + "\n");
                    JOptionPane.showMessageDialog(this, "Registration successful! Data saved.");
                    dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving user data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Action for back button
            backButton.addActionListener(e -> dispose());
        }
    }
    }
