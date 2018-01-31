class Solution {
    public int strStr(String source, String target) {
        if (source == null || target == null || target.length() > source.length()) {
            return -1;
        }
        if (target.length() == 0) {
            return 0;
        }
        for (int i = 0; i <= source.length() - target.length(); i++) {
            if(source.substring(i, i + target.length()).equals(target)) {
                return i;
            }
        }
        return -1;
    }
}
