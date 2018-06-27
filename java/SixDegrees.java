/**
 * Definition for Undirected graph.
 * class UndirectedGraphNode {
 *     int label;
 *     List<UndirectedGraphNode> neighbors;
 *     UndirectedGraphNode(int x) { 
 *         label = x;
 *         neighbors = new ArrayList<UndirectedGraphNode>(); 
 *     }
 * };
 */
public class Solution {
    /**
     * @param graph a list of Undirected graph node
     * @param s, t two Undirected graph nodes
     * @return an integer
     */
    public int sixDegrees(List<UndirectedGraphNode> graph,
                          UndirectedGraphNode s,
                          UndirectedGraphNode t) {
        // Write your code here
        if (s == t)
            return 0;

        Map<UndirectedGraphNode, Integer> visited = new HashMap<UndirectedGraphNode, Integer>();
        Queue<UndirectedGraphNode> queue = new LinkedList<UndirectedGraphNode>();
        
        queue.offer(s);
        visited.put(s, 0);
        while (!queue.isEmpty()) {
            UndirectedGraphNode node = queue.poll();
            int step = visited.get(node);
            for (int i = 0; i < node.neighbors.size(); i++) {
                if (visited.containsKey(node.neighbors.get(i))) {
                    continue;
                }
                visited.put(node.neighbors.get(i), step + 1);
                queue.offer(node.neighbors.get(i));
                if (node.neighbors.get(i) == t) {
                    return step + 1;
                }
            }
        }
        
        return -1;
    }
}