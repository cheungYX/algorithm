public class Solution {
    /*
     * @param nums: an array of integer
     * @param target: An integer
     * @return: An integer
     */
    public int twoSum6(int[] nums, int target) {
        // write your code here
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        Arrays.sort(nums);
        int count = 0;
        int l = 0;
        int len = nums.length - 1;
        int r = len;
        
        while (l < r) {
            if (nums[l] + nums[r] == target) {
               while(l != len && nums[l] == nums[l + 1]) {
                   l++;
               }
               while(r != 0 && nums[r] == nums[r - 1]) {
                   r--;
               }
               count++;
               l++;
               r--;
            } else if (nums[l] + nums[r] < target) {
                l++;
            } else {
                r--;
            }
        }
        
        return count;
    }
}

