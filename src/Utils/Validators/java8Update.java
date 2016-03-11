package Utils.Validators;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

import static Utils.Utils.print;

public class java8Update {
    public static int check(){
        print("Sprawdzanie wersji JRE.");
        try {
            URL updateServer = new URL("http://xpack.pl/java8version.txt");
            Scanner scanner = new Scanner(updateServer.openStream(), "UTF-8");
            String temp = scanner.nextLine();
            StringTokenizer tokenizer = new StringTokenizer(temp, " =");
            tokenizer.nextToken();
            int update = Integer.parseInt(tokenizer.nextToken());
            String local = System.getProperty("java.version");
            String[] data = local.split("_");
            int localNumber = Integer.parseInt(data[1]);
            if(data[0].equals("1.8.0")){
                if(localNumber >= update){ return 0;} else if( localNumber >= 25 && localNumber < update ) { return 1; } else if( localNumber < update) {return 2;}
            } else {
                return 2;
            }
        } catch (IOException e){
            print("Nie udało się sprawdzić aktualizacji JRE.");
        }
        return 2;
    }
}
