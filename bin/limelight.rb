require File.expand_path(File.dirname(__FILE__) + "/../ruby_src/lib/init")
require 'limelight/producer'
# 
# #TODO - MDM - Is this the right place for this?
# java.lang.System.setProperty("apple.laf.useScreenMenuBar", "true");

page_name = ARGV[0]
Limelight::Producer.open(page_name)