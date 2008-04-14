module StyleValue
  
  attr_accessor :descriptor
  
  def populate(style)
    self.text = style.get(@descriptor)
  end
  
  def focus_lost(e)
    production.controller.value_changed(@descriptor, text)
  end
  
end