public class Solution {
    public static int[] dx = {0, 1, -1, 0};
    public static int[] dy = {1, 0, 0, -1};
    /**
     * @param board: A list of lists of character
     * @param words: A list of string
     * @return: A list of string
     */
    public List<String> wordSearchII(char[][] board, List<String> words) {
        if (board == null || board.length == 0) {
            return new ArrayList<>();
        }
        if (board[0] == null || board[0].length == 0) {
            return new ArrayList<>();
        }
        
        boolean[][] visited = new boolean[board.length][board[0].length];
        Map<String, Boolean> prefixIsWord = getPrefixSet(words);
        Set<String> wordSet = new HashSet<>();
        
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                visited[i][j] = true;
                dfs(board, visited, i, j, String.valueOf(board[i][j]), prefixIsWord, wordSet);
                visited[i][j] = false;
            }
        }
        
        return new ArrayList<String>(wordSet);
    }
    
    private Map<String, Boolean> getPrefixSet(List<String> words) {
        Map<String, Boolean> prefixIsWord = new HashMap<>();
        for (String word : words) {
            for (int i = 0; i < word.length() - 1; i++) {
                String prefix = word.substring(0, i + 1);
                if (!prefixIsWord.containsKey(prefix)) {
                    prefixIsWord.put(prefix, false);
                }
            }
            prefixIsWord.put(word, true);
        }
        
        return prefixIsWord;
    }
    
    private void dfs(char[][] board,
                     boolean[][] visited,
                     int x,
                     int y,
                     String word,
                     Map<String, Boolean> prefixIsWord,
                     Set<String> wordSet) {
        if (!prefixIsWord.containsKey(word)) {
            return;
        }
        
        if (prefixIsWord.get(word)) {
            wordSet.add(word);
        }
        
        for (int i = 0; i < 4; i++) {
            int adjX = x + dx[i];
            int adjY = y + dy[i];
            
            if (!inside(board, adjX, adjY) || visited[adjX][adjY]) {
                continue;
            }
            
            visited[adjX][adjY] = true;
            dfs(board, visited, adjX, adjY, word + board[adjX][adjY], prefixIsWord, wordSet);
            visited[adjX][adjY] = false;
        }
    }
    
    private boolean inside(char[][] board, int x, int y) {
        return x >= 0 && x < board.length && y >= 0 && y < board[0].length;
    }
}