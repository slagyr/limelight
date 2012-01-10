def populate!
  text_input = find_by_id("text_input")
  text = text_input.text
  sections = find_by_name("section")
  sections.each do |section|
    switch = section.find_by_name("switch")[0]
    if switch.backstage["check-box"].checked?
      text_props = section.find_by_name("text")
      text_props.each do |p|
        p.text = text
      end
    end
  end
end

on_scene_opened do
  find_by_id("populate_button").on_button_pushed { populate! }
  populate!
end