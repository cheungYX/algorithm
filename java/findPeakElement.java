public class Solution {
    /*
     * @param A: An integers array.
     * @return: return any of peek positions.
     */
    public int findPeak(int[] A) {
        // write your code here
        int[] nums = A;
        int start = 1;
        int end = nums.length - 2;
        
        while(start + 1 < end) {
            int mid = start + (end - start) / 2;
            if(nums[mid] < nums[mid - 1]) {
                end = mid;
            } else if(nums[mid] < nums[mid + 1]) {
                start = mid;
            } else {
                end = mid;
            }
        }
        if(nums[start] < nums[end]) {
            return end;
        } else { 
            return start;
        }
    }
}

