import java.io.*;
import java.net.*;

public class Server {

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
                throw new IllegalStateException("Unexpected value: " + choice);
        }
    }

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

    public static void main(String[] args) {
        int clientScore = 0;
        int serverScore = 0;
        int ties = 0;

        boolean keepPlaying = true;

        try (ServerSocket serverSocket = new ServerSocket(37)) {
            System.out.println("Server is waiting for a connection...");
            Socket socket = serverSocket.accept();
            System.out.println("Connection established");

            try (DataInputStream in = new DataInputStream(socket.getInputStream());
                 DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {

                while (keepPlaying) {
                    try {
                        String userChoice = in.readUTF();
                        System.out.println("Client Played: " + userChoice);

                        if (!isValidChoice(userChoice)) {
                            System.out.println("Invalid user choice: " + userChoice);
                            out.writeUTF("Invalid choice. Terminating the game.");
                            break;
                        }

                        String serverChoice = choosePlay();
                        System.out.println("Server Played: " + serverChoice);

                        String roundWinner = winner(userChoice, serverChoice);
                        System.out.println("Round winner: " + roundWinner);

                        if (roundWinner.equals("Client")) {
                            clientScore++;
                        } else if (roundWinner.equals("Server")) {
                            serverScore++;
                        } else {
                            ties++;
                        }

                        out.writeUTF(String.format("%s,%s,%d,%d,%d", 
                            userChoice + "-" + serverChoice, roundWinner, clientScore, serverScore, ties));
                        out.flush();

                    } catch (IOException e) {
                        System.out.println("Connection lost or error occurred. Ending game.");
                        keepPlaying = false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error with the server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isValidChoice(String choice) {
        return choice.equalsIgnoreCase("rock") ||
               choice.equalsIgnoreCase("paper") ||
               choice.equalsIgnoreCase("scissors");
    }
}