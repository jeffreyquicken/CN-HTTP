

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;


/**
 * The Client program implements the client side of an HTTP client-server model.
 * It can send requests to a server and receive a response.
 * The response is stored in an HTML file and is processed to retreive embedded objects from the server.
 *
 * @author  Jeffrey Quicken
 * @version 1.0
 * @since   2018-03-07
 */
public class Client {

    private static void request(String command, String url, int port) throws Exception{
        InetAddress address = InetAddress.getByName(url);
        Socket socket = new Socket(address, port);
        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send an HTTP request to the web server
        out.println(command + " / HTTP/1.1");
        out.println("Host: "+ url);
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
        File file = new File("/Users/jeffreyquicken/Downloads/response.html");
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

        switch (command){
            case "HEAD":
                request(command, url, port);
            case "GET":
                request(command, url, port);
            case "PUT":
                request(command, url, port);
            case "POST":
                request(command, url, port);

        }

//        // Parses the HTML file and extracts the links for each image found on the page
//        Document doc = Jsoup.parse(file, "UTF-8", "");
//        Elements imgs = doc.select("img[src]");
//        for (Element img : imgs) {
//            System.out.println("\nimg : " + img.attr("abs:src"));
//            System.out.println("text : " + img.text());
//        }
    }


}
