require File.expand_path(File.dirname(__FILE__) + "/cell2")

module Table2
  
  def self.extended(block)
    block.cell_index = {}
    10.times do |y|
      row = Limelight::Block.new(:class_name => "row2", :id => y.to_s)
      block.add(row)
      10.times do |x|
        id = "#{x},#{y}"
        bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
        cell = Limelight::Block.new(:class_name => "cell2", :id => id, :text => id)
        cell.extend(Cell2)
        cell.style.background_color = bg_color
        row.add(cell)     
      end
    end
    block.stylize
    # block.update
  end
  
  attr_accessor :cell_index
  
end