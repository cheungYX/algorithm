public class Solution {
    /**
     * @param nums an integer array and all positive numbers
     * @param target an integer
     * @return an integer
     */
    public int backPackV(int[] nums, int target) {
        // Write your code here
        int[] f = new int[target + 1];
        f[0] = 1;
        for (int i = 0; i < nums.length; ++i)
            for (int  j = target; j >= nums[i]; --j)
                f[j] += f[j - nums[i]];

        return f[target];
    }
}