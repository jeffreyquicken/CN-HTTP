import java.net.*;
import java.io.*;
import java.util.Date;

/**
 * The server responds to the given request from the client. It sends the appropriate error response codes when needed.
 *
 * @author  Jeffrey Quicken
 * @since   2018-03-07
 */
public class Server extends Thread {
    private static String AVAILABLE_FILE = "./webpage.html";

    public void run(){
        try {
            // creates serversocket
            ServerSocket serverSocket = new ServerSocket(8080);
            while(true){

                Socket socket = serverSocket.accept();

                // initializes reader and writer for input and output
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                String line;

                //handle request based upon input from client
                if((line = reader.readLine()) != null){
                    //read request line and parse the first line
                    String [] parsed = line.split("[/]");
                    String command = parsed[0];
                    String path = parsed[1];

                    // if the given path doesn't exist a 404 will be sent
                    if(!path.equals(AVAILABLE_FILE)){
                        writer.println("HTTP/1.1 404 Not Found");
                    }

                    Date date;

                    // selects the right response to be sent based upon the request from the client
                    switch (command) {
                        case "GET":
                            writer.println("HTTP/1.1 200 OK");
                            date = new Date();
                            writer.println(date);
                            writer.println("Content-Type: text/html");
                            File file = new File("./webpage.html");
                            long length = file.length();
                            writer.println("Content-length: " + length);
                            //TODO read html file and send as body to client
                            break;
                        case "HEAD":
                            writer.println("HTTP/1.1 200 OK");
                            date = new Date();
                            writer.println(date);
                            File hFile = new File("./webpage.html");
                            long hLength = hFile.length();
                            writer.println("Content-length: " + hLength);
                            writer.println("Content-Type: text/html");
                            break;
                        case "POST":
                            //TODO parse http request and extract body, append to existing file or create new file
                            writer.println("HTTP/1.1 200 OK");
                            date = new Date();
                            writer.println(date);
                            break;
                        case "PUT":
                            //TODO parse http request and extract body, save to new file
                            writer.println("HTTP/1.1 200 OK");
                            date = new Date();
                            writer.println(date);
                            break;
                        default: // sends a 400 error when command given by client isn't recognised
                            writer.println("HTTP/1.1 400 Bad Request");
                            break;
                    }
                }
                // close the reader, writer and socket
                reader.close();
                writer.close();
                socket.close();
            }
        } catch (Exception e) {
            //TODO send 500 Server Error
        }
    }


}
