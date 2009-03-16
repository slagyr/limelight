#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

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
