// 方法一
public class Solution {
    /**
     * @param nums an integer array and all positive numbers, no duplicates
     * @param target an integer
     * @return an integer
     */
    public int backPackIV(int[] nums, int target) {
        // Write your code here
        int m = target;
        int []A = nums;
        int f[][] = new int[A.length + 1][m + 1];
        
        f[0][0] = 1;
        for (int i = 1; i <= A.length; i++) {
            for (int j = 0; j <= m; j++) {
                int k = 0; 
                while(k * A[i-1] <= j) {
                    f[i][j] += f[i-1][j-A[i-1]*k];
                    k+=1;
                }
            } // for j
        } // for i    
        return f[A.length][target];
    }
}


// 方法二

public class Solution {
    /**
     * @param nums an integer array and all positive numbers, no duplicates
     * @param target an integer
     * @return an integer
     */
    public int backPackIV(int[] nums, int target) {
        // Write your code here
        int[] f = new int[target + 1];
        f[0] = 1;
        for (int i = 0; i < nums.length; ++i)
            for (int  j = nums[i]; j <= target; ++j)
                f[j] += f[j - nums[i]];

        return f[target];
    }
}