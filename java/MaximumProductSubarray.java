public class Solution {
    /**
     * @param nums: an array of integers
     * @return: an integer
     */
    public int maxProduct(List<Integer> nums) {
        int[] max = new int[nums.size()];
        int[] min = new int[nums.size()];
        
        min[0] = max[0] = nums.get(0);
        int result = nums.get(0);
        for (int i = 1; i < nums.size(); i++) {
            min[i] = max[i] = nums.get(i);
            if (nums.get(i) > 0) {
                max[i] = Math.max(max[i], max[i - 1] * nums.get(i));
                min[i] = Math.min(min[i], min[i - 1] * nums.get(i));
            } else if (nums.get(i) < 0) {
                max[i] = Math.max(max[i], min[i - 1] * nums.get(i));
                min[i] = Math.min(min[i], max[i - 1] * nums.get(i));
            }
            
            result = Math.max(result, max[i]);
        }
        
        return result;
    }
}