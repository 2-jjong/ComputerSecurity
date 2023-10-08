//import java.io.*;
//
//public class Main {
//    public static void main(String[] args) {
//        String readFilePath = "/Users/ijonghyeon/Desktop/ComputerSecurity/무제.txt";
//        StringBuilder contentBuilder = new StringBuilder();
//
//        try (BufferedReader reader = new BufferedReader(new FileReader(readFilePath))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                contentBuilder.append(line).append(System.lineSeparator());
//                System.out.println(line);
//            }
//        } catch (IOException® e) {
//            e.printStackTrace();
//        }
//
//        String WritefilePath = "/Users/ijonghyeon/Desktop/ComputerSecurity/무제1.txt";
//        String content = contentBuilder.toString();
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(WritefilePath))) {
//            writer.write(content);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//

public class Main {
    public static void main(String[] args) {
        DES des = new DES();
        String cipherText = des.encryption("abcdefgh", "qwerasdf");
        System.out.println("\nCiphertext(ASC) : " + cipherText);
        String plainText = des.encryption(cipherText, "qwerasdf");
        System.out.println("\nPlainText(ASC) : " + plainText);
    }
}