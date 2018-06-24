public class Solution {    
//方法一:
    int initTargetHash(int []targethash, String Target) {
        int targetnum =0 ;
        for (char ch : Target.toCharArray()) {
            targetnum++;
            targethash[ch]++;
        }
        return targetnum;
    }
    boolean valid(int []sourcehash, int []targethash) {
        
        for(int i = 0; i < 256; i++) {
            if(targethash[i] > sourcehash[i])    
                return false;
        }
        return true;
    }
    public String minWindow(String Source, String Target) {
        // queueing the position that matches the char in T
        int ans = Integer.MAX_VALUE;
        String minStr = "";
        
        int[] sourcehash = new int[256];
        int[] targethash = new int[256];
        
        initTargetHash(targethash, Target);
        int j = 0, i = 0;
        for(i = 0; i < Source.length(); i++) {
            while( !valid(sourcehash, targethash) && j < Source.length()  ) {
                sourcehash[Source.charAt(j)]++;
                j++;
            }
            if(valid(sourcehash, targethash) ){
                if(ans > j - i ) {
                    ans = Math.min(ans, j - i );
                    minStr = Source.substring(i, j );
                }
            }
            sourcehash[Source.charAt(i)]--;
        }
        return minStr;
    }
}


public class Solution {
    /**
     * @param source  : A string
     * @param target: A string
     * @return: A string denote the minimum window, return "" if there is no such a string
     */
    public String minWindow(String source, String target) {
        // write your code here
        int[] cntT = new int[256];
        int numT = 0;
        for (char item : target.toCharArray()) {
            cntT[item]++;
            if (cntT[item] == 1) {
                numT++;
            }
        }

        int numS = 0;
        int ans = Integer.MAX_VALUE;
        String minStr = "";

        int[] cntS = new int[256];
        char[] sc = source.toCharArray();

        for (int l = 0, r = 0; r < source.length(); r++) {
            cntS[sc[r]]++;
            if (cntT[sc[r]] == cntS[sc[r]]) {
                numS++;
            }
            while (numS >= numT) {
                if (ans > r - l + 1) {
                    ans = Math.min(ans, r - l + 1);
                    minStr = source.substring(l, r + 1);
                }
                if (cntT[sc[l]] == cntS[sc[l]]) {
                    numS--;
                }
                cntS[sc[l]]--;
                l++;
            }
        }
        return minStr;
    }
}