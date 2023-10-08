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
    int[] per_Table = {
            15, 6, 19, 20, 28, 11, 27, 16,
            0, 14, 22, 25, 4, 17, 30, 9,
            1, 7, 23, 13, 31, 26, 2, 8,
            18, 12, 29, 5, 21, 10, 3, 24};

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

    public void encryption(String text) {
        int[] plainText = input(text);
        int[][] leftText = new int[8][4];
        int[][] rightText = new int[8][4];
        initialPermutation(plainText);
        divide_LR(plainText, leftText, rightText);


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
        System.out.println("\nCiphertext(ASC) : " + sb.toString());
        return sb.toString();
    }

    // Initial Permutation
    public void initialPermutation(int[] p) {
        int i;
        int temp;
        int[] q = new int[64];

        for (i = 0; i < 64; i++) {
            q[i] = p[i];
        }
        for (i = 0; i < 64; i++) {
            temp = IP_Table[i];
            p[i] = q[temp - 1];
        }
    }

    // Inverse Initial Permutation
    public void inverseInitialPermutation(int[] p) {
        int i;
        int temp;
        int[] q = new int[64];

        for (i = 0; i < 64; i++) {
            q[i] = p[i];
        }
        for (i = 0; i < 64; i++) {
            temp = inverse_IP_Table[i];
            p[i] = q[temp - 1];
        }
    }

    // Round Permutation
    public void permutation(int[] p) {
        int i;
        int temp;
        int[] q = new int[32];

        for (i = 0; i < 32; i++) {
            q[i] = p[i];
        }
        for (i = 0; i < 32; i++) {
            temp = per_Table[i];
            p[i] = q[temp];
        }
    }

    public void divide_LR(int[] text, int[][] left, int[][] right) {
        int i, j;
        int k = 0;
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 4; j++) {
                left[i][j] = text[j + k];
                right[i][j] = text[j + 32 + k];
            }
            k = k + 4;
        }
    }

    public void combine_8_4bit_to_32bit(int[][] arr, int[] comArr) {
        int i, j;
        int k = 0;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 4; j++) {
                comArr[j + k] = arr[i][j];
            }
            k = k + 4;
        }
    }

    public void extend_R(int[][] arr, int[][] exArr) {
        int i, j;

        for (i = 0; i < 8; i++) {
            exArr[i][5] = arr[(i + 1) % 8][0];
            exArr[i][0] = arr[(7 + i) % 8][3];

            for (j = 1; j < 5; j++) {
                exArr[i][j] = arr[i][j - 1];
            }
        }
    }

    public void combine_8_6bit_to_48bit(int[][] arr, int[] comArr) {
        int i, j;
        int k = 0;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 6; j++) {
                comArr[j + k] = arr[i][j];
            }
            k = k + 6;
        }
    }

    public void XOR(int s, int[] arr, int[] arr2, int[] arr_AfterXOR_arr2) {
        int i = 0;

        if (s == 48) {
            for (i = 0; i < 48; i++) {
                arr_AfterXOR_arr2[i] = arr[i] ^ arr2[i];
            }
        } else {
            for (i = 0; i < 32; i++) {
                arr_AfterXOR_arr2[i] = arr[i] ^ arr2[i];
            }
        }
    }

    public void divide_48bit_to_8_6bit(int[] arr, int[][] divideArr) {
        int i, j;
        int k = 0;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 6; j++) {
                divideArr[i][j] = arr[j + k];
            }
            k = k + 6;
        }
    }

    public void substitution(int[][] exArr, int[][] R_AftersBox_8_4) {
        int i, num;
        int j = 0;
        int row = 0, col = 0;

        for (i = 0; i < 8; i++) {
            row = (exArr[i][0] * 2) + (exArr[i][5] * 1);
            col = (exArr[i][1] * 8) + (exArr[i][2] * 4) + (exArr[i][3] * 2) + (exArr[i][4] * 1);
            num = s_Box[j][row][col];
            R_AftersBox_8_4[i][0] = (num / 8) != 0 ? 1 : 0;
            num %= 8;
            R_AftersBox_8_4[i][1] = (num / 4) != 0 ? 1 : 0;
            num %= 4;
            R_AftersBox_8_4[i][2] = (num / 2) != 0 ? 1 : 0;
            num %= 2;
            R_AftersBox_8_4[i][3] = (num) != 0 ? 1 : 0;
            j++;
            if (j == 8) {
                j = 0;
            }
        }
    }

    public void combine_64_bit(int[] arr, int[] arr2, int[] arr3) {
        for (int j = 0; j < 64; j++) {
            if (j < 32) {
                arr3[j] = arr[j];
            } else {
                arr3[j] = arr2[j - 32];
            }
        }
    }
}
