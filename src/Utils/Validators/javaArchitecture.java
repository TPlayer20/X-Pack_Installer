package Utils.Validators;

import static Utils.Utils.*;

public class javaArchitecture {

    public static boolean check(){
        print("Sprawdzanie poprawności architektury JRE.");
        String jre = System.getProperty("sun.arch.data.model");
        return jre.equals("64");
    }

}
