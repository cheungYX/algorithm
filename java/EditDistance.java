public class Solution {
    public int minDistance(String word1, String word2) {
        int n = word1.length();
        int m = word2.length();
        
        int[][] dp = new int[n+1][m+1];
        for(int i=0; i< m+1; i++){
            dp[0][i] = i; 
        }
        for(int i=0; i<n+1; i++){
            dp[i][0] = i;
        }
        
        
        for(int i = 1; i<n+1; i++){
            for(int j=1; j<m+1; j++){
                if(word1.charAt(i-1) == word2.charAt(j-1)){
                    dp[i][j] = dp[i-1][j-1];
                }else{
                    dp[i][j] = 1 + Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1]));
                }
            }
        }
        return dp[n][m];
    }
}

// 动态规划班版本
public class Solution {
    /**
     * @param word1 & word2: Two string.
     * @return: The minimum number of steps.
     */
    public int minDistance(String word1, String word2) {
        char[] s1 = word1.toCharArray();   
        char[] s2 = word2.toCharArray();
        int i, j;
        int m = s1.length;
        int n = s2.length;
        
        int[][] f = new int[m + 1][n + 1];
        
        // commented part is for outputting solution
        // 'I', 'D', 'R', 'S'
        //char[][] pi = new char[m + 1][n + 1];
        for (i = 0; i <= m; ++i) {
            for (j = 0; j <= n; ++j) {
                if (i == 0) {
                    f[i][j] = j;
                    continue;
                }
                
                if (j == 0) {
                    f[i][j] = i;
                    continue;
                }
                
                // i > 0, j > 0
                
                // +1, important!
                    //                       delete        insert         replace
                f[i][j] = Math.min(Math.min(f[i - 1][j], f[i][j - 1]), f[i - 1][j - 1]) + 1;
                /*if (f[i][j] == f[i - 1][j] + 1) {
                    pi[i][j] = 'D';
                }
                else {
                    if (f[i][j] == f[i][j - 1] + 1) {
                        pi[i][j] = 'I';
                    }
                    else {
                        pi[i][j] = 'R';
                    }
                }*/
                
                if (s1[i - 1] == s2[j - 1]) {
                    f[i][j] = Math.min(f[i][j], f[i - 1][j - 1]);
                  //  pi[i][j] = 'S';
                }
            }
        }
        
        /*i = m;
        j = n;
        
        while (i > 0 || j > 0) {
            if (pi[i][j] == 'D') {
                System.out.println("Delete A's " + i + "-th letter from A");
                --i;
                continue;
            }
            
            if (pi[i][j] == 'I') {
                System.out.println("Insert B's " + j + "-th letter of B to A");
                --j;
                continue;
            }
            
            if (pi[i][j] == 'R') {
                System.out.println("Replace the A's " + i + "-th letter to B's " + j + "-th letter");
                --i;
                --j;
                continue;
            }
            
            if (pi[i][j] == 'S') {
                --i;
                --j;
                continue;
            }
        }*/
        
        return f[m][n];
    }
}