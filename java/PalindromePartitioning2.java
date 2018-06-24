public class Solution {
    /**
     * @param s a string
     * @return an integer
     */
    private boolean[][] CalcPalin(String s, int n) {
        boolean[][] isPalin = new boolean[n][n];
        int i, j, p;
        for (i = 0; i < n; ++i) {
            for (j = 0; j < n; ++j) {
                isPalin[i][j] = false;
            }
        }
        
        for (p = 0; p < n; ++p) {
            i = j = p;
            while (i >= 0 && j < n && s.charAt(i) == s.charAt(j)) {
                isPalin[i][j] = true;
                --i;
                ++j;
            }
        }
        
        for (p = 0; p < n-1; ++p) {
            i = p;
            j = p + 1;
            while (i >= 0 && j < n && s.charAt(i) == s.charAt(j)) {
                isPalin[i][j] = true;
                --i;
                ++j;
            }
        }

        return isPalin;
    }
    public int minCut(String s) {
        int n = s.length();
        if (n == 0) {
            return 0;
        }
        
        int[] f = new int[n+1];
        //int[] pi = new int[n + 1];
        int i, j, p;
        boolean[][] isPalin = CalcPalin(s, n);
                
        f[0] = 0;
        for (i=1; i<=n; ++i) {
            f[i] = Integer.MAX_VALUE;
            for (j = 0; j < i; ++j) {
                if (isPalin[j][i-1] && f[j] != Integer.MAX_VALUE && f[j] + 1 < f[i]) {
                    f[i] = f[j] + 1;
                    //pi[i] = j;
                }
            }
        }
        
        // print solution
        /*i = n;
        while (i != 0) {
            // pi[i]~i-1
            for (j = pi[i]; j < i; ++j) {
                System.out.print(s[j]);
            }
            
            System.out.println("");
            i = pi[i];
        }*/
        
        return f[n] - 1;
    }
};