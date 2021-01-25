import edu.duke.*;
import java.util.*;
import java.io.*;
public class Tester
{
    public void testCaesarCipher(){
        CaesarCipher cc = new CaesarCipher(10);
        FileResource fr = new FileResource();
        String message = fr.asString();
        System.out.println(message);
        String encrypted = cc.encrypt(message);
        System.out.println(encrypted);
        System.out.println(cc.decrypt(encrypted));
    }
    public void testCaesarCracker(){
        CaesarCracker cc = new CaesarCracker('a');
        FileResource fr = new FileResource();
        String decryptedMessage = fr.asString();
        String encryptedMessage = cc.decrypt(decryptedMessage);
        System.out.println(encryptedMessage);
    }
    public void testVigenereCipher(){
        int[] keys = {17,14,12,4};
        VigenereCipher vc = new VigenereCipher(keys);
        FileResource fr = new FileResource();
        String message = fr.asString();
        System.out.println(message);
        String encryptedMessage = vc.encrypt(message);
        String decryptedMessage = vc.decrypt(encryptedMessage);
        System.out.println(encryptedMessage);
        System.out.println(decryptedMessage);
    }
    public void testVigenereBreaker(){
        VigenereBreaker vb = new VigenereBreaker();
        //FileResource fr = new FileResource();
        //String encrypted = fr.asString();
        //int[] keys = vb.tryKeyLength(encrypted,38,'e');
        //VigenereCipher vc = new VigenereCipher(keys);
        //String decrypted = vc.decrypt(encrypted);
        //FileResource dic = new FileResource();
        //HashSet<String> dictionary = vb.readDictionary(dic); 
        //int count = vb.countWords(decrypted, dictionary);
        //System.out.println(count);
        vb.breakVigenere();
    }
    public void test(){
        FileResource fr = new FileResource();
        String message = fr.asString();
        int length  = message.length();
        int[] keys = new int[length/2];
        Random rand = new Random(); 
        for(int i = 0; i < length/2; i++){
             int rand_int = rand.nextInt(25);
             rand_int = rand_int + 1;
             keys[i] = rand_int;
        }
        VigenereCipher vc = new VigenereCipher(keys);
        String encryptedMessage = vc.encrypt(message);
        //String decryptedMessage = vc.decrypt(encryptedMessage);
        //System.out.println(decryptedMessage);
        File f = new File("encryptedTest.txt");
        try {
            FileWriter myWriter = new FileWriter(f);
            myWriter.write(encryptedMessage);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        VigenereBreaker vb = new VigenereBreaker(f);
        vb.breakVigenere();
    }
}
