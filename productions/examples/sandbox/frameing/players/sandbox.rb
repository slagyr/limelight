module Sandbox

  prop_reader :subject

  def apply(style, value)
    begin
      subject.style.send("#{style}=".to_sym, value)      
    rescue Exception => e
      puts e
#      puts e.backtrace
    end
  end

end