#- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

def floaters
  @floaters = parent.find_by_name("floater") if @floaters.nil?
  return @floaters
end

on_mouse_moved do |e|
#  puts "mouse_moved!"
#  puts "e: #{e}"
  begin
    x = e.x
    y = e.y
    floaters.each { |floater| floater.get_away_from(x, y) }
  rescue Exception => e
    puts e
  end
end

on_mouse_clicked do
  puts "You missed"
end
