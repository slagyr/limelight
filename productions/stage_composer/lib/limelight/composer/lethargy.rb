#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Composer
    
    module Lethargy
      
      def self.null_event(*args)
        args.each do |sym|
          define_method(sym) { |event| } # do nothing
        end
      end
      
      null_event *Limelight::Prop::EVENTS
      
      def mouse_clicked(e)
        scene.production.controller.prop_selected(self)
      end
      
    end
    
  end
end