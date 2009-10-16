module Alert

  prop_reader :message

  def scene_opened(e)
    if message.area.height == message.style.max_height.to_i
      message.style.vertical_scrollbar = :on
    end
  end

end