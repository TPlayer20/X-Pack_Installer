package Utils.Validators;
import Utils.Reference;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

import static Utils.Utils.print;

public class java8Update {
    public static int check(){
        print("Sprawdzanie wersji JRE.");
        try {
            URL updateService = new URL(Reference.PROTOCOL, Reference.UPDATE_HOST, Reference.PORT, Reference.JAVA_UPDATE);
            String update = new Scanner(updateService.openStream(), "UTF-8").nextLine();
            String local = System.getProperty("java.version");
            String[] updateArray = update.split("_", 2);
            String[] localArray = local.split("_", 2);
            int localNumber = Integer.parseInt(localArray[1]);
            int updateNumber = Integer.parseInt(updateArray[1]);
            if(localArray[0].equals(updateArray[0])){
                if(updateNumber == localNumber ){ return 0;} else if( localNumber >= 25 && localNumber < updateNumber ) { return 1; } else if( localNumber < updateNumber) {return 2;}
            } else {
                return 2;
            }
        } catch (IOException e){
            print("Nie udało się sprawdzić aktualizacji JRE.");
        }
        return 2;
    }
}
