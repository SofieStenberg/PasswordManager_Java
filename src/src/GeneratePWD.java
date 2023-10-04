import  java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class GeneratePWD {


    private Integer replaceIndex(String generatedPwd, Character biggestChar){
        return 0;
    }

    public String generatePwd(){
        int pwdLength = 16;
        StringBuilder generatedPwd = new StringBuilder();
        char ASCIIstart = '!';
        int randomNr = 0;
        //boolean goOn = true;
        Hashtable<Character, Integer> container = new Hashtable<>();

        container.put('A', 0);
        container.put('a', 0);
        container.put('1', 0);
        container.put('!', 0);

        for (int i = 0; i < pwdLength; i++){
            Random random = new Random();
            randomNr = random.nextInt(94);
            int ASCIIchar = (int)ASCIIstart+randomNr;

            // Don't want the following chars; ', <, >, `
            while(ASCIIchar == 39 || ASCIIchar == 60 || ASCIIchar == 62 || ASCIIchar == 96) {
                randomNr = random.nextInt(94);
                ASCIIchar = (int)ASCIIstart+randomNr;
            }

            if(ASCIIchar <= 90 && ASCIIchar >= 65){
                container.put('A', container.get('A')+1);
                generatedPwd.append((char)ASCIIchar);
            }

            else if(ASCIIchar <= 122 && ASCIIchar >= 97){
                container.put('a', container.get('a')+1);
                generatedPwd.append((char)ASCIIchar);
            }

            else if(ASCIIchar <= 57 && ASCIIchar >= 48){
                container.put('1', container.get('1')+1);
                generatedPwd.append((char)ASCIIchar);
            }

            else {
                container.put('!', container.get('!')+1);
                generatedPwd.append((char)ASCIIchar);
            }

        }

        if(container.get('A') > 0 && container.get('a') > 0 && container.get('1') > 0 && container.get('!') > 0) {
            return generatedPwd.toString();
        }

        int biggestInt = 0;
        char biggestChar = 0;
        int index = 0;
        boolean goOn = true;

        while(goOn){
            if(container.get('A') > 0 && container.get('a') > 0 && container.get('1') > 0 && container.get('!') > 0){
                goOn = false;
                break;
            }

            Set<Character> keys = container.keySet();
            for(Character key : keys ){
                if(container.get(key) > biggestInt){
                    biggestInt = container.get(key);
                    biggestChar = key;
                }
            }

            index = this.replaceIndex(generatedPwd.toString(), biggestChar);
            container.put(biggestChar, container.get(biggestChar)-1);

            if(container.get('A') == 0){
                Random random = new Random();
                randomNr = random.nextInt(26);
            }
        }

        return generatedPwd.toString();
    }
}
