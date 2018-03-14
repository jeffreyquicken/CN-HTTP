import java.util.Date;

public class test {
    public static void main(String[] args) throws Exception{
        String line = "GET /path HTTP/1.1";
        String [] parsed = line.split("[ ]");
        for (String s : parsed){
            System.out.println(s);
            Date date = new Date();
            System.out.println(date);
        }

    }
}
