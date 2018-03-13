import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.net.URL;
/**
 * The client does a given HTTP request to a given server, returns the response to the terminal and saves it to an HTML file.
 * When needed it analyses the response an requests the embedded HTML objects and stores them locally.
 *
 * @author  Jeffrey Quicken
 * @version 1.0
 * @since   2018-03-07
 */
public class test {


    public static void main(String[] args) throws Exception {
        System.out.println(args[0]);
        URL url = new URL(args[0]);
        //System.out.println(url.getHost());
        System.out.println(url.getPath());
    }
}




