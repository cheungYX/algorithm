public class Solution {
    /**
     * @param nums an array with positive and negative numbers
     * @param k    an integer
     * @return the maximum average
     */
    public double maxAverage(int[] nums, int k) {
        // Write your code here
        double l = -1e12;
        double r = 1e12;
        double eps = 1e-6;

        while (l + eps < r) {
            double mid = l + (r - l) / 2;

            if (check(nums, mid, k)) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return l;
    }

    boolean check(int nums[], double avg, int k) {
        double[] sum = new double[nums.length + 1];
        double[] min_pre = new double[nums.length + 1];

        for (int i = 1; i <= nums.length; i++) {
            sum[i] = sum[i - 1] + (nums[i - 1] - avg);
            min_pre[i] = Math.min(min_pre[i - 1], sum[i]);

            if (i >= k && sum[i] - min_pre[i - k] >= 0) {
                return true;
            }
        }
        return false;
    }
}