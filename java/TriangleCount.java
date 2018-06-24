public class Solution {
    /**
     * @param S: A list of integers
     * @return: An integer
     */
    public int triangleCount(int S[]) {
        // write your code here
        int left = 0, right = S.length - 1;
        int ans = 0;
        Arrays.sort(S);
        for(int i = 0; i < S.length; i++) {
            left = 0;
            right = i - 1;
            while(left < right) {
                if(S[left] + S[right] > S[i]) {
                    ans = ans + (right - left);
                    right --;
                } else {
                    left ++;
                }
            }
        }
        return ans;
    }
}

// 九章硅谷求职算法集训营版本
public class Solution {
    /**
     * @param S: A list of integers
     * @return: An integer
     */
    public int triangleCount(int A[]) {
        // write your code here
        
        int w, i, j, res = 0;
        Arrays.sort(A);
        int n = A.length;
        for (w = 2; w < n; ++w) {
            j = w - 1;
            for (i = 0; i < w; ++i) {
                while (j >= 0 && A[i] + A[j] > A[w]) {
                    --j;
                }
                
                res += w - Math.max(i + 1, j + 1);
            }
        }
        
        return res;
    }
}