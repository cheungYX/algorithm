/**
 * Definition of TreeNode:
 * public class TreeNode {
 *     public int val;
 *     public TreeNode left, right;
 *     public TreeNode(int val) {
 *         this.val = val;
 *         this.left = this.right = null;
 *     }
 * }
 */


public class Solution {
    /*
     * @param root: the root of binary tree
     * @return: the root of the minimum subtree
     */
    private TreeNode subtree = null;
    private int subtreeSum = Integer.MAX_VALUE;
    
    public TreeNode findSubtree(TreeNode root) {
        // write your code here
        helper(root);
        return subtree;
    }
    
    private int helper(TreeNode root) {
        if(root == null) {
            return 0;
        }
        
        int sum = helper(root.left) + helper(root.right) + root.val;
        if (sum <= subtreeSum) {
            subtreeSum = sum;
            subtree = root;
        }
        return sum;
    } 
}

