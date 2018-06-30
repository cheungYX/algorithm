public class Solution {
    /**
     * @param A: An integer array.
     * @param target: An integer.
     */
    public int MinAdjustmentCost(ArrayList<Integer> A, int target) {
        // write your code here
        int n = A.size();
        int[][] f = new int[n + 1][101];
        for (int i = 0; i <= n ; ++i)
            for (int j = 0; j <=100; ++j)
                f[i][j] = Integer.MAX_VALUE;
        for (int i = 0; i <= 100; ++i)
            f[0][i] = 0;
        for (int i = 1; i <=n; ++i)
            for (int  j = 0; j <= 100;++j)
                if (f[i-1][j] != Integer.MAX_VALUE)
                for (int k = 0; k <= 100; ++k)
                    if (Math.abs(j-k) <= target)
                    if (f[i][k] > f[i-1][j] + Math.abs(A.get(i-1)-k))
                        f[i][k] = f[i-1][j] + Math.abs(A.get(i-1)-k);  
        int ans = Integer.MAX_VALUE;
        for (int i = 0; i <= 100; ++i)
            if (f[n][i] < ans)
                ans = f[n][i];
        return ans; 
    }
}