//import org.sqlite.JDBC;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;


public class PasswordManager implements ActionListener{
    private String masterHash = "";
    private String databasePath = "";
    JFrame mainFrame;
    JMenuBar menubar;
    JMenu menu;
    JMenuItem m1, m2, m3, m4;
    JButton buttonAddCredentials, buttonGeneratePWD;
    JTextField textUsername, textPWD, textDesc;
    JPanel panelInputs;
    JLabel labelUsername, labelPWD, labelDesc;
    GridBagConstraints gbc;
    JFileChooser choseFile;
    JPasswordField passwordField;

    public PasswordManager(){
        gbc = new GridBagConstraints();

        mainFrame = new JFrame("Password manager");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panelInputs = new JPanel(new GridBagLayout());
        panelInputs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //------------------------------------MENU-BAR------------------------------------------------//
        menubar = new JMenuBar();
        menu = new JMenu("Menu");

        // Creates the menu-items
        m1 = new JMenuItem("New Database");
        m2 = new JMenuItem("Open...");
        m3 = new JMenuItem("Change Master Password");
        m4 = new JMenuItem("Empty Database");

        m1.addActionListener(this);
        m2.addActionListener(this);
        m3.addActionListener(this);
        m4.addActionListener(this);

        // Adds the items to the menu
        menu.add(m1);
        menu.add(m2);
        menu.add(m3);
        menu.add(m4);

        // Adds the menu to the menubar and the menubar to the frame
        menubar.add(menu);
        mainFrame.setJMenuBar(menubar);

        //--------------------------------------INSERT PANEL-----------------------------------------------------//

        labelUsername = new JLabel("Username");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelInputs.add(labelUsername, gbc);

        labelPWD = new JLabel("Password");
        gbc.gridx = 1;
        gbc.gridy = 0;
        panelInputs.add(labelPWD, gbc);

        labelDesc = new JLabel("Description");
        gbc.gridx = 2;
        gbc.gridy = 0;
        panelInputs.add(labelDesc, gbc);

        textUsername = new JTextField(20);
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelInputs.add(textUsername, gbc);

        textPWD = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        panelInputs.add(textPWD, gbc);

        textDesc = new JTextField(20);
        gbc.gridx = 2;
        gbc.gridy = 1;
        panelInputs.add(textDesc, gbc);

        buttonAddCredentials = new JButton("Add Credentials");
        gbc.gridx = 3;
        gbc.gridy = 1;
        panelInputs.add(buttonAddCredentials, gbc);

        buttonGeneratePWD = new JButton("Auto Generate Password");
        buttonGeneratePWD.addActionListener(this);
        gbc.gridx = 4;
        gbc.gridy = 1;
        panelInputs.add(buttonGeneratePWD, gbc);





        //--------------------------------------Makes everything visible-----------------------------------------------------//
        mainFrame.add(panelInputs);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);

    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == m1)
            this.createNewDatabase();

        if(e.getSource() == m2) {
            try {
                this.openDB();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource() == m3)
            System.out.println("Change Master Password");

        if(e.getSource() == m4)
            System.out.println("Empty Database");

        if(e.getSource() == buttonGeneratePWD){
            String pwd = generatePassword();
            textPWD.setText(pwd);
        }

        if(e.getSource() == buttonAddCredentials){
            System.out.println("Add credentials");
        }


    }

//---------------------------------------------Help functions-----------------------------------------//

    private String askForMasterPWD(String message){
        passwordField = new JPasswordField();
        passwordField.setEchoChar('*');
        passwordField.setColumns(20);
        String enterdPWD = "";
        int status = JOptionPane.showConfirmDialog(mainFrame, passwordField, message, JOptionPane.OK_CANCEL_OPTION);
        if(status == JOptionPane.OK_OPTION){
            enterdPWD = Arrays.toString(passwordField.getPassword());
        }

        return enterdPWD;
    }

    private String readMasterFile(String path){
        String content = "";
        File masterfile = new File(path);
        try {
            Scanner s = new Scanner(masterfile);
            while(s.hasNextLine()){
                content = s.nextLine();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return content;
    }

    private void SQLexecution(String sqlStatement){
        try(Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.databasePath); Statement stmt = conn.createStatement()){
            stmt.execute(sqlStatement);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private String hashPWD(String pwd) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA3-256");
        md.update(pwd.getBytes());

        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest){
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }

//---------------------------------------------actionPerformed functions-----------------------------------------//

    private String generatePassword(){
        GeneratePWD gPWD = new GeneratePWD();
        return gPWD.generatePwd();
    }

    private void openDB() throws NoSuchAlgorithmException {
        choseFile = new JFileChooser();
        choseFile.setDialogTitle("Select a database file");
        choseFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
        choseFile.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .db files", "db");
        choseFile.addChoosableFileFilter(restrict);
        int status = choseFile.showOpenDialog(null);

        if(status == JFileChooser.APPROVE_OPTION){
            this.databasePath = choseFile.getSelectedFile().getAbsolutePath();
            String userPWD = this.askForMasterPWD("Enter the master password");
            if(userPWD.isEmpty()){
                JOptionPane.showMessageDialog(mainFrame, "You must provide the master password to open the database");
                return;
            }

            this.masterHash = this.readMasterFile(this.databasePath.substring(0, this.databasePath.length()-2) + "txt");
            String userHash = this.hashPWD(userPWD);
            if(!userHash.equals(this.masterHash)){
                JOptionPane.showMessageDialog(mainFrame, "You entered the wrong master password");
                this.masterHash = "";
                this.databasePath = "";
            }
        }
    }

    private void createNewDatabase() {
        choseFile = new JFileChooser();
        choseFile.setDialogTitle("Select folder");
        choseFile.setCurrentDirectory(new File(System.getProperty("user.dir")));
        choseFile.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        choseFile.setAcceptAllFileFilterUsed(false);
        File selectedFolder = null;
        int status = choseFile.showOpenDialog(null);
        if(status == JFileChooser.APPROVE_OPTION) {
            selectedFolder = choseFile.getSelectedFile();
        }
        else if(status == JFileChooser.CANCEL_OPTION){
            return;
        }
        String dbName = (String)JOptionPane.showInputDialog(mainFrame, "Enter name of the new database");
        if (dbName.isEmpty()){
            JOptionPane.showMessageDialog(mainFrame, "You have not chosen a dbfile name.");
            return;
        }
        String path = selectedFolder.getParent() + "\\" + selectedFolder.getName() + "\\" + dbName + ".db";
        File dbFile = new File(path);
        if(dbFile.exists()){
            JOptionPane.showMessageDialog(mainFrame, "The file already exists.");
            return;
        }

        String userPWD = this.askForMasterPWD("Enter a master password");
        if(!userPWD.isEmpty()){
            try {
                this.masterHash = this.hashPWD(Arrays.toString(passwordField.getPassword()));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            this.databasePath = path;

            String sqlStatement = """
                        CREATE TABLE IF NOT EXISTS Passwords(
                        id          INTEGER     PRIMARY KEY     AUTOINCREMENT,
                        username    TEXT    NOT NULL,
                        password    TEXT    NOT NULL,
                        description TEXT);
                       """;
            this.SQLexecution(sqlStatement);

            String masterFileName = selectedFolder.getParent() + "\\" + selectedFolder.getName() + "\\" + dbName + ".txt";
            File masterFile = new File(masterFileName);
            try {
                if(!masterFile.createNewFile()){
                    System.out.println("Could not create master file");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            FileWriter fw = null;
            try {
                fw = new FileWriter(masterFileName);
                fw.write(this.masterHash);
                fw.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        else{
            JOptionPane.showMessageDialog(mainFrame, "You must enter a password");
             dbFile.delete();
        }
    }

//---------------------------------------------Main function-----------------------------------------//

    public static void main(String[] args) {
        new PasswordManager();





//        Caesar c = new Caesar();
//        GeneratePWD gPWD = new GeneratePWD();
//
//        String pwd = gPWD.generatePwd();
//        System.out.println(pwd);
//
//        String encrypted = c.encrypt(pwd, "supersecret_password");
//        System.out.println(encrypted);
//
//        String decrypted = c.decrypt(encrypted, "supersecret_password");
//        System.out.println(decrypted);
    }

}