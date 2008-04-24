require File.expand_path(File.dirname(__FILE__) + "/init")
require 'limelight/producer'

if defined? LIMELIGHT_STARTUP_PRODUCTION
  production_name = LIMELIGHT_STARTUP_PRODUCTION
else
  production_name = ARGV[0]
end

raise "No input" unless production_name
Limelight::Producer.open(production_name)