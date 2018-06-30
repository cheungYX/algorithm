public class Solution {
    /**
     * @param nums: A list of integers.
     * @return: the median of numbers
     */
    private PriorityQueue<Integer> maxHeap, minHeap;
    private int numOfElements = 0;

    public int[] medianII(int[] nums) {
        // write your code here
        Comparator<Integer> revCmp = new Comparator<Integer>() {
            @Override
            public int compare(Integer left, Integer right) {
                return right.compareTo(left);
            }
        };
        int cnt = nums.length;
        maxHeap = new PriorityQueue<Integer>(cnt, revCmp);
        minHeap = new PriorityQueue<Integer>(cnt);
        int[] ans = new int[cnt];
        for (int i = 0; i < cnt; ++i) {
            addNumber(nums[i]);
            ans[i] = getMedian();
        }
        return ans;
    }
    void addNumber(int value) {
        maxHeap.add(value);
        if (numOfElements%2 == 0) {
            if (minHeap.isEmpty()) {
                numOfElements++;
                return;
            }
            else if (maxHeap.peek() > minHeap.peek()) {
                Integer maxHeapRoot = maxHeap.poll();
                Integer minHeapRoot = minHeap.poll();
                maxHeap.add(minHeapRoot);
                minHeap.add(maxHeapRoot);
            }
        }
        else {
            minHeap.add(maxHeap.poll());
        }
        numOfElements++;
    }
    int getMedian() {
        return maxHeap.peek();
    }
}