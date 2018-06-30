/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
//dp[i][0]表示以i为根的子树不偷根节点能获得的最高价值，dp[i][1]表示以i为根的子树偷根节点能获得的最高价值
public class Solution {
    public int houseRobber3(TreeNode root) {
        int[] ans = dp(root);
        return Math.max(ans[0], ans[1]);
    }
    public int[] dp(TreeNode root) {
        if (root == null) {
            int[] now = new int[]{0, 0};
            return now;
        }
        int[] left = dp(root.left);
        int[] right = dp(root.right);
        int[] now = new int[2];
        now[0] = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        now[1] = left[0] + right[0] + root.val;
        return now;
    }
}

// version 2
/**
 * Definition of TreeNode:
 * public class TreeNode {
 *     public int val;
 *     public TreeNode left, right;
 *     public TreeNode(int x) { val = x; }
 * }
 */
class ResultType {
    public int rob, not_rob;
    public ResultType() { rob = not_rob = 0; }
}

public class Solution {
    /**
     * @param root: The root of binary tree.
     * @return: The maximum amount of money you can rob tonight
     */
    public int houseRobber3(TreeNode root) {
        // write your code here
        ResultType result = visit(root);
        return Math.max(result.rob, result.not_rob);
    }
    
    public ResultType visit(TreeNode root) {
        ResultType result = new ResultType();
        if (root == null)
            return result;
            
        ResultType left_result = visit(root.left);
        ResultType right_result = visit(root.right);
        
        result.rob = root.val + left_result.not_rob + right_result.not_rob;
        result.not_rob = Math.max(left_result.rob, left_result.not_rob) + 
                         Math.max(right_result.rob, right_result.not_rob);
        return result;
    }
}