import java.io*;
import java.net*;

public class Client {
    public static void main(String[] args){
        try{
            //connect to server
            Socket socket = new Socket("localhost", 273);

            //open input/output streams
            BufferedReader in = newBufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //not sure about output stream since we're not printing messages?
            //have to look into that

            //recieve server's choice of rock [r], paper [p], scissors [s]
            String oppChoice = in.readLine();

            //compare choices

            //send message for server's result

            //display client's result

            //close connections
            in.close();
            //out.close();
            socket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}