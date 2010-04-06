#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

$LIMELIGHT_LIB = File.expand_path(File.join(File.dirname(__FILE__), '..'))
$:.unshift $LIMELIGHT_LIB unless $:.include?($LIMELIGHT_LIB)
$LIMELIGHT_HOME = File.expand_path(File.join($LIMELIGHT_LIB, ".."))

if Java::java.lang.System.getProperty("limelight.home").nil?
  Java::java.lang.System.setProperty("limelight.home", $LIMELIGHT_HOME)
end

require 'limelight/java_couplings'
