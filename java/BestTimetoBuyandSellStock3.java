// 动态规划版本 verison 1
class Solution {
    /**
     * @param prices: Given an integer array
     * @return: Maximum profit
     */
    
    private int update(int a, int b, int delta) {
        if (b == Integer.MIN_VALUE) {
            return a;
        }
        
        if (b + delta > a) {
            return b + delta;
        }
        
        return a;
    } 
     
    public int maxProfit(int[] prices) {
        int K = 2;
        int n = prices.length;
        int i, j, k;
        
        int[][] f = new int[n+1][2*K+1+1];
        for (i = 0; i <= n; ++i) {
            for (j = 1; j <= 2*K+1; ++j) {
                f[i][j] = Integer.MIN_VALUE;
            }
        }
        
        f[0][1] = 0;
        for (i = 1; i <= n; ++i) {
            for (j = 1; j <= 2 * K + 1; j += 2) {
                f[i][j] = update(f[i][j], f[i-1][j], 0);
                if (j > 1 && i > 1) f[i][j] = update(f[i][j], f[i - 1][j - 1], prices[i - 1] - prices[i - 2]);
            }
            
            for (j = 2; j <= 2 * K; j += 2) {
                if (i > 1) f[i][j] = update(f[i][j], f[i-1][j], prices[i - 1] - prices[i - 2]);
                if (j > 1) f[i][j] = update(f[i][j], f[i-1][j-1], 0);
            }
        }
        
        int res = Integer.MIN_VALUE;
        for (j = 1; j <= 2 * K + 1; j += 2) {
            res = update(res, f[n][j], 0);
        }
        
        return res;
    }
};


// version 2
public class Solution {
    public int maxProfit(int[] prices) {
        if (prices == null || prices.length <= 1) {
            return 0;
        }

        int[] left = new int[prices.length];
        int[] right = new int[prices.length];

        // DP from left to right;
        left[0] = 0;
        int min = prices[0];
        for (int i = 1; i < prices.length; i++) {
            min = Math.min(prices[i], min);
            left[i] = Math.max(left[i - 1], prices[i] - min);
        }

        //DP from right to left;
        right[prices.length - 1] = 0;
        int max = prices[prices.length - 1];
        for (int i = prices.length - 2; i >= 0; i--) {
            max = Math.max(prices[i], max);
            right[i] = Math.max(right[i + 1], max - prices[i]);
        }

        int profit = 0;
        for (int i = 0; i < prices.length; i++){
            profit = Math.max(left[i] + right[i], profit);  
        }

        return profit;
    }
}

// 方法二
class Solution {
    /**
     * @param k: An integer
     * @param prices: Given an integer array
     * @return: Maximum profit
     */
    private int update(int a, int b, int delta) {
        if (b == Integer.MIN_VALUE) {
            return a;
        }
        
        if (b + delta > a) {
            return b + delta;
        }
        
        return a;
    }  
     
    public int maxProfit(int K, int[] prices) {
        int n = prices.length;
        int i, j, k;
        if (K == 0) {
            return 0;
        }
        
        if (K >= n - 1) {
            j = 0;
            for (i = 1; i < n; ++i) {
                if (prices[i] > prices[i - 1]) {
                    j += prices[i] - prices[i - 1];
                }
            }
            
            return j;
        }
        
        int[][] f = new int[n+1][2*K+1];
        for (i = 0; i <= n; ++i) {
            for (j = 0; j <= 2*K; ++j) {
                f[i][j] = Integer.MIN_VALUE;
            }
        }
        
        f[0][0] = 0;
        for (i = 1; i <= n; ++i) {
            int delta;
            if (i == 1) {
                delta = 0;
            }
            else {
                delta = prices[i-1] - prices[i - 2];
            }
            
            f[i][0] = update(f[i][0], f[i-1][0], 0);
            for (j = 1; j <= 2 * K; j += 2) {
                if (i > 1) f[i][j] = update(f[i][j], f[i-1][j], delta);
                if (i > 1) f[i][j] = update(f[i][j], f[i-1][j-1], delta);
            }
            
            for (j = 2; j <= 2 * K; j += 2) {
                f[i][j] = update(f[i][j], f[i-1][j], 0);
                if (i > 1) f[i][j] = update(f[i][j], f[i-1][j-1], delta);
                if (i > 1) f[i][j] = update(f[i][j], f[i-1][j-2], delta);
            }
        }
        
        int res = Integer.MIN_VALUE;
        for (j = 2; j <= 2 * K; j += 2) {
            res = update(res, f[n][j], 0);
        }
        
        return res;
    }
};