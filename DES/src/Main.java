import java.io.*;


public class Main {
    // 파일을 읽어 문자열로 반환
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    // 문자열을 파일에 쓰기
    private static void writeFile(String filePath, String content) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(content);
        }
    }
    public static void main(String[] args) {
        DES des = new DES();
        KeyGeneration key = new KeyGeneration("abcdefgh");
        key.permuted_Choice1();
        key.permuted_Choice2();

        try {
            // 파일 경로 설정
            String inputFilePath = "C:\\Users\\1\\Desktop\\plainText.txt";
            String outputFilePath = "C:\\Users\\1\\Desktop\\cipherText.txt";

            // plainText 파일 읽기
            String plainText = readFile(inputFilePath);

            // DES 암호화
            String cipherText = des.encryption(plainText, key);

            // 결과를 파일에 쓰기
            writeFile(outputFilePath, cipherText);

            System.out.println("\nCiphertext(ASC) : " + cipherText);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            // 파일 경로 설정
            String inputFilePath = "C:\\Users\\1\\Desktop\\cipherText.txt";
            String outputFilePath = "C:\\Users\\1\\Desktop\\plainText2.txt";

            // plainText 파일 읽기
            String cipherText = readFile(inputFilePath);

            // DES 복호화
            String plainText = des.decryption(cipherText, key);

            // 결과를 파일에 쓰기
            writeFile(outputFilePath, plainText);

            System.out.println("\nPlaintext(ASC) : " + plainText);
        } catch (IOException e) {
            e.printStackTrace();
        }



//        DES des = new DES();
//        KeyGeneration key = new KeyGeneration("abcdefgh");
//        key.permuted_Choice1();
//        key.permuted_Choice2();
//
//        String cipherText = des.encryption("abcd", key);
//        System.out.println("\nCiphertext(ASC) : " + cipherText);
//        String plainText = des.decryption(cipherText, key);
//        System.out.println("\nPlainText(ASC) : " + plainText);
    }
}