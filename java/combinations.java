class Solution {
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        if(k == 0) {
            return result;
        }
        List<Integer> combine = new ArrayList<>();
        helper(n, combine, 1, k, result);
        return result;
    }
    
    private void helper(int n,
                        List<Integer> combine,
                        int startIndex,
                        int k,
                        List<List<Integer>> result) {
        if(combine.size() == k) {
            result.add(new ArrayList<Integer>(combine));
            return;
        }

        for (int i = startIndex; i <= n; i++) {
            combine.add(i);
            helper(n, combine, i + 1, k, result);
            combine.remove(combine.size() - 1);
        }
    }
}

