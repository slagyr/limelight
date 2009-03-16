#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module StartStopButton
  
  def self.extended(prop)
  end
  
  def mouse_clicked(e)
    if self.text == "Start"
      self.text = "Stop"
      start
    else
      stop
      self.text = "Start"
    end
  end
  
  def start
    world = scene.find("world")
    world.draw_grid
    @ant = Ant.new(50, 50, world, 100) if @ant.nil?
    @thread = Thread.new do
      begin
        @ant.walk
      rescue Exception => e
        puts e
        puts e.backtrace
      end
    end
  end
  
  def stop
    @ant.stop
  end
  
end