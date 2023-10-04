import java.util.Random;

public class Caesar {
    private final String fixedPwd = "f%5DT/6f2c8!&fdz-G?54gd7";

    public String encrypt(String plaintext, String masterHash){
        int plainLength = plaintext.length();
        int masterLength = masterHash.length();
        int fixedLength = this.fixedPwd.length();
        StringBuilder encryptedPwd = new StringBuilder();
        char ASCIIstart = '!';

        for(int i = 0; i < plainLength; i++){
            int seed = (int)masterHash.charAt(i%masterLength) * (int)this.fixedPwd.charAt((i+1)%fixedLength);
            Random random = new Random();
            random.setSeed(seed);
            int fixedIndex = random.nextInt(fixedLength-1);

            int encryptedIndex = ((int)plaintext.charAt(i) - (int)ASCIIstart + (int)this.fixedPwd.charAt(fixedIndex) - (int)ASCIIstart) % 94;
            encryptedPwd.append((char)((int)ASCIIstart + encryptedIndex));
        }

        return encryptedPwd.toString();
    }

    public String decrypt(String encText, String masterHash){
        int encLength = encText.length();
        int masterLength = masterHash.length();
        int fixedLength = this.fixedPwd.length();
        StringBuilder decryptedPwd = new StringBuilder();
        char ASCIIstart = '!';

        for(int i = 0; i < encLength; i++){
            int seed = (int)masterHash.charAt(i%masterLength) * (int)this.fixedPwd.charAt((i+1)%fixedLength);
            Random random = new Random();
            random.setSeed(seed);
            int fixedIndex = random.nextInt(fixedLength-1);

            int decryptedIndex = (94 + ((int)encText.charAt(i) - (int)ASCIIstart) - ((int)this.fixedPwd.charAt(fixedIndex) - (int)ASCIIstart)) % 94;
            decryptedPwd.append((char)((int)ASCIIstart + decryptedIndex));

        }
        return decryptedPwd.toString();
    }
}
