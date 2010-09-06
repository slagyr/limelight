#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

prop_reader :subject

def apply(style, value)
  begin
    subject.style.send("#{style}=".to_sym, value)
  rescue Exception => e
    puts e
#      puts e.backtrace
  end
end

