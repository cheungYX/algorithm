import java.util.NavigableSet;

public class TopK {
    private Map<String, Integer> words = null;
    private NavigableSet<String> topk = null;
    private int k;

    private Comparator<String> myComparator = new Comparator<String>() {
        public int compare(String left, String right) {
            if (left.equals(right))
                return 0;

            int left_count = words.get(left);
            int right_count = words.get(right);
            if (left_count != right_count) {
                return right_count - left_count;
            }
            return left.compareTo(right);
        }
    };

    public TopK(int k) {
        // initialize your data structure here
        this.k = k;
        words = new HashMap<String, Integer>();
        topk = new TreeSet<String>(myComparator);
    }

    public void add(String word) {
        // Write your code here
        if (words.containsKey(word)) {
            if (topk.contains(word))
                topk.remove(word);
            words.put(word, words.get(word) + 1);
        } else {
            words.put(word, 1);
        }

        topk.add(word);
        if (topk.size() > k) {
            topk.pollLast();
        }
    }

    public List<String> topk() {
        // Write your code here
        List<String> results = new ArrayList<String>();
        Iterator it = topk.iterator();
        while(it.hasNext()) {
             String str = (String)it.next();
             results.add(str);
        }
        return results;
    }
}