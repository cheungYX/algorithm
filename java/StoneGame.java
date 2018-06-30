//记忆化
public class Solution {
    /**
     * @param A an integer array
     * @return an integer
     */
    int search(int l, int r, int[][] f, int[][] visit, int[][] sum) {
        
        if(visit[l][r] == 1)
            return f[l][r];
        if(l == r) {
            visit[l][r] = 1;
            return f[l][r];
        }
        
        f[l][r] = Integer.MAX_VALUE;
        for (int k = l; k < r; k++) {
            f[l][r] = Math.min(f[l][r], search(l, k, f, visit, sum) + search(k + 1, r, f, visit, sum) + sum[l][r]);
        }
        visit[l][r] = 1;
        return f[l][r];
    }
    
    public int stoneGame(int[] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        
        int n = A.length;
        
        // initialize
        int[][] f = new int[n][n];
        int[][] visit = new int[n][n];
        
        for (int i = 0; i < n; i++) {
            f[i][i] = 0;
        }
        
        // preparation
        int[][] sum = new int[n][n];
        for (int i = 0; i < n; i++) {
            sum[i][i] = A[i];
            for (int j = i + 1; j < n; j++) {
                sum[i][j] = sum[i][j - 1] + A[j];
            }
        }
        
        return search(0, n-1, f, visit, sum);
        
    }
}


// for 循环
public class Solution {
    /**
     * @param A an integer array
     * @return an integer
     */
    public int stoneGame(int[] A) {
        // Write your code here
        if(A.length==0) {
            return 0;
        }
        int[][] dp=new int[A.length][A.length];
        int[] sums=new int[A.length+1];
        sums[0]=0;
        for(int i=0;i<A.length;i++){
            for(int j=i;j<A.length;j++){
                dp[i][j]=Integer.MAX_VALUE;
            }
        }
        for(int i=0;i<A.length;i++){
            dp[i][i]=0;
            sums[i+1]=sums[i]+A[i];
        }
        
        return search(0,A.length-1,dp,sums);
    }
    
    private int search(int start, int end, int[][] dp, int[] sums){
        if(dp[start][end]!=Integer.MAX_VALUE){
            return dp[start][end];
        }
        int min=Integer.MAX_VALUE;
        for(int k=start;k<end;k++){
            int left = search(start,k,dp,sums);
            int right = search(k+1,end,dp,sums);
            int now = sums[end+1]-sums[start];
            min=Math.min(min,left+right+now);
        }
        dp[start][end]=min;
        return min;
    }
}