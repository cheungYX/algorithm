public class Solution {
    /**
     * @param n: an integer
     * @param times: an array of integers
     * @return: an integer
     */
    public int copyBooksII(int n, int[] times) {
        int k = times.length;
        int[][] f = new int[2][n+1];
        for (int j = 0 ; j <= n; ++j) {
            f[0][j] = j * times[0];
        }
        for (int i = 1; i < k; ++i) {
            for (int j = 1; j <= n; ++j) {
                int a = i%2;
                int b = 1-a;
                
                f[a][j] = Integer.MAX_VALUE;
                for (int l = 0; l <= j; ++l) {
                    if (f[b][j-l] > times[i] * l) {
                        f[a][j] = Math.min(f[a][j], f[b][j-l]);
                    } else {
                        f[a][j] = Math.min(f[a][j], times[i] * l);
                        break;
                    }
                }
                
            }
        }
        return f[(k-1)%2][n];
    }
    public int copyBooksII2D(int n, int[] times) {
        int k = times.length;
        int[][] f = new int[k][n+1];
        for (int j = 0 ; j <= n; ++j) {
            f[0][j] = j * times[0];
        }
        for (int i = 1; i < k; ++i) {
            for (int j = 1; j <= n; ++j) {
                f[i][j] = Integer.MAX_VALUE;
                for (int l = 0; l <= j; ++l) {
                    f[i][j] = Math.min(f[i][j], Math.max(f[i-1][j-l], times[i] * l));
                    if (f[i-1][j-l] <= times[i] * l) {
                        break;
                    }
                }
                
            }
        }
        return f[k-1][n];
    }
}