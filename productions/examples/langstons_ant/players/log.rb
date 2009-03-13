#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Log
  
  def self.extended(prop)
  end
  
  def update_counter(steps)
    count.text = steps.to_s
    count.update
  end
  
  def update_location(coords)
    location.text = coords.to_s
    location.update
  end
  
  def count
    @count = scene.find("count") if not @count
    return @count
  end
  
  def location
    @location = scene.find("location") if not @location
    return @location
  end
  
end