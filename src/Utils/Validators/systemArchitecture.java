package Utils.Validators;
import static Utils.Utils.*;

public class systemArchitecture {
    public static boolean check(){
        print("Sprawdzanie poprawności architektury systemu operacyjnego.");
        String OS = System.getProperty("os.arch");
        return OS.equals("amd64");
    }
}
