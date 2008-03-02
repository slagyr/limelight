
module StartStopButton
  
  def self.extended(block)
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
    @ant = Ant.new(50, 50, page.find("world"), 100) if @ant.nil?
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