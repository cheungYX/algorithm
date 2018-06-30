public class Solution {
    /**
     * @param A an integer array
     * @return  A list of integers includes the index of the first number and the index of the last number
     */
    public List<Integer> continuousSubarraySumII(int[] A) {
        // Write your code here
        List<Integer> result = new ArrayList<Integer>();
        result.add(0);
        result.add(0);
        int total = 0;
        int len = A.length;
        int start = 0, end = 0;
        int local = 0;
        int global = -0x7fffffff;
        for (int i = 0; i < len; ++i) {
            total += A[i];
            if (local < 0) {
                local = A[i];
                start = end = i;
            } else {
                local += A[i];
                end = i;
            }
            if (local >= global) {
                global = local;
                result.set(0, start);
                result.set(1, end);
            }
        }
        local = 0;
        start = 0;
        end = -1;
        for (int i = 0; i < len; ++i) {
            if (local > 0) {
                local = A[i];
                start = end = i;
            } else {
                local += A[i];
                end = i;
            }
            if (start == 0 && end == len-1) continue;
            if (total - local >= global) {
                global = total - local;
                result.set(0, (end + 1) % len);
                result.set(1, (start - 1 + len) % len);
            }
        }
        return result;
    }
}