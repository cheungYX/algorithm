public class Solution {
    /**
     * @param m: An integer m denotes the size of a backpack
     * @param A: Given n items with size A[i]
     * @return: The maximum size
     */
    public int backPack(int m, int[] A) {
        boolean f[][] = new boolean[A.length + 1][m + 1];
        for (int i = 0; i <= A.length; i++) {
            for (int j = 0; j <= m; j++) {
                f[i][j] = false;
            }
        }
        f[0][0] = true;
        for (int i = 1; i <= A.length; i++) {
            for (int j = 0; j <= m; j++) {
                f[i][j] = f[i - 1][j];
                if (j >= A[i-1] && f[i-1][j - A[i-1]]) {
                    f[i][j] = true;
                }
            } // for j
        } // for i
        
        for (int i = m; i >= 0; i--) {
            if (f[A.length][i]) {
                return i;
            }
        }
        
        return 0;
    }
}


// O(m) 空间复杂度的解法
public class Solution {
    /**
     * @param m: An integer m denotes the size of a backpack
     * @param A: Given n items with size A[i]
     * @return: The maximum size
     */
    public int backPack(int m, int[] A) {
        int f[] = new int[m + 1];

        for (int i = 0; i < A.length; i++) {
            for (int j = m; j >= A[i]; j--) {
                f[j] = Math.max(f[j], f[j - A[i]] + A[i]);
            } 
        }
        return f[m];
    }
}