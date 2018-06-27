// 方法一 
public class Solution {
    /**
     * @param grid a 2D grid
     * @return an integer
     */
    public int shortestDistance(int[][] grid) {
        // Write your code here
        int n = grid.length;
        if (n == 0)
            return -1;

        int m = grid[0].length;
        if (m == 0)
            return -1;

        List<Integer> sumx = new ArrayList<Integer>();
        List<Integer> sumy = new ArrayList<Integer>();
        List<Integer> x = new ArrayList<Integer>();
        List<Integer> y = new ArrayList<Integer>();

        int result = Integer.MAX_VALUE;
        for (int i = 0; i < n; ++i)
            for (int j = 0; j < m; ++j)
                if (grid[i][j] == 1) {
                    x.add(i);
                    y.add(j);
                }
        
        Collections.sort(x);
        Collections.sort(y);

        int total = x.size();

        sumx.add(0);
        sumy.add(0);
        for (int i = 1; i <= total; ++i) {
            sumx.add(sumx.get(i-1) + x.get(i-1));
            sumy.add(sumy.get(i-1) + y.get(i-1));
        }

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < m; ++j)
                if (grid[i][j] == 0) {
                    int cost_x = get_cost(x, sumx, i, total);
                    int cost_y = get_cost(y, sumy, j, total);
                    if (cost_x + cost_y < result)
                        result = cost_x + cost_y;
                }

        return result;
    }

    public int get_cost(List<Integer> x, List<Integer> sum, int pos, int n) {
        if (n == 0)
            return 0;
        if (x.get(0) > pos)
            return sum.get(n) - pos * n;

        int l = 0, r = n - 1;
        while (l + 1 < r) {
            int mid = l + (r - l) / 2;
            if (x.get(mid) <= pos)
                l = mid;
            else
                r = mid - 1;
        }
        
        int index = 0;
        if (x.get(r) <= pos)
            index = r;
        else
            index = l;
        return sum.get(n) - sum.get(index + 1) - pos * (n - index - 1) + 
               (index + 1) * pos - sum.get(index + 1);
    }
}

// 方法二
public class Solution {
    /**
     * @param grid a 2D grid
     * @return an integer
     */
    public int shortestDistance(int[][] grid) {
        // Write your code here
        int row = grid.length, column = grid[0].length;
        if(row == 0 || column == 0 || !haveZero(grid,row,column)) {
            return -1;
        }

        int[] rowSum = new int[row];
        int[] columnSum = new int[column]; 
        for(int i = 0; i < row; i++)
        	for(int j = 0; j < column; j++)
        		if(grid[i][j] == 1) {
        			rowSum[i]++;
        			columnSum[j]++;
        		}

        int[] ansRow = new int[row];
        int[] ansColumn = new int[column];
        getSumDistance(rowSum,row,ansRow);
        getSumDistance(columnSum,column,ansColumn);

        int ans = Integer.MAX_VALUE;
        for(int i = 0; i < row; i++)
        	for(int j = 0; j < column; j++)
        		if(grid[i][j] == 0 && ans > ansRow[i] + ansColumn[j]) {
        			ans = ansRow[i] + ansColumn[j];
        		}
        return ans;
    }

    void getSumDistance(int[] a,int n,int[] ans) {
    	int[] prefixSum1 = new int[n];
    	int[] prefixSum2 = new int[n];
    	/*
    	第一阶段，处理前缀。
    	prefixSum1记录数组 a 的前缀和，即:prefixSum1[i]=a[0]+a[1]+..+a[i].
    	prefixSum2记录数组 prefixSum1 前缀和，prefixSum2即为前 i 个点到第 i 个点的代价和。
    	*/
    	prefixSum1[0] = a[0];
    	for(int i = 1; i < n; i++) {
    		prefixSum1[i] = prefixSum1[i - 1] + a[i];
    	}
    	prefixSum2[0] = 0;
    	for(int i = 1; i < n; i++) {
    		prefixSum2[i] = prefixSum2[i - 1] + prefixSum1[i - 1];
     	}

     	for(int i = 0; i < n; i++) {
     		ans[i] = prefixSum2[i];
     	}

    	/*
    	第二阶段，处理后缀。
    	prefixSum1记录数组 a 的后缀和，即:prefixSum1[i]=a[n-1]+a[n-2]+..+a[i].
    	prefixSum2记录数组 prefixSum1 的后缀和，prefixSum2即为 i 之后的点到第 i 个点的代价和。
    	*/
    	prefixSum1[n - 1] = a[n - 1];
    	for(int i = n - 2; i >= 0; i--) {
    		prefixSum1[i] = prefixSum1[i + 1] + a[i];
    	}
    	prefixSum2[n - 1] =0;
    	for(int i = n - 2; i >= 0; i--) {
    		prefixSum2[i] = prefixSum2[i + 1] + prefixSum1[i + 1];
     	}

     	for(int i = 0; i < n; i++) {
     		ans[i] += prefixSum2[i];
     	}

     	/*
     	ans[i] 即为a数组中所有点到第 i 点的代价和
     	*/
    }

    boolean haveZero(int[][] grid, int row, int column) {
    	for(int i = 0; i < row; i++) {
    		for(int j = 0; j < column; j++){
    			if(grid[i][j] == 0) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
}