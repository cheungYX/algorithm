def reverse_vowels(s)
    vowel = ['a', 'e', 'i', 'o', 'u', 'A', 'E', 'I', 'O', 'U']
    l = 0
    r = s.size - 1
    while l < r
        if vowel.include?(s[l])
            if vowel.include?(s[r])
                swap(s, l, r)
                l += 1
                r -= 1
            else
                r -= 1
            end
        else
            l += 1
        end
    end
    s
end
    
def swap(s, l, r)
    tmp = s[l]
    s[l] = s[r]
    s[r] = tmp
end

