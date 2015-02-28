package Utils;

import com.sun.management.OperatingSystemMXBean;

import javax.swing.*;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public static void print(Object x){

        System.out.println(getCurrentTime("'['HH:mm:ss']' ") +x);

    }

    public static String getCurrentTime(String format){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(calendar.getTime());

    }

    public static void setDesign(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public static int humanReadableRAM() {
        long bytes = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
        int unit = 1024;
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        return Math.round((float) (bytes / Math.pow(unit, exp)));
    }



}
