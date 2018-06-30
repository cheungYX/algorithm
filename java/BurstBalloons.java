public class Solution {
    /*
     * @param nums: A list of integer
     * @return: An integer, maximum coins
     */
    public int maxCoins(int[] nums) {
        if(nums == null || nums.length == 0) return 0;
        int n = nums.length;
        //开数组到n+2是为了保证k-1 k+1不溢出
        int[][] dp = new int[n+2][n+2];
        for(int i=1;i<=n;i++){
            int left = i-2 >= 0?nums[i-2]:1;
            int right = i < n?nums[i]:1;
            dp[i][i] = left*nums[i-1]*right;
        }
        for(int len = 2;len<=n;len++){
            for(int i=1;i+len-1<=n;i++){
                int j = i+len-1;
                int left = i-2 >= 0?nums[i-2]:1;
                int right = j < n?nums[j]:1;
                dp[i][j] = Integer.MIN_VALUE;
                for(int k=i;k<=j;k++){
                    dp[i][j] = Math.max(dp[i][j], dp[i][k-1]+dp[k+1][j]+left*right*nums[k-1]);
                }
            }
        }
        return dp[1][n];
    }
}