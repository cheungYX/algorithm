# Definition for singly-linked list.
# class ListNode
#     attr_accessor :val, :next
#     def initialize(val)
#         @val = val
#         @next = nil
#     end
# end

# @param {ListNode} l1
# @param {ListNode} l2
# @return {ListNode}
def merge_two_lists(l1, l2)
    return l2 if l1.nil?
    return l1 if l2.nil?
    
    head = ListNode.new(-1)
    tmp = ListNode.new(-1)
    
    head = tmp
    
    while (l1 != nil && l2 != nil)
        if l1.val <= l2.val
            tmp.next = l1
            l1 = l1.next
        else
            tmp.next = l2
            l2 = l2.next
        end   
            tmp = tmp.next
    end

    tmp.next = l1 if l1 != nil
    tmp.next = l2 if l2 != nil
    head.next
end

