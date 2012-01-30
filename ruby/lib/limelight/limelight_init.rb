#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

$LIMELIGHT_LIB = File.expand_path(File.join(File.dirname(__FILE__), '..'))
$:.unshift $LIMELIGHT_LIB unless $:.include?($LIMELIGHT_LIB)
$LIMELIGHT_HOME = File.expand_path(File.join($LIMELIGHT_LIB, "..", ".."))

require 'java'
require 'limelight.jar'
require 'limelight-rb.jar'

context = Java::limelight.Context.instance
context.castingDirector = Java::limelight.model.CastingDirector.new
context.castingDirector.builtin_player_recruiter = Java::limelight.java.JavaPlayerRecruiter.new(JRuby.runtime.jruby_class_loader)

if Java::java.lang.System.getProperty("limelight.home").nil?
  Java::java.lang.System.setProperty("limelight.home", $LIMELIGHT_HOME)
end

require 'limelight/java_couplings'

def log
  Java::limelight.Log
end
