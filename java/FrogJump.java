public class Solution {
    /**
     * @param stones a list of stones' positions in sorted ascending order
     * @return true if the frog is able to cross the river or false
     */
    public boolean canCross(int[] stones) {
        // Write your code here
        HashMap<Integer, HashSet<Integer>> dp =
            new HashMap<Integer, HashSet<Integer>>(stones.length);
        for (int i = 0; i < stones.length; i++) {
            dp.put(stones[i], new HashSet<Integer>() );
        }
        dp.get(0).add(0);

        for (int i = 0; i < stones.length - 1; ++i) {
        	int stone = stones[i];
        	for (int k : dp.get(stone)) {
                // k - 1
                if (k - 1 > 0 && dp.containsKey(stone + k - 1))
                    dp.get(stone + k - 1).add(k - 1);
                // k
                if (dp.containsKey(stone + k))
                    dp.get(stone + k).add(k);
                // k + 1
                if (dp.containsKey(stone + k + 1))
                    dp.get(stone + k + 1).add(k + 1);
        	}
        }
        
        return !dp.get(stones[stones.length - 1]).isEmpty();
    }
}