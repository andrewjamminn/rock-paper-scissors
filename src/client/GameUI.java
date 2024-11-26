import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameUI {

    private static final String PAGE_SELECTION = "Selection";
    private static final String PAGE_WAITING = "Waiting";
    private static final String PAGE_RESULTS = "Results";
    private static final String PAGE_SCORE = "Score";

    private static JButton rock, paper, scissors;
    private static JPanel iconPanel;
    private static JPanel mainPanel;

    private static CardLayout cardLayout;
    private static Client gameClient;

    static void createGUI(Client client) {
        // Set up and create the JFrame window
        JFrame frame = new JFrame("Rock Paper Scissors");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create main panel with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gameClient = client;

        // Add pages to CardLayout
        mainPanel.add(selectionPage(), PAGE_SELECTION);
        mainPanel.add(waitingPage(), PAGE_WAITING);
        mainPanel.add(resultsPage("", ""), PAGE_RESULTS);
        mainPanel.add(scorePage("", ""), PAGE_SCORE);

        frame.add(mainPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Show the selection page initially
        cardLayout.show(mainPanel, PAGE_SELECTION);
    }

    private static JPanel selectionPage() {
        // Create panel
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(174, 214, 241));

        // Create container for vertically aligning elements
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(0, 0, 20, 0);

        // Create icon panel with null layout for sliding animations
        iconPanel = new JPanel(null);
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(640, 200));

        // Button size and initial positions
        rock = createGameButton("resources/Rock.png", 0, 0);
        paper = createGameButton("resources/Paper.png", 220, 0);
        scissors = createGameButton("resources/Scissors.png", 420, 0);

        // Add click listeners
        rock.addActionListener(e -> onSelection("rock"));
        paper.addActionListener(e -> onSelection("paper"));
        scissors.addActionListener(e -> onSelection("scissors"));

        // Add buttons to icon panel
        iconPanel.add(rock);
        iconPanel.add(paper);
        iconPanel.add(scissors);

        // Add icon panel to the panel with GridBagConstraints
        panel.add(iconPanel, constraints);

        // Create instruction label
        JLabel label = new JLabel("Click your object to play");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        // Update GridBagConstraints to place the label below the icon panel
        constraints.gridy = 1;
        constraints.insets = new Insets(0, 0, 0, 0);
        panel.add(label, constraints);

        return panel;
    }

    private static JPanel waitingPage() {
        // Set up panel
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(241, 148, 138));

        // Add waiting image
        JLabel waitingImage = new JLabel(new ImageIcon("resources/wait.png"));
        waitingImage.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(waitingImage, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel resultsPage(String gameResult, String roundWinner) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(130, 224, 170));
    
        // Add winner label
        JLabel winnerLabel = new JLabel(roundWinner + " wins the round!", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(winnerLabel, BorderLayout.NORTH);

        // Add result image
        JLabel resultImage = new JLabel(new ImageIcon("resources/" + gameResult + ".png"));
        resultImage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultImage, BorderLayout.CENTER);
    
        // Add label below the image
        JLabel bottemTextlabel = new JLabel("(Click to continue)", SwingConstants.CENTER);
        bottemTextlabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(bottemTextlabel, BorderLayout.SOUTH);
    
        // Add a mouse listener to the panel to detect clicks
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(mainPanel, PAGE_SCORE);
            }
        });
    
        return panel;
    }
    
    private static JPanel scorePage(String clientScore, String serverScore) {
        // Create the panel with GridBagLayout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(244, 208, 63));

        // Set up GridBagConstraints for centering the components
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // center horizontally
        gbc.gridy = 0; // start from the top vertically
        gbc.anchor = GridBagConstraints.CENTER; // center the component in the grid cell
        gbc.insets = new Insets(5, 5, 5, 5); // add some space around components

        // Create and add the score label
        JLabel scoreLabel = new JLabel("Current score is " + clientScore + " to " + serverScore);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(scoreLabel, gbc);

        // Move down for the next component
        gbc.gridy++;

        // Create and add the "Would you like to play again?" text
        JLabel playAgainTextLabel = new JLabel("Would you like to play again?");
        playAgainTextLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(playAgainTextLabel, gbc);

        // Move down for the next component
        gbc.gridy++;

        // Create and add the "Play Again" button
        JButton playAgainButton = new JButton("Play Again");
        playAgainButton.setFont(new Font("Arial", Font.PLAIN, 18));
        playAgainButton.addActionListener(e -> cardLayout.show(mainPanel, PAGE_SELECTION));
        panel.add(playAgainButton, gbc);

        return panel;
    }

    private static JButton createGameButton(String iconPath, int x, int y) {
        JButton button = new JButton("", new ImageIcon(iconPath));
        button.setBounds(x, y, 200, 200);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private static void onSelection(String choice) {
        // Send the user's choice to the server
        try {
            gameClient.sendChoice(choice);

            // Transition to the waiting page
            cardLayout.show(mainPanel, PAGE_WAITING);

            // Start communication with the server
            gameClient.getResultFromServer(choice, serverMessage -> {

                // Parse server response
                String[] data = serverMessage.split(",");

                String gameResult = data[0];
                String roundWinner = data[1];
                String clientScore = data[2];
                String serverScore = data[3];

                JPanel resultsPanel = resultsPage(gameResult, roundWinner);
                mainPanel.add(resultsPanel, PAGE_RESULTS);

                JPanel scorePanel = scorePage(clientScore, serverScore);
                mainPanel.add(scorePanel, PAGE_SCORE);

                cardLayout.show(mainPanel, PAGE_RESULTS);
        });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
