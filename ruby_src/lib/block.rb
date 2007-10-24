require 'limelight_java'

class Block < JBlock
  
  attr_accessor :onclick
  
  def mouseClicked()
    puts @onclick
    puts "book: #{book}"
    puts "book.class: #{book.class}"
    eval(@onclick) if @onclick
  end
  
end