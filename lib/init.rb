$: << File.expand_path(File.dirname(__FILE__))

require 'java'
require File.expand_path(File.dirname(__FILE__) + "/limelight.jar")

ENV["GEM_HOME"] = File.expand_path(File.dirname(__FILE__) + "/../../etc/gems")
ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/../../etc/gems")
require 'rubygems'