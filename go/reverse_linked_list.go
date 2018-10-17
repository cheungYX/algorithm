/**
 * Definition for singly-linked list.
 * type ListNode struct {
 *     Val int
 *     Next *ListNode
 * }
 */


func reverseList(head *ListNode) *ListNode {
    var pre *ListNode

    for {
        if head == nil {
            break
        }
        tmp := head
        head = head.Next
        tmp.Next = pre
        pre = tmp
    }
    return pre
}
