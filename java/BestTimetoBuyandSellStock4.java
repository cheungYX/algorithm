// 动态规划版本 Version 1
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
class Solution {
    /**
     * @param k: An integer
     * @param prices: Given an integer array
     * @return: Maximum profit
     */
    public int maxProfit(int k, int[] prices) {
        // write your code here
        if (k == 0) {
            return 0;
        }
        if (k >= prices.length / 2) {
            int profit = 0;
            for (int i = 1; i < prices.length; i++) {
                if (prices[i] > prices[i - 1]) {
                    profit += prices[i] - prices[i - 1];
                }
            }
            return profit;
        }
        int n = prices.length;
        int[][] mustsell = new int[n + 1][n + 1];   // mustSell[i][j] 表示前i天，至多进行j次交易，第i天必须sell的最大获益
        int[][] globalbest = new int[n + 1][n + 1];  // globalbest[i][j] 表示前i天，至多进行j次交易，第i天可以不sell的最大获益
        
        mustsell[0][0] = globalbest[0][0] = 0;
        for (int i = 1; i <= k; i++) {
            mustsell[0][i] = globalbest[0][i] = 0;
        }
        
        for (int i = 1; i < n; i++) {
            int gainorlose = prices[i] - prices[i - 1];
            mustsell[i][0] = 0;
            for (int j = 1; j <= k; j++) {
                mustsell[i][j] = Math.max(globalbest[(i - 1)][j - 1] + gainorlose,
                                            mustsell[(i - 1)][j] + gainorlose);
                globalbest[i][j] = Math.max(globalbest[(i - 1)][j], mustsell[i ][j]);
            }
        }
        return globalbest[(n - 1)][k];
    }
};


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