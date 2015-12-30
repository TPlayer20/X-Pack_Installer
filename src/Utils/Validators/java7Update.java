package Utils.Validators;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

public class java7Update {
    public static int check(){
        try {
            URL updateServer = new URL("http://xpack.pl/javaversion.txt");
            Scanner scanner = new Scanner(updateServer.openStream(), "UTF-8");
            String temp = scanner.nextLine();
            StringTokenizer tokenizer = new StringTokenizer(temp, " =");
            tokenizer.nextToken();
            int update = Integer.parseInt(tokenizer.nextToken());
            String local = System.getProperty("java.version");
            String[] data = local.split("_");
            if(data[0].equals("1.7.0")){
                if(update <= Integer.parseInt(data[1])){return 0;}else{return 2;}
            } else {
                return 2;
            }
        } catch (IOException E){
            E.printStackTrace();
        }

        return 2;
    }
}
