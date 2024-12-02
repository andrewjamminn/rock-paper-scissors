import java.awt.*;
import javax.swing.*;
import java.io.IOException;

public class GameUI {

    private static final String PAGE_SELECTION = "Selection";
    private static final String PAGE_WAITING = "Waiting";
    private static final String PAGE_RESULTS = "Results";
    private static final String PAGE_SCORE = "Score";

    private static JButton rock, paper, scissors;
    private static JPanel mainPanel, iconPanel;
    static JFrame frame;

    private static CardLayout cardLayout;
    private static Client gameClient;

    static void createGUI(Client client) {
        // Set up and create the JFrame window
        frame = new JFrame("Rock Paper Scissors");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gameClient = client;

        // Add pages to CardLayout
        mainPanel.add(selectionPage(), PAGE_SELECTION);
        mainPanel.add(waitingPage(), PAGE_WAITING);
        mainPanel.add(resultsPage("", "", ""), PAGE_RESULTS);
        mainPanel.add(scorePage("", "", ""), PAGE_SCORE);

        frame.add(mainPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Show the selection page initially
        cardLayout.show(mainPanel, PAGE_SELECTION);
    }

    private static JPanel selectionPage() {
        // Create panel with gridbag
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(174, 214, 241));

        // Set up GridBagConstraints for centering the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        // Create icon panel with null layout
        iconPanel = new JPanel(null);
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(640, 200));

        // Buttons and initial positions
        rock = createGameButton("resources/Rock.png", 0, 0);
        paper = createGameButton("resources/Paper.png", 220, 0);
        scissors = createGameButton("resources/Scissors.png", 420, 0);

        // Add click listeners to buttons
        rock.addActionListener(e -> onSelection("rock"));
        paper.addActionListener(e -> onSelection("paper"));
        scissors.addActionListener(e -> onSelection("scissors"));

        // Add buttons to icon panel
        iconPanel.add(rock);
        iconPanel.add(paper);
        iconPanel.add(scissors);

        panel.add(iconPanel, gbc);

        // Create label for game insturctions
        JLabel label = new JLabel("Click your object to play");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Update GridBagConstraints to place the label below the icon panel
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(label, gbc);

        return panel;
    }

    private static JPanel waitingPage() {
        // Create panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(241, 148, 138));

        // Add waiting image
        JLabel waitingImage = new JLabel(new ImageIcon("resources/wait.png"));
        waitingImage.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(waitingImage, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel resultsPage(String gameResult, String serverPlay, String roundWinner) {
        // Create panels
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(130, 224, 170));

        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setOpaque(false);

        // Add label for server's play
        JLabel serverPlayLabel = new JLabel("Server chose " + serverPlay, SwingConstants.CENTER);
        serverPlayLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        serverPlayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPanel.add(Box.createVerticalStrut(20));
        labelPanel.add(serverPlayLabel);

        // Add label for winner
        JLabel winnerLabel = new JLabel(roundWinner + " wins the round!", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        winnerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        labelPanel.add(Box.createVerticalStrut(10));
        labelPanel.add(winnerLabel);

        panel.add(labelPanel, BorderLayout.NORTH);

        // Add image for game result
        JLabel resultImage = new JLabel(new ImageIcon("resources/" + gameResult + ".png"));
        resultImage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultImage, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setOpaque(false);

        // Add continue label below the image
        JLabel bottomTextLabel = new JLabel("(Click to continue)", SwingConstants.CENTER);
        bottomTextLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        bottomTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        bottomPanel.add(bottomTextLabel);
        bottomPanel.add(Box.createVerticalStrut(20));
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // Add mouse listener to the panel to detect clicks
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(mainPanel, PAGE_SCORE);
            }
        });

        return panel;
    }

    private static JPanel scorePage(String clientScore, String serverScore, String ties) {
        // Create panel with gridbag
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(244, 208, 63));

        // Set up GridBagConstraints for centering the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add label for current score text
        JLabel scoreTextLabel = new JLabel("Current score");
        scoreTextLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(scoreTextLabel, gbc);
        gbc.gridy++;

        // Add label for scores
        JLabel scoreLabel = new JLabel("Wins: " + clientScore + " | Loses: " + serverScore + " | Ties: " + ties);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(scoreLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(20, 5, 5, 5);

        // Add label for playing again
        JLabel playAgainTextLabel = new JLabel("Would you like to play again?");
        playAgainTextLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(playAgainTextLabel, gbc);
        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Create a new panel for the buttons with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        // Create play again button and add listener
        JButton playAgainButton = new JButton("Yes");
        playAgainButton.setFont(new Font("Arial", Font.PLAIN, 18));
        playAgainButton.addActionListener(e -> cardLayout.show(mainPanel, PAGE_SELECTION));
        buttonPanel.add(playAgainButton);

        // Create quit game button and add listener
        JButton quitGameButton = new JButton("No");
        quitGameButton.setFont(new Font("Arial", Font.PLAIN, 18));
        quitGameButton.addActionListener(e -> {
            try {
                gameClient.close();
                frame.dispose();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
        buttonPanel.add(quitGameButton);

        panel.add(buttonPanel, gbc);

        return panel;
    }

    // Button template
    private static JButton createGameButton(String iconPath, int x, int y) {
        JButton button = new JButton("", new ImageIcon(iconPath));
        button.setBounds(x, y, 200, 200);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    // Actions when clicking game icon button
    private static void onSelection(String choice) {
        try {
            cardLayout.show(mainPanel, PAGE_WAITING);
            // Send user choice to server and parse response from server
            gameClient.getResultFromServer(choice, serverMessage -> {
                String[] data = serverMessage.split(",");
                String gameResult = data[0];
                String serverChoice = data[1];
                String roundWinner = data[2];
                String clientScore = data[3];
                String serverScore = data[4];
                String gameTies = data[5];

                JPanel resultsPanel = resultsPage(gameResult, serverChoice, roundWinner);
                mainPanel.add(resultsPanel, PAGE_RESULTS);

                JPanel scorePanel = scorePage(clientScore, serverScore, gameTies);
                mainPanel.add(scorePanel, PAGE_SCORE);

                cardLayout.show(mainPanel, PAGE_RESULTS);
            });
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while processing your selection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}