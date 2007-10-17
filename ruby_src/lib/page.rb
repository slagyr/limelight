require 'limelight_java'

class Page < JPage
  
  def initialize(options = {})
    super()
    options.each_pair do |key, value|
      if self.respond_to?((key.to_s + "=").to_s)
        self.send((key.to_s + "=").to_s, value.to_s)
      else
        self.getStyle().send((key.to_s + "=").to_s, value.to_s)
      end
    end
  end

  def <<(value)
    add(value)
    return value
  end
  
end