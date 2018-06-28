import java.util.Iterator;

public class NestedIterator implements Iterator<Integer> {

    private Stack<NestedInteger> stack;
    
    private void pushListToStack(List<NestedInteger> nestedList) {
        Stack<NestedInteger> temp = new Stack<>();
        for (NestedInteger nested : nestedList) {
            temp.push(nested);
        }
        
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
    }
    
    public NestedIterator(List<NestedInteger> nestedList) {
        stack = new Stack<>();
        pushListToStack(nestedList);
    }

    // @return {int} the next element in the iteration
    @Override
    public Integer next() {
        if (!hasNext()) {
            return null;
        }
        return stack.pop().getInteger();
    }

    // @return {boolean} true if the iteration has more element or false
    @Override
    public boolean hasNext() {
        while (!stack.isEmpty() && !stack.peek().isInteger()) {
            pushListToStack(stack.pop().getList());
        }
        
        return !stack.isEmpty();
    }
    
    @Override
    public void remove() {}
}