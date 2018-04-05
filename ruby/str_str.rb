def strstr(source, target)
  return -1 if source.to_s.empty? || target.to_s.empty?
  for i in 0..source.size - 1
    return i if source[i..i + target.size - 1] == target
  end
  return -1
end
