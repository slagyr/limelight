module Limelight
  module Composer
    
    class Controller
      
      def initialize(production)
        @production = production
      end
      
      def prop_clicked(prop)
        puts "Prop clicked: #{prop}"
        highlight(prop)
      end
      
      def highlight(prop)
        @highlighted_prop.update unless @highlighted_prop.nil?
        
        @highlighted_prop = prop
        pen = prop.pen
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