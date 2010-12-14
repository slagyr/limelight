lcd :text => "", :id => "lcd"
%w{1 2 3 + 4 5 6 - 7 8 9 * c 0 = /}.each do |label|
  calc_button :text => label
end