public class Solution {
     class TrieNode {
        List<String> startWith;
        TrieNode[] children;

        TrieNode() {
            startWith = new ArrayList<>();
            children = new TrieNode[26];
        }
    }

    class Trie {
        TrieNode root;

        Trie(String[] words) {
            root = new TrieNode();
            for (String w : words) {
                TrieNode cur = root;
                for (char ch : w.toCharArray()) {
                    int idx = ch - 'a';
                    if (cur.children[idx] == null)
                        cur.children[idx] = new TrieNode();
                    cur.children[idx].startWith.add(w);
                    cur = cur.children[idx];
                }
            }
        }

        List<String> findByPrefix(String prefix) {
            List<String> ans = new ArrayList<>();
            TrieNode cur = root;
            for (char ch : prefix.toCharArray()) {
                int idx = ch - 'a';
                if (cur.children[idx] == null)
                    return ans;

                cur = cur.children[idx];
            }
            ans.addAll(cur.startWith);
            return ans;
        }
    }

    public List<List<String>> wordSquares(String[] words) {
        List<List<String>> ans = new ArrayList<>();
        if (words == null || words.length == 0)
            return ans;
        int len = words[0].length();
        Trie trie = new Trie(words);
        List<String> ansBuilder = new ArrayList<>();
        for (String w : words) {
            ansBuilder.add(w);
            search(len, trie, ans, ansBuilder);
            ansBuilder.remove(ansBuilder.size() - 1);
        }

        return ans;
    }

    private void search(int len, Trie tr, List<List<String>> ans,
            List<String> ansBuilder) {
        if (ansBuilder.size() == len) {
            ans.add(new ArrayList<>(ansBuilder));
            return;
        }

        int idx = ansBuilder.size();
        StringBuilder prefixBuilder = new StringBuilder();
        for (String s : ansBuilder)
            prefixBuilder.append(s.charAt(idx));
        List<String> startWith = tr.findByPrefix(prefixBuilder.toString());
        for (String sw : startWith) {
            ansBuilder.add(sw);
            search(len, tr, ans, ansBuilder);
            ansBuilder.remove(ansBuilder.size() - 1);
        }
    }
}

// version: 高频题班
public class Solution {
    /**
     * @param words a set of words without duplicates
     * @return all word squares
     */

    void initPrefix(String[] words, Map<String, List<String>> hash) {
        for (String item : words) {
            hash.putIfAbsent("", new ArrayList<>());
            hash.get("").add(item);

            String prefix = "";
            for (char c : item.toCharArray()) {
                prefix += c;
                hash.putIfAbsent(prefix, new ArrayList<>());
                hash.get(prefix).add(item);
            }
        }
    }

    boolean checkPrefix(int l, String nextWord, int wordLen, Map<String, List<String>> hash, List<String> squares) {
        for (int j = l + 1; j < wordLen; j++) {
            String prefix = "";
            for (int k = 0; k < l; k++) {
                prefix += squares.get(k).charAt(j);
            }
            prefix += nextWord.charAt(j);
            if (!hash.containsKey(prefix)) {
                return false;
            }
        }
        return true;
    }

    void dfs(int l, int wordLen, Map<String, List<String>> hash, List<String> squares, List<List<String>> ans) {
        if (l == wordLen) {
            ans.add(new ArrayList<>(squares));
            return;
        }
        String prefix = "";
        for (int i = 0; i < l; i++) {
            prefix += squares.get(i).charAt(l);
        }

        for (String item : hash.get(prefix)) {
            if (!checkPrefix(l, item, wordLen, hash, squares)) {
                continue;
            }
            squares.add(item);
            dfs(l + 1, wordLen, hash, squares, ans);
            squares.remove(squares.size() - 1);
        }
    }

    public List<List<String>> wordSquares(String[] words) {
        // Write your code here
        List<List<String>> ans = new ArrayList<>();
        if (words.length == 0) {
            return ans;
        }
        Map<String, List<String>> hash = new HashMap<>();
        initPrefix(words, hash);

        List<String> squares = new ArrayList<>();
        dfs(0, words[0].length(), hash, squares, ans);
        return ans;
    }
}