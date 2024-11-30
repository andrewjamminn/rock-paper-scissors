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
    private static JFrame frame;

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
        mainPanel.add(resultsPage("", ""), PAGE_RESULTS);
        mainPanel.add(scorePage("", ""), PAGE_SCORE);

        frame.add(mainPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Show the selection page initially
        cardLayout.show(mainPanel, PAGE_SELECTION);
    }

    private static JPanel selectionPage() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(174, 214, 241));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);

        iconPanel = new JPanel(null);
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(640, 200));

        rock = createGameButton("resources/Rock.png", 0, 0);
        paper = createGameButton("resources/Paper.png", 220, 0);
        scissors = createGameButton("resources/Scissors.png", 420, 0);

        rock.addActionListener(e -> onSelection("rock"));
        paper.addActionListener(e -> onSelection("paper"));
        scissors.addActionListener(e -> onSelection("scissors"));

        iconPanel.add(rock);
        iconPanel.add(paper);
        iconPanel.add(scissors);

        panel.add(iconPanel, gbc);

        JLabel label = new JLabel("Click your object to play");
        label.setFont(new Font("Arial", Font.PLAIN, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(label, gbc);

        return panel;
    }

    private static JPanel waitingPage() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(241, 148, 138));

        JLabel waitingImage = new JLabel(new ImageIcon("resources/wait.png"));
        waitingImage.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(waitingImage, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel resultsPage(String gameResult, String roundWinner) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(130, 224, 170));

        JLabel winnerLabel = new JLabel(roundWinner + " wins the round!", SwingConstants.CENTER);
        winnerLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(winnerLabel, BorderLayout.NORTH);

        JLabel resultImage = new JLabel(new ImageIcon("resources/" + gameResult + ".png"));
        resultImage.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(resultImage, BorderLayout.CENTER);

        JLabel bottomTextLabel = new JLabel("(Click to continue)", SwingConstants.CENTER);
        bottomTextLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        panel.add(bottomTextLabel, BorderLayout.SOUTH);

        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                cardLayout.show(mainPanel, PAGE_SCORE);
            }
        });

        return panel;
    }

    private static JPanel scorePage(String clientScore, String serverScore) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(244, 208, 63));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel scoreLabel = new JLabel("Current score is " + clientScore + " to " + serverScore);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(scoreLabel, gbc);
        gbc.gridy++;

        JLabel playAgainTextLabel = new JLabel("Would you like to play again?");
        playAgainTextLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        panel.add(playAgainTextLabel, gbc);
        gbc.gridy++;

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton playAgainButton = new JButton("Yes");
        playAgainButton.setFont(new Font("Arial", Font.PLAIN, 18));
        playAgainButton.addActionListener(e -> cardLayout.show(mainPanel, PAGE_SELECTION));
        buttonPanel.add(playAgainButton);

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

    private static JButton createGameButton(String iconPath, int x, int y) {
        JButton button = new JButton("", new ImageIcon(iconPath));
        button.setBounds(x, y, 200, 200);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        return button;
    }

    private static void onSelection(String choice) {
        try {
            cardLayout.show(mainPanel, PAGE_WAITING);
            gameClient.getResultFromServer(choice, serverMessage -> {
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
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while processing your selection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}