import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;

@SuppressWarnings("unused")
public class VotingForm extends JFrame {
    private TwoPhaseCommit tpc; // safe vote writing
    private MixNet mixNet;
    private JComboBox<String> candidateBox;
    private JButton castVoteButton, backButton, summaryButton;
    private JLabel welcomeLabel;
    private String voterUsername;
    private TransactionalVotingSystem votingSystem;

    public VotingForm(String username, TransactionalVotingSystem votingSystem) {
        this.votingSystem = votingSystem;
        votingSystem.mainFrame.setVisible(false);

        tpc = new TwoPhaseCommit(); // (2PC) reads existing votes

        // --- Setup GUI --- //
        setTitle("Cast Your Vote");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(66, 133, 244));
        JLabel titleLabel = new JLabel("Online Voting System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        add(headerPanel, BorderLayout.NORTH);

        // --- MAIN CONTENT --- //
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.NORTH;

        // WELCOME LABEL
        gbc.gridy = 0;
        gbc.weighty = 0;
        welcomeLabel = new JLabel("Welcome, " + username + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 35));
        welcomeLabel.setForeground(new Color(44, 62, 80));
        centerPanel.add(welcomeLabel, gbc);

        // Candidate selection
        gbc.gridy++;
        JLabel selectLabel = new JLabel("Select your candidate:");
        selectLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        gbc.insets = new Insets(30, 10, 10, 10);
        centerPanel.add(selectLabel, gbc);

        gbc.gridy++;
        String[] candidates = votingSystem.candidates.stream()
                .map(c -> c.name)
                .toArray(String[]::new);
        candidateBox = new JComboBox<>(candidates);
        candidateBox.setPreferredSize(new Dimension(250, 30));
        centerPanel.add(candidateBox, gbc);

        // Buttons
        gbc.gridy++;
        gbc.weighty = 0;   
        gbc.fill = GridBagConstraints.NONE;  
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 30, 40)); // 1 row, 3 columns, 20px horizontal gap
        buttonPanel.setBackground(Color.WHITE);

        castVoteButton = new JButton("Cast Vote");
        castVoteButton.setPreferredSize(new Dimension(180, 70));
        castVoteButton.setBackground(new Color(66, 133, 244));
        castVoteButton.setForeground(Color.WHITE);
        castVoteButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(castVoteButton);

        summaryButton = new JButton("Show Vote Summary");
        summaryButton.setPreferredSize(new Dimension(200, 70));
        summaryButton.setBackground(new Color(66, 133, 244));
        summaryButton.setForeground(Color.WHITE);
        summaryButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(summaryButton);

        backButton = new JButton("Back to Home");
        backButton.setPreferredSize(new Dimension(180, 80));
        backButton.setBackground(new Color(66, 133, 244));
        backButton.setForeground(Color.WHITE);
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(backButton);
        

        centerPanel.add(buttonPanel, gbc);

        // FILLER
        gbc.gridy++;
        gbc.weighty = 1;
        JPanel filler = new JPanel();
        gbc.fill = GridBagConstraints.VERTICAL;
        filler.setBackground(Color.WHITE);
        centerPanel.add(filler, gbc);

        // Console / Performance Summary
        gbc.gridy++;
        gbc.weighty = 0;
        JLabel consoleLabel = new JLabel("System Feed / Time Complexity Output:");
        consoleLabel.setFont(new Font("DialogInput", Font.BOLD, 20));
        consoleLabel.setForeground(new Color(44, 62, 80));
        centerPanel.add(consoleLabel, gbc);

        // Performance Summary Panel
        JPanel perfPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        perfPanel.setBorder(BorderFactory.createTitledBorder("Performance Summary"));
        JLabel encryptTimeLabel = new JLabel("Last Encrypt Time: N/A");
        JLabel decryptTimeLabel = new JLabel("Last Decrypt Time: N/A");
        perfPanel.add(encryptTimeLabel);
        perfPanel.add(decryptTimeLabel);

        gbc.gridy++;
        gbc.weighty = 0.1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(perfPanel, gbc);

        // Console Area
        gbc.gridy++;
        gbc.weighty = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea consoleArea = new JTextArea(5, 50);
        consoleArea.setEditable(false);
        consoleArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        JScrollPane consoleScroll = new JScrollPane(consoleArea);
        consoleScroll.setPreferredSize(new Dimension(800, 150));
        consoleScroll.setMinimumSize(new Dimension(400, 50));
        centerPanel.add(consoleScroll, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- Initialize MixNet with console and labels ---
        try {
            mixNet = new MixNet(consoleArea, encryptTimeLabel, decryptTimeLabel);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error initializing MixNet: " + ex.getMessage());
        }

        this.voterUsername = username;

        // --- Button Listeners --- //
        castVoteButton.addActionListener(e -> {
    String selectedCandidate = (String) candidateBox.getSelectedItem();

    // Check if voter already voted
    if (votingSystem.registeredVoters.get(voterUsername).hasVoted) {
        JOptionPane.showMessageDialog(null, "Error: You have already voted.");
        return; // exit early, no encryption
    }

    try {
        // --- Encrypt voter ID using MixNet ---
        List<String> encryptedList = mixNet.anonymizeVotes(Collections.singletonList(voterUsername));
        String encryptedVoter = encryptedList.get(0);

        // --- Store encrypted vote ---
        boolean success = tpc.castVote(encryptedVoter, selectedCandidate);

        if (success) {
            // Update in-memory status
            votingSystem.registeredVoters.get(voterUsername).hasVoted = true;

            // Disable UI
            castVoteButton.setEnabled(false);
            candidateBox.setEnabled(false);

            JOptionPane.showMessageDialog(null, "Vote successfully cast for " + selectedCandidate + "!");
        } else {
            JOptionPane.showMessageDialog(null, "Error: Cannot cast vote.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Encryption error: " + ex.getMessage());
    }
});
        summaryButton.addActionListener(e -> showVoteSummary());

        backButton.addActionListener(e -> {
            dispose();
            votingSystem.mainFrame.setVisible(true);
        });
    }

    private void showVoteSummary() {
        File file = new File("votes.csv");
        if (!file.exists()) {
            JOptionPane.showMessageDialog(null, "No votes recorded yet.");
            return;
        }

        Map<String, Integer> counts = new HashMap<>();
        for (TransactionalVotingSystem.Candidate c : votingSystem.candidates) {
            counts.put(c.name, 0);
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] data = line.split(",");
                if (data.length < 2) continue;
                String candidate = data[1].trim();
                counts.put(candidate, counts.getOrDefault(candidate, 0) + 1);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error reading votes.csv: " + ex.getMessage());
        }

        StringBuilder summary = new StringBuilder("<html><body>");
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            summary.append(entry.getKey())
                   .append(": ")
                   .append(entry.getValue())
                   .append(" votes<br>");
        }
        summary.append("</body></html>");

        JOptionPane.showMessageDialog(null, summary.toString(), "Vote Summary", JOptionPane.INFORMATION_MESSAGE);
    }
}
