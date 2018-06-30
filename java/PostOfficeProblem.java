public class Solution {
    /**
     * @param A an integer array
     * @param k an integer
     * @return an integer
     */
    int [][]init(int []A)  
    {  
        int n = A.length;
        int [][]dis = new int [n+1][n+1];
        for(int i = 1; i <= n; i++) {  
            for(int j = i+1 ;j <= n;++j)  
            {  
                int mid = (i+j) /2;  
                for(int k = i;k <= j;++k)  
                    dis[i][j] += Math.abs(A[k - 1] - A[mid - 1]);  
            } 
        }
        return dis; 
    } 
    
    public int postOffice(int[] A, int k) {
        // Write your code here
        int n = A.length;
        Arrays.sort(A);

        int [][]dis = init(A);
        int [][]dp = new int[n + 1][k + 1];
        if(n == 0 || k >= A.length)
            return 0;
        int ans = Integer.MAX_VALUE;
        for(int i = 0;i <= n;++i)  {
            dp[i][1] = dis[1][i];

        }
        
        
        for(int nk = 2; nk <= k; nk++) {
            
            for(int i = nk; i <= n; i++) {
                dp[i][nk] = Integer.MAX_VALUE;
                for(int j = 0; j < i; j++) {  
                    if(dp[i][nk] == Integer.MAX_VALUE || dp[i][nk] > dp[j][nk-1] + dis[j+1][i])  
                        dp[i][nk] = dp[j][nk-1] + dis[j+1][i];   
                }  
            }
        }
        return dp[n][k];
    }
}