public class Solution {
    public int climbStairs(int n) {
        if (n <= 1) {
            return n;
        }
        int last = 1, lastlast = 1;
        int now = 0;
        for (int i = 2; i <= n; i++) {
            now = last + lastlast;
            lastlast = last;
            last = now;
        }
        return now;
    }
}

// v2 记忆化搜索
public class Solution {
    /**
     * @param n: An integer
     * @return: An integer
     */
    int[] result = null;

    void f(int X) {
         if (result[X] != -1) return;                                                 
         if (X == 0 || X == 1) {
            result[X] = 1;
            return;
         }
         
         f(X - 1);
         f(X - 2);
         result[X] = result[X - 1] + result[X - 2];
    }

    public int climbStairs(int n) {
        if (n == 0) {
            return 0;
        }
        
        result  = new int[n + 1];
        for (int i = 0; i <= n; ++i) {
            result[i] = -1;
        }
        
        f(n);
        return result[n];
    }
}