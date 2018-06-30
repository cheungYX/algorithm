class Solution {
    public int maxProfit(int[] prices, int fee) {
        int sell=0;
        int own=-prices[0];
        for (int i = 1; i < prices.length; i++){
            sell = Math.max(sell, own + prices[i] - fee);
            own = Math.max(own, sell - prices[i]);
        }
        return sell;        
    }
}