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

        def image
          return image_panel.image_file
        end

        def rotation=(value)
          image_panel.rotation = value.to_f
        end

        def rotation
          return image_panel.rotation
        end

      end

    end
  end
end
