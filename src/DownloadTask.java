import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Execute file download in a background thread and update the progress.
 * @author www.codejava.net
 *
 */
public class DownloadTask extends SwingWorker<Void, Void> {
    private static final int BUFFER_SIZE = 4096;
    private String downloadURL;
    private String saveDirectory;
    private String type;
    private Display gui;

    public DownloadTask(Display gui, String type, String downloadURL, String saveDirectory) {
        this.gui = gui;
        this.type = type;
        this.downloadURL = downloadURL;
        this.saveDirectory = saveDirectory;
    }

    /**
     * Executed in background thread
     */
    @Override
    protected Void doInBackground() throws Exception {
        try {
            HTTPDownloadUtil util = new HTTPDownloadUtil();
            util.downloadFile(downloadURL);

            if(type.equals("pack")){
                XPackInstaller.modpack_path = System.getenv("appdata") + System.getProperty("file.separator") +  "XPackInstaller" + System.getProperty("file.separator") + util.getFileName();
            }

            String saveFilePath = saveDirectory + File.separator + util.getFileName();

            InputStream inputStream = util.getInputStream();
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead;
            long totalBytesRead = 0;
            int percentCompleted;
            long fileSize = util.getContentLength();

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalBytesRead += bytesRead;
                percentCompleted = (int) (totalBytesRead * 100 / fileSize);

                setProgress(percentCompleted);
            }
            outputStream.close();

            util.disconnect();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error downloading file: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            setProgress(0);
            cancel(true);
        }
        return null;
    }

    /**
     * Executed in Swing's event dispatching thread
     */
    @Override
    protected void done() {
        if (!isCancelled()) {
            if(type.equals("pack")){
                gui.changeAfterModpack();
            } else if (type.equals("launcher")){
                gui.changeAfterLauncher();
            }

        }
    }
}