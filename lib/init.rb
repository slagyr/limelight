#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.



require 'java'
if File.exists?(File.dirname(__FILE__) + "/limelight.jar")
  require File.expand_path(File.dirname(__FILE__) + "/limelight.jar")
end

require File.expand_path(File.dirname(__FILE__) + "/limelight/limelight_init")

