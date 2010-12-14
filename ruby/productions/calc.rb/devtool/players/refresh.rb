
on_mouse_clicked do
  production.theater.stages.each do |stage|
    if stage.current_scene
      production.producer.open_scene(stage.current_scene.name, stage)
    end
  end
end
