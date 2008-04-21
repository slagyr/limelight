# $LIMELIGHT_LIB = File.expand_path(File.dirname(__FILE__))
# $LIMELIGHT_HOME = File.expand_path(File.join($LIMELIGHT_LIB, "..", ".."))
# $RUBY_LIB = File.join($LIMELIGHT_HOME, "jars")
# 
# $: << $RUBY_LIB
# $: << File.join($RUBY_LIB, "jars", "ruby", "site_ruby", "1.8")
# $: << File.join($RUBY_LIB, "jars", "ruby", "site_ruby", "1.8", "java")
# $: << File.join($RUBY_LIB, "jars", "ruby", "site_ruby")
# $: << File.join($RUBY_LIB, "jars", "ruby", "1.8")
# $: << File.join($RUBY_LIB, "jars", "ruby", "1.8", "java")
$: << File.expand_path(File.dirname(__FILE__))

require 'java'
require File.expand_path(File.dirname(__FILE__) + "/../../dist/limelight.jar")

ENV["GEM_HOME"] = File.expand_path(File.dirname(__FILE__) + "/../../etc/gems")
ENV["GEM_PATH"] = File.expand_path(File.dirname(__FILE__) + "/../../etc/gems")
require 'rubygems'