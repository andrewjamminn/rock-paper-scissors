import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args){
        try {
        	//establish a connection 
        	Socket socket = new Socket("localhost", 37);
        	//reads input from terminal
        	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        	//sends output to the socket
        	DataOutputStream toSocket = new DataOutputStream(socket.getOutputStream());
        	
        	int playAgain=0;
        	int clientPlay;
        	
        	while(playAgain!=1) {
        		System.out.println("Rock [1], Paper [2], Scissors [3]");
        		//client chooses rock/paper/scissors
        		clientPlay = Integer.parseInt(input.readLine());
        		//sends choice to server
        		toSocket.write(clientPlay);
        		
        		System.out.println("Play again? [0] yes, [1] no");
        		//client chooses to play again
        		playAgain = Integer.parseInt(input.readLine());
        		//sends choice to server
        		toSocket.write(playAgain);
        	}
        	
        	//close connection and streams
        	socket.close();
        	input.close();
        	toSocket.close();
        }
        catch (UnknownHostException e) {
        	e.printStackTrace();
        }
        catch (IOException e) {
        	e.printStackTrace();
        }
    }
}