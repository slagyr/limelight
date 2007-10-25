$: << File.expand_path(File.dirname(__FILE__) + "/../ruby_src/lib")
require 'book'

Book.new().load("sandbox/actions.llm")