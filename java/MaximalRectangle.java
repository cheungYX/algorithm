public class Solution {
    /**
     * @param matrix a boolean 2D matrix
     * @return an integer
     */
    public int maximalRectangle(boolean[][] matrix) {
        // Write your code here
        int m = matrix.length;
        int n = m == 0 ? 0 : matrix[0].length;
        int[][] height = new int[m][n + 1];
 
        int maxArea = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (! matrix[i][j]) {
                    height[i][j] = 0;
                } else {
                    height[i][j] = i == 0 ? 1 : height[i - 1][j] + 1;
                }
            }
        }
 
        for (int i = 0; i < m; i++) {
            int area = maxAreaInHist(height[i]);
            if (area > maxArea) {
                maxArea = area;
            }
        }
 
        return maxArea;
    }
 
    private int maxAreaInHist(int[] height) {
        Stack<Integer> stack = new Stack<Integer>();
    
        int i = 0;
        int max = 0;
     
        while (i < height.length) {
            if (stack.isEmpty() || height[stack.peek()] <= height[i]) {
                stack.push(i++);
            } else {
                int t = stack.pop();
                max = Math.max(max, height[t]
                        * (stack.isEmpty() ? i : i - stack.peek() - 1));
            }
        }
        return max;
    }
}

// 动态规划专题班版本
public class Solution {
    /**
     * @param matrix a boolean 2D matrix
     * @return an integer
     */
    public int maximalRectangle(boolean[][] A) {
        // A is boolean
        // when calculating left and right, check A[i-1][j] is true
        if (A==null||A.length==0||A[0].length==0) {
            return 0;
        }
        int m = A.length;
        int n = A[0].length;
        int[][] up = new int[m][n];
        int[][] left = new int[m][n];
        int[][] right = new int[m][n];
        int i, j, k, l, r, res = 0;
        
        for (i=0; i<m; ++i) {
            // calc up
            for (j=0; j<n; ++j) {
                if (!A[i][j]) {
                    up[i][j] = 0;
                }
                else {
                    up[i][j] = 1;
                    if (i>0) {
                        up[i][j] += up[i-1][j];
                    }
                }
            }
            
            // calc left
            l = 0;
            for (j=0; j<n; ++j) {
                if (!A[i][j]) {
                    l = left[i][j] = 0;
                }
                else {
                    ++l;
                    left[i][j] = l;
                    if (i>0&&A[i-1][j]&&left[i-1][j] < left[i][j]) {
                        left[i][j] = left[i-1][j];
                    }
                }
            }
            
            // calc right
            r=0;
            for (j=n-1; j>=0; --j) {
                if (!A[i][j]) {
                    r = right[i][j] = 0;
                }
                else {
                    ++r;
                    right[i][j] = r;
                    if (i>0&&A[i-1][j]&&right[i-1][j] < right[i][j]) {
                        right[i][j] = right[i-1][j];
                    }
                }
            }
        }
        
        for (i=0; i<m; ++i) {
            for (j=0; j<n; ++j) {
                res = Math.max(res, up[i][j] * (left[i][j] + right[i][j] - 1));
            }
        }
        
        return res;
    }
}