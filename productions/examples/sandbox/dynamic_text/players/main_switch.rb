on_value_changed do |e|
  is_on = backstage["check-box"].checked?
  scene.find_by_name("switch").each do |switch|
    switch.backstage["check-box"].checked = is_on
  end
end