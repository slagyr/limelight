#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Players
    
    module CheckBox
      class << self
        def extended(prop)
          check_box = Limelight::UI::Model::Inputs::CheckBoxPanel.new
          prop.panel.add(check_box)
          prop.check_box = check_box.check_box
        end
      end
      
      attr_accessor :check_box
      
      def checked=(value)
        check_box.selected = value
      end
      
      def checked
        return check_box.selected?
      end
      alias :checked? :checked
  
    end
  
  end
end