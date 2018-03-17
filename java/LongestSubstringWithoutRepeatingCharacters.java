public class Solution {
    /**
     * @param s: a string
     * @return: an integer
     */
    public int lengthOfLongestSubstring(String s) {
        // write your code here
        int[] map = new int[256];
        int i, j = 0;
        int res = 0;
        for (i = 0; i < s.length(); i++) {
            while (j < s.length() && map[s.charAt(j)] == 0) {
                map[s.charAt(j)] = 1;
                res = Math.max(res, j-i + 1);
                j++;
            }
            map[s.charAt(i)] = 0;
        }
        return res;
    }
}

