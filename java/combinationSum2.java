public class Solution {
    /*
     * @param num: Given the candidate numbers
     * @param target: Given the target number
     * @return: All the combinations that sum to target
     */
    public List<List<Integer>> combinationSum2(int[] num, int target) {
        // write your code here
        List<List<Integer>> result = new ArrayList<>();
        if (num == null || num.length == 0) {
            return result;
        }
        
        Arrays.sort(num);
        List<Integer> combination = new ArrayList<Integer>();
        helper(num, combination, 0, target, result);
        return result;
    }
    
    private void helper(int[] num,
                        List<Integer> combination,
                        int startIndex,
                        int target,
                        List<List<Integer>> result) {
        if (target == 0) {
            result.add(new ArrayList<>(combination));
            return;
        }
        for (int i = startIndex; i < num.length; i++) {
            if (i != startIndex && num[i] == num[i - 1]) {
                continue;
            }
            if (num[i] > target) {
                break;
            }
            combination.add(num[i]);
            helper(num, combination, i + 1, target - num[i], result);
            combination.remove(combination.size() - 1);
        }
    }
}

