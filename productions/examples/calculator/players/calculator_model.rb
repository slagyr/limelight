class CalculatorModel
  attr_accessor :display_text
  def button_pressed(button)
    if(button == "=")
      @display_text = eval(@display_text).to_s
    else
      @display_text = display_text + button
    end
  end
end
