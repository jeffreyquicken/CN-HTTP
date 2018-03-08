import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

/**
 * The client does a given HTTP request to a given server, returns the response to the terminal and saves it to an HTML file.
 * When needed it analyses the response an requests the embedded HTML objects and stores them locally.
 *
 * @author  Jeffrey Quicken
 * @version 1.0
 * @since   2018-03-07
 */
public class Client {

    // Path where response is stored
    public static String RESPONSE_PATH = "/Users/jeffreyquicken/Downloads/response.html";

    // Opens a client socket and connects to the given server at the given port, outputs the response and saves it to disk
    public static void request(String command, String url, String path,  int port) throws Exception{
        InetAddress address = InetAddress.getByName(url);
        Socket socket = new Socket(address, port);
        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send an HTTP request to the server
        out.println(command + " " + path + " HTTP/1.1");
        out.println("Host: " + url);
        out.println("Connection: Close");
        out.println();

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

        socket.close();

        // saves the response to an html file
        File file = new File(RESPONSE_PATH);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(sb.toString());
        } finally {
            if (writer != null) writer.close();
        }
    }

    public static void main(String[] args) throws Exception {
        String command = args[0];
        String url = args[1];
        int port = Integer.parseInt(args[2]);
        Scanner scanner = new Scanner(System.in);

        switch (command) {
            case "GET":
                request(command, url,"/", port);

                // Parses the HTML file and extracts the links for each image found on the page
                File input = new File(RESPONSE_PATH);
                Document doc = Jsoup.parse(input, "UTF-8", "");
                Elements imgs = doc.getElementsByTag("img");
                for (Element img : imgs) {
                    //TODO Retreive found images with request and save to disk
                    System.out.println("image tag: " + img.attr("src"));
                }
                break;
            case "HEAD":
                request(command, url,"/", port);
                break;
            case "POST":
                //TODO read line from terminal and send forward
                System.out.print("Enter your message: ");
                String message = scanner.next();
                request(command, url,"/", port);
                break;
            case "PUT":
                request(command, url,"/", port);
                break;
        }
    }
}




