public class Solution {
    /**
     * @param envelopes a number of envelopes with widths and heights
     * @return the maximum number of envelopes
     */
    public int maxEnvelopes(int[][] envelopes) {
        // Write your code here
        if(envelopes == null || envelopes.length == 0 
            || envelopes[0] == null || envelopes[0].length != 2)
            return 0;
        Arrays.sort(envelopes, new Comparator<int[]>(){
            public int compare(int[] arr1, int[] arr2){
                if(arr1[0] == arr2[0])
                    return arr2[1] - arr1[1];
                else
                    return arr1[0] - arr2[0];
            } 
        });
        int dp[] = new int[envelopes.length];
        int len = 0;
        for(int[] envelope : envelopes){
            int index = Arrays.binarySearch(dp, 0, len, envelope[1]);
                if(index < 0)
                    index = -index - 1;
            dp[index] = envelope[1];
            if (index == len)
                len++;
        }
        return len;
    }
}


// O(n^2) 九章动态规划专题班版本
// 去掉所有/* */注释可以打印路径
public class Solution {
    /**
     * @param envelopes a number of envelopes with widths and heights
     * @return the maximum number of envelopes
     */
                         // int[n][2]
    public int maxEnvelopes(int[][] A) {
        if (A == null || A.length == 0) {
            return 0;
        }
        
        int n = A.length;
        Arrays.sort(A, new Comparator<int[]>() {
            // compare two envelopes
            // (a[0], a[1])
            // (b[0], b[1])
           public int compare(int[] a, int[] b) {
               if (a[0] != b[0]) { // length
                   return a[0] - b[0];
               }
               else { // width
                   return a[1] - b[1];
               }
           }
        });
        
        int[] f = new int[n];
        /*int[] pi = new int[n];*/
        int i, j;
        for (i = 0; i < n; ++i) {
            f[i] = 1;
            /*pi[i] = -1;*/
            for (j = 0; j < i; ++j) {
                if (A[j][0] < A[i][0] && A[j][1] < A[i][1]) {
                    f[i] = Math.max(f[i], f[j] + 1);
                    /*if (f[j] + 1 == f[i]) {
                        pi[i] = j;
                    }*/
                }
            }
        }
        
        int res = 0;
        /*j = 0;*/
        for (i = 0; i < n; ++i) {
            res = Math.max(res, f[i]);
            /*if (res == f[i]) {
                j = i;
            }*/
        }
        
        // outmost: j
        /*while (j != -1) {
            System.out.println("[" + A[j][0] + ", " + A[j][1] + "]");
            j = pi[j];
        }*/
        
        return res;
    }
}