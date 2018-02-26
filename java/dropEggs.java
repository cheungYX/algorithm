public class Solution {
    /*
     * @param n: An integer
     * @return: The sum of a and b
     */
    public int dropEggs(int n) {
        // write your code here
        long ans = 0;
        for(int i = 1; ; i++) {
            ans += (long)i;
            if(ans >= (long)n) {
                return i;
            }
        }
    }
}

