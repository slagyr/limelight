
def populate!
  text_input = find_by_id("text_input")
  text = text_input.text
  text_props = find_by_name("text")
  text_props.each do |p|
    p.text = text
  end
end

on_scene_opened do
  find_by_id("populate_button").on_button_pushed { populate! }
  populate!
end