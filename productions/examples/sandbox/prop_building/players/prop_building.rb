def add_children
  colors = ["red", "sky_blue", "yellow", "green", "orange", "violet"]
  child_count = find_by_id("child_count").text.to_i
  child_width = find_by_id("child_width").text
  child_height = find_by_id("child_height").text
  find_by_id("container").build do
    child_count.times do |i|
      child :text => (i + 1).to_s, :width => child_width, :height => child_height, :background_color => colors[i % colors.size]
    end
  end
end

def clear_children
  find_by_id("container").remove_all
end

def readd_children
  clear_children
  add_children
end