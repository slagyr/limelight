backdrop do
  %w{ red orange yellow green blue magenta }.each do |color|
    sample :background_color => color, :text => color
  end
  selection :id => "the_selection"
end