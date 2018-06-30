public class TwoSum {
    /*
     * @param number: An integer
     * @return: nothing
     */
    ArrayList<Integer> nums = new ArrayList<>();
    
    public void add(int number) {
        // write your code here
        nums.add(number);
    }

    /*
     * @param value: An integer
     * @return: Find if there exists any pair of numbers which sum is equal to the value.
     */
    public boolean find(int value) {
        // write your code here
        Collections.sort(nums);
        for (int i = 0, j = nums.size() - 1; i < j;) {
            if (nums.get(i) + nums.get(j) == value) {
                return true;
            } else if (nums.get(i) + nums.get(j) < value) {
                i++;
            } else {
                j--;
            }
        }
        return false;
    }
}