import  java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class GeneratePWD {


    private Integer replaceIndex(String generatedPwd, Character biggestChar){
        int pwdLength = generatedPwd.length();

        if(biggestChar == 'A' || biggestChar == 'a'){
            for(int i = 0; i < pwdLength; i++){
                if(((int)generatedPwd.charAt(i) > (int)biggestChar+0) && ((int)generatedPwd.charAt(i) < (int)biggestChar+26))
                    return i;
            }
        }
        else if(biggestChar == '1'){
            for(int i = 0; i < pwdLength; i++){
                if(((int)generatedPwd.charAt(i) > (int)biggestChar+0) && ((int)generatedPwd.charAt(i) < (int)biggestChar+10))
                    return i;
            }
        }
        else{
            for(int i = 0; i < pwdLength; i++){
                if((((int)generatedPwd.charAt(i) > (int)biggestChar+0) && ((int)generatedPwd.charAt(i) < (int)biggestChar+6)) ||
                        (((int)generatedPwd.charAt(i) > (int)biggestChar+7) && ((int)generatedPwd.charAt(i) < (int)biggestChar+14)) ||
                        (((int)generatedPwd.charAt(i) > (int)biggestChar+24) && ((int)generatedPwd.charAt(i) < (int)biggestChar+27)) ||
                        ((int)generatedPwd.charAt(i) == (int)biggestChar+29) ||
                        (((int)generatedPwd.charAt(i) > (int)biggestChar+31) && ((int)generatedPwd.charAt(i) < (int)biggestChar+33)) ||
                        (((int)generatedPwd.charAt(i) > (int)biggestChar+59) && ((int)generatedPwd.charAt(i) < (int)biggestChar+64)) ||
                        (((int)generatedPwd.charAt(i) > (int)biggestChar+91) && ((int)generatedPwd.charAt(i) < (int)biggestChar+95)))
                            return i;
            }
        }
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
            char newChar;

            if(container.get('A') == 0){
                Random random = new Random();
                randomNr = random.nextInt(26);
                newChar = (char)((int)'A' + randomNr);
                generatedPwd.setCharAt(index, newChar);
                container.put('A', container.get('A')+1);
            }
            else if(container.get('a') == 0){
                Random random = new Random();
                randomNr = random.nextInt(26);
                newChar = (char)((int)'a' + randomNr);
                generatedPwd.setCharAt(index, newChar);
                container.put('a', container.get('a')+1);
            }
            else if(container.get('1') == 0){
                Random random = new Random();
                randomNr = random.nextInt(10);
                newChar = (char)((int)'1' + randomNr);
                generatedPwd.setCharAt(index, newChar);
                container.put('1', container.get('1')+1);
            }
            else{
                boolean innerGoOn = true;
                while(innerGoOn){
                    Random random = new Random();
                    randomNr = random.nextInt(94);
                    if((randomNr >= (int)ASCIIstart+0) && (randomNr <= (int)ASCIIstart+6) ||
                            (randomNr >= (int)ASCIIstart+7) && (randomNr <= (int)ASCIIstart+14) ||
                            (randomNr >= (int)ASCIIstart+25) && (randomNr <= (int)ASCIIstart+26) ||
                            (randomNr == (int)ASCIIstart+28) ||
                            (randomNr >= (int)ASCIIstart+30) && (randomNr <= (int)ASCIIstart+31) ||
                            (randomNr >= (int)ASCIIstart+58) && (randomNr <= (int)ASCIIstart+62) ||
                            (randomNr >= (int)ASCIIstart+90) && (randomNr <= (int)ASCIIstart+93)) {
                        innerGoOn = false;
                    }
                }
                newChar = (char)((int)ASCIIstart + randomNr);
                generatedPwd.setCharAt(index, newChar);
                container.put(ASCIIstart, container.get(ASCIIstart)+1);
            }
        }

        return generatedPwd.toString();
    }
}
