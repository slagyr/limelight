require File.expand_path(File.dirname(__FILE__) + "/init")

Java::limelight::Main.initialize_context

if defined? LIMELIGHT_STARTUP_PRODUCTION
  production_name = LIMELIGHT_STARTUP_PRODUCTION
elsif ARGV[0]
  production_name = ARGV[0]
else
  production_name = File.expand_path(File.dirname(__FILE__) + "/../productions/startup")
end

puts "production_name: #{production_name}"

require 'limelight/producer'
Limelight::Producer.open(production_name)