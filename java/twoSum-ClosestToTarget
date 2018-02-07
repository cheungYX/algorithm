public class Solution {
    /*
     * @param nums: an integer array
     * @param target: An integer
     * @return: the difference between the sum and the target
     */
    public int twoSumClosest(int[] nums, int target) {
        // write your code here
        if (nums == null || nums.length < 2) {
            return -1;
        }
        Arrays.sort(nums);
        int l = 0;
        int r = nums.length - 1;
        int diff = Integer.MAX_VALUE;
        while (l < r) {
           if (nums[l] + nums[r] < target ) {
               diff = Math.min(diff, target - nums[l] - nums[r]);
               l++;
           } else {
               diff = Math.min(diff, nums[l] + nums[r] - target);
               r--;
           }
        }
        
        return diff;
    }
}

