//  Ceasar;

public class Main {
    public static void main(String[] args) {
        // System.out.println("Hello world!");

        Caesar c = new Caesar();
        GeneratePWD gPWD = new GeneratePWD();
        String pwd = gPWD.generatePwd();
        System.out.println(pwd);

        String encrypted = c.encrypt(pwd, "supersecret_password");
        System.out.println(encrypted);

        String decrypted = c.decrypt(encrypted, "supersecret_password");
        System.out.println(decrypted);
    }

}