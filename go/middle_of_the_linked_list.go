/**
 * Definition for singly-linked list.
 * type ListNode struct {
 *     Val int
 *     Next *ListNode
 * }
 */
func middleNode(head *ListNode) *ListNode {
    if head.Next == nil {
        return head
    }
    
    faster := head
    slow := head

    for {
        if faster == nil || faster.Next == nil {
            break
        }
        slow = slow.Next
        faster = faster.Next.Next
    }
    return slow
}
