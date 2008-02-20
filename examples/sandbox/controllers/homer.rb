module Homer
  
  def mouseEntered
    100.times do |i|
      style.transparency = (100 - i).to_s
      update_now
    end
  end
  
  def mouseExited
    100.times do |i|
      style.transparency = i.to_s
      update_now
    end
  end
  
end