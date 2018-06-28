// version 1: Stack
public class Solution {
    /**
     * @param s  an expression includes numbers, letters and brackets
     * @return a string
     */
    public String expressionExpand(String s) {
        Stack<Object> stack = new Stack<>();
        int number = 0;
        
        for (char c : s.toCharArray()) {
            if (Character.isDigit(c)) {
                number = number * 10 + c - '0';
            } else if (c == '[') {
                stack.push(Integer.valueOf(number));
                number = 0;
            } else if (c == ']') {
                String newStr = popStack(stack);
                Integer count = (Integer) stack.pop();
                for (int i = 0; i < count; i++) {
                    stack.push(newStr);
                }
            } else {
                stack.push(String.valueOf(c));
            }
        }
        
        return popStack(stack);
    }
    
    private String popStack(Stack<Object> stack) {
        // pop stack until get a number or empty
        Stack<String> buffer = new Stack<>();
        while (!stack.isEmpty() && (stack.peek() instanceof String)) {
            buffer.push((String) stack.pop());
        }
        
        StringBuilder sb = new StringBuilder();
        while (!buffer.isEmpty()) {
            sb.append(buffer.pop());
        }
        return sb.toString();
    }
}

// version 2: Recursion
public class Solution {
    /**
     * @param s  an expression includes numbers, letters and brackets
     * @return a string
     */
    public String expressionExpand(String s) {
        int number = 0;
        int paren = 0;
        String subString = "";
        StringBuilder sb = new StringBuilder(); 
        
        for (char c : s.toCharArray()) {
            if (c == '[') {
                if (paren > 0) {
                    subString = subString + c;
                }
                paren++;
            } else if (c == ']') {
                paren--;
                if (paren == 0) {
                    // push number * substring to sb
                    String expandedString = expressionExpand(subString);
                    for (int i = 0; i < number; i++) {
                        sb.append(expandedString);
                    }
                    number = 0;
                    subString = "";
                } else {
                    subString = subString + c;
                }
            } else if (c >= '0' && c <= '9') {
                if (paren == 0) {
                    number = number * 10 + c - '0';
                } else {
                    subString = subString + c;
                }
            } else {
                if (paren == 0) {
                    sb.append(String.valueOf(c));
                } else {
                    subString = subString + c;
                }
            }
        }
        
        return sb.toString();
    }
}