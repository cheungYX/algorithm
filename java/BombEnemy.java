public class Solution {
    /**
     * @param grid Given a 2D grid, each cell is either 'W', 'E' or '0'
     * @return an integer, the maximum enemies you can kill using one bomb
     */
    public int maxKilledEnemies(char[][] grid) {
        // Write your code here
        int m = grid.length;
        int n = m > 0 ? grid[0].length : 0;

        int result = 0, rows = 0;
        int[] cols = new int[n];
        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (j == 0 || grid[i][j-1] == 'W') {
                    rows = 0;
                    for (int k = j; k < n && grid[i][k] != 'W'; ++k)
                        if (grid[i][k] == 'E')
                            rows += 1;
                }
                if (i == 0 || grid[i-1][j] == 'W') {
                    cols[j] = 0;
                    for (int k = i; k < m && grid[k][j] != 'W'; ++k)
                        if (grid[k][j] == 'E')
                            cols[j] += 1;
                }

                if (grid[i][j] == '0' && rows + cols[j] > result)
                    result = rows + cols[j];
            }
        }
        return result;
    }
}

// 方法二
public class Solution {
    /**
     * @param grid Given a 2D grid, each cell is either 'W', 'E' or '0'
     * @return an integer, the maximum enemies you can kill using one bomb
     */
    public int maxKilledEnemies(char[][] A) {
        if (A == null || A.length == 0 || A[0].length == 0) {
            return 0;
        }
        
        int m = A.length;
        int n = A[0].length;
        int[][] up = new int[m][n];
        int[][] down = new int[m][n];
        int[][] left = new int[m][n];
        int[][] right = new int[m][n];
        int i, j, t;
        
        for (i = 0; i < m; ++i) {
            for (j = 0; j < n; ++j) {
                up[i][j] = 0;
                if (A[i][j] != 'W') {
                    if (A[i][j] == 'E') {
                        up[i][j] = 1;
                    }
                    
                    if (i - 1 >= 0) {
                        up[i][j] += up[i-1][j];
                    }
                }
            }
        }
        
        for (i = m - 1; i >= 0; --i) {
            for (j = 0; j < n; ++j) {
                down[i][j] = 0;
                if (A[i][j] != 'W') {
                    if (A[i][j] == 'E') {
                        down[i][j] = 1;
                    }
                    
                    if (i + 1 < m) {
                        down[i][j] += down[i+1][j];
                    }
                }
            }
        }
        
        for (i = 0; i < m; ++i) {
            for (j = 0; j < n; ++j) {
                left[i][j] = 0;
                if (A[i][j] != 'W') {
                    if (A[i][j] == 'E') {
                        left[i][j] = 1;
                    }
                    
                    if (j - 1 >= 0) {
                        left[i][j] += left[i][j-1];
                    }
                }
            }
        }
        
        for (i = 0; i < m; ++i) {
            for (j = n - 1; j >= 0; --j) {
                right[i][j] = 0;
                if (A[i][j] != 'W') {
                    if (A[i][j] == 'E') {
                        right[i][j] = 1;
                    }
                    
                    if (j + 1 < n) {
                        right[i][j] += right[i][j+1];
                    }
                }
            }
        }
        
        int res = 0;
        for (i = 0; i < m; ++i) {
            for (j = 0; j < n; ++j) {
                if (A[i][j] == '0') {
                    t = up[i][j] + down[i][j] + left[i][j] + right[i][j];
                    if (t > res) {
                        res = t;
                    }
                }
            }
        }
        
        return res;
    }
}