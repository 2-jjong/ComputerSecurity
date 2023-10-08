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

    public String encryption(String text, String key) {
        int[] plainText = input(text);
        permutation(plainText, IP_Table);
        int[] leftText = divide(plainText, 0, 32);
        int[] rightText = divide(plainText, 32, 64);

        KeyGeneration keyGeneration = new KeyGeneration(key);
        keyGeneration.permuted_Choice1();
        keyGeneration.permuted_Choice2();

        for (int round = 0; round < 16; round++) {
            int[] expandText = expansion(rightText);
            int[] xorText = XOR(expandText, keyGeneration.getSubkey(round));
            int[] substitutionText = substitution(xorText);
            int[] permutationText = permutation(substitutionText, per_Table);
            leftText = rightText;
            rightText = XOR(leftText, permutationText);
        }

        int[] combineText = combine(rightText, leftText);
        int[] cipherText = permutation(combineText, inverse_IP_Table);
        String cipher = output(cipherText);

        return cipher;
    }

    public String decryption(String text, String key) {
        int[] cipherText = input(text);
        permutation(cipherText, IP_Table);
        int[] leftText = divide(cipherText, 0, 32);
        int[] rightText = divide(cipherText, 32, 64);

        KeyGeneration keyGeneration = new KeyGeneration(key);
        keyGeneration.permuted_Choice1();
        keyGeneration.permuted_Choice2();

        for (int round = 15; round >= 0; round--) {
            int[] expandText = expansion(rightText);
            int[] xorText = XOR(expandText, keyGeneration.getSubkey(round));
            int[] substitutionText = substitution(xorText);
            int[] permutationText = permutation(substitutionText, per_Table);
            leftText = rightText;
            rightText = XOR(leftText, permutationText);
        }

        int[] combineText = combine(rightText, leftText);
        int[] plainText = permutation(combineText, inverse_IP_Table);
        String plain = output(plainText);

        return plain;
    }

    public int[] input(String input) {
        int[][] arr = new int[8][8];
        int[] num = new int[8];
        int[] plainText = new int[64];
        int p = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(input);
        String padding = "\0".repeat(9 - input.length());
        sb.append(padding);

        for (int i = 0; i < 8; i++) {
            num[i] = sb.charAt(i);
        }

        for (int i = 0; i < 8; i++) {
            arr[i][0] = (num[i] / 128) == 1 ? 1 : 0;
            num[i] %= 128;
            arr[i][1] = (num[i] / 64) == 1 ? 1 : 0;
            num[i] %= 64;
            arr[i][2] = (num[i] / 32) == 1 ? 1 : 0;
            num[i] %= 32;
            arr[i][3] = (num[i] / 16) == 1 ? 1 : 0;
            num[i] %= 16;
            arr[i][4] = (num[i] / 8) == 1 ? 1 : 0;
            num[i] %= 8;
            arr[i][5] = (num[i] / 4) == 1 ? 1 : 0;
            num[i] %= 4;
            arr[i][6] = (num[i] / 2) == 1 ? 1 : 0;
            num[i] %= 2;
            arr[i][7] = (num[i]) == 1 ? 1 : 0;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                plainText[j + p] = arr[i][j];
            }
            p = p + 8;
        }

        return plainText;
    }

    public String output(int[] arr) {
        int i, j, s = 0;
        int[] num = new int[8];
        int Decimal = 0;
        StringBuilder sb = new StringBuilder();
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                num[j] = arr[j + s];
            }
            s = s + 8;
            Decimal = (num[7] * 1) + (num[6] * 2) + (num[5] * 4) + (num[4] * 8) + (num[3] * 16) + (num[2] * 32) + (num[1] * 64) + (num[0] * 128);
            sb.append((char) Decimal);
        }
        return sb.toString();
    }

    public int[] permutation(int[] arr, int[] permutation_Table) {
        int[] result = new int[arr.length];
        int temp;

        for (int i = 0; i < arr.length; i++) {
            temp = permutation_Table[i];
            result[i] = arr[temp - 1];
        }

        return result;
    }

    public int[] divide(int[] arr, int start, int end) {
        int[] result = Arrays.copyOfRange(arr, start, end);
        return result;
    }

    public int[] expansion(int[] arr) {
        int[] result = new int[48];
        for (int i = 0; i < 48; i++) {
            int index = expansionTable[i] - 1; // 테이블의 인덱스
            result[i] = arr[index];
        }
        return result;
    }

    public int[] XOR(int[] arr, int[] arr2) {
        int[] result = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i] ^ arr2[i];
        }

        return result;
    }

    public int[] substitution(int[] arr) {
        int[] result = new int[32]; // 32비트 출력 배열
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

    public int[] combine(int[] arr, int[] arr2) {
        int[] result = new int[arr.length + arr2.length];

        System.arraycopy(arr, 0, result, 0, arr.length);
        System.arraycopy(arr2, 0, result, arr.length, arr2.length);

        return result;
    }
}