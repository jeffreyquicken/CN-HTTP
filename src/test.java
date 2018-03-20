import java.io.File;
import java.nio.file.Files;
import java.util.Date;

public class test {
    public static void main(String[] args) throws Exception {
        File file = new File("./webpage.html");
        System.out.println(file.length());
    }
}
