public class Solution {
    /*
     * @param nums: An integer array sorted in ascending order
     * @param target: An integer
     * @return: An integer
     */
    public int lastPosition(int[] nums, int target) {
        // write your code here
        if(nums == null || nums.length == 0) {
            return -1;
        }
        
        int start = 0;
        int len = nums.length;
        int end = len - 1;
        
        while(start + 1 < end) {
            int mid = start + (end - start) / 2;
            if(nums[mid] == target) {
                while(mid != len - 1 && nums[mid] == nums[mid + 1]) {
                    mid+=1;
                }
                return mid;
            }else if(nums[mid] < target) {
                start = mid;
            }else if(target < nums[mid]) {
                end = mid;
            }
        }
        return -1;
    }
}

