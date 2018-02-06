public class TwoSum {
    private List<Integer> list = null;
    private Map<Integer, Integer> map = null;
    public TwoSum() {
        list = new ArrayList<Integer>();
        map = new HashMap<Integer, Integer>();
    }
    /*
     * @param number: An integer
     * @return: nothing
     */
    public void add(int number) {
        // write your code here
        if (map.containsKey(number)) {
            map.put(number, map.get(number) + 1);
        } else {
            map.put(number, 1);
            list.add(number);
        }
    }

    /*
     * @param value: An integer
     * @return: Find if there exists any pair of numbers which sum is equal to the value.
     */
    public boolean find(int value) {
        // write your code here
        for (int i = 0; i < list.size(); i++) {
            int num1 = list.get(i), num2 = value - num1;
            if ((num1 == num2 && map.get(num1) > 1) || 
                (num1 != num2 && map.containsKey(num2))) 
                return true;
        }
        return false;        
    }
}

