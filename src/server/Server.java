// Importing classes for input and output streams, networking, and delay management.
import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

// Defining the server
public class Server {

    // Randomly choose between rock, paper, or scissors for the server.
    public static String choosePlay() {
        int choice = 1 + ((int) (Math.random() * 3));
        switch (choice) {
            case 1:
                return "rock";
            case 2:
                return "paper";
            case 3:
                return "scissors";
            default:
                // Handles unexpected cases
                throw new IllegalStateException("Unexpected value: " + choice);
        }
    }

    // Determine the winner of the round based on user and server choices.
    public static String winner(String userChoice, String serverChoice) {
        if (userChoice.equals(serverChoice)) {
            return "No one";
        }
        if ((userChoice.equals("rock") && serverChoice.equals("scissors")) ||
            (userChoice.equals("paper") && serverChoice.equals("rock")) ||
            (userChoice.equals("scissors") && serverChoice.equals("paper"))) {
            return "Client";
        }
        return "Server";
    }

    // Main method to run the server.
    public static void main(String[] args) {
        // Keep track of scores for client, server, and tie.
        int clientScore = 0;
        int serverScore = 0;
        int ties = 0;

        // Flag to control game loop.
        boolean keepPlaying = true;


        try (ServerSocket serverSocket = new ServerSocket(37)) { // Creates socket on port 37
            System.out.println("Server is waiting for a connection...");
            Socket socket = serverSocket.accept(); // Waits for a client connection.
            System.out.println("Connection established");

            // Input and output streams for client communication.
            try (DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
                
                // Main game loop.
                while (keepPlaying) {
                    try {
                        // Read the clients choice.
                        String userChoice = in.readUTF();
                        System.out.println("Client Played: " + userChoice);
                        
                        // Validate clients choice.
                        if (!isValidChoice(userChoice)) {
                            System.err.println("Invalid user choice: " + userChoice + ". Terminating the game.");
                            break;
                        }

                        // Generate servers choice.
                        String serverChoice = choosePlay();
                        System.out.println("Server Played: " + serverChoice);
                        
                        // Determine the winner of the round.
                        String roundWinner = winner(userChoice, serverChoice);
                        System.out.println("Round winner: " + roundWinner);

                        // Update the scores.
                        if (roundWinner.equals("Client")) {
                            clientScore++;
                        } else if (roundWinner.equals("Server")) {
                            serverScore++;
                        } else {
                            ties++;
                        }
                        System.out.println("Score: Client = " + clientScore + " | Server = " + serverScore + " | Ties = " + ties);

                        // Delay simulation.
                        TimeUnit.MILLISECONDS.sleep(500);

                        // Send the results of the round to the client.
                        out.writeUTF(String.format("%s,%s,%s,%d,%d,%d", 
                            userChoice + "-" + serverChoice, serverChoice, roundWinner, clientScore, serverScore, ties));
                        out.flush(); // Make sure data is sent.
                    } catch (IOException | InterruptedException e) {
                        System.err.println("Connection lost or error occurred. Ending game.");
                        keepPlaying = false; // End game.
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error with the server: " + e.getMessage()); // Handle server socket errors.
            e.printStackTrace();
        }
    }

    // Validate the users choice.
    private static boolean isValidChoice(String choice) {
        return choice.equalsIgnoreCase("rock") ||
               choice.equalsIgnoreCase("paper") ||
               choice.equalsIgnoreCase("scissors");
    }
}