public class Solution {
    
    /*
     * @param nums: A set of numbers
     * @return: A list of lists
     */
    public List<List<Integer>> subsets(int[] nums) {
        // write your code here
        List<List<Integer>> res = new ArrayList<>();
        if(nums == null) {
            return res;
        }

        if (nums.length == 0) {
            res.add(new ArrayList<Integer>());
            return res;
        }        
        
        Arrays.sort(nums);
        int star = 0;
        List<Integer> subset = new ArrayList<>();
        Backtraking(subset, nums, star, res);
        return res;
    }
    
    private void Backtraking(List<Integer> subset,
                             int[] nums,
                             int star,
                             List<List<Integer>> res) {
        // deep copy
        res.add(new ArrayList<>(subset));
        for(int i = star; i < nums.length; i++) {
            subset.add(nums[i]);
            Backtraking(subset, nums, i + 1, res);
            subset.remove(subset.size() - 1);
        }
    }
}

