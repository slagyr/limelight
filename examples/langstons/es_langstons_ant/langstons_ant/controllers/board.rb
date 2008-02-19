module Board
  
  def self.extended(block)
    100.times do |i|
      100.times do |j|
        square = Limelight::Block.new(:class_name => "dot", :id => "dot_#{i}_#{j}")
        block.add(square)
      end
    end
  end
end