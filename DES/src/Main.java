import java.io.*;


public class Main {
    // 파일을 읽어 문자열로 반환
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();

//        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
//            String line;
//            boolean appendNewLine = false;
//            while ((line = br.readLine()) != null) {
//                content.append(appendNewLine ? '\n' : "");
//                content.append(line);
//                appendNewLine = true;
//            }
//        }

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

        // 암호화
        try {
            // 파일 경로 설정
            String inputFilePath = "C:\\Users\\1\\Desktop\\plainText.txt";
            String encryptionFilePath = "C:\\Users\\1\\Desktop\\cipherText.txt";
            String decryptionFilePath = "C:\\Users\\1\\Desktop\\decryptionText.txt";


            System.out.println("< Encryption >");
            // plainText 파일 읽기
            String plainText = readFile(inputFilePath);
            System.out.println("Key: " + key.getKey());
            System.out.println("PlainText: " + plainText);

            // DES 암호화
            String cipherText = des.encryption(plainText, key);
            System.out.println("CipherText: " + cipherText);

            // 결과를 파일에 쓰기
            writeFile(encryptionFilePath, cipherText);




            System.out.println("\n< Decryption >");
            // cipherText 파일 읽기
            cipherText = readFile(encryptionFilePath);
            System.out.println("Key: " + key.getKey());
            System.out.println("CipherText: " + cipherText);

            // DES 복호화
            String decryptionText = des.decryption(cipherText, key);
            System.out.println("PlainText: " + decryptionText);

            // 결과를 파일에 쓰기
            writeFile(decryptionFilePath, decryptionText);


            // plainText와 암호화 후 복호화한 decryptionText 검증
            if(des.verification(plainText, decryptionText)){
                System.out.println("\n검증: PlainText와 DecryptionText가 같습니다.");
            } else{
                System.out.println("\n검증: PlainText와 DecryptionText가 같지 않습니다.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}