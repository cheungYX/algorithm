public class Solution {
    /**
     * @param nums an array of integers
     * @param k an integer
     * @return the largest sum
     */
    public int maxSubarray4(int[] nums, int k) {
        // Write your code here
        int n = nums.length;
        if (n < k)
            return 0;

        int result = 0;
        for (int i = 0; i < k; ++i)
            result += nums[i];

        int[] sum = new int[n + 1];
        sum[0] = 0;
        
        int min_prefix = 0;
        for (int i = 1; i <= n; ++i) {
            sum[i] = sum[i - 1] + nums[i - 1];
            if (i >= k && sum[i] - min_prefix > result) {
                result = Math.max(result, sum[i] - min_prefix);
            }
            if (i >= k) {
                min_prefix = Math.min(min_prefix, sum[i - k + 1]);
            }
        }
        return result;
    }
}