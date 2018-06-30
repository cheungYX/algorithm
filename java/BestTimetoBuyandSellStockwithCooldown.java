int maxProfit(vector<int>& prices) {
        int sold = 0, rest = 0, hold = INT_MIN;
        for (const int p : prices) {
            int prev_sold = sold;
            sold = hold + p;
            hold = max(hold, rest - p);
            rest = max(rest, prev_sold);
        }
        return max(rest, sold);
}