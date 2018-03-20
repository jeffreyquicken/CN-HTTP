import java.net.*;
import java.io.*;
import java.util.Date;


public class Server extends Thread {
    public static String AVAILABLE_FILE = "./webpage.html";

    public void run(){
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            while(true){
                Socket socket = serverSocket.accept();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                String answer = "";
                String line = null;

                //handle request

                if((line = reader.readLine()) != null){
                    //read request line
                    String [] parsed = line.split("[/]");
                    String command = parsed[0];
                    String path = parsed[1];

                    if(!path.equals(AVAILABLE_FILE)){
                        writer.println("HTTP/1.1 404 Not Found");
                    }
                    Date date = new Date();
                    switch (command) {
                        case "GET":
                            writer.println("HTTP/1.1 200 OK");
                            date = new Date();
                            writer.println(date);
                            writer.println("Content-Type: text/html");
                            File file = new File("./webpage.html");
                            long length = file.length();
                            writer.println("Content-length: " + length);
                            //TODO send body to client
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
                            writer.println("HTTP/1.1 200 OK");
                            date = new Date();
                            writer.println(date);
                            break;
                        case "PUT":
                            writer.println("HTTP/1.1 200 OK");
                            date = new Date();
                            writer.println(date);
                            break;
                        default:
                            writer.println("HTTP/1.1 400 Bad Request");
                            break;
                    }
                }
                reader.close();
                writer.close();
                socket.close();
            }
        } catch (Exception e) {
            //TODO send 500 Server Error
        }
    }


}
