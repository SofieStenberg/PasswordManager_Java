//  Ceasar;

public class Main {
    public static void main(String[] args) {
        // System.out.println("Hello world!");

        Caesar c = new Caesar();
        String encrypted = c.encrypt("TestingTesting", "supersecret_password");
        System.out.println(encrypted);

        String decrypted = c.decrypt(encrypted, "supersecret_password");
        System.out.println(decrypted);
    }

}