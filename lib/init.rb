#- Copyright 2008 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

LIMELIGHT_LIB = File.expand_path(File.dirname(__FILE__))
$: << LIMELIGHT_LIB unless $:.include?(LIMELIGHT_LIB)

require 'java'
require File.expand_path(File.dirname(__FILE__) + "/limelight.jar")

# ENV["GEM_HOME"] = ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/../../jruby/lib/ruby/gems/1.8")
require 'rubygems'
require 'limelight/java_couplings'   
