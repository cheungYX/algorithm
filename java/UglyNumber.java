public class Solution {
    /**
     * @param num an integer
     * @return true if num is an ugly number or false
     */
    public boolean isUgly(int num) {
        if (num <= 0) return false;  
        if (num == 1) return true;  
          
        while (num >= 2 && num % 2 == 0) num /= 2;  
        while (num >= 3 && num % 3 == 0) num /= 3;  
        while (num >= 5 && num % 5 == 0) num /= 5;  
          
        return num == 1;
    }
}