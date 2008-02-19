module Grid
   
  def self.extended(element)    
     element.page.find("status").text = "Not Started..."      
                                                                                                   
     100.times do |y|
       100.times do |x|
         element.add(Limelight::Block.new( :class_name => "square", :id => "#{x}_#{y}"))
       end                                                         
     end              
     element.update                
  end
  
  
end