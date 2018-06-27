// 参考程序1
public class Solution {
    /**
     * @param nums an integer array
     * @param low an integer
     * @param high an integer
     * @return nothing
     */
    public void partition2(int[] nums, int low, int high) {
        // Write your code here
        if (nums == null || nums.length <= 1) {
            return;
        }
        
        int pl = 0, pr = nums.length - 1;
        int i = 0;
        while (i <= pr) {
            if (nums[i] < low) {
                swap(nums, pl, i);
                pl++;
                i++;
            } else if (nums[i] > high) {
                swap(nums, pr, i);
                pr--;
            } else {
                i ++;
            }
        }
    }
    
    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}

// 参考程序2
public class Solution {
    /**
     * @param nums an integer array
     * @param low an integer
     * @param high an integer
     * @return nothing
     */
    public void partition2(int[] nums, int low, int high) {
        // Write your code here
        int left = 0;
        int right = nums.length - 1;

        // 首先把区间分为 < low 和 >= low 的两个部分 
        while(left <= right) {
            while(left <= right && nums[left] < low) {
                left ++;
            }
            while(left <= right && nums[right] >= low) {
                right --;
            }

            if(left <= right) {
                int tmp = nums[left];
                nums[left] = nums[right];
                nums[right] = tmp;
                left ++;
                right --;
            }
        }

        // 然后从 >= low 的部分里分出 <= high 和 > high 的两个部分
        right = nums.length - 1;
        while(left <= right) {
            while(left <= right && nums[left] <= high) {
                left ++;
            }
            while(left <= right && nums[right] > high) {
                right --;
            }
            if(left <= right) {
                int tmp = nums[left];
                nums[left] = nums[right];
                nums[right] = tmp;
                left ++;
                right --;
            }
        }
    }
}