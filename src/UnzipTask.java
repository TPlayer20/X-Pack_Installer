import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class UnzipTask extends SwingWorker<Void, Void> {

    private Display gui;

    public UnzipTask(Display gui){
        this.gui = gui;
    }

    @Override
    protected Void doInBackground() throws Exception {
        try {
            File dir0 = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "assets");
            File dir1 = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "config");
            File dir2 = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "libraries");
            File dir3 = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "mods");
            File dir4 = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "versions");
            File dir5 = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "launcher_profiles.json");
            if(dir0.exists()){
                FileUtils.deleteDirectory(dir0);
            }
            if(dir1.exists()){
                FileUtils.deleteDirectory(dir1);
            }
            if(dir2.exists()){
                FileUtils.deleteDirectory(dir2);
            }
            if(dir3.exists()){
                FileUtils.deleteDirectory(dir3);
            }
            if(dir4.exists()){
                FileUtils.deleteDirectory(dir4);
            }
            if(dir5.exists()){
                FileUtils.forceDelete(dir5);
            }
            ZipFile zipFile = new ZipFile(XPackInstaller.modpack_path);
            net.lingala.zip4j.progress.ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
            zipFile.setRunInThread(true);
            zipFile.extractAll(System.getenv("appdata"));
            while(progressMonitor.getPercentDone() != 100){
                setProgress(progressMonitor.getPercentDone());
            }

        } catch (ZipException e){
            e.printStackTrace();
        }
        return null;
    }
    @SuppressWarnings({"unchecked", "ResultOfMethodCallIgnored"})
    @Override
    protected void done(){
        if(!isCancelled()){
            try {
                File file = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "launcher_profiles.json");
                JSONObject objecthead = new JSONObject();
                JSONObject profiles = new JSONObject();
                JSONObject XPack = new JSONObject();
                XPack.put("name", XPackInstaller.profile);
                XPack.put("lastVersionId", XPackInstaller.profile);
                XPack.put("javaDir", System.getProperty("java.home") + File.separator + "bin" + File.separator + "javaw.exe");
                int unit = 1024;
                Integer XMX = XPackInstaller.allocatedRAM * unit;
                Integer XMS = 1024;
                if(XPackInstaller.javaVersion == 8){
                    XPack.put("javaArgs", "-Xmx" + XMX.toString() + "m " + "-Xms" + XMS.toString() + "m " + "-Dfml.ignoreInvalidMinecraftCertificates=true -Dfml.ignorePatchDiscrepancies=true");
                } else {
                    XPack.put("javaArgs", "-Xmx" + XMX.toString() + "m " + "-Xms" + XMS.toString() + "m " + "-XX:MaxPermSize=" + XMX.toString() + "m " + "-XX:PermSize=" + XMS.toString() + "m " + "-Dfml.ignoreInvalidMinecraftCertificates=true -Dfml.ignorePatchDiscrepancies=true");
                }

                XPack.put("useHopperCrashService", false);
                profiles.put(XPackInstaller.profile, XPack);
                objecthead.put("profiles", profiles);
                objecthead.put("selectedProfile", XPackInstaller.profile);
                if(file.exists()){
                    file.delete();
                    file.createNewFile();
                } else {
                    file.createNewFile();
                }
                FileWriter fileWriter = new FileWriter(file, false);
                fileWriter.write(objecthead.toJSONString());
                fileWriter.close();
                File mainDir = new File(System.getenv("appdata") + File.separator + "XPackInstaller");
                File optionalDir = new File(mainDir.getAbsolutePath() + File.separator + "OptionalMods");
                File outDir = new File(System.getenv("appdata") + File.separator + ".minecraft" + File.separator + "mods");
                File[] fileList = optionalDir.listFiles();
                if (fileList != null && fileList.length != 0){
                    for(File x: fileList){
                        FileUtils.copyFileToDirectory(x, outDir);
                    }
                }
                setProgress(100);
                gui.changeAfterModpackInstall();
            } catch (IOException e){
                e.printStackTrace();
            }

        }
    }
}
