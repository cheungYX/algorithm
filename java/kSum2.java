public class Solution {
    /**
     * @param A: an integer array.
     * @param k: a positive integer (k <= length(A))
     * @param target: a integer
     * @return an integer
     */    
    List<List<Integer> > ans;
    public void dfs(int A[], int K, int target, int index, List<Integer> tans)
    {

        if(K == 0 && target == 0) {
            ans.add(new ArrayList<Integer>(tans));
            return ;
        }
        if(K < 0 || target < 0 || index < 0)
            return ;
        dfs(A, K, target, index - 1, tans);
        tans.add(A[index]);
        dfs(A, K  - 1, target - A[index], index - 1, tans);
        tans.remove(tans.size() - 1);
        
    }
    
    public List<List<Integer>> kSumII(int A[], int K, int target) {
        ans = new ArrayList<List<Integer>>();
        List<Integer> tans = new ArrayList<Integer>();
        dfs(A, K, target, A.length - 1, tans);
        return ans;
    }
}