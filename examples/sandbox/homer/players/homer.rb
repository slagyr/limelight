module Homer
  
  def mouse_entered(e)
    100.times do |i|
      style.transparency = (100 - i).to_s
      update_now
    end
  end
  
  def mouse_exited(e)
    100.times do |i|
      style.transparency = i.to_s
      update_now
    end
  end
  
end