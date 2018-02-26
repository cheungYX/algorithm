# @param {String} s
# @return {Integer}
def longest_palindrome(s)
    return 0 if s.nil? || s.size == 0
    map = {}
    s.chars.map do |v|
        if map.has_key?(v)
            map.delete(v)
        else 
            map[v] = 1
        end
    end
    cnt = map.size
    cnt -= 1 if map.size > 0
    return s.size - cnt
end

