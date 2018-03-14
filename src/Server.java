import java.net.*;
import java.io.*;
import java.util.Date;


public class Server extends Thread {
    public static String AVAILABLE_FILE = "/webpage.html";

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

                    switch (command) {
                        case "GET":
                            writer.println("HTTP/1.1 200 OK");
                            Date date = new Date();
                            writer.println(date);
                            break;
                        case "HEAD":
                            break;
                        case "POST":
                            break;
                        case "PUT":
                            break;
                        default:
                            writer.println("HTTP/1.1 400 Bad Request");
                            break;
                    }
                }
                //send answer back
                writer.print(answer + "\n");
                writer.flush();
                reader.close();
                writer.close();
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
