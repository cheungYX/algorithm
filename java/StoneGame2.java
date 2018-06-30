public class Solution {
    /**
     * @param A an integer array
     * @return an integer
     */
    public int stoneGame2(int[] A) {
        // Write your code here
        int n = A.length;
        if (n <= 1)
            return 0;

        int[][] dp = new int[2 * n][2 * n];

        int[] sum = new int[2 * n + 1];

        for (int i = 1; i <= 2 * n; ++i) {
            sum[i] = sum[i - 1] + A[(i - 1) % n];
        }

        for (int i = 0; i < 2 * n; ++i) {
            dp[i][i] = 0;
        }

        for(int len = 2; len <= 2 * n; ++len)
            for(int i= 0;i < 2 * n && i + len - 1 < 2 * n; ++i) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; ++k) {
                    if (dp[i][k] + dp[k+1][j] + sum[j + 1] - sum[i] < dp[i][j])
                        dp[i][j] = dp[i][k] + dp[k+1][j] + sum[j + 1] - sum[i];
                }
        }

        int ans = Integer.MAX_VALUE;
        for (int i = 0; i < n; ++i)
            if (dp[i][i + n - 1] < ans)
                ans = dp[i][i + n - 1];
        return ans;
        
    }
}