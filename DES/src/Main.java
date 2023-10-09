import java.io.*;

public class Main {
    // 파일을 읽어 문자열로 반환
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

        try (FileReader reader = new FileReader(filePath)) {
            int c;
            while ((c = reader.read()) != -1) {
                content.append((char) c);
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
        KeyGeneration key = new KeyGeneration("abcdefgh");  // Key: abcdefgh
        String ivStr = "abcdefgh";

        // 암호화
        try {
            // 파일 경로 설정
            String inputFilePath = "/Users/ijonghyeon/Desktop/plaintext.txt";
            String encryptionFilePath = "/Users/ijonghyeon/Desktop/ciphertext.txt";
            String decryptionFilePath = "/Users/ijonghyeon/Desktop/decryptiontext.txt";

            System.out.println("< Encryption >");
            // plainText 파일 읽기
            String plainText = readFile(inputFilePath);
            System.out.println("Key: " + key.getKey());
            System.out.println("Initialization Vector: " + ivStr);
            System.out.println("\nPlainText: " + plainText);

            // DES 암호화
            String cipherText = des.encryption_cbc(plainText, key, ivStr);
            System.out.println("\nCipherText: " + cipherText);

            // 결과를 파일에 쓰기
            writeFile(encryptionFilePath, cipherText);


            System.out.println("\n< Decryption >");
            // cipherText 파일 읽기
            cipherText = readFile(encryptionFilePath);
            System.out.println("Key: " + key.getKey());
            System.out.println("Initialization Vector: " + ivStr);
            System.out.println("\nCipherText: " + cipherText);

            // DES 복호화
            String decryptionText = des.decryption_cbc(cipherText, key, ivStr);
            System.out.println("\nPlainText: " + decryptionText);

            // 결과를 파일에 쓰기
            writeFile(decryptionFilePath, decryptionText);


            // plainText와 암호화 후 복호화한 decryptionText 검증
            if (plainText.equals(decryptionText)) {
                System.out.println("\n검증: PlainText와 DecryptionText가 같습니다.");
            } else {
                System.out.println("\n검증: PlainText와 DecryptionText가 같지 않습니다.");
                System.out.println("PlainText Length: " + plainText.length());
                System.out.println("DecryptionText Length: " + decryptionText.length());

                // 원본 텍스트와 복호화된 텍스트가 다른 경우 어느 부분이 다른지 출력
                for (int i = 0; i < plainText.length(); i++) {
                    if (plainText.charAt(i) != decryptionText.charAt(i)) {
                        System.out.println("다른 위치: 인덱스 " + i + ", 원본: " + plainText.charAt(i) + ", 복호화: " + decryptionText.charAt(i));
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}