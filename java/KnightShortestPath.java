public class Solution {
    int n, m; // size of the chessboard
    int[] deltaX = {1, 1, 2, 2, -1, -1, -2, -2};
    int[] deltaY = {2, -2, 1, -1, 2, -2, 1, -1};
    /**
     * @param grid a chessboard included 0 (false) and 1 (true)
     * @param source, destination a point
     * @return the shortest path 
     */
    public int shortestPath(boolean[][] grid, Point source, Point destination) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return -1;
        }
        
        n = grid.length;
        m = grid[0].length;
        
        Queue<Point> queue = new LinkedList<>();
        queue.offer(source);
        
        int steps = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Point point = queue.poll();
                if (point.x == destination.x && point.y == destination.y) {
                    return steps;
                }
                
                for (int direction = 0; direction < 8; direction++) {
                    Point nextPoint = new Point(
                        point.x + deltaX[direction],
                        point.y + deltaY[direction]
                    );
                    
                    if (!inBound(nextPoint, grid)) {
                        continue;
                    }
                    
                    queue.offer(nextPoint);
                    // mark the point not accessible
                    grid[nextPoint.x][nextPoint.y] = true;
                }
            }
            steps++;
        }
        
        return -1;
    }
    
    private boolean inBound(Point point, boolean[][] grid) {
        if (point.x < 0 || point.x >= n) {
            return false;
        }
        if (point.y < 0 || point.y >= m) {
            return false;
        }
        return (grid[point.x][point.y] == false);
    }
}