public class Solution {
    /**
     * @param A an integer array
     * @param V an integer array
     * @param m an integer
     * @return an array
     */
    public int backPackIII(int[] A, int[] V, int m) {
        // Write your code here
        int n = A.length;
        int[] f = new int[m+1];        
        for (int i = 0; i < n; ++i)
            for (int j = A[i]; j <= m; ++j)
                if (f[j - A[i]] + V[i] > f[j])
                    f[j] = f[j - A[i]] + V[i];
        return f[m];
    }
}

// 2D version, 如果你无法理解一维的solution, 可以从二维的solution入手,然后思考空间的优化
public class Solution {
    /**
     * @param A an integer array
     * @param V an integer array
     * @param m an integer
     * @return an array
     */
    public int backPackIII(int[] A, int[] V, int m) {
        // Write your code here
        int n = A.length;
        int[][] f = new int[n + 1][m + 1];        
        for (int i = 1; i <= n; ++i)
            for (int j = 0; j <= m; ++j) {
                f[i][j] = f[i - 1][j];
                if (j >= A[i - 1])
                    f[i][j] = Math.max(f[i][j - A[i - 1]] + V[i - 1], f[i][j]);
            }
        return f[n][m];
    }
}