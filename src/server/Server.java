import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

public class Server {

    //server chooses rock [1], paper [2], scissors [3]
    public static int choosePlay(){
        return 1+((int)(Math.random()*((3-1)+1)));
    }

    public static void main(String[] args){
        //starts server and waits for connection
        try{
            ServerSocket server = new ServerSocket(37);
            //accept client connection
            Socket socket = server.accept();
            System.out.println("Connection established");
            //taking input from client socket
            DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			// Sends output to the socket
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            


			System.out.println("Client Played: " + in.readUTF());
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Args: gameResult, roundWinner, clientScore, serverScore
			out.writeUTF(String.format("%s,%s,%d,%d", "paper-paper", "Client", 4, 2));
			out.flush();


    /*        
            //initial input value 0 to start game from scratch
            int clientPlay;
            int playAgain = 0;

			// Get client selection with in.readUTF();
            
            
            // INPUTS:
            // [1] -> rock
            // [2] -> paper
            // [3] -> scissors
            
            while(playAgain!=1) {
            	try {
            	//read client play selection
            	clientPlay = in.read();
            	System.out.println("Client Played: " + clientPlay);

            	//generate server play
            	int serverPlay = choosePlay();
            	System.out.println(serverPlay);
            	//compute result
            	
            	//DRAW
            	if (clientPlay == serverPlay){
            		System.out.println("DRAW");
            	}
            	//CLIENT PLAYS ROCK
            	else if (clientPlay==1) {
            		//SERVER PLAYS PAPER
            		if (serverPlay==2) {
            			System.out.println("Server played paper -- LOSS");
            		}
            		//SERVER PLAYS SCISSORS
            		else if (serverPlay==3) {
            			System.out.println("Server played scissors -- WIN");
            		}
            	}
            	//CLIENT PLAYS PAPER
            	else if (clientPlay==2) {
            		//SERVER PLAYS ROCK
            		if(serverPlay==1) {
            			System.out.println("Server played rock -- WIN");
            		}
            		//SERVER PLAYS SCISSORS
            		else if(serverPlay==3) {
            			System.out.println("Server played scissors -- LOSE");
            		}            		
            	}
            	//CLIENT PLAYS SCISSORS
            	else if (clientPlay==3) {
            		//SERVER PLAYS ROCK
            		if (serverPlay==1) {
            			System.out.println("Server played rock -- LOSE");
            		}
            		//SERVER PLAYS PAPER
            		else if(serverPlay==2) {
            			System.out.println("Server played paper -- WIN");
            		}
            	}
            	playAgain = in.read();
            	System.out.println("Play Again Result: " + playAgain);
            	}
            	catch (IOException e) {
            		e.printStackTrace();
            	}
            }
	*/		

            // close server and data streams
            server.close();
            in.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }
}
