func twoSum(nums []int, target int) []int {
	// 方法1: 暴力破解 o(n2)
    for i := range nums {
        for j := i + 1; j < len(nums); j++ {
            if nums[i] + nums[j] == target {
                return []int{i, j}
            }
        }
    }
    return nil
}

func twoSum2(nums []int, target int) []int {
	// 方法2: 哈希表 用空间换时间
    m := map[int]int{}
    
    for i, v := range nums {
    	// 不能直接取 m[v] != nil
        if idx, ok := m[v]; ok {
            return []int{idx, i}
        }
        m[target - v] = i
    }
    
    return nil
}