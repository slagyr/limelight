#- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

$: << File.expand_path(File.dirname(__FILE__) + "/lib")
require 'limelight/composer/controller'


production = Limelight::Production["Stage Composer"]
controller = Limelight::Composer::Controller.new(production)
production.controller = controller
