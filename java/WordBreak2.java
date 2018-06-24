public class Solution {
    public ArrayList<String> wordBreak(String s, Set<String> dict) {
        // Note: The Solution object is instantiated only once and is reused by each test case.
        Map<String, ArrayList<String>> memo = new HashMap<String, ArrayList<String>>();
        return wordBreakHelper(s, dict, memo);
    }

    public ArrayList<String> wordBreakHelper(String s,
                                             Set<String> dict,
                                             Map<String, ArrayList<String>> memo){
        if (memo.containsKey(s)) {
            return memo.get(s);
        }
        
        ArrayList<String> results = new ArrayList<String>();
        
        if (s.length() == 0) {
            return results;
        }
        
        if (dict.contains(s)) {
            results.add(s);
        }
        
        for (int len = 1; len < s.length(); ++len){
            String word = s.substring(0, len);
            if (!dict.contains(word)) {
                continue;
            }
            
            String suffix = s.substring(len);
            ArrayList<String> segmentations = wordBreakHelper(suffix, dict, memo);
            
            for (String segmentation: segmentations){
                results.add(word + " " + segmentation);
            }
        }
        
        memo.put(s, results);
        return results;
    }
}