public class Solution {
    private int maxSize;
    private Queue<Integer> minheap;
    public Solution(int k) {
        minheap = new PriorityQueue<>();
        maxSize = k;
    }

    public void add(int num) {
        if (minheap.size() < maxSize) {
            minheap.offer(num);
            return;
        }
        
        if (num > minheap.peek()) {
            minheap.poll();
            minheap.offer(num);
        }
    }

    public List<Integer> topk() {
        Iterator it = minheap.iterator();
        List<Integer> result = new ArrayList<Integer>();
        while (it.hasNext()) {
            result.add((Integer) it.next());
        }
        Collections.sort(result, Collections.reverseOrder());
        return result;
    }
}