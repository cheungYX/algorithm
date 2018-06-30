class BITree {
    public int[] bit;

    public BITree(int range) {
        bit = new int[range + 1];
    }
    
    public void increase(int index, int delta) {
        for (int i = index + 1; i < bit.length; i = i + lowbit(i)) {
            bit[i] += delta;
        }
    }
    
    public int getPrefixSum(int index) {
        int sum = 0;
        for (int i = index + 1; i >= 1; i = i - lowbit(i)) {
            sum += bit[i];
        }
        return sum;
    }
    
    private int lowbit(int x) {
        return x & (-x);
    }
}

public class Solution {
    /**
     * @param A: an integer array
     * @return: A list of integers includes the index of the first number and the index of the last number
     */
    public List<Integer> countOfSmallerNumberII(int[] A) {
        BITree bitree = new BITree(10000);
        List<Integer> results = new ArrayList<>();
        for (int i = 0; i < A.length; i++) {
            results.add(bitree.getPrefixSum(A[i] - 1));
            bitree.increase(A[i], 1); 
        }
        
        return results;
    }
    
}