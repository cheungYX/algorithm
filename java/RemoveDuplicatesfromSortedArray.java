class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }
        int i = 0;
        for (int n = 0; n < nums.length; n++) {
            if (nums[n] != nums[i]) {
                nums[++i] = nums[n];
            }
        }
        return i + 1;
    }
}

