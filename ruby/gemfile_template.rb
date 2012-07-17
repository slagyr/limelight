Gem::Specification.new do |s|
  s.name        = "limelight"
  s.version     = "<%= version %>"
  s.platform    = "java"
  s.authors     = ["Micah Martin"]
  s.email       = %w(micah@8thlight.com)
  s.homepage    = "http://github.com/slagyr/limelight"
  s.summary     = "UI Framework for Ruby"
  s.description = "Build rich client applications using nothing but ruby."

  s.required_rubygems_version = ">= 1.3.6"
  s.rubyforge_project         = "limelight"

  s.add_development_dependency "rspec"

  s.files        = Dir.glob("{bin,ruby,productions}/**/*") + %w(LICENSE)
  s.executables  = %w(limelight)
  s.require_path = 'lib'
end