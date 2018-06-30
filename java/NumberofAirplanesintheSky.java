/**
 * Definition of Interval:
 * public classs Interval {
 *     int flag, end;
 *     Interval(int flag, int end) {
 *         this.flag = flag;
 *         this.end = end;
 *     }
 */
class Point{
    int time;
    int flag;

    Point(int t, int s){
      this.time = t;
      this.flag = s;
    }
    public static Comparator<Point> PointComparator  = new Comparator<Point>(){
      public int compare(Point p1, Point p2){
        if(p1.time == p2.time) return p1.flag - p2.flag;
        else return p1.time - p2.time;
      }
    };
}
  
class Solution {
    /**
     * @param intervals: An interval array
     * @return: Count of airplanes are in the sky.
     */
  public int countOfAirplanes(List<Interval> airplanes) { 
    List<Point> list = new ArrayList<>(airplanes.size()*2);
    for(Interval i : airplanes){
      list.add(new Point(i.start, 1));
      list.add(new Point(i.end, 0));
    }

    Collections.sort(list,Point.PointComparator );
    int count = 0, ans = 0;
    for(Point p : list){
      if(p.flag == 1) count++;
      else count--;
      ans = Math.max(ans, count);
    }

    return ans;
  }
    
}

//硅谷求职算法班版
class Point {
    int time;
    int flag;
    
    Point(int t, int f) {
        time = t;
        flag = f;
    }
}

public class Solution {
    /**
     * @param airplanes: An interval array
     * @return: Count of airplanes are in the sky.
     */
    public int countOfAirplanes(List<Interval> airplanes) {
        // write your code here
        Point[] list = new Point[airplanes.size() * 2];
        int n = 0;
        for (Interval item : airplanes) {
            list[n++] = new Point(item.start, 1);
            list[n++] = new Point(item.end, -1);
        }
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (!cmp(list[i], list[j])) {
                    Point tmp = list[i];
                    list[i] = list[j];
                    list[j] = tmp;
                }
            }
        }
        
        int cnt = 0, ans = 0; 
        for (Point item : list) {
            cnt += item.flag;
            ans = Math.max(ans, cnt);
        }
        
        return ans;
    }
    
    private boolean cmp(Point a, Point b) {
        if (a.time < b.time) {
            return true;
        }
        if (a.time == b.time && a.flag < b.flag) {
            return true;
        }
        return false;
    }
}