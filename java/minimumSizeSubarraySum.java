class Solution {
    public int minSubArrayLen(int s, int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int j = 0;
        int prefixSum = 0;
        int result = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            while (j < nums.length && prefixSum < s) {
                prefixSum += nums[j];
                j++;
            }
            if (prefixSum >= s) {
                result = Math.min(result, j - i);
            }
            prefixSum -= nums[i];
        }
        if (result == Integer.MAX_VALUE) {
            result = 0;
        }
        return result;
    }
}

