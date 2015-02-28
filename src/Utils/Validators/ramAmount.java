package Utils.Validators;
import Utils.Utils;

import static Utils.Utils.print;

public class ramAmount {
    public static boolean check(){
        print("Sprawdzanie wymaganej ilości pamięci RAM.");
        int memoryAmount = Utils.humanReadableRAM();
        int requestedMemory = 4;
        return requestedMemory <= memoryAmount;

    }
}
