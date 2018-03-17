public class ConnectingGraph3 {
    private int[] father = null;
    private int count;

    private int find(int x) {
        if (father[x] == x) {
            return x;
        }
        return father[x] = find(father[x]);
    }
    /*
    * @param n: An integer
    */public ConnectingGraph3(int n) {
        // do intialization if necessary
        father = new int[n + 1];
        count = n;
        for (int i = 1; i <= n; ++i) {
            father[i] = i;
        }
    }

    /*
     * @param a: An integer
     * @param b: An integer
     * @return: nothing
     */
    public void connect(int a, int b) {
        // write your code here
        int root_a = find(a);
        int root_b = find(b);
        if (root_a != root_b) {
            father[root_a] = root_b;
            count --;
        }
    }

    /*
     * @return: An integer
     */
    public int query() {
        // write your code here
        return count;
    }
}

