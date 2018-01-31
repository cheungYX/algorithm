public class Solution {
    /*
     * @param :  A list of integers
     * @return: A list of unique permutations
     */
    public List<List<Integer>> permuteUnique(int[] nums) {
        // write your code here
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) {
            return result;
        }

        Arrays.sort(nums);
        ArrayList<Integer> permute = new ArrayList<Integer>();
        int[] visited = new int[nums.length];
        for (int i = 0; i < visited.length; i++){
            visited[i] = 0;
        }
        helper(nums, permute, visited, result);
        return result;
    }
    
    private void helper(int[] nums,
                        ArrayList<Integer> permute,
                        int[] visited,
                        List<List<Integer>> result) {
        if(permute.size() == nums.length) {
            result.add(new ArrayList<>(permute));
            return;
        }
        for(int i = 0; i < nums.length; i++) {
            if (visited[i] == 1 || ( i != 0 && nums[i] == nums[i - 1]
            && visited[i-1] == 0)){
                continue;
            }
            visited[i] = 1;
            permute.add(nums[i]);
            helper(nums, permute, visited, result);
            permute.remove(permute.size() - 1);
            visited[i] = 0;
        }        
    }
}

