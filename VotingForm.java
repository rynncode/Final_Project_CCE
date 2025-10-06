import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class VotingForm extends JFrame {

    private JComboBox<String> candidateBox;
    private JButton castVoteButton, backButton, summaryButton;
    private JLabel welcomeLabel;
    private String voterUsername;
  

    public VotingForm(String username, cce_final_proj.TransactionalVotingSystem votingSystem) {
        this.voterUsername = username;
       

        // Ensure the voter is registered
        votingSystem.registerVoter(username);

        setTitle("Cast Your Vote");
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== HEADER =====
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(66, 133, 244));
        JLabel titleLabel = new JLabel("Online Voting System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN CONTENT =====
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        centerPanel.add(welcomeLabel, gbc);

        // Candidate selection
        gbc.gridy++;
        centerPanel.add(new JLabel("Select your candidate:"), gbc);

        gbc.gridy++;
        String[] candidates = {"Alice Reyes", "Brandon Cruz", "Carla Santos"}; // realistic candidates
        candidateBox = new JComboBox<>(candidates);
        candidateBox.setPreferredSize(new Dimension(250, 30));
        centerPanel.add(candidateBox, gbc);

        // Cast vote button
        gbc.gridy++;
        castVoteButton = new JButton("Cast Vote");
        castVoteButton.setPreferredSize(new Dimension(150, 35));
        centerPanel.add(castVoteButton, gbc);

        // Vote summary button
        gbc.gridy++;
        summaryButton = new JButton("Show Vote Summary");
        summaryButton.setPreferredSize(new Dimension(180, 35));
        centerPanel.add(summaryButton, gbc);

        // Back button
        gbc.gridy++;
        backButton = new JButton("Back to Home");
        backButton.setPreferredSize(new Dimension(150, 35));
        centerPanel.add(backButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // ===== ACTIONS =====

        castVoteButton.addActionListener(e -> {
            try ( FileWriter fw = new FileWriter("votes.csv", true)) {
                fw.write(voterUsername + "," + candidateBox.getSelectedItem() + "\n");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving vote: " + ex.getMessage());
                return;
            }
            String selectedCandidate = (String) candidateBox.getSelectedItem();

            boolean success = votingSystem.castVote(voterUsername, selectedCandidate);

            if (success) {
                castVoteButton.setEnabled(false);
                JOptionPane.showMessageDialog(null, "Vote successfully cast for " + selectedCandidate + "!");
            } else {
                JOptionPane.showMessageDialog(null, "Error: You have already voted or cannot vote.");
            }
        });

        summaryButton.addActionListener(e -> showVoteSummary());
        

        backButton.addActionListener(e -> {
            dispose();
            new cce_final_proj.OnlineVotingSystem().setVisible(true);
        });
    }

    private void showVoteSummary() {
        File file = new File("votes.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "No votes recorded yet.");
            return;
        }

        StringBuilder summary = new StringBuilder("<html><body>");
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                summary.append("Voter: ").append(data[0])
                        .append(" → Candidate: ").append(data[1])
                        .append("<br>");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error reading votes.csv: " + ex.getMessage());
        }
        summary.append("</body></html>");

        JOptionPane.showMessageDialog(null, summary.toString(), "Vote Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}
