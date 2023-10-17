import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import javax.swing.JTextField;
import java.util.Objects;


public class PasswordManager implements ActionListener{
    JFrame mainFrame;
    JMenuBar menubar;
    JMenu menu;
    JMenuItem m1, m2, m3, m4;
    JButton buttonAddCredentials, buttonGeneratePWD;
    JTextField textUsername, textPWD, textDesc;
    JPanel panelInputs;
    JLabel labelUsername, labelPWD, labelDesc;
    GridBagConstraints gbc;

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

    private String generatePassword(){
        GeneratePWD gPWD = new GeneratePWD();
        return gPWD.generatePwd();
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == m1)
            System.out.println("New Database");
        if(e.getSource() == m2)
            System.out.println("Open...");
        if(e.getSource() == m3)
            System.out.println("Change Master Password");
        if(e.getSource() == m4)
            System.out.println("Empty Database");
        if(e.getSource() == buttonGeneratePWD){
            String pwd = generatePassword();
            textPWD.setText(pwd);
        }


    }

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