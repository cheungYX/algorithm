class Solution {
    public int findDuplicate(int[] nums) { 
        if (nums == null || nums.length < 2) {
            return -1;
        }
        
        Arrays.sort(nums);
        int index = 0;
        for (int i = 1; i < nums.length; i++) {
            if(nums[index++] == nums[i]) {
                return nums[i];   
            }
        }
        return -1;
    }
}
