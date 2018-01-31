/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return addNumber(l1, l2, 0);
    }
    
    public static ListNode addNumber(ListNode l1, ListNode l2, int carry) {
        if(l1 == null && l2 == null) {
            return carry == 0? null : new ListNode(carry);
        }
        if(l1 == null && l2 != null) {
            l1 = new ListNode(0);
        }
        if(l2 == null && l1 != null) {
            l2 = new ListNode(0);
        }
        
        int sum = l1.val + l2.val + carry;
        ListNode current = new ListNode(sum % 10);
        carry = sum / 10;
        current.next = addNumber(l1.next, l2.next, carry);
        return current;
    }
}

