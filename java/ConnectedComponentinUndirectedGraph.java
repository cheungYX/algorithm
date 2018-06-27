// bfs 方法
/**
 * Definition for Undirected graph.
 * class UndirectedGraphNode {
 *     int label;
 *     ArrayList<UndirectedGraphNode> neighbors;
 *     UndirectedGraphNode(int x) { label = x; neighbors = new ArrayList<UndirectedGraphNode>(); }
 * };
 */
public class Solution {
    /**
     * @param nodes a array of Undirected graph node
     * @return a connected set of a Undirected graph
     */
    public List<List<Integer>> connectedSet(List<UndirectedGraphNode> nodes) {
        // Write your code here
        
        Map<UndirectedGraphNode, Boolean> visited = new HashMap<>();
        
       for (UndirectedGraphNode node : nodes){
            visited.put(node, false);
       }
        
        List<List<Integer>> result = new ArrayList<>();
        
        for (UndirectedGraphNode node : nodes){
            if (visited.get(node) == false){
                bfs(node, visited, result);
            }
        }
        
        return result;
    }
    
    public void bfs(UndirectedGraphNode node, Map<UndirectedGraphNode, Boolean> visited, List<List<Integer>> result){
        List<Integer>row = new ArrayList<>();
        Queue<UndirectedGraphNode> queue = new LinkedList<>();
        visited.put(node, true);
        queue.offer(node);
        while (!queue.isEmpty()){
            UndirectedGraphNode u = queue.poll();
            row.add(u.label);    
            for (UndirectedGraphNode v : u.neighbors){
                if (visited.get(v) == false){
                    visited.put(v, true);
                    queue.offer(v);
                }
            }
        }
        Collections.sort(row);
        result.add(row);
        
    }
}

//------------------------我是分割线--------------------------------------
// 并查集方法
public class Solution {
    class UnionFind {
        HashMap<Integer, Integer> father = new HashMap<Integer, Integer>();
        UnionFind(HashSet<Integer> hashSet)
        {
            for (Integer now : hashSet) {
                father.put(now, now);
            }
        }
        int find(int x)
        {
            int parent = father.get(x);
            while (parent != father.get(parent)) {
                parent = father.get(parent);
            }
            return parent;
        }
        int compressed_find(int x)
        {
            int parent = father.get(x);
            while (parent != father.get(parent)) {
                parent = father.get(parent);
            }
            int next;
            while (x != father.get(x)) {
                next = father.get(x);
                father.put(x, parent);
                x = next;
            }
            return parent;
        }

        void union(int x, int y)
        {
            int fa_x = find(x);
            int fa_y = find(y);
            if (fa_x != fa_y)
                father.put(fa_x, fa_y);
        }
    }
    
    List<List<Integer> > print(HashSet<Integer> hashSet, UnionFind uf, int n) {
        List<List<Integer> > ans = new ArrayList<List<Integer> >();
        HashMap<Integer, List<Integer> > hashMap = new HashMap<Integer, List<Integer> >();
        for (int i : hashSet) {
            int fa = uf.find(i);
            if (!hashMap.containsKey(fa)) {
                hashMap.put(fa, new ArrayList<Integer>());
            }
            List<Integer> now = hashMap.get(fa);
            now.add(i);
            hashMap.put(fa, now);
        }
        for (List<Integer> now : hashMap.values()) {
            Collections.sort(now);
            ans.add(now);
        }
        return ans;
    }

public List<List<Integer> > connectedSet(List<UndirectedGraphNode> nodes)
    {


        HashSet<Integer> hashSet = new HashSet<Integer>();
        for (UndirectedGraphNode now : nodes) {
            hashSet.add(now.label);
            for (UndirectedGraphNode neighbour : now.neighbors) {
                hashSet.add(neighbour.label);
            }
        }
        UnionFind uf = new UnionFind(hashSet);

        for (UndirectedGraphNode now : nodes) {

            for (UndirectedGraphNode neighbour : now.neighbors) {
                int fnow = uf.find(now.label);
                int fneighbour = uf.find(neighbour.label);
                if (fnow != fneighbour) {
                    uf.union(now.label, neighbour.label);
                }
            }
        }

        return print(hashSet, uf, nodes.size());
    }
}