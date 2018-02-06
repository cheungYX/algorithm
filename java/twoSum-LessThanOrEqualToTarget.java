public class Solution {
    /*
     * @param nums: an array of integer
     * @param target: an integer
     * @return: an integer
     */
    public int twoSum5(int[] nums, int target) {
        // write your code here
        if  (nums == null || nums.length == 0) {
            return 0;
        }
        int l = 0;
        int r = nums.length - 1;
        int count = 0;
        Arrays.sort(nums);
        
        while (l < r) {
            if (nums[l] + nums[r] <= target) {
                count += r - l;
                l++;
            } else {
                r--;
            }
        }
        
        return count;
    }
}

