require File.expand_path(File.dirname(__FILE__) + "/../ruby_src/lib/init")
require 'limelight/producer'

puts "ARGV"
10.times do |i|
  puts "#{i}: #{ARGV[i]}"
end

puts "$LIMELIGHT_STARTUP_PRODUCTION: #{$LIMELIGHT_STARTUP_PRODUCTION}"

page_name = ARGV[0]
Limelight::Producer.open(page_name)