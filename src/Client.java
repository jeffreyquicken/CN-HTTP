import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.net.URL;
//TODO savepath; single connection; saving images
/**
 * The client sends a given HTTP request to a given server, returns the response to the terminal and saves it to an HTML file.
 * When needed it analyses the response an requests the embedded HTML objects and stores them locally.
 *
 * @author  Jeffrey Quicken
 * @since   2018-03-07
 */
public class Client {

    // Path where response is stored
    public static String RESPONSE_PATH = "./response.html";

    // Opens a client socket and connects to the given server at the given port, outputs the response and saves it to disk
    public static void request(String command, String url, String path,  int port, String message, Boolean save) throws Exception{

        InetAddress address = InetAddress.getByName(url);
        Socket socket = new Socket(address, port);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        DataInputStream imageIn = new DataInputStream(socket.getInputStream());

        if (command.equals("GET") || command.equals("HEAD")) {
            // send an HTTP request to the server
            out.println(command + " " + path + " HTTP/1.1");
            out.println("Host: " + url);
            out.println("Connection: Keep-Alive");
            out.println("");
        }else if (command.equals("POST") || command.equals("PUT")){
            // send an HTTP request to the server
            out.println(command + " " + path + " HTTP/1.1");
            out.println("Host: " + url);
            out.println("Content-Type: plain/text");
            out.println(message);
            out.println("Connection: Close");
            out.println();
        }

        // read the response
        boolean loop = true;
        StringBuilder sb = new StringBuilder(8096);
        while (loop) {
            if (in.ready()) {
                int i = 0;
                //end of InputStream when next read int is -1
                while (i != -1) {
                    i = in.read();
                    sb.append((char) i);
                }
                loop = false;
            }
        }

        // prints the response to the terminal
        System.out.println(sb.toString());



        // saves the response to an html file
        File file = new File(RESPONSE_PATH);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sb.toString());
        } finally {
            if (writer != null) writer.close();
        }


        //TODO save image to disk
        if (save){
            // Parses the HTML file and extracts the links for each image found on the page
            File input = new File(RESPONSE_PATH);
            Document doc = Jsoup.parse(input, "UTF-8", "");
            Elements imgs = doc.getElementsByTag("img");
            for (Element img : imgs) {
                path = "/" + img.attr("src");
                // send an HTTP request to the server
                out.println(command + " " + path + " HTTP/1.1");
                out.println("Host: " + url);
                out.println("Connection: Keep-Alive");
                out.println();

                OutputStream dos = new FileOutputStream("."+ path);
                int count;
                byte[] buffer = new byte[2048];
                boolean eohFound = false;
                while ((count = imageIn.read(buffer)) != -1)
                {
                    if(!eohFound){
                        String string = new String(buffer, 0, count);
                        int indexOfEOH = string.indexOf("\r\n\r\n");
                        if(indexOfEOH != -1) {
                            count = count-indexOfEOH-4;
                            buffer = string.substring(indexOfEOH+4).getBytes();
                            eohFound = true;
                        } else {
                            count = 0;
                        }
                    }
                    dos.write(buffer, 0, count);
                    dos.flush();
                }
                in.close();
                dos.close();
            }

        }
        socket.close();
    }


    public static void main(String[] args) throws Exception {
        String command = args[0];
        // Parses the url and extracts the host and the path
        URL full = new URL(args[1]);
        String url = full.getHost();
        String path = full.getPath();

        // If homepage is given the path will be empty and the request will be bad unless the path is "/".
        if(path == null || path.isEmpty()) { path = "/";}
        int port = Integer.parseInt(args[2]);
        Scanner scanner = new Scanner(System.in);

        switch (command) {
            case "GET":
                request(command, url,path, port,"",false);
                break;
            case "HEAD":
                request(command, url,path, port,"", false);
                break;
            case "POST":
            case "PUT":
                // Read user input via terminal
                System.out.print("Enter your message: ");
                String message = scanner.next();

                // Send request with message
                request(command, url,path, 8080, message, false);
                break;
        }
    }
}




