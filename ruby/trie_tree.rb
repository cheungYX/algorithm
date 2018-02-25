class TrieTree
  def initialize
    @root = TrieNode.new('')
  end

  class TrieNode
    attr_accessor :val, :score, :next, :is_word
    def initialize(val)
      @val = val
      @score = 0
      @is_word = is_word
      @next = {}
    end
  end

  def add_word(keyword, score)
    return if keyword.blank?

    current_node = @root
    for i in 0..keyword.size - 1
      char = keyword.slice(0, i + 1)
      idx = keyword[i].ord
      current_node.next[idx] = TrieNode.new(char) if current_node.next[idx].nil?
      current_node.next[idx].score += score
      current_node = current_node.next[idx]
    end
    current_node.is_word = true
  end

  def suggest(keyword, is_brand = false)
    return [] if keyword.blank?
    res = []

    current_node = @root
    # search keyword olog(keyword.length)
    for i in 0..keyword.size - 1
      idx = keyword[i].ord
      return res if current_node.next[idx].nil?
      current_node = current_node.next[idx]
    end
    # Traversal node
    next_level = current_node.next.sort_by { |r| r[1].score }.reverse
    until next_level.empty?
      node = next_level.shift.second
      if node.is_word
        res << [node.val, node.score]
        return res if res.size > 9
      else
        next if node.next.empty?
        next_level << node.next.sort_by { |r| r[1].score }.reverse.first
        next_level = next_level.sort_by { |r| r[1].score }.reverse
      end
    end
    res
  end
end

