import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class XPackInstaller {

    static String selected_url;
    static boolean installLauncher = false;
    static String launcher_path;
    static int allocatedRAM = 2;
    static int javaVersion = 8;
    static boolean canGoForward;
    static String modpack_path;
    static String profile;
    static String urlOptifine;
    static String urlMoC;
    static String urlCMS;

    public static void main(String[] args){
        try {
            String IMAGE_PATH = "Icon4.png";
            InputStream inputStream = Display.class.getResourceAsStream(IMAGE_PATH);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Utils.Utils.setDesign();
            Display display = new Display();
            display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            display.setSize(new Dimension(560, 400));
            display.setLocationRelativeTo(null);
            display.setVisible(true);
            display.setIconImage(bufferedImage);
            display.setResizable(false);
        } catch (IOException ex){
            ex.printStackTrace();
        }

    }

}
