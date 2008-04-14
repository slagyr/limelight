$: << File.expand_path(File.dirname(__FILE__) + "/lib")
require 'limelight/composer/controller'


production = Limelight::Production["Stage Composer"]
controller = Limelight::Composer::Controller.new(production)
production.controller = controller
