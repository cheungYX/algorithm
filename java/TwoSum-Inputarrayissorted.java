public class Solution {
    /*
     * @param nums an array of Integer
     * @param target = nums[index1] + nums[index2]
     * @return [index1 + 1, index2 + 1] (index1 < index2)
     */
    public int[] twoSum(int[] nums, int target) {
        if (nums == null || nums.length < 2) {
            return null;
        }
        
        int start = 0, end = nums.length - 1;
        while (start < end) {
            if (nums[start] + nums[end] == target) {
                int[] pair = new int[2];
                pair[0] = start + 1;
                pair[1] = end + 1;
                return pair;
            }
            if (nums[start] + nums[end] < target) {
                start++;
            } else {
                end--;
            }
        }
        
        return null;
    }
}


// 九章硅谷求职算法集训营版本
public class Solution {
    /*
     * @param nums an array of Integer
     * @param target = nums[index1] + nums[index2]
     * @return [index1 + 1, index2 + 1] (index1 < index2)
     */
    public int[] twoSum(int[] A, int T) {
        int[] res = new int[2];
        if (A == null || A.length <= 1) {
            return res;
        }
        
        int n = A.length;
        int i, j = n - 1;
        for (i = 0; i < n; ++i) {
            // i must point to the first number among a bunch of duplicates
            if (i > 0 && A[i] == A[i - 1]) {
                continue;
            }
            
            while (j >= 0 && A[j] > T - A[i]) {
                --j;
            }
            
            if (j < 0 || i >= j) {
                break;
            }
            
            // i < j
            if (A[i] + A[j] == T) {
                res[0] = i + 1;
                res[1] = j + 1;
                return res;
            }
        }
        
        return res;
    }
}