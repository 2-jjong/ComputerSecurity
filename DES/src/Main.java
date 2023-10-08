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

//public class Main {
//    public static void main(String[] args) {
//        int[] plainText;
//        int[][] L0 = new int[8][4];
//        int[][] R0 = new int[8][4];
//        int[] comL0 = new int[32];
//        int[] comR0 = new int[32];
//        int[][] exArr = new int[8][6];
//        int[] comExArr = new int[48];
//        int[] subkey;
//        int[] resultArr = new int[48];
//        int[][] devideArr = new int[8][6];
//        int[][] sBoxArr = new int[8][4];
//        int[] comSBoxArr = new int[32];
//        int[] resultArr2 = new int[32];
//
//        DES des = new DES();
//        KeyGeneration keyGeneration = new KeyGeneration("abcdefgh");
//        keyGeneration.permuted_Choice1();
//        keyGeneration.permuted_Choice2();
//
//        plainText = des.input("abcdefgh");
//        des.initialPermutation(plainText);
//
//        for (int i = 0; i < 16; i++) {
//            des.divide_LR(plainText, L0, R0);
//            des.combine_8_4bit_to_32bit(L0, comL0);
//            des.combine_8_4bit_to_32bit(R0, comR0);
//            des.extend_R(R0, exArr);
//            des.combine_8_6bit_to_48bit(exArr, comExArr);
//            des.XOR(48, comExArr, keyGeneration.getSubkey(i), resultArr);
//            des.divide_48bit_to_8_6bit(resultArr, devideArr);
//            des.sBox(devideArr, sBoxArr);
//            des.combine_8_4bit_to_32bit(sBoxArr, comSBoxArr);
//            des.permutation(comSBoxArr);
//            des.XOR(32, comL0, comSBoxArr, resultArr2);
//            comL0 = comR0;
//            comR0 = resultArr2;
//            if (i == 15) {
//                des.combine_64_bit(comR0, comL0, plainText);
//            } else {
//                des.combine_64_bit(comL0, comR0, plainText);
//            }
//        }
//        des.inverseInitialPermutation(plainText);
//        String text = des.output(plainText);
//
//
//        plainText = des.input(text);
//        des.initialPermutation(plainText);
//
//        for (int i = 15; i >= 0; i--) {
//            des.divide_LR(plainText, L0, R0);
//            des.combine_8_4bit_to_32bit(L0, comL0);
//            des.combine_8_4bit_to_32bit(R0, comR0);
//            des.extend_R(R0, exArr);
//            des.combine_8_6bit_to_48bit(exArr, comExArr);
//            subkey = keyGeneration.getSubkey(i);
//            des.XOR(48, comExArr, subkey, resultArr);
//            des.divide_48bit_to_8_6bit(resultArr, devideArr);
//            des.sBox(devideArr, sBoxArr);
//            des.combine_8_4bit_to_32bit(sBoxArr, comSBoxArr);
//            des.permutation(comSBoxArr);
//            des.XOR(32, comL0, comSBoxArr, resultArr2);
//            comL0 = comR0;
//            comR0 = resultArr2;
//            if (i == 0) {
//                des.combine_64_bit(comR0, comL0, plainText);
//            } else {
//                des.combine_64_bit(comL0, comR0, plainText);
//            }
//        }
//        des.inverseInitialPermutation(plainText);
//        des.output(plainText);
//    }
//}