import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class XPackInstaller {

    public static String selected_url;
    public static boolean installLauncher;
    public static String launcher_path;
    public static int allocatedRAM;
    public static int javaVersion = 7;
    public static boolean canGoFoward;
    public static String modpack_path;
    public static String profile;

    public static void main(String[] args){
        try {
            String IMAGE_PATH = "Icon4.png";
            InputStream inputStream = Display.class.getResourceAsStream(IMAGE_PATH);
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Utils.Utils.setDensign();
            Display display = new Display();
            display.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            display.setSize(new Dimension(560, 375));
            display.setLocationRelativeTo(null);
            display.setVisible(true);
            display.setIconImage(bufferedImage);
            display.setResizable(false);
        } catch (IOException ex){
            ex.printStackTrace();
        }







    }

}
