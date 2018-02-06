public class Solution {
    /*
     * @param nums: an array of integers
     * @return: the number of unique integers
     */
    public int deduplication(int[] nums) {
        // write your code here
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        HashMap<Integer, Integer> map = new HashMap<>();
        int index = 0;
        for (int i = 0; i < nums.length; i++) {
            if (map.get(nums[i]) != null) {
                continue;
            }
            nums[index++] = nums[i];
            map.put(nums[i], 1);
        }
        
        return index;
     }
}

