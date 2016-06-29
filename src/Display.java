import Utils.Reference;
import jdk.nashorn.api.scripting.URLReader;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.URL;

public class Display extends JFrame{
    private JPanel panel;
    private JButton button2;
    private JPanel panel1;
    private JPanel panel2;
    private JLabel osarch;
    private JLabel Ram;
    private JLabel javaarch;
    private JLabel javaversion;
    private JCheckBox licenseC;
    private JScrollPane scrollPane1;
    private JEditorPane editorPane1;
    private JButton dalejButton;
    private JPanel panel3;
    private JComboBox comboBox1;
    private JCheckBox pobierzOryginalnyLauncherMCCheckBox;
    private JButton button1;
    private JTextField textField1;
    private JButton button3;
    private JSlider slider1;
    private JLabel PathLauncherLabel;
    private JPanel panel4;
    private JProgressBar progressBar1;
    private JProgressBar progressBar2;
    private JProgressBar progressBar3;
    private JButton zamknijButton;
    private JLabel labelInstall;
    private JLabel labelmodpack;
    private JLabel labelLauncher;
    private JButton wsteczButton;
    private JCheckBox checkOptifine;
    private JCheckBox zainstalujMoCreaturesCheckBox;
    private JLabel optionalText;
    private CardLayout cardLayout;
    private Display gui = this;
    private int optifineProgress = 0;
    private int MocProgress = 0;
    private int CmsProgress = 0;
    private int modpackProgress = 0;

    @SuppressWarnings("unchecked")
    Display(){
        super(Reference.NAME);
        cardLayout = new CardLayout();
        panel.setLayout(cardLayout);
        final int java7Update = Utils.Validators.java7Update.check();
        final int java8Update = Utils.Validators.java8Update.check();
        try {
            URL host = new URL(Reference.PROTOCOL, Reference.SOURCE_HOST, Reference.PORT, Reference.JSON_ARRAY);
            JSONParser parser = new JSONParser();
            Object json =  parser.parse(new URLReader(host));
            final JSONArray array = (JSONArray) json;
            JSONObject nameObject1 = (JSONObject) array.get(0);
            XPackInstaller.selected_url = nameObject1.get("url").toString();
            XPackInstaller.javaVersion = Integer.parseInt(nameObject1.get("version").toString());
            XPackInstaller.profile = nameObject1.get("profile").toString();
            XPackInstaller.canAcceptOptional = Boolean.parseBoolean(nameObject1.get("canAcceptOptional").toString());
            if(!XPackInstaller.canAcceptOptional){
                checkOptifine.setEnabled(false);
                checkOptifine.setSelected(false);
                zainstalujMoCreaturesCheckBox.setEnabled(false);
                zainstalujMoCreaturesCheckBox.setSelected(false);
                optionalText.setEnabled(false);
            } else {
                checkOptifine.setEnabled(true);
                zainstalujMoCreaturesCheckBox.setEnabled(true);
                optionalText.setEnabled(true);
            }
            for (Object anArray : array) {
                comboBox1.addItem(new JSONObject((JSONObject) anArray).get("name"));
            }
            comboBox1.addActionListener(e -> {
                int i = 0;
                while(true){
                    JSONObject nameObject = (JSONObject) array.get(i);
                    String name1 = nameObject.get("name").toString();
                    if(name1 ==comboBox1.getSelectedItem()){
                        XPackInstaller.canAcceptOptional = Boolean.parseBoolean(nameObject.get("canAcceptOptional").toString());
                        XPackInstaller.selected_url = nameObject.get("url").toString();
                        XPackInstaller.javaVersion = Integer.parseInt(nameObject.get("version").toString());
                        XPackInstaller.profile = nameObject.get("profile").toString();
                        if(!XPackInstaller.canAcceptOptional){
                            checkOptifine.setEnabled(false);
                            checkOptifine.setSelected(false);
                            zainstalujMoCreaturesCheckBox.setEnabled(false);
                            zainstalujMoCreaturesCheckBox.setSelected(false);
                            optionalText.setEnabled(false);
                        } else {
                            checkOptifine.setEnabled(true);
                            zainstalujMoCreaturesCheckBox.setEnabled(true);
                            optionalText.setEnabled(true);
                        }
                        break;
                    }
                    i++;
                }
                int javaStatus;
                if(XPackInstaller.javaVersion == 8){
                    javaStatus = java8Update;
                } else {
                    javaStatus = java7Update;
                }
                if (javaStatus == 0){
                    javaversion.setText("TAK");
                    javaversion.setForeground(Reference.COLOR_DARK_GREEN);
                } else if (javaStatus == 1) {
                    javaversion.setText("NIE");
                    javaversion.setForeground(Reference.COLOR_DARK_ORANGE);
                } else if (javaStatus == 2){
                    if(XPackInstaller.javaVersion == 8){
                        javaversion.setText("Nie posiadasz JRE8 w wersji 25 lub nowszej!");
                    } else {
                        javaversion.setText("Nie posiadasz najnowszej wersji JRE7!");
                    }
                    javaversion.setForeground(Color.RED);
                }
                if (osarch.getText().equals("TAK") && Ram.getText().equals("TAK") && javaarch.getText().equals("TAK") && (javaversion.getText().equals("TAK") || javaversion.getText().equals("NIE"))){
                    XPackInstaller.canGoForward = true;
                    button2.setText("Dalej");
                } else {
                    XPackInstaller.canGoForward = false;
                    button2.setText("Anuluj");
                }
            });
        } catch (FileNotFoundException | ParseException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Brak połączenia z Internetem!", "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        pobierzOryginalnyLauncherMCCheckBox.addActionListener(e -> {
            if(pobierzOryginalnyLauncherMCCheckBox.isSelected()){
                PathLauncherLabel.setEnabled(true);
                textField1.setEnabled(true);
                button3.setEnabled(true);
                XPackInstaller.installLauncher = true;
                labelLauncher.setEnabled(true);
                progressBar3.setEnabled(true);
            } else {
                PathLauncherLabel.setEnabled(false);
                textField1.setEnabled(false);
                button3.setEnabled(false);
                XPackInstaller.installLauncher = false;
                labelLauncher.setEnabled(false);
                progressBar3.setEnabled(false);
            }
        });
        button3.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser(System.getProperty("user.home"));
            chooser.setApproveButtonText("Wybierz");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogTitle("Wybierz ścieżkę");
            int returnValue = chooser.showOpenDialog(getContentPane());
            if(returnValue == JFileChooser.APPROVE_OPTION){
                try{
                    XPackInstaller.launcher_path = chooser.getSelectedFile().getCanonicalPath();
                } catch (IOException x){
                    x.printStackTrace();
                }
                textField1.setText(XPackInstaller.launcher_path);
            }
        });
        slider1.setMaximum(Utils.Utils.humanReadableRAM() - 2);
        slider1.addChangeListener(e -> XPackInstaller.allocatedRAM = slider1.getValue());
        button1.addActionListener(e -> {
            if(pobierzOryginalnyLauncherMCCheckBox.isSelected()){
                File launcher = new File(XPackInstaller.launcher_path + File.separator + "Minecraft.exe");
                if(textField1.getText().equals("")){
                    JOptionPane.showMessageDialog(panel, "Nie wybrałeś ścieżki instalacji Launcher'a!", "Błąd", JOptionPane.ERROR_MESSAGE);
                } else if(launcher.exists()){
                    JOptionPane.showMessageDialog(panel, "W podanym katalogu istanieje już plik o nazwie 'Minecraft.exe'!", "Bład", JOptionPane.ERROR_MESSAGE);
                } else {
                    cardLayout.next(panel);
                }
            } else {
                cardLayout.next(panel);
                if(osarch.getText().equals("NIE") && Ram.getText().equals("TAK") && javaarch.getText().equals("NIE") && (javaversion.getText().equals("TAK") || javaversion.getText().equals("NIE"))){
                    XPackInstaller.canGoForward = false;
                    button2.setText("Anuluj");
                    JOptionPane.showMessageDialog(gui, "Prosimy sprawdzi\u0107 czy na komputerze nie ma zainstalowanych dw\u00f3ch \u015brodowisk Java: w wersji 32-bitowej i 64-bitowej.\nJe\u015bli zainstalowane s\u0105 obie wersje prosimy o odinstalowanie wersji 32-bitowej. To rozwi\u0105\u017ce problem.", "B\u0142\u0105d konfiguracji Javy", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        if (Utils.Validators.systemArchitecture.check()){
            osarch.setText("TAK");
            osarch.setForeground(Reference.COLOR_DARK_GREEN);
        } else {
            osarch.setText("NIE");
            osarch.setForeground(Color.RED);
        }
        if (Utils.Validators.ramAmount.check()){
            Ram.setText("TAK");
            Ram.setForeground(Reference.COLOR_DARK_GREEN);
        } else {
            Ram.setText("NIE");
            Ram.setForeground(Color.RED);
        }
        if (Utils.Validators.javaArchitecture.check()){
            javaarch.setText("TAK");
            javaarch.setForeground(Reference.COLOR_DARK_GREEN);
        } else {
            javaarch.setText("NIE");
            javaarch.setForeground(Color.RED);
        }
        int javaStatus;

        if(XPackInstaller.javaVersion == 8){
            javaStatus = java8Update;
        } else {
            javaStatus = java7Update;
        }

        if (javaStatus == 0){
            javaversion.setText("TAK");
            javaversion.setForeground(Reference.COLOR_DARK_GREEN);
        } else if (javaStatus == 1) {
            javaversion.setText("NIE");
            javaversion.setForeground(Reference.COLOR_DARK_ORANGE);
        } else if (javaStatus == 2){
            javaversion.setText("Nie posiadasz najnowszej wersji JRE!");
            javaversion.setForeground(Color.RED);
        }
        if (osarch.getText().equals("TAK") && Ram.getText().equals("TAK") && javaarch.getText().equals("TAK") && (javaversion.getText().equals("TAK") || javaversion.getText().equals("NIE"))){
            XPackInstaller.canGoForward = true;
            button2.setText("Dalej");
        } else {
            XPackInstaller.canGoForward = false;
            button2.setText("Anuluj");
        }
        button2.addActionListener(e -> {
            if(XPackInstaller.canGoForward){
                cardLayout.next(panel);
            } else {
                System.exit(1);
            }
        });
        wsteczButton.addActionListener(e -> cardLayout.previous(panel));
        try {
            editorPane1.setPage("http://xpack.pl/licencja.html");
        } catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Brak połączenia z Internetem!", "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        licenseC.addActionListener(e -> {
            if (licenseC.isSelected()) {
                dalejButton.setEnabled(true);
            } else {
                dalejButton.setEnabled(false);
            }
        });
        try{
            File mainDir = new File(System.getenv("appdata") + File.separator + "XPackInstaller");
            if(mainDir.exists()){
                FileUtils.deleteDirectory(mainDir);
                if(!mainDir.mkdir()){
                    JOptionPane.showMessageDialog(gui, "Nie udało się utworzyć katalogu, prosimy spróbować ponownie!", "Błąd", JOptionPane.ERROR_MESSAGE );
                    System.out.println("Nie udało się utworzyć katalogu!");
                    System.exit(1);
                }
            } else {
                if(!mainDir.mkdir()){
                    JOptionPane.showMessageDialog(gui, "Nie udało się utworzyć katalogu, prosimy spróbować ponownie!", "Błąd", JOptionPane.ERROR_MESSAGE );
                    System.out.println("Nie udało się utworzyć katalogu!");
                    System.exit(1);
                }
            }
            File optionalDir = new File(mainDir.getAbsolutePath() + File.separator + "OptionalMods");
            if(!optionalDir.mkdir()){
                JOptionPane.showMessageDialog(gui, "Nie udało się utworzyć katalogu, prosimy spróbować ponownie!", "Błąd", JOptionPane.ERROR_MESSAGE );
                System.out.println("Nie udało się utworzyć katalogu!");
                System.exit(1);
            }
            dalejButton.addActionListener(e -> {
                cardLayout.next(panel);
                try {
                    progressBar1.setValue(0);
                    if(checkOptifine.isSelected() && zainstalujMoCreaturesCheckBox.isSelected()){
                        DownloadTask task2 = new DownloadTask(gui, "mod", Reference.downloadOptifine, optionalDir.getAbsolutePath());
                        task2.addPropertyChangeListener(evt -> {
                            if(evt.getPropertyName().equals("progress")){
                                labelmodpack.setText("Pobieranie Optifine HD w toku...");
                                if(task2.isDone()){task3();}
                                optifineProgress = (Integer) evt.getNewValue();
                                progressBar1.setValue(optifineProgress);
                            }
                        });
                        task2.execute();
                    } else if (checkOptifine.isSelected()) {
                        DownloadTask task2 = new DownloadTask(gui, "mod", Reference.downloadOptifine, optionalDir.getAbsolutePath());
                        task2.addPropertyChangeListener(evt -> {
                            if(evt.getPropertyName().equals("progress")){
                                labelmodpack.setText("Pobieranie Optifine HD w toku...");
                                if(task2.isDone()){task();}
                                optifineProgress = (Integer) evt.getNewValue();
                                progressBar1.setValue(optifineProgress);
                            }
                        });
                        task2.execute();
                    } else if (zainstalujMoCreaturesCheckBox.isSelected()){
                        task3();
                    } else {
                        task();
                    }
                } catch (Exception exx){
                    exx.printStackTrace();
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }

        final JScrollBar bar = scrollPane1.getVerticalScrollBar();
        bar.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int extent = bar.getModel().getExtent();
                int total = extent + bar.getValue();
                int max = bar.getMaximum();
                if(total==max){
                    licenseC.setEnabled(true);
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                int extent = bar.getModel().getExtent();
                int total = extent + bar.getValue();
                int max = bar.getMaximum();
                if(total==max){
                    licenseC.setEnabled(true);
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        bar.addMouseWheelListener(e -> {
            int extent = bar.getModel().getExtent();
            int total = extent + bar.getValue();
            int max = bar.getMaximum();
            if(total==max){
                licenseC.setEnabled(true);
            }
        });
        scrollPane1.setWheelScrollingEnabled(true);
        scrollPane1.addMouseWheelListener(e -> {
            int extent = bar.getModel().getExtent();
            int total = extent + bar.getValue();
            int max = bar.getMaximum();
            if(total==max){
                licenseC.setEnabled(true);
            }
        });
        panel.add(panel3);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel4);
        add(panel);
    }

    private void task3(){
        File mainDir = new File(System.getenv("appdata") + File.separator + "XPackInstaller");
        File optionalDir = new File(mainDir.getAbsolutePath() + File.separator + "OptionalMods");
        DownloadTask task3 = new DownloadTask(gui, "mod", Reference.downloadMoC, optionalDir.getAbsolutePath());
        task3.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals("progress")){
                if(task3.isDone()){task4();}
                labelmodpack.setText("Pobieranie Mo' Creatures w toku...");
                MocProgress = (Integer) evt.getNewValue();
                progressBar1.setValue(MocProgress);
            }
        });
        task3.execute();
    }
    private void task4(){
        File mainDir = new File(System.getenv("appdata") + File.separator + "XPackInstaller");
        File optionalDir = new File(mainDir.getAbsolutePath() + File.separator + "OptionalMods");
        DownloadTask task4 = new DownloadTask(gui, "mod", Reference.downloadCMS, optionalDir.getAbsolutePath());
        task4.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals("progress")){
                labelmodpack.setText("Pobieranie Custom Mob Spawner w toku...");
                if(task4.isDone()){task();}
                CmsProgress = (Integer) evt.getNewValue();
                progressBar1.setValue(CmsProgress);
            }
        });
        task4.execute();
    }
    private void task(){
        File mainDir = new File(System.getenv("appdata") + File.separator + "XPackInstaller");
        DownloadTask task = new DownloadTask(gui, "pack", XPackInstaller.selected_url, mainDir.getAbsolutePath());
        task.addPropertyChangeListener(evt -> {
            if(evt.getPropertyName().equals("progress")){
                labelmodpack.setText("Pobieranie paczki w toku...");
                modpackProgress = (Integer) evt.getNewValue();
                progressBar1.setValue(modpackProgress);
            }
        });
        task.execute();
    }
    void changeAfterModpack(){
        labelmodpack.setText("Pobieranie paczki zakończone!");
        labelInstall.setText("Instalowanie paczki w toku:");
        try {
            progressBar2.setValue(0);
            UnzipTask unzipTask = new UnzipTask(gui);
            unzipTask.addPropertyChangeListener(evt -> {
                if(evt.getPropertyName().equals("progress")){
                    int progress = (Integer) evt.getNewValue();
                    progressBar2.setValue(progress);
                }
            });
            unzipTask.execute();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    void changeAfterModpackInstall(){
        labelInstall.setText("Instalowanie paczki zakończone!");
        if(pobierzOryginalnyLauncherMCCheckBox.isSelected()){
            labelLauncher.setText("Instalowanie Launcher'a w toku: ");
            try {
                progressBar3.setValue(0);
                DownloadTask task = new DownloadTask(gui, "launcher", Reference.Launcher, XPackInstaller.launcher_path);
                task.addPropertyChangeListener(evt -> {
                    if(evt.getPropertyName().equals("progress")){
                        int progress = (Integer) evt.getNewValue();
                        progressBar3.setValue(progress);
                    }
                });
                task.execute();
            } catch (Exception e){
                e.printStackTrace();
            }

        } else {
            zamknijButton.addActionListener(e -> System.exit(0));
            JOptionPane.showMessageDialog(panel, "Instalacja zakończona pomyślnie!", "Instalacja", JOptionPane.INFORMATION_MESSAGE);
            zamknijButton.setEnabled(true);
        }
    }

    void changeAfterLauncher(){
        labelLauncher.setText("Instalowanie Launcher'a zakończone!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        zamknijButton.addActionListener(e -> System.exit(0));
        zamknijButton.setEnabled(true);
        JOptionPane.showMessageDialog(panel, "Instalacja zakończona pomyślnie!", "Instalacja", JOptionPane.INFORMATION_MESSAGE);
    }
}
