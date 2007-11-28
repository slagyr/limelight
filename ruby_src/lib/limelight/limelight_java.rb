require 'java'
require File.expand_path(File.dirname(__FILE__) + "/../../../dist/limelight.jar")

module Limelight
  JBlock = Java::limelight.Block
  JPage = Java::limelight.Page
  JBook = Java::limelight.Book
  Style = Java::limelight.Style
end