//import org.sqlite.JDBC;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.reflect.Array;
import java.awt.Color;



public class PasswordManager implements ActionListener{
    Caesar c = new Caesar();
    GeneratePWD gPWD = new GeneratePWD();
    private String masterHash = "";
    private String databasePath = "";
    int columns = 4;
    int rows = 0;
    private String[][] DBcontent = new String[rows][columns];
    JFrame mainFrame;
    JMenuBar menubar;
    JMenu menu;
    JMenuItem m1, m2, m3, m4;
    JButton buttonAddCredentials, buttonGeneratePWD, buttonShowPassword, buttonHidePassword;
    JTextField textUsername, textPWD, textDesc;
    JPanel panelInputs, panelDB;
    JLabel labelUsername, labelPWD, labelDesc;
    GridBagConstraints gbc;
    JFileChooser choseFile;
    JPasswordField passwordField;
    JTable tableDB;
    // JScrollPane tableScroll;
    DefaultTableModel tableModel;

    public PasswordManager(){
        gbc = new GridBagConstraints();

        mainFrame = new JFrame("Password manager");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.getContentPane().setBackground(Color.BLUE);

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
        buttonAddCredentials.addActionListener(this);
        gbc.gridx = 3;
        gbc.gridy = 1;
        panelInputs.add(buttonAddCredentials, gbc);

        buttonGeneratePWD = new JButton("Auto Generate Password");
        buttonGeneratePWD.addActionListener(this);
        gbc.gridx = 4;
        gbc.gridy = 1;
        panelInputs.add(buttonGeneratePWD, gbc);

        panelDB = new JPanel();
        panelDB.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tableModel = new DefaultTableModel();
        tableDB = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Username");
        tableModel.addColumn("Description");
        tableModel.addColumn("Password");
        panelDB.add(new JScrollPane(tableDB));

        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.gridy = 4;
        panelInputs.add(panelDB, gbc);

        buttonShowPassword = new JButton("Display Password");
        buttonShowPassword.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        panelInputs.add(buttonShowPassword, gbc);

        buttonHidePassword = new JButton("Hide Password");
        buttonHidePassword.addActionListener(this);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.gridy = 5;
        panelInputs.add(buttonHidePassword, gbc);

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

        if(e.getSource() == m3) {
            try {
                this.changeMasterPWD();
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource() == m4)
            this.emptyDB();

        if(e.getSource() == buttonGeneratePWD)
            this.textPWD.setText(generatePassword());

        if(e.getSource() == buttonAddCredentials)
            this.addCredentials();

        if(e.getSource() == buttonShowPassword)
            this.displayPWD();

        if(e.getSource() == buttonHidePassword)
            this.hidePWD();
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

    private void SQLexecution(String statement, String[] values, boolean read){
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.databasePath)) {
            PreparedStatement pstmt = null;
            if (read){
                pstmt = conn.prepareStatement("SELECT COUNT(*) FROM Passwords");
                ResultSet rs = pstmt.executeQuery();
                rs.next();
                this.rows = rs.getInt(1);
                this.DBcontent = new String[this.rows][this.columns];
                if (rows > 0){
                    pstmt = conn.prepareStatement(statement);
                    rs = pstmt.executeQuery();
                    int outerIndex = 0;
                    while (rs.next()){
                        this.DBcontent[outerIndex][0] = String.valueOf(rs.getInt("ID"));
                        this.DBcontent[outerIndex][1] = rs.getString("Username");
                        this.DBcontent[outerIndex][2] = rs.getString("Description");
                        outerIndex++;
                    }
                }
            }

            else{
                pstmt = conn.prepareStatement(statement);
                if (values.length != 0){
                    for (int i = 0; i < values.length; i++)
                        pstmt.setString(i+1, Array.get(values, i).toString());
                }
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    private boolean checkSelectedDB(){
        if (this.databasePath.isEmpty()){
            JOptionPane.showMessageDialog(mainFrame, "You have to choose a database first");
            return false;
        }
        return true;
    }

    private void displayDBtable(boolean update){
        this.tableModel.getDataVector().removeAllElements();
        if(!update) {
            String[] values = {};
            this.SQLexecution("SELECT id, username, description FROM Passwords", values, true);
        }
        for (String[] strings : this.DBcontent)
            this.tableModel.addRow(new Object[]{strings[0], strings[1], strings[2], strings[3]});

    }

//---------------------------------------------actionPerformed functions-----------------------------------------//

    private void changeMasterPWD() throws NoSuchAlgorithmException {
        if (!this.checkSelectedDB())
            return;

        String oldMasterPWD = this.askForMasterPWD("Enter the current master password");
        if(oldMasterPWD.isEmpty()){
            JOptionPane.showMessageDialog(mainFrame, "You must enter the current master password");
            return;
        }
        if (!this.hashPWD(oldMasterPWD).equals(this.masterHash)) {
            JOptionPane.showMessageDialog(mainFrame, "Wrong master password");
            return;
        }
        String newMasterPWD = this.askForMasterPWD("Enter a new master password");
        if(newMasterPWD.isEmpty()){
            JOptionPane.showMessageDialog(mainFrame, "You must enter a new master password");
            return;
        }
        String newMasterHash = hashPWD(newMasterPWD);

        String[] encryptedPWD = new String[this.tableDB.getRowCount()];
        String[] decryptedPWD = new String[this.tableDB.getRowCount()];
        int index = 0;

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.databasePath)) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("SELECT password FROM passwords");
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()){
                encryptedPWD[index] = rs.getString("password");
                index++;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < this.tableDB.getRowCount(); i++)
            decryptedPWD[i] = c.decrypt(encryptedPWD[i], this.masterHash);

        String[] values = new String[2];
        String statement = "UPDATE passwords SET password = ? WHERE id = ?";
        for(int i = 0; i < this.tableDB.getRowCount(); i++){
            values[0] = c.encrypt(decryptedPWD[i], newMasterHash);
            values[1] = (String) this.tableDB.getValueAt(i, 0);
            this.SQLexecution(statement, values, false);
        }

        this.masterHash = newMasterHash;
        try {
            PrintWriter writer = new PrintWriter(this.databasePath.substring(0, this.databasePath.length()-2) + "txt");
            writer.println(this.masterHash);
            writer.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void hidePWD(){
        if (!this.checkSelectedDB())
            return;

        int selectedRow = this.tableDB.getSelectedRow();
        if(selectedRow < 0 || selectedRow > this.tableDB.getRowCount()){
            JOptionPane.showMessageDialog(mainFrame, "You have to select a row");
            return;
        }

        this.DBcontent[selectedRow][this.columns-1] = "";
        this.displayDBtable(true);
    }

    private void displayPWD() {
        if (!this.checkSelectedDB())
            return;

        int selectedRow = this.tableDB.getSelectedRow();
        if(selectedRow == -1){
            JOptionPane.showMessageDialog(mainFrame, "You have to select a row");
            return;
        }

        int id = Integer.parseInt(this.DBcontent[selectedRow][0]);
        String selectedPWD = "";

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.databasePath)) {
            PreparedStatement pstmt = null;
            pstmt = conn.prepareStatement("SELECT password FROM passwords WHERE id = ?");
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            selectedPWD = rs.getString(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String decryptedPWD = c.decrypt(selectedPWD, this.masterHash);
        this.DBcontent[selectedRow][this.columns-1] = decryptedPWD;
        this.displayDBtable(true);
    }

    private String generatePassword(){
        return gPWD.generatePwd();
    }

    private void emptyDB(){
        if (!this.checkSelectedDB())
            return;

        String[] values = {};
        this.SQLexecution("DELETE FROM Passwords", values, false);
        this.displayDBtable(false);
    }

    private void addCredentials(){
        if (!this.checkSelectedDB())
            return;

        if (this.textUsername.getText().isEmpty() || this.textPWD.getText().isEmpty()){
            JOptionPane.showMessageDialog(mainFrame, "You have to provide a username and a password");
            return;
        }

        String[] values = {this.textUsername.getText(), c.encrypt(this.textPWD.getText(), this.masterHash), this.textDesc.getText()};
        String statement = "INSERT INTO Passwords(username, password, description) VALUES(?,?,?)";
        this.SQLexecution(statement, values, false);

        this.textUsername.setText("");
        this.textPWD.setText("");
        this.textDesc.setText("");

        this.displayDBtable(false);
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
        this.displayDBtable(false);
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
            String[] values = {};
            this.SQLexecution(sqlStatement, values, false);

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

        this.displayDBtable(false);
    }

//---------------------------------------------Main function-----------------------------------------//

    public static void main(String[] args) {
        new PasswordManager();
    }

}