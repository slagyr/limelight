module Production

  def default_scene
    nil
  end

  def process_incompatible_version_response(response)
    @incompatible_version_response = response
    @incompatible_version_stage.close
    @main_thread.run
  end

  def proceed_with_incompatible_version?(production_name, required_version)
    @incompatible_version_response = nil
    @main_thread = Thread.current
    load_incompatible_version_scene(production_name, required_version)
    while @incompatible_version_response == nil
      Thread.stop
    end
    return @incompatible_version_response
  end

  def load_incompatible_version_scene(production_name, required_version)
    if !theater["Incompatible Version"]
      @incompatible_version_stage = theater.add_stage("Incompatible Version", :location => [:center, :center], :size => [400, 300],
            :background_color => :transparent, :framed => false, :always_on_top => true, :vital => false)
    end
    producer.open_scene("incompatible_version", @incompatible_version_stage, :instance_variables => { :production_name => production_name, :required_version => required_version })
  end

end