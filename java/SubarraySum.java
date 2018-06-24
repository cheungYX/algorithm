public class Solution {
    /**
     * @param nums: A list of integers
     * @return: A list of integers includes the index of the first number
     *          and the index of the last number
     */
    public ArrayList<Integer> subarraySum(int[] nums) {
        // write your code here
       
        int len = nums.length;
       
        ArrayList<Integer> ans = new ArrayList<Integer>();
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
       
        map.put(0, -1);
       
        int sum = 0;
        for (int i = 0; i < len; i++) {
            sum += nums[i];
           
            if (map.containsKey(sum)) {
                ans.add(map.get(sum) + 1);
                ans.add(i);
                return ans;
            }
            
            map.put(sum, i);
        }
       
        return ans;
    }
}