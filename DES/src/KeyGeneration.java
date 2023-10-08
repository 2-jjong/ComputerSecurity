public class KeyGeneration {
    private final String key;

    // Permuted Choice1 Table
    private static final int[] pc1_Table = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4};
    private int[] Lkey;
    private int[] Rkey;
    private int[][] subkey;

    public int[] getSubkey(int round) {
        return subkey[round];
    }

    public KeyGeneration(String key) {
        this.key = key;
        this.Lkey = new int[28];
        this.Rkey = new int[28];
        this.subkey = new int[16][48];
    }

    public void permuted_Choice1() {
        int[] num = new int[8];
        int[][] key_8_8bit = new int[8][8];
        int[] key_64bit = new int[64];
        int[] key_56bit = new int[56];
        int temp;
        int k = 0;

        StringBuilder sb = new StringBuilder();
        sb.append(this.key);
        String padding = "\0".repeat(9 - this.key.length());
        sb.append(padding);

        for (int i = 0; i < 8; i++) {
            num[i] = sb.charAt(i);
        }

        for (int i = 0; i < 8; i++) {
            key_8_8bit[i][0] = (num[i] / 128) == 1 ? 1 : 0;
            num[i] %= 128;
            key_8_8bit[i][1] = (num[i] / 64) == 1 ? 1 : 0;
            num[i] %= 64;
            key_8_8bit[i][2] = (num[i] / 32) == 1 ? 1 : 0;
            num[i] %= 32;
            key_8_8bit[i][3] = (num[i] / 16) == 1 ? 1 : 0;
            num[i] %= 16;
            key_8_8bit[i][4] = (num[i] / 8) == 1 ? 1 : 0;
            num[i] %= 8;
            key_8_8bit[i][5] = (num[i] / 4) == 1 ? 1 : 0;
            num[i] %= 4;
            key_8_8bit[i][6] = (num[i] / 2) == 1 ? 1 : 0;
            num[i] %= 2;
            key_8_8bit[i][7] = (num[i]) == 1 ? 1 : 0;
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                key_64bit[j + k] = key_8_8bit[i][j];
            }
            k = k + 8;
        }

        for (int i = 0; i < this.pc1_Table.length; i++) {
            temp = pc1_Table[i];
            key_56bit[i] = key_64bit[temp - 1];
        }

        for (int i = 0; i < key_56bit.length; i++) {
            if (i < 28) {
                Lkey[i] = key_56bit[i];
            } else {
                Rkey[i - 28] = key_56bit[i];
            }
        }
    }

    public void permuted_Choice2() {
        for (int round = 0; round < 16; round++) {
            // 각 라운드에서의 left shift 횟수 결정
            int shiftCount = (round == 0 || round == 1 || round == 8 || round == 15) ? 1 : 2;

            // Lkey와 Rkey를 왼쪽으로 shift
            for (int i = 0; i < shiftCount; i++) {
                leftShift(Lkey);
                leftShift(Rkey);
            }

            // Lkey와 Rkey를 합쳐서 새로운 서브키 생성 (48비트)
            generateSubKey(Lkey, Rkey, round);
        }
    }

    // 왼쪽으로 한 칸 시프트하는 메서드
    public void leftShift(int[] key) {
        int temp = key[0];

        for (int i = 0; i < key.length - 1; i++) {
            key[i] = key[i + 1];
        }

        key[key.length - 1] = temp;
    }

    public void generateSubKey(int[] Lkey, int[] Rkey, int round) {
        int[] temp = new int[56];
        int index = 0;

        for (int i = 0; i < Lkey.length + Rkey.length; i++) {
            if (i < 28) {
                temp[i] = Lkey[i];
            } else {
                temp[i] = Rkey[i - 28];
            }
        }

        int[] excludedbits = {8, 17, 21, 24, 34, 37, 42, 53};

        for (int i = 0; i < excludedbits.length; i++) {
            temp[(excludedbits[i] - 1)] = -1;
        }

        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != -1) {
                subkey[round][index] = temp[i];
                index++;
            }
        }
    }
}
