limelight_require_path = File.expand_path(File.dirname(__FILE__))
$: << limelight_require_path unless $:.include?(limelight_require_path)

require 'java'
require File.expand_path(File.dirname(__FILE__) + "/limelight.jar")

# ENV["GEM_HOME"] = ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/../../jruby/lib/ruby/gems/1.8")
require 'rubygems'
