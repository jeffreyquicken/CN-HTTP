

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;




/**
 * The ChatClient program implements the client side of an HTTP client-server model.
 * It can send requests to a server and receive a response.
 * The response is stored in an HTML file and is processed to retreive embedded objects from the server.
 *
 * @author  Jeffrey Quicken
 * @version 1.0
 * @since   2018-03-07
 */
public class ChatClient {
    public static void main(String[] args) throws Exception {
        InetAddress address = InetAddress.getByName(args[1]);
        Socket socket = new Socket(address, 80);
        boolean autoflush = true;
        PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // send an HTTP request to the web server
        out.println(args[0] + " / HTTP/1.1");
        out.println("Host: "+ args[1]);
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

        // Parses the HTML file and extracts the links for each image found on the page
        Document doc = Jsoup.parse(file, "UTF-8", "");
        Elements imgs = doc.select("img[src]");
        for (Element img : imgs) {
            System.out.println("\nimg : " + img.attr("href"));
            System.out.println("text : " + img.text());
        }
    }


}
