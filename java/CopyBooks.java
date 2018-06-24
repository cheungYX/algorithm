// version 1: Binary Search
public class Solution {
    /**
     * @param pages: an array of integers
     * @param k:     an integer
     * @return: an integer
     */
    public int copyBooks(int[] pages, int k) {
        int l = 0;
        int r = Integer.MAX_VALUE;

        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (check(pages, mid, k)) {
                r = mid;
            } else {
                l = mid;
            }
        }
        
        if (check(pages, l, k)) {
            return l;
        }
        return r;
    }
    
    private boolean check(int[] pages, int limit, int k) {
        int num = 0;
        int left = 0;
        for (int item : pages) {
            if (item > limit) {
                return false;
            }
            if (item > left) {
                num++;
                left = limit;
            }
            left -= item;
        }
        return num <= k;
    }
}

// version 2: Dynamic Programming
public class Solution {
    /**
     * @param pages: an array of integers
     * @param k: an integer
     * @return: an integer
     */
    public int copyBooks(int[] pages, int k) {
        // write your code here
        if(pages == null){
            return 0;
        }
        int n = pages.length;
        if (n == 0){
            return 0;
        }
           
        if (k > n) {
            k = n;
        }
        int[] sum = new int[n];
        sum[0] = pages[0];
        for (int i = 1; i < n; ++i) {
            sum[i] = sum[i-1] + pages[i];
        }
        int[][] f = new int[n][k];
        for (int i=0; i<n; ++i) f[i][0] = sum[i];
        for (int j=1; j<k; ++j) {
            int p = 0;
            f[0][j] = pages[0];
            for (int i = 1; i < j; ++i) f[i][j] = Math.max(f[i-1][j], pages[i]); 
            for (int i = j; i < n; ++i) {
                while (p < i && f[p][j-1] < sum[i] - sum[p]) ++p;
                f[i][j] = Math.max(f[p][j - 1], sum[i] - sum[p]);                
                if (p > 0) {
                    --p;
                }
                f[i][j] = Math.min(f[i][j], Math.max(f[p][j - 1], sum[i] - sum[p]));         
            }
        }
        return f[n - 1][k - 1];
    }
}

// verison 3 动态规划专题班版本
public class Solution {
    /**
     * @param pages: an array of integers
     * @param k: an integer
     * @return: an integer
     */
    public int copyBooks(int[] A, int K) {
        int n = A.length;
        if (n == 0) {
            return 0;
        }
        
        int i, j, k, sum;
        
        int[][] f = new int[2][n + 1];
        int old, now = 0;
        for (i = 0; i <= n; ++i) {
            f[now][i] = Integer.MAX_VALUE;
        }
        
        f[now][0] = 0;
        for (i = 1; i <= K; ++i) {
            old = now;
            now = 1 - now;
            for (j = 0; j <= n; ++j) {
                f[now][j] = Integer.MAX_VALUE;
                sum = 0;
                for (k = j; k >= 0; --k) {
                    if (f[old][k] < Integer.MAX_VALUE) {
                        f[now][j] = Math.min(f[now][j], Math.max(sum, f[old][k]));
                    }
                    
                    if (k > 0) {
                        sum += A[k - 1];
                    }
                }
            }
        }
        
        return f[now][n];
    }
}

// 记忆化搜索
public class Solution {
    /**
     * @param pages: an array of integers
     * @param k: an integer
     * @return: an integer
     */
    int[][] f;
    int[] A;
    int n, K;
    
    void calc(int k, int i) {
        if (f[k][i] != -1) {
            return;
        }
        
        if (k == 0) {
            f[k][i] = i == 0 ? 0 : Integer.MAX_VALUE;
            return;
        }
        
        if (i == 0) {
            f[k][i] = 0;
            return;
        }
        
        f[k][i] = Integer.MAX_VALUE;
        int s = 0;
        for (int j = i; j >= 0; --j) {
            calc(k - 1, j);
            f[k][i] = Math.min(Math.max(s, f[k - 1][j]), f[k][i]);
            if (j > 0) {
                s += A[j - 1];
            }
        }
    }
    
    public int copyBooks(int[] AA, int KK) {
        A = AA;
        n = A.length;
        K = KK;
        
        if (n == 0) {
            return 0;
        }
        
        if (K > n) {
            K = n;
        }
        
        f = new int[K + 1][n + 1];
        int i, j, k, s;
        for (i = 0; i <= K; ++i) {
            for (j = 0; j <= n; ++j) {
                f[i][j] = -1;
            }
        }
        
        calc(K, n);
        return f[K][n];
    }
}