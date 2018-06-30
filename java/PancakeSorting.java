/**
 * public class FlipTool {
 *   static public void flip(int[] arr, int i) {
 *      ...
 *   }
 * }
 */
public class Solution {
    /**
     * @param array: an integer array
     * @return: nothing
     */
    public void pancakeSort(int[] array) {
        // Write your code here
        int n = array.length;
        while(n > 1){
            for(int i = 0; i < n - 1; i++){
                if(array[0] < array[i + 1]){
                    FlipTool.flip(array, i + 1);
                }
            }
            FlipTool.flip(array, n - 1);
            n--;
        }
    }
}