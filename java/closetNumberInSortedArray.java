public class Solution {
    /*
     * @param A: an integer array sorted in ascending order
     * @param target: An integer
     * @return: an integer
     */
    public int closestNumber(int[] A, int target) {
        // write your code here
        if(A == null || A.length == 0) {
            return -1;
        }
        int str = 0;
        int end = A.length - 1;
        
        while(str + 1 < end) {
            int mid = str + (end - str) / 2;
            if(A[mid] == target) {
                return mid;
            }else if(A[mid] < target) {
                str = mid;
            }else if(A[mid] > target) {
                end = mid;
            }
        }
        if (target <= A[str]) {
            return str;
        } else if (A[end] <= target) {
            return end;
        } else {
            if (target - A[str] > A[end] - target) {
                return end;
            } else {
                return str;
            }
        }        
    }
}

