import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Arrays;


class Page_Home extends JFrame {
    private final CloudInterface	o;
    private final String          account;
    //private JLabel labelURL = new JLabel("Upload URL: ");
    //private JTextField fieldURL = new JTextField(30);

    private JFilePicker filePicker = new JFilePicker("Choose a file: ",
            "Browse");

    private JButton buttonUpload = new JButton("Upload");
    private JButton buttonShowFile = new JButton("Download");

    //private JLabel labelProgress = new JLabel("Progress:");
    //private JProgressBar progressBar = new JProgressBar(0, 100);

    public Page_Home(CloudInterface	o,String account) {
        super("Home");
        this.o = o;
        this.account = account;
        // set up layout
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);

        // set up components
        filePicker.setMode(JFilePicker.MODE_OPEN);

        buttonUpload.addActionListener(event -> buttonUploadActionPerformed());
        buttonShowFile.addActionListener(event -> buttonShowFileAction());
        //progressBar.setPreferredSize(new Dimension(200, 30));
        //progressBar.setStringPainted(true);

        // add components to the frame
//        constraints.gridx = 0;
//        constraints.gridy = 0;
//        add(labelURL, constraints);

//        constraints.gridx = 1;
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        constraints.weightx = 1.0;
//        add(fieldURL, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        add(filePicker, constraints);

        constraints.gridy = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonUpload, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        add(buttonShowFile, constraints);

//        constraints.gridx = 0;
//        constraints.gridy = 3;
//        constraints.gridwidth = 1;
//        constraints.anchor = GridBagConstraints.WEST;
//        add(labelProgress, constraints);
//
//        constraints.gridx = 1;
//        constraints.weightx = 1.0;
//        constraints.fill = GridBagConstraints.HORIZONTAL;
//        add(progressBar, constraints);

        pack();
        setLocationRelativeTo(null);    // center on screen
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void buttonShowFileAction(){
        try {
            Object[][] myFiles = o.showMyFile(account);
            Object[] cols = {
                    "ID","FileName","Creater","Date"
            };
            JTable table = new JTable(myFiles, cols);
            Object[] values = new Object[myFiles.length];
            for(int i=0;i<myFiles.length;i++){
                values[i] = i;
            }

            Object selected = JOptionPane.showInputDialog(null, new JScrollPane(table), "Selection", JOptionPane.PLAIN_MESSAGE, null, values, "0");


            //Object selected = JOptionPane.showInputDialog(null, "What is the target Nicotine level?", "Selection", JOptionPane.DEFAULT_OPTION, null, myFiles, "0");
            if ( selected != null ){//null if the user cancels.
                String selectedString = selected.toString();
                //do something
            }else{
                System.out.println("User cancelled");
            }

            //JOptionPane.showMessageDialog(null,"File has been uploaded successfully!", "Message",JOptionPane.QUESTION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error executing upload task: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    /**
     * handle click event of the Upload button
     */
    private void buttonUploadActionPerformed() {
        //String uploadURL = fieldURL.getText();
        String filePath = filePicker.getSelectedFilePath();
        String[] splitPath = filePath.split("/");
        String fileName = splitPath[splitPath.length-1];
        System.out.println(filePath);
        // validate input first
//        if (uploadURL.equals("")) {
//            JOptionPane.showMessageDialog(this, "Please enter upload URL!",
//                    "Error", JOptionPane.ERROR_MESSAGE);
//            fieldURL.requestFocus();
//            return;
//        }

        if (filePath.equals("")) {
            JOptionPane.showMessageDialog(this,
                    "Please choose a file to upload!", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            File file = new File(filePath);
            //progressBar.setValue(0);

            //File file = new File(path);
            int size = (int) file.length();
            byte[] bytes = new byte[size];
            try {
                BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));

                //addPropertyChangeListener(this);

                buf.read(bytes, 0, bytes.length);
                buf.close();
                if(o.writeDataToServer(fileName,bytes,bytes.length,account)){
                    //setProgress(percentCompleted);
                    //UIManager.put("ProgressBar.foreground", Color.BLUE);
                    //progressBar.setValue(100);
                    JOptionPane.showMessageDialog(null,
                            "File has been uploaded successfully!", "Message",
                            JOptionPane.INFORMATION_MESSAGE);
                }else{
                    //progressBar.setValue(0);
                    JOptionPane.showMessageDialog(null, "Error uploading file",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // TODO
            //uploadFile.createNewFile();
//            FileOutputStream out=new FileOutputStream(f,true);
//            out.write(data,0,len);
//            out.flush();
//            out.close();
//            System.out.println("Done writing data...");
            // TODO
//            UploadTask task = new UploadTask(uploadURL, uploadFile);

//            task.execute();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error executing upload task: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Update the progress bar's state whenever the progress of upload changes.
     */
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if ("progress".equals(evt.getPropertyName())) {
//            int progress = (Integer) evt.getNewValue();
//            //progressBar.setValue(progress);
//        }
//    }

    /**
     * Launch the application
     */
    public static void main(String[] args,CloudInterface o,String account) {
        try {
            // set look and feel to system dependent
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new Page_Home(o,account).setVisible(true));
    }
}