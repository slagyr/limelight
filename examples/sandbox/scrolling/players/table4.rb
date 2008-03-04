require File.expand_path(File.dirname(__FILE__) + "/cell2")

module Table4
  
  def self.extended(prop)
    prop.cell_index = {}
    10.times do |y|
      row = Limelight::Prop.new(:class_name => "row2", :id => y.to_s)
      row.style.width = "100"
      prop.add(row)
      2.times do |x|
        id = "#{x},#{y}"
        bg_color = ( (x + y) % 2 == 0 ) ? "blue" : "#DDDDDD"
        cell = Limelight::Prop.new(:class_name => "cell2", :id => id, :text => id)
        cell.extend(Cell2)
        cell.style.background_color = bg_color
        row.add(cell)     
      end
    end
  end
  
  attr_accessor :cell_index
  
end