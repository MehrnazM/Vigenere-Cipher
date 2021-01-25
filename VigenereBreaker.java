import java.util.*;
import edu.duke.*;
import java.io.File;
public class VigenereBreaker {
    private FileResource fr;
    public VigenereBreaker(){
        fr = new FileResource();
    }
    public VigenereBreaker(File f){
        fr = new FileResource(f);
    }
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //REPLACE WITH YOUR CODE
        StringBuilder answer = new StringBuilder();
        for(int i = whichSlice; i < message.length(); i+=totalSlices){
            answer.append(message.charAt(i));
        }
        return answer.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        String[] slicedText = new String[klength];
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for(int i = 0; i < klength; i++){
            slicedText[i] = sliceString(encrypted,i,klength);
            key[i] = cc.getKey(slicedText[i]);
           // System.out.println(key[i]);
        }
        
        return key;
    }

    public void breakVigenere() {
        HashMap<String, HashSet<String>> languages = new HashMap<String,HashSet<String>>();
        //System.out.println("Select an encrypted file");
        //FileResource fr = new FileResource();
        String message = fr.asString();
        System.out.println(message);
        System.out.println("Select all the dictionaries you may need");
        DirectoryResource dr = new DirectoryResource();
        for(File f : dr.selectedFiles()){
            FileResource dic = new FileResource(f);
            String name = f.getName();
            HashSet<String> dictionary = readDictionary(dic);
            languages.put(name,dictionary);
        }
        System.out.println("Decrypt the message!");
        breakForAllLangs(message,languages);
    }
    public HashSet<String> readDictionary(FileResource fr){
        HashSet<String> wordSet = new HashSet<String>();
        for(String word: fr.lines()){
            wordSet.add(word.toLowerCase());
        }
        return wordSet;
    }
    public int countWords(String message, HashSet<String> dictionary){
        int count = 0;
        String[] words = message.split("\\W+");
        for(int i = 0; i < words.length; i++){
            String lcWord = words[i].toLowerCase();
            if(dictionary.contains(lcWord)){
                count++;
            }
        }
        return count;
    }
    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
        int max = 0; int keyLength = 0;
        String decrypted = "";
        char commonChar = mostCommonCharIn(dictionary);
        for(int i = 1; i < 101; i++){
            int[] keys = tryKeyLength(encrypted,i,commonChar);
            VigenereCipher vc = new VigenereCipher(keys);
            String tempDecrypted = vc.decrypt(encrypted);
            int temp = countWords(tempDecrypted,dictionary);
            if(temp > max){
                max = temp;
                decrypted = tempDecrypted;
                keyLength = i;
            }
        }
        System.out.println("The key length is: " + keyLength + " and the number of valid words is: " + max);
        return decrypted;
    }
    public char mostCommonCharIn(HashSet<String> dictionary){
        char ans = ' ';
        Iterator<String> it = dictionary.iterator(); 
        HashMap<Character,Integer> charCount = new HashMap<Character,Integer>();
        while(it.hasNext()){
            String word = it.next();
            for(int i = 0; i < word.length(); i++){
                char ch = word.charAt(i);
                if(!charCount.containsKey(ch)){
                    charCount.put(ch,1);
                }
                else{
                    charCount.put(ch,charCount.get(ch)+1);
                }
            }
        }
        int max = 0;
        for(Character ch: charCount.keySet()){
            if(max < charCount.get(ch)){
                max = charCount.get(ch);
                ans = ch;
            }
        }
        return ans;
    }
    public void breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages){
        int max = 0;
        String decrypted = null; String language = null;
        for(String lang: languages.keySet()){
            String tempDecrypted = breakForLanguage(encrypted,languages.get(lang));
            int tempCount = countWords(tempDecrypted,languages.get(lang));
            if(max < tempCount){
                max = tempCount;
                decrypted = tempDecrypted;
                language = lang;
            }
        }
        System.out.println("The language is: " + language);
        System.out.println("The message is: " + decrypted);
    }
}
