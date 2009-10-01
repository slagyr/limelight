#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Limelight
  module Builtin
    module Players

      module Image

        class << self

          def extended(prop) #:nodoc:
            image_panel = Limelight::UI::Model::ImagePanel.new
            prop.panel.add(image_panel)
            prop.image_panel = image_panel
          end
          
        end

        attr_accessor :image_panel #:nodoc:
        
        def image=(path)
          image_panel.image_file = path
        end

        def image_data=(data_hash)
          image_panel.setImageData(data_hash[:format], data_hash[:data])
        end

        def image
          return image_panel.image_file
        end

        def rotation=(value)
          image_panel.rotation = value.to_f
        end

        def rotation
          return image_panel.rotation
        end

        def scaled=(value)
          image_panel.scaled = value
        end

        def scaled?
          return image_panel.scaled
        end

      end

    end
  end
end
