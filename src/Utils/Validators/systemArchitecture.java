package Utils.Validators;
import static Utils.Utils.*;

public class systemArchitecture {
    public static boolean check(){
        print("Sprawdzanie poprawności architektury systemu operacyjnego.");
        String OS = System.getenv("PROCESSOR_ARCHITECTURE");
        return OS.equals("AMD64");
    }
}
