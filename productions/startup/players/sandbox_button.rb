module SandboxButton

  def mouse_clicked(e)
    sandbox_path = File.join(Java::limelight.Main.LIMELIGHT_HOME, "productions", "examples", "sandbox")
    scene.open_production(sandbox_path)
  end

end