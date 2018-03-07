import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) throws Exception {
        InetAddress address = InetAddress.getByName(args[1]);
        Socket socket = new Socket(address, 80);
        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
        BufferedReader in = new BufferedReader(

                new InputStreamReader(socket.getInputStream()));

        // send an HTTP request to the web server
        out.println(args[0] + " / HTTP/1.1");
        out.println("Host: "+ args[1] + ":80");
        out.println("Connection: Close");
        out.println();

        // read the response
        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                while (i != -1) { //end of InputStream when next read int is -1
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }
        System.out.println(sb.toString());
        File file = new File("/Users/jeffreyquicken/Downloads/response.html");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sb.toString());
        } finally {
            if (writer != null) writer.close();
        }
        socket.close();
    }
}
