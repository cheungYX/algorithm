// bfs version
public class Solution {
    /**
     * @param root the root of binary tree
     * @return a lists of linked list
     */
    public List<ListNode> binaryTreeToLists(TreeNode root) {
        List<ListNode> result = new ArrayList<ListNode>();
        
        if (root == null)
            return result;
            
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        
        ListNode dummy = new ListNode(0);
        ListNode lastNode = null;
        while (!queue.isEmpty()) {
            dummy.next = null;
            lastNode = dummy;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode head = queue.poll();
                lastNode.next = new ListNode(head.val);
                lastNode = lastNode.next;

                if (head.left != null)
                    queue.offer(head.left);
                if (head.right != null)
                    queue.offer(head.right);
            }
            result.add(dummy.next);
        }
        
        return result;
    }
}

// dfs
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
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
public class Solution {
    /**
     * @param root the root of binary tree
     * @return a lists of linked list
     */
    public List<ListNode> binaryTreeToLists(TreeNode root) {
        // Write your code here
        List<ListNode> result = new ArrayList<ListNode>();
        dfs(root, 1, result);
        return result;
    }

    void dfs(TreeNode root, int depth, List<ListNode> result) {
        if (root == null)
            return;
        ListNode node = new ListNode(root.val);
        if (result.size() < depth) {
            result.add(node);
        }
        else {
            node.next = result.get(depth-1);
            result.set(depth-1, node);
        }
        dfs(root.right, depth + 1, result);
        dfs(root.left, depth + 1, result);
    }
}