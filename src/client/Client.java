import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Client {

	private Socket socket;
	private DataOutputStream out;
    private DataInputStream in;

	public static void main(String[] args){
        try {
        	// Establish connection to the server
            Client client = new Client();
			client.startClient();
        
        	SwingUtilities.invokeLater(() -> GameUI.createGUI(client));

        }
        catch (IOException e) {
        	e.printStackTrace();
        }
    }

    public void startClient() throws IOException {
        // Establish a connection 
		socket = new Socket("localhost", 37);
		// Reads input from terminal
		in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		// Sends output to the socket
		out = new DataOutputStream(socket.getOutputStream());
    }

	public void getResultFromServer(String userChoice, ResultCallback callback) {
		new Thread(() -> {
			try {
				// Send the user's choice to the server
				out.writeUTF(userChoice);
				out.flush();

				// Set a timeout for server response
				socket.setSoTimeout(5000);
	
				// Wait for the server's response
				String serverResponse = in.readUTF();
	
				// Notify the UI on the Swing thread
				SwingUtilities.invokeLater(() -> callback.onResultReceived(serverResponse));
			} catch (SocketTimeoutException e) {
				System.err.println("Server did not respond in time. Closing client.");
				exit();
			} catch (IOException e) {
				System.err.println("Connection to server lost. Closing client.");
				exit();
			}
		}).start();
	}
	
	// Define a callback interface for passing the result to the UI
	public interface ResultCallback {
		void onResultReceived(String result);
	}

	// Cleanup and exit game
	private void exit() {
		try {
			close();
			GameUI.frame.dispose();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
    // Close the connection
    public void close() throws IOException {
        socket.close();
        out.close();
        in.close();
    }
}