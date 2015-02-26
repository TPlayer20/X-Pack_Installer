import Utils.Reference;
import jdk.nashorn.api.scripting.URLReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
    private CardLayout cardLayout;
    private int licenseC_count = 0;
    private Display gui = this;

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
            for (Object anArray : array) {
                comboBox1.addItem(new JSONObject((JSONObject) anArray).get("name"));
            }
            comboBox1.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int i = 0;
                    while(true){
                        JSONObject nameObject = (JSONObject) array.get(i);
                        String name = nameObject.get("name").toString();
                        if(name==comboBox1.getSelectedItem()){
                            XPackInstaller.selected_url = nameObject.get("url").toString();
                            XPackInstaller.javaVersion = Integer.parseInt(nameObject.get("version").toString());
                            XPackInstaller.profile = nameObject.get("profile").toString();
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
                }
            });
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Brak połączenia z Internetem!", "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } catch (ParseException e){
            e.printStackTrace();
        }

        pobierzOryginalnyLauncherMCCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        slider1.setMaximum(Utils.Utils.humanReadableRAM() - 2);
        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                XPackInstaller.allocatedRAM = slider1.getValue();
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            javaversion.setText("Nie posiadasz najnowszej wersji JRE7!");
            javaversion.setForeground(Color.RED);
        }
        if (osarch.getText().equals("TAK") && Ram.getText().equals("TAK") && javaarch.getText().equals("TAK") && (javaversion.getText().equals("TAK") || javaversion.getText().equals("NIE"))){
            XPackInstaller.canGoForward = true;
            button2.setText("Dalej");
        } else {
            XPackInstaller.canGoForward = false;
            button2.setText("Anuluj");
        }
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(XPackInstaller.canGoForward){
                    cardLayout.next(panel);
                } else {
                    System.exit(1);
                }
            }
        });
        wsteczButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.previous(panel);
            }
        });

        try {
            editorPane1.setPage("http://xpack.pl/licencja.html");
        } catch (IOException e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(panel, "Brak połączenia z Internetem!", "Błąd", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        licenseC.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (licenseC.isSelected()) {
                    dalejButton.setEnabled(true);
                } else {
                    dalejButton.setEnabled(false);
                }
            }
        });
        dalejButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.next(panel);
                try {
                    progressBar1.setValue(0);
                    DownloadTask task = new DownloadTask(gui, false, XPackInstaller.selected_url, System.getenv("appdata"));
                    task.addPropertyChangeListener(new PropertyChangeListener() {
                        @Override
                        public void propertyChange(PropertyChangeEvent evt) {
                            if(evt.getPropertyName().equals("progress")){
                                int progress = (Integer) evt.getNewValue();
                                progressBar1.setValue(progress);
                            }
                        }
                    });
                    task.execute();
                } catch (Exception exx){
                    exx.printStackTrace();
                }
            }
        });
        final JScrollBar bar = scrollPane1.getVerticalScrollBar();
        bar.addAdjustmentListener(new AdjustmentListener() {
            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                int extent = bar.getModel().getExtent();
                int total = extent + bar.getValue();
                int max = bar.getMaximum();
                if(total==max){
                    licenseC_count++;
                    if (licenseC_count == 4){
                        licenseC.setEnabled(true);
                    }
                }
            }
        });

        panel.add(panel3);
        panel.add(panel1);
        panel.add(panel2);
        panel.add(panel4);
        add(panel);
    }

    public void changeAfterModpack(){
        labelmodpack.setText("Pobieranie paczki zakończone!");
        labelInstall.setText("Instalowanie paczki w toku:");
        try {
            progressBar2.setValue(0);
            UnzipTask unzipTask = new UnzipTask(gui);
            unzipTask.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent evt) {
                    if(evt.getPropertyName().equals("progress")){
                        int progress = (Integer) evt.getNewValue();
                        progressBar2.setValue(progress);
                    }
                }
            });
            unzipTask.execute();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void changeAfterModpackInstall(){
        labelInstall.setText("Instalowanie paczki zakończone!");
        if(pobierzOryginalnyLauncherMCCheckBox.isSelected()){
            labelLauncher.setText("Instalowanie Launcher'a w toku: ");
            try {
                progressBar3.setValue(0);
                DownloadTask task = new DownloadTask(gui, true, Reference.Launcher, XPackInstaller.launcher_path);
                task.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        if(evt.getPropertyName().equals("progress")){
                            int progress = (Integer) evt.getNewValue();
                            progressBar3.setValue(progress);
                        }
                    }
                });
                task.execute();
            } catch (Exception e){
                e.printStackTrace();
            }

        } else {
            zamknijButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            JOptionPane.showMessageDialog(panel, "Instalacja zakończona pomyślnie!", "Instalacja", JOptionPane.INFORMATION_MESSAGE);
            zamknijButton.setEnabled(true);
        }
    }

    public void changeAfterLauncher(){
        labelLauncher.setText("Instalowanie Launcher'a zakończone!");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        zamknijButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        zamknijButton.setEnabled(true);
    }
}
