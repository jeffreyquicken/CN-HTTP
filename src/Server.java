import java.net.*;
import java.io.*;

public class Server extends Thread {
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
                    if(line.contains("GET")){
                        answer = "bla";
                    }
                    else if (line.contains("HEAD"){
                        answer = "kdjkd";
                    }
                    else if (line.contains("PUT"){
                        answer = "kdjkd";
                    }
                    else if (line.contains("POST"){
                        answer = "kdjkd";
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
