def is_palindrome(s)
    return true if s.to_s.empty?
    s = s.downcase.scan(/\w/)
    return s == s.reverse
end

