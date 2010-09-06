#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

on_cast do
  image_panel = Limelight::UI::Model::ImagePanel.new
  panel.add(image_panel)
  @image_panel = image_panel
end

attr_accessor :image_panel #:nodoc:

def image=(path)
  image_panel.image_file = path
end

def image_data=(data_hash)
  image_panel.setImageData(data_hash[:data])
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
