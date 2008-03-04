description_text = <<END
The Rules:
If the ant is on a black dot, paint it white and go right.
If the ant is on a white dot, paint it black and go left.
END

__ :class_name => "earth"
title :text => "Langston's Ant"
description :text => description_text
world :id => "world"
console {
  start_stop_button :text => "Start", :players => "button"
  log :id => "log" do
    moves_label :text => "Moves:"
    moves_counter :id => "count", :text => "# of moves"
    location_label :text => "Location:"
    location :id => "location", :text => "???"
  end
}