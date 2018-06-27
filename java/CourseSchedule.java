public class Solution {
    /**
     * @param numCourses a total of n courses
     * @param prerequisites a list of prerequisite pairs
     * @return true if can finish all courses or false
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // Write your code here
        List[] edges = new ArrayList[numCourses];
        int[] degree = new int[numCourses];
        
        for (int i = 0;i < numCourses; i++)
            edges[i] = new ArrayList<Integer>();
            
        for (int i = 0; i < prerequisites.length; i++) {
            degree[prerequisites[i][0]] ++ ;
            edges[prerequisites[i][1]].add(prerequisites[i][0]);
        }

        Queue queue = new LinkedList();
        for(int i = 0; i < degree.length; i++){
            if (degree[i] == 0) {
                queue.add(i);
            }
        }
        
        int count = 0;
        while(!queue.isEmpty()){
            int course = (int)queue.poll();
            count ++;
            int n = edges[course].size();
            for(int i = 0; i < n; i++){
                int pointer = (int)edges[course].get(i);
                degree[pointer]--;
                if (degree[pointer] == 0) {
                    queue.add(pointer);
                }
            }
        }
        
        return count == numCourses;
    }
}