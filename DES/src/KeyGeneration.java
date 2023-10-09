import java.util.Arrays;

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

    private static final int[] pc2_Table = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    // 16 라운드를 진행할 SubKey
    private int[][] subKey;

    public String getKey() {
        return this.key;
    }

    public int[] getSubKey(int round) {
        return this.subKey[round];
    }

    public KeyGeneration(String key) {
        this.key = key;
        this.subKey = new int[16][48];
        create_SubKey();
    }

    // subKey 생성
    public void create_SubKey(){
        int[] pc1_Key = permuted_Choice1(); // Permuted Choice1 진행
        int[] leftKey = division(pc1_Key, 0,28);    // leftKey로 분할
        int[] rightKey = division(pc1_Key, 28,56);  // rightKey로 분할
        permuted_Choice2(leftKey, rightKey);    // Permuted Choice2로 16 라운드의 subKey 생성
    }

    // Permuted Choice1
    public int[] permuted_Choice1() {
        int[] key_64bit = new int[64];
        int[] key_56bit = new int[56];
        int temp;

        // 문자열의 길이가 64bit(8글자)인지 확인하고 아닌 경우 공백으로 채움
        StringBuilder sb = new StringBuilder();
        sb.append(this.key);
        String padding = "\0".repeat(9 - this.key.length());
        sb.append(padding);

        // 문자열을 2진수 64bit로 변환
        for (int i = 0; i < 8; i++) {
            char c = sb.charAt(i);
            temp = (int) c;

            for (int j = 0; j < 8; j++) {
                key_64bit[i * 8 + j] = (temp & (1 << (7 - j))) != 0 ? 1 : 0;
            }
        }

        // pc1_Table에 넣어 64bit key를 56bit key로 축소
        for (int i = 0; i < this.pc1_Table.length; i++) {
            temp = pc1_Table[i];
            key_56bit[i] = key_64bit[temp - 1];
        }

        return key_56bit;
    }

    // Division
    public int[] division(int[] key, int start, int end) {
        // key을 start부터 end까지 복사하여 나눔
        int[] result = Arrays.copyOfRange(key, start, end);
        return result;
    }

    // Permuted Choice2
    public void permuted_Choice2(int[] leftKey, int[] rightKey) {
        for (int round = 0; round < 16; round++) {
            // 1,2,9,16 라운드는 1번, 나머지 라운드는 2번 shift 진행
            int shiftCount = (round == 0 || round == 1 || round == 8 || round == 15) ? 1 : 2;

            // Lkey와 Rkey를 각각 left shift
            for (int i = 0; i < shiftCount; i++) {
                leftShift(leftKey);
                leftShift(rightKey);
            }

            // leftKey와 rightKey를 합쳐서 새로운 subKey 생성 (48비트)
            generateSubKey2(leftKey, rightKey, round);
        }
    }

    // Left Shift
    public void leftShift(int[] key) {
        int temp = key[0];

        for (int i = 0; i < key.length - 1; i++) {
            key[i] = key[i + 1];
        }

        key[key.length - 1] = temp;
    }

    // SubKey 생성
    public void generateSubKey(int[] leftKey, int[] rightKey, int round) {
        int[] temp = new int[56];
        int index = 0;

        // leftKey, rightKey 결합
        for (int i = 0; i < leftKey.length + rightKey.length; i++) {
            if (i < 28) {
                temp[i] = leftKey[i];
            } else {
                temp[i] = rightKey[i - 28];
            }
        }

        // 48bit로 축소하기 위해 left의 8,17,21,25 bit, rihgt의 6,9,14,25 bit 제거
        int[] removeBit = {8, 17, 21, 24, 34, 37, 42, 53};

        for (int i = 0; i < removeBit.length; i++) {
            temp[(removeBit[i] - 1)] = -1;
        }

        // 최종 subKey 생성
        for (int i = 0; i < temp.length; i++) {
            if (temp[i] != -1) {
                this.subKey[round][index] = temp[i];
                index++;
            }
        }
    }

    // SubKey 생성
    public void generateSubKey2(int[] leftKey, int[] rightKey, int round) {
        int[] key_56bit = new int[56];
        int[] key_48bit = new int[48];
        int temp;
        int index = 0;

        // leftKey, rightKey 결합
        for (int i = 0; i < leftKey.length + rightKey.length; i++) {
            if (i < 28) {
                key_56bit[i] = leftKey[i];
            } else {
                key_56bit[i] = rightKey[i - 28];
            }
        }

        // pc2_Table에 넣어 64bit key를 56bit key로 축소
        for (int i = 0; i < this.pc2_Table.length; i++) {
            temp = pc2_Table[i];
            key_48bit[i] = key_56bit[temp - 1];
        }


        // 최종 subKey 생성
        for (int i = 0; i < key_48bit.length; i++) {
            if (key_48bit[i] != -1) {
                this.subKey[round][index] = key_48bit[i];
                index++;
            }
        }
    }
}
