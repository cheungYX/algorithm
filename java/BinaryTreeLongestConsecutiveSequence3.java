class ResultType {
    public int max_len, max_down, max_up;
    ResultType(int len, int down, int up) {
        max_len = len;
        max_down = down;
        max_up = up;    
    }
}

public class Solution {
    /**
     * @param root the root of k-ary tree
     * @return the length of the longest consecutive sequence path
     */
    public int longestConsecutive3(MultiTreeNode root) {
        // Write your code here
        return helper(root).max_len;
    }
    
    ResultType helper(MultiTreeNode root) {
        if (root == null) {
            return new ResultType(0, 0, 0);
        }

        int down = 0, up = 0, max_len = 1;
        for (MultiTreeNode node : root.children) {
            ResultType type = helper(node);
            if (node.val + 1 == root.val)
                down = Math.max(down, type.max_down + 1);
            if (node.val - 1 == root.val)
                up = Math.max(up, type.max_up + 1);
            max_len = Math.max(max_len, type.max_len);
        }

        max_len = Math.max(down + 1 + up, max_len);
        return new ResultType(max_len, down, up);
    }
}