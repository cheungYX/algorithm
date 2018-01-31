public class Solution {
    /*
     * @param candidates: A list of integers
     * @param target: An integer
     * @return: A list of lists of integers
     */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        // write your code here
        List<List<Integer>> result = new ArrayList<>();
        if(candidates == null || candidates.length == 0) {
            return result;
        }
        removeDuplicate(candidates);
        dfs(candidates, new ArrayList<Integer>(), target, 0, result);
        return result;
    }
    
    private int[] removeDuplicate(int[] candidates) {
        Arrays.sort(candidates);
        int index = 0;
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i] != candidates[index]) {
                candidates[++index] = candidates[i];
            }
        }
        
        int[] nums = new int[index + 1];
        for (int i = 0; i < index + 1; i++) {
            nums[i] = candidates[i];
        }
        
        return nums;
    }
    
    private void dfs(int[] candidates,
                     ArrayList<Integer> combination,
                     int remainTarget,
                     int startIndex,
                     List<List<Integer>> result) {
        if (remainTarget == 0) {
            result.add(new ArrayList<>(combination));
        }
        for(int i = startIndex; i < candidates.length; i++) {
            if(candidates[i] > remainTarget) {
                break;
            }
            combination.add(candidates[i]);
            dfs(candidates, combination, remainTarget - candidates[i], i, result);
            combination.remove(combination.size() - 1);
        }
    }
}

