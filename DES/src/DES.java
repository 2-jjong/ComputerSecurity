import java.util.Arrays;

public class DES {
    // IP Table
    private static final int[] IP_Table = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    // inverse IP Table
    private static final int[] inverse_IP_Table = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    // Permutation Table
    private static final int[] per_Table = {
            16, 7, 20, 21, 29, 12, 28, 17,
            1, 15, 23, 26, 5, 18, 31, 10,
            2, 8, 24, 14, 30, 27, 3, 9,
            19, 13, 32, 6, 22, 11, 4, 25};

    // Expansion Table
    private static final int[] expansionTable = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1};

    // S-Box
    private static final int[][][] s_Box = {
            // S-Box 1
            {
                    {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
            },

            // S-Box 2
            {
                    {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
            },

            // S-Box 3
            {
                    {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
            },

            // S-Box 4
            {
                    {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
            },

            // S-Box 5
            {
                    {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
            },

            // S-Box 6
            {
                    {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
            },

            // S-Box 7
            {
                    {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
            },

            // S-Box 8
            {
                    {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
            }
    };

    // Encryption
    public String encryption(String text, KeyGeneration key) {
        StringBuilder cipherBuilder = new StringBuilder();

        // 입력 문자열을 64bit 블록으로 나누어 암호화 진행
        for (int i = 0; i < text.length(); i += 8) {
            // 64bit(8글자)씩 블록을 나눔
            String block = text.substring(i, Math.min(i + 8, text.length()));

            int[] plainText = input(block); // 문자열을 64bit text로 변환
            int[] initialPermutationText = permutation(plainText, IP_Table);    // IP_Table을 사용하여 Initial Permutation 진행
            int[] leftText = division(initialPermutationText, 0, 32);   // 32bit leftText
            int[] rightText = division(initialPermutationText, 32, 64); // 32bit rightText

            for (int round = 0; round < 16; round++) {  // 16 round 진행
                int[] expandText = expansion(rightText);    // 32bit rightText를 48bit로 확장
                int[] xorText = XOR(expandText, key.getSubKey(round));  // expansion한 text를 각 round에 맞는 subkey와 XOR
                int[] substitutionText = substitution(xorText); // XOR한 text를 32bit로 축소
                int[] permutationText = permutation(substitutionText, per_Table);   // per_Table을 사용하여 Permutation 진행
                int[] temp = rightText;   // rightText를 leftText로 옮기기 위해 임시로 저장
                rightText = XOR(leftText, permutationText); // leftText와 permutation한 text XOR
                leftText = temp;    // 임시 저장한 rightText를 leftText로 변경
            }

            int[] combineText = combine(rightText, leftText);   // 16 round 진행 한 left, right text를 swap해서 결합
            int[] cipherText = permutation(combineText, inverse_IP_Table);  // inverse_IP_Table을 사용하여 Inverse Permutation 진행
            String cipherBlock = output(cipherText);    // 64bit text를 문자열로 변환

            cipherBuilder.append(cipherBlock);  // 한 블록의 문자열을 추가
        }

        return cipherBuilder.toString();    // 모든 블록을 끝낸 최종 문자열을 반환
    }

    // Decryption
    public String decryption(String text, KeyGeneration key) {
        StringBuilder plainBuilder = new StringBuilder();

        // 입력 문자열을 64bit 블록으로 나누어 복호화 진행
        for (int i = 0; i < text.length(); i += 8) {
            // 64bit(8글자)씩 블록을 나눔
            String block = text.substring(i, Math.min(i + 8, text.length()));

            int[] cipherText = input(block);    // 문자열을 64bit text로 변환
            int[] initialPermutationText = permutation(cipherText, IP_Table);   // IP_Table을 사용하여 Initial Permutation 진행
            int[] leftText = division(initialPermutationText, 0, 32);   // 32bit leftText
            int[] rightText = division(initialPermutationText, 32, 64); // 32bit rightText

            for (int round = 15; round >= 0; round--) { // 복호화는 암호화와 반대로 key를 마지막부터 사용하여 16round 진행
                int[] expandText = expansion(rightText);    // 32bit rightText를 48bit로 확장
                int[] xorText = XOR(expandText, key.getSubKey(round));  // expansion한 text를 각 round에 맞는 subkey와 XOR
                int[] substitutionText = substitution(xorText); // XOR한 text를 32bit로 축소
                int[] permutationText = permutation(substitutionText, per_Table);    // per_Table을 사용하여 Permutation 진행
                int[] temp = rightText;   // rightText를 leftText로 옮기기 위해 임시로 저장
                rightText = XOR(leftText, permutationText); // leftText와 permutation한 text XOR
                leftText = temp;    // 임시 저장한 rightText를 leftText로 변경
            }

            int[] combineText = combine(rightText, leftText);   // 16 round 진행 한 left, right text를 swap해서 결합
            int[] plainText = permutation(combineText, inverse_IP_Table);   // inverse_IP_Table을 사용하여 Inverse Permutation 진행
            String plainBlock = output(plainText);  // 64bit text를 문자열로 변환

            plainBuilder.append(plainBlock);    // 한 블록의 문자열을 추가
        }

        removePadding(plainBuilder);    // 64비트 블록으로 잘랐을 때 넣은 패딩 제거

        return plainBuilder.toString();     // 모든 블록을 끝낸 최종 문자열을 반환
    }

    // 검증
    public boolean verification(String plainText, String decryptionText){
        int[] text1 = new int[plainText.length()];
        int[] text2 = new int[decryptionText.length()];
        char c;

        for (int i = 0; i < text1.length; i++) {
            c = plainText.charAt(i);
            text1[i] = (int) c;
        }

        for (int i = 0; i < text2.length; i++) {
            c = decryptionText.charAt(i);
            text2[i] = (int) c;
        }

        if(text1.length == text2.length){
            for (int i = 0; i < text2.length; i++) {
                if(text1[i] != text2[i])
                    return false;
            }
        }

        return true;
    }

    // Input
    public int[] input(String input) {
        // 문자열의 길이가 64bit(8글자)인지 확인하고 아닌 경우 공백으로 패딩
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 8 - input.length(); i++) {
            sb.append(' ');
        }

        input = input + sb.toString();

        // 문자열을 2진수 64bit로 변환
        int[] plainText = new int[64];
        int temp;

        for (int i = 0; i < 8; i++) {
            char c = input.charAt(i);
            temp = (int) c;

            for (int j = 0; j < 8; j++) {
                plainText[i * 8 + j] = (temp & (1 << (7 - j))) != 0 ? 1 : 0;
            }
        }


        return plainText;
    }

    // Output
    public String output(int[] arr) {
        StringBuilder sb = new StringBuilder();

        // 2진수 64bit를 문자열로 변환
        int temp;

        for (int i = 0; i < arr.length; i += 8) {
            temp = 0;

            for (int j = 0; j < 8; j++) {
                temp += arr[i + j] * (1 << (7 - j));
            }

            sb.append((char) temp);
        }

        return sb.toString();
    }

    public void removePadding(StringBuilder sb) {
        int i = sb.length() - 1;

        while (i >= 0 && Character.isWhitespace(sb.charAt(i))) {
            i--;
        }

        sb.setLength(i + 1);
    }

    // Permutation
    public int[] permutation(int[] arr, int[] permutation_Table) {
        // 매개변수로 받은 Table로 Permutation 진행
        int[] result = new int[arr.length];
        int index;

        for (int i = 0; i < arr.length; i++) {
            index = permutation_Table[i] - 1; // Table의 값이 인덱스로 사용될 경우 -1
            result[i] = arr[index];
        }

        return result;
    }

    // Division
    public int[] division(int[] arr, int start, int end) {
        // arr을 start부터 end까지 복사하여 나눔
        int[] result = Arrays.copyOfRange(arr, start, end);
        return result;
    }

    // Expansion
    public int[] expansion(int[] arr) {
        // Table을 사용하여 32bit -> 48bit 확장
        int[] result = new int[48];
        int index;

        for (int i = 0; i < 48; i++) {
            index = expansionTable[i] - 1; // Table의 값이 인덱스로 사용될 경우 -1
            result[i] = arr[index];
        }

        return result;
    }

    // XOR
    public int[] XOR(int[] arr, int[] arr2) {
        // arr과 arr2를 XOR 진행
        int[] result = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i] ^ arr2[i];
        }

        return result;
    }

    // Substitution
    public int[] substitution(int[] arr) {
        // S-Box를 사용하여 48bit -> 32bit로 축소
        int[] result = new int[32];
        int i, num;
        int row = 0, col = 0;

        for (i = 0; i < 8; i++) {
            row = (arr[i * 6] * 2) + (arr[i * 6 + 5] * 1);
            col = (arr[i * 6 + 1] * 8) + (arr[i * 6 + 2] * 4) + (arr[i * 6 + 3] * 2) + (arr[i * 6 + 4] * 1);
            num = s_Box[i][row][col];
            result[i * 4] = (num / 8) != 0 ? 1 : 0;
            num %= 8;
            result[i * 4 + 1] = (num / 4) != 0 ? 1 : 0;
            num %= 4;
            result[i * 4 + 2] = (num / 2) != 0 ? 1 : 0;
            num %= 2;
            result[i * 4 + 3] = (num) != 0 ? 1 : 0;
        }

        return result;
    }

    // Combine
    public int[] combine(int[] arr, int[] arr2) {
        // arr과 arr2를 결합
        int[] result = new int[arr.length + arr2.length];

        System.arraycopy(arr, 0, result, 0, arr.length);
        System.arraycopy(arr2, 0, result, arr.length, arr2.length);

        return result;
    }
}