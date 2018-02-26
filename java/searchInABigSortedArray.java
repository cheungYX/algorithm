public class Solution {
    /*
     * @param reader: An instance of ArrayReader.
     * @param target: An integer
     * @return: An integer which is the first index of target.
     */
    public int searchBigSortedArray(ArrayReader reader, int target) {
        // write your code here
        int index = 1;
        while(reader.get(index - 1) < target) {
            index = index * 2;
        }
        
        int start = 0;
        int end = index;
        
        while(start + 1 < end) {
            int mid = start + (end - start) / 2;
            if(reader.get(mid) < target) {
                start = mid;
            }else{
                end = mid;
            }
        }
        
        if(reader.get(start) == target) {
            return start;
        }
        
        if(reader.get(end) == target) {
            return end;
        }
        
        return -1;
    }
}

