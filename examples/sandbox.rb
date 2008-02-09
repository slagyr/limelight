puts "SANDBOX STARTUP"
require File.expand_path(File.dirname(__FILE__) + "/../ruby_src/lib/init")
require 'limelight/book'

Limelight::Book.new().load("sandbox/actions.llm")
# Limelight::Book.new().load("sandbox/colors.llm")
# Limelight::Book.new().load("sandbox/transparency.llm")
# Limelight::Book.new().load("sandbox/gradients.llm")
# Limelight::Book.new().load("sandbox/inputs.llm")