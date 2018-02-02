public class Solution {
    /*
     * @param nums: a mountain sequence which increase firstly and then decrease
     * @return: then mountain top
     */
    public int mountainSequence(int[] nums) {
        // write your code here
        if(nums == null || nums.length == 0) {
            return -1;
        }
        
        int start = 0;
        int end = nums.length - 1;
        
        while(start + 1 < end) {
            int mid = start + (end - start) / 2;
            if(nums[mid - 1] < nums[mid] ) {
                if(nums[mid] < nums[mid - 1]) {
                    return nums[mid];
                }else{
                    start = mid;
                }
            }else{
                end = mid;
            }
        }
        
        if(nums[start] > nums[end]) {
            return nums[start];
        }else{
            return nums[end];
        }
    }
}

