#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Composer
    
    class Controller
      
      def initialize(production)
        @production = production
      end
      
      def prop_selected(prop)
        puts "Prop clicked: #{prop}"
        highlight(prop)
        @production.inspector.inspect_prop(prop)
      end
      
      def value_changed(descriptor, value)
        @highlighted_prop.style.put(descriptor, value)
        @highlighted_prop.update
      end
      
      private #############################################
      
      def highlight(prop)
        if @highlighted_prop
          @highlighted_prop.after_painting(nil)
          @highlighted_prop.update
        end
        @highlighted_prop = prop
        prop.after_painting { |pen| paint_hightlight(prop, pen) }
        prop.update
      end
      
      def paint_hightlight(prop, pen)
        area = prop.area
        
        pen.color = "#B8C6F2DD"
        pen.fill_rectangle(0, 0, area.width, area.height)
        
        pen.color = "#0638CCDD"
        pen.width = 5
        pen.draw_rectangle(0, 0, area.width - 1, area.height - 1)
      end
      
      
    end
    
  end
end